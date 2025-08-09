#ifndef ITMO_Labs_CPP_Lab4_EQUILATERALTRIANGLE_H
#define ITMO_Labs_CPP_Lab4_EQUILATERALTRIANGLE_H

#include "interfaces.h"


class EquilateralTriangle : public IFigure {
private:
    CVector2D start{};
    CVector2D a{};
    CVector2D b{};

public:
    EquilateralTriangle();
    EquilateralTriangle( CVector2D& s,  CVector2D& aVec,  CVector2D& bVec);

    double square()  override;
    double perimeter()  override;
    double mass()  override;
    CVector2D position()  override;
    bool operator==( IPhysObject& ob)  override;
    bool operator<( IPhysObject& ob)  override;
    void draw()  override;
    void initFromDialog() override;
    const char* classname()  override;
    unsigned int size()  override;
};


#endif