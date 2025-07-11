package modelo;

public class Peca {
    private CorPeca cor;
    private TipoPeca tipo;
    private Posicao posicao; // A posição atual da peça

    public Peca(CorPeca cor, Posicao posicao) {
        this.cor = cor;
        this.tipo = TipoPeca.NORMAL; // Começa como peça normal
        this.posicao = posicao;
    }

    public CorPeca getCor() {
        return cor;
    }

    public TipoPeca getTipo() {
        return tipo;
    }

    public void setTipo(TipoPeca tipo) {
        this.tipo = tipo;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }

    // Método para verificar se a peça pode ser promovida a Dama
    public boolean podeVirarDama(int tamanhoTabuleiro) {
        if (tipo == TipoPeca.NORMAL) {
            if (cor == CorPeca.BRANCA && posicao.getLinha() == 0) { // Última linha para Brancas (topo)
                return true;
            }
            if (cor == CorPeca.PRETA && posicao.getLinha() == tamanhoTabuleiro - 1) { // Última linha para Pretas (base)
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String simbolo = "";
        if (cor == CorPeca.BRANCA) {
            simbolo = (tipo == TipoPeca.NORMAL) ? "B" : "D"; // Branco Normal, Dama Branca
        } else {
            simbolo = (tipo == TipoPeca.NORMAL) ? "P" : "K"; // Preto Normal, Dama Preta
        }
        return simbolo;
    }
}