/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.SupportTicketStatusEnum;

/**
 *
 * @author Koh Wen Jie
 */
@Entity
public class SupportTicketEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supportTIcketId;
    @Column(nullable = false, length = 400)
    @NotNull
    @Size(min = 1, max = 400)
    private String issue;
    @Enumerated
    @NotNull
    @Column(nullable = false)
    private SupportTicketStatusEnum supportTIcketStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private PurchaseOrderLineItemEntity purchaseOrderLineItemEntity;

    public SupportTicketEntity() {
    }

    public SupportTicketEntity(String issue, SupportTicketStatusEnum supportTIcketStatus) {
        this();
        this.issue = issue;
        this.supportTIcketStatus = supportTIcketStatus;
    }

    public Long getSupportTIcketId() {
        return supportTIcketId;
    }

    public void setSupportTIcketId(Long supportTIcketId) {
        this.supportTIcketId = supportTIcketId;
    }

    /**
     * @return the issue
     */
    public String getIssue() {
        return issue;
    }

    /**
     * @param issue the issue to set
     */
    public void setIssue(String issue) {
        this.issue = issue;
    }

    /**
     * @return the supportTIcketStatus
     */
    public SupportTicketStatusEnum getSupportTIcketStatus() {
        return supportTIcketStatus;
    }

    /**
     * @param supportTIcketStatus the supportTIcketStatus to set
     */
    public void setSupportTIcketStatus(SupportTicketStatusEnum supportTIcketStatus) {
        this.supportTIcketStatus = supportTIcketStatus;
    }

    /**
     * @return the purchaseOrderLineItemEntity
     */
    public PurchaseOrderLineItemEntity getPurchaseOrderLineItemEntity() {
        return purchaseOrderLineItemEntity;
    }

    /**
     * @param purchaseOrderLineItemEntity the purchaseOrderLineItemEntity to set
     */
    public void setPurchaseOrderLineItemEntity(PurchaseOrderLineItemEntity purchaseOrderLineItemEntity) {
        this.purchaseOrderLineItemEntity = purchaseOrderLineItemEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (supportTIcketId != null ? supportTIcketId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the supportTIcketId fields are not set
        if (!(object instanceof SupportTicketEntity)) {
            return false;
        }
        SupportTicketEntity other = (SupportTicketEntity) object;
        if ((this.supportTIcketId == null && other.supportTIcketId != null) || (this.supportTIcketId != null && !this.supportTIcketId.equals(other.supportTIcketId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SupportTIcketEntity[ id=" + supportTIcketId + " ]";
    }

}
