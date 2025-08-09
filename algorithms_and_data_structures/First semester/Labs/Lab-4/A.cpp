#include <iostream>
#include <vector>

using namespace std;

bool isMinHeap(const vector<int>& arr) {
    int n = arr.size();

    for (int i = 1; i <= n; ++i) {
        if (2 * i <= n && arr[i - 1] > arr[2 * i - 1]) {
            return false;
        }
        if (2 * i + 1 <= n && arr[i - 1] > arr[2 * i]) {
            return false;
        }
    }

    return true;
}

int main() {
    int n;
    cin >> n;

    vector<int> arr(n);
    for (int i = 0; i < n; ++i) {
        cin >> arr[i];
    }

    if (isMinHeap(arr)) {
        cout << "YES";
    } else {
        cout << "NO";
    }

    return 0;
}
