/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.PurchaseOrderEntitySessionBeanLocal;
import entity.PurchaseOrderEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author srinivas
 */
@Named(value = "purchaseOrderManagementManagedBean")
@ViewScoped
public class PurchaseOrderManagementManagedBean implements Serializable {

    @EJB
    private PurchaseOrderEntitySessionBeanLocal purchaseOrderEntitySessionBean;
    
    private List<PurchaseOrderEntity> listOfAllPurchaseOrders;
    private List<PurchaseOrderEntity> filteredListOfAllPurchasOrders;

    /**
     * Creates a new instance of PurchaseOrderManagementManagedBean
     */
    public PurchaseOrderManagementManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        setListOfAllPurchaseOrders(purchaseOrderEntitySessionBean.retrieveAllPurchaseOrders());
        setFilteredListOfAllPurchasOrders(new ArrayList<>());
    }

    /**
     * @return the listOfAllPurchaseOrders
     */
    public List<PurchaseOrderEntity> getListOfAllPurchaseOrders() {
        return listOfAllPurchaseOrders;
    }

    /**
     * @param listOfAllPurchaseOrders the listOfAllPurchaseOrders to set
     */
    public void setListOfAllPurchaseOrders(List<PurchaseOrderEntity> listOfAllPurchaseOrders) {
        this.listOfAllPurchaseOrders = listOfAllPurchaseOrders;
    }

    /**
     * @return the filteredListOfAllPurchasOrders
     */
    public List<PurchaseOrderEntity> getFilteredListOfAllPurchasOrders() {
        return filteredListOfAllPurchasOrders;
    }

    /**
     * @param filteredListOfAllPurchasOrders the filteredListOfAllPurchasOrders to set
     */
    public void setFilteredListOfAllPurchasOrders(List<PurchaseOrderEntity> filteredListOfAllPurchasOrders) {
        this.filteredListOfAllPurchasOrders = filteredListOfAllPurchasOrders;
    }
    
}