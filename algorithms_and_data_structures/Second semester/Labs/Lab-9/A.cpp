#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;

void addEdge(vector<vector<int>>& adj, int v, int w) {
    adj[v].push_back(w);
    adj[w].push_back(v);
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
        char act;
        int start, end;
        cin >> act >> start >> end;
        if (act=='?') {
            if (start == end) {
                cout << "YES" << "\n";
            } else {
                bool reachable_forward = isReachable(adj, start, end, n);
                if (reachable_forward)
                    cout << "YES" << "\n";
                else
                    cout << "NO" << "\n";
            }
        }
        else {
            adj[start].erase(remove(adj[start].begin(), adj[start].end(), end), adj[start].end());
            adj[end].erase(remove(adj[end].begin(), adj[end].end(), start), adj[end].end());
        }

    }

    return 0;
}
