#include <stdio.h>

int main() {
    char str[100];
    int number;

    // Ввод строки
    printf("Введите строку: ");
    fgets(str, 100, stdin);

    // Ввод целого числа
    printf("Введите целое число: ");
    scanf("%d", &number);

    // Вывод введённых данных
    printf("Введенная строка: %s", str);
    printf("Введенное число: %d\n", number);

    return 0;
}
