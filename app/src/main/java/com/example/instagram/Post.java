package com.example.instagram;

import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED = "createdAt";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public Like saveLike(ParseUser currentUser) {
        Like like = new Like();
        like.setPost(this);
        like.setUser(currentUser);
        like.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                  Log.e("Post", "Error saving", e);
                }
            }
        });
        return like;
    }

    public void deleteLike(Like like) {
        like.setPost(this);
        like.setUser(ParseUser.getCurrentUser());
        like.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("Post", "Error saving delete", e);
                }
            }
        });
    }

}
