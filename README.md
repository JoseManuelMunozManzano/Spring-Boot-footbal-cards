# SPRING BOOT 3 COOKBOOK

Creación de un sistema para gestionar un juego de intercambio de cartas de fútbol.

Se usa la arquitectura MVC y RESTful endpoints.

Se crean tests y se usa Postman.

## Creación de proyecto

Uso Spring Initializr: `https://start.spring.io/`

![alt Spring Initialzr](./images/01-Spring-Initializr.png)

## Ejecución del proyecto

- Clonar/descargar el proyecto
- Ejecutar el comando: `./mvnw spring-boot:run`
  - O ejecutar directamente desde IntelliJ Idea
- En la carpeta `postman` se encuentran los endpoints para probar


## Creación de Controller

Creo el paquete `controllers` y dentro el controller `PlayerController.java`.

Indicamos la anotación `@RestController` a la clase, para que Spring Boot lo registre en su contenedor de dependencias.

Añadimos también la anotación `@RequestMapping` para informar al contenedor web que mapee las peticiones HTTP a su handler, en este caso la clase `PlayerController`.

Mapeamos peticiones a sus métodos handling:

- @GetMapping: mapea una petición GET a un método
- @PostMapping: mapea una petición POST a un método
- @PutMapping: mapea una petición PUT a un método
- @DeleteMapping: mapea una petición DELETE a un método

Para usar toda la información de una petición HTTP podemos usar las siguientes anotaciones:

- @PathVariable: mapea una parte del path de la petición HTTP al argumento del método
- @RequestBody: mapea el body de la petición al argumento del método
- @RequestHeader: mapea las cabeceras de la petición al argumento del método
- @RequestParam: mapea parámetros de la petición, como parámetros query string, form data, o parts in peticiones multipart