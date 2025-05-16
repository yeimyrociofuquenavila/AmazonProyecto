package runners;  // Define el paquete "runners" donde se encuentra esta clase.
// Clase RunCucumberTest: punto de entrada centralizado para la ejecución de pruebas

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)  // Le indica a JUnit que ejecute esta clase usando Cucumber como el ejecutor de las pruebas.
@CucumberOptions(  // Inicia la configuración de las opciones de Cucumber.
        features = "src/test/resources/features",  // Especifica la ubicación de los archivos ".feature" que contienen los escenarios de prueba.
        glue = {"steps", "utilidades"},  // Indica los paquetes donde se encuentran las implementaciones de los pasos (steps) y utilidades necesarias para las pruebas.
        plugin = {"pretty"}  // Activa el plugin "pretty" para generar una salida más legible en la consola durante la ejecución de las pruebas.
)
public class RunCucumberTest {  // Define la clase pública "RunCucumberTest", que se usará para ejecutar las pruebas.
}
