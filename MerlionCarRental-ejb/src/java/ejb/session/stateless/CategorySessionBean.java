/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author edenyew
 */
@Stateless
public class CategorySessionBean implements CategorySessionBeanRemote, CategorySessionBeanLocal {

    @PersistenceContext(unitName = "MerlionCarRental-ejbPU")
    private EntityManager em;
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createNewCategory(Category category)
    {
        em.persist(category);
        em.flush();
        
        return category.getCategoryId();
   
    }
    
    public List<Category> retrieveAllCategory()
    {
        Query query = em.createQuery("Select c From Category c");
        return query.getResultList();
    }
    
    public Category retrieveCategoyrById(Long categoryId)
    {
        Category category = em.find(Category.class, categoryId);
        return category;
    }
    
}
