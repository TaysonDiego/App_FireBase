package Modelo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.projeto.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.AvaliacaoDAO;

public class FilmeArrayAdapter extends ArrayAdapter<Filme> {

    private LayoutInflater myInflater;
    private Map<String, Float> notasMap = new HashMap<>();

    public FilmeArrayAdapter(Context context,
                             int resource,
                             List<Filme> objects) {

        super(context, resource, objects);

        this.myInflater = LayoutInflater.from(context);
    }

    // 🔥 recebe notas do Firebase
    public void setNotas(Map<String, Float> notas) {
        this.notasMap = notas;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {

        ViewHolder holder;

        if (row == null) {
            row = myInflater.inflate(R.layout.activity_filme_layout, parent, false);

            holder = new ViewHolder();
            holder.nome = row.findViewById(R.id.NomeFilme);
            holder.genero = row.findViewById(R.id.GeneroFilme);
            holder.ano = row.findViewById(R.id.AnoFilme);
            holder.classificacao = row.findViewById(R.id.ClassificacaoFilme);
            holder.duracao = row.findViewById(R.id.DuracaoFilme);
            holder.imagem = row.findViewById(R.id.ImagemItem);
            holder.nota = row.findViewById(R.id.notaFilme);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Filme filme = getItem(position);

        holder.nome.setText(filme.getNome());
        holder.genero.setText(filme.getGenero());
        holder.ano.setText(String.valueOf(filme.getAno()));
        holder.classificacao.setText(filme.getClassificacao());
        holder.duracao.setText(filme.getDuracao() + " min");

        // 🔥 nota vinda do Firebase (mapa carregado antes)
        String id = filme.getId();

        Float notaAtual = notasMap.get(id);

        holder.nota.setRating(notaAtual != null ? notaAtual : 0f);
        holder.nota.setIsIndicator(true);;

        // imagem
        String base64 = filme.getImagem();

        if (base64 != null) {
            byte[] decoded = android.util.Base64.decode(base64, android.util.Base64.DEFAULT);

            Bitmap photoImg = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);

            holder.imagem.setImageBitmap(photoImg);
        }


        return row;
    }

    static class ViewHolder {
        TextView nome;
        TextView genero;
        TextView ano;
        TextView classificacao;
        TextView duracao;
        ImageView imagem;
        RatingBar nota;
    }
}