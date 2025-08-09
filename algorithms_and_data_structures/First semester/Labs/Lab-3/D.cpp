#include <iostream>
#include <algorithm>
using namespace std;

class Node {
public:
    int data;
    Node* next;
    Node* prev;
    Node(int value) : data(value), next(nullptr), prev(nullptr) {}
};

class Queue {
private:
    Node* head;
    Node* tail;

public:
    Queue() : head(nullptr), tail(nullptr) {}
    ~Queue() {
        while (!isEmpty()) {
            dequeue();
        }
    }

    void enqueue(int value) {
        Node* newNode = new Node(value);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail->next= newNode;
            newNode->prev = tail;
            tail = newNode;
        }
    }

    void dequeue() {
        if (head==nullptr) {
            cout << "Queue is empty." << "\n";
            return;
        }

        Node* temp = head;
        head = head->next;
        if (head != nullptr) {
            head->prev = nullptr;
        } else {
            tail = nullptr;
        }
        delete temp;
    }

    void dequeueTail(){
        if (isEmpty()) {
            cout << "Queue is empty." << "\n";
            return;
        }
        Node* temp = tail;
        tail = tail->prev;
        if (tail !=nullptr){
            tail->next=nullptr;
        } else {
            head = nullptr;
        }
        delete temp;
    }

    int peekUp() const {
        if (isEmpty()) {
            cout << "Queue is empty." << "\n";
        }
        return head->data;
    }

    int peekDown() const {
        if (isEmpty()) {
            cout << "Queue is empty." << "\n";
        }
        return tail->data;
    }

    bool isEmpty() const {return head == nullptr;}

};

int main() {
    Queue sumka;
    int n,m,k,x,y,z,a,b,premax;
    cin >> n >> m;
    if (n>m) {
        for (int i=0;i<m;i++){cin >> k; sumka.enqueue(k);}
        for (int i = 0; i < (n - m); i++) {
            x = sumka.peekUp();
            y = sumka.peekDown();
            cin >> z;
            a=max({x,y,z});
            b=min({x,y,z});
            premax=(x+y+z)-a-b;

            sumka.dequeueTail();
            sumka.dequeue();
            sumka.enqueue(premax);
            sumka.enqueue(a);
        }
    }
    else{
        for (int i = 0; i < n; i++) {cin >> k; sumka.enqueue(k);}
    }
    while (!sumka.isEmpty()){cout << sumka.peekUp() << " "; sumka.dequeue();}
    return 0;
}