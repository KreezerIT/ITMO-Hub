#include <iostream>
#include <vector>
#include <queue>
using namespace std;

void addEdge(vector<vector<int>>& adj, int v, int w) {
    adj[v].push_back(w);
}

bool isReachable(vector<vector<int>>& adj, int start, int end, int n) {
    vector<bool> visited(n+1, false);
    visited[start] = true;
    queue<int> q;
    q.push(start);

    while (!q.empty()) {
        int u = q.front();
        q.pop();
        for (int v : adj[u]) {
            if (v == end)
                return true;
            if (!visited[v]) {
                visited[v] = true;
                q.push(v);
            }
        }
    }
    return false;
}

int main() {
    int n, m, q;
    cin >> n >> m >> q;
    vector<vector<int>> adj(n+1);

    for (int i = 0; i < m; i++) {
        int a, b;
        cin >> a >> b;
        addEdge(adj, a , b);
    }

    for (int i = 0; i < q; ++i) {
        int start, end;
        cin >> start >> end;
        bool reachable_forward = isReachable(adj, start, end, n);
        bool reachable_backward = isReachable(adj, end, start, n);
        if (reachable_forward && reachable_backward)
            cout << "YES" << endl;
        else
            cout << "NO" << endl;
    }

    return 0;
}
