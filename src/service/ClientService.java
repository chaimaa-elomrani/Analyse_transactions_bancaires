package service;

import dao.ClientDAO;
import entity.Client;

import java.util.List;

public class ClientService {

    private final ClientDAO clientDAO = new ClientDAO();

    public void addClient(String nom, String email) {
        Client client = new Client(0, nom, email); // id = 0 car la base le génère
        clientDAO.create(client);
    }

    public Client findById(int id) {
        return clientDAO.findById(id);
    }

    public List<Client> getAll() {
        return clientDAO.getALl();
    }
}