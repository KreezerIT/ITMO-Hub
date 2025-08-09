#include <iostream>
#include <vector>
#define inf ULLONG_MAX
#define ull unsigned long long
using namespace std;

void floydWarshall(vector<vector<ull>>& distance) {
    ull n = distance.size();
    for (ull k = 1; k < n; k++) {
        for (ull i = 1; i < n; i++) {
            for (ull j = 1; j < n; j++) {
                if (distance[i][k]!=inf && distance[k][j]!=inf) {
                    if (distance[i][k] + distance[k][j] < distance[i][j]) {
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }
                }
            }
        }
    }
}

ull findCenter(vector<vector<ull>>& distance) {
    ull n = distance.size();
    ull minMaxDist = inf;
    ull center = -1;

    for (ull i = 1; i < n; i++) {
        ull maxDist = 0;
        for (ull j = 1; j < n; j++) {
            if (distance[i][j] != inf) {
                maxDist += distance[i][j];
            }
        }
        if (maxDist < minMaxDist) {
            minMaxDist = maxDist;
            center = i;
        }
    }

    return center;
}

int main() {
    ull n, m;
    cin >> n >> m;

    vector<vector<ull>> distance(n+1, vector<ull>(n+1, inf));
    for (ull i = 1; i <= n; i++) {
        distance[i][i] = 0;
    }

    for (ull i = 0; i < m; i++) {
        ull a, b, w;
        cin >> a >> b >> w;
        if ((a!=b) && ((distance[a][b]==inf || distance[a][b]>w))) {
            distance[a][b] = w;
            distance[b][a] = w;
        }
    }

    floydWarshall(distance);

    ull center = findCenter(distance);

    cout << center;

    return 0;
}