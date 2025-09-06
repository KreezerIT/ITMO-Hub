package infrastructure.presentation.consoleui.menus;

import application.services.AccountServiceImpl;
import core.exceptions.*;
import core.valueobjects.CurrencyType;
import core.valueobjects.Money;
import infrastructure.presentation.consoleui.ValidateInput;
import infrastructure.presentation.viewmodels.TransactionViewModel;
import infrastructure.services.LoginServiceImpl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserMenu {
    private final AccountServiceImpl accountServiceImpl;

    public UserMenu(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    public void show(LoginServiceImpl loginServiceImpl) throws SQLException, AccountNotFoundException, InvalidAccountException, InvalidPinCodeException, DataMappingException {
        System.out.print("Enter 20-digit account number: ");
        String accountNumber = ValidateInput.getValidatedInput("^\\d{20}$", "Invalid account number! It must be exactly 20 digits.");

        System.out.print("Enter PIN code in **** format: ");
        String pinCode = ValidateInput.getValidatedInput("^\\d{4}$", "Invalid PIN code! It must be exactly 4 digits.");

        loginServiceImpl.loginUser(accountNumber, pinCode);
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. View Transaction History");
            System.out.println("5. Exit");
            System.out.print("Your choice: ");

            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> viewBalance(accountNumber);
                case "2" -> depositMoney(accountNumber);
                case "3" -> withdrawMoney(accountNumber);
                case "4" -> viewTransactionHistory(accountNumber);
                case "5" -> {
                    System.out.println("Exiting user mode...");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void viewBalance(String accountNumber) {
        try {
            String balance = accountServiceImpl.getBalance(accountNumber);
            System.out.println("Your current balance: " + balance);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void depositMoney(String accountNumber) {
        try {
            System.out.print("Enter amount to deposit: ");
            String amountInput = ValidateInput.getValidatedInput(
                    "^\\d+(\\.\\d{1,2})?$",
                    "Invalid amount! It must be a positive number with up to two decimal places."
            );

            System.out.print("Enter currency (e.g., USD, EUR): ");
            String currencyInput = ValidateInput.getValidatedInput(
                    "^[A-Z]{3}$",
                    "Invalid currency! It must be exactly 3 uppercase letters."
            );

            try {
                CurrencyType currency = CurrencyType.valueOf(currencyInput);
                Money money = new Money(new BigDecimal(amountInput), currency);

                accountServiceImpl.deposit(accountNumber, money);
                System.out.println("Money deposited successfully!");

            } catch (IllegalArgumentException e) {
                throw new InvalidCurrencyException(currencyInput);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void withdrawMoney(String accountNumber) {
        try {
            System.out.print("Enter amount to withdraw: ");
            String amountInput = ValidateInput.getValidatedInput("^\\d+(\\.\\d{1,2})?$", "Invalid amount! It must be a positive number with up to two decimal places.");
            accountServiceImpl.withdraw(accountNumber, Double.parseDouble(amountInput));
            System.out.println("Money withdrawn successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewTransactionHistory(String accountNumber) throws InvalidAccountException {
        try {
            List<TransactionViewModel> transactions = accountServiceImpl.getAccountTransactionsByViewModel(accountNumber);
            System.out.println("\n--- Transaction History ---");

            for (TransactionViewModel transaction : transactions) {
                System.out.println(transaction.toString());
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
