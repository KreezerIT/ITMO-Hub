#include <iostream>
#include <vector>
using namespace std;

int visitTime=1;
bool dfs_khun(int v,vector<int>& matching,  vector<vector<int>>& graph, vector<int>& used) {
    if (used[v]==visitTime) {return false;}
    used[v] = visitTime;
    for (auto u : graph[v]) {
        if (matching[u] == -1 || dfs_khun(matching[u],matching,graph, used)) {
            matching[u] = v;
            return true;
        }
    }
    return false;
}

void khunAlgoritm(int v, vector<int>& used, vector<vector<int>>& pairs) {
    used[v] = 1;
    for (auto u : pairs[v]) {
        if (used[u] != 1) {
            khunAlgoritm(u,used,pairs);
        }
    }
}

int main() {
    int n, m, k;
    cin >> n >> k >> m;
    vector<int> matching(n+k,-1), used(n+k,0);
    vector<vector<int>> graph(n+k), pairs(n+k);

    for (int i = 0; i < m; ++i) {
        int u, v;
        cin >> u >> v;
        u--; v--;
        graph[u].push_back(n + v);
    }

    for (int i = 0; i < n + k; ++i) {
        ++visitTime;
        dfs_khun(i,matching,graph,used);
    }

    used=vector<int>(n+k,0);
    for (int i = 0; i < n + k; ++i) {
        for (int j = 0; j < graph[i].size(); ++j) {
            if (matching[graph[i][j]] == i) {continue;}
            else pairs[i].push_back(graph[i][j]);
        }
        if (matching[i] != -1) {
            used[matching[i]] = 2;
            pairs[i].push_back(matching[i]);
        }
    }

    for (int i = 0; i < n; ++i) {if (used[i] == 0) khunAlgoritm(i,used,pairs);}
    for (int i = 0; i < n; ++i) {if (used[i] == 2) used[i] = 0;}

    int Lcount = 0;
    for (int i = 0; i < n; ++i) {if (used[i] == 0) Lcount++;}
    cout << Lcount << "\n";
    for (int i = 0; i < n; ++i) {if (used[i] == 0) cout << i + 1 << " ";}
    cout << "\n";

    int Rcount = 0;
    for (int i = n; i < n + k; ++i) {if (used[i] == 1) Rcount++;}
    cout << Rcount << "\n";
    for (int i = n; i < n + k; ++i) {if (used[i] == 1) cout << i + 1 - n << " ";}
}