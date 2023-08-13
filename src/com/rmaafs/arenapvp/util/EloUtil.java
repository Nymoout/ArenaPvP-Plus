package com.rmaafs.arenapvp.util;

import com.google.common.collect.Range;

import java.util.ArrayList;
import java.util.List;

public class EloUtil {

    private static final List<Range<Integer>> eloRanges = new ArrayList<>();
    private static final List<Integer> eloDifferences = new ArrayList<>();

    static {
        addRange(-Integer.MAX_VALUE, -500, 31);
        addRange(-499, -450, 30);
        addRange(-449, -400, 29);
        addRange(-399, -350, 28);
        addRange(-349, -300, 27);
        addRange(-299, -250, 26);
        addRange(-249, -200, 24);
        addRange(-199, -150, 22);
        addRange(-149, -100, 20);
        addRange(-99, -50, 19);
        addRange(-49, 0, 17);
        addRange(1, 49, 17);
        addRange(50, 99, 14);
        addRange(100, 149, 12);
        addRange(150, 199, 10);
        addRange(200, 249, 8);
        addRange(250, 299, 6);
        addRange(300, 349, 4);
        addRange(350, 399, 3);
        addRange(400, 449, 2);
        addRange(450, Integer.MAX_VALUE, 1);
    }

    private static void addRange(int min, int max, int difference) {
        eloRanges.add(Range.closed(min, max));
        eloDifferences.add(difference);
    }

    public static int getEloDifference(int total) {
        for (int i = 0; i < eloRanges.size(); i++) {
            if (eloRanges.get(i).contains(total)) {
                return eloDifferences.get(i);
            }
        }
        return total;
    }
}
