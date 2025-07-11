package modelo;

// Classe para representar uma coordenada (linha, coluna)
public class Posicao {
    private int linha;
    private int coluna;

    public Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    // Sobrescreve equals e hashCode para permitir comparações de Posicao
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posicao posicao = (Posicao) o;
        return linha == posicao.linha && coluna == posicao.coluna;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(linha, coluna);
    }

    @Override
    public String toString() {
        return "(" + linha + ", " + coluna + ")";
    }
}