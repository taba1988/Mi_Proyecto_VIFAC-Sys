
# Tomcat y jdk utilizado 
FROM tomcat:8.5.100-jdk17

# Limpiar aplicaciones por defecto
RUN rm -rf /usr/local/tomcat/webapps/*

# Crear carpetas necesarias 
RUN mkdir -p /usr/local/tomcat/uploads
RUN mkdir -p /usr/local/tomcat/uploads/perfiles
RUN mkdir -p /usr/local/tomcat/uploads/ayuda

# Ruta del war se debe ajustar si es diferente
COPY dist/Mi_Proyecto_VIFAC-Sys.war /usr/local/tomcat/webapps/ROOT.war

# este Expone el puerto
EXPOSE 8080

# permite Iniciar el Tomcat
CMD ["catalina.sh", "run"]
