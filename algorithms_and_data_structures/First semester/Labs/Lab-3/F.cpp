#include <iostream>
using namespace std;

class Stack {
private:
    struct Node {
        long long data;
        Node* next;
        Node(long long data) : data(data), next(nullptr) {}
    };
    Node* top;
public:
    Stack(): top(nullptr) {}
    ~Stack() {
        while (!isEmpty()) {pop();}
    }

    void push(long long data) {
        Node* newNode = new Node(data);
        newNode->next = top;
        top = newNode;
    }

    void pop() {
        if (isEmpty()) {
            cout << "Stack is empty." << "\n";
            return;
        }
        Node* temp = top;
        top = top->next;
        delete temp;
    }

    long long peek() {
        if (isEmpty()) {
            cout << "Stack is empty." << "\n";
            return -1;
        }
        return top->data;
    }

    bool isEmpty() {return top == nullptr;}
};

const long long MAX_DAYS = 1000000;
int main() {
    int n;
    cin >> n;
    long long difficulties[MAX_DAYS];
    for (int i = 0; i < n; ++i) {cin >> difficulties[i];}

    long long result[MAX_DAYS];
    for (int i = 0; i < n; ++i) {result[i]=-1;}

    Stack zadachi;
    for (int i = 0; i < n; ++i) {
        while (!zadachi.isEmpty() && difficulties[i] > difficulties[zadachi.peek()]) {
            result[zadachi.peek()] = i - zadachi.peek();
            zadachi.pop();
        }
        zadachi.push(i);
    }

    for (int i = 0; i < n; ++i) {cout << result[i] << " ";}
    return 0;
}
