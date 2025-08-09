#include <iostream>
#include <string>
using namespace std;

class Queue {
private:
    struct Node {
        int data;
        Node* next;
        Node(int data) : data(data), next(nullptr) {}
    };
    Node* head;
    Node* tail;
public:
    Queue(): head(nullptr), tail(nullptr) {}
    ~Queue() {
        while (!isEmpty()) {dequeue();}
    }

    void enqueue(int data) {
        Node* newNode = new Node(data);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail->next = newNode;
            tail = newNode;
        }
    }
    void dequeue() {
        if (isEmpty()) {
            cout << "Queue is empty" << "\n";
            return;
        }
        Node* temp = head;
        head = head->next;
        delete temp;
        if (head == nullptr) {tail = nullptr;}
    }

    int front() {
        if (isEmpty()) {
            cout << "Queue is empty" << "\n";
            return -1;
        }
        return head->data;
    }

    bool isEmpty() {return head == nullptr;}
};

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    Queue queue;
    int n,x;
    string znak;
    cin >> n;
    for (int i = 0; i < n; i++) {
        cin >> znak;
        if (znak=="+") {
            cin >> x;
            queue.enqueue(x);
        }
        else {cout << queue.front() << "\n"; queue.dequeue();}
    }
    return 0;
}