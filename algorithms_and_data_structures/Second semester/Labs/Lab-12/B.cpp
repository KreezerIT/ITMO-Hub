#include "blazingio.hpp"
#include <vector>
#include <algorithm>
#define ll long long
#define MAXS 110000
#define INTMAX 2147483647
using namespace std;


ll hash_function(ll key, ll n){
    return (key%n);
}


bool hash_search(vector<ll>& hash_table, ll key, ll n) {
    ll index = hash_function(key,n);
    ll original_index = index;

    while (hash_table[index] != INTMAX) {
        if (hash_table[index] == key) {
            return true;
        }
        index++;
        index %= n;

        if (index == original_index)
            break;
    }
    return false;
}

void hash_insert(vector<ll>& hash_table, ll key, ll n) {
    ll index = hash_function(key,n);
    while (hash_table[index] != INTMAX && hash_table[index] != key) {
        index = (index + 1) % n;
    }
    hash_table[index] = key;
}

ll hash_function() {
    return ((ll) (rand()) << (sizeof(ll) * 4)) | rand();
}

bool comparator(ll a, ll b) {
    return a < b;
}

void updateAns(vector<ll>& Permutations, vector<ll>& word2, ll& ans, ll i, ll j){
    ll diffs = word2[j] - word2[i];

    if (binary_search(Permutations.begin(), Permutations.end(), diffs)) {
        ans=max(j-i,ans);
    }
}

void insertWord(vector<ll>& Permutations, vector<ll>& word1, ll i, ll j){
    ll Perm = word1[j] - word1[i];
    Permutations.push_back(Perm);
}

void updateWord(vector<ll>& word, vector<ll>& hashTable, ll i){
    word[i] = word[i - 1] + hashTable[word[i]];
}

int main() {
    /*
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    */

    ll n, m;
    cin >> n;
    vector<ll> word1(n + 1), hashTable(MAXS, 0), Permutations;

    for (ll i = 1; i <= n; i++) {
        cin >> word1[i];
        hashTable[word1[i]] = hash_function();
    }

    cin >> m;
    vector<ll> word2(m + 1);

    for (ll i = 1; i <= m; i++) {
        cin >> word2[i];
        hashTable[word2[i]] = hash_function();
    }


    for (ll i = 1; i <= n; i++) {updateWord(word1,hashTable,i);}
    for (ll i = 1; i <= m; i++) {updateWord(word2,hashTable,i);}

    for (ll i = 0; i <= n ; i++) {
        for (ll j = i; j <= n; j++) {
            insertWord(Permutations, word1, i, j);
        }
    }

    sort(Permutations.begin(), Permutations.end(), comparator);

    ll ans = 0;
    for (ll i = 0; i <= m; i++) {
        for (ll j = i; j <= m; j++) {
            updateAns(Permutations, word2, ans, i, j);
        }
    }

    cout << ans;
    return 0;

}