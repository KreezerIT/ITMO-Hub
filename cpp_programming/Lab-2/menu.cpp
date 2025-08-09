#include "menu.h"

Menu::Menu(QuadraticPolynomial& poly) : poly(poly) {}

void Menu::displayOptions() {
    cout << "================================================\n";
    cout << "1. Calculate value at a point\n";
    cout << "2. Count roots\n";
    cout << "3. Find roots\n";
    cout << "4. Find minimum\n";
    cout << "5. Find maximum\n";
    cout << "6. Print polynomial\n";
    cout << "7. Exit\n";
    cout << "Choose an option: ";
}

void Menu::handleInput() {
    int choice;
    double value;
    while (true) {
        displayOptions();
        cin >> choice;
        switch (choice) {
            case 1:
                cout << "Enter a value for x: ";
                cin >> value;
                cout << "Value at x: " << poly.calculateValue(value) << "\n";
                break;
            case 2:
                cout << "Number of roots: " << poly.countRoots() << "\n";
                break;
            case 3:
                poly.findRoots();
                break;
            case 4:
                cout << "Minimum value: " << poly.findMinimum() << "\n";
                break;
            case 5:
                cout << "Maximum value: " << poly.findMaximum() << "\n";
                break;
            case 6:
                poly.print();
                break;
            case 7:
                return;
            default:
                cout << "Enter a number between 1 and 7" << "\n";
                break;
        }
    }
}
