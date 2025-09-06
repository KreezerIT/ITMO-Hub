package infrastructure.presentation.consoleui;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ValidateInput {
    public static String getValidatedInput(String regex, String errorMessage) {
        Scanner scanner = new Scanner(System.in);
        Pattern pattern = Pattern.compile(regex);

        while (true) {
            String input = scanner.nextLine();
            if (pattern.matcher(input).matches()) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }
}