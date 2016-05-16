package ru.easyjava.data.jpa.hibernate.entity;

import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;

public class QueryTest {
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        Passport p = new Passport();
        p.setSeries("AS");
        p.setNo("123456");
        p.setIssueDate(LocalDate.now());
        p.setValidity(Period.ofYears(20));

        Address a = new Address();
        a.setCity("Kickapoo");
        a.setStreet("Main street");
        a.setBuilding("1");

        Person person = new Person();
        person.setFirstName("Test");
        person.setLastName("Testoff");
        person.setDob(LocalDate.now());
        person.setPrimaryAddress(a);
        person.setPassport(p);

        Company c = new Company();
        c.setName("Acme Ltd");

        p.setOwner(person);
        person.setWorkingPlaces(Collections.singletonList(c));

        entityManagerFactory = Persistence.createEntityManagerFactory("ru.easyjava.data.jpa.hibernate");
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.merge(person);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void testGreeter() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("from Person")
                .getResultList()
                .forEach(System.out::println);
        em.createQuery("select passport from Person ", Passport.class)
                .getResultList()
                .forEach(System.out::println);
        /*em.createQuery("select passport from Person ", Person.class)
                .getResultList()
                .forEach(System.out::println);*/
        em.createQuery("from Passport as p where p.owner.lastName='Testoff'")
                .getResultList()
                .forEach(System.out::println);
        em.createQuery("from Passport as p where p.owner.lastName like :name")
                .setParameter("name", "Te%")
                .getResultList()
                .forEach(System.out::println);
        em.createQuery("Select p from Person as p, IN(p.workingPlaces) as wp where wp.name = ?1")
                .setParameter(1, "Acme Ltd")
                .getResultList()
                .forEach(System.out::println);
        em.createNamedQuery("findCompaniesWithWorkerPassport")
                .setParameter("series", "AS")
                .getResultList()
                .forEach(System.out::println);
        em.getTransaction().commit();
        em.close();
    }
}
