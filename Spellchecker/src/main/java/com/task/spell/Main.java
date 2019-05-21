package com.task.spell;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class Main {
    public static void main(String... args) {

        try (var scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            var checker = new SpellChecker();
            var word = scanner.nextLine();
            var result = checker.check(word);
            if (result.size() == 1 && result.contains(word)) {
                System.out.println(word + " is the correct word");
            } else if (result.isEmpty()) {
                System.out.println("No idea what it could be");
            } else {
                System.out.println("Maybe you meant:");
                for (var variant : result) {
                    System.out.println(variant);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
