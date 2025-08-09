#include "data_types.h"

int main() {
    int data1[3][3] = {{1, 2, 3},
                       {4, 5, 6},
                       {7, 8, 9}};

    int data2[3][3] = {{9, 8, 7},
                       {6, 5, 4},
                       {3, 2, 1}};

    Matrix3x3 matrix1(data1);
    Matrix3x3 matrix2(data2);

    int type;
    cout << "==================================================\n";
    cout << "Choose type:\n";
    cout << "1. Matrix3x3\n";
    cout << "2. FIFO\n";
    cin >> type;

    if (type==1) {
        cout << "==================================================\n";
        cout << "Choose operation:\n";
        cout << "0. print matrix\n";
        cout << "1. matrix multiplication (explicit)\n";
        cout << "11. matrix multiplication (implicit)\n";
        cout << "2. matrix multiplication by scalar (explicit)\n";
        cout << "22. matrix multiplication by scalar (implicit)\n";
        cout << "3. matrix addition (explicit)\n";
        cout << "33. matrix addition (implicit)\n";
        cout << "4. matrix subtraction (explicit)\n";
        cout << "44. matrix subtraction (implicit)\n";
        cout << "5. matrix comparison\n";

        int choise;
        while (cin >> choise) {

            if (choise == 0) {
                cout << "Matrix1:\n";
                matrix1.print();
                cout << "Matrix2:\n";
                matrix2.print();
            } else if (choise == 1) {

                // Проверка явного вызова оператора умножения матриц
                cout << "Result of explicit matrix multiplication:\n";
                Matrix3x3 result_explicit_mult = matrix1.operator*(matrix2);
                result_explicit_mult.print();
            } else if (choise == 11) {
                // Проверка неявного вызова оператора умножения матриц
                cout << "Result of implicit matrix multiplication:\n";
                Matrix3x3 result_implicit_mult = matrix1 * matrix2;
                result_implicit_mult.print();
            } else if (choise == 2) {
                // Проверка явного вызова оператора умножения матрицы на вещественное число
                cout << "Result of explicit matrix multiplication by scalar:\n";
                Matrix3x3 result_explicit_scalar_mult = matrix1.operator*(2);
                result_explicit_scalar_mult.print();
            } else if (choise == 22) {
                // Проверка неявного вызова оператора умножения матрицы на вещественное число
                cout << "Result of implicit matrix multiplication by scalar:\n";
                Matrix3x3 result_implicit_scalar_mult = matrix1 * 2;
                result_implicit_scalar_mult.print();
            } else if (choise == 3) {
                // Проверка явного вызова оператора сложения матриц
                cout << "Result of explicit matrix addition:\n";
                Matrix3x3 result_explicit_add = matrix1.operator+(matrix2);
                result_explicit_add.print();
            } else if (choise == 33) {
                // Проверка неявного вызова оператора сложения матриц
                cout << "Result of implicit matrix addition:\n";
                Matrix3x3 result_implicit_add = matrix1 + matrix2;
                result_implicit_add.print();
            } else if (choise == 4) {
                // Проверка явного вызова оператора вычитания матриц
                cout << "Result of explicit matrix subtraction:\n";
                Matrix3x3 result_explicit_subtract = matrix1.operator-(matrix2);
                result_explicit_subtract.print();
            } else if (choise == 44) {
                // Проверка неявного вызова оператора вычитания матриц
                cout << "Result of implicit matrix subtraction:\n";
                Matrix3x3 result_implicit_subtract = matrix1 - matrix2;
                result_implicit_subtract.print();
            } else if (choise == 5) {
                cout << "Matrix1 == Matrix2: " << (matrix1 == matrix2) << "\n";
                cout << "Matrix1 != Matrix2: " << (matrix1 != matrix2) << "\n";
                cout << "Matrix1 <  Matrix2: " << (matrix1 < matrix2) << "\n";
                cout << "Matrix1 >  Matrix2: " << (matrix1 > matrix2) << "\n";
            } else {
                cout << "Invalid choice\n";
            }
        }
    }
    else if (type == 2) {
        FIFO fifo;

        cout << "==================================================\n";
        cout << "Choose operation:\n";
        cout << "0. Print the FIFO\n";
        cout << "1. Add element to FIFO\n";
        cout << "2. Retrieve element from FIFO\n";

        int choice;
        while (cin >> choice) {

            if (choice == 0) {
                // Вывод содержимого очереди
                if (fifo.isEmpty()) {
                    cout << "FIFO is empty\n";
                } else {
                    cout << "Contents of FIFO:\n";
                    int tempHead = fifo.getHead();
                    int tempTail = fifo.getTail();

                    while (tempHead != tempTail) {
                        cout << fifo[tempHead] << " ";
                        tempHead = (tempHead + 1) % 100;
                    }
                    cout << "\n";
                }
            } else if (choice == 1) {
                // Добавление элементов в очередь
                int num;
                cout << "Enter element to add to FIFO: ";
                cin >> num;
                fifo << num;
                cout << "Element added to FIFO\n";
            } else if (choice == 2) {
                // Извлечение элементов из очереди
                int num;
                if (fifo.isEmpty()) {
                    cout << "FIFO is empty\n";
                } else {
                    fifo >> num;
                    cout << "Retrieved element from FIFO: " << num << "\n";
                }
            } else {
                cout << "Invalid choice\n";
            }
        }
    }


    return 0;
}
