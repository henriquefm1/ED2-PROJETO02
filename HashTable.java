public class HashTable{

    //nó que trata colisões
    private class Entrada{
        String chave;
        int frequencia;
        Entrada proximo;

        public Entrada(String chave, int frequencia){
            this.chave = chave;
            this.frequencia = frequencia;
            this.proximo = null;
        }
    }

    private Entrada[] tabela;
    private int capacidade;
    private int tamanho; //numero de palavras unicas
    private int tipoHash; // 1 -> divisao, 2 -> multiplicacao

    public HashTable(int capacidade, int tipoHash){
        this.capacidade = capacidade;
        this.tipoHash = tipoHash;
        this.tabela = new Entrada[capacidade];
        this.tamanho = 0;
    }

    //funcoes de dispersao

    private int calcularHash(String chave){
        int h = Math.abs(chave.hashCode());

        if(tipoHash == 1){
            //metodo da divisao
            return h % capacidade;
        }else{
            //metodo da multiplicacao
            double A = 0.6180;
            double fracionario = (h * A) % 1;
            return (int) (capacidade * fracionario);
        }
    }

    //insere/atualiza frequencia de uma palavra na tabela
    public void put(String chave){
        int indice = calcularHash(chave);
        Entrada atual = tabela[indice];

        //verifica se alguma palavra ja existe
        while(atual != null){
            if(atual.chave.equals(chave)){
                atual.frequencia++; //incementa
                return;
            }
            atual = atual.proximo;
        }

        //se nao existe, cria uma nova entrada no inicio da lista 
        Entrada novaEntrada = new Entrada(chave, 1);
        novaEntrada.proximo = tabela[indice];
        tabela[indice] = novaEntrada;
        tamanho++;
    }

    //retorna a frequencia de uma palavra
    public int get(String chave){
        int indice = calcularHash(chave);
        Entrada atual = tabela[indice];

        while (atual != null){
            if(atual.chave.equals(chave)){
                return atual.frequencia;
            }
            atual = atual.proximo;
        }
        return 0; //se nao encontra nenhuma frequencia
    }

    //retorna todas as palavras unicas (util para calculo de similaridade)
    public String[] obterTodasChaves(){
        String[] chaves = new String[tamanho];
        int index = 0;
        for(int i = 0; i < capacidade; i++){
            Entrada atual = tabela[i];
            while(atual != null){
                chaves[index++] = atual.chave;
            }
        }
        return chaves;
    }
}