package com.example.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * This class registers the parse models and initializes it so it can be used in the app.
 */

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Register parse model
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Like.class);
        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("beatrice-parstagram") // should correspond to APP_ID env variable
                .clientKey("CodepathMoveFastParse")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://beatrice-parstagram.herokuapp.com/parse/").build());
    }

}

