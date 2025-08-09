#include <iostream>
#include <vector>

using namespace std;

class PriorityQueue {
private:
    vector<int> heap;

    int parent(int i) { return (i - 1) / 2; }
    int leftChild(int i) { return 2 * i + 1; }
    int rightChild(int i) { return 2 * i + 2; }

    void siftUp(int i) {
        while (heap[i]<heap[parent(i)]) {
            swap(heap[i], heap[parent(i)]);
            i = parent(i);
        }
    }

    void siftDown(int i) {
        while (leftChild(i)<heap.size()){
            int j= leftChild(i);
            if (rightChild(i)<heap.size() && heap[rightChild(i)]<heap[leftChild(i)]){
                j= rightChild(i);
            }
            if (heap[i]<=heap[j]){
                break;
            }
            swap(heap[i],heap[j]);
            i=j;
        }
    }

public:
    void insert(int key) {
        heap.push_back(key);
        siftUp(heap.size() - 1);
    }

    int extractMin() {
        if (heap.empty()) {
            return -100;
        }

        int minElement = heap[0];
        heap[0] = heap.back();
        heap.pop_back();
        siftDown(0);
        return minElement;
    }
    int Min() {
        if (heap.empty()) {
            return -100;
        }

        int minElement = heap[0];
        heap[0] = heap.back();
        return minElement;
    }

    void decreaseKey(int x, int y) {
        for (int i = 0; i < heap.size(); ++i) {
            if (heap[i] == x) {
                heap[i] = y;
                siftUp(i);
                return;
            }
        }
    }
    int isEmpty() const {
        if (heap.empty()){
            return 1;
        }
        else {return -100;}
    }
};

struct Operation {
    string type;
    int k, x, m, y;
};

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    vector<PriorityQueue> queues;
    vector<Operation> operations;

    string operationType;
    while (cin >> operationType) {
        if (operationType == "end") {
            break;
        }
        Operation op;
        op.type = operationType;

        if (operationType == "create") {
        } else if (operationType == "insert") {
            cin >> op.k >> op.x;
        } else if (operationType == "extract-min") {
            cin >> op.k;
        } else if (operationType == "merge") {
            cin >> op.k >> op.m;
        } else if (operationType == "decrease-key") {
            cin >> op.k >> op.x >> op.y;
        }

        operations.push_back(op);
    }

    for (int i=0; i<operations.size();i++) {
        Operation op= operations[i];
        if (op.type == "create") {
            queues.emplace_back();
        } else if (op.type == "insert") {
            queues[op.k].insert(op.x);
        } else if (op.type == "extract-min") {
            int result;
            if (queues[op.k].isEmpty()==1){result=-100;}
            else{result=queues[op.k].extractMin();}
            if (result == -100) {
                cout << "*\n";
            } else {
                cout << result << "\n";
            }
        } else if (op.type == "merge") {
            PriorityQueue mergedQueue = queues[op.k];
            PriorityQueue queue_m = queues[op.m];
            while (queue_m.isEmpty()==-100) {
                mergedQueue.insert(queue_m.Min());
                queue_m.extractMin();
            }
            queues.emplace_back(mergedQueue);
        } else if (op.type == "decrease-key") {
            queues[op.k].decreaseKey(op.x, op.y);
        }
    }

    return 0;
}
