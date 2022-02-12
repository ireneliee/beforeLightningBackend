/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AccessoryEntity;
import entity.ProductEntity;
import entity.ProductTypeEntity;
import entity.PurchaseOrderLineItemEntity;
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
import util.exception.DeleteProductTypeException;
import util.exception.InputDataValidationException;
import util.exception.ProductTypeEntityNotFoundException;
import util.exception.UnknownPersistenceException;

@Stateless
public class ProductTypeSessionBean implements ProductTypeSessionBeanLocal {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @PersistenceContext(unitName = "beforeLightningBackend-ejbPU")
    private EntityManager entityManager;

    public ProductTypeSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public ProductTypeEntity createNewProductTypeEntity(ProductTypeEntity newProductTypeEntity) throws InputDataValidationException, UnknownPersistenceException {

        Set<ConstraintViolation<ProductTypeEntity>> constraintViolations = validator.validate(newProductTypeEntity);

        if (constraintViolations.isEmpty()) {
            try {
                entityManager.persist(newProductTypeEntity);

                entityManager.flush();

                return newProductTypeEntity;
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            }

        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ProductTypeEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    private ProductTypeEntity retrieveProductTypeByProductId(Long productTypeId) {

        ProductTypeEntity productType = entityManager.find(ProductTypeEntity.class, productTypeId);

        return productType;
    }

    @Override
    public void deleteProductType(Long productTypeId) throws DeleteProductTypeException, ProductTypeEntityNotFoundException {

        ProductTypeEntity productTypeToBeDeleted = retrieveProductTypeByProductId(productTypeId);

        if (productTypeToBeDeleted != null) {

            String queryInString = "SELECT po FROM PurchaseOrderLineItemEntity po WHERE po.productTypeEntity = iProductType";

            Query query = entityManager.createQuery(queryInString);

            query.setParameter("iProductType", productTypeToBeDeleted);

            List<PurchaseOrderLineItemEntity> listOfPurchaseOrders = query.getResultList();

            if (listOfPurchaseOrders.isEmpty()) {

                entityManager.remove(productTypeToBeDeleted);

            } else {

                throw new DeleteProductTypeException("There exists purchase order line items with the product type. Product type cannot be deleted. ");

            }

        } else {
            throw new ProductTypeEntityNotFoundException("Product type id: " + productTypeId + " does not exist.");
        }

    }

    @Override
    public void updateProductType(ProductTypeEntity productTypeEntity) throws ProductTypeEntityNotFoundException {

        if (productTypeEntity != null && productTypeEntity.getProductTypeEntityId() != null) {

            Set<ConstraintViolation<ProductTypeEntity>> constraintViolations = validator.validate(productTypeEntity);

            if (constraintViolations.isEmpty()) {

                ProductTypeEntity productTypeEntityToUpdate = retrieveProductTypeByProductId(productTypeEntity.getProductTypeEntityId());

                productTypeEntityToUpdate.setProductTypeName(productTypeEntity.getProductTypeName());
            }

        } else {

            throw new ProductTypeEntityNotFoundException("Product Id is not provided.");
        }
    }

    @Override
    public ProductTypeEntity retrieveProductTypeByProductTypeId(Long productTypeEntityId) throws ProductTypeEntityNotFoundException {
        
        ProductTypeEntity productTypeEntity = entityManager.find(ProductTypeEntity.class, productTypeEntityId);
        
        if (productTypeEntity != null) {
            
            return productTypeEntity;
            
        } else {
            
            throw new ProductTypeEntityNotFoundException("Product type with id " + productTypeEntityId + " does not exist.");
        }
        
    }
    
    @Override
    public List<ProductTypeEntity> retrieveAllProductTypeEntities() {
        
        String databaseQueryString = "SELECT p FROM ProductTypeEntity p";
        
        Query databaseQuery = entityManager.createQuery(databaseQueryString);
        
        List<ProductTypeEntity> listOfProductTypeEntites = databaseQuery.getResultList();
        
        
        listOfProductTypeEntites.forEach(p -> {
            
            if(p instanceof ProductEntity) {
                
                ((ProductEntity) p).getParts().size();
                
            } else if (p instanceof AccessoryEntity) {
                
                ((AccessoryEntity) p).getAccessoryItem().size();
                
            }
            
        });
        
        return  listOfProductTypeEntites;
        
    }

}