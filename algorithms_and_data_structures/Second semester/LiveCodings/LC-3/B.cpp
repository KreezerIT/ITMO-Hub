#include <iostream>
#include <vector>
#include <algorithm>
#define INF INT_MAX
using namespace std;

int actualCycle = 0;
int minCycle = INF;

void dfs(int v, int p, const vector<vector<int>>& adj, vector<string>& used, vector<int>& parent,  int& minCycle) {
    used[v] = "busy";
    parent[v] = p;

    for (int u : adj[v]) {
        if (used[u] == "free") {
            dfs(u, v, adj, used, parent, minCycle);
        } else if (used[u] == "busy" && u != p) {
            int cur = v;
            actualCycle = 1;
            while (cur != u) {
                cur = parent[cur];
                actualCycle++;
            }
            minCycle = min(minCycle, actualCycle);
        }
    }

    used[v] = "done";
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    int n, m;
    cin >> n >> m;

    vector<vector<int>> adj(n);
    vector<string> used(n, "free");
    vector<int> parent(n, INF);

    for (int i = 0; i < m; ++i) {
        int x, y;
        cin >> x >> y;
        --x; --y;
        adj[x].push_back(y);
        adj[y].push_back(x);
    }

    for (int i = 0; i < n; i++) {
        if (used[i] == "free") {
            dfs(i, -1, adj, used, parent, minCycle);
        }
    }

    cout << minCycle;
    return 0;
}