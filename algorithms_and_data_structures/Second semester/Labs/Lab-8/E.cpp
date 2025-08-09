#include <iostream>
#include <vector>

using namespace std;

bool isCyclicUtil(int v, vector<vector<int>>& adj, bool visited[], int parent) {
    visited[v] = true;

    for (int j = 0; j < adj[v].size(); ++j) {
        int i = adj[v][j];
        if (!visited[i]) {
            if (isCyclicUtil(i, adj, visited, v))
                return true;
        } else if (i != parent) {
            return true;
        }
    }

    return false;
}

void addEdge(vector<vector<int>>& adj, int v, int w) {
    adj[v].push_back(w);
    adj[w].push_back(v);
}

bool isCyclic(int n, vector<vector<int>>& adj) {
    bool *visited = new bool[n];
    for (int i = 0; i < n; i++)
        visited[i] = false;

    for (int u = 0; u < n; u++) {
        if (!visited[u]) {
            if (isCyclicUtil(u, adj, visited, -1))
                return true;
        }
    }
    return false;
}

int main() {
    int n, v;
    cin >> n >> v;

    vector<vector<int>> adj(n);

    for (int i = 0; i < v; i++) {
        int a, b;
        cin >> a >> b;
        addEdge(adj, a - 1, b - 1);
    }

    if (isCyclic(n, adj))
        cout << "YES";
    else
        cout << "NO";

    return 0;
}
