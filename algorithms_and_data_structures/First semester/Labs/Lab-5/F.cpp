#include <iostream>
#include <vector>

using namespace std;

bool isValidTraversalMain(vector<int>& preOrder, vector<int>& inOrder, vector<int>& postOrder, int preStart, int preEnd, int inStart, int inEnd, int postStart, int postEnd) {
    if (preStart > preEnd || inStart > inEnd || postStart > postEnd) {
        return true;
    }

    int nodeValue = postOrder[postEnd];
    int nodeIndex = inStart;
    while (nodeIndex < inEnd && inOrder[nodeIndex] != nodeValue) {
        nodeIndex++;
    }

    if (nodeIndex > inEnd || preOrder[preStart] != nodeValue) {
        return false;
    }

    int leftSubtreeSize = nodeIndex - inStart;
    int leftPreEnd = preStart + leftSubtreeSize;

    return isValidTraversalMain(preOrder, inOrder, postOrder, preStart + 1, leftPreEnd, inStart, nodeIndex - 1, postStart, postStart + leftSubtreeSize - 1) &&
           isValidTraversalMain(preOrder, inOrder, postOrder, leftPreEnd + 1, preEnd, nodeIndex + 1, inEnd, postStart + leftSubtreeSize, postEnd - 1);
}

bool isValidTraversal(vector<int>& preOrder, vector<int>& inOrder, vector<int>& postOrder) {
    int n = preOrder.size();

    return isValidTraversalMain(preOrder, inOrder, postOrder, 0, n - 1, 0, n - 1, 0, n - 1);
}

int main() {
    int n;
    cin >> n;

    vector<int> preOrder(n);
    vector<int> inOrder(n);
    vector<int> postOrder(n);

    for (int i = 0; i < n; i++) {cin >> preOrder[i];}
    for (int i = 0; i < n; i++) {cin >> inOrder[i];}
    for (int i = 0; i < n; i++) {cin >> postOrder[i];}

    if (isValidTraversal(preOrder, inOrder, postOrder)) {
        cout << "YES";
    } else {
        cout << "NO";
    }

    return 0;
}
