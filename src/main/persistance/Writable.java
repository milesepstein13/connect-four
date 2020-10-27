package persistance;

import org.json.JSONObject;

// Source: Json Serialization Demo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
