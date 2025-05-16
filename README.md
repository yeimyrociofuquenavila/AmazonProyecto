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

### 1.  Driver.java - Gestión de WebDriver

* **Propósito:** Centraliza la gestión de instancias de WebDriver para Chrome, Firefox y Edge, asegurando la seguridad de hilos y simplificando la configuración y el cierre de los navegadores.
* **Características Clave:**
    * Gestión de WebDriver y WebDriverWait segura para hilos (`ThreadLocal`).
    * Inicialización de navegadores configurada mediante la propiedad del sistema `browser`.
    * Configuración optimizada de WebDriver (maximización de la ventana, tiempos de espera, opciones para evitar la detección de bots).
    * Cierre adecuado del WebDriver (`quitDriver()`).
* **Uso:**
    * `Driver.getDriver()`:  Obtener la instancia de WebDriver.
    * `Driver.getWait()`: Obtener la instancia de WebDriverWait.
    * `-Dbrowser=chrome|firefox|edge`:  Establecer el navegador.
    * `Driver.quitDriver()`:  Cerrar el WebDriver.
* **Dependencias:** Selenium WebDriver, WebDriverManager.
* **Configuración:** Propiedad del sistema `browser`, constante `WAIT_TIMEOUT`.

### 2.  PaginaAmazon.java - Page Object de Amazon

* **Propósito:** Encapsula las interacciones con el sitio web de Amazon, proporcionando métodos para acciones comunes (buscar productos, navegar, seleccionar artículos, agregar al carrito).
* **Características Clave:**
    * Abstracción de las interacciones con la página.
    * Definición de localizadores de elementos (`By`).
    * Gestión de WebDriver y WebDriverWait.
    * Manejo de elementos dinámicos y popups.
    * Funcionalidad de búsqueda y navegación.
    * Lógica de selección de artículos.
    * Funcionalidad de agregar al carrito con reintentos y manejo de excepciones (`AgregarAlCarritoException`).
    * Manejo de errores y registros.
    * Captura de pantalla.
    * Desplazamiento (scrolling) y esperas (waits).

* **Dependencias:** Selenium WebDriver, `Driver.java`, `AgregarAlCarritoException.java`.
* **Configuración:** Configuración del WebDriver en la clase `Driver`.

### 3.  RunCucumberTest.java - Ejecutor de Pruebas de Cucumber

* **Propósito:** Configura y ejecuta las pruebas de Cucumber.
* **Características Clave:**
    * Ejecutor de pruebas de Cucumber (`@RunWith(Cucumber.class)`).
    * Configuración de Cucumber (`@CucumberOptions`):
        * `features`:  Ubicación de los archivos `.feature`.
        * `glue`:  Paquetes con definiciones de pasos y utilidades.
        * `plugin`:  Plugin "pretty" para una salida legible.
* **Uso:** Ejecutar esta clase como una prueba de JUnit.
* **Dependencias:** Cucumber JUnit, JUnit.
* **Configuración:** Anotación `@CucumberOptions`.

### 4.  PasosAmazon.java - Definiciones de Pasos de Cucumber

* **Propósito:** Implementa los pasos de prueba definidos en los archivos `.feature`, utilizando Selenium WebDriver y el Page Object `PaginaAmazon`.
* **Características Clave:**
    * Definiciones de pasos de Cucumber (`@Dado`, `@Cuando`, `@Y`, `@Entonces`).
    * Automatización de la interfaz de usuario con Selenium WebDriver.
    * Gestión del WebDriver con la clase `Driver`.
    * Generación de informes con ExtentReports.
    * Configuración y limpieza de escenarios (`@Before`, `@After`).
    * Manejo de errores y capturas de pantalla.
* **Uso:** Ejecutado automáticamente por Cucumber.
* **Dependencias:** Selenium WebDriver, Cucumber Java, ExtentReports, `PaginaAmazon.java`, `Driver.java`, `Reporte.java`, `AgregarAlCarritoException.java`.
* **Configuración:** WebDriver (`Driver`), informes (`Reporte`), mapeo de pasos (anotaciones de Cucumber).

### 5.  AgregarAlCarritoException.java - Excepción Personalizada

* **Propósito:** Define una excepción personalizada para errores específicos al agregar productos al carrito de compras.
* **Características Clave:**
    * Excepción personalizada (extiende `Exception`).
    * Constructores:
        * `AgregarAlCarritoException(String mensaje)`
        * `AgregarAlCarritoException(String mensaje, Throwable causa)`
* **Uso:** Señalar errores específicos en la funcionalidad de agregar al carrito.
* **Dependencias:** Java Core (clase `Exception`).
* **Configuración:** No requiere configuración.

### 6.  Reporte.java - Utilidades para Informes de Pruebas

* **Propósito:** Genera informes de pruebas detallados y visuales utilizando ExtentReports.
* **Características Clave:**
    * Generación de informes con ExtentReports.
    * Inicialización de ExtentReports y SparkReporter.
    * Creación de pruebas (`createTest()`).
    * Registro de información (`logInfo()`) y resultados de pasos (`logPass()`, `logFail()`, `logWarning()`).
    * Soporte para adjuntar capturas de pantalla.
    * Volcado inmediato de cambios al informe (`flush()`).
* **Uso:** Se utiliza en el código de automatización para generar informes de la ejecución.
* **Dependencias:** ExtentReports, ExtentReports Spark Reporter.
* **Configuración:** Ruta del archivo de informe en la inicialización de `ExtentSparkReporter`.

## Flujo de Ejecución

1.  La clase `RunCucumberTest` inicia la ejecución de las pruebas de Cucumber.
2.  Cucumber lee los archivos `.feature` y ejecuta los pasos definidos en `PasosAmazon.java`.
3.  `PasosAmazon.java` utiliza `PaginaAmazon.java` para interactuar con el sitio web de Amazon y `Driver.java` para gestionar el WebDriver.
4.  Durante la ejecución, se pueden lanzar excepciones `AgregarAlCarritoException.java` para manejar errores específicos.
5.  `Reporte.java` genera informes detallados de la ejecución de las pruebas.
