#include <iostream>
#include <vector>
#include <algorithm>
#define pii pair<int, int>
using namespace std;

class DSU {
private:
    vector<int> parent;
    vector<int> rank;

public:
    DSU(int n) {
        parent.resize(n);
        rank.resize(n);
        for (int i = 0; i < n; ++i) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    int find(int u) {
        if (parent[u] != u)
            parent[u] = find(parent[u]);
        return parent[u];
    }

    void unite(int u, int v) {
        int rootU = find(u);
        int rootV = find(v);
        if (rootU != rootV) {
            if (rank[rootU] < rank[rootV])
                parent[rootU] = rootV;
            else if (rank[rootU] > rank[rootV])
                parent[rootV] = rootU;
            else {
                parent[rootV] = rootU;
                rank[rootU]++;
            }
        }
    }
};

int kruskal(const vector<pair<int, pii>>& edges, int n) {
    DSU dsu(n);
    int total_cost = 0;

    for (const auto& edge : edges) {
        int u = edge.second.first;
        int v = edge.second.second;
        int weight = edge.first;

        if (dsu.find(u) != dsu.find(v)) {
            dsu.unite(u, v);
            total_cost += weight;
        }
    }

    return total_cost;
}

int main() {
    int n, m;
    cin >> n >> m;
    vector<pair<int, pii>> edges(m);

    for (int i = 0; i < m; ++i) {
        int A, B, C;
        cin >> A >> B >> C;
        A--;
        B--;
        edges[i] = {C, {A, B}};
    }

    sort(edges.begin(), edges.end());

    int min_cost_1 = kruskal(edges, n);

    int min_cost_2 = INT_MAX;

    for (int i = 0; i < m; ++i) {
        vector<pair<int, pii>> temp_edges = edges;
        temp_edges.erase(temp_edges.begin() + i);

        int cost = kruskal(temp_edges, n);

        if (cost < min_cost_2 && cost > min_cost_1)
            min_cost_2 = cost;
    }

    cout << min_cost_1 << " " << min_cost_2;

    return 0;
}
