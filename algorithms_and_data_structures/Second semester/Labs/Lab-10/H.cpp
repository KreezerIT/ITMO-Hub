#include <iostream>
#include <vector>
#include <queue>
#define inf INT_MAX
using namespace std;

struct Edge {
    int to;
    int time;
};

int dijkstra(vector<vector<Edge>>& graph, int start, int end) {
    vector<int> dist(graph.size(), inf);
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
    pq.push({0, start});
    dist[start] = 0;

    while (!pq.empty()) {
        int u = pq.top().second;
        int time = pq.top().first;
        pq.pop();

        if (time > dist[u]) continue;

        for (Edge& e : graph[u]) {
            int v = e.to;
            int new_dist = time + e.time;
            if (new_dist < dist[v]) {
                dist[v] = new_dist;
                pq.push({new_dist, v});
            }
        }
    }

    return dist[end];
}

int main() {
    int n, m, k;
    cin >> n >> m >> k;

    vector<vector<Edge>> graph(n + 1);
    for (int i = 0; i < m; ++i) {
        int s, t;
        cin >> s >> t;
        vector<int> islands(s);
        for (int j = 0; j < s; ++j) {
            cin >> islands[j];
        }
        for (int j = 0; j < s; ++j) {
            for (int l = j + 1; l < s; ++l) {
                graph[islands[j]].push_back({islands[l], t});
                graph[islands[l]].push_back({islands[j], t});
            }
        }
    }

    int min_time = dijkstra(graph, 1, k);
    if (min_time == inf) {
        cout << -1 << endl;
    } else {
        cout << min_time << endl;
    }

    return 0;
}
