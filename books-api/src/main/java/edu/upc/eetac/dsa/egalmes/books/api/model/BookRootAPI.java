package edu.upc.eetac.dsa.egalmes.books.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

import edu.upc.eetac.dsa.egalmes.books.api.BookRootAPIResource;
import edu.upc.eetac.dsa.egalmes.books.api.BooksResource;
import edu.upc.eetac.dsa.egalmes.books.api.MediaType;

public class BookRootAPI { // rel el que no se cambiara el home
	@InjectLinks({ // absolte: http...... entero
			@InjectLink(resource = BookRootAPIResource.class, style = Style.ABSOLUTE, rel = "self bookmark home", title = "Beeter Root API", method = "getRootAPI"),
			@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, rel = "books", title = "Books collection", type = MediaType.BOOK_API_BOOK_COLLECTION),
			@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, condition="${resource.administrator}", rel = "create-books", title = "Create books", type = MediaType.BOOK_API_BOOK, method = "createBook"),
			//@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, condition="${resource.registered}", rel = "books/reviews", title = "Create Review", type = MediaType.BOOK_API_REVIEW, method = "createReview"),
			//@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, condition="${resource.administrator}", rel = "books/{idbook}", title = "update books", type = MediaType.BOOK_API_BOOK, method = "updateBook")
			})
	private List<Link> links;

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
}