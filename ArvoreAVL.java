import Java.util.ArrayList;
import java.util.Last

public class ArvoreAVL{
    
    private class No{
        double chave;
        List <Resultado> pares;
        No esquerdo, direito;
        int altura;

        No(double chave, Resultado, resultado){
            this.chave = chave;
            this.pares = new ArrayList<>();
            this.pares.add(resultado);
            this.altura = 1;
        }
    }

    private No raiz;
    private int TotalRotacoesSimples;
    private int TotalRotacoesDuplas;

    public int Inserir(Resultado, resultado){
        int[] rotacoesNaInsercao = {0, 0};
        raiz = inserirRec(raiz, resultado, getSimilaridade(), resultado, rotacoesNaInsercao);

        TotalRotacoesSimples += rotacoesNaInsercao[0];
        TotalRotacoesDuplas += rotacoesNaInsercao[1];
        return rotacoesNaInsercao[0] + rotacoesNaInsercao[1];
    }

    public List<Resultado> emOrdemDescrescente(){
        List<Resultado> lista = new ArrayList<> ();
        emOrdemDescrescenteRec(raiz, lista);
        return lista;
    }

    public int getTotalRotacoesSimples(){
        return TotalRotacoesSimples;
    }
    public int getTotalRotacoesDuplas(){
        return TotalRotacoesDuplas;
    }
    public int getTotalRotacoes(){
        return TotalRotacoesDuplas + TotalRotacoesSimples;
    }

    private No inserirRec (No no, chave, Resultado resultado, int[] rot){
        if(no == null){
            return new No(chave, resultado);
        }

        if(chave < no.chave){
            no.esquerdo = inserirRec(no.esquerdo, chave, resultado, rot);
        }else if(chave > no.chave){
            no.direito = inserirRec (no.direito, chave, resultado, rot);
        }else{
            no.pares.add(resultado);
            return no;
        }

        no.altura = 1 + Math.max(altura(no.esquerdo), altura(no.direito));

        int fator = fatorBalanceamento(no);

        if(fator > 1 && chave < no.esquerdo.chave){
            rot[0] ++;
            return rotacionaDireita;
        }
        if(fator < -1 && chave > no.direito.chave){
            rot[0]++;
            return rotacionaEsquerda(no);
        }
        if(fator > 1 && chave > no.esquerdo.chave){
            rot[1]++;
            return rotacionaDireita(no);
        }
        if(fator < -1 && chave < no.direito.chave){
            rot[1]++;
            return rotacionaEsquerda(no);
        }

        return no
    }

    private void emOrdemDescrescenteRec (No no, List<Resultado> lista){
        if (no == null) return;

        emOrdemDescrescenteRec(no.direito, lista);
        lista.addAll(no.pares);
        emOrdemDescrescenteRec(no.esquerdo, lista);
    }

    private int altura (No no){
        return (no == null) ? 0 : altura(no.esquerdo) - altura(no.direito);
    }

    private No rotacionaDireita(No y){
        No x = y.esquerdo;
        No T2 = x.direito;

        x.direito = y;
        y.esquerdo = T2;

        y.altura = 1 + Math.max(altura(y.esquerdo) altura(y.direito));
        x.altura = 1 + Math.max(altura(x.esquerdo) altura(x.direito));

        return x;
    }

    private No rotacionaEsquerda(No x){
        No y = x.direito;
        No T2 = y.esquerdo;

        y.esquerdo = x;
        x.direito = T2;

        x.altura = 1 + Math.max(altura(x.esquerdo) altura(x.direito));
        y.altura = 1 + Math.max(altura(y.esquerdo) altura(y.direito));

        return y;
    }
}