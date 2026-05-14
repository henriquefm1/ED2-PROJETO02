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
import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class Documento {

    private String nome;
    private HashTable vocabulario;

    // capacidade padrão da tabela; ajuste conforme o tamanho esperado dos docs
    private static final int CAPACIDADE_TABELA = 1024;
    // tipoHash: 1 = divisão, 2 = multiplicação
    private static final int TIPO_HASH = 1;

    public Documento(String caminhoArquivo) throws IOException {
        this.nome       = Path.of(caminhoArquivo).getFileName().toString();
        this.vocabulario = new HashTable(CAPACIDADE_TABELA, TIPO_HASH);
        processar(caminhoArquivo);
    }

    private void processar(String caminho) throws IOException {
        String conteudo = Files.readString(Path.of(caminho));
        // mantém apenas letras e espaços, converte para minúsculas
        String limpo = conteudo.toLowerCase().replaceAll("[^a-záàâãéèêíïóôõöúüçñ ]", " ");
        String[] tokens = limpo.split("\\s+");
        for (String token : tokens) {
            if (!token.isEmpty()) {
                vocabulario.put(token);
            }
        }
    }

    public String getNome()           { return nome;        }
    public HashTable getVocabulario() { return vocabulario; }
}
