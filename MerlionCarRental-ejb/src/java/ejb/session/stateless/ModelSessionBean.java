/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.Model;
import exception.DeleteModelException;
import exception.ModelNotFoundException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jonta
 */
@Stateless
public class ModelSessionBean implements ModelSessionBeanRemote, ModelSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewModel(Model model, Long categoryId){
       
       Category category = em.find(Category.class, categoryId);
       em.persist(model);
       
       category.getModels().add(model);
       model.setCategory(category);
       
       em.flush();
       return model.getModelId();
       
   }
   
    @Override
    public List<Model> retrieveAllModels() {
       
       Query query = em.createQuery("SELECT m FROM Model m");
       
       return query.getResultList();
   }
   
    @Override
    public Model retrieveModelById(Long modelId) throws ModelNotFoundException
    {
        Model model = em.find(Model.class, modelId);
        
            return model;
    }
   
    @Override
    public void updateModel(Model model) throws ModelNotFoundException
    {
         if (model != null && model.getModelId()!= null)
        {
            Model modelToUpdate = retrieveModelById(model.getModelId());
            
            modelToUpdate.setCategory(model.getCategory());
            modelToUpdate.setDisabled(model.getDisabled());
            modelToUpdate.setInUse(model.getInUse());
            modelToUpdate.setMakeName(model.getMakeName());
            modelToUpdate.setModelName(model.getModelName());
            
            em.merge(modelToUpdate);

        }
        else 
        {
            throw new ModelNotFoundException("Model does not exist!");
        }
    }
   
    @Override
    public void deleteModel(Model model)throws ModelNotFoundException, DeleteModelException
    {
        
        Model modelToDelete = retrieveModelById(model.getModelId());
        if (modelToDelete != null) {
            
            if (modelToDelete.getInUse() == true)
                {
                    modelToDelete.setDisabled(true);
                    throw new DeleteModelException("Model is in use and cannot be deleted");
                }
            else if (modelToDelete.getInUse() == false) 
                {
                    
                    em.remove(modelToDelete);
                }
        } else {
            throw new ModelNotFoundException("Model does not exist!");
        }
    }
    

}

