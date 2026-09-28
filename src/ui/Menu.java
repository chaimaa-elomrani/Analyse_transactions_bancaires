package ui;

import service.ClientService;
import service.CompteService;
import service.TransactionService;

import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private final ClientService clientService = new ClientService();
    private final CompteService compteService = new CompteService();
    private final TransactionService transactionService = new TransactionService();

    public void start () {
        while (true) {
            afficherMenu();
            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1 -> clientService.addClient();
                case 2 -> compteService.createAccCourant();
                case 3 -> transactionService.addTransaction();
                case 4 -> clientService.getAll();
                case 0 -> {
                    System.out.println("Au revoir !");
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void afficherMenu() {
        System.out.println("\n===== MENU BANQUE =====");
        System.out.println("1. Ajouter un client");
        System.out.println("2. Créer un compte");
        System.out.println("3. Ajouter une transaction");
        System.out.println("4. Lister les clients");
        System.out.println("0. Quitter");
        System.out.print("Votre choix : ");
    }

    private void ajouterClient() {
        System.out.print("Nom du client : ");
        String nom = scanner.nextLine();

        System.out.print("Email du client : ");
        String email = scanner.nextLine();

        clientService.addClient(nom, email);
        System.out.println("Client ajouté !");
    }

    private void creerCompte() {
        System.out.print("ID du client : ");
        int idClient = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Numéro du compte : ");
        String numero = scanner.nextLine();

        System.out.print("Solde initial : ");
        double solde = scanner.nextDouble();

        System.out.print("Type (1 = Courant, 2 = Épargne) : ");
        int type = scanner.nextInt();

        if (type == 1) {
            System.out.print("Découvert autorisé : ");
            double decouvert = scanner.nextDouble();
            compteService.createAccCourant(numero, solde, idClient, decouvert);
        } else {
            System.out.print("Taux d'intérêt : ");
            double taux = scanner.nextDouble();
            compteService.creerCompteEpargne(numero, solde, idClient, taux);
        }

        System.out.println("Compte créé !");
    }

    private void ajouterTransaction() {
        System.out.print("ID du compte : ");
        int idCompte = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Montant : ");
        double montant = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Type (VERSEMENT, RETRAIT, VIREMENT) : ");
        String typeStr = scanner.nextLine().toUpperCase();

        System.out.print("Lieu : ");
        String lieu = scanner.nextLine();

        transactionService.addTransaction(
                montant,
                enums.TypeTransaction.valueOf(typeStr),
                lieu,
                idCompte
        );

        System.out.println("Transaction ajoutée !");
    }

    private void listerClients() {
        var clients = clientService.getAll();

        if (clients.isEmpty()) {
            System.out.println("Aucun client trouvé.");
            return;
        }

        for (var client : clients) {
            System.out.println(client.id() + " - " + client.nom() + " - " + client.email());
        }
    }
}