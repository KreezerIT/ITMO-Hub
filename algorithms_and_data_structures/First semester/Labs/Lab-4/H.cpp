#include <iostream>
#include <vector>
#include <string>
using namespace std;

const int BASE = 1000 * 1000 * 1000;
const int DIGITS_PER_ELEMENT = 9;

void add(vector<int>& a, vector<int>& b) {
    int carry = 0;
    for (int i = 0; i < max(a.size(), b.size()) || carry; ++i) {
        if (i == a.size())
            a.push_back(0);
        a[i] += carry + (i < b.size() ? b[i] : 0);
        carry = a[i] >= BASE;
        if (carry) a[i] -= BASE;
    }
}

void outputWithLeadingZeros(int num) {
    string numStr = to_string(num);
    if (numStr.length() < DIGITS_PER_ELEMENT) {
        cout << string(DIGITS_PER_ELEMENT - numStr.length(), '0');
    }
    cout << numStr;
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);

    int n;
    cin >> n;

    vector<int> sum;

    for (int j = 0; j < n; ++j) {
        string s;
        cin >> s;

        vector<int> a;
        for (int i = (int)(s.length()); i > 0; i -= DIGITS_PER_ELEMENT) {
            a.push_back(stoi(i >= DIGITS_PER_ELEMENT ? s.substr(i - DIGITS_PER_ELEMENT, DIGITS_PER_ELEMENT) : s.substr(0, i)));
        }

        add(sum, a);
    }

    bool first = true;
    for (int i = (int)(sum.size()) - 1; i >= 0; --i) {
        if (!first) {
            outputWithLeadingZeros(sum[i]);
        } else {
            cout << sum[i];
            first = false;
        }
    }

    return 0;
}
