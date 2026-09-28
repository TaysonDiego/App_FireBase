package connection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataBaseConnection  {
    private static DatabaseReference db;
    public static DatabaseReference getInstance() {
        if (db == null) {
            db = FirebaseDatabase.getInstance().getReference();
        }
        return db;
    }
}
