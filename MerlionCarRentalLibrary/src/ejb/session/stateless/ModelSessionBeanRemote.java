/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import exception.DeleteModelException;
import exception.ModelNotFoundException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author jonta
 */
@Remote
public interface ModelSessionBeanRemote {
    
    public Long createNewModel(Model model, Long categoryId);
    
    public List<Model> retrieveAllModels();
    
    public void updateModel(Model model) throws ModelNotFoundException;
     
    public Model retrieveModelById(Long modelId) throws ModelNotFoundException;

    public void deleteModel(Model model) throws ModelNotFoundException, DeleteModelException;
    
    
}
