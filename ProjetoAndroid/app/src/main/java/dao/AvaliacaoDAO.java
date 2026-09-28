package dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import Modelo.Avaliacao;
import Modelo.FilmeArrayAdapter;

public class AvaliacaoDAO {

    private final DatabaseReference db;

    public AvaliacaoDAO() {
        db = FirebaseDatabase.getInstance().getReference("Avaliacoes");
    }

    // INSERT (equivalente ao SQLite insert)
    public void insert(Avaliacao a) {
        String id = db.push().getKey(); // gera ID único

        if (id != null) {
            db.child(id).setValue(a);
        }
    }

    // DELETE
    public void delete(String id) {
        db.child(id).removeValue();
    }

    // PEGAR ÚLTIMA NOTA DO FILME
    public void getNotaAtual(String filmeId, NotaCallback callback) {

        db.orderByChild("filmeId")
                .equalTo(filmeId)
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        float nota = 0;

                        for (DataSnapshot child : snapshot.getChildren()) {
                            Avaliacao a = child.getValue(Avaliacao.class);
                            if (a != null) {
                                nota = a.getNota();
                            }
                        }

                        callback.onResult(nota);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        callback.onResult(0);
                    }
                });
    }
    // CALLBACK (substitui retorno síncrono do SQLite)
    public interface NotaCallback {
        void onResult(float nota);
    }
    public void getNotas(NotasCallback callback) {

        db.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Map<String, Float> map = new HashMap<>();

                for (DataSnapshot snap : snapshot.getChildren()) {

                    Avaliacao a = snap.getValue(Avaliacao.class);

                    if (a != null) {
                        // sempre sobrescreve → fica a última encontrada
                        map.put(String.valueOf(a.getFilmeId()), a.getNota());
                    }
                }

                callback.onResult(map);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onResult(new HashMap<>());
            }
        });
    }

    public interface NotasCallback {
        void onResult(Map<String, Float> notas);
    }
    public void getMedias(NotasCallback callback) {

        db.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Map<String, Float> soma = new HashMap<>();
                Map<String, Integer> total = new HashMap<>();

                for (DataSnapshot snap : snapshot.getChildren()) {

                    Avaliacao a = snap.getValue(Avaliacao.class);

                    if (a != null) {

                        String id = a.getFilmeId();

                        float atualSoma = soma.containsKey(id) ? soma.get(id) : 0f;
                        int atualTotal = total.containsKey(id) ? total.get(id) : 0;

                        soma.put(id, atualSoma + a.getNota());
                        total.put(id, atualTotal + 1);
                    }
                }

                Map<String, Float> medias = new HashMap<>();

                for (String id : soma.keySet()) {
                    medias.put(id, soma.get(id) / total.get(id));
                }

                callback.onResult(medias);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onResult(new HashMap<>());
            }
        });
    }

}