package com.fr.design.ui;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-03-05
 */
class ModernUIConstants {

    static final String SCRIPT_STRING = "var arr = \"%s\".split(\".\").reverse();\n" +
            "var create = function(obj, names) {\n" +
            "var name = names.pop();\n" +
            "if (!name) {return;}\n" +
            "if (!obj[name]) {obj[name] = {};}\n" +
            "    create(obj[name], names);\n" +
            "}\n" +
            "create(window, arr);";
}
