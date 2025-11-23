@echo off
:: Configuración
set IPAPI=0.0.0.0
set PORTAPI=9191

:: Entramos en la carpeta del proyecto
cd php

:: EJECUTAMOS PHP DESDE TU CARPETA C:\php
"C:\php\php.exe" -S %IPAPI%:%PORTAPI%

pause