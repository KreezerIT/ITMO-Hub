#include <iostream>
#include <vector>
#include <algorithm>
#define ll long long
using namespace std;
const ll MOD = 1000000007;

vector<ll> compute_degrees_m(ll m, ll n) {
    vector<ll> m_degrees(n + 1, 1);
    for (ll i = 1; i <= n; i++) {
        m_degrees[i] = (m_degrees[i - 1] * m) % MOD;
    }
    return m_degrees;
}

vector<ll> compute_hashes( vector<ll>& arr, ll m) {
    ll n = arr.size();
    vector<ll> hashes(n + 1, 0);
    for (ll i = 0; i < n; i++) {
        hashes[i + 1] = (hashes[i] * m + arr[i]) % MOD;
    }
    return hashes;
}

ll compute_sub_hash( vector<ll>& backward_hashes,  vector<ll>& m_degrees, ll start, ll len) {
    ll hash_value = (backward_hashes[start] - backward_hashes[start - len] * m_degrees[len] % MOD + MOD) % MOD;
    return hash_value;
}

int main() {
    ll n, m;
    cin >> n >> m;

    vector<ll> cards(n);
    for (ll i = 0; i < n; i++) {
        cin >> cards[i];
    }

    vector<ll> cards_mirror(cards.rbegin(), cards.rend());

    vector<ll> forward_hashes = compute_hashes(cards, m);
    vector<ll> backward_hashes = compute_hashes(cards_mirror, m);
    vector<ll> m_degrees = compute_degrees_m(m, n);

    vector<ll> results;
    for (ll i = 0; i <n ; i++) {
        ll forward_hash = forward_hashes[i];
        ll backward_hash = compute_sub_hash(backward_hashes, m_degrees, n - i, i);
        if (forward_hash == backward_hash) {
            results.push_back(n - i);
        }
    }

    reverse(results.begin(), results.end());
    for (ll result : results) {
        cout << result << " ";
    }

    cout << "\n";
    return 0;
}
