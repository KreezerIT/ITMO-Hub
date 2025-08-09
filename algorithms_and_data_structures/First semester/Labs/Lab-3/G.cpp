#include <iostream>
#include <algorithm>
using namespace std;

bool isPossible(int mid, int k, int seats[], int n) {
    int count = 1;
    int prev = seats[0];
    for (int i = 1; i < n; ++i) {
        if (seats[i] - prev >= mid) {
            prev = seats[i];
            count++;
        }
    }
    return count >= k;
}

int findMaxMinDistance(int k, int seats[], int n) {
    sort(seats, seats + n);

    int low = 0;
    int high = seats[n - 1] - seats[0];
    while (low < high) {
        int mid = low + (high - low) / 2;
        if (isPossible(mid, k, seats, n)) {low = mid + 1;}
        else {high = mid;}
    }
    return low - 1;
}

int main() {
    int n, k;
    cin >> n >> k;
    int seats[n];
    for (int i = 0; i < n; ++i) {cin >> seats[i];}
    int result = findMaxMinDistance(k, seats, n);
    cout << result << "\n";
    return 0;
}
