#include <iostream>
using namespace std;

void qs(int *arr, int first, int last);

int main(){
    int n;
    cin >> n;
    int arr[n];
    for (int i=0;i<n;i++){cin >> arr[i];}
    qs(arr,0,n-1);
    for (int i=0;i<n;i++){cout << arr[i] << " ";}
}

int mediana(int *arr, int a, int b, int c){
    if ((arr[b] < arr[a] && arr[a] < arr[c]) || (arr[b] > arr[a] && arr[a] > arr[c])){return a;}
    if ((arr[a] < arr[b] && arr[b] < arr[c]) || (arr[a] > arr[b] && arr[b] > arr[c])){return b;}
    return c;
}

void qs(int *arr, int first, int last){
    if (first<last){
        int left=first;
        int right=last;
        int mid=arr[mediana(arr,left,(left+right)/2,right)];
        while (left<right) {
            while (arr[left] < mid) { left++; }
            while (arr[right] > mid) { right--; }
            if (left <= right) {
                int temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
                left++;
                right--;
            }
        }
        qs(arr,first,right);
        qs(arr,left,last);
    }
}