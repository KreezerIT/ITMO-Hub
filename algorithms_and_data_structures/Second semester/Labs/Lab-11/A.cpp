#include <iostream>
#include <vector>
#define MAXNM 100
#define INF INT_MAX
using namespace std;

vector<int> g[MAXNM];
int match[MAXNM];
bool used[MAXNM];

bool dfs(int v) {
    if (used[v]) return false;
    used[v] = true;
    for (int to : g[v]) {
        if (match[to] == -1 || dfs(match[to])) {
            match[to] = v;
            return true;
        }
    }
    return false;
}

vector<pair<int, int>> next(vector<vector<int>> &graph, int n, int m, int cury, int curx) {
    vector<pair<int, int>> ans;
    if (curx != 0 && graph[cury][curx - 1] != INF) ans.push_back({cury, curx - 1});
    if (cury != 0 && graph[cury - 1][curx] != INF) ans.push_back({cury - 1, curx});
    if (curx != m - 1 && graph[cury][curx + 1] != INF) ans.push_back({cury, curx + 1});
    if (cury != n - 1 && graph[cury + 1][curx] != INF) ans.push_back({cury + 1, curx});
    return ans;
}

bool dfs_kuhn(vector<vector<int>> &graph, vector<vector<int>> &used, vector<vector<pair<int, int>>> &matching, int n, int m, int cury, int curx) {
    if (used[cury][curx]) return false;
    used[cury][curx] = 1;

    for (auto &next_pair : next(graph, n, m, cury, curx)) {
        int next_i = next_pair.first;
        int next_j = next_pair.second;

        auto &matching_pair = matching[next_i][next_j];
        if (matching_pair == make_pair(-1, -1) || dfs_kuhn(graph, used, matching, n, m, matching_pair.first, matching_pair.second)) {
            matching_pair = {cury, curx};
            return true;
        }
    }

    return false;
}

int main() {
    int n, m, q;
    cin >> n >> m >> q;
    vector<vector<int>> matrix(n, vector<int>(m, 1));

    for (int i = 0; i < q; i++) {
        int a, b;
        cin >> a >> b;
        a--;
        b--;
        matrix[a][b] = INF;
    }

    vector<vector<pair<int, int>>> matching(n, vector<pair<int, int>>(m, {-1, -1}));
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            if (matrix[i][j] != INF) {
                vector<vector<int>> used(n, vector<int>(m, 0));
                dfs_kuhn(matrix, used, matching, n, m, i, j);
            }
        }
    }

    int ans = 0;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            if (matching[i][j] != make_pair(-1, -1)) ans++;
        }
    }

    cout << ans;
    return 0;
}
