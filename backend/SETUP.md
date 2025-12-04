# Configuración del Entorno de Desarrollo

Este documento explica cómo configurar el entorno para ejecutar el backend del Sistema OSS UPC.

---

## Requisitos

- Java 21 (OpenJDK o Temurin)
- Docker Desktop
- Git

---

## 1. Instalar Java 21

### Opción A: Con SDKMAN (Recomendado para macOS/Linux)

```bash
# Instalar SDKMAN si no lo tienes
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Instalar Java 21 (Temurin)
sdk install java 21.0.1-tem

# Verificar instalación
java -version
# Debe mostrar: openjdk version "21.0.1"
```

### Opción B: Con ASDF (Si ya usas asdf)

```bash
# Agregar plugin de Java
asdf plugin add java

# Listar versiones disponibles de Java 21
asdf list all java | grep temurin-21

# Instalar Java 21
asdf install java temurin-21.0.5+11

# Establecer como versión global
asdf global java temurin-21.0.5+11

# Verificar instalación
java -version
```

### Opción C: Descarga directa

**macOS:**
```bash
# Con Homebrew
brew install openjdk@21

# Agregar a PATH
echo 'export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

**Windows:**
1. Descargar desde: https://adoptium.net/temurin/releases/?version=21
2. Ejecutar instalador
3. Agregar a PATH en variables de entorno

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

---

## 2. Configurar Variables de Entorno

### Crear archivo .env

```bash
cd backend
cp .env.example .env
```

Edita `.env` con tus credenciales:

```bash
DB_HOST=localhost
DB_PORT=5432
DB_NAME=oss_db
DB_USER=ossuser
DB_PASSWORD=TU_PASSWORD_AQUI
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev
```

---

## 3. Instalar Docker Desktop

### macOS

```bash
# Con Homebrew
brew install --cask docker

# O descarga desde: https://www.docker.com/products/docker-desktop
```

### Windows

Descargar desde: https://www.docker.com/products/docker-desktop

### Linux

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install docker.io docker-compose

# Iniciar servicio
sudo systemctl start docker
sudo systemctl enable docker

# Agregar usuario al grupo docker (para no usar sudo)
sudo usermod -aG docker $USER
# Cerrar sesión y volver a iniciar
```

**Verificar Docker:**
```bash
docker --version
docker-compose --version
```

---

## 4. Iniciar Base de Datos

```bash
cd database
docker-compose up -d
```

**Verificar que PostgreSQL está corriendo:**
```bash
docker ps
# Debe aparecer: oss_postgres

docker logs oss_postgres
# Debe mostrar: "database system is ready to accept connections"
```

**Conectar a PostgreSQL (opcional):**
```bash
docker exec -it oss_postgres psql -U ossuser -d oss_db

# Dentro de psql:
\dt              # Listar tablas
\q               # Salir
```

---

## 5. Compilar y Ejecutar Backend

### Compilar proyecto

```bash
cd backend
./gradlew build
```

Si falla con error de permisos:
```bash
chmod +x gradlew
./gradlew build
```

### Ejecutar aplicación

```bash
./gradlew bootRun
```

**Logs esperados:**
```
Flyway: Successfully validated 4 migrations
Flyway: Current version: 4
Started OssBackendApplication in X.XXX seconds
```

La aplicación estará disponible en: **http://localhost:8080**

---

## 6. Verificar Instalación

### Verificar migraciones Flyway

```bash
docker exec -it oss_postgres psql -U ossuser -d oss_db -c "SELECT version, description, installed_on FROM flyway_schema_history ORDER BY installed_rank;"
```

**Resultado esperado:**
```
 version |          description          |        installed_on
---------+-------------------------------+----------------------------
 1       | create catalog tables         | 2025-XX-XX XX:XX:XX.XXXXXX
 2       | create main tables            | 2025-XX-XX XX:XX:XX.XXXXXX
 3       | create indexes                | 2025-XX-XX XX:XX:XX.XXXXXX
 4       | insert initial data           | 2025-XX-XX XX:XX:XX.XXXXXX
```

### Verificar datos iniciales

```bash
docker exec -it oss_postgres psql -U ossuser -d oss_db -c "SELECT * FROM cat_marca;"
```

**Resultado esperado:**
```
 id | codigo  |   nombre   | activo |      fecha_creacion
----+---------+------------+--------+------------------------
  1 | DELL    | Dell       | t      | 2025-XX-XX XX:XX:XX
  2 | HP      | HP         | t      | 2025-XX-XX XX:XX:XX
  ...
```

### Verificar endpoint health

```bash
curl http://localhost:8080/actuator/health
```

**Resultado esperado:**
```json
{"status":"UP"}
```

---

## 7. Herramientas Recomendadas

### IDEs

**IntelliJ IDEA Community (Recomendado):**
```bash
# macOS con Homebrew
brew install --cask intellij-idea-ce

# O descargar desde: https://www.jetbrains.com/idea/download/
```

**VSCode con extensiones:**
```bash
# Extensiones necesarias:
- Extension Pack for Java (Microsoft)
- Spring Boot Extension Pack
- Gradle for Java
```

### Cliente de Base de Datos

**DBeaver (Gratis):**
```bash
# macOS
brew install --cask dbeaver-community

# O descargar desde: https://dbeaver.io/download/
```

**Configuración de conexión en DBeaver:**
- Host: localhost
- Port: 5432
- Database: oss_db
- Username: ossuser
- Password: (el que pusiste en .env)

### Cliente REST

**Postman:**
```bash
# macOS
brew install --cask postman

# O descargar desde: https://www.postman.com/downloads/
```

**Insomnia (alternativa):**
```bash
brew install --cask insomnia
```

---

## 8. Comandos Útiles

### Gradle

```bash
# Compilar
./gradlew build

# Ejecutar aplicación
./gradlew bootRun

# Ejecutar tests
./gradlew test

# Ver reporte de tests
./gradlew test && open build/reports/tests/test/index.html

# Limpiar build
./gradlew clean

# Ver dependencias
./gradlew dependencies

# Actualizar wrapper de Gradle
./gradlew wrapper --gradle-version=8.5
```

### Docker

```bash
# Iniciar contenedores
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener contenedores
docker-compose stop

# Detener y eliminar contenedores
docker-compose down

# Detener y eliminar contenedores + volúmenes (¡CUIDADO! borra datos)
docker-compose down -v

# Reiniciar PostgreSQL
docker-compose restart postgres
```

### PostgreSQL

```bash
# Conectar a PostgreSQL
docker exec -it oss_postgres psql -U ossuser -d oss_db

# Ejecutar query desde terminal
docker exec -it oss_postgres psql -U ossuser -d oss_db -c "SELECT COUNT(*) FROM cat_marca;"

# Backup de base de datos
docker exec oss_postgres pg_dump -U ossuser oss_db > backup.sql

# Restaurar backup
docker exec -i oss_postgres psql -U ossuser -d oss_db < backup.sql
```

---

## 9. Troubleshooting

### Error: "JAVA_HOME is set to an invalid directory"

**Solución:**
```bash
# Verificar JAVA_HOME
echo $JAVA_HOME

# Si está mal, corregir (ejemplo con SDKMAN):
export JAVA_HOME=$HOME/.sdkman/candidates/java/current

# O con asdf:
export JAVA_HOME=$(asdf where java)

# Agregar a ~/.zshrc o ~/.bashrc para que persista
echo 'export JAVA_HOME=$(asdf where java)' >> ~/.zshrc
```

### Error: "Cannot connect to Docker daemon"

**Solución:**
```bash
# macOS: Iniciar Docker Desktop desde aplicaciones
# Linux: Iniciar servicio
sudo systemctl start docker

# Verificar
docker ps
```

### Error: "Port 5432 is already in use"

**Solución:**
```bash
# Ver qué está usando el puerto
lsof -i :5432

# Opción 1: Detener el servicio existente
# Opción 2: Cambiar puerto en docker-compose.yml
# Cambiar: "5433:5432" y actualizar .env con DB_PORT=5433
```

### Error: Flyway migration failed

**Solución:**
```bash
# Ver estado de migraciones
docker exec -it oss_postgres psql -U ossuser -d oss_db -c "SELECT * FROM flyway_schema_history;"

# Si hay error, limpiar y reiniciar (¡CUIDADO! borra datos)
docker-compose down -v
docker-compose up -d
./gradlew bootRun
```

### Error: "The driver has not received any packets from the server"

**Solución:**
```bash
# Esperar a que PostgreSQL esté completamente iniciado
docker logs oss_postgres | grep "ready to accept connections"

# Reiniciar backend
./gradlew bootRun
```

---

## 10. Variables de Entorno en Diferentes Ambientes

### Desarrollo (local)

Usa archivo `.env` en la carpeta `backend/`

### Testing (CI/CD)

Configura variables en tu pipeline:
```yaml
# Ejemplo GitHub Actions
env:
  DB_HOST: localhost
  DB_USER: postgres
  DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
```

### Producción

Usa variables de entorno del servidor (no archivo .env):
```bash
# Ejemplo en servidor Linux
export DB_HOST=db.production.com
export DB_USER=produser
export DB_PASSWORD=securepassword
java -jar oss-backend.jar
```

---

## ¿Listo?

Una vez completados todos los pasos, deberías ver:

✅ Java 21 instalado
✅ Docker corriendo
✅ PostgreSQL con 22 tablas creadas
✅ Backend compilando sin errores
✅ Backend ejecutándose en http://localhost:8080
✅ Actuator health en UP

**Siguiente paso:** Empezar con la Fase 2 - Desarrollo de Catálogos
