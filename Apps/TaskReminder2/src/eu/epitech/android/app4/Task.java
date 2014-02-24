package eu.epitech.android.app4;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {
	private String status;
	private String name;
	
	public Task(String status, String name) {
		this.status = status;
		this.name = name;
	}
	
	String getStatus() {
		return status;
	}
	
	String getName() {
		return name;
	}
	
	void setStatus(String status) {
		this.status = status;
	}
	
	public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Name", name);
            obj.put("Status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
