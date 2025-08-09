#include <iostream>
#include <vector>
#include <string>
using namespace std;

int main() {
    string str;
    cin >> str;
    vector<int> ans(str.size(),0);
    vector<int> pi(str.size(),0);

    for (int i = 1; i < str.size(); i++) {
        int j = pi[i - 1];
        while (j > 0 && str[i] != str[j]) {
            j = pi[j - 1];
        }
        if (str[i] == str[j]) {
            j++;
        }
        pi[i] = j;
    }

    for (int i = 0; i < str.size(); i++) {
        ans[i] = pi[i];
    }

    for (int i : ans) {
        cout << i << " ";
    }
    return 0;
}
