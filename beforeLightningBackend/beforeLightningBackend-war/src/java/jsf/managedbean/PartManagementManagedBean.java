/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AccessoryEntitySessionBeanLocal;
import ejb.session.stateless.AccessoryItemEntitySessionBeanLocal;
import ejb.session.stateless.PartChoiceEntitySessionBeanLocal;
import ejb.session.stateless.PartEntitySessionBeanLocal;
import ejb.session.stateless.ProductEntitySessionBeanLocal;
import entity.PartEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author Koh Wen Jie
 */
@Named(value = "partManagementManagedBean")
@ViewScoped
public class PartManagementManagedBean implements Serializable {

    @EJB(name = "PartChoiceEntitySessionBeanLocal")
    private PartChoiceEntitySessionBeanLocal partChoiceEntitySessionBeanLocal;

    @EJB(name = "PartEntitySessionBeanLocal")
    private PartEntitySessionBeanLocal partEntitySessionBeanLocal;

    @EJB(name = "ProductEntitySessionBeanLocal")
    private ProductEntitySessionBeanLocal productEntitySessionBeanLocal;

    @EJB(name = "AccessoryItemEntitySessionBeanLocal")
    private AccessoryItemEntitySessionBeanLocal accessoryItemEntitySessionBeanLocal;

    @EJB(name = "AccessoryEntitySessionBeanLocal")
    private AccessoryEntitySessionBeanLocal accessoryEntitySessionBeanLocal;
    
    @Inject
    private ViewPartManagedBean viewPartManagedBean;

    private List<PartEntity> listOfPartEntities;
    private List<PartEntity> filteredListOfPartEntities;

    public PartManagementManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        initializeState();
    }

    public void initializeState() {
        setListOfPartEntities(partEntitySessionBeanLocal.retrieveAllPartEntities());
    }

    /**
     * @return the listOfPartEntities
     */
    public List<PartEntity> getListOfPartEntities() {
        return listOfPartEntities;
    }

    /**
     * @param listOfPartEntities the listOfPartEntities to set
     */
    public void setListOfPartEntities(List<PartEntity> listOfPartEntities) {
        this.listOfPartEntities = listOfPartEntities;
    }

    /**
     * @return the filteredListOfPartEntities
     */
    public List<PartEntity> getFilteredListOfPartEntities() {
        return filteredListOfPartEntities;
    }

    /**
     * @param filteredListOfPartEntities the filteredListOfPartEntities to set
     */
    public void setFilteredListOfPartEntities(List<PartEntity> filteredListOfPartEntities) {
        this.filteredListOfPartEntities = filteredListOfPartEntities;
    }

    public ViewPartManagedBean getViewPartManagedBean() {
        return viewPartManagedBean;
    }

    public void setViewPartManagedBean(ViewPartManagedBean viewPartManagedBean) {
        this.viewPartManagedBean = viewPartManagedBean;
    }
}
