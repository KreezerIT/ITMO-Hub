#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

void dfs(int x, vector<int>& a, vector<int>& b, vector<int>& c, const vector<vector<pair<int, int>>>& adj, vector<bool>& visited) {
    visited[x] = true;
    for (auto [i, l] : adj[x]) {
        if (!visited[i]) {
            dfs(i, a, b, c, adj, visited);
            a[x] = max(a[x], b[i] + l - c[i]);
            b[x] += c[i];
        }
    }
    a[x] += b[x];
    c[x] = max(a[x], b[x]);
}

int dsu_parent(int v,  vector<int>& p) {
    return (v == p[v]) ? v : (p[v] = dsu_parent(p[v], p));
}

void dsu_unite(int a, int b, vector<int>& p) {
    a = dsu_parent(a, p);
    b = dsu_parent(b, p);
    if (a != b)
        p[b] = a;
}

bool compareEdges(const pair<int, pair<int, int>>& a, const pair<int, pair<int, int>>& b) {
    return a.first < b.first;
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    int n, m;
    cin >> n >> m;
    vector<pair<int, pair<int, int>>> edges(m);
    vector<int> p(n);
    vector<vector<pair<int, int>>> adj(n);

    for (int i = 0; i < m; ++i) {
        int u, v, w;
        cin >> u >> v >> w;
        u--; v--;
        edges[i] = {w, {u, v}};
    }

    sort(edges.begin(), edges.end(), compareEdges);
    for (int i = 0; i < n; ++i) {
        p[i] = i;
    }


    for (int i = 0; i < m; ++i) {
        int a = edges[i].second.first, b = edges[i].second.second, l = edges[i].first;
        if (dsu_parent(a, p) != dsu_parent(b, p)) {
            dsu_unite(a, b, p);
            adj[a].push_back({b, l});
            adj[b].push_back({a, l});
        }
    }

    vector<int> a(n, 0), b(n, 0), c(n, 0);
    vector<bool> visited(n, false);
    dfs(0, a, b, c, adj, visited);

    cout << c[0];

    return 0;
}
