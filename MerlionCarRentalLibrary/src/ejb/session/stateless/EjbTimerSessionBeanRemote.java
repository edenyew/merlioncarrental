/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import exception.OutletNotFoundException;
import java.text.ParseException;
import javax.ejb.Remote;

/**
 *
 * @author jonta
 */
@Remote
public interface EjbTimerSessionBeanRemote {
    
    public void timer(Long outletId) throws ParseException, OutletNotFoundException;
    
}
