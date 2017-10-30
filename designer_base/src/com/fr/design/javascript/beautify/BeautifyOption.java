package com.fr.design.javascript.beautify;

import java.util.HashMap;
import java.util.Map;

public class BeautifyOption {

    private int indentSize = 4;
    private String indentChar = " ";
    private boolean indentWithTabs = true;
    private String eol = "\n";
    private boolean endWithNewline = false;
    private int indentLevel = 0;
    private boolean preserveNewlines = true;
    private int maxPreserveNewlines = 10;
    private boolean spaceInParen = false;
    private boolean spaceInEmptyParen = false;
    private boolean jslintHappy = false;
    private boolean spaceAfterAnonFunction = false;
    private String braceStyle = "collapse";
    private boolean unindentChainedMethods = false;
    private boolean breakChainedMethods = false;
    private boolean keepArrayIndentation = false;
    private boolean unescapeStrings = false;
    private int wrapLineLength = 0;
    private boolean e4x = false;
    private boolean commaFirst = false;
    private String operatorPosition = "before-newline";

    public static BeautifyOption create() {
        return new BeautifyOption();
    }

    private BeautifyOption() {

    }

    public BeautifyOption indentSize(int indentSize) {
        this.indentSize = indentSize;
        return this;
    }

    public BeautifyOption indentChar(String indentChar) {
        this.indentChar = indentChar;
        return this;
    }

    public BeautifyOption indentWithTabs(boolean indentWithTabs) {
        this.indentWithTabs = indentWithTabs;
        return this;
    }

    public BeautifyOption eol(String eol) {
        this.eol = eol;
        return this;
    }

    public BeautifyOption endWithNewline(boolean endWithNewline) {
        this.endWithNewline = endWithNewline;
        return this;
    }

    public BeautifyOption indentLevel(int indentLevel) {
        this.indentLevel = indentLevel;
        return this;
    }

    public BeautifyOption preserveNewlines(boolean preserveNewlines) {
        this.preserveNewlines = preserveNewlines;
        return this;
    }

    public BeautifyOption maxPreserveNewlines(int maxPreserveNewlines) {
        this.maxPreserveNewlines = maxPreserveNewlines;
        return this;
    }

    public BeautifyOption spaceInParen(boolean spaceInParen) {
        this.spaceInParen = spaceInParen;
        return this;
    }

    public BeautifyOption spaceInEmptyParen(boolean spaceInEmptyParen) {
        this.spaceInEmptyParen = spaceInEmptyParen;
        return this;
    }

    public BeautifyOption jslintHappy(boolean jslintHappy) {
        this.jslintHappy = jslintHappy;
        return this;
    }

    public BeautifyOption spaceAfterAnonFunction(boolean spaceAfterAnonFunction) {
        this.spaceAfterAnonFunction = spaceAfterAnonFunction;
        return this;
    }

    private BeautifyOption braceStyle(String braceStyle) {
        this.braceStyle = braceStyle;
        return this;
    }

    public BeautifyOption unindentChainedMethods(boolean unindentChainedMethods) {
        this.unindentChainedMethods = unindentChainedMethods;
        return this;
    }

    public BeautifyOption breakChainedMethods(boolean breakChainedMethods) {
        this.breakChainedMethods = breakChainedMethods;
        return this;
    }

    public BeautifyOption keepArrayIndentation(boolean keepArrayIndentation) {
        this.keepArrayIndentation = keepArrayIndentation;
        return this;
    }

    public BeautifyOption unescapeStrings(boolean unescapeStrings) {
        this.unescapeStrings = unescapeStrings;
        return this;
    }

    public BeautifyOption wrapLineLength(int wrapLineLength) {
        this.wrapLineLength = wrapLineLength;
        return this;
    }

    public BeautifyOption e4x(boolean e4x) {
        this.e4x = e4x;
        return this;
    }

    public BeautifyOption commaFirst(boolean commaFirst) {
        this.commaFirst = commaFirst;
        return this;
    }

    public BeautifyOption operatorPosition(String operatorPosition) {
        this.operatorPosition = operatorPosition;
        return this;
    }

    public Map<String, Object> toFormatArgument() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("indent_size", indentSize);
        options.put("indent_char", indentChar);
        options.put("indent_with_tabs", indentWithTabs);
        options.put("eol", eol);
        options.put("end_with_newline", endWithNewline);
        options.put("indent_level", indentLevel);
        options.put("preserve_newlines", preserveNewlines);
        options.put("max_preserve_newlines", maxPreserveNewlines);
        options.put("space_in_paren", spaceInParen);
        options.put("space_in_empty_paren", spaceInEmptyParen);
        options.put("jslint_happy", jslintHappy);
        options.put("space_after_anon_function", spaceAfterAnonFunction);
        options.put("brace_style", braceStyle);
        options.put("unindent_chained_methods", unindentChainedMethods);
        options.put("break_chained_methods", breakChainedMethods);
        options.put("keep_array_indentation", keepArrayIndentation);
        options.put("unescape_strings", unescapeStrings);
        options.put("wrap_line_length", wrapLineLength);
        options.put("e4x", e4x);
        options.put("comma_first", commaFirst);
        options.put("operator_position", operatorPosition);
        return options;
    }
}
