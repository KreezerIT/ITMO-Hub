#include <stdio.h>
#include <windows.h>

int main() {
    SetConsoleOutputCP(CP_UTF8);

    // 2.
    int totalMinutes;
    float subscriptionFee, limitFreeMinutes=499, extraMinuteCost;

    printf("Введите общую продолжительность телефонных переговоров в минутах: ");
    scanf("%d", &totalMinutes);

    printf("Введите абонентскую плату: ");
    scanf("%f", &subscriptionFee);
    float freeMinuteCost = subscriptionFee / limitFreeMinutes;

    printf("Введите стоимость минуты сверх лимита: ");
    scanf("%f", &extraMinuteCost);
    if (freeMinuteCost<extraMinuteCost) {


        // Расчет стоимости месячного обслуживания
        float totalCost;
        if (totalMinutes <= limitFreeMinutes) {
            totalCost = subscriptionFee;
        } else {
            totalCost = subscriptionFee + (totalMinutes - limitFreeMinutes) * extraMinuteCost;
        }

        printf("Стоимость месячного обслуживания телефона: %.2f руб.\n", totalCost);
    } else {
        printf("Ошибка, стоимость бесплатной минуты выше платной\n");
    }

    // 3.
    char *numbers[] = {"Ноль", "Один", "Два", "Три", "Четыре", "Пять", "Шесть", "Семь", "Восемь", "Девять"};
    int digit;

    printf("Введите цифру (от 0 до 9): ");
    scanf("%d", &digit);

    if (digit >= 0 && digit <= 9) {
        printf("Числительное: %s\n", numbers[digit]);
    } else {
        printf("Ошибка\n");
        return 1;
    }

    return 0;
}