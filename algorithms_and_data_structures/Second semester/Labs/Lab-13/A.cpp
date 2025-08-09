#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

int main() {
    vector<int> ans;
    string gimn,x;
    cin >> gimn >> x;
    for (int i=0; i<gimn.size(); i++){
        int j = 0;
        while (j < x.size() && gimn[i + j] == x[j]) {
            j++;
        }
        if (j == x.size()) {
            ans.push_back(i);
        }
    }

    cout << ans.size() << "\n";
    sort(ans.begin(),ans.end());
    for (int i=0; i<ans.size();i++){
        cout << ans[i] << " ";
    }


    return 0;
}
