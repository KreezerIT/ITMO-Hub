#include <iostream>
using namespace std;

struct TreeNode {
    int data;
    TreeNode* left;
    TreeNode* right;

    TreeNode(int value) : data(value), left(nullptr), right(nullptr) {}
};

class BinarySearchTree {
private:
    TreeNode* root;


    TreeNode* sortedArrayToBST(int arr[], int start, int end) {
        if (start > end) {
            return nullptr;
        }

        int mid = (start + end) / 2;
        TreeNode* node = new TreeNode(arr[mid]);

        node->left = sortedArrayToBST(arr, start, mid - 1);
        node->right = sortedArrayToBST(arr, mid + 1, end);

        return node;
    }

public:
    BinarySearchTree() : root(nullptr) {}


    void sortedArrayToBST(int arr[], int size) {
        root = sortedArrayToBST(arr, 0, size - 1);
    }

    void preorderTraversal(TreeNode* node) const {
        if (node != nullptr) {
            cout << node->data << " ";
            preorderTraversal(node->left);
            preorderTraversal(node->right);
        }
    }

    void preorderTraversal() const {
        preorderTraversal(root);
    }
};

int main() {
    BinarySearchTree bst;
    int n;
    cin >> n;
    int arr[n];
    for (int i=0;i<n;i++){
        cin >> arr[i];
    }

    bst.sortedArrayToBST(arr, n);

    bst.preorderTraversal();
    return 0;
}
