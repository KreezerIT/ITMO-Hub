#include <stdio.h>
#include <windows.h>

int main() {
    SetConsoleOutputCP(CP_UTF8);

    int userInputFirst;
    int userInputSecond;

    // Ввод числа с консоли 1
    printf("Введите целое число: ");
    scanf("%d", &userInputFirst);

    // Проверка на попадание в заданный диапазон
    printf("Число %d %s в диапазон\n", userInputFirst,
           (userInputFirst >= -1000 && userInputFirst <= 100) ? "попадает" : "не попадает");

    // Ввод числа с консоли 2
    printf("Введите число в 16 СС: ");
    scanf("%x", &userInputSecond);

    // Проверка значения бита 22
    int bitValue = (userInputSecond >> 21) & 1;
    printf("%d\n", bitValue);

    return 0;
}