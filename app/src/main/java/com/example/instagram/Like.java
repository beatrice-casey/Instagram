package com.example.instagram;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * This class parses the Like class from the Parse database.
 */

@ParseClassName("Like")
public class Like extends Post {

    public static final String KEY_USER = "User";

    public static final String KEY_POST = "Post";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseObject getPost() {return getParseObject(KEY_POST); }

    public void setPost(ParseObject post) { put(KEY_POST, post);}
}
