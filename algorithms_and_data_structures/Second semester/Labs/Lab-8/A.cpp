#include <iostream>
#include <vector>
#include <stack>
using namespace std;

vector<int> topologicalSort(vector<vector<int>>& smej, vector<int>& vxodov, int n) {
    vector<int> zakaz;
    stack<int> stack;

    for (int i = 0; i < n; i++) {
        if (vxodov[i] == 0) {
            stack.push(i);
        }
    }

    while (!stack.empty()) {
        int u = stack.top();
        stack.pop();
        zakaz.push_back(u);

        for (int v : smej[u]) {
            vxodov[v]--;
            if (vxodov[v] == 0) {
                stack.push(v);
            }
        }
    }

    return zakaz;
}

int main() {
    int n, m;
    cin >> n >> m;
    vector<int> vxodov(n, 0);
    vector<vector<int>> smej(n);

    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        u-=1; v-=1;
        smej[u].push_back(v);
        vxodov[v]++;
    }

    vector<int> zakaz = topologicalSort(smej, vxodov, n);
    vector<int> ans(n);

    for (int i = 0; i < n; i++) {ans[zakaz[i]] = i + 1;}

    for (int i = 0; i < n; i++) {cout << ans[i] << endl;}

    return 0;
}
