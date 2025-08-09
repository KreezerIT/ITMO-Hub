#include <iostream>
using namespace std;

struct Node {
    int key;
    int size;
    Node* left;
    Node* right;

    Node(int k) : key(k), size(1), left(nullptr), right(nullptr) {}
};

Node* insert(Node* node, int key) {
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

Node* remove(Node* node, int key) {
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

    node->size = 1 + (node->left ? node->left->size : 0) + (node->right ? node->right->size : 0);
    return node;
}

Node* kthMax(Node* node, int k) {
    if (!node || k <= 0 || k > node->size) {
        return nullptr;
    }

    int rightSize = node->right ? node->right->size : 0;

    if (k == rightSize + 1) {
        return node;
    } else if (k <= rightSize) {
        return kthMax(node->right, k);
    } else {
        return kthMax(node->left, k - rightSize - 1);
    }
}

int main() {
    int n;
    cin >> n;

    Node* node = nullptr;

    for (int i = 0; i < n; ++i) {
        int command, k;
        cin >> command >> k;

        if (command == 1) {
            node = insert(node, k);
        } else if (command == 0) {
            Node* kthMaxNode = kthMax(node, k);
            cout << kthMaxNode->key << "\n";
        } else if (command == -1) {
            node = remove(node, k);
        }
    }

    return 0;
}
