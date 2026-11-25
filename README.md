# Películas UAH

Aplicación web de gestión de fichas de películas y actores, al estilo de Filmaffinity. Trabajo de la asignatura **Frameworks Backend y Microservicios** (Máster en Desarrollo Ágil de Software para la Web, Universidad de Alcalá, curso 2026-27).

Esta versión corresponde al **trabajo inicial**: funcionalidad de administrador (alta, baja, modificación, listado y búsqueda de películas y actores).

## Arquitectura

Dos microservicios Spring Boot independientes:

| Proyecto | Puerto | Qué hace |
|---|---|---|
| `peliculasActores` | 8001 | **Backend.** API REST con JPA sobre MySQL: películas, actores y reparto. |
| `clientePeliculas` | 9000 | **Frontend.** Páginas Thymeleaf que consumen el backend con `RestClient`. No accede a la base de datos. |

```
Navegador ──HTTP──▶ clientePeliculas (9000) ──REST/JSON──▶ peliculasActores (8001) ──JPA──▶ MySQL
```

```
Peliculas_FBM/
├── db/
│   ├── peliculas.sql         # Crea la base de datos y las tablas (vacías)
│   └── datos-ejemplo.sql     # 25 actores y 13 películas con su reparto
├── docs/mejoras.md           # Decisiones que se apartan del código de ejemplo y su motivo
├── peliculasActores/         # Backend (Maven)
└── clientePeliculas/         # Frontend (Maven)
```

## Requisitos

- **JDK 21** (`java -version`).
- **MySQL 8**, por ejemplo el de Laragon, escuchando en `localhost:3306`.
- No hace falta instalar Maven: cada proyecto trae su *wrapper* (`mvnw` / `mvnw.cmd`).

## 1. Crear la base de datos

Desde la raíz del repositorio (ajusta la ruta de `mysql` si no es la de Laragon):

```bash
"/c/laragon/bin/mysql/mysql-8.0.30-winx64/bin/mysql.exe" -u root < db/peliculas.sql
```

Opcionalmente, carga los datos de ejemplo:

```bash
"/c/laragon/bin/mysql/mysql-8.0.30-winx64/bin/mysql.exe" -u root --default-character-set=utf8mb4 < db/datos-ejemplo.sql
```

- `peliculas.sql` **borra y vuelve a crear** las tablas: ejecutarlo de nuevo deja la base de datos vacía.
- `--default-character-set=utf8mb4` es necesario para que las tildes se guarden bien.

### Contraseña de MySQL

El backend se conecta como `root`. La contraseña se lee de la variable de entorno `DB_PASSWORD_PELICULAS`; si no existe, se usa una contraseña vacía (la de Laragon por defecto). No hay ninguna contraseña escrita en el código.

```bash
export DB_PASSWORD_PELICULAS=tu_contraseña
```

## 2. Arrancar

Primero el backend, en una terminal:

```bash
cd peliculasActores && ./mvnw spring-boot:run
```

Cuando aparezca `Started PeliculasActoresApplication`, el cliente en otra terminal:

```bash
cd clientePeliculas && ./mvnw spring-boot:run
```

Abre **http://localhost:9000**.

En Windows con `cmd` o PowerShell, usa `mvnw.cmd` en lugar de `./mvnw`. Para parar, `Ctrl+C` en cada terminal.

Al arrancar, el backend comprueba que las entidades coinciden con las tablas (`ddl-auto=validate`): si falta alguna tabla o columna, no arranca y el error dice cuál. Hibernate no crea ni modifica tablas; el esquema lo define solo `db/peliculas.sql`.

## Portadas de las películas

- Se suben desde el formulario de película (JPG, PNG, WebP o GIF, hasta 5 MB).
- Se guardan en la carpeta **`uploads/`** del directorio desde el que se arranca el cliente (con los comandos de arriba, `clientePeliculas/uploads/`). En la base de datos solo se guarda el nombre del fichero.
- La carpeta **no está en el repositorio** (`.gitignore`): son datos, no código. Si cargas `datos-ejemplo.sql` sin copiar también esa carpeta, las películas se muestran con «Sin portada».
- Para usar otra carpeta: `app.portadas.carpeta=/ruta/absoluta` en `clientePeliculas/src/main/resources/application.properties`.

## API REST del backend

Base: `http://localhost:8001`. Los errores se devuelven en formato `ProblemDetail` (RFC 9457), con los errores de validación por campo en `errors`.

| Método | Ruta | Respuesta |
|---|---|---|
| GET | `/api/actores?nombre=&paisNacimiento=` | 200, lista (los filtros son opcionales y combinables) |
| GET | `/api/actores/{id}` | 200, con sus películas · 404 |
| POST | `/api/actores` | 201 + `Location` · 400 |
| PUT | `/api/actores/{id}` | 200 · 400 · 404 |
| DELETE | `/api/actores/{id}` | 204 (se quita también de los repartos) · 404 |
| GET | `/api/peliculas?titulo=&genero=&actor=` | 200, lista (filtros opcionales y combinables) |
| GET | `/api/peliculas/{id}` | 200, con su reparto · 404 |
| POST | `/api/peliculas` | 201 + `Location` · 400 · 404 si un actor del reparto no existe |
| PUT | `/api/peliculas/{id}` | 200 · 400 · 404 |
| DELETE | `/api/peliculas/{id}` | 204 · 404 |

Las búsquedas usan «contiene» y no distinguen mayúsculas ni tildes («ficcion» encuentra «Ciencia ficción»).

Ejemplo de alta de película con reparto (el reparto son ids de actores; un id repetido se ignora):

```json
POST /api/peliculas
{
  "titulo": "Origen",
  "anio": 2010,
  "duracion": 148,
  "pais": "Estados Unidos",
  "director": "Christopher Nolan",
  "genero": "Ciencia ficción",
  "sinopsis": "Un ladrón especializado en robar secretos a través de los sueños...",
  "imagen": null,
  "actores": [12, 13, 14]
}
```

## Pruebas

Solo existe la prueba de arranque que genera Spring Initializr (necesita MySQL con la base de datos creada):

```bash
cd peliculasActores && ./mvnw test
```

Las operaciones se han probado manualmente con peticiones HTTP y desde la interfaz.

## Pendiente para el trabajo final (febrero de 2027)

Microservicio de usuarios y críticas, Eureka, Spring Cloud Gateway, rol de usuario registrado y nota media en la ficha de cada película.
