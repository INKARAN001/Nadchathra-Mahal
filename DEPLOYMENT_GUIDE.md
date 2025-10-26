# Deployment Guide

This guide covers deploying the Hall Booking System to a production environment.

## 📋 Table of Contents

- [Pre-Deployment Checklist](#pre-deployment-checklist)
- [Production Configuration](#production-configuration)
- [Deployment Options](#deployment-options)
- [Database Setup](#database-setup)
- [Security Considerations](#security-considerations)
- [Monitoring and Maintenance](#monitoring-and-maintenance)

## ✅ Pre-Deployment Checklist

Before deploying to production, ensure:

- [ ] All tests pass
- [ ] Database is backed up
- [ ] Environment variables are configured
- [ ] SSL/TLS certificates are ready (if using HTTPS)
- [ ] Firewall rules are configured
- [ ] Monitoring tools are set up
- [ ] Logging is configured
- [ ] Security patches are applied

## ⚙️ Production Configuration

### 1. Create Production Properties File

Create `src/main/resources/application-prod.properties`:

```properties
# Production Server Configuration
server.port=8080

# Production Database Configuration
spring.datasource.url=jdbc:mysql://your-db-host:3306/hall_booking_db?useSSL=true&requireSSL=true&serverTimezone=UTC
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Connection Pool
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1200000

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=false

# Thymeleaf
spring.thymeleaf.cache=true
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html

# Logging
logging.level.root=INFO
logging.level.com.gsc=INFO
logging.level.org.springframework.security=WARN
logging.file.name=logs/application.log
logging.file.max-size=10MB
logging.file.max-history=30

# Security
security.require-ssl=true

# Session Management
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
server.servlet.session.timeout=30m
```

### 2. Environment Variables

Set these environment variables on your production server:

```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_USERNAME=your_username
export DB_PASSWORD=your_secure_password
```

## 🚀 Deployment Options

### Option 1: Standalone JAR Deployment

#### Step 1: Build JAR
```bash
mvn clean package -Pprod
```

#### Step 2: Upload JAR to Server
```bash
scp target/hall-booking-system-1.0.0.jar user@server:/opt/app/
```

#### Step 3: Create Systemd Service

Create `/etc/systemd/system/hall-booking.service`:

```ini
[Unit]
Description=Hall Booking System
After=network.target mysql.service

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/app
ExecStart=/usr/bin/java -jar /opt/app/hall-booking-system-1.0.0.jar
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal

Environment="SPRING_PROFILES_ACTIVE=prod"
Environment="DB_USERNAME=your_username"
Environment="DB_PASSWORD=your_password"

[Install]
WantedBy=multi-user.target
```

#### Step 4: Start Service
```bash
sudo systemctl daemon-reload
sudo systemctl enable hall-booking
sudo systemctl start hall-booking
sudo systemctl status hall-booking
```

### Option 2: Tomcat Deployment

#### Step 1: Build WAR File

Add to `pom.xml`:
```xml
<packaging>war</packaging>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>
```

#### Step 2: Build WAR
```bash
mvn clean package
```

#### Step 3: Deploy to Tomcat
```bash
cp target/hall-booking-system-1.0.0.war /opt/tomcat/webapps/
```

### Option 3: Docker Deployment

#### Step 1: Create Dockerfile

Create `Dockerfile` in project root:

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/hall-booking-system-1.0.0.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Step 2: Build Docker Image
```bash
docker build -t hall-booking-system:1.0.0 .
```

#### Step 3: Run Container
```bash
docker run -d \
  --name hall-booking \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_USERNAME=your_username \
  -e DB_PASSWORD=your_password \
  hall-booking-system:1.0.0
```

#### Step 4: Create docker-compose.yml

```yaml
version: '3.8'

services:
  app:
    image: hall-booking-system:1.0.0
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_USERNAME=${DB_USERNAME}
      - DB_PASSWORD=${DB_PASSWORD}
    depends_on:
      - db
    restart: always

  db:
    image: mysql:8.0
    environment:
      - MYSQL_DATABASE=hall_booking_db
      - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - MYSQL_USER=${DB_USERNAME}
      - MYSQL_PASSWORD=${DB_PASSWORD}
    volumes:
      - mysql_data:/var/lib/mysql
    restart: always

volumes:
  mysql_data:
```

### Option 4: Cloud Deployment

#### AWS Deployment (Elastic Beanstalk)

1. Install EB CLI:
```bash
pip install awsebcli
```

2. Initialize EB:
```bash
eb init -p java-17
```

3. Create environment:
```bash
eb create prod-env
```

4. Deploy:
```bash
eb deploy
```

#### Heroku Deployment

1. Create `Procfile`:
```
web: java -jar target/hall-booking-system-1.0.0.jar
```

2. Deploy:
```bash
heroku create your-app-name
heroku config:set SPRING_PROFILES_ACTIVE=prod
git push heroku main
```

## 🗄️ Database Setup

### Production Database Configuration

1. **Create Database:**
```sql
CREATE DATABASE hall_booking_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **Create User:**
```sql
CREATE USER 'hall_booking_user'@'%' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON hall_booking_db.* TO 'hall_booking_user'@'%';
FLUSH PRIVILEGES;
```

3. **Run Migration Scripts:**
```bash
mysql -u hall_booking_user -p hall_booking_db < database/recreate_database.sql
```

4. **Configure SSL:**
Update `application-prod.properties`:
```properties
spring.datasource.url=jdbc:mysql://host:3306/hall_booking_db?useSSL=true&requireSSL=true
```

### Database Backup Strategy

#### Automated Backups
```bash
# Create backup script
cat > /opt/scripts/backup_db.sh << 'EOF'
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
mysqldump -u hall_booking_user -p hall_booking_db > /opt/backups/hall_booking_$DATE.sql
find /opt/backups/ -name "*.sql" -mtime +7 -delete
EOF

chmod +x /opt/scripts/backup_db.sh
```

#### Schedule with Cron
```bash
# Backup daily at 2 AM
0 2 * * * /opt/scripts/backup_db.sh
```

## 🔒 Security Considerations

### 1. Update Default Passwords

Change default admin credentials:
```sql
USE hall_booking_db;
UPDATE users SET password = 'secure_hashed_password' WHERE email = 'admin@gsc.com';
```

### 2. Enable HTTPS

#### Using Nginx Reverse Proxy

`/etc/nginx/sites-available/hall-booking`:
```nginx
server {
    listen 443 ssl http2;
    server_name your-domain.com;

    ssl_certificate /etc/ssl/certs/your-cert.pem;
    ssl_certificate_key /etc/ssl/private/your-key.pem;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

#### Redirect HTTP to HTTPS
```nginx
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}
```

### 3. Firewall Configuration

```bash
# Allow only necessary ports
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw allow 22/tcp  # SSH
sudo ufw enable
```

### 4. Rate Limiting

Add to security configuration:
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    return http
        .authorizeHttpRequests(auth -> {
            auth.requestMatchers("/login").permitAll()
                 .anyRequest().authenticated();
        })
        .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        )
        .build();
}
```

## 📊 Monitoring and Maintenance

### Logging Configuration

```properties
# Application Logging
logging.level.com.gsc=INFO
logging.file.name=/var/log/hall-booking/application.log
logging.file.max-size=100MB
logging.file.max-history=60
```

### Health Check Endpoint

The application exposes actuator endpoints:
```properties
# Enable actuator
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=when-authorized
```

Access at: `http://your-server:8080/actuator/health`

### Performance Monitoring

```bash
# Check application logs
tail -f /var/log/hall-booking/application.log

# Check system resources
top
htop

# Check database connections
mysql -u root -p -e "SHOW PROCESSLIST;"
```

### Regular Maintenance Tasks

1. **Daily:**
   - Check application logs for errors
   - Monitor database backups

2. **Weekly:**
   - Review user registrations
   - Check disk space

3. **Monthly:**
   - Update dependencies
   - Review and rotate logs
   - Check security updates

## 🔄 Update and Rollback

### Update Procedure

```bash
# 1. Backup current version
cp hall-booking-system-1.0.0.jar hall-booking-system-1.0.0.jar.bak

# 2. Stop service
sudo systemctl stop hall-booking

# 3. Deploy new version
# (upload new JAR)

# 4. Start service
sudo systemctl start hall-booking

# 5. Verify
sudo systemctl status hall-booking
```

### Rollback Procedure

```bash
# 1. Stop service
sudo systemctl stop hall-booking

# 2. Restore previous version
mv hall-booking-system-1.0.0.jar hall-booking-system-1.0.0.jar.new
mv hall-booking-system-1.0.0.jar.bak hall-booking-system-1.0.0.jar

# 3. Start service
sudo systemctl start hall-booking
```

## 📞 Support

For deployment issues, contact the development team.

---

**Deployment Complete!** Your Hall Booking System is now running in production.

