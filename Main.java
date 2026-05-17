import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Inicia o cronômetro para o seu relatório
        long tempoInicio = System.currentTimeMillis();

        if (args.length < 3) {
            System.err.println("Uso: java Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]");
            return;
        }

        String diretorioStr = args[0];
        double limiar = Double.parseDouble(args[1]);
        String modo = args[2].toLowerCase();
        
        //(1 = Divisao, 2 = Multiplicacao)
        int tipoHash = 2; 

        StringBuilder log = new StringBuilder();
        log.append("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS\n");

        //variáveis para coletar estatísticas no final
        int totalColisoes = 0;
        ArvoreAVL avl = new ArvoreAVL();

        if (modo.equals("busca")) {
            if (args.length < 5) {
                System.err.println("Para o modo busca, informe os dois arquivos: java Main <dir> 0.0 busca doc1.txt doc2.txt");
                return;
            }
            String arq1 = args[3];
            String arq2 = args[4];

            //usando Paths para evitar erros de barra (/) no Windows/Linux
            Documento d1 = new Documento(Paths.get(diretorioStr, arq1).toString(), tipoHash);
            Documento d2 = new Documento(Paths.get(diretorioStr, arq2).toString(), tipoHash);

            double sim = ComparadorDeDocumentos.calcularCosseno(d1, d2);
            
            log.append(String.format("Comparando: %s <-> %s\n", arq1, arq2));
            log.append(String.format("Similaridade calculada: %.2f\n", sim).replace(",", "."));
            log.append("Métrica utilizada: Cosseno\n");

            totalColisoes = d1.getVocabulario().getColisoes() + d2.getVocabulario().getColisoes();

        } else {
            //modos "lista" e "topk"
            List<Path> caminhos;
            try {
                caminhos = listarArquivosTxt(diretorioStr);
            } catch (IOException e) {
                System.err.println("Erro ao ler a pasta: " + e.getMessage());
                return;
            }

            List<Documento> documentos = new ArrayList<>();
            for (Path p : caminhos) {
                Documento doc = new Documento(p.toString(), tipoHash);
                documentos.add(doc);
                totalColisoes += doc.getVocabulario().getColisoes();
            }

            int totalPares = 0;

            for (int i = 0; i < documentos.size(); i++) {
                for (int j = i + 1; j < documentos.size(); j++) {
                    Documento d1 = documentos.get(i);
                    Documento d2 = documentos.get(j);

                    double sim = ComparadorDeDocumentos.calcularCosseno(d1, d2);
                    Resultado res = new Resultado(d1.getNome(), d2.getNome(), sim);

                    avl.inserir(res);
                    totalPares++;
                }
            }

            log.append("Total de documentos processados: ").append(documentos.size()).append("\n");
            log.append("Total de pares comparados: ").append(totalPares).append("\n");
            log.append("Função hash utilizada: ").append(tipoHash == 2 ? "hashMultiplicativo" : "hashDivisao").append("\n");
            log.append("Métrica de similaridade: Cosseno\n");

            List<Resultado> ranking = avl.emOrdemDecrescente();

            if (modo.equals("lista")) {
                log.append(String.format("Pares com similaridade >=%.2f:\n", limiar).replace(",", "."));
                
                List<Resultado> menores = new ArrayList<>();
                for (Resultado r : ranking) {
                    if (r.getSimilaridade() >= limiar) {
                        log.append(r.toString()).append("\n");
                    } else {
                        menores.add(r);
                    }
                }
                
                log.append("Pares com menor similaridade:\n");
                for (Resultado r : menores) {
                    log.append(r.toString()).append("\n");
                }

            } else if (modo.equals("topk")) {
                int k = 5; //padrão
                if (args.length >= 4) {
                    k = Integer.parseInt(args[3]);
                }
                
                //adaptação para o formato de saída
                for (int i = 0; i < Math.min(k, ranking.size()); i++) {
                    log.append(ranking.get(i).toString()).append("\n");
                }
            }
        }

        //imprime o log oficial na tela
        System.out.print(log.toString());

        //grava no arquivo (resultado.txt) oficial
        try (FileWriter writer = new FileWriter("resultado.txt")) {
            writer.write(log.toString());
        } catch (IOException e) {
            System.err.println("Erro ao gravar arquivo resultado.txt: " + e.getMessage());
        }

        //saida
        long tempoFim = System.currentTimeMillis();
        
        System.out.println("\n\n===========================================");
        System.out.println("   DADOS PARA O RELATÓRIO EXPERIMENTAL     ");
        System.out.println("===========================================");
        System.out.printf("Tempo total de execucao: %d ms%n", (tempoFim - tempoInicio));
        System.out.printf("Total de Colisoes na Hash: %d%n", totalColisoes);
        System.out.printf("Rotacoes Simples (AVL): %d%n", avl.getTotalRotacoesSimples());
        System.out.printf("Rotacoes Duplas (AVL): %d%n", avl.getTotalRotacoesDuplas());
        System.out.printf("Total de Rotacoes (AVL): %d%n", avl.getTotalRotacoes());
        System.out.println("===========================================\n");
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
