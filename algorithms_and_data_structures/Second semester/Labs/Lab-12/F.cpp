#include <iostream>
#include <vector>
#define ll long long
#define MAXM 1100000
using namespace std;

struct StudentData {
    string student1;
    string student2;
    string student3;
};

struct Counter {
    ll student1;
    ll student2;
    ll student3;

    Counter() : student1(0), student2(0), student3(0) {}
};

ll h(string& value) {
    ll result = 0;
    for (ll i = 0; i < value.length(); ++i) {
        result *= 151;
        result += (ll)value[i];
    }
    return result;
}

ll h1(ll& value) {
    return value % MAXM;
}

ll h2(ll& value) {
    return value % MAXM;
}

void hash_insert(vector<vector<ll>>& hashTable, ll& key) {
    ll value;
    if (hashTable[h1(key)].empty()) {
        hashTable[h1(key)].emplace_back(key);
    } else if (hashTable[h2(key)].empty()) {
        hashTable[h2(key)].emplace_back(key);
    } else {
        if (hashTable[h2(key)][0] == key) {
            return;
        }
        value = hashTable[h2(key)][0];
        hashTable[h2(key)][0] = key;
        hash_insert(hashTable, value);
    }
}


void checkStudent(vector<vector<ll>>& student, ll& Hf, ll& localcounter, ll& key){
    if ((student[Hf].size() == 1 && student[Hf][0] == key) ||
        (student[Hf].size() == 1 && student[Hf][0] == key)) {
        localcounter++;
    }
}

int main() {
    /*
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
     */
    ll n;
    cin >> n;

    vector<StudentData> students(n);
    vector<vector<ll>> student1(MAXM);
    vector<vector<ll>> student2(MAXM);
    vector<vector<ll>> student3(MAXM);

    string value;
    ll key;
    for (ll i = 0; i < 3 * n; ++i) {
        cin >> value;
        key = h(value);
        if (i < n) {
            students[i].student1 = value;
            hash_insert(student1, key);
        } else if (i>=n && i < 2 * n) {
            students[i - n].student2 = value;
            hash_insert(student2, key);
        } else {
            students[i - 2 * n].student3 = value;
            hash_insert(student3, key);
        }
    }


    Counter counter;
    ll localcounter = 0;
    for (ll i = 0; i < n; ++i) {

        key = h(students[i].student1);
        if ((student2[h1(key)].size() == 1 && student2[h1(key)][0] == key) ||
            (student2[h2(key)].size() == 1 && student2[h2(key)][0] == key)) {
            localcounter++;
        }
        if ((student3[h1(key)].size() == 1 && student3[h1(key)][0] == key) ||
            (student3[h2(key)].size() == 1 && student3[h2(key)][0] == key)) {
            localcounter++;
        }
        if (localcounter == 0) {
            counter.student1++;
            counter.student1++;
            counter.student1++;
        }
        if (localcounter == 1) {
            counter.student1++;
        }
        localcounter = 0;

        key = h(students[i].student2);
        if ((student1[h1(key)].size() == 1 && student1[h1(key)][0] == key) ||
            (student1[h2(key)].size() == 1 && student1[h2(key)][0] == key)) {
            localcounter++;
        }
        if ((student3[h1(key)].size() == 1 && student3[h1(key)][0] == key) ||
            (student3[h2(key)].size() == 1 && student3[h2(key)][0] == key)) {
            localcounter++;
        }
        if (localcounter == 0) {
            counter.student2++;
            counter.student2++;
            counter.student2++;
        }
        if (localcounter == 1) {
            counter.student2++;
        }
        localcounter = 0;

        key = h(students[i].student3);
        if ((student2[h1(key)].size() == 1 && student2[h1(key)][0] == key) ||
            (student2[h2(key)].size() == 1 && student2[h2(key)][0] == key)) {
            localcounter++;
        }
        if ((student1[h1(key)].size() == 1 && student1[h1(key)][0] == key) ||
            (student1[h2(key)].size() == 1 && student1[h2(key)][0] == key)) {
            localcounter++;
        }
        if (localcounter == 0) {
            counter.student3++;
            counter.student3++;
            counter.student3++;
        }
        if (localcounter == 1) {
            counter.student3++;
        }
        localcounter = 0;
    }

    cout << counter.student1 << " " << counter.student2 << " " << counter.student3;
    return 0;
}
