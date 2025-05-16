package steps;

import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.es.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import pages.PaginaAmazon;
import utilidades.AgregarAlCarritoException;
import utilidades.Driver;
import utilidades.Reporte;

public class PasosAmazon {

    private WebDriver driver;
    private PaginaAmazon page;
    private ExtentTest test;
    private Scenario scenario;

    @Before
    public void setUp(Scenario scenario) {
        this.scenario = scenario;
        // Inicializa el driver antes de cada escenario
        driver = Driver.getDriver();
        // Crea la instancia de la página de Amazon
        page = new PaginaAmazon();
        // Crea un test en ExtentReports con el nombre del escenario
        test = Reporte.createTest("Prueba Amazon: " + scenario.getName());

        // Log del inicio del escenario
        Reporte.logInfo(test, "Iniciando escenario: " + scenario.getName(), null);
    }

    @Dado("que el usuario está en la página de Amazon")
    public void abrirAmazon() {
        try {
            // Intenta abrir la página de Amazon y maneja posibles errores
            page.abrirHome();
            // Registra el éxito en los reportes
            Reporte.logPass(test, "Home de Amazon abierta correctamente", takeScreenshot());
            // También registra en el reporte de Cucumber
            scenario.log("Home de Amazon abierta correctamente");
        } catch (Exception e) {
            // Registra el error en los reportes
            String mensaje = "Error al abrir la página de Amazon: " + e.getMessage();
            Reporte.logFail(test, mensaje, takeScreenshot());
            scenario.log(mensaje);
            scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES),
                    "image/png", "Error en abrirAmazon");
            // Relanza la excepción para que Cucumber sepa que el paso falló
            throw e;
        }
    }

    @Cuando("busca el artículo {string}")
    public void buscaArticulo(String producto) {
        try {
            // Intenta buscar el producto
            page.buscar(producto);
            // Registra el éxito
            Reporte.logPass(test, "Buscado: " + producto, takeScreenshot());
            scenario.log("Búsqueda exitosa: " + producto);
        } catch (Exception e) {
            // Registra el error
            String mensaje = "Error al buscar " + producto + ": " + e.getMessage();
            Reporte.logFail(test, mensaje, takeScreenshot());
            scenario.log(mensaje);
            scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES),
                    "image/png", "Error en buscaArticulo");
            throw e;
        }
    }

    @Y("navega a la segunda página de resultados")
    public void pagina2() {
        try {
            // Intenta navegar a la página 2
            page.irAPagina2();
            // Registra el éxito
            Reporte.logPass(test, "Navegación a página 2 exitosa", takeScreenshot());
            scenario.log("Navegación a página 2 exitosa");
        } catch (Exception e) {
            // Registra el error
            String mensaje = "Error al navegar a la página 2: " + e.getMessage();
            Reporte.logFail(test, mensaje, takeScreenshot());
            scenario.log(mensaje);
            scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES),
                    "image/png", "Error en pagina2");
            throw e;
        }
    }

    @Y("selecciona el tercer producto disponible")
    public void seleccionarTercer() {
        try {
            // Intenta seleccionar el tercer producto
            page.seleccionarTercerItem();
            // Registra el éxito
            Reporte.logPass(test, "Tercer ítem seleccionado", takeScreenshot());
            scenario.log("Tercer ítem seleccionado exitosamente");
        } catch (Exception e) {
            // Registra el error
            String mensaje = "Error al seleccionar el tercer ítem: " + e.getMessage();
            Reporte.logFail(test, mensaje, takeScreenshot());
            scenario.log(mensaje);
            scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES),
                    "image/png", "Error en seleccionarTercer");
            throw e;
        }
    }

    @Entonces("agrega al carrito más de dos unidades si está disponible")
    public void agregaCarrito() {
        try {
            // Intenta agregar al carrito dos unidades
            page.agregarAlCarrito(2);
            // Registra el éxito solo si llegamos aquí (sin excepciones)
            Reporte.logPass(test, "Producto agregado al carrito exitosamente", takeScreenshot());
            scenario.log("Producto agregado al carrito exitosamente");
        } catch (AgregarAlCarritoException e) {
            // Registra el error específico de agregar al carrito
            String mensaje = "Error al agregar al carrito: " + e.getMessage();
            Reporte.logFail(test, mensaje, takeScreenshot());
            scenario.log(mensaje);
            scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES),
                    "image/png", "Error en agregaCarrito");
            throw new RuntimeException(e); // Propagar como RuntimeException para Cucumber
        } catch (Exception e) {
            // Registra otros errores inesperados
            String mensaje = "Error inesperado: " + e.getMessage();
            Reporte.logFail(test, mensaje, takeScreenshot());
            scenario.log(mensaje);
            scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES),
                    "image/png", "Error inesperado en agregaCarrito");
            throw e;
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        // Registra el resultado final del escenario
        if (scenario.isFailed()) {
            Reporte.logFail(test, "Escenario fallido: " + scenario.getName(), takeScreenshot());
        } else {
            Reporte.logPass(test, "Escenario exitoso: " + scenario.getName(), takeScreenshot());
        }

        // Cierra el WebDriver después de finalizar el escenario
        Driver.quitDriver();
    }

    private String takeScreenshot() {
        try {
            // Captura la pantalla como array de bytes
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            // Codifica ese array a Base64 y devuelve la cadena
            return java.util.Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            System.out.println("Error al tomar captura de pantalla: " + e.getMessage());
            return null;
        }
    }
}
