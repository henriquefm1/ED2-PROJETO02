import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Documento {
    private String nome;
    private HashTable vocabulario;
    
    // Lista básica de stop words em português. Você pode expandir!
    private static final String[] STOP_WORDS = {
        "o", "a", "os", "as", "um", "uma", "uns", "umas", 
        "de", "do", "da", "dos", "das", "em", "no", "na", 
        "nos", "nas", "para", "com", "por", "que", "é", 
        "são", "e", "ou", "se", "como"
    };

    // Construtor: recebe o caminho do arquivo e qual função hash usar
    public Documento(String caminhoArquivo, int tipoHash) {
        File arquivo = new File(caminhoArquivo);
        this.nome = arquivo.getName();
        
        // Capacidade 1009 é um número primo (bom para reduzir colisões no método da divisão)
        this.vocabulario = new HashTable(1009, tipoHash); 
        
        processarArquivo(caminhoArquivo);
    }

    private void processarArquivo(String caminho) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // 1. Normalização: Minúsculas
                linha = linha.toLowerCase();
                // 2. Normalização: Remove pontuação (mantém letras, acentos e números)
                linha = linha.replaceAll("[^a-záéíóúâêîôûãõç0-9\\s]", "");
                
                // 3. Tokenização: separa por espaços
                String[] palavras = linha.split("\\s+");
                
                for (String palavra : palavras) {
                    // Ignora strings vazias ou stop words
                    if (!palavra.isEmpty() && !ehStopWord(palavra)) {
                        // 4. Adiciona na HashTable (vai registrar a frequência internamente)
                        vocabulario.put(palavra);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo " + nome + ": " + e.getMessage());
        }
    }

    // Método auxiliar para verificar stop words sem usar Collections prontas
    private boolean ehStopWord(String palavra) {
        for (String sw : STOP_WORDS) {
            if (sw.equals(palavra)) {
                return true;
            }
        }
        return false;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public HashTable getVocabulario() {
        return vocabulario;
    }
}
