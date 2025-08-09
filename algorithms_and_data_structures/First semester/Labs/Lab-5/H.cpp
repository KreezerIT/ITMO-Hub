#include <iostream>
using namespace std;

struct Node {
    int data;
    Node* left=nullptr;
    Node* right=nullptr;

    Node(int data) : data(data) {}
};

Node* insert(Node* node, int data) {
    if (node == nullptr) {
        return new Node(data);
    }

    if (data <= node->data) {
        node->left = insert(node->left, data);
    } else if (data >= node->data) {
        node->right = insert(node->right, data);
    }

    return node;
}

Node* findMin(Node* node) {
    while (node->left) {
        node = node->left;
    }
    return node;
}

Node* remove(Node* node, int data) {
    if (!node) {
        return node;
    }

    if (data < node->data) {
        node->left = remove(node->left, data);
    } else if (data > node->data) {
        node->right = remove(node->right, data);
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

        Node* successor = findMin(node->right);
        node->data = successor->data;
        node->right = remove(node->right, successor->data);
    }

    return node;
}

void mergeTrees(Node* &mainNode, Node* additionalNode) {
    if (additionalNode == nullptr) {
        return;
    }

    mergeTrees(mainNode, additionalNode->left);
    mainNode = insert(mainNode, additionalNode->data);
    mergeTrees(mainNode, additionalNode->right);

    delete additionalNode;
}

void printSorted(Node* node, bool& done) {
    if (node != nullptr) {
        printSorted(node->left, done);
        if (done) {
            cout << " ";
        }
        cout << node->data;
        done=true;
        printSorted(node->right, done);
    }
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);

    Node* mainNode = nullptr;
    Node* additionalNode = nullptr;
    int n;
    cin >> n;
    string operation;

    for (int i = 0; i < n; ++i) {
        cin >> operation;

        if (operation == "buy") {
            int Acc, ID;
            cin >> Acc >> ID;
            if (Acc == 1) {
                mainNode = insert(mainNode, ID);
            } else if (Acc == 2) {
                additionalNode = insert(additionalNode, ID);
            }
        } else if (operation == "sell") {
            int Acc, ID;
            cin >> Acc >> ID;
            if (Acc == 1) {
                mainNode = remove(mainNode, ID);
            } else if (Acc == 2) {
                additionalNode = remove(additionalNode, ID);
            }
        } else if (operation == "merge") {
            if (mainNode!=nullptr || additionalNode!=nullptr) {
                mergeTrees(mainNode, additionalNode);
                additionalNode = nullptr;

                bool done = false;
                printSorted(mainNode, done);
                cout << "\n";
            }
        }
    }

    return 0;
}