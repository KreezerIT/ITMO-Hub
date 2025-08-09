#include <iostream>
#include <stack>
using namespace std;

bool isValid(string s) {
    stack<char> st;
    const int n = s.length();
    char opening[] = {'(', '[', '{'};
    char closing[] = {')', ']', '}'};

    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < 3; ++j) {
            if (s[i] == opening[j]) {
                st.push(s[i]);
            } else if (s[i] == closing[j]) {
                if (st.empty() || st.top() != opening[j]) {
                    return false;
                }
                st.pop();
            }
        }
    }

    return st.empty();
}

int main() {
    int n;
    cin >> n;
    string brackets;
    cin >> brackets;

    cout << (isValid(brackets) ? "YES" : "NO");

    return 0;
}
