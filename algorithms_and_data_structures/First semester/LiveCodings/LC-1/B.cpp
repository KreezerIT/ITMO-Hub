#include <iostream>
#include <vector>
using namespace std;

const double EPS = 1e-10;

double distanceAtTime(double time, vector<pair<int, int>>& horses) {
    double minDistance = 1e7;
    double maxDistance = 0;

    for (int i = 0; i < horses.size(); i++) {
        double distance = horses[i].first + time * horses[i].second;
        minDistance = min(minDistance, distance);
        maxDistance = max(maxDistance, distance);
    }

    return maxDistance - minDistance;
}

pair<double, double> ternarySearch(vector<pair<int, int>>& horses) {
    double left = 0.0;
    double right = 1e7;

    while (right - left > EPS) {
        double mid1 = (left*2 + right) / 3;
        double mid2 = (left + right*2) / 3;

        if (distanceAtTime(mid1, horses) < distanceAtTime(mid2, horses))
            right = mid2;
        else
            left = mid1;
    }

    double time = (left + right) / 2;
    double distance = distanceAtTime(time, horses);

    return make_pair(time, distance);
}

int main() {
    int n;
    cin >> n;

    vector<pair<int, int>> horses(n);
    for (int i = 0; i < n; ++i) {
        cin >> horses[i].first >> horses[i].second;
    }

    pair<double, double> result = ternarySearch(horses);

    cout << fixed;
    cout.precision(6);
    cout << result.first << " " << result.second;

    return 0;
}
