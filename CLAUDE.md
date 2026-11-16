# Contexto del proyecto — Gestión de películas (Frameworks Backend y Microservicios)

Trabajo **individual** de la asignatura «Frameworks Backend y Microservicios» (Máster en Desarrollo Ágil de Software para la Web, UAH; profesor Salvador Otón). **No tiene nada que ver con el proyecto de F1**, que es de otra materia.

## Cómo quiero trabajar
- Conozco SQL y ya he hecho en otro proyecto capas controller → service → repository, DTOs y errores con `ProblemDetail`. Sigo aprendiendo Spring Boot; Thymeleaf, `RestClient`, Eureka y Gateway son nuevos para mí.
- **Modo de trabajo:** guíame como senior. Explícame cada pieza antes de escribirla; **yo escribo el código** y tú lo revisas; pistas antes que soluciones; al terminar cada pieza, 3–4 preguntas de repaso (tengo que poder defenderlo). Trozos cortos, poca información de golpe. Responde en español.
- Seguir el estilo y las versiones del proyecto de ejemplo del profesor («aplicación de cursos»).

## Enunciados
Los PDF están en `C:\Users\jdieg\Downloads\calse UAH\` («Trabajo inicial FBM.pdf» y «Trabajo final FBM.pdf»).

### Trabajo inicial — entrega 29-nov-2026 (2 puntos: 1 por microservicio)
Aplicación tipo Filmaffinity, solo funcionalidad de **administrador**:
- **Película:** id autoincremental, título, año, duración, país, dirección, género, sinopsis, imagen de portada, y reparto (actores, relación N:M).
- **Actor:** id autoincremental, nombre, fecha de nacimiento, país de nacimiento.
- Alta, baja, modificación y listado de películas y de actores.
- Búsqueda de películas por título, género y actor; de actores por nombre y país de nacimiento.
- Orden obligado: 1) tablas en **MySQL**; 2) microservicio backend con **JPA + REST**; 3) microservicio frontend con **RestClient + Thymeleaf** (MVC, diseño libre).
- Si no se hace el frontend, vale documentar las pruebas REST con Postman.

### Trabajo final — entrega 21-feb-2027 (6 puntos: 3,5 backend + 2,5 frontend)
Amplía el inicial:
- Nuevo microservicio de **usuarios y críticas**: tablas `Users` y `Authorities` con su tabla intermedia (patrón de la aplicación de cursos); crítica = id, id de película, valoración (texto), nota entera 1–10, fecha; usuario 1:N críticas.
- **Eureka** y **Spring Cloud Gateway**.
- Dos roles. Ambos ven películas, actores y críticas, y buscan. Administrador: además CRUD de usuarios, baja y listado de críticas. Usuario: registrarse y hacer críticas.
- La ficha de la película muestra portada, detalles, críticas y **nota media**.
- Además: **vídeo explicativo**.

### Entrega (ambos)
- Zip `Trabajo_Inicial_ApellidosAlumno` / `Trabajo_Final_ApellidosAlumno` con: memoria en Word o PDF (análisis/diseño breve + manual de usuario con pantallas), proyecto Maven y script(s) SQL.
- **Obligatorio:** «Anexo: Declaración de Uso de Inteligencia Artificial» al final de la memoria: herramientas, ámbitos (SQL/JPA, DTOs con records y mappers, `@RestControllerAdvice` + RFC 7807, RestClient, Gateway/Eureka, Thymeleaf/CSS, depuración), 2–3 prompts clave (objetivo, prompt textual, resultado, adaptación crítica mía) y reflexión de 5–10 líneas. Código sin comprender penaliza.
- **Registro de prompts:** ir anotándolos en `docs/registro-ia.md` a medida que se trabaja.

## Código de referencia del profesor
En `C:\Users\jdieg\Downloads\calse UAH\CodigoSesionN\CodigoSesionN\` (la 5 está en `CodigoSesion5(1)`). Es el patrón a seguir.
- **Sesión 6 = plantilla directa del trabajo** (`Proyectos\`): `cursosAlumnosEureka` (backend JPA + REST; Curso–Alumno N:M ≈ Película–Actor), `clienteCursosFinal` (Thymeleaf + RestClient), `usuariosMatriculasEureka` (Users/Authorities + Matrículas ≈ usuarios + críticas), `eurekaServer`, `gatewayR`. Scripts en `db\`.
- Sesión 5: `usuariosMatriculas` sin Eureka. Sesión 7: `clienteCursosSeguro` (login con Spring Security). Sesión 8: `servidorConfig`. Sesión 9: `cursosjpaSwagger`.
- Versiones: **Spring Boot 4.1.1**, Spring Cloud 2025.1.3, groupId `es.uah`. El profesor usa Java 25; aquí **Java 21** (`<java.version>21</java.version>`).
- Paquetes: `controller`, `service` (`IXxxService` + `XxxServiceImpl`), `repository`, `model`, `dto`, `exception`. Inyección por constructor.
- Backend: entidades JPA sin Lombok, con constructor desde DTO y `actualizarDesdeDTO`; `@ManyToMany` con `@JoinTable` en el lado dueño y `mappedBy` en el otro, más métodos `add/remove`; DTOs **records** (`XxxRequestDTO` con validación, `XxxResponseDTO` y `XxxSimpleResponseDTO` con `fromEntity` para evitar recursión); consultas derivadas (`findByNombreContainingIgnoreCase`); rutas `/api/cursos`, `/api/cursos/nombre/{nombre}`…; POST devuelve 201 con `Location`, DELETE 204; `GlobalExceptionHandler` (`@RestControllerAdvice` + `ProblemDetail`) con `ResourceNotFoundException` y `BadRequestException`. La relación N:M se gestiona con `POST /api/alumnos/{idAlumno}/cursos/{idCurso}`.
- BD: script estilo MySQL Workbench, tablas `Cursos`, `Alumnos`, `Cursos_has_Alumnos`, ids `idCurso`; `spring.jpa.generate-ddl=false`, usuario `root`. Backend en el puerto 8001, usuarios 8002, Eureka 8761, Gateway 8090, cliente 9000.
- Cliente: `RestClientConfig` con un bean `RestClient` por servicio (URL en `application.properties`), modelos POJO, servicios que llaman al REST y paginan a mano (`PageImpl`, `PageRender`), controladores `@Controller` que devuelven vistas (`listX`, `formX`, `verX`, `searchX`), Bootstrap local, imagen subida a la carpeta `uploads` del cliente (en la BD solo se guarda el nombre del fichero).

## Decisiones (14-nov-2026)
- **El código del profesor es un ejemplo, no una norma.** Se respeta su arquitectura (dos microservicios, capas, records, `ProblemDetail`, `RestClient`, Thymeleaf), pero lo que esté anticuado o sea mejorable se hace con lo actual de Java/Spring. Cada desviación se anota en `docs/mejoras.md` con su porqué, para la memoria y el anexo de IA.
- **Búsquedas (16-nov-2026):** una sola búsqueda por entidad con filtros opcionales combinables (`GET /api/peliculas?titulo=&genero=&actor=`), con un único método `@Query` (JPQL) por repositorio, en vez de un método y una ruta por campo como el profesor. Servicios sin prefijo `I` (`ActorService`).
- **Git:** un solo repositorio con los dos microservicios como carpetas, rama `main`. Remoto: `https://github.com/jdiiego/Peliculas_FBM_UAH` (privado). **Todo en español** (mensajes de commit, comentarios y nombres en el código); prevalece sobre la regla global de inglés. Commits pequeños con prefijo Conventional Commits (`feat:`, `fix:`, `docs:`…), sin líneas de coautoría. Los hago yo (el alumno) salvo que pida lo contrario; nunca `push` sin preguntar. `gh` no está instalado: el repositorio de GitHub se crea desde la web.

## Entorno (14-nov-2026)
- Temurin JDK 21. **Maven no está en el PATH**: usar el wrapper `mvnw` de cada proyecto.
- MySQL 8.0.30 de Laragon (`C:\laragon\bin\mysql\mysql-8.0.30-winx64`), en ejecución.

## Estado
- 14-nov-2026: carpeta creada. Código del profesor revisado (sesiones 5–9). Siguiente: paso 1, script SQL (`Peliculas`, `Actores`, tabla intermedia).
