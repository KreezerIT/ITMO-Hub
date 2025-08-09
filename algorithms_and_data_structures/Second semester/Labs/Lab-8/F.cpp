#include <iostream>
#include <vector>
#include <queue>
using namespace std;

void addEdge(vector<vector<int>>& adj, int v, int w) {
    adj[v].push_back(w);
    adj[w].push_back(v);
}

void searchKeyAwayIslands(vector<vector<int>> &adj, const int start, const int k){
    vector<int> distances(adj.size(), -1);
    queue<int> q;
    q.push(start);
    distances[start] = 0;

    while (!q.empty()) {
        int current = q.front();
        q.pop();

        for (int i = 0; i < adj[current].size(); ++i) {
            int next = adj[current][i];
            if (distances[next] == -1) {
                distances[next] = distances[current] + 1;
                q.push(next);
            }
        }
    }

    bool flag=false;
    for (int i = 1; i < adj.size(); i++) {
        if (distances[i] == k) {
            cout << i << "\n";
            flag=true;
        }
    }
    if (flag==false){
        cout << "NO";
    }
}

int main() {
    int n, v, k;
    cin >> n >> v >> k;
    vector<vector<int>> adj(n+1);

    for (int i = 0; i < v; i++) {
        int a, b;
        cin >> a >> b;
        addEdge(adj, a , b);
    }

    searchKeyAwayIslands(adj, 1, k);

    return 0;
}
