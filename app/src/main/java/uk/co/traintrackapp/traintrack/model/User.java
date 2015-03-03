package uk.co.traintrackapp.traintrack.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class User {

    public static final String FILENAME = "user.json";
    private int id;
    private String uuid;
    private String email;
    private String username;
    private int points;
    private String imageUrl;
    private ArrayList<Journey> journeys;

    public User() {
        id = 0;
        uuid = UUID.randomUUID().toString();
        email = "";
        username = "";
        points = 0;
        imageUrl = "";
        journeys = new ArrayList<>();
    }

    /**
     * Converts JSON object to user
     * @param json json
     */
    public User(JSONObject json) {
        super();
        try {
            this.id = json.getInt("id");
            this.uuid = json.getString("uuid");
            this.username = json.getString("username");
            this.email = json.getString("email");
            this.points = json.getInt("points");
            this.imageUrl = json.getString("image_url");
            JSONArray journeys = json.getJSONArray("journeys");
            for (int i = 0; i < journeys.length(); i++) {
                this.journeys.add(new Journey(journeys.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ArrayList<Journey> getJourneys() {
        return journeys;
    }

    public void setJourneys(ArrayList<Journey> journeys) {
        this.journeys = journeys;
    }

    /**
     * @return the image
     */
    public Bitmap getImage() {

        try {
            URL url = new URL(getImageUrl());
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            Utils.log(e.getMessage());
            return null;
        }
    }

    /**
     * @return the title
     */
    public String toString() {
        return getUsername();
    }

    /**
     *
     * @return jsonObject the representation of the user as JSON
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", getId());
            json.put("uuid", getUuid());
            json.put("email", getEmail());
            json.put("username", getUsername());
            json.put("points", getPoints());
            json.put("image_url", getImageUrl());
            JSONArray journeys = new JSONArray();
            for (Journey journey : getJourneys()) {
                journeys.put(journey.toJson());
            }
            json.put("journeys", journeys);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        return json;
    }

    /**
     * Saves list of journeys file
     * @param context the context in which we are saving them
     */
    public void save(Context context) {
        Utils.log("SAVING: " + this.toString());
        try {
            FileOutputStream outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outputStream.write(this.toJson().toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            Utils.log(e.getMessage());
        }
    }
}
