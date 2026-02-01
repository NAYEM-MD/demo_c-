@echo off
cd /d "%~dp0"
if not exist "WEB-INF\classes" mkdir "WEB-INF\classes"
dir /s /B WEB-INF\src\*.java > sources.txt
javac -d WEB-INF\classes -cp "WEB-INF\lib\*;C:\tomcat10\lib\*" @sources.txt
if errorlevel 1 (
  echo Compilation FAILED.
  del sources.txt
  exit /b 1
)
del sources.txt
echo Compilation completed.
