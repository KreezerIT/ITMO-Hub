#include <iostream>
using namespace std;

int part(long long a[], int l, int r){
    int v = a[(l+r)/2];
    int i=l,j=r;
    while (i<=j){
        while(a[i]>v){i++;}
        while(a[j]<v){j--;}
        if (i>=j){break;}
        swap(a[i++],a[j--]);
    }
    return j;
}
void qs(long long a[], int l, int r){
    if (l<r){
        int q=part(a,l,r);
        qs(a,l,q);
        qs(a,q+1,r);
    }
}

int main() {
    int n;
    cin >> n;
    long long vetki[n], a, b, s = 0;
    for (int i = 0; i < n; i++) { cin >> vetki[i]; }
    qs(vetki, 0, n - 1);
    for (int i = 0; i < n - 1; ++i) {
        if (vetki[i] == vetki[i + 1] || (vetki[i] - 1) == vetki[i + 1]) {
            if (vetki[i] == vetki[i + 1]){a = vetki[i];}
            else {a=vetki[i]-1;}
            for (int j = i + 2; j < n - 1; j++) {
                if (vetki[j] == vetki[j + 1]){
                    b = vetki[j];
                    s += a * b;
                    i=j+1;
                    break;
                }
                if ((vetki[j] - 1) == vetki[j + 1]){
                    b = (vetki[j]-1);
                    s += a * b;
                    i=j+1;
                    break;
                }
            }
        }
    }
    cout << s;
    return 0;
}