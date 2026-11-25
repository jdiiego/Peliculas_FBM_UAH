# Mejoras respecto al código de ejemplo

Diferencias deliberadas con el proyecto de la asignatura (aplicación de cursos, sesión 6: `cursosAlumnosEureka` y `clienteCursosFinal`) y su motivo. Sirve de base para la memoria y para el anexo de uso de IA.

## Backend (`peliculasActores`)

### Base de datos

- **Nombres en `snake_case` en minúscula** (`peliculas`, `id_pelicula`, `fecha_nacimiento`) en vez de `Cursos` / `idCurso`. Spring traduce solo `fechaNacimiento` → `fecha_nacimiento`, así que las entidades no necesitan `@Column` en cada campo ni cambiar la estrategia de nombres de Hibernate.
- **`utf8mb4` / `utf8mb4_spanish_ci`** en vez de `utf8`, que en MySQL 8 es un alias en desuso de `utf8mb3` (3 bytes, sin todos los caracteres Unicode). Efecto añadido: las búsquedas no distinguen tildes («ficcion» encuentra «Ciencia ficción») sin escribir código para ello.
- **`ON DELETE CASCADE`** en las claves foráneas de `peliculas_actores` en vez de `NO ACTION`. Al borrar una película o un actor, MySQL elimina sus filas del reparto; el servicio no tiene que desvincular la relación a mano antes de borrar (el ejemplo recorre la lista y la vacía).
- **Script de datos de ejemplo** (`db/datos-ejemplo.sql`) separado del de tablas, para poder probar la aplicación con datos realistas.

### Configuración

- **Sin `driver-class-name` ni `hibernate.dialect`**: Spring Boot los deduce de la URL de conexión.
- **`ddl-auto=validate`**: Hibernate no crea ni modifica tablas (el script SQL es la fuente de verdad), pero comprueba al arrancar que las entidades encajan con ellas.
- **Contraseña desde una variable de entorno** (`${DB_PASSWORD_PELICULAS:}`) en vez de escrita en `application.properties`. Con un nombre propio del proyecto: con un nombre genérico (`DB_PASSWORD`) chocaba con otra variable del mismo equipo.

### Entidades

- **`@JoinColumn` sin `referencedColumnName`**: si no se indica, apunta a la clave primaria. Con `referencedColumnName = "id_pelicula"` Hibernate exige un campo declarado con ese nombre de columna, cosa que el ejemplo resuelve con `@Column` en cada id.
- **Lado dueño de la relación N:M en `Pelicula`** (en el ejemplo, en `Alumno`): lo natural es editar el reparto de una película.

### Búsquedas

- **Una búsqueda por entidad con filtros opcionales y combinables** (`GET /api/peliculas?titulo=…&genero=…&actor=…`) en vez de un método y una ruta por campo (`/api/cursos/nombre/{nombre}`). Permite combinar filtros, sigue la convención REST de filtrar la colección con parámetros y evita repetir cinco búsquedas en cada capa (repositorio, servicio, controlador, cliente y vista). Se implementa con un único método `@Query` en JPQL por repositorio.
- **`LEFT JOIN` y `DISTINCT`** en la búsqueda de películas: con un `JOIN` normal desaparecerían del listado las películas sin reparto, y sin `DISTINCT` una película saldría repetida si coinciden varios de sus actores.
- **Una búsqueda sin resultados devuelve una lista vacía (200), no un 404**: el 404 indica que un recurso concreto no existe, no que un filtro no encuentre nada.

### Reparto (relación N:M)

- **El reparto viaja dentro de la película** (`"actores": [1, 3]` en `PeliculaRequestDTO`) en vez de endpoints aparte para añadir o quitar cada relación (`POST /api/alumnos/{id}/cursos/{idCurso}` en el ejemplo). El reparto forma parte de la ficha de la película, se edita en el mismo formulario y una película no queda nunca a medio crear. Cada envío sustituye el reparto anterior.
- **Validación del reparto**: si se pide un id de actor que no existe se responde 404 (se compara cuántos ids se pidieron con cuántos actores devuelve `findAllById`); los ids repetidos se ignoran (`distinct()`), y un id nulo da 400 gracias a `List<@NotNull Integer>`, que valida cada elemento de la lista.

### Errores

- **`GlobalExceptionHandler` extiende `ResponseEntityExceptionHandler`**. El ejemplo solo captura sus propias excepciones; los errores que lanza Spring (id no numérico, JSON mal formado, método no permitido) salían con el formato por defecto e incluso con la traza Java. Al extender esa clase, todos se devuelven como `ProblemDetail`. Para añadir el mapa de errores por campo se **sobrescribe** `handleMethodArgumentNotValid` en lugar de declarar otro `@ExceptionHandler`, que sería ambiguo.

### Estilo

- **Servicios sin el prefijo `I`** (`ActorService` + `ActorServiceImpl` en vez de `IActorService`): el prefijo es una convención de C#, no de Java/Spring.

## Frontend (`clientePeliculas`)

### Comunicación con el backend

- **Un único `RestClient`** con la URL base en `application.properties` (`peliculas.api.url`). En el trabajo final solo cambiará esa URL por la del Gateway.
- **Listas con `ParameterizedTypeReference<List<…>>`** en vez de pedir un array (`Curso[].class`) y convertirlo después.
- **Filtros con `uriBuilder` y `queryParamIfPresent`**: los parámetros vacíos no se añaden a la URL, en vez de concatenar texto.
- **Inyección por constructor** en todos los controladores y servicios. El cliente de ejemplo usa `@Autowired` sobre los campos.
- **Modelos como `record`** para lo que devuelve el backend (solo lectura) y **clases con setters** (`ActorForm`, `PeliculaForm`) para los formularios, que Spring MVC rellena campo a campo.

### Validación y errores

- **Las reglas de validación están solo en el backend.** Cuando responde 400, el cliente lee el mapa `errors` del `ProblemDetail` y coloca cada mensaje junto a su campo del formulario (`ErroresApi`). Así no se duplican las reglas en los dos proyectos ni pueden contradecirse.
- **404 del backend convertido en un aviso** («La película no existe») con redirección al listado, en vez de una página de error.
- **Página `error.html` propia** para fallos inesperados, por ejemplo con el backend apagado.

### Páginas

- **Borrar con un formulario `POST`** (`/peliculas/{id}/eliminar`) en vez de con un enlace `GET /borrar/{id}`. Un `GET` no debe modificar datos: un buscador o una precarga del navegador que visitara el enlace borraría el registro.
- **Fragmentos Thymeleaf** (`fragments/layout.html`) para el `<head>`, el menú y los mensajes, en vez de repetirlos en cada plantilla.
- **Listado y búsqueda en la misma página**, con los filtros conservados en el formulario, en vez de una página de búsqueda aparte.
- **Un único formulario para crear y editar** cada entidad.
- **Reparto con casillas** en el formulario de película, con los actores ya asignados marcados al editar.

### Portadas

- **Nombre único generado** (UUID + extensión) en vez de `UUID_nombreOriginal`: el nombre del usuario no llega al disco.
- **Solo se aceptan imágenes** (JPG, PNG, WebP, GIF) y hasta 5 MB, con un mensaje claro si se supera.
- **Sin ficheros huérfanos**: si el backend rechaza la película, se borra la portada recién subida; al sustituirla, se borra la anterior; al borrar la película, también su portada.
- **Solo se borran ficheros con nombre generado por la aplicación**: el nombre llega en un campo oculto del formulario y no debe poder apuntar a otro fichero del disco.
- **Se sirven con una regla de configuración** (`WebConfig`, `/uploads/**`) en vez de con un método de controlador que lee el fichero.
