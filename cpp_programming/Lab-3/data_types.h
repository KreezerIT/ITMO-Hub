#ifndef ITMO_Labs_CPP_Lab3_DATA_TYPES_H
#define ITMO_Labs_CPP_Lab3_DATA_TYPES_H

#include <iostream>
using namespace std;

class Matrix3x3 {
private:
    int data[3][3];

public:
    Matrix3x3();
    Matrix3x3(int initData[3][3]);

    Matrix3x3 operator*(const Matrix3x3& other) const;
    Matrix3x3 operator*(const int scalar) const;
    Matrix3x3 operator+(const Matrix3x3& other) const;
    Matrix3x3 operator-(const Matrix3x3& other) const;
    bool operator==(const Matrix3x3& other) const;
    bool operator!=(const Matrix3x3& other) const;
    bool operator>(const Matrix3x3& other) const;
    bool operator<(const Matrix3x3& other) const;


    void print() const;
};

class FIFO {
private:
    int data[100];
    int front;
    int rear;

public:
    FIFO();


    FIFO& operator<<(int num);
    FIFO& operator>>(int& num);

    bool isEmpty() const {
        return front == rear;
    }

    int getHead() const {
        return front;
    }

    int getTail() const {
        return rear;
    }

    int& operator[](int index) {
        return data[index];
    }
};

#endif
