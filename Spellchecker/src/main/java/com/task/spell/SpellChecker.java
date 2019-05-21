package com.task.spell;

import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

/**
 * Class for single word spell checking
 */
public class SpellChecker {

    private final @NotNull HashSet<String> dictionary;
    private int depth = 2;
    private static final String DEFAULT_DICT = "src/main/resources/words.txt";

    /**
     * Creates a database of words to check based on this file
     */
    public SpellChecker(File file) throws IOException {
        dictionary = new HashSet<>();
        try (var reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            var s = reader.readLine();
            while (s != null) {
                var wordsList = Arrays.stream(s.split(" "))
                        .filter(word -> word.length() > 1 || "a".equals(word)).collect(Collectors.toList());
                dictionary.addAll(wordsList);
                s = reader.readLine();
            }
        }
    }

    /**
     * Creates a database from default english word list
     */
    public SpellChecker() throws IOException {
        this(new File(DEFAULT_DICT));
    }

    /**
     * Uses given list as word patterns
     */
    public SpellChecker(@NotNull List<String> words) {
        dictionary = new HashSet<>();
        dictionary.addAll(words);
    }

    /**
     * Adds new words to the list
     */
    public void addWords(@NotNull List<String> words) {
        dictionary.addAll(words);

    }

    /**
     * Sets the number of typos that can be made in a word.
     * Default value is 2
     */
    public void setDepht(int depth) {
        this.depth = depth;
    }

    private static String swapped(@NotNull String word, int i) {
        var pref = "";
        if (i > 0) {
            pref = word.substring(0, i);
        }
        var suff = "";
        if (i < word.length() - 2) {
            suff = word.substring(i + 2);
        }

        return pref + word.charAt(i + 1) + word.charAt(i) + suff;
    }

    private static List<String> missing(@NotNull String word, int i) {
        var pref = "";
        if (i > 0) {
            pref = word.substring(0, i);
        }
        var suff = "";
        if (i < word.length()) {
            suff = word.substring(i);
        }
        var res = new ArrayList<String>();

        for (char c = 'a'; c <= 'z'; c++) {
            res.add(pref + c + suff);
        }

        return res;
    }

    private static List<String> wrong(@NotNull String word, int i ) {
        var pref = "";
        if (i > 0) {
            pref = word.substring(0, i);
        }
        var suff = "";
        if (i < word.length() - 1) {
            suff = word.substring(i + 1);
        }
        var res = new ArrayList<String>();

        for (char c = 'a'; c <= 'z'; c++) {
            res.add(pref + c + suff);
        }

        return res;
    }

    private static String extra(@NotNull String word, int i) {
        var pref = "";

        if (i > 0) {
            pref = word.substring(0, i);
        }
        var suff = "";
        if (i < word.length() - 1) {
            suff = word.substring(i + 1);
        }

        return pref + suff;
    }


    private HashSet<String> possibleWords(@NotNull String word) {
        var words = new HashSet<String>();

        for (int i = 0; i < word.length(); i++) {

            if (i + 1 < word.length()) {
                words.add(swapped(word, i));
            }

            words.addAll(missing(word, i));
            words.add(extra(word, i));
            words.addAll(wrong(word, i));
        }

        return words;
    }

    private HashSet<String> deepPossibleWords(@NotNull String word) {
        var newWords = new HashSet<String>();
        newWords.add(word);
        
        var result = new HashSet<String>();
        result.add(word);
        
        for (int d = 0; d < depth; d++) {
            var tmp = new HashSet<String>();

            for (var s : newWords) {
                tmp.addAll(possibleWords(s));
            }

            tmp.removeAll(result);
            newWords = tmp;
            result.addAll(tmp);
        }

        return result;
    }

    /**
     * Searches for words that can be converted to this using no more than depht typos.
     * Default depht is 2
     * @return set of possible words
     */
    public Set<String> check(@NotNull String word) {
        if (dictionary.contains(word)) {
            return Set.of(word);
        }

        Set<String> set;
        if (depth == 1) {
            set = possibleWords(word);
        } else {
            set = deepPossibleWords(word);
        }

        set.retainAll(dictionary);
        return set;
    }

}
