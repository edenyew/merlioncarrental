/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author edenyew
 */
public class PartnerEntityNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>PartnerEntityNotFoundException</code>
     * without detail message.
     */
    public PartnerEntityNotFoundException() {
    }

    /**
     * Constructs an instance of <code>PartnerEntityNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public PartnerEntityNotFoundException(String msg) {
        super(msg);
    }
}
