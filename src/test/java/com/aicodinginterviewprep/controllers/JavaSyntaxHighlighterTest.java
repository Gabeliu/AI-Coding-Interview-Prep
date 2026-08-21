package com.aicodinginterviewprep.controllers;

import java.util.Collection;

import org.fxmisc.richtext.model.StyleSpan;
import org.fxmisc.richtext.model.StyleSpans;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaSyntaxHighlighterTest {

    @Test
    void tagsAKeywordAsCodeKeyword() {
        assertStyleAt("public int x;", 0, "code-keyword");
    }

    @Test
    void tagsAStringLiteralAsCodeString() {
        String text = "String s = \"hello\";";
        assertStyleAt(text, text.indexOf('"'), "code-string");
    }

    @Test
    void tagsALineCommentAsCodeComment() {
        String text = "// a comment\nint x;";
        assertStyleAt(text, 0, "code-comment");
    }

    @Test
    void tagsABlockCommentAsCodeComment() {
        String text = "/* block */ int x;";
        assertStyleAt(text, 0, "code-comment");
    }

    @Test
    void tagsANumberLiteralAsCodeNumber() {
        String text = "int x = 42;";
        assertStyleAt(text, text.indexOf("42"), "code-number");
    }

    @Test
    void tagsAnAnnotationAsCodeAnnotation() {
        String text = "@Override\npublic void foo() {}";
        assertStyleAt(text, 0, "code-annotation");
    }

    @Test
    void tagsBracesParensAndBracketsAsCodeBracket() {
        String text = "int[] a = new int[3];";
        assertStyleAt(text, 3, "code-bracket");
    }

    @Test
    void leavesPlainIdentifiersUnstyled() {
        String text = "myVariable";
        StyleSpans<Collection<String>> spans = JavaSyntaxHighlighter.computeHighlighting(text);
        StyleSpan<Collection<String>> firstSpan = spans.iterator().next();
        assertTrue(firstSpan.getStyle().isEmpty());
        assertEquals(text.length(), firstSpan.getLength());
    }

    @Test
    void handlesEmptyText() {
        StyleSpans<Collection<String>> spans = JavaSyntaxHighlighter.computeHighlighting("");
        assertEquals(1, spans.getSpanCount());
        assertEquals(0, spans.iterator().next().getLength());
    }

    private void assertStyleAt(String text, int position, String expectedStyleClass) {
        StyleSpans<Collection<String>> spans = JavaSyntaxHighlighter.computeHighlighting(text);

        int consumed = 0;
        for (StyleSpan<Collection<String>> span : spans) {
            int start = consumed;
            int end = consumed + span.getLength();
            if (position >= start && position < end) {
                assertTrue(
                    span.getStyle().contains(expectedStyleClass),
                    "expected style " + expectedStyleClass + " at position " + position
                        + " but was " + span.getStyle()
                );
                return;
            }
            consumed = end;
        }
        throw new AssertionError("position " + position + " not covered by any span");
    }
}
