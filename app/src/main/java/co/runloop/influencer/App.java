package co.runloop.influencer;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class App extends Application {

    private static App instance;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        LeakCanary.install(this);
        LeakCanary.enableDisplayLeakActivity(this);
    }
}
