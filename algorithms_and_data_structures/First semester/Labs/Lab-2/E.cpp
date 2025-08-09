#include <iostream>
using namespace std;

long long ans=0;
void merge(int *arr, int left, int mid, int right){
    int n1=mid-left+1;
    int n2=right-mid;
    int leftHalf[n1], rightHalf[n2];
    for (int i=0;i<n1;i++){leftHalf[i]=arr[left+i];}
    for (int j=0;j<n2;j++){rightHalf[j]=arr[mid+1+j];}
    int i=0,j=0,k=left;
    while (i<n1 && j<n2){
        if (leftHalf[i]<=rightHalf[j]){
            arr[k]=leftHalf[i];
            i++;
        }
        else {
            arr[k]=rightHalf[j];
            j++;
            ans+=n1-i;
        }
        k++;
    }
    while (i<n1){
        arr[k]=leftHalf[i];
        i++;
        k++;
    }
    while (j<n2){
        arr[k]=rightHalf[j];
        j++;
        k++;
    }
}

void mergeSort(int *arr, int left, int right){
    if (left<right){
        int mid=left+(right-left)/2;
        mergeSort(arr,left,mid);
        mergeSort(arr,mid+1,right);
        merge(arr,left,mid,right);
    }
}

int main(){
    int n;
    cin >> n;
    int arr[n];
    for (int i=0;i<n;i++){cin >> arr[i];}
    mergeSort(arr,0,n-1);
    cout << ans;
    return 0;
}