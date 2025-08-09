#include <iostream>
using namespace std;

int main(){
    int n,summa=0,sumleft=0;
    cin >> n;
    int arr[n];
    for (int i=0;i<n;i++){cin >> arr[i];}
    for (int i=0;i<n;i++){summa+=arr[i];}
    for (int i=0;i<n;i++){
        summa-=arr[i];
        if (sumleft==summa){cout << i; return 0;}
        sumleft+=arr[i];
    }
    cout << "-1";
}