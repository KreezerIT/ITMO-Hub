#include "circularbuffer.h"
#include <iostream>

void menu() {
    Ring r;
    int choice;
    while (true) {
        std::cout << "1 - add element next; 2 - add element prev; 3 - remove current element; 4 - go next; 5 - go prev; 6 - pull front; 7 - pull tail; 8 - pull by index; 9 - insert by index; 10 - exit\n";
        do {
            std::cin >> choice;
        } while (choice < 1 || choice > 10);

        int tmp;
        switch (choice) {
            case 1:
                std::cout << "Input data for new element: ";
                std::cin >> tmp;
                r.pushNext(tmp);
                break;

            case 2:
                std::cout << "Input data for new element: ";
                std::cin >> tmp;
                r.pushPrev(tmp);
                break;

            case 3:
                tmp = r.pull_front();
                std::cout << "Removed element with data: " << tmp << "\n";
                break;

            case 4:
                r.goNext();
                std::cout << "Rotated ring to next\n";
                break;

            case 5:
                r.goPrev();
                std::cout << "Rotated ring to prev\n";
                break;

            case 6:
                tmp = r.pull_front();
                std::cout << "Removed front element with data: " << tmp << "\n";
                break;

            case 7:
                tmp = r.pull_tail();
                std::cout << "Removed tail element with data: " << tmp << "\n";
                break;

            case 8:
                std::cout << "Input index to remove element: ";
                std::cin >> tmp;
                r.pull_by_index(tmp);
                break;

            case 9:
                int index;
                std::cout << "Input index to insert element: ";
                std::cin >> index;
                std::cout << "Input data for new element: ";
                std::cin >> tmp;
                r.insert_by_index(index, tmp);
                break;

            case 10:
                return;
        }
        r.print();
        std::cout << "\n";
    }
}

int main() {
    menu();
    Ring rr;
    rr.adjustCapacity(10);
    return 0;
}
