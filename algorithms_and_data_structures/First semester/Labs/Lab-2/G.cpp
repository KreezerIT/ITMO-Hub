#include <iostream>
using namespace std;

struct laba{
    long long dnei;
    long long krai;
};

void qs(laba* arr, int first, int last);
int main(){
    int n;
    long long s=0,days=0;
    cin >> n;
    laba arr[n];
    for (int i=0;i<n;i++){cin >> arr[i].dnei >> arr[i].krai;}
    for (int i=0;i<n;i++){days+=arr[i].dnei;}
    qs(arr,0,n-1);
    int i=0;
    for (long long j=0;j<days;){
        j+=arr[i].dnei;
        s+=(arr[i].krai-j);
        i++;
    }
    cout << s;
    return 0;
}

int mediana(laba* arr, int a, int b, int c){
    if ((arr[b].dnei < arr[a].dnei && arr[a].dnei < arr[c].dnei) || (arr[b].dnei > arr[a].dnei && arr[a].dnei > arr[c].dnei)){return a;}
    if ((arr[a].dnei < arr[b].dnei && arr[b].dnei < arr[c].dnei) || (arr[a].dnei > arr[b].dnei && arr[b].dnei > arr[c].dnei)){return b;}
    return c;
}
void qs(laba* arr, int first, int last){
    if (first<last){
        int left=first;
        int right=last;
        laba mid=arr[mediana(arr,left,(left+right)/2,right)];
        while (left<right) {
            while (arr[left].dnei < mid.dnei) { left++; }
            while (arr[right].dnei > mid.dnei) { right--; }
            if (left <= right) {
                laba temp = arr[left];
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