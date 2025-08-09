#include <iostream>
#include <string>
using namespace std;

int main(){
    int t,n,mn;
    string znak;
    cin >> t;
    for (int i=0;i<t;i++){
        cin >> n;
        int max=15,min=31;
        for (int j=0;j<n;j++){
            cin >> znak >> mn;
            if (znak==">="){
                if (mn>max) {max=mn;}
            }
            else {
                if (mn<min){min=mn;}
            }
            if (max<=min){cout << max << "\n";}
            else {cout << "-1" << "\n";}
        }
    }
}