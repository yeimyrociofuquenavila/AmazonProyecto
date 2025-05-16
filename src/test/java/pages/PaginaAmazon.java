package pages;
// Clase PaginaAmazon: encapsula la interacción con la interfaz web (Page Object)

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilidades.AgregarAlCarritoException;
import utilidades.Driver;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class PaginaAmazon { // Define la clase PaginaAmazon, que representa una página de Amazon en la automatización

    private WebDriver driver; // Controlador principal que permite interactuar con el navegador web

    private WebDriverWait esperar; // Permite realizar esperas explícitas hasta que se cumplan ciertas condiciones


    // Localizador privado del cuadro de búsqueda de Amazon, identificado por su atributo ID
    private By CuadroBusqueda = By.id("twotabsearchtextbox"); // Cuadro donde se escribe lo que se quiere buscar en Amazon
    private By Botonbusqueda = By.id("nav-search-submit-button"); // Botón que se presiona para iniciar la búsqueda
    private By Pagina2 = By.xpath("//a[contains(@class, 's-pagination-item') and contains(text(), '2')]"); // Botón para ir a la segunda página de resultados
    private By resultados = By.cssSelector("div[data-component-type='s-search-result'], div.s-result-item:not(.AdHolder)"); // Lista de los resultados de búsqueda (excluye anuncios)
    private By seleccióndecantidad = By.id("quantity"); // Selector para elegir la cantidad del producto que se desea comprar
    private By botónAgregarAlCarrito = By.id("add-to-cart-button"); // Botón que se presiona para agregar el producto al carrito de compras
    private By títuloDelProducto = By.cssSelector("h1#title, span#productTitle"); // Título del producto en la página de detalles


    // Elementos para manejar popups y diálogos
    private By aceptarCookies = By.cssSelector("#sp-cc-accept, .a-button-close, [data-action='a-popover-close']"); // Botón para aceptar cookies o cerrar ventanas emergentes
    private By logoDeNavegaciónDelSitio = By.id("nav-logo-sprites"); // Logo de Amazon, que normalmente redirige a la página principal
    private By confirmaciónDelCarrito = By.cssSelector("div#sw-ptc-container, #huc-v2-order-row-confirm-text, #attach-accessory-pane"); // Elemento que confirma que el producto fue agregado al carrito


    public PaginaAmazon() {
        this.driver = Driver.getDriver(); // Se obtiene una instancia del navegador desde la clase Driver
        this.esperar = Driver.getWait(); // Se obtiene una instancia de WebDriverWait para manejar esperas explícitas
    }

    /**
     * Espera aleatoria para simular comportamiento humano
     */
    private void esperaAleatoria() {
        try {
            Thread.sleep((long) (Math.random() * 2000 + 1000)); // Pausa la ejecución entre 1 y 3 segundos para simular un comportamiento humano
        } catch (InterruptedException e) {
            e.printStackTrace(); // Imprime el error si la espera es interrumpida
        }
    }


    /**
     * Maneja diálogos emergentes como cookies o inicios de sesión
     */
    private void manejarDialogos() {
        try {
            // Buscar todos los diálogos que tengan los selectores CSS correspondientes
            List<WebElement> dialogos = driver.findElements(
                    By.cssSelector("#sp-cc, .a-popover-visible, .a-modal-active")
            );

            // Iterar sobre cada diálogo encontrado
            for (WebElement dialogo : dialogos) {
                // Verificar si el diálogo está visible en la pantalla
                if (dialogo.isDisplayed()) {
                    // Buscar el botón para aceptar cookies dentro del diálogo
                    List<WebElement> botones = dialogo.findElements(aceptarCookies);
                    // Verificar si el botón de aceptar cookies está disponible y visible
                    if (!botones.isEmpty() && botones.get(0).isDisplayed()) {
                        // Hacer clic en el botón de aceptar cookies
                        botones.get(0).click();
                        // Esperar aleatoriamente entre 1 y 3 segundos
                        esperaAleatoria();
                    }
                }
            }
        } catch (Exception e) {
            // Si ocurre algún error (por ejemplo, no hay diálogos o no se pueden cerrar), se ignora
            System.out.println("No se encontraron diálogos para cerrar o no pudieron cerrarse");
        }
    }


    /**
     * Scroll hasta un elemento para asegurarse que sea visible
     */
    private void scrollToElement(WebElement element) {
        try {
            // Ejecutar un script de JavaScript para hacer scroll hasta el elemento especificado
            ((JavascriptExecutor) driver).executeScript(
                    // Desplazar el elemento hasta el centro de la vista con un comportamiento suave
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element
            );
            // Esperar un tiempo aleatorio entre 1 y 3 segundos después de hacer el scroll
            esperaAleatoria();
        } catch (Exception e) {
            // Si ocurre un error, imprimir un mensaje de error en la consola
            System.out.println("No se pudo hacer scroll: " + e.getMessage());
        }
    }

    /**
     * Abre la página principal de Amazon con manejo de errores mejorado
     */
    public void abrirHome() {
        try {
            // Abre la página principal de Amazon utilizando la URL proporcionada
            driver.get("https://www.amazon.com/");

            // Intentar esperar por el logo de Amazon
            boolean logoCargado = esperarPorLogo();

            // Si no se carga el logo, volver a intentar cargar la página
            int intentos = 0;
            while (!logoCargado && intentos < 3) {  // Intentar hasta 3 veces
                System.out.println("Logo no encontrado. Intentando recargar la página...");
                driver.get("https://www.amazon.com/");  // Recargar la página
                logoCargado = esperarPorLogo();  // Esperar nuevamente por el logo
                intentos++;
            }

            if (!logoCargado) {
                // Manejo de error si no se carga el logo después de varios intentos
                System.out.println("No se pudo cargar el logo después de varios intentos");
                return; // Se detiene la ejecución del método
            }

            // Maneja cualquier posible diálogo emergente (como ventanas de cookies o pop-ups) que aparezca en la página
            manejarDialogos();

            // Espera hasta que la caja de búsqueda (donde se pueden ingresar términos de búsqueda) sea visible
            esperar.until(ExpectedConditions.visibilityOfElementLocated(CuadroBusqueda));

            // Imprime un mensaje en la consola indicando que la página de Amazon se ha cargado correctamente
            System.out.println("Página de Amazon cargada correctamente");
        } catch (Exception e) {
            // Si ocurre algún error durante la ejecución (por ejemplo, no se encuentra un elemento), se imprime el mensaje de error
            System.out.println("Error al abrir la página de Amazon: " + e.getMessage());
            // Relanza la excepción para que pueda ser reportada por Cucumber u otras herramientas de prueba
            throw e;
        }
    }

    // Método auxiliar para esperar el logo
    private boolean esperarPorLogo() {
        try {
            // Espera por el logo de Amazon hasta que sea visible
            esperar.until(ExpectedConditions.visibilityOfElementLocated(logoDeNavegaciónDelSitio));
            return true;  // Si se carga el logo, retorna true
        } catch (Exception e) {
            return false;  // Si no se encuentra el logo, retorna false
        }
    }


    /**
     * Busca un producto con manejo de errores y comportamiento más humano
     */
    public void buscar(String producto) {
        try {
            // Espera a que la caja de búsqueda sea clickeable, lo cual asegura que el elemento esté interactuable
            WebElement caja = esperar.until(ExpectedConditions.elementToBeClickable(CuadroBusqueda));

            // Limpia el contenido actual de la caja de búsqueda y espera un momento para simular una pausa natural
            caja.clear();
            esperaAleatoria();

            // Envía el texto letra por letra para simular una escritura humana
            try {
                for (char c : producto.toCharArray()) {
                    // Convierte cada carácter en un String y lo escribe en la caja de búsqueda
                    caja.sendKeys(String.valueOf(c));
                    // Hace una pausa aleatoria entre cada letra, para simular el tiempo que tomaría escribir
                    Thread.sleep((long) (Math.random() * 150)); // Pausa entre 0 y 150 ms
                }
            } catch (InterruptedException e) {
                // Si ocurre un error durante la pausa, lo captura y lo imprime en consola
                System.out.println("Error durante la escritura pausada: " + e.getMessage());
            }

            // Después de escribir el texto, hace otra pequeña espera aleatoria
            esperaAleatoria();

            // Espera a que el botón de búsqueda sea clickeable antes de hacer clic en él
            WebElement botonBusqueda = esperar.until(ExpectedConditions.elementToBeClickable(Botonbusqueda));
            botonBusqueda.click();

            // Espera hasta que los resultados de la búsqueda sean visibles en la página
            esperar.until(ExpectedConditions.presenceOfElementLocated(resultados));
            System.out.println("Búsqueda realizada correctamente: " + producto);

        } catch (Exception e) {
            // Si ocurre algún error en el proceso de búsqueda, lo imprime en consola y relanza la excepción
            System.out.println("Error al buscar producto: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Navega a la segunda página con mejor manejo de errores.
     */
    public void irAPagina2() {
        try {
            // Busca el enlace a la página 2 con un selector CSS más robusto
            List<WebElement> paginadores = driver.findElements(Pagina2);

            // Si no encuentra el enlace de la página 2 exacto, busca cualquier paginador y selecciona el segundo
            if (paginadores.isEmpty()) {
                // Si no se encuentra el enlace exacto, imprime un mensaje indicando que se está buscando una alternativa
                System.out.println("No se encontró el paginador exacto, buscando alternativas...");
                paginadores = driver.findElements(By.cssSelector(".s-pagination-item"));

                // Verifica si encontró al menos 2 elementos de paginación
                if (paginadores.size() >= 2) {
                    // Si es así, selecciona el segundo elemento (que corresponde a la página 2)
                    WebElement pagina = paginadores.get(1); // El segundo elemento debería ser la página 2
                    // Desplaza la vista hasta el elemento antes de hacer clic en él
                    scrollToElement(pagina);
                    // Hace clic en la página 2
                    pagina.click();
                } else {
                    // Si no se encuentra el paginador o la segunda página, lanza una excepción
                    throw new RuntimeException("No se encontró ningún paginador");
                }
            } else {
                // Si encuentra el enlace exacto a la página 2, lo selecciona directamente
                WebElement pagina = paginadores.get(0);
                // Desplaza la vista hasta el enlace de la página 2 antes de hacer clic
                scrollToElement(pagina);
                // Hace clic en el enlace de la página 2
                pagina.click();
            }

            // Espera hasta que los resultados sean visibles en la nueva página
            esperar.until(ExpectedConditions.presenceOfElementLocated(resultados));
            System.out.println("Navegación a página 2 exitosa");

        } catch (Exception e) {
            // Si ocurre algún error al intentar navegar, lo imprime en consola y relanza la excepción
            System.out.println("Error al navegar a la página 2: " + e.getMessage());
            throw e;
        }
    }


    /**
     * Selecciona el tercer ítem con mejor manejo de errores y casos extremos
     */
    public void seleccionarTercerItem() {
        try {
            // Espera a que los resultados de búsqueda estén presentes
            esperar.until(ExpectedConditions.presenceOfElementLocated(resultados));

            // Pausa corta para asegurar carga inicial
            try {
                Thread.sleep(1000); // Reducido a 1 segundo
            } catch (InterruptedException e) {
                // Ignorar interrupción
            }

            // Obtener resultados de búsqueda visibles
            List<WebElement> items = driver.findElements(resultados);

            // Limitar a 10 elementos para optimizar
            int maxItems = Math.min(items.size(), 10);
            if (maxItems > 0) {
                items = items.subList(0, maxItems);
            }

            // Verificar que tengamos suficientes resultados y mostrar cuántos hay
            if (items.size() < 3) {
                System.out.println("Advertencia: Solo se encontraron " + items.size() + " resultados");
                throw new RuntimeException("No hay suficientes resultados para seleccionar el tercero");
            }

            System.out.println("Número total de resultados (limitado a 10): " + items.size());

            // Para depuración: Listar los elementos encontrados (máximo 10)
            System.out.println("=== LISTADO DE ELEMENTOS ENCONTRADOS (MÁXIMO 10) ===");
            for (int i = 0; i < items.size(); i++) {
                try {
                    WebElement item = items.get(i);
                    String title = "No disponible";
                    try {
                        // Intentar obtener el título del producto con selector simplificado
                        List<WebElement> titleElems = item.findElements(
                                By.cssSelector("h2, .a-text-normal")
                        );
                        if (!titleElems.isEmpty()) {
                            title = titleElems.get(0).getText();
                        }
                    } catch (Exception e) {
                        // Si no se puede obtener el título, usar atributo aria-label
                        try {
                            title = item.getAttribute("aria-label");
                        } catch (Exception e2) {
                            // Ignorar si tampoco funciona
                        }
                    }
                    System.out.println("Ítem #" + (i+1) + ": " + title);
                } catch (Exception e) {
                    System.out.println("Error al leer información del ítem #" + (i+1));
                }
            }

            // Filtrar solo ítems que sean productos reales (limitado a 10)
            List<WebElement> productosReales = new java.util.ArrayList<>();
            int contadorReales = 0;

            for (WebElement item : items) {
                // Verificar si este item parece un producto real (tiene título y precio)
                boolean esProductoReal = false;

                try {
                    // Verificación simplificada
                    boolean tieneEnlace = !item.findElements(By.tagName("a")).isEmpty();
                    boolean tieneTitulo = !item.findElements(By.cssSelector("h2, .a-text-normal")).isEmpty();

                    // Si tiene ambos elementos, considerarlo un producto real
                    esProductoReal = tieneEnlace && tieneTitulo;
                } catch (Exception e) {
                    // Verificación alternativa por atributo
                    try {
                        esProductoReal = item.getAttribute("data-component-type").equals("s-search-result");
                    } catch (Exception e2) {
                        // No hacer nada si esto también falla
                    }
                }

                // Si parece un producto real, añadirlo a nuestra lista filtrada
                if (esProductoReal) {
                    productosReales.add(item);
                    contadorReales++;
                    System.out.println("Añadido a productos reales: " + contadorReales);

                    // Limitar a 10 productos reales para optimizar
                    if (contadorReales >= 10) {
                        break;
                    }
                }
            }

            System.out.println("Productos reales identificados: " + productosReales.size());

            // Verificar si tenemos suficientes productos reales
            if (productosReales.size() < 3) {
                System.out.println("ADVERTENCIA: No hay suficientes productos reales. Usando lista original.");
                productosReales = items; // Volver a la lista original si no hay suficientes productos filtrados
            }

            // SELECCIÓN DEL TERCER PRODUCTO (índice 2)
            WebElement tercerItem = productosReales.get(2);

            String itemText = "No disponible";
            try {
                // Intentar obtener el texto simplificado
                List<WebElement> titleElems = tercerItem.findElements(By.cssSelector("h2, .a-text-normal"));
                if (!titleElems.isEmpty()) {
                    itemText = titleElems.get(0).getText();
                }
            } catch (Exception e) {
                // Si no se puede obtener el texto, usar aria-label
                try {
                    itemText = tercerItem.getAttribute("aria-label");
                } catch (Exception e2) {
                    System.out.println("No se pudo obtener ningún texto del tercer ítem");
                }
            }

            System.out.println("=== SELECCIONANDO TERCER ÍTEM ===");
            System.out.println("Texto/título del tercer ítem seleccionado: " + itemText);

            // Tomar screenshot para verificación
            try {
                tomarCapturaPantalla("antes_de_clic_tercer_item");
            } catch (Exception e) {
                System.out.println("No se pudo tomar captura de pantalla: " + e.getMessage());
            }

            // Scroll al elemento con JavaScript - asegura que esté visible
            try {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});", tercerItem);

                // Añadir destaque visual para confirmar selección
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].style.border='3px solid red';", tercerItem);

                // Esperar brevemente
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println("Error durante el scroll: " + e.getMessage());
            }

            // Buscar el elemento clickeable dentro del tercer ítem
            WebElement enlace = null;
            try {
                // Selector simplificado para enlaces
                List<WebElement> posiblesEnlaces = tercerItem.findElements(By.cssSelector("h2 a, a.a-link-normal"));
                if (!posiblesEnlaces.isEmpty()) {
                    enlace = posiblesEnlaces.get(0);
                } else {
                    enlace = tercerItem.findElement(By.tagName("a"));
                }
            } catch (Exception e) {
                System.out.println("Error buscando el enlace: " + e.getMessage());
                throw new RuntimeException("No se pudo encontrar un enlace clickeable");
            }

            // Guardar referencia del URL actual para verificar cambio
            String urlAntes = driver.getCurrentUrl();

            // CLIC EN EL ENLACE
            try {
                // Intento con clic básico
                enlace.click();
                System.out.println("Clic normal realizado");
            } catch (Exception e) {
                // Si falla, usar JavaScript
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", enlace);
                    System.out.println("Clic con JavaScript realizado");
                } catch (Exception e2) {
                    throw new RuntimeException("No se pudo hacer clic en el enlace");
                }
            }

            System.out.println("CLIC REALIZADO EN EL TERCER ÍTEM (índice 2) DE LA LISTA");

            // Esperar a que cambie la página - selector simplificado
            try {
                // Verificar cambio de pestañas
                if (driver.getWindowHandles().size() > 1) {
                    String currentHandle = driver.getWindowHandle();
                    for (String handle : driver.getWindowHandles()) {
                        if (!handle.equals(currentHandle)) {
                            driver.switchTo().window(handle);
                            break;
                        }
                    }
                }

                // Esperar elementos de página de producto
                esperar.until(ExpectedConditions.or(
                        ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")),
                        ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart-button"))
                ));

                System.out.println("Página de producto cargada correctamente");

                // Obtener título del producto para confirmar
                try {
                    WebElement titulo = driver.findElement(By.id("productTitle"));
                    System.out.println("Título del producto: " + titulo.getText());
                } catch (Exception e) {
                    System.out.println("No se pudo obtener el título del producto");
                }

            } catch (Exception e) {
                System.out.println("Error esperando la página de producto: " + e.getMessage());
                try {
                    tomarCapturaPantalla("error_carga_producto");
                } catch (Exception e2) {
                    // Ignorar errores de captura
                }
                throw e;
            }

            System.out.println("Tercer ítem seleccionado correctamente");

        } catch (Exception e) {
            System.out.println("Error al seleccionar tercer ítem: " + e.getMessage());
            try {
                tomarCapturaPantalla("error_seleccion");
            } catch (Exception e2) {
                // Ignorar errores de captura
            }
            throw e;
        }
    }

    /**
     * Método auxiliar para tomar capturas de pantalla
     */
    private void tomarCapturaPantalla(String nombre) {
        try {
            // Crear directorio si no existe
            File directorio = new File("target/screenshots");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            Files.copy(scrFile.toPath(), new File("target/screenshots/" + nombre + "_" + System.currentTimeMillis() + ".png").toPath());
        } catch (Exception e) {
            System.out.println("No se pudo tomar captura de pantalla: " + e.getMessage());
        }
    }
    /**
     * Agrega al carrito con manejo robusto de diferentes diseños de página
     * y reintento de selección de cantidad
     */
    public void agregarAlCarrito(int cantidad) throws AgregarAlCarritoException {
        boolean productoAgregadoExitosamente = false;
        String mensajeError = "";
        int maxIntentos = 2;
        int intentoActual = 0;

        while (intentoActual < maxIntentos && !productoAgregadoExitosamente) {
            intentoActual++;
            System.out.println("Intento " + intentoActual + " de " + maxIntentos + " para agregar al carrito");

            try {
                // Esperar a que la página se cargue completamente
                esperaAleatoria();
                manejarDialogos();

                /* En cada intento, volver a intentar seleccionar la cantidad */
                boolean haySelector = false;
                try {
                    List<WebElement> selectores = driver.findElements(seleccióndecantidad);
                    if (!selectores.isEmpty() && selectores.get(0).isDisplayed()) {
                        WebElement select = selectores.get(0);
                        scrollToElement(select);

                        // Seleccionar la cantidad usando Select
                        new org.openqa.selenium.support.ui.Select(select)
                                .selectByValue(String.valueOf(cantidad));

                        // IMPORTANTE: Añadir un tiempo de espera para que el dropdown se cierre
                        Thread.sleep(1000);

                        // Hacer clic en otro lugar para cerrar el dropdown
                        Actions actions = new Actions(driver);
                        actions.moveToElement(driver.findElement(By.tagName("body"))).click().perform();
                        Thread.sleep(500);

                        haySelector = true;
                        System.out.println("Selector de cantidad encontrado y configurado a: " + cantidad);
                    }
                } catch (Exception e) {
                    mensajeError = "No se encontró selector de cantidad o no se pudo usar: " + e.getMessage();
                    System.out.println(mensajeError);
                    // No lanzamos excepción aquí porque intentaremos verificar si la cantidad es 1
                }

                // Si no hay selector de cantidad, NUNCA permitir continuar
                if (!haySelector) {
                    mensajeError = "No hay selector de cantidad disponible. No se puede agregar al carrito.";
                    System.out.println(mensajeError);
                    throw new AgregarAlCarritoException(mensajeError);
                }

                // NUEVO: Re-obtener el botón cada vez para evitar el error "stale element reference"
                try {
                    // Esperar y obtener el botón justo antes de usarlo
                    WebElement boton = esperar.until(
                            ExpectedConditions.refreshed(
                                    ExpectedConditions.elementToBeClickable(botónAgregarAlCarrito)
                            )
                    );

                    scrollToElement(boton);

                    // Intentar con click() directo primero
                    try {
                        boton.click();
                        System.out.println("Clic directo en botón Agregar al carrito");
                    } catch (Exception e) {
                        // Si falla el click directo, intentar con JavaScript
                        System.out.println("Click directo falló, intentando con JavaScript: " + e.getMessage());
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("arguments[0].click();", boton);
                        System.out.println("Clic en botón Agregar al carrito (usando JavaScript)");
                    }

                    // Esperar confirmación (probar varios selectores)
                    try {
                        esperar.until(ExpectedConditions.or(
                                ExpectedConditions.visibilityOfElementLocated(confirmaciónDelCarrito),
                                ExpectedConditions.urlContains("cart"),
                                ExpectedConditions.urlContains("huc")
                        ));
                        productoAgregadoExitosamente = true;
                        System.out.println("Producto agregado al carrito exitosamente");
                    } catch (TimeoutException te) {
                        if (intentoActual >= maxIntentos) {
                            mensajeError = "Timeout esperando confirmación de carrito: " + te.getMessage();
                            System.out.println(mensajeError);
                            throw new AgregarAlCarritoException(mensajeError);
                        } else {
                            System.out.println("Timeout en confirmación, reintentando... (" + intentoActual + "/" + maxIntentos + ")");
                            // Permitir que continue el bucle para reintentar
                            continue;
                        }
                    }

                } catch (Exception e) {
                    if (intentoActual >= maxIntentos) {
                        mensajeError = "Error al hacer clic en Agregar al carrito: " + e.getMessage();
                        System.out.println(mensajeError);
                        throw new AgregarAlCarritoException(mensajeError);
                    } else {
                        System.out.println("Error en clic, reintentando... (" + intentoActual + "/" + maxIntentos + "): " + e.getMessage());
                        // Permitir que continue el bucle para reintentar
                        continue;
                    }
                }

            } catch (AgregarAlCarritoException e) {
                throw e; // Relanzamos la excepción específica
            } catch (Exception e) {
                if (intentoActual >= maxIntentos) {
                    mensajeError = "Error no controlado al agregar al carrito: " + e.getMessage();
                    System.out.println(mensajeError);
                    throw new AgregarAlCarritoException(mensajeError, e);
                } else {
                    System.out.println("Error general, reintentando... (" + intentoActual + "/" + maxIntentos + "): " + e.getMessage());
                    // Permitir que continue el bucle para reintentar
                }
            }
        }

        // Si llegamos hasta aquí sin verificar el éxito, lanzamos una excepción
        if (!productoAgregadoExitosamente) {
            throw new AgregarAlCarritoException("No se pudo confirmar que el producto fue agregado al carrito después de " + maxIntentos + " intentos");
        }
    }

}
