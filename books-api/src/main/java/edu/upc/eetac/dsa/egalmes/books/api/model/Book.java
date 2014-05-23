package edu.upc.eetac.dsa.egalmes.books.api.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.egalmes.books.api.BooksResource;
import edu.upc.eetac.dsa.egalmes.books.api.MediaType;

public class Book {
	@InjectLinks({
			@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, rel = "book", type = MediaType.BOOK_API_BOOK, method="getBook", bindings=@Binding(name = "idbook", value = "${instance.idbook}")),
			@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, condition = "${resource.administrator}", rel = "self-editbooks-up", title = "Edit books", type = MediaType.BOOK_API_BOOK, method = "updateBook", bindings=@Binding(name = "idbook", value = "${instance.idbook}")),
			@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, condition = "${resource.administrator}", rel = "self-editbooks-del", title = "Edit books", type = MediaType.BOOK_API_BOOK, method = "deleteBook", bindings=@Binding(name = "idbook", value = "${instance.idbook}")),
			@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, rel = "get author-books", title = "author books", type = MediaType.BOOK_API_BOOK_COLLECTION, method = "getBooksFromAuthor", bindings=@Binding(name = "author", value = "${instance.author}"))})
	private List<Link> link;
	
	private String idbook;
	private String tittle;
	private String author;
	private String language;
	private String edition;
	private Date dateedition;
	private Date dateprint;
	private String editorial;
	private List<Review> reviews = new ArrayList<Review>();

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Date getDateedition() {
		return dateedition;
	}

	public void setDateedition(Date dateedition) {
		this.dateedition = dateedition;
	}

	public Date getDateprint() {
		return dateprint;
	}

	public void setDateprint(Date dateprint) {
		this.dateprint = dateprint;
	}

	public String getIdbook() {
		return idbook;
	}

	public void setIdbook(String idbook) {
		this.idbook = idbook;
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getEditorial() {
		return editorial;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	public void setReview(List<Review> reviews) {
		this.reviews = reviews;
	}

	public void addReview(Review review) {
		reviews.add(review);
	}

	public List<Link> getLink() {
		return link;
	}

	public void setLink(List<Link> link) {
		this.link = link;
	}

}
