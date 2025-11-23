@echo off
TITLE Cliente Java Albuaves

:: compilar
echo * Compilando el codigo Java...
javac SearchBirdsAPI.java

:: comprobación de errores
IF %ERRORLEVEL% NEQ 0 (
   echo.
   echo [ERROR] Fallo al compilar. Revisa el codigo.
   pause
   exit /b
)

:: ejecutar
echo.
echo * Ejecutando el Cliente...
echo --------------------------------------------
java SearchBirdsAPI
echo --------------------------------------------

pause