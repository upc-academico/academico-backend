# Configuración para Deploy en Railway

## Resumen
Este documento describe las configuraciones realizadas para desplegar el proyecto **academico-backend** en Railway desde GitHub.

## Archivos Creados

### 1. `Procfile`
Define el comando de inicio para Railway:
```
web: java -Dserver.port=$PORT $JAVA_OPTS -jar target/jwt-demo-0.0.1-SNAPSHOT.jar
```
- Especifica que es un proceso web
- Usa la variable de entorno `$PORT` de Railway
- Ejecuta el JAR generado por Maven

### 2. `railway.json`
Configuración específica de Railway para el build y deploy:
```json
{
  "$schema": "https://railway.app/railway.schema.json",
  "build": {
    "builder": "NIXPACKS",
    "buildCommand": "./mvnw clean install -DskipTests"
  },
  "deploy": {
    "startCommand": "java -Dserver.port=$PORT $JAVA_OPTS -jar target/jwt-demo-0.0.1-SNAPSHOT.jar",
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 10
  }
}
```
- **Builder**: Nixpacks (detección automática del entorno Java/Maven)
- **Build Command**: Compila el proyecto usando Maven Wrapper sin ejecutar tests
- **Start Command**: Inicia la aplicación Spring Boot
- **Restart Policy**: Reinicia automáticamente en caso de fallo (máximo 10 intentos)

### 3. `system.properties`
Especifica la versión de Java Runtime:
```properties
java.runtime.version=17
```
- Asegura que Railway use Java 17 (compatible con Spring Boot 3.1.3)

## Archivos Modificados

### `src/main/resources/application.properties`
Se actualizó para usar variables de entorno con valores por defecto:

```properties
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost/dbSistemaBI}
spring.datasource.username=${DATABASE_USER:postgres}
spring.datasource.password=${DATABASE_PASSWORD:admin}
server.port=${PORT:8080}
jwt.secret=${JWT_SECRET:4t7w!z%C*F-JaNdRgUkXn2r5u8x/A?D(G+KbPeShVmYq3s6v9y$B&E)H@McQfTjW}
```

**Cambios realizados:**
- `DATABASE_URL`: URL de conexión a PostgreSQL
- `DATABASE_USER`: Usuario de la base de datos
- `DATABASE_PASSWORD`: Contraseña de la base de datos
- `PORT`: Puerto del servidor (Railway asigna dinámicamente)
- `JWT_SECRET`: Clave secreta para JWT (se debe configurar en Railway)

## Pasos para Desplegar en Railway

### 1. Conectar el Repositorio
1. Crear una cuenta en [Railway.app](https://railway.app)
2. Hacer clic en "New Project" → "Deploy from GitHub repo"
3. Autorizar Railway para acceder al repositorio
4. Seleccionar el repositorio `academico-backend`

### 2. Agregar Base de Datos PostgreSQL
1. En el proyecto de Railway, hacer clic en "+ New"
2. Seleccionar "Database" → "PostgreSQL"
3. Railway creará automáticamente la base de datos

### 3. Configurar Variables de Entorno
En la pestaña "Variables" del servicio, agregar:

| Variable | Valor | Descripción |
|----------|-------|-------------|
| `DATABASE_URL` | `${{Postgres.DATABASE_URL}}` | URL de PostgreSQL (automática) |
| `DATABASE_USER` | `${{Postgres.PGUSER}}` | Usuario de PostgreSQL (automática) |
| `DATABASE_PASSWORD` | `${{Postgres.PGPASSWORD}}` | Contraseña de PostgreSQL (automática) |
| `PORT` | Automático | Railway lo asigna automáticamente |
| `JWT_SECRET` | `[tu-clave-secreta-segura]` | Generar una clave segura nueva |
| `JAVA_OPTS` | `-Xmx512m -Xms256m` | (Opcional) Optimización de memoria |

**Nota:** Railway detecta automáticamente las variables de PostgreSQL cuando se vincula el servicio.

### 4. Deploy
1. Railway detectará automáticamente los archivos de configuración
2. Iniciará el build usando el comando en `railway.json`
3. Una vez completado, desplegará la aplicación
4. Railway proveerá una URL pública para acceder a la API

## Endpoints Disponibles
Una vez desplegado, la API estará disponible en:
- **Base URL**: `https://[tu-proyecto].railway.app`
- **Swagger UI**: `https://[tu-proyecto].railway.app/swagger-ui.html`

## Estructura Técnica del Proyecto
- **Framework**: Spring Boot 3.1.3
- **Java**: 17
- **Base de Datos**: PostgreSQL
- **Build Tool**: Maven
- **Seguridad**: Spring Security + JWT
- **Documentación**: OpenAPI/Swagger

## Recomendaciones de Seguridad

1. **JWT_SECRET**: Generar una nueva clave secreta fuerte en Railway:
   ```bash
   openssl rand -base64 64
   ```

2. **Credenciales de Base de Datos**: Railway las genera automáticamente de forma segura.

3. **Variables de Entorno**: Nunca commitear valores reales en el código, siempre usar variables de entorno.

4. **HTTPS**: Railway proporciona HTTPS automáticamente en todas las URLs públicas.

## Troubleshooting

### Build Fails
- Verificar que `mvnw` tenga permisos de ejecución
- Revisar logs de build en Railway Dashboard
- Asegurar que todas las dependencias en `pom.xml` sean correctas

### Application Crashes
- Revisar logs de deploy en Railway
- Verificar que las variables de entorno estén correctamente configuradas
- Confirmar que la base de datos PostgreSQL esté running

### Connection Issues
- Verificar que `DATABASE_URL` apunte correctamente a la instancia de PostgreSQL
- Confirmar que el puerto esté configurado correctamente (`$PORT`)

## Monitoreo
Railway proporciona:
- Logs en tiempo real
- Métricas de CPU y memoria
- Estado del deployment
- Historial de builds

---
**Fecha de Configuración**: Noviembre 2025  
**Versión del Proyecto**: 0.0.1-SNAPSHOT
