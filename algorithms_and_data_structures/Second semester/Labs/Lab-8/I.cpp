#include <iostream>
#include <vector>
using namespace std;

const int MAXN = 100005;
vector<int> graph[MAXN];
int visited[MAXN];
int parent[MAXN];

void dfs(int v, int p) {
    visited[v] = true;
    parent[v] = p;
    for (int u : graph[v]) {
        if (!visited[u]) {
            dfs(u, v);
        }
    }
}

int find_parent(int v) {
    if (parent[v] == v)
        return v;
    return parent[v] = find_parent(parent[v]);
}

int main() {
    int n, m;
    cin >> n >> m;

    for (int i = 0; i < m; ++i) {
        int x, y;
        cin >> x >> y;
        graph[x].push_back(y);
        graph[y].push_back(x);
    }

    for (int i = 1; i <= n; ++i) {
        parent[i] = i;
    }

    int components = 0;
    for (int i = 1; i <= n; ++i) {
        if (!visited[i]) {
            ++components;
            dfs(i, i);
        }
    }

    if (components == 1 && m == n && n > 2) {
        cout << "ARCHIMEDES";
        return 0;
    }

    for (int i = 1; i <= n; ++i) {
        for (int j : graph[i]) {
            int u = find_parent(i);
            int v = find_parent(j);
            if (u != v) {
                parent[u] = v;
            }
        }
    }

    int cycles = 0;
    for (int i = 1; i <= n; ++i) {
        if (find_parent(i) == i) {
            int edges = 0;
            for (int j : graph[i]) {
                if (find_parent(j) == i) {
                    ++edges;
                }
            }
            if (edges == 2) {
                ++cycles;
            }
        }
    }

    if (cycles >= 3) {
        cout << "ARCHIMEDES";
    } else {
        cout << "EUCLID";
    }

    return 0;
}
