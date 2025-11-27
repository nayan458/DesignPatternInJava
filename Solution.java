
class Solv {
    private int m;
    private int n;
    private int k;
    private int[][][] t;
    public int numberOfPaths(int[][] grid, int k) {
        this.k = k;
        this.m = grid.length;
        this.n = grid[0].length;
        this.t = new int[m][n][k];
        for(int i = 0; i < m; i++)
            for(int j = 0; j < n; j++)
                for(int l = 0; l < k; l++)
                    t[i][j][l] = -1;

        return dfs(grid,0,0,0);
    }

    private int dfs(int[][] grid, int row, int col, int mod_sum) {
        if(col == n || row == m)
            return 0;
        mod_sum = ((mod_sum % k) + (grid[row][col] % k) % k);
        if(t[row][col][mod_sum] != -1)
            return t[row][col][mod_sum];

        if(row == m-1 && col == n-1)
            return t[row][col][mod_sum] = (mod_sum == 0) ? 1 : 0;

        int rightPath = dfs(grid, row, col + 1, mod_sum);
        int leftPath = dfs(grid, row + 1, col, mod_sum);
        return t[row][col][mod_sum] = (rightPath + leftPath);
    }
}

public class Solution {
    public static void main(String[] args) {
        Solv x = new Solv();
        int[][] grid = {{5,2,4},{3,0,5},{0,7,2}};
        int k = 3;
        System.out.println(x.numberOfPaths(grid, k));
    }
}