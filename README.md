# AmazonProyecto
Descripción general del proyecto de pruebas automatizadas para Amazon.

Este proyecto contiene pruebas automatizadas para Amazon utilizando Selenium WebDriver con un enfoque BDD (Behavior-Driven Development) implementado con Cucumber.

## Tecnologías Utilizadas

- java JDK version "21.0.7" 2025-04-15 LTS
- Java(TM) SE Runtime Environment (build 21.0.7+8-LTS-245)
- Java HotSpot(TM) 64-Bit Server VM (build 21.0.7+8-LTS-245, mixed mode, sharing)
- javac 21.0.7
- Maven  3.9.9
- Cucumber 7.14.0
- Selenium WebDriver 4.18.1
- ExtentReports 5.1.1
- JUnit 4.13.2
- WebDriverManager bonigarcia 5.6.3
- Logback 1.4.14
- IDE IntelliJ IDEA Community Edition 2025.1

## Estructura del Proyecto

El proyecto está configurado como una aplicación Maven estándar con las siguientes dependencias principales:

- **Cucumber**: Framework para la implementación de pruebas BDD
- **Selenium WebDriver**: Herramienta para la automatización de navegadores web
- **ExtentReports**: Biblioteca para generar informes de pruebas detallados
- **WebDriverManager**: Gestor automático de controladores de navegadores
- **Logback**: Sistema de registro de logs
- **Commons IO**: Biblioteca para operaciones de entrada/salida de archivos

## Requisitos Previos

Para ejecutar este proyecto, necesitas tener instalado:

1. JDK 21
2. Maven 3.6.0 o superior
3. Un navegador web compatible (Chrome, Firefox, Edge)

## Configuración del Entorno

1. Clona este repositorio
2. Asegúrate de tener configurado Java 21 en tu entorno
3. Ejecuta `mvn clean install` para descargar todas las dependencias

## Cómo ejecutar los tests
1. Ejecuta `mvn test` para Chrome
2. Ejecuta RunCucumberTest 
3. Ejecuta `mvn test -Dbrowser=firefox` Tener instalado firefox
4. Ejecuta `mvn test -Dbrowser=edge` Tener instalado edge
5. Ejecutar el archivo `ejecutar_pruebas.bat` desde Windows.
   

## Reportes

Después de la ejecución de las pruebas, los reportes HTML se generan en:

`/reports/cucumber/index.html`
Extent Reports
Los reportes incluyen:
- Capturas de pantalla de cada paso de la prueba
- Capturas de pantalla de fallos
- Información detallada de la ejecución

## Configuración de la Herramienta

El proyecto utiliza Maven para gestionar dependencias y construir la aplicación. La configuración principal se encuentra en el archivo `pom.xml`.

### Versiones Principales

- Java: 21
- Cucumber: 7.14.0
- ExtentReports: 5.1.1
- Selenium WebDriver: 4.18.1


AmazonProyecto
│── .idea
│── reports
│   └── cucumber
│       └── index.html
│── src
│   ├── main
│   │   └── java
│   │       └── utilidades
│   │           └── Driver
│   ├── test
│   │   └── java
│   │       ├── pages
│   │       │   └── PaginaAmazon
│   │       ├── runners
│   │       │   └── RunCucumberTest
│   │       ├── steps
│   │       │   └── PasosAmazon
│   │       ├── utilidades
│   │       │   ├── AgregarAlCarritoException
│   │       │   └── Reporte
│── resources
│   ├── features
│   │   ├── buscar_producto.feature
│   │   └── cucumber.properties
│── target
│── .gitignore
│── desktop.ini
│── ejecutar_pruebas.bat
│── pom.xml
│── README.md