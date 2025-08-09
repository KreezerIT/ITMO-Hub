#include <iostream>
#include <vector>
using namespace std;

void dfs(int v, const vector<vector<int>>& adj_list, vector<bool>& visited) {
    visited[v] = 1;
    for (int i = 0; i < adj_list[v].size(); ++i) {
        int u = adj_list[v][i];
        if (!visited[u]) {
            dfs(u, adj_list, visited);
        }
    }
}

int main() {
    int n, m, x, y;
    cin >> n >> m;
    vector<vector<int>> adj_list(n+1);
    vector<bool> visited(n+1,0);

    for (int i = 0; i < m; ++i) {
        cin >> x >> y;
        adj_list[x].push_back(y);
        adj_list[y].push_back(x);
    }

    int connections = 0;
    for (int i = 1; i <= n; ++i) {
        if (!visited[i]) {
            dfs(i, adj_list, visited);
            connections++;
        }
    }

    cout << connections;
    return 0;
}