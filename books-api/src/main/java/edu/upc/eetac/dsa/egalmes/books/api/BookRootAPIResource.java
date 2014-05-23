package edu.upc.eetac.dsa.egalmes.books.api;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.egalmes.books.api.model.BookRootAPI;

@Path("/")
// raiz
public class BookRootAPIResource {
	@Context
	SecurityContext security;

	private boolean administrator;
//	private boolean registered;
	
	

	@GET
	public BookRootAPI getRootAPI() {
		setAdministrator(security.isUserInRole("admin"));
		BookRootAPI api = new BookRootAPI();
		return api;
	}
//	@POST
//	public BookRootAPI createBook() {
//		setAdministrator(security.isUserInRole("admin"));
//		BookRootAPI api = new BookRootAPI();
//		return api;
//	}
//	@PUT
//	public BookRootAPI updateBook() {
//		setAdministrator(security.isUserInRole("admin"));
//		BookRootAPI api = new BookRootAPI();
//		return api;
//	}
//	
//	@POST
//	public BookRootAPI createReview() {
//		setRegistered(security.isUserInRole("registered"));
//		BookRootAPI api = new BookRootAPI();
//		return api;
//	}
//	

	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}
	
	
//	public boolean isRegistered() {
//		return registered;
//	}
//
//	public void setRegistered(boolean registered) {
//		this.registered = registered;
//	}
}
