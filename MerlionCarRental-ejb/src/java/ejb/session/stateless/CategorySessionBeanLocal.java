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
import javax.ejb.Local;

/**
 *
 * @author edenyew
 */
@Local
public interface CategorySessionBeanLocal {

    public Long createNewCategory(Category category) throws CategoryNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public List<Category> retrieveAllCategory();
    
    public Category retrieveCategoryById(Long categoryId);
    
}
