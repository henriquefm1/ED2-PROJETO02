import java.util.ArrayList;
import java.util.List;                                          

public class ArvoreAVL {

    private class No {
        double chave;
        List<Resultado> pares;
        No esquerdo, direito;
        int altura;

        No(double chave, Resultado resultado) {                 
            this.chave  = chave;
            this.pares  = new ArrayList<>();
            this.pares.add(resultado);
            this.altura = 1;
        }
    }

    private No raiz;
    private int totalRotacoesSimples;                          
    private int totalRotacoesDuplas;

    public int inserir(Resultado resultado) {                  
        int[] rotacoesNaInsercao = {0, 0};
        raiz = inserirRec(raiz, resultado.getSimilaridade(),   
                          resultado, rotacoesNaInsercao);

        totalRotacoesSimples += rotacoesNaInsercao[0];
        totalRotacoesDuplas  += rotacoesNaInsercao[1];
        return rotacoesNaInsercao[0] + rotacoesNaInsercao[1];
    }

    public List<Resultado> emOrdemDecrescente() {              
        List<Resultado> lista = new ArrayList<>();
        emOrdemDecrescenteRec(raiz, lista);
        return lista;
    }

    public int getTotalRotacoesSimples() { return totalRotacoesSimples; }
    public int getTotalRotacoesDuplas()  { return totalRotacoesDuplas;  }
    public int getTotalRotacoes()        { return totalRotacoesSimples + totalRotacoesDuplas; }

    private No inserirRec(No no, double chave,                 
                          Resultado resultado, int[] rot) {
        if (no == null) return new No(chave, resultado);

        if (chave < no.chave) {
            no.esquerdo = inserirRec(no.esquerdo, chave, resultado, rot);
        } else if (chave > no.chave) {
            no.direito  = inserirRec(no.direito,  chave, resultado, rot);
        } else {
            no.pares.add(resultado);
            return no;
        }

        no.altura = 1 + Math.max(altura(no.esquerdo), altura(no.direito));

        int fator = fatorBalanceamento(no);

        // LL — simples direita
        if (fator > 1 && chave < no.esquerdo.chave) {
            rot[0]++;
            return rotacionarDireita(no);                      
        }
        // RR — simples esquerda
        if (fator < -1 && chave > no.direito.chave) {
            rot[0]++;
            return rotacionarEsquerda(no);
        }
        // LR — dupla esquerda-direita
        if (fator > 1 && chave > no.esquerdo.chave) {
            rot[1]++;
            no.esquerdo = rotacionarEsquerda(no.esquerdo);   
            return rotacionarDireita(no);
        }
        // RL — dupla direita-esquerda
        if (fator < -1 && chave < no.direito.chave) {
            rot[1]++;
            no.direito = rotacionarDireita(no.direito);        
            return rotacionarEsquerda(no);
        }

        return no;                                            
    }

    private void emOrdemDecrescenteRec(No no, List<Resultado> lista) {
        if (no == null) return;
        emOrdemDecrescenteRec(no.direito, lista);
        lista.addAll(no.pares);
        emOrdemDecrescenteRec(no.esquerdo, lista);
    }

    private int altura(No no) {
        return (no == null) ? 0 : no.altura;                 
    }

    private int fatorBalanceamento(No no) {
        return (no == null) ? 0 : altura(no.esquerdo) - altura(no.direito);
    }

    private No rotacionarDireita(No y) {
        No x  = y.esquerdo;
        No T2 = x.direito;
        x.direito  = y;
        y.esquerdo = T2;
        y.altura = 1 + Math.max(altura(y.esquerdo), altura(y.direito));
        x.altura = 1 + Math.max(altura(x.esquerdo), altura(x.direito));
        return x;
    }

    private No rotacionarEsquerda(No x) {
        No y  = x.direito;
        No T2 = y.esquerdo;
        y.esquerdo = x;
        x.direito  = T2;
        x.altura = 1 + Math.max(altura(x.esquerdo), altura(x.direito)); 
        y.altura = 1 + Math.max(altura(y.esquerdo), altura(y.direito));
        return y;
    }
}