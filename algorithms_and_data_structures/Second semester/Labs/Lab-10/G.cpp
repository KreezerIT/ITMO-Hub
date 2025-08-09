#include <iostream>
#include <vector>
#include <algorithm>
#define pii pair<ull, ull>
#define ull unsigned long long
using namespace std;

class DSU {
private:
    vector<ull> parent;
    vector<ull> rank;

public:
    DSU(ull n) {
        parent.resize(n);
        rank.resize(n);
        for (ull i = 0; i < n; ++i) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    ull find(ull u) {
        if (parent[u] != u)
            parent[u] = find(parent[u]);
        return parent[u];
    }

    void unite(ull u, ull v) {
        ull rootU = find(u);
        ull rootV = find(v);
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

ull kruskal(const vector<pair<ull, pii>>& edges, ull n) {
    DSU dsu(n);
    ull total_cost = 0;

    for (const auto& edge : edges) {
        ull u = edge.second.first;
        ull v = edge.second.second;
        ull weight = edge.first;

        if (dsu.find(u) != dsu.find(v)) {
            dsu.unite(u, v);
            total_cost += weight;
        }
    }

    return total_cost;
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    ull N, M, K, goodCost=0;
    cin >> N >> M >> K;
    vector<pair<ull, pii>> edges(M+K);

    for (ull i = 0; i < M+K; ++i) {
        ull A, B, C;
        cin >> A >> B >> C;
        A--;
        B--;
        edges[i] = {C, {A, B}};
        if (i<M)
            goodCost+=C;
    }

    sort(edges.begin(), edges.end());
    ull min_cost = kruskal(edges, N);

    if ((min_cost-goodCost)<min_cost) {
        min_cost=(min_cost-goodCost);
    }
    else {min_cost=0; }

    cout << min_cost;
    return 0;
}
