#ifndef ITMO_Labs_CPP_Lab4_SHAPESET_H
#define ITMO_Labs_CPP_Lab4_SHAPESET_H

#include "parallelogram.h"
#include "equilateraltriangle.h"
#include <set>

class Figures {
private:
    set<IFigure *> figs;
public:
    void addfigure();
    void display();
    double squareall();
    double perimeterall();
    CVector2D positionall();
    unsigned int memoryall();
};

#endif
