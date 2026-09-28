package Modelo;

public class Avaliacao {
    private int id;
    private String filmeId;
    private float nota;

    public Avaliacao() {}
    public Avaliacao(String filmeId, float nota) {
        this.filmeId = filmeId;
        this.nota = nota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public String getFilmeId() {
        return filmeId;
    }

    public void setFilmeId(String filmeId) {
        this.filmeId = filmeId;
    }
}
