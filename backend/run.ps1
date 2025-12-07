$env:DB_HOST='localhost'
$env:DB_PORT='5432'
$env:DB_NAME='oss_db'
$env:DB_USER='postgres'
$env:DB_PASSWORD=''

Write-Host "Starting Spring Boot with local DB configuration..."
.\gradlew.bat bootRun
