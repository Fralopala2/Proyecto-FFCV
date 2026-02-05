#!/bin/bash

# https://raw.githubusercontent.com/Fralopala2/Proyecto-FFCV/refs/heads/entrega_final/installServerAws.sh

set -e

echo "=== Script instalacion automatica AWS ==="
echo "Version: 1.0 - Enero 2026"
echo ""

# Variables globales
machineType=""
webServer=""
appServer=""
webServerIp=""
appServerIp=""
myPrivateIp=""
domainName="app.local"

# Detectar IP privada automaticamente
detectPrivateIp() {
  myPrivateIp=$(hostname -I | awk '{print $1}')
  echo "IP privada detectada: $myPrivateIp"
}

# Modo interactivo
interactiveMode() {
  echo "Tipo instalacion:"
  echo " 1) Maquina 1 (Servidor Web)"
  echo " 2) Maquina 2 (Servidor Aplicaciones)"
  read -p "Selecciona [1-2]: " machineType

  if [ "$machineType" = "1" ]; then
    echo ""
    echo "Servidor web a instalar:"
    echo " 1) NGINX"
    echo " 2) Apache"
    read -p "Selecciona [1-2]: " webServer
    echo ""

    read -p "IP privada de Maquina 2 (dejar vacio si aun no existe): " appServerIp
    echo ""

    echo "Configuracion:"
    echo "- Tipo: Maquina 1 (Web)"
    if [ "$webServer" = "1" ]; then
      echo "- Software: NGINX"
    else
      echo "- Software: Apache"
    fi
    echo "- IP Maquina 2: ${appServerIp:-pendiente}"

  elif [ "$machineType" = "2" ]; then
    echo ""
    echo "Servidor aplicaciones a instalar:"
    echo " 1) Tomcat"
    echo " 2) Glassfish"
    echo " 3) Payara"
    echo " 4) WildFly"
    read -p "Selecciona [1-4]: " appServer
    echo ""

    read -p "IP privada de Maquina 1: " webServerIp
    echo ""

    echo "Configuracion:"
    echo "- Tipo: Maquina 2 (Aplicaciones)"
    case "$appServer" in
      1) echo "- Software: Tomcat" ;;
      2) echo "- Software: Glassfish" ;;
      3) echo "- Software: Payara" ;;
      4) echo "- Software: WildFly" ;;
    esac
    echo "- IP Maquina 1: $webServerIp"

  else
    echo "Opcion invalida"
    exit 1
  fi

  echo ""
  read -p "Continuar? [s/N]: " confirm
  if [ "$confirm" != "s" ] && [ "$confirm" != "S" ]; then
    echo "Instalacion cancelada"
    exit 0
  fi
}

# Modo no interactivo (variables de entorno)
nonInteractiveMode() {
  if [ -n "$MACHINE_TYPE" ]; then
    machineType="$MACHINE_TYPE"
  fi
  if [ -n "$WEB_SERVER" ]; then
    webServer="$WEB_SERVER"
  fi
  if [ -n "$APP_SERVER" ]; then
    appServer="$APP_SERVER"
  fi
  if [ -n "$WEB_SERVER_IP" ]; then
    webServerIp="$WEB_SERVER_IP"
  fi
  if [ -n "$APP_SERVER_IP" ]; then
    appServerIp="$APP_SERVER_IP"
  fi
}

# Instalacion NGINX
installNginx() {
  echo ""
  echo "=== Instalando NGINX ==="
  apt update
  apt install nginx curl net-tools -y

  if [ -n "$appServerIp" ]; then
    echo "Configurando proxy inverso hacia $appServerIp..."
    cat > /etc/nginx/sites-available/app <<EOF
server {
    listen 80;
    server_name ${domainName};

    location / {
        proxy_pass http://${appServerIp}:8080/;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
    }
}
EOF
  else
    echo "IP Maquina 2 no proporcionada, creando configuracion basica..."
    cat > /etc/nginx/sites-available/app <<EOF
server {
    listen 80;
    server_name ${domainName};

    location / {
        proxy_pass http://CAMBIAR_IP_MAQUINA_2:8080/;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
    }
}
EOF
    echo "NOTA: Editar /etc/nginx/sites-available/app y cambiar CAMBIAR_IP_MAQUINA_2"
  fi

  ln -sf /etc/nginx/sites-available/app /etc/nginx/sites-enabled/app
  rm -f /etc/nginx/sites-enabled/default
  nginx -t
  systemctl enable nginx
  systemctl restart nginx
  echo "NGINX instalado y configurado"
}

# Instalacion Apache
installApache() {
  echo ""
  echo "=== Instalando Apache ==="
  apt update
  apt install apache2 curl net-tools -y
  a2enmod proxy proxy_http rewrite

  if [ -n "$appServerIp" ]; then
    echo "Configurando proxy inverso hacia $appServerIp..."
    cat > /etc/apache2/sites-available/app.conf <<EOF
<VirtualHost *:80>
    ServerName ${domainName}

    ProxyPreserveHost On
    ProxyRequests Off
    ProxyPass / http://${appServerIp}:8080/
    ProxyPassReverse / http://${appServerIp}:8080/

    ErrorLog \${APACHE_LOG_DIR}/app_error.log
    CustomLog \${APACHE_LOG_DIR}/app_access.log combined
</VirtualHost>
EOF
  else
    echo "IP Maquina 2 no proporcionada, creando configuracion basica..."
    cat > /etc/apache2/sites-available/app.conf <<EOF
<VirtualHost *:80>
    ServerName ${domainName}

    ProxyPreserveHost On
    ProxyRequests Off
    ProxyPass / http://CAMBIAR_IP_MAQUINA_2:8080/
    ProxyPassReverse / http://CAMBIAR_IP_MAQUINA_2:8080/

    ErrorLog \${APACHE_LOG_DIR}/app_error.log
    CustomLog \${APACHE_LOG_DIR}/app_access.log combined
</VirtualHost>
EOF
    echo "NOTA: Editar /etc/apache2/sites-available/app.conf y cambiar CAMBIAR_IP_MAQUINA_2"
  fi

  a2ensite app.conf
  a2dissite 000-default.conf
  apache2ctl configtest
  systemctl enable apache2
  systemctl restart apache2
  echo "Apache instalado y configurado"
}

# Instalacion Tomcat
installTomcat() {
  echo ""
  echo "=== Instalando Tomcat ==="
  apt update
  apt install default-jdk wget curl net-tools -y

  id -u tomcat &>/dev/null || {
    groupadd -r tomcat 2>/dev/null || true
    useradd -r -g tomcat -d /opt/tomcat -s /bin/false tomcat 2>/dev/null || true
  }

  cd /opt
  rm -rf tomcat apache-tomcat-* tomcat.tar.gz
  echo "Descargando Tomcat 10.1.34..."
  wget -O tomcat.tar.gz "https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.34/bin/apache-tomcat-10.1.34.tar.gz"
  tar -xf tomcat.tar.gz
  mv apache-tomcat-10.1.34 tomcat
  chown -R tomcat:tomcat /opt/tomcat
  chmod +x /opt/tomcat/bin/*.sh
  rm tomcat.tar.gz

  cat > /etc/systemd/system/tomcat.service <<EOF
[Unit]
Description=Apache Tomcat Web Application Container
After=network.target

[Service]
Type=forking

User=tomcat
Group=tomcat

Environment="JAVA_HOME=/usr/lib/jvm/default-java"
Environment="CATALINA_BASE=/opt/tomcat"
Environment="CATALINA_HOME=/opt/tomcat"
Environment="CATALINA_PID=/opt/tomcat/temp/tomcat.pid"
Environment="CATALINA_OPTS=-Xms512M -Xmx512M -server -XX:+UseParallelGC"
Environment="JAVA_OPTS=-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom"

ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/opt/tomcat/bin/shutdown.sh

Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF

  systemctl daemon-reload
  systemctl enable tomcat
  systemctl start tomcat

  echo "Tomcat instalado y arrancado"
}

# Instalacion Glassfish
installGlassfish() {
  echo ""
  echo "=== Instalando Glassfish ==="
  apt update
  apt install default-jdk wget unzip curl net-tools -y

  id -u glassfish &>/dev/null || {
    groupadd -r glassfish 2>/dev/null || true
    useradd -r -g glassfish -d /opt/glassfish -s /bin/false glassfish 2>/dev/null || true
  }

  cd /opt
  rm -rf glassfish glassfish7 glassfish.zip
  echo "Descargando Glassfish 7.0.20..."
  wget -O glassfish.zip "https://download.eclipse.org/ee4j/glassfish/glassfish-7.0.20.zip"
  unzip -q glassfish.zip
  mv glassfish7 glassfish
  chown -R glassfish:glassfish /opt/glassfish
  chmod +x /opt/glassfish/bin/*
  rm glassfish.zip

  cat > /etc/systemd/system/glassfish.service <<EOF
[Unit]
Description=Glassfish Application Server
After=network.target

[Service]
Type=forking
User=glassfish
Group=glassfish

ExecStart=/opt/glassfish/bin/asadmin start-domain domain1
ExecStop=/opt/glassfish/bin/asadmin stop-domain domain1

Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF

  systemctl daemon-reload
  systemctl enable glassfish
  systemctl start glassfish

  echo "Glassfish instalado y arrancado"
}

# Instalacion Payara
installPayara() {
  echo ""
  echo "=== Instalando Payara ==="
  apt update
  apt install default-jdk wget unzip curl net-tools -y

  id -u payara &>/dev/null || {
    groupadd -r payara 2>/dev/null || true
    useradd -r -g payara -d /opt/payara -s /bin/false payara 2>/dev/null || true
  }

  cd /opt
  rm -rf payara payara6 payara.zip
  echo "Descargando Payara 6.2025.1..."
  wget -O payara.zip "https://nexus.payara.fish/repository/payara-community/fish/payara/distributions/payara/6.2025.1/payara-6.2025.1.zip"
  unzip -q payara.zip
  mv payara6 payara
  chown -R payara:payara /opt/payara
  chmod +x /opt/payara/bin/*
  rm payara.zip

  cat > /etc/systemd/system/payara.service <<EOF
[Unit]
Description=Payara Application Server
After=network.target

[Service]
Type=forking
User=payara
Group=payara

ExecStart=/opt/payara/bin/asadmin start-domain domain1
ExecStop=/opt/payara/bin/asadmin stop-domain domain1

Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF

  systemctl daemon-reload
  systemctl enable payara
  systemctl start payara

  echo "Payara instalado y arrancado"
}

# Instalacion WildFly
installWildfly() {
  echo ""
  echo "=== Instalando WildFly ==="
  apt update
  apt install default-jdk wget curl net-tools -y

  id -u wildfly &>/dev/null || {
    groupadd -r wildfly 2>/dev/null || true
    useradd -r -g wildfly -d /opt/wildfly -s /bin/false wildfly 2>/dev/null || true
  }

  cd /opt
  rm -rf wildfly wildfly-* wildfly.tar.gz
  echo "Descargando WildFly 34.0.1..."
  wget -O wildfly.tar.gz "https://github.com/wildfly/wildfly/releases/download/34.0.1.Final/wildfly-34.0.1.Final.tar.gz"
  tar -xzf wildfly.tar.gz
  mv wildfly-34.0.1.Final wildfly
  chown -R wildfly:wildfly /opt/wildfly
  chmod +x /opt/wildfly/bin/*.sh
  rm wildfly.tar.gz

  cat > /etc/systemd/system/wildfly.service <<EOF
[Unit]
Description=WildFly Application Server
After=network.target

[Service]
Type=simple
User=wildfly
Group=wildfly

ExecStart=/opt/wildfly/bin/standalone.sh -b 0.0.0.0
ExecStop=/bin/kill -TERM \$MAINPID

Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF

  systemctl daemon-reload
  systemctl enable wildfly
  systemctl start wildfly

  echo "WildFly instalado y arrancado"
}

# Despliegue aplicacion Magic 8 Ball
deployMagic8Ball() {
  echo ""
  echo "=== Desplegando aplicacion Magic 8 Ball ==="

  deployMethod="$1"
  deployDir="$2"

  tmpAppDir="/tmp/magic8ball_app"
  rm -rf "$tmpAppDir"
  mkdir -p "$tmpAppDir/WEB-INF"

  cat > "${tmpAppDir}/index.jsp" <<'EOF'
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Magic 8 Ball - AWS</title>
</head>
<body>
    <h1>Magic 8 Ball</h1>
    <p>Tu respuesta:</p>
    <p>
    <%
        String[] answers = {
            "Si, definitivamente.",
            "Es cierto.",
            "Sin duda.",
            "Puedes confiar en ello.",
            "Muy probablemente.",
            "Mejor pregunta mas tarde.",
            "No puedo responder ahora.",
            "Las perspectivas no son buenas.",
            "Muy dudoso.",
            "Mi respuesta es no."
        };
        int idx = (int)(Math.random() * answers.length);
        out.println(answers[idx]);
    %>
    </p>
    <hr>
    <p>Desplegado en AWS - 2 DAW</p>
</body>
</html>
EOF

  if [ "$deployMethod" = "asadmin" ]; then
    cat > "${tmpAppDir}/WEB-INF/web.xml" <<'EOF'
<web-app>
    <display-name>magic8ball</display-name>
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>
</web-app>
EOF
  else
    cat > "${tmpAppDir}/WEB-INF/web.xml" <<'EOF'
<web-app>
    <display-name>magic8ball</display-name>
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>
</web-app>
EOF
  fi

  cd "${tmpAppDir}"
  jar cf magic8ball.war *

  if [ "$deployMethod" = "war" ]; then
    rm -rf "${deployDir}/magic8ball" "${deployDir}/magic8ball.war"
    cp magic8ball.war "${deployDir}/"
    chown tomcat:tomcat "${deployDir}/magic8ball.war"
    sleep 15
  elif [ "$deployMethod" = "asadmin" ]; then
    if [ -d /opt/glassfish ]; then
      sudo -u glassfish /opt/glassfish/bin/asadmin --host localhost --port 4848 deploy --force=true magic8ball.war
    elif [ -d /opt/payara ]; then
      sudo -u payara /opt/payara/bin/asadmin --host localhost --port 4848 deploy --force=true magic8ball.war
    fi
    sleep 5
  fi

  echo "Aplicacion Magic 8 Ball desplegada"
}

# Configuracion hosts
configureHosts() {
  if [ "$machineType" = "1" ] && [ -n "$appServerIp" ]; then
    grep -q "${appServerIp} app-server" /etc/hosts || echo "${appServerIp} app-server" >> /etc/hosts
  fi

  if [ "$machineType" = "2" ] && [ -n "$webServerIp" ]; then
    grep -q "${webServerIp} web-server" /etc/hosts || echo "${webServerIp} web-server" >> /etc/hosts
  fi
}

# Resumen final
showSummary() {
  echo ""
  echo "==================================================================="
  echo "Instalacion completada"
  echo "============================================================
