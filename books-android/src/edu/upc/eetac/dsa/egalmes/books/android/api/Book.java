package edu.upc.eetac.dsa.egalmes.books.android.api;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.eetac.dsa.egalmes.books.android.api.Link;




public class Book {
	private Map<String, Link> links = new HashMap<>();
	private String idbook;
	private String tittle;
	private String author;
	private String language;
	private String edition;
	private Date dateedition;
	private Date dateprint ; 
	private String editorial;
	List <Review> reviews = new ArrayList <Review>();
	
	
	
	
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
	public Map<String, Link> getLinks() {
		// TODO Auto-generated method stub
		return links;
	}
 
	
}
