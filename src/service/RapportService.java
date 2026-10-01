package service;

import dao.ClientDAO;
import dao.CompteDAO;
import dao.TransactionDAO;
import entity.Client;
import entity.Compte;
import entity.Transaction;
import enums.TypeTransaction;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class RapportService {

    private final ClientDAO clientDAO = new ClientDAO();
    private final CompteDAO compteDAO = new CompteDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    // ========== TOP 5 CLIENTS PAR SOLDE ==========
    public List<Client> top5ClientsParSolde() {
        List<Client> clients = clientDAO.getALl(); 
        return clients.stream()
                .sorted((c1, c2) -> {
                    double solde1 = calculerSoldeTotalClient(c1.id());
                    double solde2 = calculerSoldeTotalClient(c2.id());
                    return Double.compare(solde2, solde1); // décroissant
                })
                .limit(5)
                .collect(Collectors.toList());
    }

    private double calculerSoldeTotalClient(int idClient) {
        return compteDAO.findByClient(idClient).stream()
                .mapToDouble(Compte::getSolde)
                .sum();
    }

    // ========== COMPTES INACTIFS ==========
    // comptes sans transaction depuis X jours
    public List<Compte> comptesInactifs(int jours) {
        List<Compte> tousComptes = compteDAO.findAll(); // il faut que cette méthode existe
        LocalDateTime limite = LocalDateTime.now().minusDays(jours);

        return tousComptes.stream()
                .filter(compte -> {
                    List<Transaction> txs = transactionDAO.findByAccount(compte.getId());
                    if (txs.isEmpty()) return true;
                    LocalDateTime derniere = txs.stream()
                            .map(Transaction::date)
                            .max(LocalDateTime::compareTo)
                            .orElse(LocalDateTime.MIN);
                    return derniere.isBefore(limite);
                })
                .collect(Collectors.toList());
    }

    // ========== TRANSACTIONS SUSPECTES ==========
    public List<Transaction> transactionsSuspectes(double seuil) {
        List<Transaction> toutes = transactionDAO.findAll();

        return toutes.stream()
                .filter(t -> t.montant() > seuil)
                .collect(Collectors.toList());
    }

    // ========== RAPPORT PAR TYPE ==========
    public Map<TypeTransaction, Long> nombreTransactionsParType() {
        return transactionDAO.findAll().stream()
                .collect(Collectors.groupingBy(
                        Transaction::type,
                        Collectors.counting()
                ));
    }

    // ========== ALERTES SOLDE BAS ==========
    public List<Compte> comptesSoldeBas(double seuil) {
        return compteDAO.findAll().stream()
                .filter(c -> c.getSolde() < seuil)
                .collect(Collectors.toList());
    }
}