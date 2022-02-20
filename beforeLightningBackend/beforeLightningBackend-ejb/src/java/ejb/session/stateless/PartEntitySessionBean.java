/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartChoiceEntity;
import entity.PartEntity;
import entity.ProductEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewPartEntityException;
import util.exception.DeletePartEntityException;
import util.exception.InputDataValidationException;
import util.exception.PartEntityNotFoundException;
import util.exception.ProductSkuNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePartEntityException;

/**
 *
 * @author irene
 */
@Stateless
public class PartEntitySessionBean implements PartEntitySessionBeanLocal {

    @EJB
    private ProductEntitySessionBeanLocal productEntitySessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    @PersistenceContext(unitName = "beforeLightningBackend-ejbPU")
    private EntityManager entityManager;

    public PartEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    // to add new parts with all new part choices, linked with existing product
    @Override
    public PartEntity createNewPartEntity(List<String> productCodes, PartEntity newPartEntity) throws CreateNewPartEntityException,
            UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<PartEntity>> constraintViolations = validator.validate(newPartEntity);

        if (constraintViolations.isEmpty()) {
            try {
                // persisting the new part choices
                for (PartChoiceEntity p : newPartEntity.getPartChoices()) {

                    entityManager.persist(p);

                }

                entityManager.persist(newPartEntity);

                entityManager.flush();

                PartEntity newlyCreatedPart = entityManager.find(PartEntity.class, newPartEntity.getPartId());

                // updating the product
                for (String skuCode : productCodes) {
                    try {
                        ProductEntity product = productEntitySessionBeanLocal.retrieveProductEntityBySkuCode(skuCode);
                        product.getParts().add(newlyCreatedPart);
                        newlyCreatedPart.getProducts().add(product);
                    } catch (ProductSkuNotFoundException ex) {

                        throw new CreateNewPartEntityException("An error has occured while creating the part: " + ex.getMessage());

                    }
                }

                return newlyCreatedPart;
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public PartEntity retrievePartEntityByPartEntityId(Long partEntityId) throws PartEntityNotFoundException {
        PartEntity pe = entityManager.find(PartEntity.class, partEntityId);
        if (pe == null) {
            throw new PartEntityNotFoundException();
        }
        return pe;
    }

    // updating parts with existing products
    @Override
    public void updatePartEntity(PartEntity updatedPartEntity) throws PartEntityNotFoundException,
            UpdatePartEntityException, InputDataValidationException {

        if (updatedPartEntity != null && updatedPartEntity.getPartId() != null) {
            Set<ConstraintViolation<PartEntity>> constraintViolations = validator.validate(updatedPartEntity);
            if (constraintViolations.isEmpty()) {

                try {
                    PartEntity partToBeUpdated = retrievePartEntityByPartEntityId(updatedPartEntity.getPartId());
                    List<ProductEntity> newProductLists = new ArrayList<>();

                    // update the product
                    for (ProductEntity p : updatedPartEntity.getProducts()) {
                        ProductEntity pToBeUpdated = entityManager.find(ProductEntity.class, p.getProductTypeEntityId());
                        if (pToBeUpdated == null) {
                            throw new UpdatePartEntityException("An error has occured while updating the part: product cannot be found. ");
                        } else {
                            newProductLists.add(p);
                        }

                    }

                    partToBeUpdated.setProducts(newProductLists);

                    // update part choices of individual product
                    for (ProductEntity p : partToBeUpdated.getProducts()) {
                        p.getParts().remove(partToBeUpdated);
                    }

                    // one way adding part choices
                    List<PartChoiceEntity> listOfPartChoices = new ArrayList<>();
                    for (PartChoiceEntity p : updatedPartEntity.getPartChoices()) {
                        PartChoiceEntity toBeAdded = entityManager.find(PartChoiceEntity.class, p.getPartChoiceId());

                        // if the part choices do not currently exist in database
                        if (toBeAdded == null) {
                            entityManager.persist(toBeAdded);
                            entityManager.flush();
                            toBeAdded = entityManager.find(PartChoiceEntity.class, toBeAdded.getPartChoiceId());
                        }

                        listOfPartChoices.add(toBeAdded);
                    }

                    partToBeUpdated.setPartChoices(listOfPartChoices);

                    entityManager.persist(this);

                } catch (PartEntityNotFoundException ex) {
                    throw new UpdatePartEntityException("An error has occured while updating the part: part ID does not exist. ");
                }

            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new PartEntityNotFoundException("Part entity ID is not provided. ");
        }
    }

    public void removePartEntity(Long partEntityId) throws PartEntityNotFoundException, DeletePartEntityException {
        try {
            PartEntity partEntity = retrievePartEntityByPartEntityId(partEntityId);

            List<ProductEntity> listOfProductEntitiesLinked = partEntity.getProducts();
            
            if(listOfProductEntitiesLinked.isEmpty()) {
                List<PartChoiceEntity> listOfPartChoiceEntity = partEntity.getPartChoices();
                if(listOfPartChoiceEntity.isEmpty()) {
                    
                    entityManager.remove(partEntity);
                
                } else {
                    
                   throw new DeletePartEntityException("An error has occured while removing part entity: part entity is linked to a part choice entity. ");
                   
                }
                
            
            } else {
                throw new DeletePartEntityException("An error has occured while removing part entity: part entity is linked to a product. ");
            }
        } catch (PartEntityNotFoundException ex) {
            throw new PartEntityNotFoundException("An error has occured while removing part entity: part ID does not exist.");
        }

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<PartEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}