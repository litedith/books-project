package edu.upc.eetac.dsa.egalmes.books.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.egalmes.books.api.BooksResource;
import edu.upc.eetac.dsa.egalmes.books.api.MediaType;

public class Review {
	@InjectLinks({
		@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, rel = "create-review", title = "create review", type = MediaType.BOOK_API_BOOK , method = "createReview", bindings = @Binding (name ="bookid", value ="${instance.idbook}" ))

		})
	private List<Link> links;
	private String idreview;
	private String username;
	private String dateupdate;
	private String text;
	private String idbook;
//	@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, condition = "${resource.registered}", rel = "self-editreview", title = "Edit review", type = MediaType.BOOK_API_REVIEW, method = "updateReview", bindings={
//	@Binding(name = "idbook", value = "${instance.idbook}"), @Binding(name = "idreview", value = "${instance.idreview}")}),
//@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, condition = "${resource.registered}", rel = "self-editreview", title = "Edit review", type = MediaType.BOOK_API_REVIEW, method = "deleteReview", bindings={
//	@Binding(name = "idbook", value = "${instance.idbook}"), @Binding(name = "idreview", value = "${instance.idreview}")})
	
	
//	@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, condition = "${resource.administrator}", rel = "self-editreview", title = "Edit review", type = MediaType.BOOK_API_REVIEW, method = "deleteReview", bindings={
	//	@Binding(name = "idbook", value = "${instance.idbook}"), @Binding(name = "idreview", value = "${instance.idreview}")}
	
//	 @InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, rel = "book-review", type = MediaType.BOOK_API_REVIEW, method = "getReview", bindings = {
//			@Binding(name = "idbook", value = "${instance.idbook}"),
//			@Binding(name = "idreview", value = "${instance.idreview}") }),
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDateupdate() {
		return dateupdate;
	}

	public void setDateupdate(String dateupdate) {
		this.dateupdate = dateupdate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIdreview() {
		return idreview;
	}

	public void setIdreview(String idreview) {
		this.idreview = idreview;
	}

	public String getIdbook() {
		return idbook;
	}

	public void setIdbook(String idbook) {
		this.idbook = idbook;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

}
