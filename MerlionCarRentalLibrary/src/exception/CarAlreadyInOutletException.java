/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author jonta
 */
public class CarAlreadyInOutletException extends Exception {

    /**
     * Creates a new instance of <code>CarAlreadyInOutletException</code>
     * without detail message.
     */
    public CarAlreadyInOutletException() {
    }

    /**
     * Constructs an instance of <code>CarAlreadyInOutletException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CarAlreadyInOutletException(String msg) {
        super(msg);
    }
}
