#include <iostream>
#define ll long long
using namespace std;

void Hf (ll& HashSum, ll x, ll n){
    HashSum+=(x*x) % n;
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    ll n;
    cin >> n;
    ll hashFirst = 0, hashSecond = 0;
    ll summFirst = 0, summSecond = 0;
    for (int i = 0; i < n; ++i) {
        ll a;
        cin >> a;
        summFirst+=a;
        Hf (hashFirst,a,n);
    }
    for (int i = 0; i < n; ++i) {
        ll a;
        cin >> a;
        summSecond+=a;
        Hf (hashSecond,a,n);
    }

    if (summFirst!=summSecond) {
        cout << "NO";
        return 0;
    }

    cout << (hashFirst == hashSecond ? "YES" : "NO");
    return 0;
}