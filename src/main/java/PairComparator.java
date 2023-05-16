package main.java;

import javafx.util.Pair;
import java.util.Comparator;

public class PairComparator implements Comparator<Pair<Integer, Integer>> {

    @Override
    public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
        int cmp = p1.getKey().compareTo(p2.getKey());

        if (cmp != 0) {
            return cmp;
        }
        else {
            return p1.getValue().compareTo(p2.getValue());
        }
    }
}