#include <iostream>
#include <vector>
#include <queue>
using namespace std;

struct Kitten {
    vector<int> preferences;
    int matchIndex;

    Kitten() : matchIndex(-1) {}
};

struct Owner {
    vector<int> preferences;
    int matchIndex;

    Owner() : matchIndex(-1) {}
};

class StableMatcher {
private:
    vector<Kitten> kittens;
    vector<Owner> owners;
    queue<int> freeKittens;

public:
    StableMatcher(int n) {
        kittens.resize(n);
        owners.resize(n);
    }

    void addKittenPreferences(int kittenIndex, const vector<int>& preferences) {
        kittens[kittenIndex].preferences = preferences;
        freeKittens.push(kittenIndex);
    }

    void addOwnerPreferences(int ownerIndex, const vector<int>& preferences) {
        owners[ownerIndex].preferences = preferences;
    }

    void match() {
        while (!freeKittens.empty()) {
            int kittenIndex = freeKittens.front();
            freeKittens.pop();
            Kitten& kitten = kittens[kittenIndex];

            for (int i = 0; i < kitten.preferences.size(); ++i) {
                int ownerIndex = kitten.preferences[i];
                Owner& owner = owners[ownerIndex];
                if (owner.matchIndex == -1) {
                    owner.matchIndex = kittenIndex;
                    kitten.matchIndex = ownerIndex;
                    break;
                } else {
                    int currentKittenIndex = owner.matchIndex;
                    if (isUnstable(ownerIndex, kittenIndex, currentKittenIndex)) {
                        owner.matchIndex = kittenIndex;
                        kitten.matchIndex = ownerIndex;
                        freeKittens.push(currentKittenIndex);
                        break;
                    }
                }
            }
        }
    }

    vector<pair<int, int>> getMatches() const {
        vector<pair<int, int>> matches;
        for (int i = 0; i < kittens.size(); ++i) {
            if (kittens[i].matchIndex != -1) {
                matches.emplace_back(i, kittens[i].matchIndex);
            }
        }
        return matches;
    }

private:
    bool isUnstable(int ownerIndex, int newKittenIndex, int currentKittenIndex) const {
        const Owner& owner = owners[ownerIndex];
        for (int i = 0; i < owner.preferences.size(); ++i) {
            int kittenIndex = owner.preferences[i];
            if (kittenIndex == newKittenIndex) return true;
            if (kittenIndex == currentKittenIndex) return false;
        }
        return false;
    }
};

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    int n;
    cin >> n;
    StableMatcher matcher(n);

    for (int i = 0; i < n; i++) {
        vector<int> preferences(n);
        for (int j = 0; j < n; j++) {
            cin >> preferences[j];
        }
        matcher.addKittenPreferences(i, preferences);
    }

    for (int i = 0; i < n; i++) {
        vector<int> preferences(n);
        for (int j = 0; j < n; j++) {
            cin >> preferences[j];
        }
        matcher.addOwnerPreferences(i, preferences);
    }

    matcher.match();

    vector<pair<int, int>> matches = matcher.getMatches();

    for (auto match : matches)
        cout << match.first << " " << match.second << "\n";

    return 0;
}
