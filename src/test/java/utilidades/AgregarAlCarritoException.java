package utilidades;

/**
 * Excepción personalizada que se lanza cuando ocurre un error al agregar productos al carrito.
 * Esta clase extiende de Exception y proporciona funcionalidad para manejar errores específicos
 * relacionados con la operación de agregar elementos al carrito de compras.
 */
public class AgregarAlCarritoException extends Exception {

    /**
     * Constructor que crea una nueva excepción con un mensaje descriptivo.
     *
     * @param mensaje Descripción del error que causó la excepción
     */
    public AgregarAlCarritoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor que crea una nueva excepción con un mensaje descriptivo y la causa original.
     * Útil para encadenar excepciones y mantener la traza del error original.
     *
     * @param mensaje Descripción del error que causó la excepción
     * @param causa Excepción original que provocó este error
     */
    public AgregarAlCarritoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}