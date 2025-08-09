#include <stdio.h>
#include <windows.h>

enum Months {
    Январь, Февраль, Март, Апрель, Май, Июнь, Июль, Август, Сентябрь, Октябрь, Ноябрь, Декабрь
};

struct Rectangle {
    int length;
    int width;
};

int calculateArea(struct Rectangle rect) {
    return rect.length * rect.width;
}

union KeyboardState {
    unsigned int hexValue;
    struct {
        unsigned int numLock : 1;
        unsigned int capsLock : 1;
        unsigned int scrollLock : 1;
    } state;
};

int main() {
    SetConsoleOutputCP(CP_UTF8);

    //1
    enum Months month = Июль;
    printf("Значение для месяца июля: %d\n", month);

    //2
    struct Rectangle myRectangle;
    myRectangle.length = 5;
    myRectangle.width = 3;

    int area = calculateArea(myRectangle);
    printf("Площадь прямоугольника: %d\n", area);

    //3
    union KeyboardState keyboard;
    printf("Введите число в 16 СС: ");
    scanf("%x", &keyboard.hexValue);

    printf("Состояния элементов клавиатуры:\n");
    printf("NumLock: %s\n", keyboard.state.numLock ? "вкл" : "выкл");
    printf("CapsLock: %s\n", keyboard.state.capsLock ? "вкл" : "выкл");
    printf("ScrollLock: %s\n", keyboard.state.scrollLock ? "вкл" : "выкл");

    return 0;
}