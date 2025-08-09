#include <iostream>
#include <algorithm>
using namespace std;

int main() {
    long long n, k;
    cin >> n >> k;
    int women[n], men[n];
    for (int i = 0; i < n; ++i) {cin >> women[i];}
    for (int i = 0; i < n; ++i) {cin >> men[i];}

    sort(women, women + n);
    sort(men, men + n);

    int left = women[0] + men[0];
    int right = women[n - 1] + men[n - 1];

    while (left < right) {
        int mid = left + (right - left) / 2;
        int j = n - 1;
        long long count = 0;

        for (int i = 0; i < n; ++i) {
            while (j >= 0 && women[i] + men[j] > mid) {j--;}
            count += (j + 1);
        }

        if (count >= k) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }

    cout << left << "\n";
    return 0;
}
