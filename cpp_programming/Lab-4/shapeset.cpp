#include "shapeset.h"


void Figures::addfigure() {
    int choice;
    cout << "1 - Parallelogram, 2 - EquilateralTriangle" << "\n";
    cin >> choice;
    while (choice != 1 and choice != 2) {
        cout << "Choose 1 or 2" << "\n";
        cin >> choice;
    }

    if (choice == 1) {
        Parallelogram *temp = new Parallelogram;
        temp->initFromDialog();
        IFigure* tempIFigure = temp;
        figs.insert(tempIFigure);
    } else {
        EquilateralTriangle *temp = new EquilateralTriangle;
        temp ->initFromDialog();
        IFigure* tempIFigure = temp;
        figs.insert(tempIFigure);
    }

}

void Figures::display() {
    for (auto i:figs) {
        i->draw();
    }
}

double Figures::squareall() {
    double counter = 0;
    for (auto i:figs) {
        counter += i->square();
    }


    return counter;
}

double Figures::perimeterall() {
    double counter = 0;
    for (auto i:figs) {
        counter += i->perimeter();
    }

    return counter;
}

CVector2D Figures::positionall() {
    CVector2D ans{0, 0};
    double curx = 0;
    double cury = 0;
    double m = 0;

    for (auto i:figs) {
        curx += i->position().x * i->mass();
        cury += i->position().y * i->mass();
        m += i->mass();
    }

    ans.x = curx/m;
    ans.y = cury/m;

    return ans;
}

unsigned int Figures::memoryall() {
    unsigned int ans = 0;

    for (auto i:figs) {
        ans += i->size();
    }

    return ans;
}