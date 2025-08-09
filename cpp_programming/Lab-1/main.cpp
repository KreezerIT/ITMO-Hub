#include "header.h"

// 1 //////////////////////////////////////////////////
void changeP(int* a, int* b){
    int temp=*a;
    *a=*b;
    *b=temp;
}

void changeR(int& a, int& b) {
    int temp = a;
    a = b;
    b = temp;
}

// 6 //////////////////////////////////////////////////
void roundingP(float* x){
    *x=round(*x);
}

void roundingR(float& x) {
    x = round(x);
}

// 9 //////////////////////////////////////////////////
void multiplicationP(Complex* a, float b){
    a->real *=b;
    a->image *=b;
}

void multiplicationR(Complex& a, float b){
    a.real *=b;
    a.image *=b;
}

// 14 //////////////////////////////////////////////////
void transpositionP(int (*x)[3]){
    for (int i=0;i<3;i++){
        for (int j=i+1;j<3;j++){
            int temp=x[i][j];
            x[i][j]=x[j][i];
            x[j][i]=temp;
        }
    }
}

void transpositionR(int (&x)[3][3]){
    for (int i=0;i<3;i++){
        for (int j=i+1;j<3;j++){
            int temp=x[i][j];
            x[i][j]=x[j][i];
            x[j][i]=temp;
        }
    }
}
