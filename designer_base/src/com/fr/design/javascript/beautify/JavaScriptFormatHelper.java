package com.fr.design.javascript.beautify;


import com.fr.general.FRLogger;
import com.fr.general.IOUtils;
import com.fr.script.ScriptFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class JavaScriptFormatHelper {

    /**
     * 格式化JavaScript代码
     *
     * @param jsCode 代码内容
     * @return 格式化后的JavaScript代码
     */
    public static String beautify(String jsCode) {
        return beautify(jsCode, BeautifyOption.create());
    }

    /**
     * 格式化JavaScript代码
     *
     * @param jsCode 代码内容
     * @param option 格式化配置参数
     * @return 格式化后的JavaScript代码
     * @see <a href="https://github.com/beautify-web/js-beautify">JSBeautify<a/>
     */
    public static String beautify(String jsCode, BeautifyOption option) {
        InputStream resourceAsStream = IOUtils.readResource("com/fr/design/javascript/beautify/beautify.js");
        ScriptEngine scriptEngine = ScriptFactory.newScriptEngine();
        String result = jsCode;
        try {
            Reader reader = new InputStreamReader(resourceAsStream);
            scriptEngine.eval(reader);
            Invocable invocable = (Invocable) scriptEngine;
            result = (String) invocable.invokeFunction("js_beautify", jsCode, option.toFormatArgument());
        } catch (ScriptException | NoSuchMethodException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(JavaScriptFormatHelper.beautify("var a = function() {return 1;};var b= Math.abs(a);Console.log(b);",
                BeautifyOption.create()));
    }
}