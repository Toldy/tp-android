package eu.epitech.android.app5;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity {
	AsyncTask<Void, Void, List> a = null;
	NewsAdapter adapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ListView list = (ListView)findViewById(R.id.listView1);
		adapter = new NewsAdapter(this, new ArrayList<News>());
		list.setAdapter(adapter);
		a = new AsyncTask<Void, Void, List>() {
			@Override
			protected List doInBackground(Void... params) {
				ArrayList<News> res = new ArrayList<News>();
				try {
					URL url = new URL("http://feeds.lefigaro.fr/c/32266/f/438191/index.rss");
					   HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					   RssParser parser = new RssParser();
					try {
						return parser.parse(urlConnection.getInputStream());
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return res;
			}
			
			@Override
			protected void onPostExecute(List result) {
				adapter.update(result);
			}
		};
		a.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
