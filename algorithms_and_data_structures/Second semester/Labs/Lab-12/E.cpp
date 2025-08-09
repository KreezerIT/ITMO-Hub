#include <iostream>
#include <vector>
#define MAXN 10000000
using namespace std;

void hash_inc(vector<int> &diffs, int key) {
    diffs[key + MAXN/2 % MAXN]++;
}

int hash_search(vector<int> &diffs, int key) {
    return diffs[key + MAXN/2 % MAXN];
}

int main() {
    int n;
    cin >> n;

    vector<int> s(n, 0);
    for (int i = 0; i < n; i++) {
        cin >> s[i];
    }

    vector<int> diffs(MAXN, 0);
    int ans = 0;

    for (int i = 0; i < n; i++) {
        int request = hash_search(diffs, s[i] - i);

        if (request != 0) {
            ans += request;
        }

        hash_inc(diffs, s[i] - i);
    }

    cout << ans;
    return 0;
}
