#include <iostream>
#include <list>
#include <map>
using namespace std;

struct Node {
    int pos;
    char next;
};

list<Node> encodeLZ78(const string& s) {
    string buffer = "";
    map<string, int> dict;
    list<Node> ans;

    for (int i = 0; i < s.length(); ++i) {
        if (dict.find(buffer + s[i]) != dict.end()) {
            buffer += s[i];
        } else {
            ans.push_back({dict[buffer], s[i]});
            dict[buffer + s[i]] = dict.size();
            buffer = "";
        }
    }

    if (!buffer.empty()) {
        ans.push_back({dict[buffer], 0});
    }

    return ans;
}

int main() {
    string input;
    cin >> input;

    list<Node> result = encodeLZ78(input);

    for (auto it = result.begin(); it != result.end(); it++) {
        Node& node = *it;
        cout << node.pos << " " << node.next << "\n";
    }


    return 0;
}
