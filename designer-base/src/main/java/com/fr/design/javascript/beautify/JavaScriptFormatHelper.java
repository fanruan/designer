package com.fr.design.javascript.beautify;


import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.utils.V8ObjectUtils;
import com.fr.general.IOUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.EncodeConstants;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

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
        String result = jsCode;
        V8 v8 = V8.createV8Runtime();
        try {
            v8.executeVoidScript(IOUtils.inputStream2String(resourceAsStream, EncodeConstants.ENCODING_UTF_8));
            V8Array parameters = new V8Array(v8);
            parameters.push(jsCode);
            V8Object arg = V8ObjectUtils.toV8Object(v8, option.toFormatArgument());
            parameters.push(arg);
            result = v8.executeStringFunction("js_beautify_global", parameters);
            parameters.release();
            arg.release();
        } catch (UnsupportedEncodingException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } finally {
            v8.release(true);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(JavaScriptFormatHelper.beautify("var a = function() {return 1;};var b= Math.abs(a);Console.log(b);",
                BeautifyOption.create()));
    }
}