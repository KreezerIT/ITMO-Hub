#ifndef ITMO_Labs_CPP_Lab1_HEADER_H
#define ITMO_Labs_CPP_Lab1_HEADER_H

#include <iostream>
#include <cmath>
#include <windows.h>
using namespace std;

struct Complex{
    double real;
    double image;
};

void changeP(int*, int*);
void roundingP(float* x);
void multiplicationP(Complex*, float);
void transpositionP(int (*)[3]);

void changeR(int&, int&);
void roundingR(float&);
void multiplicationR(Complex&, float);
void transpositionR(int (&)[3][3]);

#endif
