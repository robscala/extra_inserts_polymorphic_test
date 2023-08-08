package org.hibernate.bugs;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
@DiscriminatorColumn(name="CompanyType", discriminatorType= DiscriminatorType.INTEGER)
public abstract class Company {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    protected long id;

    @Version
    private int version;

    @OneToOne(orphanRemoval=true, cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
    private ContactInfo contactInfo = new ContactInfo();

    private String name;

    public Company() {
    }

    protected Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
