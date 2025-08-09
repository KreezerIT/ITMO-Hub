#include "circularbuffer.h"

Ring::Ring() {
    current = buffer.begin();
}

Ring::~Ring() {}

void Ring::pushNext(int d) {
    if (buffer.empty()) {
        buffer.push_front(d);
        current = buffer.begin();
    } else {
        if (current == std::prev(buffer.end()))
            current = buffer.begin();
        else
            ++current;
        buffer.insert(current, d);
    }
}

void Ring::pushPrev(int d) {
    if (buffer.empty()) {
        buffer.push_back(d);
        current = buffer.begin();
    } else {
        buffer.insert(current, d);
    }
}

int Ring::pull_front() {
    if (buffer.empty())
        return 0;

    int data = *current;
    current = buffer.erase(current);
    if (current == buffer.end())
        current = buffer.begin();
    return data;
}

int Ring::pull_tail() {
    if (buffer.empty())
        return 0;

    int data = buffer.front();

    if (current == buffer.begin())
        ++current;

    buffer.pop_front();

    return data;
}



void Ring::goNext() {
    if (buffer.empty())
        return;

    ++current;
    if (current == buffer.end())
        current = buffer.begin();
}

void Ring::goPrev() {
    if (buffer.empty())
        return;

    if (current == buffer.begin())
        current = buffer.end();
    --current;
}

void Ring::print() {
    if (buffer.empty()) {
        std::cout << "Empty\n";
        return;
    }

    auto it = current;
    do {
        std::cout << *it << " <-> ";
        ++it;
        if (it == buffer.end())
            it = buffer.begin();
    } while (it != current);
    std::cout << "\n";
}

void Ring::adjustCapacity(int newCapacity) {
    if (newCapacity <= 0) {
        std::cerr << "Invalid capacity\n";
        return;
    }

    while (buffer.size() > newCapacity) {
        pull_front();
    }
}

int& Ring::operator[](int index) {
    auto it = current;
    for (int i = 0; i < index; ++i) {
        ++it;
        if (it == buffer.end())
            it = buffer.begin();
    }
    return *it;
}

void Ring::pull_by_index(int index) {
    if (buffer.empty()) {
        std::cerr << "Empty buffer\n";
        return;
    }

    auto it = current;
    for (int i = 0; i < index; ++i) {
        ++it;
        if (it == buffer.end())
            it = buffer.begin();
    }

    current = buffer.erase(it);
    if (current == buffer.end())
        current = buffer.begin();
}

void Ring::insert_by_index(int index, int value) {
    auto it = current;
    for (int i = 0; i < index; ++i) {
        ++it;
        if (it == buffer.end())
            it = buffer.begin();
    }

    buffer.insert(it, value);
}
