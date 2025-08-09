#include <iostream>
#include <vector>
using namespace std;

struct Node {
    string data;
    int Using = 1;
    int Points = 0;
    Node* left = nullptr;
    Node* right = nullptr;

    Node(string name, int Using, int Points) : data(name), Using(Using), Points(Points) {}
};

Node* insert(Node* node, string name) {
    Node* newNode = new Node(name, 1, 3);
    Node* current = node;

    while (current != nullptr) {
        if (name < current->data) {
            if (current->right == nullptr) {
                current->right = newNode;
                return node;
            }
            current = current->right;
        } else if (name > current->data) {
            if (current->left == nullptr) {
                current->left = newNode;
                return node;
            }
            current = current->left;
        } else {
            current->Using += 1;
            if (current->Using == 3) {current->Points = 0;}
            else if (current->Using == 2) {current->Points = 1;}
            else if (current->Using == 1) {current->Points = 3;}

            delete newNode;
            return node;
        }
    }

    return newNode;
}

int CountingPoints(Node* node, vector<string>& taskNames, int& totalPoints, int& maxAdd, int& Adds) {
    Node* current = node;

    while (current != nullptr && Adds < maxAdd) {
        if (taskNames[Adds] < current->data) {
            current = current->right;
        } else if (taskNames[Adds] > current->data) {
            current = current->left;
        } else {
            totalPoints += current->Points;
            Adds += 1;
            current = node;
        }
    }

    return totalPoints;
}

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);

    int n;
    cin >> n;
    Node* node = nullptr;
    vector<vector<string>> taskNames(3);

    for (int i = 0; i < 3; i++) {
        taskNames[i].resize(n);
        for (int j = 0; j < n; j++) {
            cin >> taskNames[i][j];
            node = insert(node, taskNames[i][j]);
        }
    }

    for (int i = 0; i < 3; i++) {
        int totalPoints = 0;
        int Adds = 0;
        CountingPoints(node, taskNames[i], totalPoints, n, Adds);
        cout << totalPoints << " ";
    }

    return 0;
}
