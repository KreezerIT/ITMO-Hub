#include <iostream>
using namespace std;

void swap(int* a, int i, int j) {
    int temp = a[i];
    a[i] = a[j];
    a[j] = temp;
}

int main() {
    int n;
    cin >> n;
    int perm[n];
    for (int i = 0; i < n; i++) {perm[i] = i + 1;}
    for (int i = 2; i < n; i++) {
        swap(perm, i / 2 , i);
    }
    for (int i=0;i<n;i++){cout << perm[i] << " ";}
    return 0;
}


