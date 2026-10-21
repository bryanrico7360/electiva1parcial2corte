# Parcial segundo corte — Electiva CPI

**By: Bryan Steven Ramirez Rico**

## 1. Descripción del proyecto

El proyecto consiste en el desarrollo de una API REST con Spring Boot para la gestión de productos y pedidos de una tienda. Su finalidad es implementar las operaciones CRUD del módulo de Productos y desarrollar un módulo de Pedidos que funcione como un carrito de compras.

La aplicación permite registrar y consultar productos, actualizar sus datos, administrar pedidos, agregar o retirar productos del carrito, calcular subtotales y totales automáticamente y descontar las existencias del inventario al confirmar una compra.

La persistencia de los datos se realiza mediante PostgreSQL, mientras que Spring Data JPA e Hibernate permiten gestionar las entidades y sus relaciones. Además, el proyecto utiliza Lombok para reducir el código repetitivo y contempla el manejo de errores y la validación de las reglas de negocio.

## 2. Tecnologías utilizadas

| Tecnología | Uso en el proyecto |
|---|---|
| Java 17 | Lenguaje de programación. |
| Spring Boot 3.5.5 | Plataforma para desarrollar y ejecutar la API REST. |
| Spring Web | Creación de los controladores y endpoints HTTP. |
| Spring Data JPA | Acceso y persistencia de los datos. |
| Hibernate | Mapeo objeto-relacional y generación o actualización del esquema de la base de datos. |
| PostgreSQL | Sistema gestor de bases de datos relacional. |
| Maven | Gestión de dependencias, compilación y ejecución. |
| Maven Wrapper | Ejecución de Maven mediante los archivos incluidos en el proyecto. |
| Lombok | Reducción de código repetitivo mediante anotaciones. |
| Jakarta Validation | Validación de datos cuando está configurada en los componentes correspondientes. |
| Postman | Pruebas funcionales de los endpoints. |
| IntelliJ IDEA | Entorno de desarrollo utilizado. |

## 3. Requisitos previos

Antes de ejecutar el proyecto, se requiere:

- JDK 17.
- PostgreSQL 15 o superior, de acuerdo con la guía del parcial.
- Una base de datos PostgreSQL creada para el proyecto.
- IntelliJ IDEA o un entorno compatible con proyectos Maven y Lombok.
- Postman para probar los endpoints.
- El código fuente completo del proyecto, incluido el archivo `pom.xml` y el Maven Wrapper.

No es indispensable tener Maven instalado globalmente en Windows, ya que se puede utilizar el archivo `mvnw.cmd` incluido en el proyecto.

## 4. Configuración de PostgreSQL

### 4.1. Crear la base de datos

Inicia el servicio de PostgreSQL y abre pgAdmin, DBeaver o una consola `psql`.

Crea la base de datos mediante la siguiente sentencia SQL:

```sql
CREATE DATABASE tienda_db;
```

Este paso crea la base de datos vacía. Las tablas utilizadas por la aplicación se generan o actualizan posteriormente cuando Spring Boot inicia y Hibernate establece la conexión con PostgreSQL.

Si se utiliza una base de datos existente con otro nombre, no es necesario crear otra: se debe configurar la aplicación para conectarse a la base de datos elegida.

### 4.2. Configurar la conexión

Abre el archivo:

`src/main/resources/application.properties`

Configura los datos de conexión a PostgreSQL:

```properties
spring.application.name=api
server.port=8001

# Configuracion de PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/tienda_db
spring.datasource.username=USUARIO_POSTGRES
spring.datasource.password=CONTRASENA_POSTGRES
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuracion de JPA e Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Configuracion de errores
server.error.include-stacktrace=never
server.error.include-message=always
```

Los valores `USUARIO_POSTGRES` y `CONTRASENA_POSTGRES` son marcadores de posición. Deben reemplazarse por el usuario y la contraseña reales de PostgreSQL.

| Propiedad | Descripción |
|---|---|
| `spring.application.name` | Nombre asignado a la aplicación Spring Boot. |
| `server.port` | Puerto en el que se ejecuta la API; en esta configuración es `8001`. |
| `spring.datasource.url` | Dirección del servidor, puerto y nombre de la base de datos. Si se utiliza una base de datos distinta de `tienda_db`, se debe cambiar esta URL. |
| `spring.datasource.username` | Usuario de PostgreSQL. |
| `spring.datasource.password` | Contraseña del usuario de PostgreSQL. |
| `spring.datasource.driver-class-name` | Controlador JDBC de PostgreSQL. |
| `spring.jpa.hibernate.ddl-auto=update` | Permite que Hibernate cree o actualice el esquema según las entidades mapeadas. |
| `spring.jpa.show-sql=true` | Muestra en la consola las sentencias SQL generadas por Hibernate. |
| `spring.jpa.properties.hibernate.format_sql=true` | Presenta las sentencias SQL con formato legible. |
| `server.error.include-stacktrace=never` | Evita incluir la traza de la excepción en las respuestas de error. |
| `server.error.include-message=always` | Permite incluir el mensaje de error en las respuestas cuando corresponda. |

**Importante:** no publiques contraseñas reales en el repositorio ni las incluyas en capturas de pantalla.

### 4.3. Creación de las tablas al ejecutar la aplicación

La configuración `spring.jpa.hibernate.ddl-auto=update` permite que Hibernate gestione el esquema de la base de datos a partir de las entidades JPA.

El proceso correcto es:

1. Crear la base de datos en PostgreSQL.
2. Configurar las credenciales y la URL en `application.properties`.
3. Ejecutar la aplicación Spring Boot.
4. Esperar a que la aplicación se conecte a PostgreSQL e inicialice el contexto de persistencia.
5. Abrir pgAdmin y actualizar la vista de la base de datos para verificar las tablas generadas.

Las tablas principales son:

| Tabla | Descripción |
|---|---|
| `productos` | Almacena los productos y sus datos. |
| `pedidos` | Almacena los pedidos, sus estados y sus totales. |
| `detalle_pedidos` | Registra los productos asociados a cada pedido, las cantidades, los precios unitarios y los subtotales. |

No es necesario ejecutar sentencias `CREATE TABLE` manuales cuando las entidades están correctamente mapeadas y Hibernate puede conectarse a la base de datos.

**Diferencia importante:** sincronizar Maven permite reconocer y descargar dependencias; compilar verifica el código; ejecutar Spring Boot es el paso en el que la aplicación inicializa JPA y Hibernate puede crear o actualizar las tablas. La compilación por sí sola no garantiza que las tablas se creen.

## 5. Apertura y sincronización de Maven

### 5.1. Abrir el proyecto

1. Abre IntelliJ IDEA.
2. Selecciona **Open**.
3. Elige la carpeta raíz del proyecto, donde se encuentra el archivo `pom.xml`.
4. Espera a que IntelliJ termine de cargar los archivos.

### 5.2. Sincronizar las dependencias

1. Si IntelliJ muestra una notificación para importar o cargar el proyecto Maven, selecciónala.
2. Abre el panel **Maven**.
3. Selecciona **Reload All Maven Projects**.
4. Espera a que finalice la sincronización y se descarguen las dependencias necesarias.

Si IntelliJ no reconoce Maven, verifica que el archivo `pom.xml` exista en la raíz del proyecto.

La sincronización debe realizarse cuando se abre el proyecto por primera vez o cuando se modifican sus dependencias. No es necesario repetirla si el proyecto ya está correctamente sincronizado.

## 6. Compilación y ejecución del proyecto

### 6.1. Compilar el proyecto

Abre PowerShell o la terminal de IntelliJ en la carpeta raíz del proyecto y ejecuta:

```powershell
.\mvnw.cmd clean install
```

Este comando elimina los archivos de compilaciones anteriores, compila el proyecto, ejecuta las pruebas configuradas y empaqueta la aplicación si el proceso finaliza correctamente.

Si las pruebas utilizan PostgreSQL, verifica que la configuración de conexión sea válida.

### 6.2. Ejecutar la aplicación

Para iniciar Spring Boot desde la terminal, ejecuta:

```powershell
.\mvnw.cmd spring-boot:run
```

También se puede ejecutar la clase principal de Spring Boot desde IntelliJ IDEA mediante el botón **Run**.

Durante el arranque, revisa la consola para confirmar que:

- La aplicación no presenta errores de configuración.
- La conexión con PostgreSQL se establece correctamente.
- Hibernate inicializa el esquema de persistencia.
- El servidor web se inicia en el puerto configurado.

Una vez iniciada la aplicación, la dirección base de la API es:

`http://localhost:8001`

Si PostgreSQL no está disponible o las credenciales son incorrectas, la aplicación puede fallar durante la inicialización.

## 7. Modelo de datos

El proyecto utiliza tres entidades principales: `Producto`, `Pedido` y `DetallePedido`.

### 7.1. Tabla `productos`

Almacena la información de los productos disponibles en la tienda.

| Campo | Descripción |
|---|---|
| `id` | Identificador único del producto, generado por la base de datos. |
| `nombre` | Nombre del producto. |
| `descripcion` | Descripción o características del producto. |
| `precio` | Precio unitario del producto. |
| `stock` | Cantidad de unidades disponibles en el inventario. |

Ejemplo de un producto:

```json
{
  "id": 1,
  "nombre": "Teclado mecanico",
  "descripcion": "Teclado con switches rojos y retroiluminacion",
  "precio": 189900.00,
  "stock": 15
}
```

### 7.2. Tabla `pedidos`

Representa las compras realizadas por los clientes.

| Campo | Descripción |
|---|---|
| `id` | Identificador único del pedido. |
| `cliente` | Nombre del cliente asociado al pedido. |
| `fecha` | Fecha y hora de creación del pedido. |
| `estado` | Estado actual del pedido. |
| `total` | Suma de los subtotales de los ítems del pedido. |

Los pedidos utilizan tres estados:

| Estado | Significado |
|---|---|
| `CARRITO` | El pedido se encuentra abierto y puede modificarse. |
| `CONFIRMADO` | La compra fue confirmada y se descontó el inventario correspondiente. |
| `CANCELADO` | Estado destinado a representar un pedido cancelado. |

El estado inicial definido en la guía es `CARRITO`.

**Nota:** la existencia del estado `CANCELADO` en el modelo no implica que haya un endpoint para cancelar pedidos; la guía del parcial no incluye una operación específica para esa transición.

### 7.3. Tabla `detalle_pedidos`

Almacena los ítems asociados a cada pedido.

| Campo | Descripción |
|---|---|
| `id` | Identificador único del detalle. |
| `pedido_id` | Referencia al pedido al que pertenece el ítem. |
| `producto_id` | Referencia al producto agregado. |
| `cantidad` | Número de unidades solicitadas. |
| `precio_unitario` | Precio del producto utilizado para calcular el ítem. |
| `subtotal` | Resultado de multiplicar el precio unitario por la cantidad. |

### 7.4. Relaciones entre las entidades

- Un pedido puede contener varios detalles.
- Cada detalle pertenece a un pedido.
- Cada detalle está asociado a un producto.
- Un producto puede aparecer en distintos detalles de pedidos.
- Los detalles se eliminan en cascada cuando se elimina el pedido, de acuerdo con la relación configurada en la guía.

Las relaciones se implementan mediante JPA y se representan en PostgreSQL mediante claves foráneas.

## 8. Reglas de negocio

El módulo de pedidos debe cumplir las siguientes reglas:

1. **Cálculo del subtotal:** se obtiene multiplicando la cantidad del ítem por su precio unitario.
2. **Cálculo del total:** corresponde a la suma de los subtotales de todos los ítems del pedido.
3. **Cálculo en el servidor:** el cliente no debe enviar los subtotales ni el total como valores confiables; la aplicación debe calcularlos a partir de los productos y las cantidades.
4. **Cantidad válida:** la cantidad de cada ítem debe ser mayor que cero.
5. **Estado editable:** únicamente los pedidos en estado `CARRITO` pueden actualizarse, agregar ítems, retirar ítems o confirmarse.
6. **Validación del inventario:** antes de confirmar, la aplicación verifica que exista stock suficiente para cada producto del pedido.
7. **Descuento de inventario:** al confirmar una compra, se descuenta del stock la cantidad solicitada de cada producto.
8. **Transacción:** si algún producto no tiene existencias suficientes, la operación de confirmación debe revertirse por completo, sin dejar descuentos parciales.
9. **Integridad referencial:** no se permite eliminar un producto que esté asociado a uno o más pedidos; la API debe responder con `409 Conflict`.
10. **Manejo de recursos inexistentes:** las consultas o modificaciones de recursos inexistentes deben devolver el código de error correspondiente, generalmente `404 Not Found`.

## 9. Endpoints de la API

La API utiliza el prefijo:

`http://localhost:8001/api`

La guía del parcial establece cinco endpoints para Productos y ocho para Pedidos, para un total de trece endpoints.

### 9.1. Endpoints de Productos

| Método | Ruta | Descripción | Respuesta esperada |
|---|---|---|---|
| `GET` | `/api/productos` | Lista todos los productos. | `200 OK` |
| `GET` | `/api/productos/{id}` | Consulta un producto por identificador. | `200 OK` o `404 Not Found` |
| `POST` | `/api/productos` | Crea un producto. | `201 Created` |
| `PUT` | `/api/productos/{id}` | Actualiza un producto existente. | `200 OK` o `404 Not Found` |
| `DELETE` | `/api/productos/{id}` | Elimina un producto cuando es posible. | `204 No Content`, `404 Not Found` o `409 Conflict` |

#### Crear un producto

**Método:** `POST`

**URL:** `http://localhost:8001/api/productos`

**Body → raw → JSON:**

```json
{
  "nombre": "Teclado mecanico",
  "descripcion": "Switches rojos y retroiluminacion",
  "precio": 189900.00,
  "stock": 15
}
```

La respuesta esperada es `201 Created`, con el producto creado y su identificador asignado.

#### Listar productos

**Método:** `GET`

**URL:** `http://localhost:8001/api/productos`

Devuelve un arreglo con los productos registrados, incluidos los campos de identificación, nombre, descripción, precio y stock.

#### Consultar un producto

**Método:** `GET`

**URL:** `http://localhost:8001/api/productos/1`

Si el producto existe, devuelve `200 OK` con sus datos. Si no existe, devuelve `404 Not Found`.

#### Actualizar un producto

**Método:** `PUT`

**URL:** `http://localhost:8001/api/productos/1`

**Body → raw → JSON:**

```json
{
  "nombre": "Teclado mecanico RGB",
  "descripcion": "Teclado mecanico con iluminacion RGB",
  "precio": 199900.00,
  "stock": 20
}
```

La respuesta esperada es `200 OK` con los datos actualizados, o `404 Not Found` si el producto no existe.

#### Eliminar un producto

**Método:** `DELETE`

**URL:** `http://localhost:8001/api/productos/1`

La respuesta esperada es `204 No Content` si se elimina correctamente, `404 Not Found` si no existe o `409 Conflict` si el producto está asociado a un pedido.

### 9.2. Endpoints de Pedidos

| Método | Ruta | Descripción | Respuesta esperada |
|---|---|---|---|
| `GET` | `/api/pedidos` | Lista los pedidos con sus detalles. | `200 OK` |
| `GET` | `/api/pedidos/{id}` | Consulta un pedido específico con sus ítems. | `200 OK` o `404 Not Found` |
| `POST` | `/api/pedidos` | Crea un pedido con sus ítems. | `201 Created` o `400 Bad Request` |
| `PUT` | `/api/pedidos/{id}` | Actualiza el cliente y reemplaza los ítems de un carrito. | `200 OK`, `400 Bad Request` o `404 Not Found` |
| `DELETE` | `/api/pedidos/{id}` | Elimina un pedido. | `204 No Content` o `404 Not Found` |
| `POST` | `/api/pedidos/{id}/items` | Agrega un producto al carrito. | `200 OK`, `400 Bad Request` o `404 Not Found` |
| `DELETE` | `/api/pedidos/{id}/items/{itemId}` | Retira un ítem del carrito. | `200 OK` o `404 Not Found` |
| `PATCH` | `/api/pedidos/{id}/confirmar` | Confirma la compra y descuenta el inventario. | `200 OK` o `400 Bad Request` |

#### Crear un pedido con sus ítems

**Método:** `POST`

**URL:** `http://localhost:8001/api/pedidos`

**Body → raw → JSON:**

```json
{
  "cliente": "Bryan Steven Ramirez Rico",
  "items": [
    {
      "productoId": 1,
      "cantidad": 2
    },
    {
      "productoId": 3,
      "cantidad": 1
    }
  ]
}
```

El servidor busca los productos por su identificador, obtiene los precios vigentes y calcula los subtotales y el total.

La respuesta esperada es `201 Created`, con el pedido creado, sus ítems, el total calculado y el estado inicial `CARRITO`.

#### Listar pedidos

**Método:** `GET`

**URL:** `http://localhost:8001/api/pedidos`

Devuelve los pedidos registrados con sus detalles y totales.

#### Consultar un pedido

**Método:** `GET`

**URL:** `http://localhost:8001/api/pedidos/1`

Si existe, devuelve el pedido con su cliente, fecha, estado, ítems, subtotales y total. Si no existe, devuelve `404 Not Found`.

#### Actualizar un carrito

**Método:** `PUT`

**URL:** `http://localhost:8001/api/pedidos/1`

**Body → raw → JSON:**

```json
{
  "cliente": "Bryan Steven Ramirez Rico",
  "items": [
    {
      "productoId": 1,
      "cantidad": 2
    },
    {
      "productoId": 2,
      "cantidad": 1
    }
  ]
}
```

Esta operación actualiza el cliente y reemplaza los ítems del pedido, recalculando los subtotales y el total. Solo puede realizarse si el pedido está en estado `CARRITO`.

#### Eliminar un pedido

**Método:** `DELETE`

**URL:** `http://localhost:8001/api/pedidos/1`

La respuesta esperada es `204 No Content` cuando el pedido se elimina correctamente. Los detalles asociados se eliminan según la relación configurada.

#### Agregar un producto al carrito

**Método:** `POST`

**URL:** `http://localhost:8001/api/pedidos/1/items`

**Body → raw → JSON:**

```json
{
  "productoId": 2,
  "cantidad": 4
}
```

La aplicación obtiene el producto, calcula el subtotal, agrega el ítem al pedido y recalcula el total. La respuesta esperada es `200 OK` si la operación se realiza correctamente.

#### Retirar un producto del carrito

**Método:** `DELETE`

**URL:** `http://localhost:8001/api/pedidos/1/items/1`

Elimina el ítem indicado, siempre que pertenezca al pedido y que este se encuentre en estado `CARRITO`. La aplicación recalcula el total y devuelve el pedido actualizado con `200 OK`.

#### Confirmar una compra

**Método:** `PATCH`

**URL:** `http://localhost:8001/api/pedidos/1/confirmar`

No requiere un cuerpo JSON. La aplicación valida el inventario de todos los ítems y, si hay existencias suficientes, confirma el pedido y descuenta las unidades correspondientes.

La respuesta esperada es `200 OK` con el pedido confirmado. Si no hay inventario suficiente o el pedido no puede confirmarse, la operación devuelve un error de solicitud y no debe dejar descuentos parciales.

## 10. Manejo de errores HTTP

Los principales códigos de respuesta utilizados en la API son:

| Código | Significado | Situación |
|---|---|---|
| `200 OK` | Operación realizada correctamente. | Consultar, actualizar o modificar un pedido. |
| `201 Created` | Recurso creado correctamente. | Crear un producto o pedido. |
| `204 No Content` | Operación exitosa sin cuerpo de respuesta. | Eliminar un producto o pedido. |
| `400 Bad Request` | Solicitud inválida o regla de negocio incumplida. | Cantidad inválida, stock insuficiente o intento de modificar un pedido no editable. |
| `404 Not Found` | Recurso inexistente. | Producto, pedido o ítem no encontrado. |
| `409 Conflict` | Conflicto con las restricciones de integridad. | Intentar eliminar un producto que está asociado a pedidos. |

El manejo de excepciones permite devolver respuestas controladas en lugar de exponer trazas internas de la aplicación.

## 11. Pruebas funcionales con Postman

Las pruebas deben comprobar que los endpoints respondan con los códigos HTTP esperados y que los cambios se reflejen en PostgreSQL.

Se recomienda organizar las solicitudes de Postman en dos carpetas: **Productos** y **Pedidos**.

### 11.1. Pruebas de Productos

| Código | Caso de prueba | Resultado esperado |
|---|---|---|
| P1 | Crear un producto válido. | `201 Created` y producto con ID. |
| P2 | Crear dos productos adicionales. | `201 Created` en cada solicitud. |
| P3 | Listar productos. | `200 OK` y arreglo con los productos registrados. |
| P4 | Consultar un producto existente. | `200 OK` y datos del producto. |
| P5 | Consultar un producto inexistente. | `404 Not Found`. |
| P6 | Actualizar un producto existente. | `200 OK` y datos actualizados. |
| P7 | Actualizar un producto inexistente. | `404 Not Found`. |
| P8 | Eliminar un producto sin pedidos asociados. | `204 No Content`. |
| P9 | Eliminar un producto inexistente. | `404 Not Found`. |
| P10 | Verificar los registros en PostgreSQL. | Los datos coinciden con los resultados de la API. |

### 11.2. Pruebas de Pedidos

| Código | Caso de prueba | Resultado esperado |
|---|---|---|
| C1 | Crear un pedido con dos ítems. | `201 Created`, estado `CARRITO` y total correcto. |
| C2 | Listar los pedidos. | `200 OK` y pedidos con sus detalles. |
| C3 | Consultar un pedido existente. | `200 OK`, ítems, subtotales y total. |
| C4 | Consultar un pedido inexistente. | `404 Not Found`. |
| C5 | Agregar un producto al carrito. | `200 OK` y total actualizado. |
| C6 | Retirar un producto del carrito. | `200 OK` y total actualizado. |
| C7 | Agregar un ítem con cantidad cero o negativa. | `400 Bad Request`. |
| C8 | Agregar un producto inexistente. | `404 Not Found`. |
| C9 | Actualizar cliente e ítems del carrito. | `200 OK` y detalles reemplazados. |
| C10 | Confirmar una compra con stock suficiente. | `200 OK`, estado `CONFIRMADO` e inventario descontado. |
| C11 | Intentar modificar un pedido confirmado. | Solicitud rechazada; en la guía se espera `400 Bad Request`. |
| C12 | Confirmar una compra sin stock suficiente. | `400 Bad Request` y ningún descuento parcial. |
| C13 | Eliminar un producto incluido en un pedido. | `409 Conflict`. |
| C14 | Eliminar un pedido. | `204 No Content` y detalles eliminados en cascada. |
| C15 | Verificar los pedidos y detalles en PostgreSQL. | Registros y totales coinciden con la API. |

### 11.3. Evidencias

De acuerdo con la guía del parcial, se exige un mínimo de 25 evidencias.

Cada captura de Postman debe permitir identificar:

- Método HTTP.
- URL solicitada.
- Cuerpo enviado, cuando corresponda.
- Código de estado HTTP.
- Respuesta obtenida.

Las verificaciones en PostgreSQL deben documentarse con capturas de pgAdmin o de la consola `psql`.

La evidencia del arranque de la aplicación debe mostrar la fecha y hora del sistema. Además, el nombre del aprendiz debe aparecer en al menos un registro creado, por ejemplo, en el campo `cliente` de un pedido.

La colección de Postman debe exportarse en formato JSON.

## 12. Documentación del código y arquitectura

El proyecto organiza la lógica de la API mediante componentes de Spring Boot y entidades JPA.

La arquitectura de referencia definida en la guía separa las responsabilidades en las siguientes capas:

| Capa | Responsabilidad |
|---|---|
| `controller` | Recibir las solicitudes HTTP y devolver las respuestas. |
| `service` | Implementar las reglas de negocio y coordinar las operaciones. |
| `repository` | Consultar, guardar, actualizar y eliminar información mediante JPA. |
| `model` | Definir las entidades y los estados del dominio. |
| `dto` | Representar los datos recibidos en solicitudes específicas. |
| `exception` | Gestionar excepciones y respuestas de error. |

La organización concreta de paquetes puede variar según la estructura del proyecto, pero las responsabilidades deben mantenerse separadas.

### 12.1. Uso de Lombok

La guía exige aplicar Lombok al 100 % de las clases del proyecto, eliminando constructores, getters, setters y otros métodos repetitivos escritos manualmente.

Entre las anotaciones utilizadas como referencia se encuentran:

- `@Getter` y `@Setter` para métodos de acceso.
- `@NoArgsConstructor` y `@AllArgsConstructor` para constructores.
- `@Builder` y `@Builder.Default` para la construcción de objetos y sus valores iniciales.
- `@Data` en DTO cuando corresponda.
- `@RequiredArgsConstructor` para la inyección de dependencias por constructor.
- `@Slf4j` para disponer de un logger.

En las entidades JPA se prefiere `@Getter` y `@Setter` frente a `@Data`, para evitar que métodos generados como `toString`, `equals` y `hashCode` recorran relaciones bidireccionales y puedan causar ciclos.

La inyección de dependencias debe realizarse por constructor; la guía indica evitar `@Autowired` directamente sobre atributos.

### 12.2. Comentarios Javadoc

Cada clase y cada método público deben incluir comentarios Javadoc que expliquen su propósito. En los métodos que corresponda, la documentación debe incluir sus parámetros y su valor de retorno.

Los comentarios deben describir la función real del código, no únicamente repetir el nombre de la clase o del método.

## 13. Autor

**By: Bryan Steven Ramirez Rico**

**Actividad:** Parcial segundo corte — Electiva CPI

**Institución:** Corporacion universitaria minuto de Dios UNIMINUTO

**Proyecto:** API REST para la gestión de productos y pedidos con Spring Boot y PostgreSQL