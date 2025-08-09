#include <iostream>
using namespace std;

int findCycleLength(int connections[], int n) {
    bool visited[n + 1] = {false};

    for (int i = 1; i <= n; ++i) {
        if (!visited[i]) {
            int current = i;
            while (!visited[current]) {
                visited[current] = true;
                current = connections[current - 1];
            }

            if (visited[current]) {
                int cycleLength = 1;
                int cycleStart = connections[current - 1];
                while (cycleStart != current) {
                    cycleLength++;
                    cycleStart = connections[cycleStart - 1];
                }
                return cycleLength;
            }
        }
    }

    return -1;
}

int main() {
    int n;
    cin >> n;
    int connections[n] = {0};

    for (int i = 0; i < n; i++) {
        cin >> connections[i];
    }

    for (int i = 0; i < n; i++) {
        if (connections[i] == 0) {
            cout << "-1";
            return 0;
        }
    }

    int cycleLength = findCycleLength(connections, n);
    cout << cycleLength;
    return 0;
}
