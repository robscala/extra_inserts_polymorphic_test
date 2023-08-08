package org.hibernate.bugs;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("9")
public class CustomerCompany extends Company
{
    private String zipCode;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REMOVE})
    private CustomerCompany parent;

    public CustomerCompany() {
    }

    public CustomerCompany(String name) {
        super(name);
    }

    public CustomerCompany getParent() {
        return parent;
    }

    public void setParent(CustomerCompany parent) {
        this.parent = parent;
    }
}
