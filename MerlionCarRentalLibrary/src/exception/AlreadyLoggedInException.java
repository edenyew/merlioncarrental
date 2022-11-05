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
public class AlreadyLoggedInException extends Exception {

    /**
     * Creates a new instance of <code>AlreadyLoggedInException</code> without
     * detail message.
     */
    public AlreadyLoggedInException() {
    }

    /**
     * Constructs an instance of <code>AlreadyLoggedInException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AlreadyLoggedInException(String msg) {
        super(msg);
    }
}
