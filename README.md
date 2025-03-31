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

### GET /competition

Descripción: Obtiene las competiciones disponibles para el usuario

#

### GET /competition/standings/{competitionId}

Descripción: Obtiene la clasificación actual de la competición

Parámetros:

competitionId (String): Id de la competición

#

### GET /competition/teams/{competitionId}

Descripción: Obtiene los equipos actuales de la competición

Parámetros:

competitionId (String): Id de la competición

#

### GET /competition/scorers/{competitionId}

Descripción: Obtiene la tabla de goleadores de la competición

Parámetros:

competitionId (String): Id de la competición

#

### GET /player/{playerId}

Descripción: Obtiene información general de un jugador

Parámetros:

playerId (String): Id del jugador


#

### GET /team/{teamId}

Descripción: Obtiene información general de un equipo

Parámetros:

teamId (String): Id del equipo

#

### GET /team?limit={limit}&offset={offset}

Descripción: Obtiene el listado de los equipos disponibles para el usuario

Parámetros:

limit (Integer): cantidad de equipos a obtener por petición
offset (Integer): posición de inicio de la petición

#

### GET /match/{matchId}

Descripción: Obtiene información general de un partido

Parámetros:

matchId (String): Id del partido

#

### GET /match?dateTo={dateTo}&dateFrom={dateFrom}

Descripción: Obtiene el listado de partidos disponibles entre las fechas especificadas

Parámetros:

dateFrom (ISO Date): fecha de inicio de la petición
dateTo (ISO Date): fecha de fin de la petición

#


🏁 Ejecución

Para ejecutar la aplicación:

mvn spring-boot:run

📊 Integración con Grafana

*todo*