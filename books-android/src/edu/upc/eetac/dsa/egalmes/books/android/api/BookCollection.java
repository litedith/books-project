package edu.upc.eetac.dsa.egalmes.books.android.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.eetac.dsa.egalmes.books.android.api.Link;

public class BookCollection {
	private Map<String, Link> links = new HashMap<>();
	private List<Book> books; 
	public BookCollection(){
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
	public Map<String, Link> getLinks() {
		return links;
	}
	
}
