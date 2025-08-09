#ifndef ITMO_Labs_CPP_Lab2_HEADER_H
#define ITMO_Labs_CPP_Lab2_HEADER_H

#include <iostream>
#include <cmath>
using namespace std;

class QuadraticPolynomial {
private:
    double a, b, c;

public:
    QuadraticPolynomial();
    QuadraticPolynomial(double a, double b, double c);

    double calculateValue(double x);
    int countRoots();
    void findRoots();
    double findMinimum();
    double findMaximum();
    void print();
};

#endif