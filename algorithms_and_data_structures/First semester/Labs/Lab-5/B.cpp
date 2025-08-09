#include <iostream>
#include <string>
using namespace std;

struct Node {
    int age;
    Node* left;
    Node* right;
    Node(int x) : age(x), left(nullptr), right(nullptr) {}
};

class BinaryTree {
public:
    BinaryTree() : root(nullptr) {}

    void insert(int x) {
        root = insertRecursive(root, x);
    }

    void remove(int x) {
        root = removeRecursive(root, x);
    }

    bool exists(int x) {
        return existsRecursive(root, x);
    }

    string next(int x) {
        Node* result = nextRecursive(root, x);
        return (result != nullptr) ? to_string(result->age) : "none";
    }

    string prev(int x) {
        Node* result = prevRecursive(root, x);
        return (result != nullptr) ? to_string(result->age) : "none";
    }

private:
    Node* root;

    Node* insertRecursive(Node* node, int x) {
        if (node == nullptr) {
            return new Node(x);
        }

        if (x < node->age) {
            node->left = insertRecursive(node->left, x);
        } else if (x > node->age) {
            node->right = insertRecursive(node->right, x);
        }

        return node;
    }

    Node* removeRecursive(Node* node, int x) {
        if (node == nullptr) {
            return nullptr;
        }

        if (x < node->age) {
            node->left = removeRecursive(node->left, x);
        } else if (x > node->age) {
            node->right = removeRecursive(node->right, x);
        } else {
            if (node->left == nullptr) {
                Node* temp = node->right;
                delete node;
                return temp;
            } else if (node->right == nullptr) {
                Node* temp = node->left;
                delete node;
                return temp;
            }

            Node* temp = findMin(node->right);
            node->age = temp->age;
            node->right = removeRecursive(node->right, temp->age);
        }

        return node;
    }

    bool existsRecursive(Node* node, int x) {
        if (node == nullptr) {
            return false;
        }

        if (x == node->age) {
            return true;
        } else if (x < node->age) {
            return existsRecursive(node->left, x);
        } else {
            return existsRecursive(node->right, x);
        }
    }

    Node* nextRecursive(Node* node, int x) {
        Node* result = nullptr;
        while (node != nullptr) {
            if (node->age > x) {
                result = node;
                node = node->left;
            } else {
                node = node->right;
            }
        }
        return result;
    }

    Node* prevRecursive(Node* node, int x) {
        Node* result = nullptr;
        while (node != nullptr) {
            if (node->age < x) {
                result = node;
                node = node->right;
            } else {
                node = node->left;
            }
        }
        return result;
    }

    Node* findMin(Node* node) {
        while (node->left != nullptr) {
            node = node->left;
        }
        return node;
    }
};

int main() {
    BinaryTree tree;

    string operation;
    int x;
    while (cin >> operation >> x) {
        if (operation == "insert") {
            tree.insert(x);
        } else if (operation == "delete") {
            tree.remove(x);
        } else if (operation == "exists") {
            cout << (tree.exists(x) ? "true" : "false") << "\n";
        } else if (operation == "next") {
            cout << tree.next(x) << "\n";
        } else if (operation == "prev") {
            cout << tree.prev(x) << "\n";
        }
    }

    return 0;
}
