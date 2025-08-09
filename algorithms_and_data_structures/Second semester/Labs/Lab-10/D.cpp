#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
using namespace std;

struct Edge {
    int to;
    int id;
};

void dfs(int v, vector<bool>& visited, const vector<vector<Edge>>& g) {
    visited[v] = true;
    for (Edge e : g[v]) {
        if (!visited[e.to])
            dfs(e.to, visited, g);
    }
}

bool checkForEulerPath(const vector<vector<Edge>>& g) {
    int n = g.size();

    for (int v = 0; v < n; ++v) {
        if (g[v].size() % 2 != 0)
            return false;
    }

    vector<bool> visited(n, false);
    dfs(0, visited, g);

    for (int v = 0; v < n; ++v) {
        if (!g[v].empty() && !visited[v])
            return false;
    }

    return true;
}

void findEulerPath(int v, vector<vector<Edge>>& g) {
    vector<int> result;
    stack<int> st;
    st.push(v);
    int n = g.size();

    while (!st.empty()) {
        int w = st.top();
        bool found_edge = false;

        for (Edge& e : g[w]) {
            if (e.id != -1) {
                st.push(e.to);
                for (Edge& rev : g[e.to]) {
                    if (rev.to == w && rev.id != -1) {
                        rev.id = -1;
                        break;
                    }
                }
                e.id = -1;
                found_edge = true;
                break;
            }
        }

        if (!found_edge) {
            result.push_back(w + 1);
            st.pop();
        }
    }

    reverse(result.begin(), result.end());
    for (auto i : result) {
        cout << i << " ";
    }
}

int main() {
    int n, m;
    cin >> n >> m;

    vector<vector<Edge>> g(n);

    for (int i = 0; i < m; ++i) {
        int u, v;
        cin >> u >> v;
        u--;
        v--;
        g[u].push_back({v, i});
        g[v].push_back({u, i});
    }

    if (checkForEulerPath(g)) {
        findEulerPath(0, g);
    } else {
        cout << ":(";
    }

    return 0;
}
