package com.task.spell;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SpellCheckerTest {

    private SpellChecker checker;

    @BeforeEach
    void clean() {
        checker = new SpellChecker(List.of());
    }

    @Test
    void simpleDephtTest(){
        checker.addWords(List.of("cat", "act"));
        checker.setDepht(0);
        assertTrue(checker.check("cct").isEmpty());
    }

    @Test
    void simpleFindTest(){
        var list = new ArrayList<String>();
        list.add("cat");
        list.add("act");
        checker.addWords(list);
        assertEquals(Set.of(list.toArray()), checker.check("cct"));
    }

    @Test
    void checkMissingLetterTest() {
        checker.addWords(List.of("aaa"));
        checker.setDepht(1);
        assertEquals(1, checker.check("aa").size());
    }

    @Test
    void checkExtraLetterTest() {
        checker.addWords(List.of("aaa"));
        checker.setDepht(1);
        assertEquals(1, checker.check("aaaa").size());
    }

    @Test
    void checkWrongLetterTest() {
        checker.addWords(List.of("aaa"));
        checker.setDepht(1);
        assertEquals(1, checker.check("aca").size());
    }

    @Test
    void checkSwappedLettersTest() {
        checker.addWords(List.of("abc"));
        checker.setDepht(1);
        assertEquals(1, checker.check("bac").size());
    }

    @Test
    void checkTwoTyposTest() {
        checker.addWords(List.of("abc"));
        assertEquals(1, checker.check("dcc").size());
    }

    @Test
    void checkTwoSwapsTest() {
        checker.addWords(List.of("abcd"));
        assertEquals(1, checker.check("badc").size());
    }

    @Test
    void dephtComplexTest() {
        checker.addWords(List.of("abcdef"));
        assertTrue(checker.check("abc").isEmpty());
        checker.setDepht(3);
        assertFalse(checker.check("abc").isEmpty());
    }

    @Test
    void checkExistingWord() {
        checker.addWords(List.of("abc"));
        assertEquals(Set.of("abc"), checker.check("abc"));
    }
}