#include "triangle.h"
#include <math.h>

double calculateDistance(struct Point p1, struct Point p2) {
    double dx = p2.x - p1.x;
    double dy = p2.y - p1.y;
    return sqrt(dx * dx + dy * dy);
}

double calculateTrianglePerimeter(struct Triangle triangle) {
    double side1 = calculateDistance(triangle.point1, triangle.point2);
    double side2 = calculateDistance(triangle.point2, triangle.point3);
    double side3 = calculateDistance(triangle.point3, triangle.point1);
    return side1 + side2 + side3;
}

double calculateTriangleArea(struct Triangle triangle) {
    double side1 = calculateDistance(triangle.point1, triangle.point2);
    double side2 = calculateDistance(triangle.point2, triangle.point3);
    double side3 = calculateDistance(triangle.point3, triangle.point1);
    double p = (side1 + side2 + side3) / 2;
    return sqrt(p * (p - side1) * (p - side2) * (p - side3));
}