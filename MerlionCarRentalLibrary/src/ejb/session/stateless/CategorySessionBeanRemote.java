/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import exception.CategoryNotFoundException;
import exception.InputDataValidationException;
import exception.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author edenyew
 */
@Remote
public interface CategorySessionBeanRemote {

    public Long createNewCategory(Category category) throws CategoryNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public Category retrieveCategoryById(Long categoryId);

    public List<Category> retrieveAllCategory();

 
    
}
