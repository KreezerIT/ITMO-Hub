#include <iostream>
#include <vector>
using namespace std;

bool dfs(vector<vector<pair<int, int>>>& graph, int istok, int stok, vector<bool>& visited, vector<vector<int>>& potokMatrix) {
    visited[istok] = true;

    if (istok == stok)
        return true;

    for (const auto& edge : graph[istok]) {
        int b = edge.first;
        int weight = edge.second;
        if (!visited[b] && weight > potokMatrix[istok][b]) {
            if (dfs(graph, b, stok, visited, potokMatrix)) {
                potokMatrix[istok][b]++;
                potokMatrix[b][istok]--;
                return true;
            }
        }
    }

    return false;
}

int fordFulkerson(int istok, int stok, vector<vector<pair<int, int>>>& graph, vector<vector<int>>& potokMatrix) {
    int maxPotok = 0;

    while (true) {
        vector<bool> visited(graph.size(), false);
        if (!dfs(graph, istok, stok, visited, potokMatrix))
            break;

        fill(visited.begin(), visited.end(), false);
        maxPotok++;
    }

    return maxPotok;
}

int main(){
    int N,M;
    cin >> N >> M;
    vector<vector<pair<int,int>>> graph(N);

    for (int i=0;i<M;i++){
        int a,b,weight;
        cin >> a >> b >> weight;
        a--;
        b--;
        graph[a].push_back({b,weight});
        graph[b].push_back({a,0});
    }

    vector<vector<int>> potokMatrix(N, vector<int>(N, 0));
    int ans = fordFulkerson(0, N-1, graph, potokMatrix);

    cout << ans;
    return 0;
}
