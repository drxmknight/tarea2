package token;

import java.io.Serializable;
import java.util.ArrayList;

public class Token implements Serializable {
    public ArrayList<Integer> queue;
    public int[] LN;

    public Token(int n) {
        queue = new ArrayList<>();

        LN = new int[n];
        for (int i = 0; i < n; i++) {
            LN[i] = 0;
        }
    }
}
