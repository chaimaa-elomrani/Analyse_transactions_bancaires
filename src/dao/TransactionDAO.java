package dao;

import entity.Transaction;
import enums.TypeTransaction;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void create (Transaction transaction) {
        String sql = "INSERT INTO transaction (date, montant, type, lieu, id_compte) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(transaction.date()));
            stmt.setDouble(2, transaction.montant());
            stmt.setString(3, transaction.type().name());
            stmt.setString(4, transaction.lieu());
            stmt.setInt(5, transaction.idCompte());

            stmt.executeUpdate();
            System.out.println("Transaction ajoutée !");

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la transaction : " + e.getMessage());
        }
    }


    public List<Transaction> findByAccount(int idCompte) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE id_compte = ? ORDER BY date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCompte);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("id"),
                        rs.getTimestamp("date").toLocalDateTime(),
                        rs.getDouble("montant"),
                        TypeTransaction.valueOf(rs.getString("type")),
                        rs.getString("lieu"),
                        rs.getInt("id_compte")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return transactions;
    }
}