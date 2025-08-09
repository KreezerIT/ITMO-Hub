#include <iostream>
#include <vector>
#include <algorithm>
#include <set>
#define UNVISITED -1
using namespace std;

void dfs(int b, int parent, int& time, vector<int>& enter, vector<int>& ret, vector<vector<int>>& adj, vector<bool>& visited, set<pair<int, int>>& bridges) {
    visited[b] = true;
    time++;
    enter[b] = ret[b] = time;

    for (int a : adj[b]) {
        if (a == parent) continue;

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

int main() {
    int n, m;
    cin >> n >> m;
    vector<vector<int>> adj(n+1);

    for (int i = 0; i < m; ++i) {
        int a, b;
        cin >> a >> b;
        adj[a].push_back(b);
        adj[b].push_back(a);
    }

    set<pair<int, int>> bridges = findBridges(adj);

    for (auto& bridge : bridges) {
        cout << bridge.first << " " << bridge.second << "\n";
    }

    return 0;
}
