#include <iostream>
#include <vector>
#include <queue>
#define INF INT_MAX
using namespace std;

struct Edge {
    int to, capacity, cost, flow, reverse_edge;
};

void add_edge(vector<vector<Edge>>& graph, int u, int v, int capacity, int cost) {
    graph[u].push_back({v, capacity, cost, 0, static_cast<int>(graph[v].size())});
    graph[v].push_back({u, 0, -cost, 0, static_cast<int>(graph[u].size()) - 1});
}

void initialize_distance_and_parent_vectors(vector<int>& distance, vector<int>& parent, vector<int>& edge_index, vector<bool>& in_queue, int n) {
    distance.assign(n, INF);
    parent.assign(n, -1);
    edge_index.assign(n, -1);
    in_queue.assign(n, false);
}

void shortest_path_bellman_ford(vector<vector<Edge>>& graph, vector<int>& distance, vector<int>& parent, vector<int>& edge_index, vector<bool>& in_queue, int s, int n) {
    distance[s] = 0;
    queue<int> q;
    q.push(s);
    in_queue[s] = true;

    while (!q.empty()) {
        int u = q.front();
        q.pop();
        in_queue[u] = false;

        for (int i = 0; i < graph[u].size(); ++i) {
            Edge& edge = graph[u][i];
            if (edge.capacity > edge.flow && distance[edge.to] > distance[u] + edge.cost) {
                distance[edge.to] = distance[u] + edge.cost;
                parent[edge.to] = u;
                edge_index[edge.to] = i;
                if (!in_queue[edge.to]) {
                    q.push(edge.to);
                    in_queue[edge.to] = true;
                }
            }
        }
    }
}

pair<int, int> find_min_cost_max_flow(vector<vector<Edge>>& graph, int s, int t, int n) {
    int max_flow = 0, min_cost = 0;

    while (true) {
        vector<int> distance, parent, edge_index;
        vector<bool> in_queue;

        initialize_distance_and_parent_vectors(distance, parent, edge_index, in_queue, n);

        shortest_path_bellman_ford(graph, distance, parent, edge_index, in_queue, s, n);

        if (parent[t] == -1) break;

        int flow = INF;
        for (int u = t; u != s; u = parent[u]) {
            flow = min(flow, graph[parent[u]][edge_index[u]].capacity - graph[parent[u]][edge_index[u]].flow);
        }

        for (int u = t; u != s; u = parent[u]) {
            int reverse_edge = edge_index[u];
            graph[parent[u]][reverse_edge].flow += flow;
            graph[u][graph[parent[u]][reverse_edge].reverse_edge].flow -= flow;
            min_cost += flow * graph[parent[u]][reverse_edge].cost;
        }
        max_flow += flow;
    }
    return {max_flow, min_cost};
}


int main() {
    int n, m, s, t;
    cin >> n >> m >> s >> t;
    vector<vector<Edge>> graph(n);

    for (int i = 0; i < m; ++i) {
        int u, v, c, p;
        cin >> u >> v >> c >> p;
        u--; v--;
        add_edge(graph, u, v, c, p);
    }

    pair<int,int> result = find_min_cost_max_flow(graph, s - 1, t - 1, n);
    cout << result.first << " " << result.second << "\n";

    return 0;
}
