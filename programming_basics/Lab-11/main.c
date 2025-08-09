#include "triangle.h"
#include <stdio.h>
#include <windows.h>

int main() {
    SetConsoleOutputCP(CP_UTF8);
    struct Triangle triangle;

    printf("Координаты точки 1 (x y): ");
    scanf("%lf %lf", &triangle.point1.x, &triangle.point1.y);

    printf("Координаты точки 2 (x y): ");
    scanf("%lf %lf", &triangle.point2.x, &triangle.point2.y);

    printf("Координаты точки 3 (x y): ");
    scanf("%lf %lf", &triangle.point3.x, &triangle.point3.y);

    double perimeter = calculateTrianglePerimeter(triangle);
    double area = calculateTriangleArea(triangle);

    printf("Периметр треугольника: %.21f\n", perimeter);
    printf("Площадь треугольника: %.21f\n", area);

    return 0;
}