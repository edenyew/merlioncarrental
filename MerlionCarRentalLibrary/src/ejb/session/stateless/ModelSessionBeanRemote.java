/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import exception.DeleteModelException;
import exception.InputDataValidationException;
import exception.ModelNotFoundException;
import exception.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author jonta
 */
@Remote
public interface ModelSessionBeanRemote {
    
    public Long createNewModel(Model model, Long categoryId) throws ModelNotFoundException, UnknownPersistenceException, InputDataValidationException;
    
    public List<Model> retrieveAllModels();
    
    public void updateModel(Model model) throws ModelNotFoundException, InputDataValidationException;
     
    public Model retrieveModelById(Long modelId) throws ModelNotFoundException;

    public void deleteModel(Model model) throws ModelNotFoundException, DeleteModelException;
    
    
}
