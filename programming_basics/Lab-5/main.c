#include <stdio.h>
#include <windows.h>

void printMatrix(int matrix[2][2]) {
    for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
            printf("%d ", matrix[i][j]);
        }
        printf("\n");
    }
}

int main() {
    SetConsoleOutputCP(CP_UTF8);

    int array[7] = {23, 5678, 2, 564, 365, 77,443};

    printf("Массив:\n");
    for (int i = 0; i < 7; i++) {
        printf("%d\n", array[i]);
    }

    int matrixA[2][2] = {{1, 1}, {2, 0}};
    int matrixB[2][2] = {{0, 1}, {3, 0}};
    int resultMatrix[2][2] = {{0, 0}, {0, 0}};

    for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 2; k++) {
                resultMatrix[i][j] += matrixA[i][k] * matrixB[k][j];
            }
        }
    }

    printf("\nРезультирующая матрица:\n");
    printMatrix(resultMatrix);

    return 0;
}