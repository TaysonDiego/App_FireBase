package Modelo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import dao.FilmeDAO;

public class AdicionarFilmeActivity extends AppCompatActivity {

    Filme filmeAtual;

    EditText edtNome, edtGenero, edtAno, edtClassificacao, edtDuracao;
    Button btnInserir, btnRemover, btnAtualizar;
    ImageView imagemFilme;

    String dadosPhoto;
    String base64Image;
    FilmeDAO fDAO;

    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_filme);
        fDAO = new FilmeDAO();
        filmeAtual = (Filme) getIntent().getSerializableExtra("filme");

        if (filmeAtual == null) {
            filmeAtual = new Filme("","","",0,"",0,null);
        }

        edtNome = findViewById(R.id.edtNome);
        edtGenero = findViewById(R.id.edtGenero);
        edtAno = findViewById(R.id.edtAno);
        edtClassificacao = findViewById(R.id.edtClassificacao);
        edtDuracao = findViewById(R.id.edtDuracao);

        edtNome.setText(filmeAtual.getNome());
        edtGenero.setText(filmeAtual.getGenero());
        edtAno.setText(String.valueOf(filmeAtual.getAno()));
        edtClassificacao.setText(filmeAtual.getClassificacao());
        edtDuracao.setText(String.valueOf(filmeAtual.getDuracao()));

        edtNome.requestFocus();

        btnInserir = findViewById(R.id.btnInserir);
        btnRemover = findViewById(R.id.btnRemover);
        btnAtualizar = findViewById(R.id.btnAtualizar);

        imagemFilme = findViewById(R.id.imagemFilme);

        String base64 = filmeAtual.getImagem();

        if (base64 != null) {
            byte[] decoded = android.util.Base64.decode(base64, android.util.Base64.DEFAULT);

            Bitmap photoImg = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);

            if (photoImg != null) {
                imagemFilme.setImageBitmap(photoImg);
            }
        }

        boolean hasId = filmeAtual.getId() != null && !filmeAtual.getId().isEmpty();

        btnInserir.setVisibility(hasId ? View.GONE : View.VISIBLE);
        btnAtualizar.setVisibility(hasId ? View.VISIBLE : View.GONE);
        btnRemover.setVisibility(hasId ? View.VISIBLE : View.GONE);

        registerGalleryLauncher();
    }

    public void AtualizarTap(View view) {

        try {

            filmeAtual.setNome(edtNome.getText().toString());
            filmeAtual.setGenero(edtGenero.getText().toString());
            filmeAtual.setClassificacao(edtClassificacao.getText().toString());
            filmeAtual.setAno(Integer.parseInt(edtAno.getText().toString()));
            filmeAtual.setDuracao(Integer.parseInt(edtDuracao.getText().toString()));

            fDAO.update(String.valueOf(filmeAtual.getId()), filmeAtual);

            Toast.makeText(this, "Dados Atualizados!", Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK);
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show();
        }
    }

    public void DeletarTap(View view) {

        fDAO.delete(String.valueOf(filmeAtual.getId()));

        Toast.makeText(this, "Filme Deletado!", Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK);
        finish();
    }
    public void InserirTap(View view) {

        try {

            Filme filminho = new Filme(
                    "",
                    edtNome.getText().toString(),
                    edtGenero.getText().toString(),
                    Integer.parseInt(edtAno.getText().toString()),
                    edtClassificacao.getText().toString(),
                    Integer.parseInt(edtDuracao.getText().toString()),
                    base64Image
            );

            fDAO.insert(filminho);

            Toast.makeText(this, "Filme adicionado!", Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK);
            finish();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void registerGalleryLauncher(){

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {

                    if(uri != null){

                        try{

                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), uri);

                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                                    bitmap,300,300,true);

                            setPhotoData(scaledBitmap);

                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }

                    }

                }
        );
    }

    private void setPhotoData(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

        String base64 = android.util.Base64.encodeToString(
                stream.toByteArray(),
                android.util.Base64.DEFAULT
        );

        imagemFilme.setImageBitmap(bitmap);

        base64Image = base64;
        filmeAtual.setImagem(base64);

        filmeAtual.setImagem(base64);
    }

    public void EscolherFotoEvt(View view){

        galleryLauncher.launch("image/*");

    }
}