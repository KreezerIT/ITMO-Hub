#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
using namespace std;

void dfs(int house, vector<int> &disc, vector<int> &low, stack<int> &st, vector<bool> &stackMember, vector<vector<int>> &adjList, vector<vector<int>> &SCCs, vector<int> &houseToFamily) {
    static int time = 0;
    disc[house] = low[house] = ++time;
    st.push(house);
    stackMember[house] = true;

    for (int neighbor : adjList[house]) {
        if (disc[neighbor] == -1) {
            dfs(neighbor, disc, low, st, stackMember, adjList, SCCs, houseToFamily);
            low[house] = min(low[house], low[neighbor]);
        } else if (stackMember[neighbor]) {
            low[house] = min(low[house], disc[neighbor]);
        }
    }

    int w;
    if (low[house] == disc[house]) {
        vector<int> component;
        while (st.top() != house) {
            w = st.top();
            component.push_back(w);
            stackMember[w] = false;
            houseToFamily[w] = SCCs.size() + 1;
            st.pop();
        }
        w = st.top();
        component.push_back(w);
        stackMember[w] = false;
        houseToFamily[w] = SCCs.size() + 1;
        st.pop();
        SCCs.push_back(component);
    }
}

void findFamilies(int n, vector<vector<int>> &adjList) {
    vector<int> disc(n + 1, -1), low(n + 1, -1);
    stack<int> st;
    vector<vector<int>> SCCs;
    vector<int> houseToFamily(n + 1, -1);
    vector<bool> stackMember(n + 1, false);

    for (int house = 1; house <= n; ++house)
        if (disc[house] == -1)
            dfs(house, disc, low, st, stackMember, adjList, SCCs, houseToFamily);

    vector<int> familyMinHouse(SCCs.size() + 1, n + 1);
    for (int i = 1; i <= n; ++i)
        if (houseToFamily[i] != -1 && i < familyMinHouse[houseToFamily[i]])
            familyMinHouse[houseToFamily[i]] = i;

    vector<pair<int, int>> roadsBetweenFamilies;
    for (int i = 1; i <= n; ++i)
        for (int neighbor : adjList[i]) {
            int family1 = houseToFamily[i], family2 = houseToFamily[neighbor];
            if (family1 != family2)
                roadsBetweenFamilies.push_back({familyMinHouse[family1], familyMinHouse[family2]});
        }

    sort(roadsBetweenFamilies.begin(), roadsBetweenFamilies.end());
    roadsBetweenFamilies.erase(unique(roadsBetweenFamilies.begin(), roadsBetweenFamilies.end()), roadsBetweenFamilies.end());

    cout << SCCs.size() << " " << roadsBetweenFamilies.size() << "\n";
    for (auto road : roadsBetweenFamilies)
        cout << road.first << " " << road.second << "\n";

}

int main() {
    int n, m;
    cin >> n >> m;
    vector<vector<int>> adjList(n + 1);

    for (int i = 0, a, b; i < m && cin >> a >> b; adjList[a].push_back(b), ++i);

    findFamilies(n, adjList);

    return 0;
}
