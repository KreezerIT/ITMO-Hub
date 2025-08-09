#include <iostream>
using namespace std;

long long maxim=0;

struct Node {
    long long data;
    long long sym=0;
    long long borderMin=LLONG_MIN;
    long long borderMax=LLONG_MAX;
    bool shit=false;
    Node* left = nullptr;
    Node* right = nullptr;
    Node* parent = nullptr;

    Node(long long value) : data(value) {}
};

Node* moveLeft(Node* node, long long value) {
    if (!node->left) {
        Node* newNode = new Node(value);
        newNode->parent=node;
        node->left=newNode;
        newNode->borderMin=newNode->data;
        newNode->borderMax=newNode->data;
    }
    return node->left;
}

Node* moveRight(Node* node, long long value) {
    if (!node->right) {
        Node* newNode = new Node(value);
        newNode->parent=node;
        node->right=newNode;
        newNode->borderMin=newNode->data;
        newNode->borderMax=newNode->data;
    }
    return node->right;
}

Node* moveUp(Node* node,bool& done) {
    if (!node->parent) {
        done=true;
    }
    return node->parent;
}

void makingShit(Node* node) {
    if (!node) {
        return;
    }

    makingShit(node->left);
    makingShit(node->right);

    if ((node->left && node->left->shit) || (node->right && node->right->shit)) {
        node->shit = true;
    }
}


long long countingSym(Node* node) {
    if (!node) {
        return 0;
    }

    if (node->shit == true) {
        countingSym(node->left);
        countingSym(node->right);
    }
    else {
        node->sym = node->data + countingSym(node->left) + countingSym(node->right);
        maxim=max(maxim,node->sym);
    }

    return node->sym;
}


int main() {
    string element;
    cin >> element;
    bool done=false;
    Node* node = new Node(stoll(element));
    while (cin >> element) {
        if (element == "l") {
            cin >> element;

            node=moveLeft(node,stoll(element));


            if ((node->data < node->borderMin) || (node->data > node->borderMax)){
                node->parent->shit=true;
            }

        } else if (element == "r") {
            cin >> element;

            node=moveRight(node,stoll(element));

            if ((node->data < node->borderMin) || (node->data > node->borderMax)){
                node->parent->shit=true;
            }

        } else if (element == "u") {
            if (!node->parent){
                break;
            } else {
                node=moveUp(node,done);
                if ((node->left && node->data > node->left->borderMax && node->right && node->data < node->right->borderMin) ||
                    (!node->left && node->right && node->data < node->right->borderMin) ||
                    (node->left && node->data > node->left->borderMax && !node->right)) {
                    if (node->left && node->right) {
                        node->borderMin = min(node->borderMin, min(node->left->borderMin, node->right->borderMin));
                        node->borderMax = max(node->borderMax, min(node->left->borderMax, node->right->borderMax));
                    } else if (!node->left && node->right) {
                        node->borderMin = min(node->borderMin, node->right->borderMin);
                        node->borderMax = max(node->borderMax, node->right->borderMax);
                    } else if (node->left && !node->right) {
                        node->borderMin = min(node->borderMin, node->left->borderMin);
                        node->borderMax = max(node->borderMax, node->left->borderMax);
                    }
                } else {
                    node->shit=true;
                }
            }
        }
    }

    makingShit(node);

    countingSym(node);

    cout << maxim;
    return 0;
}
