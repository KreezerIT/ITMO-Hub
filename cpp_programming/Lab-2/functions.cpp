#include "header.h"

QuadraticPolynomial::QuadraticPolynomial() : a(0), b(0), c(0) {}
QuadraticPolynomial::QuadraticPolynomial(double a, double b, double c) : a(a), b(b), c(c) {}

double QuadraticPolynomial::calculateValue(double x) {
    return a * x * x + b * x + c;
}

int QuadraticPolynomial::countRoots() {
    double discriminant = b * b - 4 * a * c;
    if (discriminant > 0)
        return 2;
    else if (discriminant == 0)
        return 1;
    else
        return 0;
}

void QuadraticPolynomial::findRoots() {
    double discriminant = b * b - 4 * a * c;
    if (discriminant > 0) {
        double root1 = (-b + sqrt(discriminant)) / (2 * a);
        double root2 = (-b - sqrt(discriminant)) / (2 * a);
        cout << "Roots: " << root1 << ", " << root2 << "\n";
    } else if (discriminant == 0) {
        double root = -b / (2 * a);
        cout << "Root: " << root << "\n";
    } else {
        cout << "No real roots" << "\n";
    }
}

double QuadraticPolynomial::findMinimum() {
    double vertex_x = -b / (2 * a);
    double min_value = calculateValue(vertex_x);
    return min_value;
}

double QuadraticPolynomial::findMaximum() {
    double vertex_x = -b / (2 * a);
    double max_value = calculateValue(vertex_x);
    return max_value;
}

void QuadraticPolynomial::print() {
    cout << "Quadratic Polynomial: " << a << "x^2 + " << b << "x + " << c << "\n";
}
