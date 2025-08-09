#include <iostream>
#include <vector>
using namespace std;

struct Student {
    int isu;
    int points;
    Student(int isu, int points) : isu(isu), points(points) {}
};

class Group {
private:
    vector<Student> students;
public:
    void addStudent(int isu, int points) {
        students.emplace_back(isu, points);
    }

    void removeStudent(int isu) {
        for (auto it = students.begin(); it != students.end(); ++it) {
            if (it->isu == isu) {
                students.erase(it);
                break;
            }
        }
    }

    int getAveragePoints() const {
        if (students.empty()) return 0;

        int totalPoints = 0;
        for (const auto& student : students) {
            totalPoints += student.points;
        }
        return totalPoints / students.size();
    }

    int findTopStudent() const {
        if (students.empty()) return -1;

        int topPoints = INT_MIN;
        for (const auto& student : students) {
            if (student.points > topPoints) {
                topPoints = student.points;
            }
        }
        return topPoints;
    }
};

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    int M, Q;
    cin >> M >> Q;
    vector<Group> groups(M + 1);

    for (int i = 0; i < Q; ++i) {
        int group, isu, points;
        char queryType;
        cin >> queryType >> group;

        if (queryType == 'a') {
            cout << groups[group].getAveragePoints() << "\n";
        } else if (queryType == '-') {
            cin >> isu;
            groups[group].removeStudent(isu);
        } else if (queryType == '+') {
            cin >> isu >> points;
            groups[group].addStudent(isu, points);
        } else if (queryType == 'm') {
            cout << groups[group].findTopStudent() << "\n";
        }
    }

    return 0;
}
