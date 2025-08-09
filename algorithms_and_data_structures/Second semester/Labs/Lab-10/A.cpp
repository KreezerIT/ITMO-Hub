#include <iostream>
#include <vector>
using namespace std;

void dfs (int v, const vector<vector<int>>& g, vector<int>& parent) {
    for (int i=0;i<g[v].size();++i){
        int to=g[v][i];
        if (to!=parent[v]){
            parent[to]=v;
            dfs(to,g,parent);
        }
    }
}

vector<int> prufer_code(const vector<vector<int>>& g, int n) {
    vector<int> parent(n);
    vector<int> degree(n);
    parent[n-1]=-1;
    dfs(n-1,g,parent);

    int ptr=-1;
    for (int i=0;i<n;++i){
        degree[i] = (int) g[i].size();
        if (degree[i] == 1 && ptr == -1)
            ptr = i;
    }

    vector<int> result;
    int leaf = ptr;
    for (int i=0; i<n-2; ++i) {
        int next = parent[leaf];
        result.push_back (next);
        --degree[next];
        if (degree[next] == 1 && next < ptr)
            leaf = next;
        else {
            ++ptr;
            while (ptr<n && degree[ptr] != 1)
                ++ptr;
            leaf = ptr;
        }
    }
    return result;
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    int n, m;
    cin >> n >> m;

    vector<vector<int>> g(n);
    for (int i = 0; i < m; ++i) {
        int u, v;
        cin >> u >> v;
        u--; v--;
        g[u].push_back(v);
        g[v].push_back(u);
    }

    vector<int> code = prufer_code(g, n);
    for (int i : code) {
        cout << i + 1 << " ";
    }

    return 0;
}