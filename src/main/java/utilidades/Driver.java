package utilidades;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
public class Driver {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();

    // Tiempo de espera aumentado a 20 segundos
    private static final int WAIT_TIMEOUT = 20;

    /**
     * Obtiene o crea una instancia de WebDriver para el hilo actual
     */
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            initDriver();
        }
        return driver.get();
    }

    /**
     * Obtiene o crea una instancia de WebDriverWait para el hilo actual
     */
    public static WebDriverWait getWait() {
        if (wait.get() == null && driver.get() != null) {
            wait.set(new WebDriverWait(driver.get(), Duration.ofSeconds(WAIT_TIMEOUT)));
        }
        return wait.get();
    }

    /**
     * Inicializa el WebDriver según el navegador configurado
     */
    private static void initDriver() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        WebDriver webDriver;

        switch (browser) {
            case "firefox":
                webDriver = setupFirefoxDriver();
                break;
            case "edge":
                webDriver = setupEdgeDriver();
                break;
            case "chrome":
            default:
                webDriver = setupChromeDriver();
                break;
        }

        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        webDriver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));

        driver.set(webDriver);
        wait.set(new WebDriverWait(webDriver, Duration.ofSeconds(WAIT_TIMEOUT)));
    }

    /**
     * Configura ChromeDriver con opciones optimizadas
     */
    private static WebDriver setupChromeDriver() {
        // Configurar ChromeDriver usando WebDriverManager
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // Configuraciones para evitar detección como bot
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");

        // User-Agent más realista
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.7103.49 Safari/537.36");

        // Evitar problemas con el sandbox (opcional, usar con precaución)
        // options.addArguments("--no-sandbox");
        // options.addArguments("--disable-dev-shm-usage");

        // Desactivar extensiones
        options.addArguments("--disable-extensions");

        // Usar perfil limpio
        // options.addArguments("--incognito");

        // Evitar mostrar el mensaje "Chrome está siendo controlado por software automatizado"
        options.setExperimentalOption("excludeSwitches", java.util.Arrays.asList("enable-automation"));

        return new ChromeDriver(options);
    }

    /**
     * Configura FirefoxDriver con opciones optimizadas
     */
    private static WebDriver setupFirefoxDriver() {
        // Configurar FirefoxDriver usando WebDriverManager
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-private");

        // User-Agent más realista
        options.addPreference("general.useragent.override",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:120.0) Gecko/20100101 Firefox/120.0");

        return new FirefoxDriver(options);
    }

    /**
     * Configura EdgeDriver
     */
    private static WebDriver setupEdgeDriver() {
        // Configurar EdgeDriver usando WebDriverManager
        WebDriverManager.edgedriver().setup();

        return new EdgeDriver();
    }

    /**
     * Cierra el WebDriver actual
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }

        if (wait.get() != null) {
            wait.remove();
        }
    }
}
