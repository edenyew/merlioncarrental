/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.Model;
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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}
