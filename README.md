# TiendaYa API REST (PARCIAL SEGUNDO CORTE ELECTIVA CPI)

API REST para la gestión de productos y pedidos, desarrollada con Java 17, Spring Boot y PostgreSQL.

## 1. Tecnologías y requisitos

- Java 17
- Spring Boot 3.5.5
- Maven Wrapper
- PostgreSQL 17
- Spring Data JPA
- Lombok
- Postman para pruebas de endpoints

## 2. Configuración de la base de datos

Crear una base de datos PostgreSQL llamada `tiendaya` y configurar las credenciales mediante variables de entorno.

Variables requeridas:

- `DB_URL`: URL JDBC de PostgreSQL.
- `DB_USERNAME`: usuario de la base de datos.
- `DB_PASSWORD`: contraseña del usuario.

La configuración de conexión se encuentra en `src/main/resources/application.properties`. Este archivo se excluye de Git para evitar publicar credenciales.

## 3. Ejecutar el proyecto

Desde PowerShell, ubicado en la carpeta raíz:

```powershell
.\mvnw.cmd clean compile
```

Para ejecutar las pruebas:

```powershell
.\mvnw.cmd test
```

Para iniciar la aplicación:

```powershell
.\mvnw.cmd spring-boot:run
```

También se puede ejecutar desde IntelliJ IDEA mediante la configuración de `DemoApplication`.

La API utiliza el puerto `8001` y está disponible localmente en:

`http://localhost:8001`

## 4. Endpoints de productos

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/productos` | Listar productos |
| GET | `/api/productos/{id}` | Consultar un producto |
| POST | `/api/productos` | Crear un producto |
| PUT | `/api/productos/{id}` | Actualizar un producto |
| DELETE | `/api/productos/{id}` | Eliminar un producto |

Para crear un producto, enviar los parámetros `name` y `price`, según la implementación del controlador.

## 5. Endpoints de pedidos

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/pedidos` | Crear un pedido |
| GET | `/api/pedidos` | Listar pedidos |
| GET | `/api/pedidos/{id}` | Consultar un pedido |
| PUT | `/api/pedidos/{id}` | Actualizar un pedido |
| DELETE | `/api/pedidos/{id}` | Eliminar un pedido |
| POST | `/api/pedidos/{id}/items` | Agregar un producto al pedido |
| DELETE | `/api/pedidos/{id}/items/{itemId}` | Retirar un producto del pedido |
| PATCH | `/api/pedidos/{id}/confirmar` | Confirmar el pedido |

Los endpoints de pedidos deben probarse una vez implementados.

## 6. Pruebas con Postman

La colección de pruebas se encuentra en `TiendaYa_API.postman_collection.json`.

Las pruebas deben verificar las respuestas de la API, los códigos HTTP, las operaciones CRUD y las reglas de negocio de pedidos e inventario.

## 7. Estructura del proyecto

El código fuente se encuentra en `src/main/java/com/example/demo/`, organizado en paquetes para controladores, servicios, repositorios y modelos.

La aplicación utiliza PostgreSQL como base de datos y Spring Data JPA para la persistencia.

## 8. Seguridad

Las credenciales de la base de datos no deben almacenarse en el repositorio. Configurar `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` en el entorno local antes de ejecutar la aplicación.
