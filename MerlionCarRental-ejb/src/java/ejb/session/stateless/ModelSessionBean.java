/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.Model;
import exception.DeleteModelException;
import exception.InputDataValidationException;
import exception.ModelNotFoundException;
import exception.UnknownPersistenceException;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author jonta
 */
@Stateless
public class ModelSessionBean implements ModelSessionBeanRemote, ModelSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    

    public ModelSessionBean() 
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }


    @Override
    public Long createNewModel(Model model, Long categoryId) throws ModelNotFoundException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<Model>>constraintViolations = validator.validate(model);
        
        if (constraintViolations.isEmpty())
        {
            try
            {
       
                Category category = em.find(Category.class, categoryId);
                em.persist(model);

                category.getModels().add(model);
                model.setCategory(category);

                em.flush();
                return model.getModelId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new ModelNotFoundException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
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
    public void updateModel(Model model) throws ModelNotFoundException, InputDataValidationException
    {
        if (model != null && model.getModelId()!= null)
        {
            Set<ConstraintViolation<Model>>constraintViolations = validator.validate(model);
            
            if (constraintViolations.isEmpty())
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
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
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
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Model>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    

}

