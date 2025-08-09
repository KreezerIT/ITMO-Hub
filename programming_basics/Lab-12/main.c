#include <stdio.h>
#include <time.h>
#include <windows.h>

void actualDate(char* date) {
    time_t t = time(NULL);
    struct tm* current_time = localtime(&t);

    sprintf(date, "%02d.%02d.%04d", current_time->tm_mday, current_time->tm_mon + 1, current_time->tm_year + 1900);
}

void upDate(char* date) {
    int day, month, year;
    sscanf(date, "%d.%d.%d", &day, &month, &year);
    day++;
    if (day > 31) {day = 1;month++;
        if (month > 12) {month = 1;year++;}
    }
    sprintf(date, "%02d.%02d.%04d", day, month, year);
}

int main() {
    SetConsoleOutputCP(CP_UTF8);
    FILE *file;
    file = fopen("dates.txt", "w");

    if (file == NULL) {
        printf("Ошибка\n");
        return 1;
    }
    char date[50];
    actualDate(date);

    for (int i = 0; i < 10; i++) {
        fprintf(file, "%s\n", date);
        upDate(date);
    }

    printf("Даты записаны\n");
    fclose(file);
    return 0;
}