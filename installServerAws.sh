#!/bin/bash
# https://raw.githubusercontent.com/Fralopala2/Proyecto-FFCV/refs/heads/entrega_final/installServerAws.sh
# https://github.com/Fralopala2/DAW_AWS/blob/main/AWS/installServerAws.sh
# https://aules.edu.gva.es/fp/pluginfile.php/12834326/assignsubmission_file/submission_files/28366324/installServerAws.sh?forcedownload=1
# TCP Personalizado - TCP - 9990 - "Mi Ip"(Origen)
# SSH - TCP - 22 - 0.0.0.0
# HTPPS - TCP - 443 - 0.0.0.0
# HTTP - TCP - 80 - 0.0.0.0
# TCP Personalizado - TCP - 8080 - 0.0.0.0

set -e

echo "=== Script instalacion automatica AWS ==="
echo "Version: 3.0 - Febrero 2026"
echo ""

machineType=""
webServer=""
appServer=""
webServerIp=""
appServerIp=""
myPrivateIp=""
domainName="app.local"

detectPrivateIp() {
  myPrivateIp=$(hostname -I | awk '{print $1}')
  echo "IP privada detectada: $myPrivateIp"
}

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
  echo "Descargando Tomcat 10.1.35..."
  wget -O tomcat.tar.gz https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.35/bin/apache-tomcat-10.1.35.tar.gz
  tar -xf tomcat.tar.gz
  mv apache-tomcat-10.1.35 tomcat
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

deployTemperaturaApp() {
  echo ""
  echo "=== Desplegando aplicacion de conversion de temperaturas ==="

  deployMethod="$1"
  deployDir="$2"

  tmpAppDir="/tmp/temperatura_app"
  rm -rf "$tmpAppDir"
  mkdir -p "$tmpAppDir/WEB-INF"

  cat > "${tmpAppDir}/index.jsp" <<'EOF'
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Conversor de temperaturas - AWS 2 DAW</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            max-width: 500px;
            margin: 50px auto;
            padding: 30px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: #333;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
            border-radius: 15px;
        }
        h1 {
            text-align: center;
            color: white;
            margin-bottom: 30px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }
        form {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        label {
            display: block;
            margin-bottom: 10px;
            font-weight: bold;
            color: #555;
        }
        input[type="number"] {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 18px;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }
        input[type="number"]:focus {
            border-color: #667eea;
            outline: none;
        }
        select {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 16px;
            margin-bottom: 20px;
            background: white;
        }
        button {
            width: 100%;
            padding: 15px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 18px;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.3s;
        }
        button:hover {
            background: #5a67d8;
        }
        .result {
            margin-top: 20px;
            padding: 15px;
            background: #f0f9ff;
            border: 2px solid #0ea5e9;
            border-radius: 8px;
            text-align: center;
            font-size: 20px;
            font-weight: bold;
            color: #0369a1;
        }
        footer {
            text-align: center;
            margin-top: 30px;
            color: white;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <h1>Conversor de temperaturas</h1>
    
    <form method="POST">
        <label for="valor">Valor:</label>
        <input type="number" id="valor" name="valor" step="0.01" required autofocus>
        
        <label for="unidad">De:</label>
        <select id="unidad" name="unidad">
            <option value="C">Celsius (C)</option>
            <option value="F">Fahrenheit (F)</option>
        </select>
        
        <button type="submit">Convertir</button>
    </form>
    
    <%
        String valorStr = request.getParameter("valor");
        String unidad = request.getParameter("unidad");
        if (valorStr != null && unidad != null) {
            try {
                double valor = Double.parseDouble(valorStr);
                double resultado;
                String unidadResultado;
                
                if ("C".equals(unidad)) {
                    resultado = (valor * 9.0 / 5.0) + 32;
                    unidadResultado = "F";
                } else {
                    resultado = (valor - 32) * 5.0 / 9.0;
                    unidadResultado = "C";
                }
    %>
                <div class="result">
                    <%= String.format("%.2f %s = %.2f %s", valor, unidad.equals("C") ? "C" : "F", resultado, unidadResultado) %>
                </div>
    <%
            } catch (NumberFormatException e) {
    %>
                <div class="result" style="background: #fee2e2; border-color: #ef4444; color: #dc2626;">
                    Error: introduce un numero valido.
                </div>
    <%
            }
        }
    %>
    
    <footer>Desplegado en AWS - Proyecto 2.DAW por: Paco Lopez Alarte</footer>
</body>
</html>
EOF

  cat > "${tmpAppDir}/WEB-INF/web.xml" <<'EOF'
<web-app>
    <display-name>temperatura</display-name>
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>
</web-app>
EOF

  cd "${tmpAppDir}"
  jar cf temperatura.war *

  if [ "$deployMethod" = "war" ]; then
    rm -rf "${deployDir}/temperatura" "${deployDir}/temperatura.war"
    cp temperatura.war "${deployDir}/"
    # sin cambio de propietario explicito para que funcione en Tomcat o WildFly
    sleep 15
  elif [ "$deployMethod" = "asadmin" ]; then
    if [ -d /opt/glassfish ]; then
      sudo -u glassfish /opt/glassfish/bin/asadmin --host localhost --port 4848 deploy --force=true temperatura.war
    elif [ -d /opt/payara ]; then
      sudo -u payara /opt/payara/bin/asadmin --host localhost --port 4848 deploy --force=true temperatura.war
    fi
    sleep 5
  fi

  echo "Aplicacion de temperaturas desplegada"
}


configureHosts() {
  if [ "$machineType" = "1" ] && [ -n "$appServerIp" ]; then
    grep -q "${appServerIp} app-server" /etc/hosts || echo "${appServerIp} app-server" >> /etc/hosts
  fi

  if [ "$machineType" = "2" ] && [ -n "$webServerIp" ]; then
    grep -q "${webServerIp} web-server" /etc/hosts || echo "${webServerIp} web-server" >> /etc/hosts
  fi
}

showSummary() {
  echo ""
  echo "==================================================================="
  echo "Instalacion completada"
  echo "==================================================================="

  if [ "$machineType" = "1" ]; then
    echo "Tipo: Maquina 1 (Servidor Web)"
    if [ "$webServer" = "1" ]; then
      echo "Software: NGINX"
      echo ""
      echo "Archivos configuracion:"
      echo " - /etc/nginx/sites-available/app"
      echo ""
      echo "Comandos utiles:"
      echo " sudo systemctl status nginx"
      echo " sudo nginx -t"
      echo " sudo systemctl reload nginx"
      echo " sudo tail -f /var/log/nginx/access.log"
    else
      echo "Software: Apache"
      echo ""
      echo "Archivos configuracion:"
      echo " - /etc/apache2/sites-available/app.conf"
      echo ""
      echo "Comandos utiles:"
      echo " sudo systemctl status apache2"
      echo " sudo apache2ctl configtest"
      echo " sudo systemctl reload apache2"
      echo " sudo tail -f /var/log/apache2/access.log"
    fi

    echo ""
    if [ -n "$appServerIp" ]; then
      echo "Proxy configurado hacia: $appServerIp:8080"
    else
      echo "ATENCION: Configurar IP de Maquina 2 manualmente"
    fi

    echo ""
    echo "Acceso:"
	echo "  http://${domainName}/temperatura/"

  elif [ "$machineType" = "2" ]; then
    echo "Tipo: Maquina 2 (Servidor Aplicaciones)"
    echo "IP privada: $myPrivateIp"
    case "$appServer" in
      1)
        echo "Software: Tomcat"
        echo ""
        echo "Directorios:"
        echo " - /opt/tomcat"
        echo " - /opt/tomcat/webapps"
        echo ""
        echo "Comandos utiles:"
        echo " sudo systemctl status tomcat"
        echo " sudo systemctl restart tomcat"
        echo " sudo tail -f /opt/tomcat/logs/catalina.out"
        ;;
      2)
        echo "Software: Glassfish"
        echo ""
        echo "Directorios:"
        echo " - /opt/glassfish"
        echo ""
        echo "Comandos utiles:"
        echo " sudo systemctl status glassfish"
        echo " sudo -u glassfish /opt/glassfish/bin/asadmin list-applications"
        echo " sudo tail -f /opt/glassfish/glassfish/domains/domain1/logs/server.log"
        echo ""
        echo "Consola admin: http://${myPrivateIp}:4848"
        ;;
      3)
        echo "Software: Payara"
        echo ""
        echo "Directorios:"
        echo " - /opt/payara"
        echo ""
        echo "Comandos utiles:"
        echo " sudo systemctl status payara"
        echo " sudo -u payara /opt/payara/bin/asadmin list-applications"
        echo " sudo tail -f /opt/payara/glassfish/domains/domain1/logs/server.log"
        echo ""
        echo "Consola admin: http://${myPrivateIp}:4848"
        ;;
      4)
        echo "Software: WildFly"
        echo ""
        echo "Directorios:"
        echo " - /opt/wildfly"
        echo " - /opt/wildfly/standalone/deployments"
        echo ""
        echo "Comandos utiles:"
        echo " sudo systemctl status wildfly"
        echo " sudo systemctl restart wildfly"
        echo " sudo tail -f /opt/wildfly/standalone/log/server.log"
        echo ""
        echo "Consola admin: http://${myPrivateIp}:9990"
        ;;
    esac

    echo ""
    echo "Aplicacion desplegada: temperatura"
	echo "Prueba local: curl http://localhost:8080/temperatura/"
	echo "Desde Maquina 1: curl http://${myPrivateIp}:8080/temperatura/"
  fi

  echo "==================================================================="
}

main() {
  if [ "$EUID" -ne 0 ]; then
    echo "Este script debe ejecutarse como root (sudo)"
    exit 1
  fi

  detectPrivateIp

  if [ -n "$MACHINE_TYPE" ] || [ -n "$WEB_SERVER" ] || [ -n "$APP_SERVER" ]; then
    nonInteractiveMode
  else
    interactiveMode
  fi

  if [ "$machineType" = "1" ]; then
    if [ "$webServer" = "1" ]; then
      installNginx
    elif [ "$webServer" = "2" ]; then
      installApache
    fi
	elif [ "$machineType" = "2" ]; then
	  case "$appServer" in
		1)
		  installTomcat
		  deployTemperaturaApp "war" "/opt/tomcat/webapps"
		  ;;
		2)
		  installGlassfish
		  deployTemperaturaApp "asadmin" ""
		  ;;
		3)
		  installPayara
		  deployTemperaturaApp "asadmin" ""
		  ;;
		4)
		  installWildfly
		  deployTemperaturaApp "war" "/opt/wildfly/standalone/deployments"
		  systemctl restart wildfly
		  ;;
	  esac
	fi

  configureHosts
  showSummary
}

main

