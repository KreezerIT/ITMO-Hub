#include <iostream>
#include <string>
using namespace std;

class Stack {
private:
    struct Node {
        int data;
        Node* next;
        Node(int data) : data(data), next(nullptr) {}
    };
    Node* top;
public:
    Stack()
            : top(nullptr) {}
    ~Stack() {while (!isEmpty()) {pop();}}

    void push(int data) {
        Node* newNode = new Node(data);
        newNode->next = top;
        top = newNode;
    }

    void pop() {
        if (isEmpty()) {
            std::cout << "Stack is empty." << std::endl;
            return;
        }
        Node* temp = top;
        top = top->next;
        delete temp;
    }

    int peek() {
        if (isEmpty()) {
            cout << "Stack is empty" << "\n";
            return -1;
        }
        return top->data;
    }

    bool isEmpty() {return top == nullptr;}
};

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    Stack norm;
    int n,x;
    string znak;
    cin >> n;
    for (int i = 0; i < n; i++) {
        cin >> znak;
        if (znak=="+") {cin >> x; norm.push(x);}
        else if (znak=="-"){
            if (!norm.isEmpty()) {
                cout << norm.peek() << "\n";
                norm.pop();
            }
        }
    }
    return 0;
}