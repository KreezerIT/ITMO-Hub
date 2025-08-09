#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

vector<int> getDegrees(const vector<vector<int>>& edges, int n) {
    vector<int> degrees(n + 1, 0);

    for (const auto& edge : edges) {
        degrees[edge[0]]++;
        degrees[edge[1]]++;
    }

    return degrees;
}

void sortDegrees(vector<int>& degrees) {
    sort(degrees.begin(), degrees.end());
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    int n;
    cin >> n;

    vector<vector<int>> tree1Edges(n - 1, vector<int>(2));
    for (int i = 0; i < n - 1; ++i) {
        cin >> tree1Edges[i][0] >> tree1Edges[i][1];
    }

    vector<vector<int>> tree2Edges(n - 1, vector<int>(2));
    for (int i = 0; i < n - 1; ++i) {
        cin >> tree2Edges[i][0] >> tree2Edges[i][1];
    }

    vector<int> tree1Degrees = getDegrees(tree1Edges, n);
    vector<int> tree2Degrees = getDegrees(tree2Edges, n);

    sortDegrees(tree1Degrees);
    sortDegrees(tree2Degrees);

    if (tree1Degrees == tree2Degrees) {
        cout << "YES";
    } else {
        cout << "NO";
    }

    return 0;
}
