#include <iostream>
#include <vector>
using namespace std;

void shift(vector<int>& arr, int k) {
    if (k < 0) {
        for (int i = 0; i < abs(k); i++) {
            arr.push_back(arr[0]);
            arr.erase(arr.begin());
        }
    }
    else {
        for (int i = 0; i < k; i++) {
            arr.insert(arr.begin(), arr[arr.size() - 1]);
            arr.pop_back();
        }
    }
}

int main() {
    int n, k;
    cin >> n >> k;
    k=k%n;
    vector<int> a(n);
    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }
    shift(a, k);
    for (int i = 0; i < n; i++) {
        cout << a[i] << " ";
    }
    return 0;
}