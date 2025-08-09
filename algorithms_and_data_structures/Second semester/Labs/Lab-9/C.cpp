#include <iostream>
#include <vector>
#define inf 10e5
using namespace std;

struct rebro{
    int to,weight;
};

void addEdge(vector<vector<rebro>>& adj, int v, rebro w) {
    adj[v].push_back(w);
}


vector<int> deikstra(vector<vector<rebro>>& adj, int start){
    vector<int> distance(adj.size()+1,inf);
    distance[start]=0;
    vector<bool> visited(adj.size()+1, false);

    for (int i=1; i<adj.size();i++){
        int nearest=-1;
        for (int v=0; v<adj.size();v++){
            if (!visited[v] && (nearest==-1 || distance[nearest] > distance[v])){
                nearest=v;
            }
        }

        visited[nearest]=true;

        for (auto &[b,weight] : adj[nearest]){
            if (distance[b]>distance[nearest] + weight){
                distance[b] = distance[nearest] + weight;
            }
        }
    }
    return distance;
}

int main() {
    int n, m, finish;
    cin >> n >> m;
    vector<vector<rebro>> adj(n+1);
    finish=n;

    for (int i = 0; i < m; i++) {
        int a;
        rebro b;
        cin >> a >> b.to >> b.weight;
        addEdge(adj, a , b);

    }

    vector<int> dist = deikstra(adj,1);
    if (dist[finish]!=inf){
        cout << dist[finish];
    }
    else {
        cout << "-1";
    }

    return 0;
}
