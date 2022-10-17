/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Category;
import entity.EmployeeEntity;
import entity.PartnerEntity;


/**
 *
 * @author edenyew
 */
public class EntityManager {

    public EntityManager() 
    {
    }
    
     public Long create(Object object) throws EntityManagerException
    {
        if(object.getClass() == EmployeeEntity.class)
        {
            return EmployeeEntityManager.create((EmployeeEntity)object);
        }
        else if(object.getClass() == Category.class)
        {
            return CategoryManager.create((Category)object);
        }
        else if(object.getClass() == PartnerEntity.class)
        {
            return PartnerEntityManager.create((PartnerEntity)object);
        }
        else
        {
            throw new EntityManagerException("Invalid entity class type");
        }
    }
    
    
    
    
}
