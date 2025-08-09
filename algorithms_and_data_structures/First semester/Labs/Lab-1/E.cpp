#include <iostream>
#include <string>
using namespace std;

int main(){
    int n,left,right,new_left,new_right;
    string str;
    cin >> n >> str;
    left=0,right=n-1;
    while (left<right && str[left]==str[right]){
        left++;
        right--;
    }
    if (left>=right){cout << "YES";return 0;}
    new_left=left;
    new_right=right-1;
    while (new_left<new_right && str[new_left]==str[new_right]){
        new_left++;
        new_right--;
    }
    if (new_left>=new_right){cout << "YES";return 0;}
    new_left=left+1;
    new_right=right;
    while (new_left<new_right && str[new_left]==str[new_right]){
        new_left++;
        new_right--;
    }
    if (new_left>=new_right){cout << "YES";return 0;}
    cout << "NO";
}