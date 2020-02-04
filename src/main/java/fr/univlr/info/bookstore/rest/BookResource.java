package fr.univlr.info.bookstore.rest;

import fr.univlr.info.bookstore.domain.Book;
import fr.univlr.info.bookstore.domain.Person;


import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.net.URI;
import java.util.*;

@Path("/api")
public class BookResource {
    // pseudo database

    @Context private UriInfo uriInfo;
    private static Map<String, Book> books = new HashMap<>();
    private static Map<String, Person> allAuthors = new HashMap<>();
    static {
        Person pierre = new Person("Pierre","Durand");
        Person paul = new Person("Paul","Martin");
        allAuthors.put("pierre", pierre);
        allAuthors.put("paul", paul);
        Book book1 = new Book("ZT56", "Essai", Arrays.asList(pierre,paul), 12.4f);
        Book book2 = new Book("ZT57", "Roman", Arrays.asList(pierre), 8f);
        books.put("ZT56", book1);
        books.put("ZT57", book2);
    }

    /**
     * @param isbn
     * @return le livre correspondant a l'isbn
     */

    @GET
    @Path("/book/{isbn}")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Book getBook(@PathParam("isbn") String isbn) {
        Book book = books.get(isbn);
        if (book != null) {
            return book;
        } else {
            throw new NotFoundException("Book does not exist");
        }
    }

    /**
     *
     * @return tout les livres
     */
    @GET
    @Path("/book")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Collection<Book> getAllBook(){
        return books.values();
    }

    /**
     *
     * @param book
     * @return 201 si le livre a bien été ajouté, return 409 si le livre est deja présent
     */
    @POST
    @Path("/book")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response addBook(Book book){
        if(books.containsKey(book.getIsbn())){
          return Response.status(Response.Status.CONFLICT).build();
        }else {
            UriBuilder ub = uriInfo.getAbsolutePathBuilder();
            URI usbnuri = ub.path(book.getIsbn()).build();
            books.put(book.getIsbn(),book);
            return Response.created(usbnuri).build();
        }

    }

    /**
     *
     * @param isbn
     * @return NO_CONTENT si supprimer, NOT_FOUND si non trouvé
     */
    @DELETE
    @Path("/book/{isbn}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteBook(@PathParam("isbn") String isbn) {
        if (books.containsKey(isbn)) {
            books.remove(isbn);
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return  Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     *
     * @return 200 quand tout les livres sont supprimer
     */
    @DELETE
    @Path("/book")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteAllBook() {
        books.clear();
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        return Response.ok().build();
    }

    /**
     *
     * @param book
     * @param isbn
     * @return NOT_FOUND si non trouvé,
     *         UNPROCESSABLE_ENTITY si les deux isbn ne corresponde pas
     *         NO_CONTENT si le livre a été mis a jour
     */
    @PUT
    @Path("/book/{isbn}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateBook(Book book, @PathParam("isbn") String isbn) {

        if (!books.containsKey(isbn)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!book.getIsbn().equals(isbn)) {
            return Response.status(422).build();
        }

        books.remove(isbn);
        books.put(book.getIsbn(), book);

        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        return Response.noContent().build();

    }

    ///////////////////// PARTIE 2 ////////////////////////
    /**
     *
     * @param isbn
     * @return tout les livres d'un auteurs
     */
    @GET
    @Path("/book/{isbn}/authors")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Person> getBookAuthor(@PathParam("isbn") String isbn){
     Set<Person> res = new HashSet<>();
     res.addAll(books.get(isbn).getAuthors());

     return res;
    }


}
