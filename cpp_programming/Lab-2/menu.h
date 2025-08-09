#ifndef ITMO_Labs_CPP_Lab2_MENU_H
#define ITMO_Labs_CPP_Lab2_MENU_H

#include "header.h"

class Menu {
public:
    Menu(QuadraticPolynomial& poly);
    void displayOptions();
    void handleInput();
private:
    QuadraticPolynomial poly;
};

#endif
