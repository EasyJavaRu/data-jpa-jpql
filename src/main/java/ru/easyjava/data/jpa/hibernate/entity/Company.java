package ru.easyjava.data.jpa.hibernate.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import java.util.Collection;
import java.util.stream.Collectors;

@SuppressWarnings("PMD")
@Entity
@NamedQuery(name = "findCompaniesWithWorkerPassport",
query = "Select c from Company as c, IN(c.workers) as w where w.passport.series = :series")
public class Company extends AbstractIdentifiableObject {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @ManyToMany(mappedBy = "workingPlaces")
    private Collection<Person> workers;

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", workers=" + workers
                .stream()
                .map(Person::getFirstName)
                .collect(Collectors.joining(","))
                + '}';
    }
}
