package Modelo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import dao.FilmeDAO;

import com.example.projeto.R;

import dao.AvaliacaoDAO;

public class AvaliacaoFilmeActivity extends AppCompatActivity {

    RatingBar avaliacao;
    TextView txtNota;
    TextView txtNomeFilme;
    ImageView imagemItem;

    Filme filmeAvaliado;
    String dadosPhoto;

    private String filmeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao_filme);

        filmeId = getIntent().getStringExtra("filmeId");

        avaliacao = findViewById(R.id.ratingBar);
        txtNota = findViewById(R.id.txtNota);
        txtNomeFilme = findViewById(R.id.txtNomeFilme);
        imagemItem = findViewById(R.id.imagemItem);


        FilmeDAO fDAO = new FilmeDAO();

        // 🔥 AQUI mudou (assíncrono)
        fDAO.getById(filmeId, filme -> {

            if (filme == null) {
                Toast.makeText(this, "Filme não encontrado", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }


            filmeAvaliado = filme;

            txtNomeFilme.setText(filme.getNome());

            String base64 = filme.getImagem();

            if (base64 != null) {
                byte[] decoded = android.util.Base64.decode(base64, android.util.Base64.DEFAULT);

                Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);

                imagemItem.setImageBitmap(bitmap);
            }
        });


        avaliacao.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            txtNota.setText("Nota: " + rating);
        });
    }

    public void btnSalvar(View view) {

        float nota = avaliacao.getRating();

        Avaliacao avaliacaoObj = new Avaliacao(filmeId, nota);

        AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
        avaliacaoDAO.insert(avaliacaoObj);

        Toast.makeText(this, "Avaliação salva com sucesso!", Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK);
        finish();
    }
}