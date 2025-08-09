#include "blazingio.hpp"
using namespace std;
#define s string

pair<int, int> WindowExtension(s &text, int left, int right) {
    while (left >= 0 && right < text.length() && text[left] == text[right]) {
        --left;
        ++right;
    }
    return {left + 1, right - 1};
}

void updateMaxPalindrome(int &start, int &maxLength, int low, int high) {
    int length = high - low + 1;
    if (length > maxLength) {
        start = low;
        maxLength = length;
    }
}

s expandingWindowMethod(s &text) {
    if (text.empty()) return "";
    int start = 0, maxLength = 1;

    for (int i = 0; i < text.length(); ++i) {
        auto [low1, high1] = WindowExtension(text, i, i);
        updateMaxPalindrome(start, maxLength, low1, high1);

        auto [low2, high2] = WindowExtension(text, i, i + 1);
        updateMaxPalindrome(start, maxLength, low2, high2);
    }

    return text.substr(start, maxLength);
}

int main() {
    /*
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
     */
    s inputString;
    cin >> inputString;
    cout << expandingWindowMethod(inputString);
    return 0;
}