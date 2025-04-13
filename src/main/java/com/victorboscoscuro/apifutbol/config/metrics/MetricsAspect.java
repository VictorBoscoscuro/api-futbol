package com.victorboscoscuro.apifutbol.config.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
public class MetricsAspect {
    private final MeterRegistry meterRegistry;

    public MetricsAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("within(@org.springframework.web.bind.annotation.RestController *) || " +
            "within(@org.springframework.stereotype.Controller *)")
    public Object measureEndpoint(ProceedingJoinPoint joinPoint) throws Throwable {
        String httpMethod = getHttpMethod(joinPoint);

        String basePath = getBasePath(joinPoint);

        // Incrementa el contador de llamadas
        Counter.builder("endpoint.calls")
                .tags("method", httpMethod, "path", basePath)
                .register(meterRegistry)
                .increment();

        Timer.Sample sample = Timer.start(meterRegistry);
        Object result = joinPoint.proceed();

        if (result instanceof Mono<?> mono) {
            return mono.doFinally(signalType ->
                    sample.stop(Timer.builder("endpoint.latency")
                            .tags("method", httpMethod, "path", basePath)
                            .register(meterRegistry)));
        } else if (result instanceof Flux<?> flux) {
            return flux.doFinally(signalType ->
                    sample.stop(Timer.builder("endpoint.latency")
                            .tags("method", httpMethod, "path", basePath)
                            .register(meterRegistry)));
        } else {
            // fallback para controlador no reactivo
            sample.stop(Timer.builder("endpoint.latency")
                    .tags("method", httpMethod, "path", basePath)
                    .register(meterRegistry));
            return result;
        }
    }

    private String getHttpMethod(ProceedingJoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getTarget().getClass().getMethods())
                .filter(method -> method.getName().equals(joinPoint.getSignature().getName()))
                .findFirst()
                .map(method -> {
                    if (method.getAnnotation(GetMapping.class) != null) return "GET";
                    if (method.getAnnotation(PostMapping.class) != null) return "POST";
                    if (method.getAnnotation(PutMapping.class) != null) return "PUT";
                    if (method.getAnnotation(DeleteMapping.class) != null) return "DELETE";
                    return "UNKNOWN";
                })
                .orElse("UNKNOWN");
    }

    // Extrae la ruta base (ej: "/users" en lugar de "/users/{id}")
    private String getBasePath(ProceedingJoinPoint joinPoint) {
        Class<?> controllerClass = joinPoint.getTarget().getClass();
        String basePath = Optional.ofNullable(controllerClass.getAnnotation(RequestMapping.class))
                .map(RequestMapping::value)
                .map(paths -> paths.length > 0 ? paths[0] : "")
                .orElse("");

        String methodPath = Arrays.stream(joinPoint.getTarget().getClass().getMethods())
                .filter(method -> method.getName().equals(joinPoint.getSignature().getName()))
                .findFirst()
                .map(method -> {
                    if (method.getAnnotation(GetMapping.class) != null) {
                        return method.getAnnotation(GetMapping.class).value()[0];
                    } else if (method.getAnnotation(PostMapping.class) != null) {
                        return method.getAnnotation(PostMapping.class).value()[0];
                    } else if (method.getAnnotation(PutMapping.class) != null) {
                        return method.getAnnotation(PutMapping.class).value()[0];
                    } else if (method.getAnnotation(DeleteMapping.class) != null) {
                        return method.getAnnotation(DeleteMapping.class).value()[0];
                    }
                    return "";
                })
                .orElse("");

        return (basePath + methodPath).replaceAll("/\\{.*?\\}", ""); // Elimina {id}, {name}, etc.
    }
}
