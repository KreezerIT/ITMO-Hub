#include <iostream>
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
    Stack(): top(nullptr) {}
    ~Stack() {
        while (!isEmpty()) {pop();}
    }

    void push(int data) {
        Node* newNode = new Node(data);
        newNode->next = top;
        top = newNode;
    }

    void pop() {
        if (isEmpty()) {
            cout << "Stack is empty" << "\n";
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
    long long N;
    cin >> N;

    long long* lengths = new long long[N];
    for (long long i = 0; i < N; ++i) {cin >> lengths[i];}

    Stack s;
    long long max_area = 0;
    long long i = 0;

    while (i < N) {
        if (s.isEmpty() || lengths[i] >= lengths[s.peek()]) {
            s.push(i);
            ++i;
        } else {
            long long tp = s.peek();
            s.pop();
            long long width = s.isEmpty() ? i : i - s.peek() - 1;
            max_area = max(max_area, lengths[tp] * width);
        }
    }

    while (!s.isEmpty()) {
        long long tp = s.peek();
        s.pop();
        long long width = s.isEmpty() ? i : i - s.peek() - 1;
        max_area = max(max_area, lengths[tp] * width);
    }

    cout << max_area;
    delete[] lengths;
    return 0;
}
