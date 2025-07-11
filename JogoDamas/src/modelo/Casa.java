package modelo;

public class Casa {
    private Posicao posicao;
    private Peca peca; // A peça que ocupa esta casa, ou null se estiver vazia

    public Casa(int linha, int coluna) {
        this.posicao = new Posicao(linha, coluna);
        this.peca = null; // Inicialmente vazia
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public Peca getPeca() {
        return peca;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
    }

    public boolean estaVazia() {
        return peca == null;
    }

    public void removerPeca() {
        this.peca = null;
    }
}