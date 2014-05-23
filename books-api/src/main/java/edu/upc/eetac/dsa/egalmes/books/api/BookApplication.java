package edu.upc.eetac.dsa.egalmes.books.api;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
 
public class BookApplication extends ResourceConfig {
	public BookApplication() {
		super();
		register(DeclarativeLinkingFeature.class);
	}
}