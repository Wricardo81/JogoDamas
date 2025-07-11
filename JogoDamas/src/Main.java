import logica.JogoDamas;
import util.ConsoleInput;

public class Main {
    public static void main(String[] args) {
        JogoDamas jogo = new JogoDamas();
        jogo.iniciarJogo();
        ConsoleInput.fecharScanner(); // Fechar o scanner ao final do jogo
    }
}