@echo off
echo ===============================================
echo   TFGFITAPP - INICIO RAPIDO
echo ===============================================
echo.

REM Verificar que estamos en el directorio correcto
if not exist "pom.xml" (
    echo ERROR: No se encuentra pom.xml
    echo Asegurate de ejecutar este script desde la raiz del proyecto
    pause
    exit /b 1
)

echo [1/3] Limpiando y compilando el proyecto...
echo.
call mvnw.cmd clean compile -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: La compilacion fallo
    pause
    exit /b 1
)

echo.
echo ===============================================
echo [2/3] Compilacion exitosa!
echo ===============================================
echo.
echo Ahora debes ejecutar el script SQL:
echo   1. Abre MySQL Workbench
echo   2. Ejecuta: datos_prueba_SIMPLE.sql
echo   3. Verifica que se crearon los datos
echo.
echo ===============================================
echo [3/3] Iniciando Spring Boot...
echo ===============================================
echo.
echo CREDENCIALES DE ACCESO:
echo   URL: http://localhost:8081/login
echo   Email: admin@tfgfitapp.com
echo   Password: password
echo.
echo Presiona Ctrl+C para detener la aplicacion
echo.
echo ===============================================
echo.

call mvnw.cmd spring-boot:run

