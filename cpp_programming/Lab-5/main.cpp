#include <iostream>
#include <stdexcept>

template<typename T>
T min(T a, T b) {
    return (a < b) ? a : b;
}

template<int N, class T>
class MaxSizeQueue {
private:
    T queue[N];
    int head;
    int tail;
    int size;

public:
    MaxSizeQueue() : head(0), tail(0), size(0) {}

    void enqueue(const T& value) {
        if (size == N) {
            throw std::overflow_error("Queue overflow");
        }
        queue[tail] = value;
        tail = (tail + 1) % N;
        size++;
    }

    T dequeue() {
        if (size == 0) {
            throw std::underflow_error("Queue underflow");
        }
        T value = queue[head];
        head = (head + 1) % N;
        size--;
        return value;
    }
};


int main() {
    std::cout << "Min of 5 and 10 is: " << min(5, 10) << "\n";
    std::cout << "Min of 3.14 and 2.71 is: " << min(1.3, 2.5) << "\n";

    MaxSizeQueue<3, int> queue;
    try {
        for (int i = 0; i < 4; ++i) {
            queue.enqueue(i);
            std::cout << "Enqueued: " << i << "\n";
        }
    } catch (const std::overflow_error& error) {
        std::cerr << "Exception: " << error.what() << "\n";
    }

    try {
        for (int i = 0; i < 4; ++i) {
            std::cout << "Dequeued: " << queue.dequeue() << "\n";
        }
    } catch (const std::underflow_error& error) {
        std::cerr << "Exception: " << error.what() << "\n";
    }

    return 0;
}
