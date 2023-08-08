package org.hibernate.bugs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PersonTest
{
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );

        // Populate records in the database:
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        System.out.println("\nCreating database records");
        Person person = new Person("Henry");
        CustomerCompany customerCompany = new CustomerCompany("Customer");
        CustomerCompany parentCustomer = new CustomerCompany("Parent customer");
        customerCompany.setParent(parentCustomer);
        person.setCompany(customerCompany);
        entityManager.getTransaction().begin();
        entityManager.persist(person);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @After
    public void destroy() {
        entityManagerFactory.close();
    }

    @Test
    public void person_test() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // Count the rows of selected tables.
        List<String> tableNames = List.of("ContactInfo");
        Map<String,Long> initialRowCountMap = getTableRowCounts(tableNames, entityManager);

        System.out.println("\n---------\n\nFinding person");
        Person person = entityManager.find(Person.class, 1L);
        System.out.println("\n---------\n\nGetting company");
        Company company = person.getCompany();
        System.out.println("person's company class=" + company.getClass());
        System.out.println("\n---------\n\nGetting company name");
        System.out.println("person's company name=" + company.getName());
        System.out.println("\n---------\n\nPerforming transaction");
        entityManager.getTransaction().begin();
        person.setFirstName("Liza");
        entityManager.getTransaction().commit();

        // Count the rows of selected tables.
        Map<String,Long> finalRowCountMap = getTableRowCounts(tableNames, entityManager);

        // Show if row counts for the tables have changed.
        System.out.println("\n---------\n\nShowing record counts");
        for (String tableName : tableNames)
            System.out.println("Original " + tableName + " count: " + initialRowCountMap.get(tableName) + "  Final count: " + finalRowCountMap.get(tableName));
        System.out.println("\n---------\n");

        // Make assertions
        for (String tableName : tableNames)
            assertEquals("Number of " + tableName + " records", initialRowCountMap.get(tableName), finalRowCountMap.get(tableName));

        entityManager.close();
    }

    /*
     * Create a map of row counts for the provided list of table names.
     */
    private Map<String,Long> getTableRowCounts(List<String> tableNames, EntityManager entityManager) {
        Map<String,Long> rowCountMap = new HashMap<>();
        for (String tableName : tableNames)
            rowCountMap.put(tableName, (Long) entityManager.createNativeQuery("select count(*) from " + tableName).getSingleResult());
        return rowCountMap;
    }
}