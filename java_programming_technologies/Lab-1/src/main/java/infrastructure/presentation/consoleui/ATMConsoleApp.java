package infrastructure.presentation.consoleui;

import application.services.AccountServiceImpl;
import application.services.TransactionServiceImpl;
import core.exceptions.*;
import core.valueobjects.UserMods;
import infrastructure.persistance.AccountRepositoryImpl;
import infrastructure.persistance.TransactionRepositoryImpl;
import infrastructure.presentation.consoleui.menus.AdminMenu;
import infrastructure.presentation.consoleui.menus.UserMenu;
import infrastructure.services.LoginServiceImpl;

import java.sql.SQLException;
import java.util.Scanner;

public class ATMConsoleApp {
    public static void main(String[] args) throws SQLException, AccountNotFoundException, InvalidPinCodeException, DataMappingException, InvalidAccountException, InvalidInputException, DatabaseOperationException {
        try {
            AccountRepositoryImpl accountRepositoryImpl = new AccountRepositoryImpl();
            TransactionRepositoryImpl transactionRepositoryImpl = new TransactionRepositoryImpl();
            AccountServiceImpl accountServiceImpl = new AccountServiceImpl(accountRepositoryImpl, new TransactionServiceImpl(accountRepositoryImpl, transactionRepositoryImpl));
            ATMConsoleApp app = new ATMConsoleApp(new AdminMenu(accountServiceImpl), new UserMenu(accountServiceImpl), new LoginServiceImpl(accountServiceImpl));
            app.run();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private final AdminMenu adminMenu;
    private final UserMenu userMenu;
    private final LoginServiceImpl loginServiceImpl;

    public ATMConsoleApp(AdminMenu adminMenu, UserMenu userMenu, LoginServiceImpl loginServiceImpl) {
        this.adminMenu = adminMenu;
        this.userMenu = userMenu;
        this.loginServiceImpl = loginServiceImpl;
    }

    public void run() throws SQLException, AccountNotFoundException, InvalidAccountException, InvalidPinCodeException, DataMappingException, DatabaseOperationException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the ITMO-ATM!");
        System.out.println("Please choose a mode:");
        System.out.println("1. " + UserMods.CLIENT);
        System.out.println("2. " + UserMods.ADMIN);
        System.out.print("Your choice: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> userMenu.show(loginServiceImpl);
            case "2" -> adminMenu.show(loginServiceImpl);
            default -> System.out.println("Invalid choice. Try again.");
        }
    }
}