package fr.univlr.info.bookstore.rest;

import fr.univlr.info.bookstore.domain.Book;
import fr.univlr.info.bookstore.domain.Person;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.*;

@Path("/api")
public class PersonResource {

    @Context
    private UriInfo uriInfo;
    ///////////// FAKE DB /////////////////
    private static Map<String, Person> allAuthors = new HashMap<>();
    private static Map<String, Book> books = new HashMap<>();
    static{
        Person pierre = new Person("Pierre","Durand");
        Person paul = new Person("Paul","Martin");
        allAuthors.put("Pierre", pierre);
        allAuthors.put("Paul", paul);
        Book book1 = new Book("ZT56", "Essai", Arrays.asList(pierre,paul), 12.4f);
        Book book2 = new Book("ZT57", "Roman", Arrays.asList(pierre), 8f);
        books.put("ZT56", book1);
        books.put("ZT57", book2);
        pierre.getBooks().add(book2);
        pierre.getBooks().add(book1);
        paul.getBooks().add(book1);
    }
    ////////////////////////////////////////
    /**
     * @param firstname
     * @param lastname
     * @return tout les auteurs si aucun paramètre n'est spécifié, sinon renvoie l'auteur correspondant au param
     */
    @GET
    @Path("/author")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Collection<Person>  getAuthor(@QueryParam("firstname") String firstname,@QueryParam("lastname") String lastname) {
        Set<Person> res = new HashSet<>();
        if(firstname ==null && lastname == null){
            res.addAll(allAuthors.values());
        }
        if(firstname != null && lastname !=null){
            res.add(allAuthors.get(firstname));
        }
        return res;
    }
/**
 * @param firstname
 * @param lastname
 * @return tout les livres si aucun paramètre n'est spécifié, sinon renvoie les livres de l'auteur correspondant au param
 */
    @GET
    @Path("/author/book")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Collection<Book> getAuthorBooks(@QueryParam("firstname") String firstname,@QueryParam("lastname") String lastname){
        Set<Book> res = new HashSet<>();
        if(firstname == null && lastname == null){
            res.addAll(books.values());
        }

        if(firstname != null && lastname !=null){
        Collection<Book> temp = allAuthors.get(firstname).getBooks();
        res.addAll(temp);
        }

        return res;
    }
}
