#ifndef ITMO_Labs_CPP_Lab7_CIRCULARBUFFER_H
#define ITMO_Labs_CPP_Lab7_CIRCULARBUFFER_H

#include <list>
#include <iostream>

class Ring {
private:
    std::list<int> buffer;
    std::list<int>::iterator current;
public:
    Ring();
    ~Ring();

    void pushNext(int d);
    void pushPrev(int d);
    int pull_front();
    int pull_tail();
    void pull_by_index(int index);
    void insert_by_index(int index, int value);
    void goNext();
    void goPrev();
    void print();
    void adjustCapacity(int newCapacity);
    int& operator[](int index);

    class Iterator {
    private:
        std::list<int>::const_iterator ptr;
        const Ring* container;
    public:
        Iterator(std::list<int>::const_iterator el, const Ring* cont) : ptr(el), container(cont) {}
        int& operator*() { return const_cast<int&>(*ptr); }
        Iterator& operator++() { ++ptr; if (ptr == container->buffer.end()) ptr = container->buffer.begin(); return *this; }
        Iterator& operator--() { if (ptr == container->buffer.begin()) ptr = container->buffer.end(); --ptr; return *this; }
        bool operator!=(const Iterator& other) const { return ptr != other.ptr; }
    };

    Iterator begin() const { return Iterator(buffer.begin(), this); }
    Iterator end() const { return Iterator(buffer.end(), this); }
};

#endif
