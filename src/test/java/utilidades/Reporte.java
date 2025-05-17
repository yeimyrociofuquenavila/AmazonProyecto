package utilidades;  // Declara el paquete 'utilidades' al que pertenece esta clase
// Clase Reporte: genera reportes visuales con ExtentReports

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class Reporte {  // Define la clase pública 'Reporte'

    private static ExtentReports extent;  // Declara una variable estática privada para la instancia de ExtentReports

    static {  // Bloque estático que se ejecuta cuando la clase es cargada por primera vez
        // Inicializa el reporter de tipo Spark (HTML moderno)
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/ExtentReports/index.html");  // Crea un nuevo reporter Spark que guardará el informe en la ruta especificada
        extent = new ExtentReports();  // Inicializa la instancia de ExtentReports
        extent.attachReporter(spark);  // Asocia el reporter Spark con la instancia de ExtentReports
    }

    public static ExtentTest createTest(String name) {  // Método estático público para crear un nuevo caso de prueba
        return extent.createTest(name);  // Crea y devuelve un nuevo caso de prueba con el nombre especificado
    }

    public static void logInfo(ExtentTest test, String message, String screenshotBase64) {  // Método para registrar información con posibilidad de adjuntar capturas de pantalla
        if (screenshotBase64 != null) {  // Verifica si se proporcionó una captura de pantalla en base64
            test.info(message,  // Registra el mensaje como información en el caso de prueba
                    com.aventstack.extentreports.MediaEntityBuilder  // Utiliza el constructor de entidades multimedia
                            .createScreenCaptureFromBase64String(screenshotBase64)  // Crea una captura de pantalla a partir de la cadena base64
                            .build()  // Construye la entidad multimedia
            );
        } else {  // Si no se proporcionó una captura de pantalla
            test.info(message);  // Registra solo el mensaje como información
        }
        extent.flush();  // Escribe los cambios en el archivo de reporte
    }

    // Método para registrar pasos exitosos
    public static void logPass(ExtentTest test, String message, String screenshotBase64) {  // Método para registrar pasos exitosos con posibilidad de adjuntar capturas
        if (screenshotBase64 != null) {  // Verifica si se proporcionó una captura de pantalla
            test.pass(message,  // Registra el mensaje como paso exitoso
                    com.aventstack.extentreports.MediaEntityBuilder  // Utiliza el constructor de entidades multimedia
                            .createScreenCaptureFromBase64String(screenshotBase64)  // Crea una captura a partir de la cadena base64
                            .build()  // Construye la entidad multimedia
            );
        } else {  // Si no se proporcionó una captura de pantalla
            test.pass(message);  // Registra solo el mensaje como paso exitoso
        }
        extent.flush();  // Escribe los cambios en el archivo de reporte
    }

    // Método para registrar pasos fallidos
    public static void logFail(ExtentTest test, String message, String screenshotBase64) {  // Método para registrar pasos fallidos con posibilidad de adjuntar capturas
        if (screenshotBase64 != null) {  // Verifica si se proporcionó una captura de pantalla
            test.fail(message,  // Registra el mensaje como paso fallido
                    com.aventstack.extentreports.MediaEntityBuilder  // Utiliza el constructor de entidades multimedia
                            .createScreenCaptureFromBase64String(screenshotBase64)  // Crea una captura a partir de la cadena base64
                            .build()  // Construye la entidad multimedia
            );
        } else {  // Si no se proporcionó una captura de pantalla
            test.fail(message);  // Registra solo el mensaje como paso fallido
        }
        extent.flush();  // Escribe los cambios en el archivo de reporte
    }

    // Método para registrar advertencias
    public static void logWarning(ExtentTest test, String message, String screenshotBase64) {  // Método para registrar advertencias con posibilidad de adjuntar capturas
        if (screenshotBase64 != null) {  // Verifica si se proporcionó una captura de pantalla
            test.warning(message,  // Registra el mensaje como advertencia
                    com.aventstack.extentreports.MediaEntityBuilder  // Utiliza el constructor de entidades multimedia
                            .createScreenCaptureFromBase64String(screenshotBase64)  // Crea una captura a partir de la cadena base64
                            .build()  // Construye la entidad multimedia
            );
        } else {  // Si no se proporcionó una captura de pantalla
            test.warning(message);  // Registra solo el mensaje como advertencia
        }
        extent.flush();  // Escribe los cambios en el archivo de reporte
    }

}