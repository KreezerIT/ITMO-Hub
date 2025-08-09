#include <iostream>
using namespace std;

void countSort(string arr[], int n, int exp) {
    const int range = 256;
    string* output = new string[n];
    int count[range] = {0};

    for (int i = 0; i < n; i++) {
        int index = (exp < arr[i].length()) ? (int)arr[i][arr[i].length() - exp - 1] : 0;
        count[index]++;
    }
    for (int i = 1; i < range; i++) {
        count[i] += count[i - 1];
    }
    for (int i = n - 1; i >= 0; i--) {
        int index = (exp < arr[i].length()) ? (int)arr[i][arr[i].length() - exp - 1] : 0;
        output[count[index] - 1] = arr[i];
        count[index]--;
    }
    for (int i = 0; i < n; i++) {arr[i] = output[i];}
    delete[] output;
}

void radixSort(string arr[], int n, int t) {
    for (int exp = 0; exp < t; exp++) {
        countSort(arr, n, exp);
    }
}

int main() {
    int n,k,t;
    cin >> n >> k >> t;
    string arr[n];
    for (int i=0;i<n;i++){cin >> arr[i];}
    radixSort(arr, n, t);
    for (int i=0;i<n;i++){cout << arr[i] << "\n";}
    return 0;
}
