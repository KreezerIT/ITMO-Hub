#include "Parallelogram.h"

Parallelogram::Parallelogram() {
    start = CVector2D{0, 0};
    a = CVector2D{0, 0};
    b = CVector2D{0, 0};
}
Parallelogram::Parallelogram( CVector2D& s,  CVector2D& aVec,  CVector2D& bVec) : start(s), a(aVec), b(bVec) {}

double Parallelogram::square()  {
    return a.x * b.y - a.y * b.x;
}

double Parallelogram::perimeter()  {
    return 2 * (a.x + a.y);
}

double Parallelogram::mass()  {
    return square();
}

CVector2D Parallelogram::position()  {
    CVector2D *vec = new CVector2D;
    vec->x = (a.x + b.x) / 2 + start.x;
    vec->y = (a.y + b.y) / 2 + start.y;

    return *vec;
}

bool Parallelogram::operator==( IPhysObject& other)  {
    double first = const_cast<Parallelogram &>(*this).mass();
    double second = const_cast<Parallelogram &>(dynamic_cast<const Parallelogram &>(other)).mass();

    if (first == second) {
        return true;
    } else {
        return false;
    }
}

bool Parallelogram::operator<( IPhysObject& other)  {
    double first = const_cast<Parallelogram &>(*this).mass();
    double second = const_cast<Parallelogram &>(dynamic_cast<const Parallelogram &>(other)).mass();

    if (first < second) {
        return true;
    } else {
        return false;
    }
}

void Parallelogram::draw()  {
    cout << "Parallelogram: " << classname() << "\n";
    cout << "Start = (" << start.x << ", " << start.y << "), A = (" << a.x << ", " << a.y << "), B = (" << b.x << ", " << b.y << ")" << "\n";
}

void Parallelogram::initFromDialog() {
    cout << "Enter the start point coordinates (x y): ";
    cin >> start.x >> start.y;
    cout << "Enter the coordinates of vector A (x y): ";
    cin >> a.x >> a.y;
    cout << "Enter the coordinates of vector B (x y): ";
    cin >> b.x >> b.y;
}

const char* Parallelogram::classname()  {
    return typeid(*this).name();
}

unsigned int Parallelogram::size()  {
    return sizeof(*this);
}