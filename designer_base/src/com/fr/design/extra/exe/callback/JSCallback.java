package com.fr.design.extra.exe.callback;

import com.fr.stable.StringUtils;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ibm on 2017/5/27.
 */
public class JSCallback {

    private JSExecutor executeScript;

    public JSCallback(final WebEngine webEngine, final JSObject callback) {
        init(webEngine, callback);
    }

    public void init(final WebEngine webEngine, final JSObject callback){
        executeScript = new JSExecutor() {
            @Override
            public void executor(final String newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        String fun = "(" + callback + ")(\"" + trimText(newValue) + "\")";
                        try {
                            webEngine.executeScript(fun);
                        } catch (Exception e) {
                            webEngine.executeScript("alert(\"" + e.getMessage() + "\")");
                        }
                    }
                });
            }
        };
    }

    public void execute(String newValue) {
        executeScript.executor(newValue);
    }


    /**
     * vito:由于使用webEngine.executeScript("(" + callback + ")(\"" + newValue + "\")")
     * 执行脚本，所以原来规范的json格式也会在拼接字符串后可能抛出参数异常，需要转换掉一些会造成错误的特殊字符，
     * 选择在java端替换的原因是异常抛出自executeScript方法的参数.
     * <p>
     * 1.""中的""必须转义
     * 2.js字符串中的\n会导致js字符串变成多行,而js字符串不支持多行拼接
     * 3.由JSONObject.toString()得到的字符串中html标签的属性会自动加上\造成替换难度加大，
     * 这边建议去除所有的html标签
     * 字符\在java中实际存储的是\\,替换字符串\\n, 需要用\\\\n
     * "\t"和"\n" 都要转义成" " 不然会解析出错
     * "\\"需要转换成"\\\"
     *  过滤掉html标签及内容
     *
     * @param old 原始字符串
     * @return 处理之后的字符串
     */
    private String trimText(String old) {
        if (StringUtils.isNotBlank(old)) {
            String b = filterHtmlTag(old);
            return b.replaceAll("\\\\n", StringUtils.EMPTY).replaceAll("\\\\t", StringUtils.EMPTY).replaceAll("\"", "\\\\\"").replaceAll("\'", "\\\\\'").replaceAll("\\\\\\\\", "\\\\\\\\\\\\");
        }
        return StringUtils.EMPTY;
    }

    /**
     * 进行html标签过滤
     * @param origin 原始字符串
     * @return 处理之后的字符串
     */
    private String filterHtmlTag(String origin) {
        String regHtml = "<[^>]+>";
        Pattern patternHtml = Pattern.compile(regHtml, Pattern.CASE_INSENSITIVE);
        Matcher matchHtml = patternHtml.matcher(origin);
        origin = matchHtml.replaceAll(StringUtils.EMPTY);
        return origin;
    }

}

