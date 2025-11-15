# Properties Service

Servicio REST desarrollado con Spring Boot que expone el catálogo de propiedades inmobiliarias de Roomiefy. Implementa arquitectura hexagonal para aislar la lógica de dominio del acceso a datos y expone filtros avanzados para búsquedas.

## Arquitectura
- **Capa de dominio**: `Property` (entidad JPA) más puertos `PropertyServicePort` y `PropertyRepositoryPort`.
- **Casos de uso**: `PropertyServiceImpl` implementa el puerto inbound y usa `PropertySpecification` para construir filtros dinámicos.
- **Adaptadores**:
  - Entrada: `PropertyController` (REST) mapea directamente al servicio.
  - Salida: `JpaPropertyRepository` conecta con Spring Data JPA.
- **Configuración**: `ApplicationConfig` arma las dependencias, `WebConfig` define CORS y `DataInitializer` carga datos semilla en perfil `dev`.

## Requisitos
- Java 17+
- Maven 3.9+
- Base de datos SQL Server accesible mediante las variables de entorno indicadas abajo.

## Configuración
El microservicio depende exclusivamente de variables de entorno (o propiedades del sistema) inyectadas en `src/main/resources/application.properties`:

| Variable | Descripción |
| --- | --- |
| `DB_URL` | Cadena JDBC completa hacia SQL Server, p. ej. `jdbc:sqlserver://localhost:1433;databaseName=roomiefy` |
| `DB_USERNAME` | Usuario de base de datos |
| `DB_PASSWORD` | Contraseña del usuario |
| `JPA_DDL_AUTO` | Estrategia de Hibernate para el esquema (`update`, `validate`, etc.). Por defecto `update`. |
| `JPA_SHOW_SQL` | `true/false` para mostrar SQL en los logs (por defecto `true`). |
| `SPRING_PROFILES_ACTIVE` | Perfil activo. `dev` carga datos ejemplo si la tabla está vacía. |

Otros datos relevantes:
- Puerto HTTP expuesto: `8081` (configurable con `server.port`).
- Dialecto: `org.hibernate.dialect.SQLServerDialect`.
- Perfil `test`: cargado automáticamente en las pruebas para aislar la configuración.

## Ejecución local
```bash
./mvnw spring-boot:run
```
El comando toma las variables anteriores del entorno. Para usar el perfil `dev` simplemente no modifiques `SPRING_PROFILES_ACTIVE` (valor por defecto). Este perfil ejecuta `DataInitializer`, que inserta tres propiedades de ejemplo cuando la tabla está vacía.

## Pruebas
```bash
./mvnw test
```
Esto ejecuta `PropertiesServiceApplicationTests`, que valida el arranque del contexto bajo el perfil `test`. Se pueden añadir pruebas unitarias/integración adicionales para la lógica de dominio y los endpoints.

## Endpoints
Todos los endpoints se publican desde la raíz `/`. La siguiente tabla resume el contrato actual:

| Método | Ruta | Descripción |
| --- | --- | --- |
| `POST` | `/` | Crea una nueva propiedad. Recibe un JSON con los campos definidos en `Property`. |
| `GET` | `/` | Lista propiedades con filtros opcionales `search`, `priceMax`, `bedrooms`, `amenities`. |
| `GET` | `/{id}` | Recupera una propiedad por su identificador numérico. |
| `GET` | `/my-properties/{ownerId}` | Lista propiedades pertenecientes a un dueño usando su `ownerId`. |
| `PUT` | `/{id}` | Actualiza los campos editables (nombre, precio, amenities, etc.) de una propiedad existente. |
| `DELETE` | `/{id}` | Elimina una propiedad. |

> **Nota sobre CORS:** `WebConfig` habilita CORS para `http://localhost:5173` sobre rutas `/api/**`. Si deseas consumir estos endpoints desde el front-end, alinea el `@RequestMapping` del controlador (actualmente `/`) con el patrón configurado o ajusta `WebConfig` para cubrir `/`.

## Tecnologías principales
- Spring Boot 3.5
- Spring Web y Spring Data JPA
- SQL Server (driver `mssql-jdbc`)
- Lombok para reducir código getters/setters

## Próximos pasos sugeridos
1. Añadir DTOs y validaciones (`jakarta.validation`) para controlar la entrada de datos y separar la entidad JPA del API público.
2. Incorporar autenticación/autorización para restringir operaciones por `ownerId`.
3. Agregar pruebas unitarias para `PropertyServiceImpl` y pruebas de integración para el controlador utilizando `MockMvc`.
