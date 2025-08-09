#include <iostream>
#include <queue>
using namespace std;

struct Node {
    int data;
    Node* left=nullptr;
    Node* right=nullptr;

    Node(int value) : data(value) {}
};

Node* insert(Node* node, int value) {
    if (node == nullptr) {
        return new Node(value);
    }

    if (value < node->data) {
        node->left = insert(node->left, value);
    } else if (value > node->data) {
        node->right = insert(node->right, value);
    } else {}

    return node;
}

void rightMostValues(Node* node) {
    if (node == nullptr) {
        return;
    }

    queue<Node*> q;
    q.push(node);

    while (!q.empty()) {
        int size = q.size();
        int rightMostValue = 0;

        for (int i = 0; i < size; ++i) {
            Node* current = q.front();
            q.pop();

            rightMostValue = current->data;

            if (current->left) {
                q.push(current->left);
            }
            if (current->right) {
                q.push(current->right);
            }
        }

        cout << rightMostValue << " ";
    }

}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    int n;
    cin >> n;
    Node* node = nullptr;
    for (int i = 0; i < n; ++i) {int value; cin >> value; node = insert(node, value);}

    rightMostValues(node);

    return 0;
}