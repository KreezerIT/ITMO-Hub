#include <iostream>
#include <string>
using namespace std;

int main() {
    int n,m,ans=0,i,j,k;
    string str;
    cin >> n >> m;
    cin >> str;
    for (int index=0; index<m; index++) {
        cin >> i >> j >> k;
        i--;
        j--;
        int count = 0;
        while (str[i] == str[j] && count < k) {
            count++;
            i++;
            j++;
        }
        if (count == k) {
            ans++;
        }
    }


    cout << ans;
    return 0;
}
