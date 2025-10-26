# Configuration Guide

Complete guide for configuring the Hall Booking System application.

## 📋 Table of Contents

- [Application Properties](#application-properties)
- [Database Configuration](#database-configuration)
- [Security Configuration](#security-configuration)
- [Logging Configuration](#logging-configuration)
- [Environment Profiles](#environment-profiles)
- [Custom Configuration](#custom-configuration)

## ⚙️ Application Properties

### Server Configuration

Location: `src/main/resources/application.properties`

#### Basic Server Settings

```properties
# Server port
server.port=8081

# Server context path (optional)
server.servlet.context-path=/hall-booking

# Session timeout
server.servlet.session.timeout=30m

# Maximum HTTP header size
server.max-http-header-size=8KB
```

### Database Configuration

#### Basic Database Settings

```properties
# Database connection URL
spring.datasource.url=jdbc:mysql://localhost:3306/hall_booking_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC

# Database credentials
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

# JDBC driver
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Connection pool settings
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
```

#### Advanced Database Configuration

```properties
# Connection pool tuning
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.leak-detection-threshold=60000

# Validation query
spring.datasource.hikari.connection-test-query=SELECT 1
```

### JPA/Hibernate Configuration

```properties
# JPA/Hibernate settings
spring.jpa.hibernate.ddl-auto=update
# Options: none, validate, update, create, create-drop

# Show SQL queries in console
spring.jpa.show-sql=true

# Format SQL queries
spring.jpa.properties.hibernate.format_sql=true

# SQL dialect
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Generate statistics
spring.jpa.properties.hibernate.generate_statistics=false

# Batch size for inserts/updates
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

### Thymeleaf Configuration

```properties
# Template engine settings
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=HTML
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.servlet.content-type=text/html

# Enable template caching in production
# spring.thymeleaf.cache=true
```

## 🔒 Security Configuration

### Spring Security Settings

The security configuration is in `SecurityConfig.java`, but you can configure these properties:

```properties
# Session management
spring.session.store-type=jdbc
spring.session.jdbc.initialize-schema=always
spring.session.timeout=1800

# Password encoder settings
# BCrypt rounds (10 = default, increase for slower but more secure)
```

### Custom Security Configuration

File: `src/main/java/com/gsc/hallbooking/config/SecurityConfig.java`

Key settings to modify:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12); // Increase strength
}

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    // Configure authorization rules
    // Set session timeout
    // Configure CSRF protection
}
```

### CSRF Configuration

```properties
# CSRF token cookie settings
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.same-site=strict
```

## 📝 Logging Configuration

### Basic Logging

```properties
# Root logger level
logging.level.root=INFO

# Application specific logging
logging.level.com.gsc=DEBUG
logging.level.com.gsc.hallbooking=DEBUG

# Spring framework logging
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.web=INFO

# Database query logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### File Logging

```properties
# Log file location
logging.file.name=logs/application.log

# Log file size and rotation
logging.file.max-size=10MB
logging.file.max-history=30

# Log pattern
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.pattern.console=%d{HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

### Advanced Logging

```properties
# Class-specific logging
logging.level.org.springframework.boot=INFO
logging.level.org.apache.tomcat=INFO
logging.level.org.apache.catalina=WARN

# Log file directory
logging.file.path=/var/log/hall-booking

# Log format customization
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n
```

## 🌍 Environment Profiles

### Development Profile

Create: `src/main/resources/application-dev.properties`

```properties
# Development specific settings
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/hall_booking_db_dev?useSSL=false
spring.datasource.username=dev_user
spring.datasource.password=dev_password

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

logging.level.root=DEBUG
logging.level.com.gsc=DEBUG

spring.thymeleaf.cache=false
```

Activate:
```bash
java -jar app.jar --spring.profiles.active=dev
```

### Production Profile

Create: `src/main/resources/application-prod.properties`

```properties
# Production settings
server.port=8080

spring.datasource.url=jdbc:mysql://prod-db-host:3306/hall_booking_db?useSSL=true&requireSSL=true
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

logging.level.root=INFO
logging.file.name=/var/log/hall-booking/application.log
logging.file.max-size=100MB
logging.file.max-history=60

spring.thymeleaf.cache=true

# Security
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
```

Activate:
```bash
export SPRING_PROFILES_ACTIVE=prod
java -jar app.jar
```

### Test Profile

Create: `src/main/resources/application-test.properties`

```properties
# Test settings
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
```

## 🎛️ Custom Configuration

### Email Configuration (if adding email service)

```properties
# SMTP settings
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### File Upload Configuration

```properties
# File upload settings
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
spring.servlet.multipart.file-size-threshold=2KB
```

### Thymeleaf Template Configuration

Add in `application.properties`:

```properties
# Enable HTML5 mode
spring.thymeleaf.mode=HTML

# Enable template caching (disable in dev)
spring.thymeleaf.cache=false

# Additional dialects
# spring.thymeleaf.enabled=true
```

### API Configuration

```properties
# REST API settings
spring.web.resources.static-locations=classpath:/static/
spring.web.resources.cache-period=3600

# Jackson JSON settings
spring.jackson.serialization.write-dates-as-timestamps=false
spring.jackson.time-zone=UTC
```

## 🔧 Environment Variables

For sensitive configuration, use environment variables:

```bash
# Set environment variables
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export DB_HOST=your_db_host

# Or use .env file (with external library)
```

In `application.properties`:

```properties
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.url=jdbc:mysql://${DB_HOST}:3306/hall_booking_db
```

## 📊 Performance Tuning

### JVM Settings

```bash
# Optimize for production
java -Xms512m \
     -Xmx2048m \
     -XX:MetaspaceSize=256m \
     -XX:MaxMetaspaceSize=512m \
     -XX:+UseG1GC \
     -jar hall-booking-system-1.0.0.jar
```

### Connection Pool Optimization

```properties
# Tune connection pool for your load
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

### Caching Configuration

```properties
# Enable caching
spring.cache.type=simple
spring.cache.cache-names=halls,foods

# Or use Redis
# spring.cache.type=redis
# spring.redis.host=localhost
# spring.redis.port=6379
```

## 🔍 Monitoring Configuration

### Actuator (if added)

In `pom.xml`, add dependency:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

In `application.properties`:

```properties
# Enable actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
management.endpoints.web.base-path=/actuator
```

## 🚀 Quick Configuration References

### Change Port

```properties
server.port=8082
```

### Change Database Name

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/YOUR_DB_NAME?...
```

### Disable SQL Logging

```properties
spring.jpa.show-sql=false
```

### Enable Template Caching

```properties
spring.thymeleaf.cache=true
```

### Production Mode

```properties
spring.jpa.hibernate.ddl-auto=validate
spring.thymeleaf.cache=true
logging.level.root=INFO
```

## 🔄 Configuration Hot-Reload

### Development Mode

```properties
# Enable dev tools
spring.devtools.restart.enabled=true
spring.devtools.livereload.enabled=true
```

## 📞 Troubleshooting

### Configuration Not Loading

1. Check file path: `src/main/resources/application.properties`
2. Verify syntax
3. Restart application

### Database Connection Failed

1. Verify credentials
2. Check database server is running
3. Ensure database exists
4. Check firewall rules

### Port Already in Use

```bash
# Find process using port
netstat -ano | findstr :8081
# Kill process or change port in config
```

## 📚 Additional Resources

- [Spring Boot Configuration Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)
- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP)
- [Logback Configuration](http://logback.qos.ch/manual/configuration.html)

---

This configuration guide covers all aspects of customizing your Hall Booking System application.

