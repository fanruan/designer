package com.fr.design.write.submit.batch;

import com.fr.general.ComparatorUtils;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.js.AbstractJavaScript;
import com.fr.js.Callback;
import com.fr.js.JavaScript;
import com.fr.js.JavaScriptXMLUtils;
import com.fr.json.JSONObject;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;
import com.fr.stable.web.Repository;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;
import com.fr.write.batch.SubmitMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by loy on 16/8/22.
 */
public class BatchCommit2DBJavaScript extends AbstractJavaScript implements Callback {

    public static String RECALCULATE_TAG = "shouldRecalculate";

    /**
     * 回调函数标识
     */
    public static final String CALLBACK = "callback";

    /**
     * 回调参数标识
     */
    public static final String FEEDBACKMAP = "feedbackMap";

    /**
     * javascript所使用的参数
     */
    protected ParameterProvider[] parameters;
    //这个MAP里放的是提交过程生成的参数，比如提交入库，在所有回调事件里，都会将其传递进去，以供解析。
    protected Map<Object, Object> paraMap = new HashMap<Object, Object>();

    private boolean recalculate;

    private List dbManipulationList = new ArrayList();
    //回调函数
    private JavaScript callBack;

    /**
     * 获取数据入库配置信息
     */
    public List getDBManipulation() {
        return dbManipulationList;
    }

    /**
     * 设置数据入库配置信息
     *
     * @param dbManipulationList 数据库配置信息
     */
    public void setDBManipulation(List dbManipulationList) {
        this.dbManipulationList = dbManipulationList;
    }

    /**
     * 回调函数，该函数将在主函数执行完毕以后开始执行
     */
    public JavaScript getCallBack() {
        return callBack;
    }

    /**
     * 设置回调函数
     *
     * @param callback 回调函数
     */
    public void setCallBack(JavaScript callback) {
        this.callBack = callback;
    }

    /**
     * JS响应
     *
     * @param repo 环境
     * @return 返回生成的JS字符串
     */
    public String actionJS(Repository repo) {
        String dmlconf = GeneralXMLTools.writeXMLableAsString(this);
        if (!this.paraMap.isEmpty() && callBack != null) {
            callBack.addParameterMap(paraMap);
        }
        String js = "var fm = this.options.form;if(fm == null) {fm = new FR.BatchForm()};fm.batchCommit({" +
                "xmlconf" + ":" + JSONObject.quote(dmlconf) +
                (callBack != null ? "," + CALLBACK + ":" + JSONObject.quote(GeneralXMLTools.writeXMLableAsString(callBack)) : "") +
                (this.paraMap.isEmpty() ? "" : "," + FEEDBACKMAP + ":" + new JSONObject(paraMap).toString()) +
                "},this)";
        return js;
    }

    /**
     * 转化为字符串
     *
     * @return 返回字符串形式
     */
    public String toString() {
        return (dbManipulationList == null) ? StringUtils.EMPTY : dbManipulationList.toString();
    }

    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);

        if (this.dbManipulationList != null) {
            for (int i = 0; i < dbManipulationList.size(); i++) {
                ((SubmitMain) this.dbManipulationList.get(i)).writeXML(writer);
            }

        }

        if (this.callBack != null) {
            GeneralXMLTools.writeXMLable(writer, this.callBack, JavaScript.XML_TAG);
        }
    }

    public void readXML(XMLableReader reader) {
        super.readXML(reader);

        if (reader.isAttr()) {
            dbManipulationList = new ArrayList();
        } else if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if (JavaScript.XML_TAG.equals(tagName)) {
                this.callBack = JavaScriptXMLUtils.readJavaScript(reader);
            } else {
                if ("Attributes".equals(tagName)) {
                    dbManipulationList.add(new SubmitMain());
                }
                if (dbManipulationList.size() > 0) {
                    ((SubmitMain) dbManipulationList.get(dbManipulationList.size() - 1)).readXML(reader);
                }
            }
        }

    }

    public boolean equals(Object obj) {
        return obj instanceof BatchCommit2DBJavaScript
                && super.equals(obj)
                && ComparatorUtils.equals(((BatchCommit2DBJavaScript) obj).callBack, this.callBack)
                && ComparatorUtils.equals(((BatchCommit2DBJavaScript) obj).dbManipulationList, this.dbManipulationList);
    }

    public Object clone() throws CloneNotSupportedException {
        BatchCommit2DBJavaScript cloned = (BatchCommit2DBJavaScript) super.clone();
        if (this.dbManipulationList != null) {
            cloned.dbManipulationList = new ArrayList();
            for (int i = 0; i < this.dbManipulationList.size(); i++) {
                cloned.dbManipulationList.add(((SubmitMain) this.dbManipulationList.get(i)).clone());
            }
        }

        if (this.callBack != null) {
            cloned.callBack = (JavaScript) this.callBack.clone();
        }

        return cloned;
    }
}
