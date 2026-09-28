package dao;

import entity.Client;
import utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public Client findById(int id) {
        String sql = "SELECT * FROM client WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return null;
    }


    public List<Client> getALl(){
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";

        try(Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()){

            while(rs.next()){
                clients.add(new Client (
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email")
                ));
            }


        }catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return clients;
    }

}
