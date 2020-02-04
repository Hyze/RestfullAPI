package fr.univlr.info.bookstore.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@XmlRootElement // for XML serialization...
public class Person {
    private String lastname;
    private String firstname;
    @XmlTransient
    private Collection<Book> books;

    public Person(String prenom,String nom) {
        this.lastname = nom;
        this.firstname = prenom;
        this.books = new HashSet<>();
    }

    public Collection<Book> getBooks() {
        return books;
    }

    public void setBooks(Collection<Book> books) {
        this.books = books;
    }

    public Person() {
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return lastname.equals(person.lastname) &&
                firstname.equals(person.firstname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastname, firstname);
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
