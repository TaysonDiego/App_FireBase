package Modelo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projeto.R;

import java.util.List;

import dao.AvaliacaoDAO;
import dao.FilmeDAO;

public class FilmeActivity extends AppCompatActivity {

    ListView lstFilmes;
    List<Filme> todosFilmes;
    FilmeDAO fDAO;
    FilmeArrayAdapter adapter;
    AvaliacaoDAO aDAO = new AvaliacaoDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filme);

        lstFilmes = findViewById(R.id.listFilmes);
        fDAO = new FilmeDAO();

        lstFilmes.setOnItemClickListener((parent, view, position, id) -> {

            Filme filmeSelecionado = todosFilmes.get(position);

            Intent intent = new Intent(FilmeActivity.this, AdicionarFilmeActivity.class);

            // ⚠️ ideal Firebase: passar ID (não objeto inteiro)
            intent.putExtra("filme", filmeSelecionado);

            startActivity(intent);
        });
    }

    public void btnNovo(View view) {
        Intent intent = new Intent(this, AdicionarFilmeActivity.class);
        startActivity(intent);
    }

    public void btnAvaliar(View view) {
        Intent intent = new Intent(this, AvaliacaoActivity.class);
        startActivity(intent);
    }

    public void ListarFilmes() {

        fDAO.getAll(filmes -> {

            todosFilmes = filmes;

            if (adapter == null) {
                adapter = new FilmeArrayAdapter(
                        this,
                        R.layout.activity_filme_layout,
                        todosFilmes
                );
                lstFilmes.setAdapter(adapter);
            } else {
                adapter.clear();
                adapter.addAll(todosFilmes);
            }

            aDAO.getMedias(medias -> {
                adapter.setNotas(medias);
                adapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListarFilmes();
    }

}