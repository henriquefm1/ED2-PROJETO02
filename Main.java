import java.io.*
import java.nio.file.*
import java.util.*

public class Main{
    public static void(String[] args){
        String pasta = (args.length > 0) ? args[0]: "documentos";

        List<path> caminhos;
        try{
            caminhos = listarArquivosTxt(pasta);
        }catch(IOExcepetion e){
             System.err.println("Erro ao ler a pasta '" + pasta + "': " + e.getMessage());
             System.err.println ("Uso: Java main <pasta_com_documentos>");
             return;
        }

        if(caminhos.size() < 2){
            System.err.println("São necessários ao menos 2 arquivos .txt em '" + pasta + "'.");
            return;
        }

        List<Documento> documentos = new ArrayList<>()
            for (Path p : caminhos){
                try{
                    documentos.add(new Documento(p.String()));
                    System.out.println("Carregado:" + p.getFileName());
                }catch(IOExcepetion e){
                    System.err.println("Não foi possível ler '" + p + "': " + e.getMessage());
                }
            }

            System.out.println();

            ArvoreAVL avl = new ArvoreAVL();
            int totalPares = 0;

            for(int i = 0; i < documentos.size(); i++){
                for(int j = i; j < documentos.size(); j++){
                    Documento d1 = documentos.get(i);
                    Documento d2 = documentos.get(j);

                    double sim = ComparadorDeDocumentos.calcularCosseno(d1, d2);
                    Resultado res = new Resultado(d1.getNome(), d2.getNome(), sim);
                    
                    int rotnaNaInsercao = avl.Inserir(rec);
                    totalPares ++;

                    System.out.printf("  Par inserido: %-40s  rotações nesta inserção: %d%n",res, rotNaInsercao);
                }
            }
        }
        
        System.out.println("Ranking de Similaridade");

        List<Resultado> Ranking = avl.emOrdemDescrescente();
        int pos = 1;

        for(Resultado r : Ranking){
             System.out.printf("  %2d. %s%n", pos++, r);
        }

        System.out.println("Estatisticas da Arvore");
        System.out.println("Pares comparados: %d%n", totalPares);
        System.out.println("Rotacoes Simples: %d%n", avl.getTotalRotacoesSimples());
        System.out.println("Rotacoes Duplas: %d%n", avl.getTotalRotacoesDuplas());
        System.out.println("Total de Rotacoes: %d%n", avl.getTotalRotacoes());

        
    }
