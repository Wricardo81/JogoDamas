package logica;

import modelo.*;
import util.ConsoleInput;

import java.util.ArrayList;
import java.util.List;

public class JogoDamas {
    private Tabuleiro tabuleiro;
    private CorPeca jogadorAtual;
    private boolean jogoAcabou;

    public JogoDamas() {
        this.tabuleiro = new Tabuleiro();
        this.jogadorAtual = CorPeca.BRANCA; // Brancas começam
        this.jogoAcabou = false;
    }

    public void iniciarJogo() {
        System.out.println("Bem-vindo ao Jogo de Damas!");
        while (!jogoAcabou) {
            tabuleiro.imprimirTabuleiro();
            System.out.println("\nÉ a vez do jogador " + (jogadorAtual == CorPeca.BRANCA ? "BRANCO (B/D)" : "PRETO (P/K)"));

            Posicao origem = pedirPosicao("Digite a linha e coluna da peça que deseja mover (ex: 0 1): ");
            if (origem == null) continue; // Usuário digitou 'sair' ou inválido

            Posicao destino = pedirPosicao("Digite a linha e coluna do destino (ex: 1 2): ");
            if (destino == null) continue; // Usuário digitou 'sair' ou inválido


            if (realizarJogada(origem, destino)) {
                // Verificar promoção a dama
                Peca pecaMovida = tabuleiro.getCasa(destino).getPeca();
                if (pecaMovida != null && pecaMovida.podeVirarDama(Tabuleiro.TAMANHO)) {
                    pecaMovida.setTipo(TipoPeca.DAMA);
                    System.out.println("Parabéns! Sua peça virou DAMA!");
                }
                trocarTurno();
            } else {
                System.out.println("Movimento inválido! Tente novamente.");
            }

            // Apenas para simplificar a demonstração, você adicionaria aqui a lógica
            // para verificar vitória/empate com contagem de peças ou movimentos válidos.
            if (ConsoleInput.gitignore.lerString("Deseja continuar? (s/n)").equalsIgnoreCase("n")) {
                jogoAcabou = true;
            }
        }
        System.out.println("Fim de Jogo!");
    }

    private Posicao pedirPosicao(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = ConsoleInput.lerString().trim();
            if (entrada.equalsIgnoreCase("sair")) {
                return null;
            }
            try {
                String[] partes = entrada.split(" ");
                if (partes.length == 2) {
                    int linha = Integer.parseInt(partes[0]);
                    int coluna = Integer.parseInt(partes[1]);
                    if (linha >= 0 && linha < Tabuleiro.TAMANHO && coluna >= 0 && coluna < Tabuleiro.TAMANHO) {
                        return new Posicao(linha, coluna);
                    }
                }
            } catch (NumberFormatException e) {
                // Ignorar, vai cair na mensagem de erro abaixo
            }
            System.out.println("Entrada inválida. Digite dois números separados por espaço (ex: 0 1) ou 'sair'.");
        }
    }


    private boolean realizarJogada(Posicao origem, Posicao destino) {
        Casa casaOrigem = tabuleiro.getCasa(origem);
        Casa casaDestino = tabuleiro.getCasa(destino);

        if (casaOrigem == null || casaDestino == null) {
            System.out.println("Posições fora do tabuleiro.");
            return false;
        }

        Peca peca = casaOrigem.getPeca();

        // 1. Verificar se há peça na origem
        if (peca == null) {
            System.out.println("Não há peça na posição de origem.");
            return false;
        }

        // 2. Verificar se a peça pertence ao jogador atual
        if (peca.getCor() != jogadorAtual) {
            System.out.println("Esta não é sua peça!");
            return false;
        }

        // 3. Verificar se o destino está vazio
        if (!casaDestino.estaVazia()) {
            System.out.println("A posição de destino já está ocupada.");
            return false;
        }

        // 4. Lógica de Movimento e Captura
        // Distância do movimento
        int deltaLinha = destino.getLinha() - origem.getLinha();
        int deltaColuna = destino.getColuna() - origem.getColuna();

        // Lógica para peças NORMAIS
        if (peca.getTipo() == TipoPeca.NORMAL) {
            // Movimento simples (uma casa diagonal)
            if (Math.abs(deltaLinha) == 1 && Math.abs(deltaColuna) == 1) {
                // Direção correta para peças normais
                if ((peca.getCor() == CorPeca.BRANCA && deltaLinha < 0) || // Brancas movem para cima (linha decresce)
                        (peca.getCor() == CorPeca.PRETA && deltaLinha > 0)) {  // Pretas movem para baixo (linha cresce)
                    tabuleiro.moverPeca(origem, destino);
                    return true;
                }
            }
            // Captura (duas casas diagonal)
            else if (Math.abs(deltaLinha) == 2 && Math.abs(deltaColuna) == 2) {
                Posicao posicaoCapturada = new Posicao(
                        origem.getLinha() + (deltaLinha / 2),
                        origem.getColuna() + (deltaColuna / 2)
                );
                Casa casaCapturada = tabuleiro.getCasa(posicaoCapturada);

                if (casaCapturada != null && !casaCapturada.estaVazia() &&
                        casaCapturada.getPeca().getCor() != peca.getCor()) { // É uma peça inimiga
                    tabuleiro.moverPeca(origem, destino);
                    tabuleiro.removerPeca(posicaoCapturada); // Remove a peça capturada
                    System.out.println("Peça capturada em " + posicaoCapturada);
                    return true;
                }
            }
        }
        // Lógica para peças DAMAS (mais complexa, permite mover e capturar várias casas)
        else if (peca.getTipo() == TipoPeca.DAMA) {
            // Damas podem mover na diagonal por múltiplas casas
            if (Math.abs(deltaLinha) == Math.abs(deltaColuna) && Math.abs(deltaLinha) > 0) {
                // Verificar se há peças no caminho
                List<Peca> pecasNoCaminho = new ArrayList<>();
                int dirLinha = deltaLinha / Math.abs(deltaLinha); // -1 para cima, 1 para baixo
                int dirColuna = deltaColuna / Math.abs(deltaColuna); // -1 para esquerda, 1 para direita

                for (int i = 1; i < Math.abs(deltaLinha); i++) {
                    Posicao posIntermediaria = new Posicao(origem.getLinha() + i * dirLinha, origem.getColuna() + i * dirColuna);
                    Casa casaIntermediaria = tabuleiro.getCasa(posIntermediaria);
                    if (casaIntermediaria != null && !casaIntermediaria.estaVazia()) {
                        pecasNoCaminho.add(casaIntermediaria.getPeca());
                    }
                }

                if (pecasNoCaminho.isEmpty()) { // Movimento simples de Dama
                    tabuleiro.moverPeca(origem, destino);
                    return true;
                } else if (pecasNoCaminho.size() == 1 && pecasNoCaminho.get(0).getCor() != peca.getCor()) {
                    // Captura de Dama: Apenas uma peça inimiga no caminho
                    Posicao posPecaCapturada = pecasNoCaminho.get(0).getPosicao();
                    if (Math.abs(destino.getLinha() - posPecaCapturada.getLinha()) == 1 &&
                            Math.abs(destino.getColuna() - posPecaCapturada.getColuna()) == 1) {
                        // O destino deve ser uma casa imediatamente após a peça capturada
                        tabuleiro.moverPeca(origem, destino);
                        tabuleiro.removerPeca(posPecaCapturada);
                        System.out.println("Peça capturada por DAMA em " + posPecaCapturada);
                        return true;
                    }
                }
            }
        }
        return false; // Movimento inválido por qualquer outra razão
    }

    private void trocarTurno() {
        jogadorAtual = (jogadorAtual == CorPeca.BRANCA) ? CorPeca.PRETA : CorPeca.BRANCA;
    }

    // Você precisará implementar a lógica de verificação de vitória de forma mais robusta,
    // por exemplo, contando as peças restantes ou verificando movimentos possíveis.
    private boolean verificarVitoria() {
        int pecasBrancas = 0;
        int pecasPretas = 0;
        for (int i = 0; i < Tabuleiro.TAMANHO; i++) {
            for (int j = 0; j < Tabuleiro.TAMANHO; j++) {
                Casa casa = tabuleiro.getCasa(i, j);
                if (!casa.estaVazia()) {
                    if (casa.getPeca().getCor() == CorPeca.BRANCA) {
                        pecasBrancas++;
                    } else {
                        pecasPretas++;
                    }
                }
            }
        }
        if (pecasBrancas == 0) {
            System.out.println("Fim de Jogo! Jogador PRETO venceu!");
            return true;
        }
        if (pecasPretas == 0) {
            System.out.println("Fim de Jogo! Jogador BRANCO venceu!");
            return true;
        }
        return false;
    }
}