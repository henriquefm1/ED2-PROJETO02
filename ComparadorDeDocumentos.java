public class ComparadorDeDocumentos {

    public static double calcularCosseno(Documento d1, Documento d2) {
        HashTable v1 = d1.getVocabulario();
        HashTable v2 = d2.getVocabulario();
        
        String[] chaves1 = v1.obterTodasChaves();
        String[] chaves2 = v2.obterTodasChaves();
        
        double produtoEscalar = 0.0;
        double normaA = 0.0;
        double normaB = 0.0;
        
        //calcula o Produto Escalar e a Norma de A
        for (String palavra : chaves1) {
            int freqA = v1.get(palavra);
            int freqB = v2.get(palavra); //0 se d2 não tiver a palavra
            
            produtoEscalar += (freqA * freqB);
            normaA += (freqA * freqA);
        }
        
        //calcula a Norma de B (precisa iterar sobre as chaves de d2)
        for (String palavra : chaves2) {
            int freqB = v2.get(palavra);
            normaB += (freqB * freqB);
        }
        
        if (normaA == 0.0 || normaB == 0.0) {
            return 0.0;
        }
        
        return produtoEscalar / (Math.sqrt(normaA) * Math.sqrt(normaB));
    }
}