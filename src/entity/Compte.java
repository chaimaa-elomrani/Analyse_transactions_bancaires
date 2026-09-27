package entity;

public sealed class Compte permits CompteCourant, CompteEpargne {

    private final int id ;
    private final String numero ;
    private double solde ;
    private final int idClient ;

    public Compte(int id, String numero, double solde, int idClient) {
    }

    public int getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public double getSolde() {
        return solde;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }
}
