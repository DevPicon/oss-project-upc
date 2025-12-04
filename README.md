# Sistema de Gestión de Activos y Dispositivos TI - OSS UPC

Sistema de gestión de activos y dispositivos TI para la Universidad Peruana de Ciencias Aplicadas (UPC), desarrollado con Spring Boot, PostgreSQL y Angular.

## 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Tecnologías](#tecnologías)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Configuración](#instalación-y-configuración)
- [Ejecutar el Proyecto](#ejecutar-el-proyecto)
- [Probar la API](#probar-la-api)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Estado del Desarrollo](#estado-del-desarrollo)

---

## 📖 Descripción

Sistema integral para la gestión de dispositivos tecnológicos asignados a empleados de UPC, permitiendo:

- Registro y control de inventario de dispositivos TI
- Asignación de equipos a empleados
- Seguimiento del ciclo de vida de dispositivos
- Gestión de reemplazos y devoluciones
- Auditoría completa de movimientos
- Catálogos de mantenimiento (áreas, puestos, marcas, proveedores, etc.)

---

## 🛠 Tecnologías

### Backend
- **Java 21** - Lenguaje de programación
- **Spring Boot 3.4.x** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **Hibernate** - ORM
- **PostgreSQL 16** - Base de datos
- **Flyway** - Migraciones de base de datos
- **Gradle 8.x** - Gestión de dependencias
- **Lombok** - Reducción de boilerplate
- **Bean Validation** - Validación de datos

### Frontend
- **Angular 17** - Framework frontend
- **TypeScript** - Lenguaje
- **PrimeNG** - Componentes UI

### Herramientas
- **Docker** - Contenedorización
- **Git** - Control de versiones
- **Postman** - Testing de API

---

## ✅ Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

### 1. Java 21
```bash
# Verificar versión
java -version

# Debe mostrar: openjdk version "21.x.x"
```

**Instalación:**
- **Con SDKMAN (recomendado):**
  ```bash
  curl -s "https://get.sdkman.io" | bash
  source "$HOME/.sdkman/bin/sdkman-init.sh"
  sdk install java 21.0.1-tem
  sdk use java 21.0.1-tem
  ```

- **Con Homebrew (macOS):**
  ```bash
  brew install openjdk@21
  ```

- **Descarga directa:**
  - [Temurin JDK 21](https://adoptium.net/temurin/releases/?version=21)
  - [Oracle JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)

### 2. Docker Desktop
```bash
# Verificar instalación
docker --version
docker-compose --version
```

**Instalación:**
- **macOS/Windows:** [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- **Linux:**
  ```bash
  # Ubuntu/Debian
  sudo apt-get update
  sudo apt-get install docker.io docker-compose
  ```

### 3. Git
```bash
# Verificar instalación
git --version
```

### 4. Postman (opcional, para testing)
- Descargar desde [postman.com](https://www.postman.com/downloads/)

---

## 🚀 Instalación y Configuración

### Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/DevPicon/oss-project-upc.git
cd oss-project-upc
```

### Paso 2: Configurar Variables de Entorno

1. **Navegar a la carpeta del backend:**
   ```bash
   cd backend
   ```

2. **Copiar el archivo de ejemplo `.env.example` a `.env`:**
   ```bash
   cp .env.example .env
   ```

3. **Editar el archivo `.env` con tus credenciales:**
   ```bash
   nano .env  # o usar tu editor preferido
   ```

   Contenido del `.env`:
   ```bash
   # Configuración de Base de Datos
   DB_HOST=localhost
   DB_PORT=5432
   DB_NAME=oss_db
   DB_USER=ossuser
   DB_PASSWORD=osspass123

   # IMPORTANTE: Cambia la contraseña en producción
   ```

   > ⚠️ **Nota de Seguridad:** El archivo `.env` NO debe ser versionado en Git. Ya está incluido en `.gitignore`.

### Paso 3: Levantar la Base de Datos con Docker

1. **Navegar a la carpeta de base de datos:**
   ```bash
   cd ../database
   ```

2. **Iniciar el contenedor de PostgreSQL:**
   ```bash
   docker-compose up -d
   ```

3. **Verificar que el contenedor está corriendo:**
   ```bash
   docker ps

   # Deberías ver algo como:
   # CONTAINER ID   IMAGE          PORTS                    NAMES
   # abc123def456   postgres:16    0.0.0.0:5432->5432/tcp   oss-postgres
   ```

4. **Verificar logs del contenedor (opcional):**
   ```bash
   docker-compose logs -f
   ```

   Para salir de los logs, presiona `Ctrl+C`.

### Paso 4: Verificar la Conexión a la Base de Datos (Opcional)

```bash
# Conectarse a PostgreSQL dentro del contenedor
docker exec -it oss-postgres psql -U ossuser -d oss_db

# Dentro de psql, ejecutar:
\dt  # Listar tablas (debería estar vacío aún)
\q   # Salir
```

---

## ▶️ Ejecutar el Proyecto

### Opción 1: Ejecutar con Gradle (Recomendado para Desarrollo)

1. **Navegar a la carpeta del backend:**
   ```bash
   cd backend
   ```

2. **Dar permisos de ejecución al script de Gradle (solo la primera vez en Linux/macOS):**
   ```bash
   chmod +x gradlew
   ```

3. **Ejecutar la aplicación:**
   ```bash
   ./gradlew bootRun
   ```

   En Windows:
   ```bash
   gradlew.bat bootRun
   ```

4. **Verificar que la aplicación está corriendo:**

   Deberías ver en la consola:
   ```
   2024-XX-XX XX:XX:XX.XXX  INFO 12345 --- [  restartedMain] p.e.u.o.g.OssBackendApplication  : Started OssBackendApplication in X.XXX seconds
   ```

5. **La API estará disponible en:**
   ```
   http://localhost:8080
   ```

### Opción 2: Compilar y Ejecutar JAR

```bash
# Compilar el proyecto
./gradlew clean build

# Ejecutar el JAR generado
java -jar build/libs/oss-backend-0.0.1-SNAPSHOT.jar
```

### Verificar que Flyway Ejecutó las Migraciones

Al iniciar la aplicación por primera vez, Flyway ejecutará automáticamente los scripts SQL. Deberías ver en los logs:

```
INFO  FlywayExecutor : Flyway Community Edition X.X.X
INFO  FlywayExecutor : Migrating schema "public" to version "1 - create catalog tables"
INFO  FlywayExecutor : Migrating schema "public" to version "2 - create main tables"
INFO  FlywayExecutor : Migrating schema "public" to version "3 - create indexes"
INFO  FlywayExecutor : Migrating schema "public" to version "4 - insert initial data"
INFO  FlywayExecutor : Successfully applied 4 migrations
```

Puedes verificar las migraciones ejecutadas:

```bash
docker exec -it oss-postgres psql -U ossuser -d oss_db -c "SELECT * FROM flyway_schema_history;"
```

---

## 🧪 Probar la API

### Opción 1: Usar Postman (Recomendado)

1. **Importar la colección de Postman:**
   - Abrir Postman
   - Click en **Import**
   - Seleccionar el archivo: `postman/OSS-UPC-API.postman_collection.json`
   - Importar también el environment: `postman/OSS-UPC-Local.postman_environment.json`

2. **Seleccionar el environment "OSS UPC - Local"** en el dropdown superior derecho

3. **Probar los endpoints:**
   - La colección incluye ejemplos de todas las operaciones CRUD para los 14 catálogos
   - Los requests ya tienen data de prueba incluida

### Opción 2: Usar cURL

**Ejemplos de comandos:**

```bash
# Listar todas las marcas
curl http://localhost:8080/api/v1/catalogos/marcas

# Listar solo marcas activas
curl http://localhost:8080/api/v1/catalogos/marcas/activas

# Obtener una marca por ID
curl http://localhost:8080/api/v1/catalogos/marcas/1

# Crear una nueva marca
curl -X POST http://localhost:8080/api/v1/catalogos/marcas \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "SAMSUNG",
    "nombre": "Samsung",
    "activo": true
  }'

# Actualizar una marca
curl -X PUT http://localhost:8080/api/v1/catalogos/marcas/1 \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "DELL",
    "nombre": "Dell Technologies",
    "activo": true
  }'

# Eliminar una marca (soft delete)
curl -X DELETE http://localhost:8080/api/v1/catalogos/marcas/1

# Buscar marcas por nombre
curl "http://localhost:8080/api/v1/catalogos/marcas/buscar?nombre=Dell"
```

### Opción 3: Swagger UI (Documentación Interactiva)

> **Nota:** Swagger UI se configurará en la Fase 4. Por ahora, usa Postman o cURL.

Cuando esté disponible:
```
http://localhost:8080/swagger-ui.html
```

---

## 📁 Estructura del Proyecto

```
oss-project-upc/
├── backend/                        # Backend Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/pe/edu/upc/oss/group1/
│   │   │   │   ├── controller/    # Controladores REST
│   │   │   │   ├── dto/           # Data Transfer Objects
│   │   │   │   │   ├── request/   # DTOs de entrada
│   │   │   │   │   └── response/  # DTOs de salida
│   │   │   │   ├── entity/        # Entidades JPA
│   │   │   │   │   └── catalogo/  # Catálogos
│   │   │   │   ├── exception/     # Excepciones personalizadas
│   │   │   │   ├── mapper/        # Conversores DTO-Entity
│   │   │   │   ├── repository/    # Repositorios Spring Data
│   │   │   │   └── service/       # Servicios de negocio
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── db/migration/  # Scripts Flyway
│   │   └── test/                  # Tests unitarios y de integración
│   ├── .env.example               # Ejemplo de variables de entorno
│   ├── .gitignore
│   ├── build.gradle               # Configuración Gradle
│   └── SETUP.md                   # Instrucciones de setup detalladas
├── database/
│   ├── docker-compose.yml         # Configuración Docker PostgreSQL
│   └── init/                      # Scripts SQL (respaldo)
├── frontend/                      # Frontend Angular
│   └── oss-app/
├── postman/                       # Colecciones Postman
│   ├── OSS-UPC-API.postman_collection.json
│   └── OSS-UPC-Local.postman_environment.json
├── .gitignore
└── README.md                      # Este archivo
```

---

## 📊 Estado del Desarrollo

### ✅ Fase 1: Fundación (Completada)
- [x] Setup del proyecto Spring Boot
- [x] Scripts SQL con Flyway
- [x] Configuración de base de datos
- [x] Variables de entorno
- [x] Docker Compose

### ✅ Fase 2: Catálogos (Completada)
- [x] 14 entidades de catálogos
- [x] Repositories con consultas derivadas
- [x] Services con lógica de negocio
- [x] DTOs Request y Response
- [x] Mappers para conversión
- [x] Controllers REST con endpoints CRUD
- [x] Validaciones con Bean Validation
- [x] Manejo de excepciones global

**Catálogos implementados:**
1. CatMarca - Marcas de dispositivos
2. CatEstadoEmpleado - Estados de empleados
3. CatArea - Áreas organizacionales (con jerarquía)
4. CatPuesto - Puestos de trabajo
5. CatSede - Sedes físicas
6. CatTipoDispositivo - Tipos de dispositivos
7. CatEstadoDispositivo - Estados de dispositivos
8. CatProveedor - Proveedores
9. CatEstadoAsignacion - Estados de asignación
10. CatMotivoReemplazo - Motivos de reemplazo
11. CatEstadoReemplazo - Estados de reemplazo
12. CatEstadoSolicitud - Estados de solicitud
13. CatCondicionDevolucion - Condiciones de devolución
14. CatTipoMovimiento - Tipos de movimiento

**Endpoints disponibles (para cada catálogo):**
- `GET /api/v1/catalogos/{catalogo}` - Listar todos
- `GET /api/v1/catalogos/{catalogo}/activos` - Listar solo activos
- `GET /api/v1/catalogos/{catalogo}/{id}` - Obtener por ID
- `GET /api/v1/catalogos/{catalogo}/codigo/{codigo}` - Obtener por código
- `POST /api/v1/catalogos/{catalogo}` - Crear nuevo
- `PUT /api/v1/catalogos/{catalogo}/{id}` - Actualizar
- `DELETE /api/v1/catalogos/{catalogo}/{id}` - Eliminar (soft delete)
- `GET /api/v1/catalogos/{catalogo}/buscar?nombre=X` - Buscar

### 🚧 Fase 3: Entidades Core (Próxima)
- [ ] Entidades principales (Usuario, Empleado, Dispositivo, etc.)
- [ ] Repositories con queries complejas
- [ ] Services con lógica de negocio avanzada
- [ ] DTOs y mappers
- [ ] Controllers REST
- [ ] Testing unitario e integración

### 📋 Fase 4: Integración y Finalización (Pendiente)
- [ ] Tests E2E
- [ ] Documentación con Swagger
- [ ] Configuración de producción
- [ ] Optimizaciones de performance

---

## 🐛 Troubleshooting

### Error: "Cannot connect to database"

**Solución:**
1. Verificar que Docker está corriendo: `docker ps`
2. Verificar logs del contenedor: `docker-compose logs -f`
3. Verificar el archivo `.env` tiene las credenciales correctas
4. Reiniciar el contenedor:
   ```bash
   docker-compose down
   docker-compose up -d
   ```

### Error: "Port 5432 is already in use"

**Solución:**
Ya tienes PostgreSQL corriendo localmente. Opciones:
1. Detener PostgreSQL local y usar Docker
2. Cambiar el puerto en `docker-compose.yml`:
   ```yaml
   ports:
     - "5433:5432"  # Usar puerto 5433 en tu máquina
   ```
   Y actualizar `DB_PORT=5433` en el `.env`

### Error: "Java version mismatch"

**Solución:**
Asegúrate de usar Java 21. Consulta el archivo `backend/SETUP.md` para instrucciones detalladas de instalación.

### Error: "Flyway migration failed"

**Solución:**
1. Limpiar la base de datos:
   ```bash
   docker-compose down -v  # Elimina volúmenes
   docker-compose up -d    # Reinicia
   ```
2. Reiniciar la aplicación: `./gradlew bootRun`

### La aplicación no inicia

**Solución:**
1. Verificar que el puerto 8080 está libre:
   ```bash
   lsof -i :8080  # macOS/Linux
   netstat -ano | findstr :8080  # Windows
   ```
2. Ver logs completos: `./gradlew bootRun --info`
3. Compilar limpio: `./gradlew clean build`

---

## 📚 Documentación Adicional

- [SETUP.md](backend/SETUP.md) - Guía detallada de configuración del entorno
- [Plan de Trabajo](docs/plan-de-trabajo.md) - Fases y tareas del proyecto
- [Arquitectura Backend](docs/arquitectura-backend.md) - Diseño arquitectónico
- [Recomendaciones](docs/RECOMENDACIONES.md) - Best practices y decisiones técnicas
- [Diagrama ER](docs/ER-Diagrama.md) - Modelo de base de datos

---

## 👥 Contribución

Este es un proyecto académico de la Universidad Peruana de Ciencias Aplicadas (UPC).

---

## 📄 Licencia

Este proyecto es de uso académico para UPC.

---

## 🤝 Equipo

- **Desarrollo:** Grupo 1 - Curso OSS
- **Universidad:** Universidad Peruana de Ciencias Aplicadas (UPC)
- **Ciclo:** 2025-1

---

## 📞 Soporte

Para problemas o preguntas:
1. Revisar la sección de [Troubleshooting](#-troubleshooting)
2. Consultar la documentación en la carpeta `docs/`
3. Revisar los issues en GitHub

---

**¡Listo para empezar! 🚀**

Ejecuta `./gradlew bootRun` en la carpeta `backend` y comienza a probar la API con Postman.
