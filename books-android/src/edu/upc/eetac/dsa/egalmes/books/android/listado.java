package edu.upc.eetac.dsa.egalmes.books.android;

import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;

import edu.upc.eetac.dsa.egalmes.books.android.api.Book;
import edu.upc.eetac.dsa.egalmes.books.android.api.BookAPI;
import edu.upc.eetac.dsa.egalmes.books.android.api.BookAndroidException;
import edu.upc.eetac.dsa.egalmes.books.android.api.BookCollection;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class listado extends ListActivity {
	
	String serverAddress;
	String serverPort;
	BookAPI api;
	private class FetchStingsTask extends AsyncTask<URL , Void, BookCollection> {
		private ProgressDialog pd;

		@Override
		protected BookCollection doInBackground(URL... params) {
			BookCollection books = null;
			
			try {
				System.out.println("hemos entrado en asynk-------------------1111");
				//books = BookAPI.getInstance(listado.this).getBooks(params[0]);
				books = api.getBooks(params[0]);
						
			} catch (BookAndroidException e) {
				e.printStackTrace();
			}
			return books;
		}

		@Override
		protected void onPostExecute(BookCollection result) {
			addbooks(result);
			if (pd != null) {
				pd.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(listado.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		}
	
	private void addbooks(BookCollection books){
		stingList.addAll(books.getBooks());
		adapter.notifyDataSetChanged();
		}
		private ArrayList<Book> stingList;
		private BookAdapter adapter;

		private final static String TAG = HelloMainActivity.class.toString();




		@Override
		public void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_books);
		Bundle bundle = this.getIntent().getExtras();
		String a= bundle.get("palabra").toString();

		BookAPI api = new BookAPI();
		URL url = null;
		try {
			url = new URL("http://" + serverAddress + ":" + serverPort
					+ "/books-api/books/searching?author=" +a);
		} catch (MalformedURLException e) {
			Log.d(TAG, e.getMessage(), e);
			finish();
		}
		stingList = new ArrayList<>();
		adapter = new BookAdapter(this, stingList);
		//System.out.println("HOOOOOOOOLITAAAAAS-------------");
		setListAdapter(adapter);
		(new FetchStingsTask()).execute(url);
		//System.out.println("HOOOOOOOOLITAAAAAS222222222-------------");
		}
		
		
//		@Override
//	    public void onContentChanged() {
//	        super.onContentChanged();
//	        View emptyView = findViewById(com.android.internal.R.id.empty);
//	        mList = (ListView)findViewById(com.android.internal.R.id.list);
//	        if (mList == null) {
//	            throw new RuntimeException(
//	                    "Your content must have a ListView whose id attribute is " +
//	                    "'android.R.id.list'");
//	        }
//	        if (emptyView != null) {
//	            mList.setEmptyView(emptyView);
//	        }
//	        mList.setOnItemClickListener(mOnClickListener);
//	        if (mFinishedStart) {
//	            setListAdapter(mAdapter);
//	        }
//	        mHandler.post(mRequestFocus);
//	        mFinishedStart = true;
//	    }
//


//		 public void setListAdapter(ListAdapter adapter) {
//		        synchronized (this) {
//		            ensureList();
//		            mAdapter = adapter;
//		            mList.setAdapter(adapter);
//		        }
//		    }


		
//		@Override
//		 detecta click en la pantalla
//		protected void onListItemClick(ListView l, View v, int position, long id) {
//		Sting sting = stingList.get(position);
//		Log.d(TAG, sting.getLinks().get("self").getTarget());
//
//		Intent intent = new Intent(this, StingDetailActivity.class);
//		intent.putExtra("url", sting.getLinks().get("self").getTarget());
//		startActivity(intent);
//		}

//		@Override
//		public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.beeter_actions, menu);
//		return true;
//		}

//		@Override
//		public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.miWrite:
//			Intent intent = new Intent(this, WriteStingActivity.class);
//			startActivity(intent);
//			return true;
//
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//
//		}

}
