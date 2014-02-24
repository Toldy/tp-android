package eu.epitech.android.app4;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {
	private String name;
	
	public Task(String name) {
		this.name = name;
	}
	
	String getName() {
		return name;
	}
	
	public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
