#include <iostream>
using namespace std;

void heap(int *arr, int i, int n){
    int max=i;
    while (true){
        int child=2*i+1;
        if (child<n && arr[child]>arr[max]){max=child;}
        child++;
        if (child<n && arr[child]>arr[max]){max=child;}

        if (max==i){break;}
        else{
            swap(arr[max],arr[i]);
            i=max;
        }
    }
}

void heapsort(int *a, int n){
    for (int i=n/2;i>=0;i--){
        heap(a,i,n);
    }
    for (int i=n-1;i>=1;i--){
        swap(a[0],a[i]);
        heap(a,0,i);
    }
}

int main(){
    int n;
    cin >>n;
    int arr[n];
    for (int i=0;i<n;i++){cin>>arr[i];}
    heapsort(arr,n);
    for (int i=0;i<n;i++){cout<<arr[i] << " ";}
    return 0;
}