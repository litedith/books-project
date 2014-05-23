package edu.upc.eetac.dsa.egalmes.books.android.api;

public interface MediaType {
	public final static String BOOK_API_USER = "application/vnd.books.api.user+json";
	public final static String BOOK_API_BOOK = "application/vnd.books.api.book+json";//enviar o recibir stings
	public final static String BOOK_API_REVIEW= "application/vnd.books.api.review+json";
	public final static String BOOK_API_BOOK_COLLECTION = "application/vnd.books.api.books.collection+json";//enviarcolecciones de sting
	public final static String BOOK_API_ERROR = "application/vnd.dsa.books.error+json";
}