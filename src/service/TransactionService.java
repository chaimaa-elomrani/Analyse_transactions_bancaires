package service;

import dao.TransactionDAO;
import entity.Transaction;
import enums.TypeTransaction;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionService {

    private final TransactionDAO transactionDAO = new TransactionDAO();

    public void addTransaction (double montant, TypeTransaction type, String lieu, int idCompte) {
        Transaction transaction = new Transaction(
                0,
                LocalDateTime.now(),
                montant,
                type,
                lieu,
                idCompte
        );
        transactionDAO.create(transaction);
    }

    public List<Transaction> trouverParCompte(int idCompte) {
        return transactionDAO.findByAccount(idCompte);
    }
}