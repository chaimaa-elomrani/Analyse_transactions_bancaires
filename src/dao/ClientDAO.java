package dao;

import entity.Client;
import utils.DatabaseConnection;
import java.sql.*;

public class ClientDAO {

    public void create(Client client){
        String sql = "INSERT INO client (nom , email) VALUES (?,?)";

        try(Connection conn = DatabaseConnection.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, client.nom());
            stmt.setString(2, client.email());
            stmt.executeUpdate();

            System.out.println("Client ajouté avec succes");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du client : " + e.getMessage());
        }
    }
}
