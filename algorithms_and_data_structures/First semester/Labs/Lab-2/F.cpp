#include <iostream>
using namespace std;

struct film{
    long long rating;
    int index;
};

int part(film* a, int l, int r){
    film v = a[(l+r)/2];
    int i=l,j=r;
    while (i<=j){
        while(a[i].rating>v.rating){i++;}
        while(a[j].rating<v.rating){j--;}
        if (i>=j){break;}
        swap(a[i++],a[j--]);
    }
    return j;
}

void qs(film* a, int l, int r){
    if (l<r){
        int q=part(a,l,r);
        qs(a,l,q);
        qs(a,q+1,r);
    }
}

long long mod=1000000007;
int main(){
    long long n,m,k,a,mn;
    cin >> n >> m >> k;
    int avt[n];
    film* film_data;
    film_data= new film[m];
    for (int i=0;i<m;i++){film_data[i].index=i+1; film_data[i].rating=0;}
    for (int i=0;i<n;i++){cin >> avt[i];}
    for (int i=0;i<m;i++){
        for (int j=0;j<n;j++){
            a=avt[j];
            cin >> mn;
            film_data[i].rating=(film_data[i].rating + a*mn)%mod;
        }
        film_data[i].rating%=mod;
    }
    qs(film_data,0,m-1);
    for (int i=0;i<k;i++){cout << film_data[i].index  << " ";}
}