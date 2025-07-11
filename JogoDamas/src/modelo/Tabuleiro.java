package modelo;

public class Tabuleiro {
    public static final int TAMANHO = 8; // Tabuleiro 8x8
    private Casa[][] casas;

    public Tabuleiro() {
        this.casas = new Casa[TAMANHO][TAMANHO];
        inicializarTabuleiro();
    }

    private void inicializarTabuleiro() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                casas[i][j] = new Casa(i, j);
            }
        }

        // Posicionar peças brancas (linhas 0, 1, 2)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                // Peças apenas em casas escuras (i+j é ímpar)
                if ((i + j) % 2 != 0) {
                    casas[i][j].setPeca(new Peca(CorPeca.BRANCA, new Posicao(i, j)));
                }
            }
        }

        // Posicionar peças pretas (linhas 5, 6, 7)
        for (int i = 5; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if ((i + j) % 2 != 0) {
                    casas[i][j].setPeca(new Peca(CorPeca.PRETA, new Posicao(i, j)));
                }
            }
        }
    }

    public Casa getCasa(int linha, int coluna) {
        if (linha >= 0 && linha < TAMANHO && coluna >= 0 && coluna < TAMANHO) {
            return casas[linha][coluna];
        }
        return null; // Fora dos limites do tabuleiro
    }

    public Casa getCasa(Posicao pos) {
        return getCasa(pos.getLinha(), pos.getColuna());
    }

    public void moverPeca(Posicao origem, Posicao destino) {
        Peca peca = getCasa(origem).getPeca();
        if (peca != null) {
            getCasa(origem).removerPeca();
            peca.setPosicao(destino); // Atualiza a posição interna da peça
            getCasa(destino).setPeca(peca);
        }
    }

    public void removerPeca(Posicao posicao) {
        getCasa(posicao).removerPeca();
    }

    public void imprimirTabuleiro() {
        System.out.println("\n  0 1 2 3 4 5 6 7");
        for (int i = 0; i < TAMANHO; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < TAMANHO; j++) {
                Casa casa = casas[i][j];
                if (casa.estaVazia()) {
                    System.out.print("- "); // Casa vazia
                } else {
                    System.out.print(casa.getPeca().toString() + " ");
                }
            }
            System.out.println();
        }
    }
}