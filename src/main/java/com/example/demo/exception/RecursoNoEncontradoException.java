
package com.example.demo.exception;

/**
 * Excepción utilizada cuando un recurso solicitado no existe
 * en la aplicación.
 *
 * <p>Al extender RuntimeException, puede lanzarse sin necesidad
 * de declararla en la firma de los métodos.</p>
 *
 * @author
 * @version 1.0
 */
public class RecursoNoEncontradoException extends RuntimeException {

    /**
     * Crea una excepción con un mensaje descriptivo del recurso
     * que no se encontró.
     *
     * @param mensaje descripción del recurso que no existe
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
