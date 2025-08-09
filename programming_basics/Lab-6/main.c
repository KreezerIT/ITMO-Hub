#include <stdio.h>
#include <stdlib.h>

int main() {
    //Статический массив
    float array[4] = {56.4, 36.5, 7.7, 44.3};
    float *ptr=array;

    for(int i=0;i<4;i++){
        printf("%.1f ", *(ptr+i));
    }
    printf("\n");

    //Динамический массив
    float *array2 = (float*)malloc(4*sizeof(float));
    array2[0] = 56.4;
    array2[1] = 36.5;
    array2[2] = 7.7;
    array2[3] = 44.3;

    for(int i=0;i<4;i++){
        printf("%.1f ", array2[i]);
    }

    free(array2);
    return 0;
}
