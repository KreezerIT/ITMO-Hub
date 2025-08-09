#include <iostream>
#include <vector>
#include <deque>
#include <algorithm>
using namespace std;

int main() {
    int n, m;
    cin >> n >> m;
    vector<vector<bool>> graph(n, vector<bool>(n, false));
    deque<int> cur;
    vector<bool> in_cur(n,false);

    for (int i = 0; i < m; i++) {
        int a, b;
        cin >> a >> b;
        graph[a][b] = true;
        graph[b][a] = true;

        if (!in_cur[a]){
            cur.push_back(a);
            in_cur[a]=true;
        }
        if (!in_cur[b]){
            cur.push_back(b);
            in_cur[b]=true;
        }
    }


    for (int i = 0; i < n; ++i) {
        if (!graph[cur[0]][cur[1]]) {
            int k = 2;
            while (k < n && (!graph[cur[0]][cur[k]] || !graph[cur[1]][cur[k + 1]]))
                k++;
            reverse(cur.begin() + 1, cur.begin() + k);
        }
        cur.push_back(cur.front());
        cur.pop_front();
    }

    for (int i=0; i<cur.size(); ++i)
        cout << cur[i] << " ";

    return 0;
}
