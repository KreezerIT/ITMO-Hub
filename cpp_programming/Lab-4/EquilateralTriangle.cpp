#include "EquilateralTriangle.h"

EquilateralTriangle::EquilateralTriangle() {
    start = CVector2D{0, 0};
    a = CVector2D{0, 0};
    b = CVector2D{0, 0};
}
EquilateralTriangle::EquilateralTriangle( CVector2D& s,  CVector2D& aVec,  CVector2D& bVec) : start(s), a(aVec), b(bVec) {}

double EquilateralTriangle::square()  {
    double sideLength = sqrt(pow(a.x - start.x, 2) + pow(a.y - start.y, 2));
    return (sqrt(3) / 4) * sideLength * sideLength;
}

double EquilateralTriangle::perimeter()  {
    double sideLength = sqrt(pow(a.x - start.x, 2) + pow(a.y - start.y, 2));
    return 3 * sideLength;
}

double EquilateralTriangle::mass()  {
    return square();
}

CVector2D EquilateralTriangle::position()  {
    double centerX = (start.x + a.x + b.x) / 3.0;
    double centerY = (start.y + a.y + b.y) / 3.0;
    return {centerX, centerY};
}


bool EquilateralTriangle::operator==( IPhysObject& ob)  {
     EquilateralTriangle* t = dynamic_cast< EquilateralTriangle*>(&ob);
    return this->mass() == t->mass();
}

bool EquilateralTriangle::operator<( IPhysObject& ob)  {
     EquilateralTriangle* t = dynamic_cast< EquilateralTriangle*>(&ob);
    return this->mass() < t->mass();
}

void EquilateralTriangle::draw()  {
    cout << "EquilateralTriangle: " << classname() << "\n";
    cout << "Start = (" << start.x << ", " << start.y << "), A = (" << a.x << ", " << a.y << "), B = (" << b.x << ", " << b.y << ")" << "\n";
}

void EquilateralTriangle::initFromDialog() {
    cout << "Enter start point coordinates (x y): ";
    cin >> start.x >> start.y;
    cout << "Enter coordinates of vector A (x y): ";
    cin >> a.x >> a.y;
    cout << "Enter coordinates of vector B (x y): ";
    cin >> b.x >> b.y;
}

const char* EquilateralTriangle::classname()  {
    return typeid(*this).name();
}

unsigned int EquilateralTriangle::size()  {
    return sizeof(*this);
}