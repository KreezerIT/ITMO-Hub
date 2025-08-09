#include <iostream>
#include <vector>
#include <algorithm>
#include <set>
#define ull unsigned long long
using namespace std;

struct DSU {
    vector<ull> parent;

    DSU(ull n) {
        parent.resize(n + 1);
        for (ull i = 1; i <= n; ++i)
            parent[i] = i;
    }

    ull find(ull v) {
        if (v == parent[v])
            return v;
        return parent[v] = find(parent[v]);
    }

    void unite(ull a, ull b) {
        a = find(a);
        b = find(b);
        if (a!=b){parent[a]=b;}
    }
};

int main() {
    ull n, m, q;
    cin >> n >> m >> q;
    DSU dsu(n);
    vector<pair<ull, ull>> edges(m);
    set<pair<ull,ull>> deleted;
    for (ull i = 0; i < m; ++i) {
        cin >> edges[i].first >> edges[i].second;
        if (edges[i].first < edges[i].second) {
            swap(edges[i].first, edges[i].second);
        }
    }


    vector<pair<char, pair<ull, ull>>> queries(q);
    for (ull i = 0; i < q; ++i) {
        cin >> queries[i].first >> queries[i].second.first >> queries[i].second.second;
        if (queries[i].second.first<queries[i].second.second) {swap(queries[i].second.first,queries[i].second.second);}
        if (queries[i].first=='-'){
            deleted.insert({queries[i].second.first,queries[i].second.second});
        }
    }

    for(int i = 0 ; i < m ; i++){
        if(deleted.find(edges[i]) != deleted.end()){
            edges[i] = {-1,-1};
        }
    }

    for (int i = 0; i < edges.size(); ++i) {
        if(edges[i].first != -1 && edges[i].second != -1){
            dsu.unite(edges[i].first - 1, edges[i].second - 1);
        }
    }

    reverse(queries.begin(), queries.end());


    vector<string> answers;
    for (auto &query : queries) {
        if (query.first == '?') {
            if (dsu.find(query.second.first-1) == dsu.find(query.second.second-1))
                answers.push_back("YES");
            else
                answers.push_back("NO");
        } else {
            dsu.unite(query.second.first-1, query.second.second-1);
        }
    }

    reverse(answers.begin(), answers.end());

    for (auto &answer : answers)
        cout << answer << "\n";

    return 0;
}
