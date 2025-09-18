
FROM eclipse-temurin:21-jre


WORKDIR /app

# Copiar el jar generado al contenedor
COPY target/online-shop-0.0.1-SNAPSHOT.jar app.jar

# Puerto expuesto
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java","-jar","/app/app.jar"]
