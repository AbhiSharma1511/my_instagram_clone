package com.appdeb.myinstagram;

import android.app.Application;
import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("8YmnB2xk9z1o5HpPhNOcvpMaMUP7lJNfkYlDMXGQ")
                // if defined
                .clientKey("iZVJnmPonkYxJYjvPpo28sn8qOwcNzSVDChv4ySI")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}