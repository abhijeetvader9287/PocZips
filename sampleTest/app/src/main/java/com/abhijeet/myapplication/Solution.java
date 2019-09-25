package com.abhijeet.myapplication;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class Solution {
    public static String getSmallestAndLargest(String s, int k) {
        String smallest = "";
        String largest = "";
        int length = s.length();
        int arraySize = length / k;
        // Complete the function
        // 'smallest' must be the lexicographically smallest substring of length 'k'
        // 'largest' must be the lexicographically largest substring of length 'k'
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            try {
                strings.add(s.substring(i, i + 3));
            } catch (Exception ed) {
            }
            Collections.sort(strings);

        }
        smallest = strings.get(0);
        largest = strings.get(strings.size() - 1);
        return smallest + "\n" + largest;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String s = scan.next();
        int k = scan.nextInt();
        scan.close();
        System.out.println(getSmallestAndLargest(s, k));
    }
}