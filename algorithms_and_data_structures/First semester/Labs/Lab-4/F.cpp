#include <iostream>
#include <vector>
#include <cmath>
using namespace std;

struct PrimeNum {
    long long number;
    long long degreedNum;
};

class PriorityQueue {
private:
    vector<PrimeNum> heap;

    long long parent(long long i) { return (i - 1) / 2; }
    long long leftChild(long long i) { return 2 * i + 1; }
    long long rightChild(long long i) { return 2 * i + 2; }

    void siftUp(long long i, string priority) {
        if (priority == "number") {
            while (i > 0 && heap[i].number < heap[parent(i)].number) {
                swap(heap[i], heap[parent(i)]);
                i = parent(i);
            }
        } else if (priority == "degreedNum") {
            while (i > 0 && heap[i].degreedNum < heap[parent(i)].degreedNum) {
                swap(heap[i], heap[parent(i)]);
                i = parent(i);
            }
        }
    }

    void siftDown(long long i, string priority) {
        if (priority == "number") {
            while (leftChild(i) < heap.size()) {
                long long j = leftChild(i);
                if (rightChild(i) < heap.size() && heap[rightChild(i)].number < heap[leftChild(i)].number) {
                    j = rightChild(i);
                }
                if (heap[i].number <= heap[j].number) {
                    break;
                }
                swap(heap[i], heap[j]);
                i = j;
            }
        } else if (priority == "degreedNum") {
            while (leftChild(i) < heap.size()) {
                long long j = leftChild(i);
                if (rightChild(i) < heap.size() && heap[rightChild(i)].degreedNum < heap[leftChild(i)].degreedNum) {
                    j = rightChild(i);
                }
                if (heap[i].degreedNum <= heap[j].degreedNum) {
                    break;
                }
                swap(heap[i], heap[j]);
                i = j;
            }
        }
    }

public:
    void insert(PrimeNum key, string priority) {
        heap.push_back(key);
        if (priority == "number") {
            siftUp(heap.size() - 1, "number");
        } else if (priority == "degreedNum") {
            siftUp(heap.size() - 1, "degreedNum");
        }
    }

    long long extractMin(string priority) {
        if (heap.empty()) {
            return -100;
        } else {
            if (priority == "number") {
                long long minElement = heap[0].number;
                heap[0] = heap.back();
                heap.pop_back();
                siftDown(0, "number");
                return minElement;
            } else if (priority == "degreedNum") {
                long long minElement = heap[0].degreedNum;
                heap[0] = heap.back();
                heap.pop_back();
                siftDown(0, "degreedNum");
                return minElement;
            }
        }
    }

    void decreaseKey(long long y, string priority) {
        if (!isEmpty()) {
            if (priority == "number") {
                heap[0].number = y;
                siftDown(0, "number");
            } else if (priority == "degreedNum") {
                heap[0].degreedNum = y;
                siftDown(0, "degreedNum");
            }
        }
    }

    PrimeNum MinDefolt() const {
        return heap[0];
    }

    bool isEmpty() const {
        return heap.empty();
    }

    long long size() const {
        return heap.size();
    }
};

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);

    long long n;
    cin >> n;
    PriorityQueue heap;
    PriorityQueue heap2;
    long long *a = new long long[n * 15];
    for (long long i = 0; i < n * 15; i++)
        a[i] = i;

    for (long long i = 2; i * i < n * 15 && heap.size() < n; i++) {
        if (a[i]) {
            for (long long j = i * i; j < n * 15; j += i)
                a[j] = 0;
        }
    }

    for (long long i = 2; heap.size() < n && i < n * 15; i++) {
        if (a[i]) {
            PrimeNum prime;
            prime.number = a[i];
            prime.degreedNum = a[i];
            heap.insert(prime, "number");
        }
    }

    delete[] a;

    for (long long i = 0; i < n; i++) {
        PrimeNum chislo;
        chislo = heap.MinDefolt();
        long long x = heap.MinDefolt().number;
        heap2.insert(chislo,"degreedNum");
        x = x * x;
        heap.decreaseKey(x, "number");
    }

    long long ans=0,count=0;
    vector<long long> primes;
    for (long long i = 0; i < n; i++){
        count=log(heap2.MinDefolt().number)/log(heap2.MinDefolt().degreedNum);
        ans+=count;
        for (long long j=0;j<count;j++){
            primes.push_back(heap2.MinDefolt().degreedNum);
        }
        heap2.extractMin("degreedNum");
    }
    cout << ans << "\n";
    for (long long i=0;i<primes.size();i++){
        cout << primes[i] << " ";
    }
    return 0;
}