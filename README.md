# Titulo
Registro de Pedidos

![alt text](docs/pedidos.jpg)

![Badge en Desarollo](https://img.shields.io/badge/STATUS-EN%20DESAROLLO-green)

# Descripción
Consumidor kafka que, recibido un mensaje con datos básicos de pedido, consulta datos adicionales de cliente y productos pedidos usando
Pi Rest y GraphQL. Para luego, registrar el documento de pedido en la BD Mongo.

# Estado
En contrucción.

# Caracteristicas de la Aplicación
En contrucción.

# Tecnologías Utilizadas

## Backend for frontend - ms-prices-core
Esta basado en el principio de Clean Architecture, con una arquitectura hexagonal usando WebFlux.

![alt text](docs/clean.png)

### Estructura de paquetes

Se definió la siguiente taxonomía de paquetes:

* **application:** Encapsula toda la lógica de negocio y el modelo del dominio.
    * **domain:** Contiene entidades del dominio. Representa el nucleo de toda la aplicación.
    * **usescases:** Abstracción de los casos de uso del sistema. Contiene además la definición de los puertos y excepciones.
* **adapters:** Representa la capa de adaptadores (como su nombre indica) que se conectarán en los puertos expuestos por el sistema
* **config:** Capa transversal a toda la aplicación que contendrá las distintas configuraciones y aspectos del bff.

![alt text](docs/packages.png)


## Java Version
La version que java que se va a utilizar es la 17 basada en el OpenJDK.

Esta la pueden descargar [aqui](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).

## Swagger
### Swagger json
http://localhost:8080/v3/api-docs

### Ambiente local
Se debe levantar el proyecto con el profile "local"

Puede realizarse de 2 maneras:

* Colocando en la configuracion del booteo del IDE en las "VM Options" la siguiente opcion:
* Ejecutando desde las Tasks de gradle la tarea bootRun y teniendo en los jvmArgs la siguiente opcion:
```
    **-Dspring.profiles.active=local**
```

### kafka
* Para ejecución local se requiere instalar y configurar adecuadamente Apache Kafka. Levantar usando localhost:9092
* El nombre del tópico por el que consume mensajes es: ecommerce.sales.orders.json_1
* Un ejemplo de un mensaje adecuado para ser procesado es: {"id" : "263dd6ff-15cc-4ca6-8efb-758989cb28a4", "data": {"id":4, "id_cliente":1, "detalle_pedido": [{"id_producto":1, "cantidad": 10},{"id_producto":2, "cantidad": 12}], "fecha": "2024-07-16", "metodo_pago" : "CREDITO"}}
* Si existen mensajes que no puedan ser procesados se realizaran los reintentos respectivos. En un ambiente real, si agotados los reintentos el mensaje  no puede ser procesado, usualmente se envian a una cola de errores para revisión o procesado de otro tipo.

### API Rest y GraphQL
* Para obtener datos del cliente se consulta Api Grahp. Puede hacerse de 2 maneras.
* * Levantando en el ambiente local el proyecto Nest GraphQL de la APi disponible en https://github.com/jsalas87/ms-clientes-graphql/tree/main y colocando aqui en ms-pedidos-core el boostrap.properties url.client=http://localhost:3000/graphql
* * Via mock, colocando aqui en ms-pedidos-core el boostrap.properties url.client=https://7e2394ab-b3cc-4618-8bd0-f742e116326c.mock.pstmn.io
* Para obtener los datos de productos puede hacerse via mock, colocando aqui en ms-pedidos-core el boostrap.properties url.product=https://387240b3-8996-4444-b6a7-a297ef998a32.mock.pstmn.io/negocio/apigov1.0/productos?ids={ids}

### Base de Datos
Se utiliza MongoDB. Para ejecución local, debe estar instalada y configurada. Se debe colocar aqui en ms-pedidos-core los boostrap.properties spring.data.mongodb.uri=mongodb://localhost:27017/sales y spring.data.mongodb.database=sales

### Tests
Se agrego test de integración, test unitario y test de arquitectura.

### Cobertura
Se puede hacer analisis de la cobertura con jacocoTest. Para ello, ejecutar los tests y luego desde el menu de grandle ir a carpeta verification y ejecutar jacocoTestReport.
luego, reporte puede ser encontrado en build/reports/jacoco/test/html/index.hetm
# Autor
[<img src="https://avatars.githubusercontent.com/u/37299779?s=400&u=e40bfe01a10c844ef8348b57774ffdb872a7d15a&v=4" width=115><br><sub>Juan Salas</sub>](https://github.com/jsalas87)