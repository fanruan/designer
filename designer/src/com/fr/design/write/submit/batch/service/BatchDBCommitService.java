package com.fr.design.write.submit.batch.service;

import com.fr.base.*;
import com.fr.data.NetworkHelper;
import com.fr.data.core.db.DBUtils;
import com.fr.data.impl.NameDatabaseConnection;
import com.fr.design.write.submit.batch.BatchCommit2DBJavaScript;
import com.fr.form.ui.WebContentUtils;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.js.JavaScript;
import com.fr.json.JSONException;
import com.fr.json.JSONFunction;
import com.fr.json.JSONObject;
import com.fr.script.Calculator;
import com.fr.stable.ColumnRow;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;
import com.fr.stable.UtilEvalError;
import com.fr.stable.fun.impl.NoSessionIDOPService;
import com.fr.stable.script.NameSpace;
import com.fr.web.RepositoryDeal;
import com.fr.web.core.ReportSessionIDInfor;
import com.fr.web.core.SessionDealWith;
import com.fr.web.core.SessionIDInfor;
import com.fr.web.utils.WebUtils;
import com.fr.write.batch.SubmitMain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by loy on 16/8/23.
 */
public class BatchDBCommitService extends NoSessionIDOPService {
    private static BatchDBCommitService service = null;

    public static final String ARG_XMLCONF = "xmlconf";
    public static final String ARG_SESSIONID = "sessionID";

    /**
     * 构造
     *
     * @return
     */
    public static BatchDBCommitService getInstance() {
        if (service == null) {
            service = new BatchDBCommitService();
        }

        return service;
    }

    /**
     * OP值
     *
     * @return 返回OP值
     */
    public String actionOP() {
        return "batchcommit";
    }

    private Map sessionMap = new HashMap();

    /**
     * 提交入库请求处理
     *
     * @param req 请求
     * @param res 响应
     * @throws Exception 抛出异常
     */
    public void process(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String sessionID = WebUtils.getHTTPRequestParameter(req, ARG_SESSIONID);
        CountLock lock = getCountLock(sessionID);
        try {
            synchronized (lock) {
                process0(req, res);
            }
        } finally {
            releaseLock(sessionID, lock);
        }

    }

    private void releaseLock(String sessionID, CountLock lock) {
        synchronized (sessionMap) {
            lock.reduce();
            if (lock.getCount() == 0) {
                sessionMap.remove(sessionID);
            }
        }
    }

    private CountLock getCountLock(String sessionID) {
        CountLock lock;
        synchronized (sessionMap) {
            lock = (CountLock) sessionMap.get(sessionID);
            if (lock == null) {
                lock = new CountLock();
                sessionMap.put(sessionID, lock);
            }
            lock.increase();
        }
        return lock;
    }

    private void process0(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String dbManiXML = NetworkHelper.getHTTPRequestEncodeParameter(req, ARG_XMLCONF, false);
        if (dbManiXML != null) {
            BatchCommit2DBJavaScript commit2DBJS = (BatchCommit2DBJavaScript) GeneralXMLTools.readStringAsXMLable(dbManiXML);
            Calculator ca = Calculator.createCalculator();
            RepositoryDeal repo = prepareRepository(req, ca);
            NameSpace ns = ParameterMapNameSpace.create(WebUtils.parameters4SessionIDInfor(req));
            ca.pushNameSpace(ns);
            Map<String, Object> feedbackMap = prepareFeedBackMap(req, ca);
            boolean success = true;
            JSONObject fr_submitinfo = new JSONObject();
            Map<String, Connection> connectionMap = new HashMap<String, Connection>();
            try {
                String location = WebUtils.getHTTPRequestParameter(req, "location");
                ColumnRow cr = ColumnRow.valueOf(location);
                for (int i = 0; i < commit2DBJS.getDBManipulation().size(); i++) {
                    if (cr != ColumnRow.ERROR) {
                        ca.setCurrentFromColumnRow(cr);// commit的时候, 也会去set关联格子的ColumnRow, 为防止上一个提交对下一个造成影响, 这边每次重置
                    }
                    SubmitMain dbManipulation = (SubmitMain) commit2DBJS.getDBManipulation().get(i);
                    if (dbManipulation != null && dbManipulation.getDmlConfig() != null) {
                        String dbName = dbManipulation.getDBName(ca);
                        Connection conn = createConnection(dbName, connectionMap);
                        ca.putConnection(dbName, conn);
                        dbManipulation.doJob(ca);
                        connectionMap.put(dbName, conn);
                    }
                }
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
                Iterator iter = connectionMap.entrySet().iterator();
                while (iter.hasNext()) {
                    DBUtils.rollback((Connection) ((Map.Entry) iter.next()).getValue());
                }
                success = false;
                fr_submitinfo.put("failinfo", e.getMessage());
            } finally {
                DBUtils.commitConnections(connectionMap);
            }
            ca.removeNameSpace(ns);
            JSONObject jo = new JSONObject();
            createJo(req, jo, feedbackMap, success, fr_submitinfo, repo);
            java.io.PrintWriter writer = WebUtils.createPrintWriter(res);
            writer.write(jo.toString());
            writer.flush();
            writer.close();
        }
    }

    private Connection createConnection(String dbName, Map<String, Connection> connectionMap) throws Exception {
        Connection conn = null;
        if (connectionMap.containsKey(dbName)) {
            return connectionMap.get(dbName);
        } else {
            NameDatabaseConnection db = new NameDatabaseConnection(dbName);
            return db.createConnection();
        }
    }

    private RepositoryDeal prepareRepository(HttpServletRequest req, Calculator ca) {
        String sessionID = WebUtils.getHTTPRequestParameter(req, ARG_SESSIONID);
        SessionIDInfor sessionIDInfor = SessionDealWith.getSessionIDInfor(sessionID);

        RepositoryDeal repo = null;
        if (sessionIDInfor != null) {
            repo = new RepositoryDeal(req, sessionIDInfor);
            if (sessionIDInfor instanceof ReportSessionIDInfor) {
                dealWithParaForCa(req, ca, (ReportSessionIDInfor) sessionIDInfor);
            }

            NameSpace sessionNamespace = sessionIDInfor.asNameSpace(sessionID);
            ca.pushNameSpace(sessionNamespace);
        }

        return repo;
    }

    private Map<String, Object> prepareFeedBackMap(HttpServletRequest req, Calculator ca) {
        String feedback = WebUtils.getHTTPRequestParameter(req, "feedbackMap");
        Map feedbackMap = null;
        if (StringUtils.isNotEmpty(feedback)) {
            try {
                feedbackMap = new JSONObject(feedback).toMap();
            } catch (JSONException e) {
                FRContext.getLogger().error(e.getMessage());
            }
            NameSpace feedbackNS = ParameterMapNameSpace.create(feedbackMap);
            ca.pushNameSpace(feedbackNS);
        }

        return feedbackMap;
    }

    private void dealWithParaForCa(HttpServletRequest req, Calculator ca, ReportSessionIDInfor sessionIDInfor) {
        ca.setAttribute(Calculator.SHEET_NUMBER_KEY, WebUtils.getHTTPRequestParameter(req, "sheetNum"));
        sessionIDInfor.setUpAttribute4dbCommit(ca);

        // 提交入库可能用当前模板的参数
        NameSpace paras = ParameterMapNameSpace.create(sessionIDInfor.getParameterMap4Execute());
        ca.pushNameSpace(paras);
    }

    private void createJo(HttpServletRequest req, JSONObject jo, Map feedbackMap,
                          boolean success, JSONObject fr_submitinfo, RepositoryDeal repo) throws Exception {
        // 生成的时候没有encode的，用没有进行urldecode的方法获取callback语句，防止+和%出错
        String callBackXML = NetworkHelper.getHTTPRequestEncodeParameter(req, "callback", false);
        JavaScript callBack = (JavaScript) GeneralXMLTools.readStringAsXMLable(callBackXML);
        fr_submitinfo.put("success", success);

        if (callBack != null) {
            dealWithSuccessPara(callBack, fr_submitinfo, repo);
            if (feedbackMap != null) {
                callBack.addParameterMap(feedbackMap);
            }
            jo.put("callback", new JSONFunction(new String[]{"res"}, callBack.createJS(repo), repo.getDevice()));
        }
    }

    private void dealWithSuccessPara(JavaScript callBack, JSONObject fr_submitinfo, RepositoryDeal repo) {
        Map submitInfo = new HashMap();
        submitInfo.put(WebContentUtils.FR_SUBMITINFO, fr_submitinfo);
        if (callBack != null) {
            callBack.addParameterMap(submitInfo);
            ParameterProvider[] ps = new ParameterProvider[callBack.getParameters().length + 1];
            int len = callBack.getParameters().length;
            for (int i = 0; i < len; i++) {
                ps[i] = callBack.getParameters()[i];
            }
            ps[len] = new Parameter(WebContentUtils.FR_SUBMITINFO, fr_submitinfo);
            callBack.setParameters(ps);
        }

        // if the parameter contains "fr_submitinfo" then recalculate it
        ParameterProvider[] paras;
        if (callBack != null) {
            paras = callBack.getParameters();
        } else {
            paras = new Parameter[0];
        }
        Calculator ca = Calculator.createCalculator();
        if (repo != null) {
            NameSpace ns = ParameterMapNameSpace.create(repo.getReportParameterMap());
            ca.pushNameSpace(ns);
        }
        ca.pushNameSpace(ParameterMapNameSpace.create(submitInfo));

        for (int i = 0; i < paras.length; i++) {
            Object obj = paras[i].getValue();
            if (obj instanceof Formula && needToRecalculate(paras[i])) {
                try {
                    ((Formula) obj).setResult(ca.eval((Formula) obj));
                } catch (UtilEvalError utilEvalError) {

                }
            }
        }
    }

    private boolean needToRecalculate(ParameterProvider p) {
        return Utils.objectToString(p.getValue()).toLowerCase().indexOf(WebContentUtils.FR_SUBMITINFO) != -1;
    }
}


class CountLock {
    private int count;

    public void increase() {
        count++;
    }

    public void reduce() {
        count--;
    }

    public int getCount() {
        return count;
    }
}
