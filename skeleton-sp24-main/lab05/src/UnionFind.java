public class UnionFind {
    private int[] parent;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        // 验证参数 N 合法性
        if (N <= 0) {
            throw new IllegalArgumentException("N must be positive");
        }

        // 初始化 parent 数组
        parent = new int[N];
        // 设置初始值
        for (int i = 0; i < N; i++) {
            parent[i] = 1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        int root = find(v);
        return -parent[root];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        return parent[v];
    }

    /* Returns true if nodes/vertices V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        // Add input validation
        if (v < 0 || v >= parent.length) {
            throw new IllegalArgumentException("Invalid index");
        }

        // 寻找根节点
        int root = v;
        while (parent[root] >= 0) {
            root = parent[root];
        }

        return root;
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing an item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        // Find the roots of v1 and v2
        int root1 = find(v1);
        int root2 = find(v2);

        // If v1 and v2 in the same sets,do nothing
        if (root1 == root2) {
            return;
        }

        // Get the size of both sets
        int size1 = -parent[root1];
        int size2 = -parent[root2];

        // Union the smaller into the larger set
        if (size1 > size2) {
            parent[root2] = root1;
            parent[root1] = -(size1 + size2);
        } else {
            parent[root1] = root2;
            parent[root2] = -(size1 + size2);
        }
    }

}
