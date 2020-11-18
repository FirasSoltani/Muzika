package Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import entities.Playlist;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class Realm {


    RealmResults<Playlist> all;
    io.realm.Realm backgroundThreadRealm;

    public Realm(io.realm.Realm backgroundThreadRealm) {
        this.backgroundThreadRealm = backgroundThreadRealm;
    }






    public void realmInsert()
    {
        Playlist playlist = new Playlist();
        playlist.setName("My playlist");
        backgroundThreadRealm.executeTransaction (transactionRealm -> {
            transactionRealm.insert(playlist);
        });
    }

    public RealmResults<Playlist> realmRead()
    {
        backgroundThreadRealm.executeTransaction(transactionRealm -> {
            all = transactionRealm.where(Playlist.class).findAll();
        });
        return all;
    }

}
