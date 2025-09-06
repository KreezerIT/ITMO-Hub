package infrastructure.presentation.consoleui.menus;

import application.services.AccountServiceImpl;
import core.exceptions.DatabaseOperationException;
import infrastructure.presentation.consoleui.ValidateInput;
import infrastructure.services.LoginServiceImpl;

import java.util.Scanner;

public class AdminMenu {
    private final AccountServiceImpl accountServiceImpl;

    public AdminMenu(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    public void show(LoginServiceImpl loginServiceImpl) throws DatabaseOperationException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nEnter system password: ");
        String password = scanner.nextLine();

        if (password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        loginServiceImpl.loginAdmin(password);
        System.out.println("\nAdmin mode activated");

        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Create Account");
            System.out.println("2. Check Account Balance");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createAccount();
                case "2" -> {
                    System.out.println("Enter AccountNumber:");
                    String accountNumber = ValidateInput.getValidatedInput("^\\d{20}$", "Invalid account number! It must be exactly 20 digits.");
                    viewBalanceByAccountNumber(accountNumber);
                }
                case "3" -> {
                    System.out.println("Exiting admin mode...");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void createAccount() throws DatabaseOperationException {
        System.out.print("Enter 20-digit account number: ");
        String accountNumber = ValidateInput.getValidatedInput("^\\d{20}$", "Invalid account number! It must be exactly 20 digits.");

        System.out.print("Enter PIN code in **** format: ");
        String pinCode = ValidateInput.getValidatedInput("^\\d{4}$", "Invalid PIN code! It must be exactly 4 digits.");

        System.out.print("Enter currency in XXX format: ");
        String currency = ValidateInput.getValidatedInput("^[A-Z]{3}$", "Invalid currency! It must be exactly 3 uppercase letters.");

        accountServiceImpl.createAccount(accountNumber, pinCode, currency);
        System.out.println("Account created successfully!");
    }

    private void viewBalanceByAccountNumber(String accountNumber) {
        try {
            String balance = accountServiceImpl.getBalance(accountNumber);
            System.out.println("Account number: " + accountNumber + " Balance: " + balance);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
