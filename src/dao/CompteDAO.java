package dao;

import entity.Compte;
import entity.CompteCourant;
import entity.CompteEpargne;
import utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompteDAO {

    public void create(Compte compte) {
        String sql = "INSERT INTO compte (numero, solde, id_client, type_compte, decouvert_autorise, taux_interet) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, compte.getNumero());
            stmt.setDouble(2, compte.getSolde());
            stmt.setInt(3, compte.getIdClient());

            if (compte instanceof CompteCourant courant) {
                stmt.setString(4, "COURANT");
                stmt.setDouble(5, courant.getDecouvertAutorise());
                stmt.setNull(6, Types.DECIMAL);
            } else if (compte instanceof CompteEpargne epargne) {
                stmt.setString(4, "EPARGNE");
                stmt.setNull(5, Types.DECIMAL);
                stmt.setDouble(6, epargne.getTauxInteret());
            }

            stmt.executeUpdate();
            System.out.println("Compte ajouté !");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Compte findById(int id) {
        String sql = "SELECT * FROM compte WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String type = rs.getString("type_compte");
                int compteId = rs.getInt("id");
                String numero = rs.getString("numero");
                double solde = rs.getDouble("solde");
                int idClient = rs.getInt("id_client");

                if ("COURANT".equals(type)) {
                    double decouvert = rs.getDouble("decouvert_autorise");
                    return new CompteCourant(compteId, numero, solde, idClient, decouvert);
                } else {
                    double taux = rs.getDouble("taux_interet");
                    return new CompteEpargne(compteId, numero, solde, idClient, taux);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return null;
    }

    public List<Compte> findByClient(int idClient){
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM compte WHERE id_client = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type_compte");
                int compteId = rs.getInt("id");
                String numero = rs.getString("numero");
                double solde = rs.getDouble("solde");
                int clientId = rs.getInt("id_client");

                if ("COURANT".equals(type)) {
                    double decouvert = rs.getDouble("decouvert_autorise");
                    comptes.add(new CompteCourant(compteId, numero, solde, clientId, decouvert));
                } else {
                    double taux = rs.getDouble("taux_interet");
                    comptes.add(new CompteEpargne(compteId, numero, solde, clientId, taux));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return comptes;
    }


    public List<Compte> findAll() {
        List<Compte> list = new ArrayList<>();
        String sql = "SELECT * FROM compte";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String numero = rs.getString("numero");
                double solde = rs.getDouble("solde");
                int idClient = rs.getInt("idClient");
                String type = rs.getString("typeCompte");

                if ("COURANT".equalsIgnoreCase(type)) {
                    double decouvert = rs.getDouble("decouvert");
                    list.add(new CompteCourant(id, numero, solde, idClient, decouvert));
                } else {
                    double taux = rs.getDouble("taux");
                    list.add(new CompteEpargne(id, numero, solde, idClient, taux));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur findAll comptes : " + e.getMessage());
        }
        return list;
    }
}
