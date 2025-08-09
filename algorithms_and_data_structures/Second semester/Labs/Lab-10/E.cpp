#include <iostream>
#include <vector>
#include <set>
#define UNVISITED -1
using namespace std;

void addEdge(vector<vector<int>>& adj, int u, int v) {
    adj[u].push_back(v);
    adj[v].push_back(u);
}

void dfs(int b, int parents, int& time, vector<int>& enter, vector<int>& ret, vector<vector<int>>& adj, vector<bool>& visited, set<pair<int, int>>& bridges) {
    visited[b] = true;
    time++;
    enter[b] = ret[b] = time;

    for (int a : adj[b]) {
        if (a == parents) continue;

        if (enter[a] == UNVISITED) {
            dfs(a, b, time, enter, ret, adj, visited, bridges);
            ret[b] = min(ret[b], ret[a]);
            if (ret[a] > enter[b]) {
                bridges.insert({min(a, b), max(a, b)});
            }
        } else {
            ret[b] = min(ret[b], enter[a]);
        }
    }
}

set<pair<int, int>> findBridges(vector<vector<int>>& adj) {
    vector<int> enter(adj.size()+1, UNVISITED);
    vector<int> ret(adj.size()+1);
    vector<bool> visited(adj.size()+1, false);
    set<pair<int, int>> bridges;

    int time = 0;
    for (int i = 1; i < adj.size(); ++i) {
        if (!visited[i]) {
            dfs(i, -1, time, enter, ret, adj, visited, bridges);
        }
    }

    return bridges;
}

struct DSU {
    vector<int> parents;

    DSU(int n) {
        parents.resize(n);
        for (int i = 0; i < n; ++i)
            parents[i] = -1;
    }

    int find(int v) {
        if (parents[v]<0)
            return v;
        return parents[v] = find(parents[v]);
    }

    void unite(int a, int b) {
        if (find(a)==find(b)){
            return;
        }

        a = find(a);
        b = find(b);
        if (parents[a] > parents[b]) {
            swap (a,b);
        }
        parents[a] += parents[b];
        parents[b] = a;
    }

    bool sameComp(int u, int v) {
        if (find(u) == find(v)){
            return true;
        }
        return false;
    }
};

int main() {
    int n, m, q;
    cin >> n >> m >> q;

    vector<vector<int>> adj(n + 1);
    DSU dsu(n+1);

    for (int i = 0; i < m; ++i) {
        int u, v;
        cin >> u >> v;
        addEdge(adj, u, v);
    }

    set<pair<int, int>> bridges=findBridges(adj);

    for (int i=0;i<adj.size();++i){
        for (auto v: adj[i]){
            if (bridges.count({v,i}) || bridges.count({i,v})) {
                continue;
            }
            dsu.unite(v,i);
        }
    }

    vector<pair<int,int>> queries(q);
    for (int i=0;i<q;++i){
        int a,b;
        cin >> a >> b;
        queries[i].first=a;
        queries[i].second=b;
    }


    for (int i = 0; i < queries.size(); ++i) {
        if (dsu.sameComp(queries[i].first,queries[i].second))
            cout << "YES\n";
        else
            cout << "NO\n";
    }

    return 0;
}
