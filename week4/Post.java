package msku.ceng.madlab.Week3;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.BitSet;
import java.util.Locale;

public class Post {
    private String message;
    private Location location;
    private Bitmap image;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }



}
