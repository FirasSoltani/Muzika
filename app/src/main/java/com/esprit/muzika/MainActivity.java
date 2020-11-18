package com.esprit.muzika;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import entities.Playlist;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class MainActivity extends AppCompatActivity {


    String appID = "muzika-zmbdq";
    private App app;
    User user;
    io.realm.Realm realmThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        //Realm_init
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(appID)
                .build());
        realmGetAccess();
        Utils.Realm dbRealm = new Utils.Realm(realmThread);
        //Realm_init_end

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //Usage
        RealmResults<Playlist> playlists = dbRealm.realmRead();
        for(Playlist pls:playlists)
        {
            Log.v("INFO",pls.toString());
        }
    }

    public void realmGetAccess()
    {
        Credentials credentials = Credentials.anonymous();

        app.login(credentials);
        Log.v("QUICKSTART", "Successfully authenticated anonymously.");
        user = app.currentUser();
        // interact with realm using your user object here

        SyncConfiguration config = new SyncConfiguration.Builder(user, "_base")

                .allowQueriesOnUiThread(true)

                .allowWritesOnUiThread(true)

                .build();
        realmThread = io.realm.Realm.getInstance(config);
        io.realm.Realm.getInstance(config);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmThread.close();
        app.currentUser().logOutAsync(result -> {
            if (result.isSuccess()) {
                Log.v("QUICKSTART", "Successfully logged out.");
            } else {
                Log.e("QUICKSTART", "Failed to log out, error: " + result.getError());
            }
        });
    }
}

