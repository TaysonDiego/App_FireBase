package Modelo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projeto.R;

import java.util.ArrayList;
import java.util.List;

import dao.AvaliacaoDAO;
import dao.FilmeDAO;

public class AvaliacaoActivity extends AppCompatActivity {

    ListView lstFilmes;
    List<Filme> todosFilmes;
    FilmeDAO fDAO;
    AvaliacaoDAO aDAO = new AvaliacaoDAO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);

        lstFilmes = findViewById(R.id.listFilmes);
        fDAO = new FilmeDAO();

        lstFilmes.setOnItemClickListener((parent, view, position, id) -> {
            Filme filmeSelecionado = todosFilmes.get(position);

            Intent intent = new Intent(this, AvaliacaoFilmeActivity.class);
            intent.putExtra("filmeId", filmeSelecionado.getId());

            startActivity(intent);
        });
    }

    public void ListarFilmes() {

        fDAO.getAll(filmes -> {

            todosFilmes = filmes;

            FilmeArrayAdapter adapter =
                    new FilmeArrayAdapter(
                            this,
                            R.layout.activity_filme_layout,
                            todosFilmes
                    );

            lstFilmes.setAdapter(adapter);

            aDAO.getMedias(media -> {
                adapter.setNotas(media);
            });
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        ListarFilmes();
    }
}