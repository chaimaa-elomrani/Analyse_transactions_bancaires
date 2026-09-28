package dao;

import entity.Compte;
import entity.CompteCourant;
import entity.CompteEpargne;
import utils.DatabaseConnection;
import java.sql.*;

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
}
