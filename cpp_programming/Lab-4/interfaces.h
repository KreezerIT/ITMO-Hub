#ifndef ITMO_Labs_CPP_Lab4_INTERFACES_H
#define ITMO_Labs_CPP_Lab4_INTERFACES_H

#include <iostream>
#include <cmath>
#include "windows.h"
using namespace std;

class IGeoFig {
public:
    virtual double square()  = 0;
    virtual double perimeter()  = 0;
};

class CVector2D {
public:
    double x, y;
};

class IPhysObject {
public:
    virtual double mass()  = 0;
    virtual CVector2D position()  = 0;
    virtual bool operator==( IPhysObject& ob)  = 0;
    virtual bool operator<( IPhysObject& ob)  = 0;
};

class IPrintable {
public:
    virtual void draw()  = 0;
};

class IDialogInitiable {
public:
    virtual void initFromDialog() = 0;
};

class BaseCObject {
public:
    virtual const char * classname()  = 0;
    virtual unsigned int size()  = 0;
};

class IFigure : public IGeoFig, public IPhysObject, public IPrintable, public IDialogInitiable, public BaseCObject {};

#endif
