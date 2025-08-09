#include <iostream>
#include <string>
#include <vector>
using namespace std;

struct Node {
    string key;
    int size;
    Node* left;
    Node* right;

    Node(const string& k) : key(k), size(1), left(nullptr), right(nullptr) {}
};

Node* insert(Node* node, const string& key) {
    if (!node) {
        return new Node(key);
    }

    if (key < node->key) {
        node->left = insert(node->left, key);
    } else {
        node->right = insert(node->right, key);
    }

    node->size = 1 + (node->left ? node->left->size : 0) + (node->right ? node->right->size : 0);
    return node;
}

Node* findMin(Node* node) {
    while (node->left) {
        node = node->left;
    }
    return node;
}

Node* remove(Node* node, const string& key) {
    if (!node) {
        return node;
    }

    if (key < node->key) {
        node->left = remove(node->left, key);
    } else if (key > node->key) {
        node->right = remove(node->right, key);
    } else {
        if (!node->left) {
            Node* temp = node->right;
            delete node;
            return temp;
        } else if (!node->right) {
            Node* temp = node->left;
            delete node;
            return temp;
        }

        Node* temp = findMin(node->right);
        node->key = temp->key;
        node->right = remove(node->right, temp->key);
    }

    if (node) {
        node->size = 1 + (node->left ? node->left->size : 0) + (node->right ? node->right->size : 0);
    }

    return node;
}

Node* kthMin(Node* node, int k) {
    if (!node || k <= 0 || k > node->size) {
        return nullptr;
    }

    int leftSize = node->left ? node->left->size : 0;

    if (k == leftSize + 1) {
        return node;
    } else if (k <= leftSize) {
        return kthMin(node->left, k);
    } else {
        return kthMin(node->right, k - leftSize - 1);
    }
}

int main() {
    int n;
    cin >> n;
    Node* root = nullptr;


    for (int i = 0; i < n; ++i) {
        string str;
        cin >> str;

        if (isdigit(str[0])) {
            Node* kthMinNode = kthMin(root, stoi(str));
            cout << kthMinNode->key << "\n";
        } else {
            root = insert(root, str);
        }
    }

    return 0;
}
