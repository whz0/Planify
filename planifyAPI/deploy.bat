@echo off
echo Compilando la aplicacion
call mvn clean package -DskipTests

echo Compilacion exitosa. Continuando con el siguiente paso...

echo Copiando archivos a la VM...
scp target/planifyAPI-0.0.1-SNAPSHOT.jar planify@34.175.115.84:/home/planify/planify/target/planifyAPI.jar

echo Ejecutando deploy.sh en la VM...
ssh planify@34.175.115.84 "bash /home/planify/planify/deploy.sh"
pause