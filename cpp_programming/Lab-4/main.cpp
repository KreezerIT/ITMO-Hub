#include "parallelogram.h"
#include "equilateraltriangle.h"
#include "shapeset.h"

int main() {

    Figures figs1;
    cout << "================================================" << "\n";
    cout << "1. add figure" << "\n";
    cout << "2. diplay figures" << "\n";
    cout << "3. total area of figures" << "\n";
    cout << "4. total perimeter of figures" << "\n";
    cout << "5. centre of mass of all figures" << "\n";
    cout << "6. memory for all instances of classes" << "\n";
    cout << "end - stop programm" << "\n";

    while (true) {
        string str;
        cin >> str;
        if (str == "end") {
            break;
        } else if (str == "1") {
            figs1.addfigure();
            cout << "done" << "\n";
        } else if (str == "2") {
            figs1.display();
        } else if (str == "3") {
            cout << "total area of figures = " << figs1.squareall() << "\n";
        } else if (str == "4") {
            cout << "total perimeter of figures = " << figs1.perimeterall() << "\n";
        } else if (str == "5") {
            cout << "centre of mass of all figures = " << figs1.positionall().x << " " << figs1.positionall().y << "\n";
        } else if (str == "6") {
            cout << "in total figures occupy " << figs1.memoryall() << " bytes of memory" << "\n";
        }
    }


    cout << "ended" << "\n";

    return 0;
}
