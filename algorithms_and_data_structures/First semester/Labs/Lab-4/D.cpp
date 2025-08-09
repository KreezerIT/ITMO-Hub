#include <iostream>
#include <vector>
using namespace std;

struct City {
    int code;
    int rating;
    int fee;
};


class PriorityQueue {
private:
    vector<City> heap;
    int parent(int i) { return (i - 1) / 2; }
    int leftChild(int i) { return 2 * i + 1; }
    int rightChild(int i) { return 2 * i + 2; }

    void siftUp(int i, string priority) {
        while (i > 0) {
            int parentIndex = parent(i);
            if ((priority == "rating" && heap[i].rating > heap[parentIndex].rating) ||
                (priority == "fee" && heap[i].fee > heap[parentIndex].fee) ||
                ((heap[i].rating == heap[parentIndex].rating || heap[i].fee == heap[parentIndex].fee) && heap[i].code > heap[parentIndex].code)) {
                swap(heap[i], heap[parentIndex]);
                i = parentIndex;
            } else {
                break;
            }
        }
    }

    void siftDown(int i, string priority) {
        while (leftChild(i) < heap.size()) {
            int j = leftChild(i);
            if (rightChild(i) < heap.size() &&
                ((priority == "rating" && heap[rightChild(i)].rating > heap[j].rating) ||
                 (priority == "fee" && heap[rightChild(i)].fee > heap[j].fee) ||
                 ((heap[rightChild(i)].rating == heap[j].rating || heap[rightChild(i)].fee == heap[j].fee) && heap[rightChild(i)].code > heap[j].code))) {
                j = rightChild(i);
            }

            if ((priority == "rating" && heap[i].rating > heap[j].rating) ||
                (priority == "fee" && heap[i].fee > heap[j].fee) ||
                ((heap[i].rating == heap[j].rating || heap[i].fee == heap[j].fee) && heap[i].code > heap[j].code)) {
                break;
            }

            swap(heap[i], heap[j]);
            i = j;
        }
    }

public:
    void insert(City key, string priority) {
        heap.push_back(key);
        if (priority=="rating") {
            siftUp(heap.size() - 1,"rating");
        }
        else {siftUp(heap.size() - 1,"fee");}
    }

    City extractMax(string priority) {
        if (heap.empty()) {
            City dummy = { -1, -1, -1 };
            return dummy;
        }

        City maxElement = heap[0];
        swap(heap[0],heap.back());
        heap.pop_back();

        if (priority == "rating") {
            siftDown(0, "rating");
        } else {
            siftDown(0, "fee");
        }

        return maxElement;
    }

    City Max() {

        City maxElement = heap[0];
        return maxElement;
    }

    void removeElement(int value) {
        for (int i = 0; i < heap.size(); ++i) {
            if (heap[i].code == value) {
                heap[i] = heap.back();
                heap.pop_back();
                siftDown(i,"fee");
                return;
            }
        }
    }

    int isEmpty() const {
        if (heap.empty()){
            return 1;
        }
        else {return 0;}
    }
};

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    PriorityQueue Ratings;
    PriorityQueue Fees;
    int n,m;
    cin >> n;
    for (int i=0;i<n;i++){
        City gorod;
        cin >> gorod.code >> gorod.rating >> gorod.fee;
        Ratings.insert(gorod, "rating");
        Fees.insert(gorod, "fee");
    }
    cin >> m;
    int j=0;
    string answer;
    vector <int> Cities;
    while (!Ratings.isEmpty()){
        if (Ratings.Max().code==Fees.Max().code){
            Cities.push_back(Ratings.Max().code);
            Ratings.extractMax("rating");
            Fees.extractMax("fee");
        }
        else {
            if (j!=m) {
                cin >> answer;
                cout << Ratings.Max().code << " ";
                if (answer == "YES") {
                    Cities.push_back(Ratings.Max().code);
                    Fees.removeElement(Ratings.Max().code);
                    Ratings.extractMax("rating");
                } else if (answer =="NO") {
                    Fees.removeElement(Ratings.Max().code);
                    Ratings.extractMax("rating");
                }
                j++;
            }
            else {Ratings.extractMax("rating");}
        }
    }

    cout << "\n";
    for (int i=0;i<Cities.size();i++){cout << Cities[i] << " ";}
    return 0;
}