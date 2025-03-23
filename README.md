⚽ Football API

API REST en Spring Boot que consume datos de una API externa de fútbol y los expone a través de endpoints.

---

🚀 Tecnologías utilizadas

Java: 21

Spring Boot: 3.x

WebClient: Para hacer llamadas HTTP a la API externa

Prometheus/Grafana: Para métricas, monitorización y visualización de datos

---

🔧 Configuración

Variables de entorno

Para evitar exponer el token en el código, se configura como variable de entorno:

Ir a Run → Edit Configurations...

En Environment Variables, agregar:

API_TOKEN=Bearer tu_token_aqui

En application.properties, definir:

api.token=${API_TOKEN}

---

📌 Endpoints

### GET /competition/{competitionId}

Descripción: Obtiene información general de la competición

Parámetros:

competitionId (String): Id de la competición

Respuesta:

JSON... todo

#

### GET /competition/standings/{competitionId}

Descripción: Obtiene la clasificación actual de la competición

Parámetros:

competitionId (String): Id de la competición

Respuesta:

JSON... todo

#

### GET /player/{playerId}

Descripción: Obtiene información general de un jugador

Parámetros:

playerId (String): Id del jugador

Respuesta:

JSON... todo

#

### GET /team/{teamId}

Descripción: Obtiene información general de un equipo

Parámetros:

teamId (String): Id del equipo

Respuesta:

JSON... todo

#

### GET /match/{matchId}

#

### GET /match?dateTo={dateTo}&dateFrom={dateFrom}

#

### GET /match?dateTo={dateTo}

🏁 Ejecución

Para ejecutar la aplicación:

mvn spring-boot:run

📊 Integración con Grafana

*todo*