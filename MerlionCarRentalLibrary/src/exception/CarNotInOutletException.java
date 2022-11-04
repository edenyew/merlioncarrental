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
public class CarNotInOutletException extends Exception{

    /**
     * Creates a new instance of <code>CarNotInOutletException</code> without
     * detail message.
     */
    public CarNotInOutletException() {
    }

    /**
     * Constructs an instance of <code>CarNotInOutletException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CarNotInOutletException(String msg) {
        super(msg);
    }
}
