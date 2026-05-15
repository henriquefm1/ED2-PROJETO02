import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String pasta = (args.length > 0) ? args[0] : "documentos";

        List<Path> caminhos;
        try {
            caminhos = listarArquivosTxt(pasta);
        } catch (IOException e) {
            System.err.println("Erro ao ler a pasta '" + pasta + "': " + e.getMessage());
            System.err.println("Uso: java Main <pasta_com_documentos>");
            return;
        }

        if (caminhos.size() < 2) {
            System.err.println("São necessários ao menos 2 arquivos .txt em '" + pasta + "'.");
            return;
        }

        List<Documento> documentos = new ArrayList<>();
        for (Path p : caminhos) {
                documentos.add(new Documento(p.toString(), 1));         
                System.out.println("Carregado: " + p.getFileName());
        }

        System.out.println();

        ArvoreAVL avl = new ArvoreAVL();
        int totalPares = 0;

        for (int i = 0; i < documentos.size(); i++) {
            for (int j = i + 1; j < documentos.size(); j++) {       
                Documento d1 = documentos.get(i);
                Documento d2 = documentos.get(j);

                double sim = ComparadorDeDocumentos.calcularCosseno(d1, d2);
                Resultado res = new Resultado(d1.getNome(), d2.getNome(), sim);

                int rotNaInsercao = avl.inserir(res);                
                totalPares++;

                System.out.printf("  Par inserido: %-40s  rotações nesta inserção: %d%n",
                        res, rotNaInsercao);
            }
        }

        System.out.println("Ranking de Similaridade");

        List<Resultado> ranking = avl.emOrdemDecrescente();        
        int pos = 1;
        for (Resultado r : ranking) {
            System.out.printf("  %2d. %s%n", pos++, r);
        }

        System.out.println("Estatisticas da Arvore");
        System.out.printf("Pares comparados: %d%n", totalPares);   
        System.out.printf("Rotacoes Simples: %d%n", avl.getTotalRotacoesSimples());
        System.out.printf("Rotacoes Duplas: %d%n", avl.getTotalRotacoesDuplas());
        System.out.printf("Total de Rotacoes: %d%n", avl.getTotalRotacoes());
    }

    private static List<Path> listarArquivosTxt(String pasta) throws IOException {
        List<Path> lista = new ArrayList<>();
        try (var stream = Files.list(Path.of(pasta))) {
            stream.filter(p -> p.toString().endsWith(".txt"))
                  .sorted()
                  .forEach(lista::add);
        }
        return lista;
    }
}