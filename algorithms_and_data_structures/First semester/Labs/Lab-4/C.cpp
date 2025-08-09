#include <iostream>
#include <vector>
using namespace std;

struct shaurmist{
    int zp;
    int Tleft;
};

class PriorityQueue {
private:
    vector<shaurmist> heap;

    int parent(int i) { return (i - 1) / 2; }
    int leftChild(int i) { return 2 * i + 1; }
    int rightChild(int i) { return 2 * i + 2; }

    void siftUpZp(int i) {
        while (heap[i].zp<heap[parent(i)].zp) {
            swap(heap[i], heap[parent(i)]);
            i = parent(i);
        }
    }
    void siftUpTleft(int i) {
        while (heap[i].Tleft<heap[parent(i)].Tleft) {
            swap(heap[i], heap[parent(i)]);
            i = parent(i);
        }
    }

    void siftDownZp(int i) {
        while (leftChild(i)<heap.size()){
            int j= leftChild(i);
            if (rightChild(i)<heap.size() && heap[rightChild(i)].zp<heap[leftChild(i)].zp){
                j= rightChild(i);
            }
            if (heap[i].zp<=heap[j].zp){
                break;
            }
            swap(heap[i],heap[j]);
            i=j;
        }
    }
    void siftDownTleft(int i) {
        while (leftChild(i)<heap.size()){
            int j= leftChild(i);
            if (rightChild(i)<heap.size() && heap[rightChild(i)].Tleft<heap[leftChild(i)].Tleft){
                j= rightChild(i);
            }
            if (heap[i].Tleft<=heap[j].Tleft){
                break;
            }
            swap(heap[i],heap[j]);
            i=j;
        }
    }

public:
    void insertZp(shaurmist key) {
        heap.push_back(key);
        siftUpZp(heap.size() - 1);
    }
    void insertTleft(shaurmist key) {
        heap.push_back(key);
        siftUpTleft(heap.size() - 1);
    }

    shaurmist extractMinZp() {
        if (heap.empty()) {
            shaurmist dummy;
            dummy.zp = 0;
            dummy.Tleft = 0;
            return dummy;
        }

        shaurmist minElement = heap[0];
        heap[0] = heap.back();
        heap.pop_back();
        siftDownZp(0);
        return minElement;
    }

    shaurmist extractMinTleft() {
        if (heap.empty()) {
            shaurmist dummy;
            dummy.zp = 0;
            dummy.Tleft = 0;
            return dummy;
        }

        shaurmist minElement = heap[0];
        heap[0] = heap.back();
        heap.pop_back();
        siftDownTleft(0);
        return minElement;
    }

    shaurmist Min() {
        if (heap.empty()) {
            shaurmist dummy;
            dummy.zp = 0;
            dummy.Tleft = 0;
            return dummy;
        }

        return heap[0];
    }

    int isEmpty() const {
        if (heap.empty()){
            return 1;
        }
        else {return 0;}
    }
};


int main(){
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    long long n,m,moment,time,total=0;
    cin >> n >> m;
    PriorityQueue free;
    PriorityQueue busy;
    vector<shaurmist> chel(n);
    for (int i=0;i<n;i++){
        chel[i].Tleft=0;
        cin >> chel[i].zp;
        free.insertZp(chel[i]);
    }

    for (int i=0;i<m;i++){
        cin >> moment >> time;
        while (!busy.isEmpty() && busy.Min().Tleft <= moment) {
            free.insertZp(busy.extractMinTleft());
        }
        if (!free.isEmpty()){
            shaurmist minEl=free.extractMinZp();
            minEl.Tleft=time+moment;
            total+=minEl.zp*time;
            busy.insertTleft(minEl);
        }
    }


    cout << total;
    return 0;
}