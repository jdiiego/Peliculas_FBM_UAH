# Mejoras respecto al código de ejemplo

Diferencias deliberadas con el proyecto de la asignatura (aplicación de cursos, sesión 6) y su motivo. Sirve de base para la memoria y para el anexo de uso de IA.

## Base de datos

- **Nombres en `snake_case` en minúscula** (`peliculas`, `id_pelicula`, `fecha_nacimiento`) en vez de `Cursos` / `idCurso`. Spring traduce solo `fechaNacimiento` → `fecha_nacimiento`, así que las entidades no necesitan `@Column` en cada campo ni cambiar la estrategia de nombres de Hibernate.
- **`utf8mb4` / `utf8mb4_spanish_ci`** en vez de `utf8`, que en MySQL 8 es un alias en desuso de `utf8mb3` (3 bytes, sin todos los caracteres Unicode).
- **`ON DELETE CASCADE`** en las claves foráneas de `peliculas_actores` en vez de `NO ACTION`. Al borrar una película o un actor, MySQL elimina sus filas del reparto; el servicio no tiene que desvincular la relación a mano antes de borrar.

## Configuración

- **Sin `driver-class-name` ni `hibernate.dialect`**: Spring Boot los deduce de la URL de conexión.
- **`ddl-auto=validate`**: Hibernate no crea ni modifica tablas (el script SQL es la fuente de verdad), pero comprueba al arrancar que las entidades encajan con ellas.
- **Contraseña desde una variable de entorno** (`${DB_PASSWORD_PELICULAS:}`) en vez de escrita en `application.properties`.

## Entidades

- **`@JoinColumn` sin `referencedColumnName`**: si no se indica, apunta a la clave primaria. Con `referencedColumnName = "id_pelicula"` Hibernate exige un campo declarado con ese nombre de columna, cosa que el ejemplo resuelve con `@Column` en cada id.
- **Lado dueño de la relación N:M en `Pelicula`** (en el ejemplo, en `Alumno`): lo natural es editar el reparto de una película.

## Búsquedas

- **Una búsqueda por entidad con filtros opcionales y combinables** (`GET /api/peliculas?titulo=…&genero=…&actor=…`) en vez de un método y una ruta por campo (`/api/cursos/nombre/{nombre}`). Permite combinar filtros, sigue la convención REST de filtrar la colección con parámetros y evita repetir cinco búsquedas en cada capa (repositorio, servicio, controlador, cliente y vista). Se implementa con un único método `@Query` en JPQL por repositorio.

## Reparto (relación N:M)

- **El reparto viaja dentro de la película** (`"actores": [1, 3]` en `PeliculaRequestDTO`) en vez de endpoints aparte para añadir o quitar cada relación (`POST /api/alumnos/{id}/cursos/{idCurso}` en el ejemplo). El reparto forma parte de la ficha de la película, se edita en el mismo formulario y una película no queda nunca a medio crear. Cada envío sustituye el reparto anterior. Si se pide un id de actor que no existe, se responde 404.

## Estilo

- **Servicios sin el prefijo `I`** (`ActorService` + `ActorServiceImpl` en vez de `IActorService`): el prefijo es una convención de C#, no de Java/Spring.
