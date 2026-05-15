import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Documento {
    private String nome;
    private HashTable vocabulario;

    private static final String[] STOP_WORDS = {
        "o", "a", "os", "as", "um", "uma", "uns", "umas",
        "de", "do", "da", "dos", "das", "em", "no", "na",
        "nos", "nas", "para", "com", "por", "que", "é",
        "são", "e", "ou", "se", "como"
    };

    // Capacidade prima reduz colisões no método da divisão
    private static final int CAPACIDADE_TABELA = 1009;

    public Documento(String caminhoArquivo, int tipoHash) {
        this.nome        = new File(caminhoArquivo).getName();
        this.vocabulario = new HashTable(CAPACIDADE_TABELA, tipoHash);
        processarArquivo(caminhoArquivo);
    }

    private void processarArquivo(String caminho) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.toLowerCase();
                linha = linha.replaceAll("[^a-záéíóúâêîôûãõç0-9\\s]", "");
                String[] palavras = linha.split("\\s+");
                for (String palavra : palavras) {
                    if (!palavra.isEmpty() && !ehStopWord(palavra)) {
                        vocabulario.put(palavra);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo " + nome + ": " + e.getMessage());
        }
    }

    private boolean ehStopWord(String palavra) {
        for (String sw : STOP_WORDS) {
            if (sw.equals(palavra)) return true;
        }
        return false;
    }

    public String getNome()           { return nome;        }
    public HashTable getVocabulario() { return vocabulario; }
}