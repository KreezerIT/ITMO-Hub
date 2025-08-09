#include <iostream>
#include <vector>

using namespace std;

vector<vector<bool>> buildCode(int n) {
    vector<vector<bool>> grayCode(1 << n, vector<bool>(n, false));

    grayCode[1][n - 1] = true;
    int p = 2;

    for (int i = 2; i <= n; ++i) {
        int t = p;
        p *= 2;

        for (int k = p / 2; k < p; ++k) {
            grayCode[k] = grayCode[t - 1];
            grayCode[t - 1][n - i] = false;
            grayCode[k][n - i] = true;
            --t;
        }
    }

    return grayCode;
}

int main() {
    int n;
    cin >> n;

    vector<vector<bool>> grayCode = buildCode(n);

    for (const auto &code : grayCode) {
        for (bool bit : code) {
            cout << bit;
        }
        cout << "\n";
    }

    return 0;
}
