#include "data_types.h"


Matrix3x3::Matrix3x3() {
    for (int i = 0; i < 3; ++i) {
        for (int j = 0; j < 3; ++j) {
            data[i][j] = 0;
        }
    }
}

Matrix3x3::Matrix3x3(int initData[3][3]) {
    for (int i = 0; i < 3; ++i) {
        for (int j = 0; j < 3; ++j) {
            data[i][j] = initData[i][j];
        }
    }
}

Matrix3x3 Matrix3x3::operator*(const Matrix3x3& other) const {
    Matrix3x3 result;
    for (int i = 0; i < 3; ++i) {
        for (int j = 0; j < 3; ++j) {
            result.data[i][j] = 0;
            for (int k = 0; k < 3; ++k) {
                result.data[i][j] += data[i][k] * other.data[k][j];
            }
        }
    }
    return result;
}

Matrix3x3 Matrix3x3::operator*(const int scalar) const {
    Matrix3x3 result;
    for (int i = 0; i < 3; ++i) {
        for (int j = 0; j < 3; ++j) {
            result.data[i][j] = data[i][j] * scalar;
        }
    }
    return result;
}

Matrix3x3 Matrix3x3::operator+(const Matrix3x3& other) const {
    Matrix3x3 result;
    for (int i = 0; i < 3; ++i) {
        for (int j = 0; j < 3; ++j) {
            result.data[i][j] = data[i][j] + other.data[i][j];
        }
    }
    return result;
}

Matrix3x3 Matrix3x3::operator-(const Matrix3x3& other) const {
    Matrix3x3 result;
    for (int i = 0; i < 3; ++i) {
        for (int j = 0; j < 3; ++j) {
            result.data[i][j] = data[i][j] - other.data[i][j];
        }
    }
    return result;
}

bool Matrix3x3::operator==(const Matrix3x3& other) const {
    for (int i = 0; i < 3; ++i) {
        for (int j = 0; j < 3; ++j) {
            if (data[i][j] != other.data[i][j]) {
                return false;
            }
        }
    }
    return true;
}

bool Matrix3x3::operator!=(const Matrix3x3& other) const {
    return !(*this == other);
}

bool Matrix3x3::operator>(const Matrix3x3& other) const {
    int sum1 = 0, sum2 = 0;
    for (int i = 0; i < 3; ++i) {
        for (int j = 0; j < 3; ++j) {
            sum1 += data[i][j];
            sum2 += other.data[i][j];
        }
    }
    return sum1 > sum2;
}

bool Matrix3x3::operator<(const Matrix3x3& other) const {
    return !(*this > other);
}

void Matrix3x3::print() const {
    for (int i = 0; i < 3; ++i) {
        for (int j = 0; j < 3; ++j) {
            cout << data[i][j] << ' ';
        }
        cout << "\n";
    }
}


FIFO::FIFO() : front(0), rear(0) {
    for (int i = 0; i < 100; ++i) {
        data[i] = 0;
    }
}

FIFO& FIFO::operator<<(int num) {
    if ((rear + 1) % 100 == front) {
        cout << "Queue is full\n";
        return *this;
    }
    data[rear] = num;
    rear = (rear + 1) % 100;
    return *this;
}

FIFO& FIFO::operator>>(int& num) {
    if (front == rear) {
        cout << "Queue is empty\n";
        return *this;
    }
    num = data[front];
    front = (front + 1) % 100;
    return *this;
}
