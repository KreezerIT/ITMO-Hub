#include "header.h"

int main(){
    SetConsoleOutputCP(CP_UTF8);
    // 1 //////////////////////////////////////////////////
    cout << "Проверка 1 [указатель]:" << "\n";
    int a=1,b=2;
    cout <<"До: " <<  "a=" << a << " b=" << b << "\n";
    changeP(&a,&b);
    cout <<"После: " <<  "a=" << a << " b=" << b << "\n \n";

    cout << "Проверка 1 [ссылка]:" << "\n";
    cout <<"До: " <<  "a=" << a << " b=" << b << "\n";
    changeR(a,b);
    cout <<"После: " <<  "a=" << a << " b=" << b << "\n \n";

    // 6 //////////////////////////////////////////////////
    cout << "Проверка 6 [указатель]:" << "\n";
    float x=1.4;
    cout << "До: " << x << "\n";
    roundingP(&x);
    cout << "После: " << x << "\n \n";

    cout << "Проверка 6 [ссылка]:" << "\n";
    float y=1.6;
    cout << "До: " << y << "\n";
    roundingR(y);
    cout << "После: " << y << "\n \n";

    // 9 //////////////////////////////////////////////////
    cout << "Проверка 9 [указатель]:" << "\n";
    Complex z={1.2,2.5};
    float c=2.2;
    cout << "До: " << "real=" << z.real << " " << "image: " << z.image << " Число= " << c << "\n";
    multiplicationP(&z,c);
    cout << "После: " << "real=" << z.real << " " << "image=" << z.image << "\n \n";

    cout << "Проверка 9 [ссылка]:" << "\n";
    cout << "До: " << "real=" << z.real << " " << "image: " << z.image << " Число= " << c << "\n";
    multiplicationR(z,c);
    cout << "После: " << "real=" << z.real << " " << "image=" << z.image << "\n \n";

    // 14 //////////////////////////////////////////////////
    cout << "Проверка 14 [указатель]:" << "\n";
    int matrix[3][3]={{1,2,3},
                      {4,5,6},
                      {7,8,9}};
    cout << "До: \n";
    for (int i=0;i<3;i++){
        for (int j=0;j<3;j++){
            cout<<matrix[i][j] << " ";
        }
        cout<<"\n";
    }
    transpositionP(matrix);
    cout << "После: \n";
    for (int i=0;i<3;i++){
        for (int j=0;j<3;j++){
            cout<<matrix[i][j] << " ";
        }
        cout<<"\n";
    }
    cout << "\n";

    cout << "Проверка 14 [ссылка]:" << "\n";
    cout << "До: \n";
    for (int i=0;i<3;i++){
        for (int j=0;j<3;j++){
            cout<<matrix[i][j] << " ";
        }
        cout<<"\n";
    }
    transpositionR(matrix);
    cout << "После: \n";
    for (int i=0;i<3;i++){
        for (int j=0;j<3;j++){
            cout<<matrix[i][j] << " ";
        }
        cout<<"\n";
    }
    cout << "\n";
}