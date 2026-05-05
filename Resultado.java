public class Resultado {
    private String documento1;
    private String documento2;
    private double similaridade;

    public Resultado(String documento1, String documento2, double similaridade) {
        this.documento1 = documento1;
        this.documento2 = documento2;
        this.similaridade = similaridade;
    }

    public String getDocumento1() { 
        return documento1; 
        }
    public String getDocumento2() { 
        return documento2; 
        }
    public double getSimilaridade() { 
        return similaridade; 
        }

    @Override
    public String toString() {
        //formata para 2 casas decimais, conforme o exemplo do PDF
        return String.format("%s <-> %s = %.2f", documento1, documento2, similaridade).replace(",", ".");
    }
}