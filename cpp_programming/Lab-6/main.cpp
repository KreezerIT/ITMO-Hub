#include <iostream>
#include <vector>
using namespace std;


template<typename InputIterator, typename Predicate>
bool alg_all_of(InputIterator first, InputIterator last, Predicate pred) {
    for (auto it = first; it != last; ++it) {
        if (!pred(*it)) {
            return false;
        }
    }
    return true;
}

template<typename InputIterator, typename Predicate>
bool one_of(InputIterator first, InputIterator last, Predicate pred) {
    int count = 0;
    for (auto it = first; it != last; ++it) {
        if (pred(*it)) {
            count++;
            if (count > 1) return false;
        }
    }
    return count == 1;
}

template<typename InputIterator, typename T>
pair<InputIterator, int> find_backward(InputIterator first, InputIterator last, const T& value) {
    for (auto it = last; it != first; ) {
        --it;
        if (*it == value) {
            return make_pair(it,last-it);
        }
    }
    return make_pair(last,-1);
}


int main() {
    vector<int> vec = {1, 2, 3, 4, 5};

    if (alg_all_of(vec.begin(), vec.end(), [](int i) { return i < 10; })) {
        cout << "All elements are <10\n";
    } else {
        cout << "Not all elements are <10\n";
    }

    if (one_of(vec.begin(), vec.end(), [](int i) { return i % 2 == 0; })) {
        cout << "Only one element is divided by 2\n";
    } else {
        cout << "No elements or more than one element is divided by 2\n";
    }

    auto result = find_backward(vec.begin(), vec.end(), 4);
    if (result.first != vec.end()) {
        cout << "Found element " << *result.first << " backwards at position -" << result.second << "\n";
    } else {
        cout << "Element not found\n";
    }

    return 0;
}
