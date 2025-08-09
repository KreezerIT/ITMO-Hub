#ifndef ITMO_Labs_C_TRIANGLE_H
#define ITMO_Labs_C_TRIANGLE_H

struct Point {
    double x;
    double y;
};

struct Triangle {
    struct Point point1;
    struct Point point2;
    struct Point point3;
};

double calculateDistance(struct Point p1, struct Point p2);
double calculateTrianglePerimeter(struct Triangle triangle);
double calculateTriangleArea(struct Triangle triangle);

#endif