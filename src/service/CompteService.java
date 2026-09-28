package service;

import dao.CompteDAO;
import entity.Compte;
import entity.CompteCourant;
import entity.CompteEpargne;

import java.util.List;

public class CompteService {

    private final CompteDAO compteDAO = new CompteDAO();

    public void creerCompteCourant(String numero, double solde, int idClient, double decouvert) {
        CompteCourant compte = new CompteCourant(0, numero, solde, idClient, decouvert);
        compteDAO.create(compte);
    }

    public void creerCompteEpargne(String numero, double solde, int idClient, double taux) {
        CompteEpargne compte = new CompteEpargne(0, numero, solde, idClient, taux);
        compteDAO.create(compte);
    }

    public Compte trouverParId(int id) {
        return compteDAO.findById(id);
    }

    public List<Compte> trouverParClient(int idClient) {
        return compteDAO.findByClient(idClient);
    }
}