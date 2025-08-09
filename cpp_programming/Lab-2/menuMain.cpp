#include "menu.h"

int main() {
    double a, b, c;
    cout << "Enter coefficients a, b, c: ";
    cin >> a >> b >> c;

    QuadraticPolynomial poly(a, b, c);
    Menu menu(poly);
    menu.handleInput();

    return 0;
}
