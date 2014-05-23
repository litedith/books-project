package edu.upc.eetac.dsa.egalmes.books.android;

import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;












import edu.upc.eetac.dsa.egalmes.books.android.api.Book.*;
import edu.upc.eetac.dsa.egalmes.books.android.api.Book;
import edu.upc.eetac.dsa.egalmes.books.android.api.BookAPI;
import edu.upc.eetac.dsa.egalmes.books.android.api.BookAndroidException;
import edu.upc.eetac.dsa.egalmes.books.android.api.BookCollection;
import edu.upc.eetac.dsa.egalmes.books.android.R;
import edu.upc.eetac.dsa.egalmes.books.android.BookAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class HelloMainActivity extends Activity
{	
	private final static String TAG = HelloMainActivity.class.toString();
	private BookAdapter adapter;
	String serverAddress;
	String serverPort;
	BookAPI api;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("HELOOOOOOO");
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	SharedPreferences prefs = getSharedPreferences("books-api",
			Context.MODE_PRIVATE);
	final String username = prefs.getString("username", "victor");
	final String password = prefs.getString("password", "victor");

	Authenticator.setDefault(new Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password
					.toCharArray());
		}
	});
	Log.d(TAG, "authenticated with " + username + ":" + password);

	
	
	}
	
	public void llamar(View v) {
		
//		api = new BookAPI();
//		URL url = null;
//		try {
//			url = new URL("http://" + serverAddress + ":" + serverPort
//					+ "/books-api/books/");
//		} catch (MalformedURLException e) {
//			Log.d(TAG, e.getMessage(), e);
//			finish();
//		}
		String palabra = ((EditText) findViewById(R.id.edittext1)).getText()
			.toString();
		Intent i = new Intent(getApplicationContext(), listado.class);
		i.putExtra("palabra", palabra);
		startActivity(i);
	}


	
}
