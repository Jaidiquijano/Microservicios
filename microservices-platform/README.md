# Lumara - Plataforma de Microservicios

Plataforma digital del centro formada por una **app móvil** (Android) y una **plataforma web**,
basada en arquitectura de microservicios con Spring Boot 3.4 + Spring Cloud 2024.

---

## Servicios y puertos

| Servicio               | Puerto | Base de datos     |
|------------------------|--------|-------------------|
| gateway-service        | 8080   | —                 |
| iam-service            | 8082   | iam_db (5432)     |
| users-service          | 8081   | users_db (5433)   |
| agenda-service         | 8083   | agenda_db (5434)  |
| notifications-service  | 8084   | notifications_db (5435) |
| tramites-service       | 8085   | tramites_db (5436)|

---

## Requisitos previos

- Java 21
- Maven 3.9+
- Docker Desktop

---

## Arranque completo paso a paso

### 1. Levantar todas las bases de datos
```bash
docker compose up -d
```

### 2. Compilar el proyecto completo desde la raíz
```bash
mvn clean install -DskipTests
```

### 3. Arrancar cada servicio en terminales separadas

Terminal 1 — IAM (primero siempre):
```bash
cd iam-service
mvn spring-boot:run
```

Terminal 2 — Users:
```bash
cd users-service
mvn spring-boot:run
```

Terminal 3 — Agenda:
```bash
cd agenda-service
mvn spring-boot:run
```

Terminal 4 — Notifications:
```bash
cd notifications-service
mvn spring-boot:run
```

Terminal 5 — Tramites:
```bash
cd tramites-service
mvn spring-boot:run
```

Terminal 6 — Gateway (último siempre):
```bash
cd gateway-service
mvn spring-boot:run
```

---

## Probar el sistema completo con Postman

### 1. Registrar usuario
**POST** `http://localhost:8080/api/auth/register`
```json
{
  "fullName": "Juan García",
  "email": "juan@lumara.es",
  "password": "123456",
  "role": "ALUMNO"
}
```
Roles disponibles: `ALUMNO`, `PROFESOR`, `JEFATURA`, `SECRETARIA`, `DIRECCION`, `ADMIN`

### 2. Login — obtener token
**POST** `http://localhost:8080/api/auth/login`
```json
{
  "email": "juan@lumara.es",
  "password": "123456"
}
```
Guarda el `token` de la respuesta.

### 3. Usar el token en las siguientes peticiones
En todas las peticiones protegidas añade el header:
```
Authorization: Bearer <token>
```

### 4. Crear evento en agenda
**POST** `http://localhost:8080/api/agenda`
- Header: `Authorization: Bearer <token>` (usuario con rol PROFESOR)
```json
{
  "title": "Examen de DAM",
  "description": "Examen parcial de programación",
  "type": "EXAMEN",
  "startDate": "2025-06-15T09:00:00",
  "targetGroup": "DAM1",
  "targetRole": "ALUMNO"
}
```

### 5. Ver próximos eventos
**GET** `http://localhost:8080/api/agenda/upcoming`
- Header: `Authorization: Bearer <token>`

### 6. Crear notificación
**POST** `http://localhost:8080/api/notifications`
- Header: `Authorization: Bearer <token>` (usuario con rol PROFESOR o superior)
```json
{
  "title": "Cambio de horario",
  "message": "La clase del lunes se traslada al martes a las 10h",
  "type": "CAMBIO_HORARIO",
  "targetRole": "ALUMNO"
}
```

### 7. Ver mis notificaciones
**GET** `http://localhost:8080/api/notifications/my`
- Header: `Authorization: Bearer <token>`

### 8. Crear trámite
**POST** `http://localhost:8080/api/tramites`
- Header: `Authorization: Bearer <token>`
```json
{
  "type": "JUSTIFICANTE",
  "descripcion": "Justificante médico del día 10 de abril"
}
```

### 9. Ver mis trámites
**GET** `http://localhost:8080/api/tramites/my`
- Header: `Authorization: Bearer <token>`

### 10. Actualizar estado de trámite (secretaría)
**PUT** `http://localhost:8080/api/tramites/1/status`
- Header: `Authorization: Bearer <token>` (usuario con rol SECRETARIA)
```json
{
  "status": "APROBADO"
}
```

---

## Roles y permisos

| Acción                        | Roles permitidos                        |
|-------------------------------|-----------------------------------------|
| Registrarse / Login           | Todos                                   |
| Ver agenda                    | Todos                                   |
| Crear eventos                 | PROFESOR, JEFATURA, DIRECCION, ADMIN    |
| Eliminar eventos              | JEFATURA, DIRECCION, ADMIN              |
| Ver notificaciones propias    | Todos                                   |
| Crear notificaciones          | PROFESOR, JEFATURA, SECRETARIA, ADMIN   |
| Crear trámites                | Todos                                   |
| Ver todos los trámites        | SECRETARIA, JEFATURA, DIRECCION, ADMIN  |
| Actualizar estado de trámite  | SECRETARIA, JEFATURA, DIRECCION, ADMIN  |

---

## Estructura del proyecto

```
microservices-platform/
├── pom.xml                  ← POM padre
├── docker-compose.yml       ← Bases de datos
├── common-lib/              ← Clases compartidas
├── gateway-service/         ← Punto de entrada único (puerto 8080)
├── iam-service/             ← Autenticación JWT (puerto 8082)
├── users-service/           ← Perfiles de usuario (puerto 8081)
├── agenda-service/          ← Eventos y calendario (puerto 8083)
├── notifications-service/   ← Avisos y recordatorios (puerto 8084)
└── tramites-service/        ← Becas, justificantes, certificados (puerto 8085)
```