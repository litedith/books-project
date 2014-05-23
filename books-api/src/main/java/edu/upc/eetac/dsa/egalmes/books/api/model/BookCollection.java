package edu.upc.eetac.dsa.egalmes.books.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.egalmes.books.api.BooksResource;
import edu.upc.eetac.dsa.egalmes.books.api.MediaType;

public class BookCollection {
	@InjectLinks({
			@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, rel = "books", title = "Books collection", type = MediaType.BOOK_API_BOOK_COLLECTION),
			@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, rel = "books", title = "Books collection", type = MediaType.BOOK_API_BOOK_COLLECTION, method = "getMeloinvento"),
			@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, condition = "${resource.administrator}", rel = "create-books", title = "Create books", type = MediaType.BOOK_API_BOOK, method = "createBook")
		})
	private List<Link> links;
	private List<Book> books;

	public BookCollection() {
		super();
		books = new ArrayList<Book>();

	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public void addBook(Book book) {
		books.add(book);

	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

}
