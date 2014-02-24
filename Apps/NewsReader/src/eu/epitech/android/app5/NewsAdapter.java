package eu.epitech.android.app5;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {
	List<News> news;
	Context context;
	
	public NewsAdapter(Context context, List<News> news) {
		this.news = news;
		this.context = context;
	}
	
	public void update(List<News> news) {
		this.news = news;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return news.size();
	}

	@Override
	public Object getItem(int arg0) {
		return news.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		LayoutInflater li = LayoutInflater.from(context);
		View v = li.inflate(android.R.layout.two_line_list_item, null);
		TextView tv1 = (TextView)v.findViewById(android.R.id.text1);
		TextView tv2 = (TextView)v.findViewById(android.R.id.text2);
		News n = (News)getItem(arg0);
		tv1.setText(n.getTitle());
		String content = Html.fromHtml(n.getContent()).toString().replace((char) 65532, (char) 32).trim();
		tv2.setText(content);
		return v;
	}
}
