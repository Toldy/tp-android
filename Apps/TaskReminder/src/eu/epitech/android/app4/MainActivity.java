package eu.epitech.android.app4;

import java.util.ArrayList;

import android.location.Address;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class MainActivity extends Activity {
	ListView v = null;
	TaskAdapter adapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		v = (ListView)findViewById(R.id.listView1);
		adapter = new TaskAdapter(this);
		adapter.load();
		v.setOnItemLongClickListener(new OnItemLongClickListener() {
			Integer position;
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long id) {
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				Task t = (Task)adapter.getItem(pos);
				position = pos;
				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage("Do you confirm the suppression of the task '" + t.getName()+ "' ?")
				       .setTitle("Confirmation")
					   .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						               // User clicked OK button
						        	   adapter.remove(position);
									   adapter.save();
						           }
						       })
					   .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						               
						           }
						       });
				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
				return false;
			}
		});
		v.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void add_task() {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		// 2. Chain together various setter methods to set the dialog characteristics
		View view = inflater.inflate(R.layout.dialog_add_task, null);
		
		builder.setTitle("New task")
		       .setView(view)
		       .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               Log.d("AddTask", "OK");
		               EditText name = (EditText)(((AlertDialog)dialog).findViewById(R.id.editText1));
		               adapter.add(new Task(name.getText().toString()));
		               adapter.save();
		           }
		       })
		       .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   Log.d("AddTask", "Cancel");
		           }
		       });
		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_add:
				add_task();
				return true;
		}
		return false;
	}
}
