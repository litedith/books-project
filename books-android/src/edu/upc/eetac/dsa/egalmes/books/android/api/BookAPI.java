package edu.upc.eetac.dsa.egalmes.books.android.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
 


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 


import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
 
public class BookAPI {
	private final static String TAG = BookAPI.class.getName();
	private static BookAPI instance = null;
	private URL url;
 
	private BookRootAPI rootAPI = null;
	public BookAPI()
	{}
	private BookAPI(Context context) throws IOException,
			BookAndroidException {
		super();
 
		AssetManager assetManager = context.getAssets();
		Properties config = new Properties();
		config.load(assetManager.open("config.properties"));//carga fichero configuracion 
		String serverAddress = config.getProperty("server.address");//obtiene los valores de es fichero
		String serverPort = config.getProperty("server.port");
		url = new URL("http://" + serverAddress + ":" + serverPort
				+ "/books-api/"); //se qeda cn la base url esta si utilizamos hateoas nunca cambia
 
		Log.d("LINKS", url.toString());
		getRootAPI();
	}
 
	public final static BookAPI getInstance(Context context)
			throws BookAndroidException {
		if (instance == null)
			try {
				instance = new BookAPI(context);//context es la actividad, para recuperar valores del fichero conf.
			} catch (IOException e) {
				throw new BookAndroidException(
						"Can't load configuration file");
			}
		return instance;
	}
 
	private void getRootAPI() throws BookAndroidException { //rea un modelo y ataka al servicio
		Log.d(TAG, "getRootAPI()");
		rootAPI = new BookRootAPI();
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);// true por defecto, significa que qiero leer
			urlConnection.connect();
		} catch (IOException e) {
			throw new BookAndroidException(
					"Can't connect to Beeter API Web Service");
		}
 
		BufferedReader reader;
		try {//lee json que le devuelve htps://localhost:8080/beeterapi
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());// aparti de un string y objeto json lo convierte
			JSONArray jsonLinks = jsonObject.getJSONArray("links");//asi poder manipular y obtener get, arrays.. 
			parseLinks(jsonLinks, rootAPI.getLinks());//lo proceso con el metodo priado de esta clase y lo guardas en el modelo rootAPI
		} catch (IOException e) {
			throw new BookAndroidException(
					"Can't get response from Beeter API Web Service");
		} catch (JSONException e) {
			throw new BookAndroidException("Error parsing Beeter Root API");
		}
 
	}
 
	public BookCollection getBooks(URL url) throws BookAndroidException {
		//Log.d(TAG, "getBooksFromAuthor(author)");
		System.out.println("Hemos llegado a api");
		BookCollection books = new BookCollection();
		
 
		HttpURLConnection urlConnection = null;
		try {
//			urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
//					.get("get author-books").getTarget()).openConnection();
			urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestProperty("Accept",
					MediaType.BOOK_API_BOOK_COLLECTION);
			
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
		} catch (IOException e) {
			throw new BookAndroidException(
					"Can't connect to Beeter API Web Service");
		}
 
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			System.out.println(jsonObject);
			//JSONArray jsonLinks = jsonObject.getJSONArray("links");//atributoss
			//parseLinks(jsonLinks, books.getLinks());
 
			
			JSONArray jsonBooks = jsonObject.getJSONArray("books");
			for (int i = 0; i < jsonBooks.length(); i++) {
				Book book = new Book();
				JSONObject jsonBook = jsonBooks.getJSONObject(i);// le doy valor a traves del array y lo añado a la coleccion qe es lo qe lo devuelves
				book.setAuthor(jsonBook.getString("author"));
				book.setLanguage(jsonBook.getString("language"));
				book.setIdbook(jsonBook.getString("idbook"));
				//jsonLinks = jsonBook.getJSONArray("links");
				//parseLinks(jsonLinks, books.getLinks());
				books.getBooks().add(book);
			}
		} catch (IOException e) {
			throw new BookAndroidException(
					"Can't get response from Beeter API Web Service");
		} catch (JSONException e) {
			throw new BookAndroidException("Error parsing Beeter Root API");
		}
 
		return books;
	}
 
	private void parseLinks(JSONArray jsonLinks, Map<String, Link> map)
			throws BookAndroidException, JSONException {
		for (int i = 0; i < jsonLinks.length(); i++) {
			Link link = SimpleLinkHeaderParser
					.parseLink(jsonLinks.getString(i));
			//REL PODIA ser multiple rel=" home boomark self" -> 3 enlaces qe obtienes a traves del mapa
			String rel = link.getParameters().get("rel");//tb podria obteet el title i el target(?) pRA QITARME LOS ESPACIOS BLANCOS DE ENCIAM
			String rels[] = rel.split("\\s");
			for (String s : rels)
				map.put(s, link);
		}
	}
	
	public Book getBook(String urlBook) throws BookAndroidException {
		Book book = new Book();
	 
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlBook);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonBook = new JSONObject(sb.toString()); //revuperado json, i procesado json
			book.setAuthor(jsonBook.getString("author"));
			book.setLanguage(jsonBook.getString("language"));
			book.setIdbook(jsonBook.getString("idbook"));
			JSONArray jsonLinks = jsonBook.getJSONArray("links");
			parseLinks(jsonLinks, book.getLinks());
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new BookAndroidException("Bad sting url");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new BookAndroidException("Exception when getting the sting");
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new BookAndroidException("Exception parsing response");
		}
	 
		return book;
	}
	public Review createReview(String idbook, String username, String idreview) throws BookAndroidException {
		Review review = new Review();
		
		review.setIdreview(idreview);
		review.setIdreview(username);
		
		HttpURLConnection urlConnection = null;
		try {
			JSONObject jsonBook= createJsonReview(review);
			URL urlPostStings = new URL(rootAPI.getLinks().get("create-books")
					.getTarget());
			urlConnection = (HttpURLConnection) urlPostStings.openConnection();
			urlConnection.setRequestProperty("Accept",
					MediaType.BOOK_API_BOOK);
			urlConnection.setRequestProperty("Content-Type",
					MediaType.BOOK_API_BOOK);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.connect();
			PrintWriter writer = new PrintWriter(
					urlConnection.getOutputStream());
			writer.println(jsonBook.toString());
			writer.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			jsonBook = new JSONObject(sb.toString());
	 
			review.setText(jsonBook.getString("text"));
			review.setIdbook(jsonBook.getString("idbook"));
			review.setUsername(username);
			
			
			JSONArray jsonLinks = jsonBook.getJSONArray("links");
			parseLinks(jsonLinks, review.getLinks());
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new BookAndroidException("Error parsing response");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new BookAndroidException("Error getting response");
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return review;
	}
	//writesting activity progrso void i sting tipo de retorno stings..params (aqi esta tanto el subject como el content) 
	//onpostexecute recarga lista con todos los stings inclusive el nuevo k hemos creado
	//oncreate carga layout
	//dosmetodos poststing y cancel
	//finish acaba actividad y vuelve a la anterior en este caso a la lista de stings , mostrat actividad tal i como estaba
	//en el showstings parecido al finish pero con la lista actualizada
	//post obtenemos  do input leemos, doutpu envamios, createstingjson, atraves de el metodo pivado, se crea json object i se van colocando valores 
	private JSONObject createJsonReview(Review review) throws JSONException {
		JSONObject jsonReview = new JSONObject();
		jsonReview.put("idreview", review.getIdreview());
		jsonReview.put("username", review.getUsername());
		jsonReview.put("text", review.getText());
	 
		return jsonReview;
	}
}