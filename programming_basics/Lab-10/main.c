#include <stdio.h>
#include <math.h>
#include <windows.h>

float calculateDistance(int x1, int y1, int x2, int y2, float *aIK) {
    *aIK = sqrt(pow(x2 - x1, 2) + pow(y2 - y1, 2));
}

void createDistanceArray(int n, int coordinates[][2], float distanceArray[][n-1]) {
    for (int i = 0; i < n; i++) {
        for (int j = 0, k = 0; j < n; j++) {
            if (j != i) {
                calculateDistance(coordinates[i][0], coordinates[i][1],
                                  coordinates[j][0], coordinates[j][1],
                                  &distanceArray[i][k]);
                k++;
            }
        }
    }
}

void getDigitArray(int n, int digitArray[]) {
    int index = 0;
    while (n > 0) {
        digitArray[index] = n % 10;
        n /= 10;
        index++;
    }
}

int main() {
    SetConsoleOutputCP(CP_UTF8);

    // 2.
    int n;
    printf("Введите количество точек: ");
    scanf("%d", &n);

    int coordinates[n][2];

    printf("Введите координаты точек:\n");
    for (int i = 0; i < n; i++) {
        printf("Точка %d: ", i + 1);
        scanf("%d %d", &coordinates[i][0], &coordinates[i][1]);
    }

    float distanceArray[n][n-1];
    createDistanceArray(n, coordinates, distanceArray);

    printf("Массив расстояний:\n");
    for (int i = 0; i < n; i++) {
        printf("Расстояния от точки %d:\n", i + 1);
        for (int j = 0; j < n - 1; ++j) {
            printf("До точки %d: %.2f\n", (j < i) ? j + 1 : j + 2, distanceArray[i][j]);
        }
        printf("\n");
    }

    // 3.
    int number;
    printf("Введите натуральное число: ");
    scanf("%d", &number);

    int temp = number;
    int digitCount = 0;
    while (temp > 0) {
        temp /= 10;
        digitCount++;
    }

    int digitArray[digitCount];
    getDigitArray(number, digitArray);

    printf("Массив цифр числа:\n");
    for (int i = digitCount - 1; i >= 0; i--) {
        printf("%d ", digitArray[i]);
    }

    return 0;
}