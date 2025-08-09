#include <iostream>
#include <vector>
using namespace std;

struct Point{
    int x;
    int y;
};

int hash_function(int key, int n){
    return (key%n);
}

bool hash_search(vector<int>& hash_table, int key, int n) {
    int index = hash_function(key,n);
    int original_index = index;

    while (hash_table[index] != INT_MAX) {
        if (hash_table[index] == key) {
            return true;
        }
        index++;
        index %= n;

        if (index == original_index)
            break;
    }
    return false;
}

void hash_insert(vector<int>& hash_table, int key, int n) {
    int index = hash_function(key,n);
    while (hash_table[index] != INT_MAX && hash_table[index] != key) {
        index = (index + 1) % n;
    }
    hash_table[index] = key;
}

void insertChoise(bool& Thread, int& Flag, vector<int>& xs, int x, vector<int>& ys, int y, int n){
    if (Flag == false) {
        hash_insert(xs, x, n);
    } else {
        hash_insert(ys, y, n);
    }
    Thread = true;
}

int main() {
    int n;
    cin >> n;
    vector<Point> points;
    for (int i = 0; i < n; ++i) {
        int x, y;
        cin >> x >> y;
        points.push_back({x, y});
    }

    for (auto firstFlag = 0; firstFlag <= 1; firstFlag++) {
        for (auto secondFlag = 0; secondFlag <= 1; secondFlag++) {
            for (auto thirdFlag = 0; thirdFlag <= 1; thirdFlag++) {
                bool firstThread = false;
                bool secondThread = false;
                bool thirdThread = false;

                bool ans = true;
                vector<int> xs(n, INT_MAX);
                vector<int> ys(n, INT_MAX);
                for (int i = 0; i < n; ++i) {
                    if (hash_search(xs, points[i].x, n) || hash_search(ys, points[i].y, n)) {
                        continue;
                    } else {
                        if (!firstThread) {
                            insertChoise(firstThread, firstFlag, xs, points[i].x, ys, points[i].y, n);

                        } else if (!secondThread) {
                            insertChoise(secondThread, secondFlag, xs, points[i].x, ys, points[i].y, n);

                        } else if (!thirdThread) {
                            insertChoise(thirdThread, thirdFlag, xs, points[i].x, ys, points[i].y, n);

                        } else {
                            ans = false;
                        }
                    }
                }
                if (ans) {
                    cout << "YES\n";
                    return 0;
                }
            }
        }
    }
    cout << "NO\n";
    return 0;
}