#include <stdio.h>
#include <windows.h>

int main() {
    SetConsoleOutputCP(CP_UTF8);
    // Задание 1
    int octalNumber;
    printf("Введите целое число в восьмеричной системе счисления: ");
    scanf("%o", &octalNumber);

    // Задание 2
    printf("Введённое число в десятичной системе счисления: %d\n", octalNumber);

    // Задание 3
    printf("Число в 16-ричной системе счисления: %x\n", octalNumber);
    printf("Число в 16-ричной системе сдвинутое вправо на 2 бита: %x\n", octalNumber >> 2);

    // Задание 4
    printf("Число в 16-ричной системе счисления: %x\n", octalNumber);
    printf("Число в 16-ричной системе после применения к нему битовой операции отрицания: %x\n", ~octalNumber);

    // Задание 5
    int hexNumber;
    printf("Введите целое число в 16-ричной системе счисления: ");
    scanf("%x", &hexNumber);

    // Результат битовой операции "или"
    int result = octalNumber | hexNumber;
    printf("Результат битовой операции \"или\": %x\n", result);

    return 0;
}