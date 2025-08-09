/*
#include "parallelogram.h"
#include "equilateraltriangle.h"

int main() {
    SetConsoleOutputCP(CP_UTF8);
    CVector2D startPoint = {0.0, 0.0};
    CVector2D sideA = {5.0, 0.0};
    CVector2D sideB = {2.0, 3.0};

    Parallelogram parallelogram(startPoint, sideA, sideB);

    cout << "Parallelogram square: " << parallelogram.square() << "\n";
    cout << "Parallelogram perimeter: " << parallelogram.perimeter() << "\n";
    cout << "Parallelogram mass: " << parallelogram.mass() << "\n";
    cout << "Parallelogram position: (" << parallelogram.position().x << ", " << parallelogram.position().y << ")" << "\n";
    parallelogram.draw();
    cout << "\n";

    CVector2D triangleStart = {0.0, 0.0};
    CVector2D triangleA = {3.0, 0.0};
    CVector2D triangleB = {1.5, 2.6}; 

    EquilateralTriangle triangle(triangleStart, triangleA, triangleB);

    cout << "Equilateral triangle square: " << triangle.square() << "\n";
    cout << "Equilateral triangle perimeter: " << triangle.perimeter() << "\n";
    cout << "Equilateral triangle mass: " << triangle.mass() << "\n";
    cout << "Equilateral triangle position: (" << triangle.position().x << ", " << triangle.position().y << ")" << "\n";
    triangle.draw();

    return 0;
}
*/
