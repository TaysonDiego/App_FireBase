package dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import Modelo.Filme;

public class FilmeDAO {

    private final DatabaseReference db;

    public FilmeDAO() {
        db = FirebaseDatabase.getInstance().getReference("Filmes");
    }

    // INSERT
    public void insert(Filme f) {
        String id = db.push().getKey();

        if (id != null) {
            f.setId(id);
            db.child(id).setValue(f);
        }
    }

    // UPDATE
    public void update(String id, Filme f) {
        db.child(id).setValue(f);
    }

    // DELETE
    public void delete(String id) {
        db.child(id).removeValue();
    }

    // GET BY ID
    public void getById(String id, FilmeCallback callback) {
        db.child(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Filme f = task.getResult().getValue(Filme.class);
                callback.onResult(f);
            } else {
                callback.onResult(null);
            }
        });
    }

    // GET ALL
    public void getAll(FilmesCallback callback) {
        db.get().addOnCompleteListener(task -> {
            List<Filme> lista = new ArrayList<>();

            if (task.isSuccessful()) {
                for (DataSnapshot snap : task.getResult().getChildren()) {
                    Filme f = snap.getValue(Filme.class);
                    if (f != null) {
                        f.setId(snap.getKey());
                        lista.add(f);
                    }
                }
            }

            callback.onResult(lista);
        });
    }

    // CALLBACKS (substituem retorno do SQLite)

    public interface FilmeCallback {
        void onResult(Filme filme);
    }

    public interface FilmesCallback {
        void onResult(List<Filme> filmes);
    }
}