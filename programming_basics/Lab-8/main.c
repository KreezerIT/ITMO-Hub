#include <stdio.h>
#include <string.h>
#include <windows.h>

int find_entry(char *str, char search_char, int last) {
    char *result = last ? strrchr(str, search_char) : strchr(str, search_char);
    return result != NULL ? result - str + 1 : -1;
}

int main() {
    SetConsoleOutputCP(CP_UTF8);

    char str1[50];
    char str2[50];

    printf("Введите первую строку: ");
    scanf("%s", str1);

    printf("Введите вторую строку: ");
    scanf("%s", str2);

    int n;
    printf("Введите число n: ");
    scanf("%d", &n);

    // 2. Конкатенация первой строки и n начальных символов второй строки
    char concatenated_str[100];
    strncpy(concatenated_str, str1, strlen(str1)); //копирование всех символов str1
    strncat(concatenated_str, str2, n);                //приписывание скопированного к str2
    printf("2. Конкатенация: %s\n", concatenated_str);

    // 5. Копирование одной строки в другую строку
    char copied_str[50];
    strcpy(copied_str, str1);
    printf("5. Копирование: %s\n", copied_str);

    // 8. Поиск первого вхождения указанного символа
    char search_char;
    printf("Введите символ для поиска в первой строке: ");
    scanf(" %c", &search_char);

    int first_entry_index = find_entry(str1, search_char,0);
    if (first_entry_index != -1) {
        printf("8. Первое вхождение символа '%c' в строке: %d\n", search_char, first_entry_index);
    } else {
        printf("8. Символ '%c' не найден в строке\n", search_char);
    }

    // 9. Поиск последнего вхождения указанного символа
    int last_entry_index = find_entry(str1, search_char,1);
    if (last_entry_index != -1) {
        printf("9. Последнее вхождение символа '%c' в строке: %d\n", search_char, last_entry_index);
    } else {
        printf("9. Символ '%c' не найден в строке\n", search_char);
    }

    // 12. Определение длины отрезка строки, не содержащего символы второй строки
    int str1_len = strcspn(str1, str2);
    printf("12. Длина отрезка строки без символов второй строки: %d\n", str1_len);

    return 0;
}