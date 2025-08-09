#include <iostream>
#include <queue>
using namespace std;

struct Node {
    char data;
    int freq;
    Node* left;
    Node* right;

    Node(char c, int f) : data(c), freq(f), left(nullptr), right(nullptr) {}
};

struct Compare {
    bool operator()(Node* lhs, Node* rhs) {
        return lhs->freq > rhs->freq;
    }
};

void generateCodes(Node* root, string codes[], string currentCode = "") {
    if (root) {
        if (!root->left && !root->right) {
            codes[root->data] = currentCode;
        }
        generateCodes(root->left, codes, currentCode + "0");
        generateCodes(root->right, codes, currentCode + "1");
    }
}

int HuffmanCoding(const string& input) {
    int freq[256] = {0};

    for (char c : input) {
        freq[static_cast<char>(c)]++;
    }

    priority_queue<Node*, vector<Node*>, Compare> pq;
    for (int i = 0; i < 256; ++i) {
        if (freq[i] > 0) {
            pq.push(new Node(static_cast<char>(i), freq[i]));
        }
    }

    while (pq.size() > 1) {
        Node* left = pq.top();
        pq.pop();
        Node* right = pq.top();
        pq.pop();

        Node* internalNode = new Node('\0', left->freq + right->freq);
        internalNode->left = left;
        internalNode->right = right;

        pq.push(internalNode);
    }

    Node* root = pq.top();
    string codes[256];
    generateCodes(root, codes);

    int ans = 0;
    for (char c : input) {
        ans += codes[static_cast<unsigned char>(c)].size();
    }

    delete root;
    return ans;
}

int main() {
    string input;
    cin>>input;
    int result = HuffmanCoding(input);

    cout << result;

    return 0;
}
