#include <iostream>
#include <vector>
#include <algorithm>
#define ull unsigned long long
using namespace std;

ull dsu_get(ull v, vector<ull>& p) {
    return (v == p[v]) ? v : (p[v] = dsu_get(p[v], p));
}

void dsu_unite(ull a, ull b, vector<ull>& p) {
    a = dsu_get(a, p);
    b = dsu_get(b, p);
    if (rand() & 1) {
        swap(a, b);
    }
    if (a != b) {
        p[a] = b;
    }
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    ull n, m;
    cin >> n >> m;

    vector<pair<ull, pair<ull, ull>>> edges(m);
    vector<ull> p(n);
    for (ull i = 0; i < m; ++i) {
        ull u, v, w;
        cin >> u >> v >> w;
        u--; v--;
        edges[i] = {w, {u, v}};
    }

    sort(edges.begin(), edges.end());
    for (ull i = 0; i < n; ++i) {
        p[i] = i;
    }

    ull mst_weight = 0;
    vector<pair<ull, ull>> mst_edges;
    for (ull i = 0; i < m; ++i) {
        ull a = edges[i].second.first, b = edges[i].second.second, l = edges[i].first;
        if (dsu_get(a, p) != dsu_get(b, p)) {
            mst_weight += l;
            mst_edges.push_back({a, b});
            dsu_unite(a, b, p);
        }
    }

    cout << mst_weight;

    return 0;
}