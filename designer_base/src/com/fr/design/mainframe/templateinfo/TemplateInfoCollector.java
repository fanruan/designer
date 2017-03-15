package com.fr.design.mainframe.templateinfo;

import com.fr.base.FRContext;
import com.fr.base.io.IOFile;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.general.http.HttpClient;
import com.fr.stable.*;
import org.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * 做模板的过程和耗时收集，辅助类
 * Created by plough on 2017/2/21.
 */
public class TemplateInfoCollector<T extends IOFile> implements Serializable {
    private static final String FILE_NAME = "tplInfo.ser";
    private static TemplateInfoCollector instance;
    private HashMap<String, HashMap<String, Object>> templateInfoList;

    @SuppressWarnings("unchecked")
    private TemplateInfoCollector() {
        // 先尝试从文件读取
        try{
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(getInfoFile()));
            templateInfoList = (HashMap<String, HashMap<String,Object>>) is.readObject();
        } catch (FileNotFoundException ex) {
            // 如果之前没有存储过，则创建新对象
            templateInfoList = new HashMap<>();
        } catch (Exception ex) {
            FRLogger.getLogger().error(ex.getMessage(), ex);
        }
    }

    /**
     * 获取缓存文件存放路径
     */
    private static File getInfoFile() {
        return new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), FILE_NAME));
    }

    public static TemplateInfoCollector getInstance() {
        if (instance == null) {
            instance = new TemplateInfoCollector();
        }
        return instance;
    }

    public static void appendProcess(String log) {
        // 获取当前编辑的模板
        JTemplate jt = DesignerContext.getDesignerFrame().getSelectedJTemplate();
        // 追加过程记录
        jt.appendProcess(log);
    }

    /**
     * 加载已经存储的模板过程
     */
    @SuppressWarnings("unchecked")
    public String loadProcess(T t) {
        HashMap<String, Object> processMap = (HashMap<String, Object>) templateInfoList.get(t.getReportletsid()).get("processMap");
        return (String)processMap.get("process");
    }

    /**
     * 根据模板ID是否在收集列表中，判断是否需要收集当前模板的信息
     */
    public boolean inList(T t) {
        return templateInfoList.containsKey(t.getReportletsid());
    }

    // 将包含所有信息的对象保存到文件
    private void saveInfo() {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(getInfoFile()));
            System.out.println("写入：" + templateInfoList);
            os.writeObject(templateInfoList);
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap getInfoList() {
        for (String key : templateInfoList.keySet()) {
            System.out.println(templateInfoList.get(key));
        }
        return templateInfoList;
    }

    /**
     * 收集模板信息。如果之前没有记录，则新增；如果已有记录，则更新。
     * 同时将最新数据保存到文件中。
     */
    public void collectInfo(T t, JTemplate jt, long openTime, long saveTime) {
        HashMap<String, Object> templateInfo;

        long timeConsume = saveTime - openTime;  // 制作模板耗时
        String reportletsid = t.getReportletsid();

        if (inList(t)) { // 已有记录
            templateInfo = templateInfoList.get(t.getReportletsid());
            // 更新 conusmingMap
            HashMap<String, Object> consumingMap = (HashMap<String, Object>) templateInfo.get("consumingMap");
            timeConsume += (long)consumingMap.get("time_consume");  // 加上之前的累计编辑时间
            consumingMap.put("time_consume", timeConsume);
        }
        else {  // 新增
            templateInfo = new HashMap<>();
            templateInfo.put("consumingMap", getNewConsumingMap(reportletsid, openTime, timeConsume));
        }

        // 直接覆盖 processMap
        templateInfo.put("processMap", getProcessMap(reportletsid, jt));

        // TODO: 更新模板是否完成的标记


        templateInfoList.put(reportletsid, templateInfo);

        saveInfo();  // 每次更新之后，都同步到暂存文件中
    }

    private HashMap<String, Object> getNewConsumingMap(String reportletsid, long openTime, long timeConsume) {
        HashMap<String, Object> consumingMap = new HashMap<>();

        String username = DesignerEnvManager.getEnvManager().getBBSName();
        String uuid = DesignerEnvManager.getEnvManager().getUUID();
        String activitykey = DesignerEnvManager.getEnvManager().getActivationKey();
//        String createTime = new Date(openTime).toString();
        String createTime = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(Calendar.getInstance().getTime());
        String jarTime = GeneralUtils.readBuildNO();
        String version = ProductConstants.VERSION;
        consumingMap.put("username", username);
        consumingMap.put("uuid", uuid);
        consumingMap.put("activitykey", activitykey);
        consumingMap.put("reportletsid", reportletsid);
        consumingMap.put("create_time", createTime);
        consumingMap.put("time_consume", timeConsume);
        consumingMap.put("jar_time", jarTime);
        consumingMap.put("version", version);

        return consumingMap;
    }

    private HashMap<String, Object> getProcessMap(String reportletsid, JTemplate jt) {
        HashMap<String, Object> processMap = new HashMap<>();

        processMap.put("reportletsid", reportletsid);
        processMap.put("process", jt.getProcess());
        processMap.put("report_type", jt.getReportType());
        processMap.put("cell_count", jt.getCellCount());
        processMap.put("float_count", jt.getFloatCount());
        processMap.put("block_count", jt.getBlockCount());
        processMap.put("widget_count", jt.getWidgetCount());

        return processMap;
    }

    /**
     * 发送本地模板信息到服务器
     */
    public void sendTemplateInfo() {

        String url1 = "http://cloud.fanruan.com/api/monitor/record_of_make_reports/single";
        ArrayList<HashMap<String, String>> completeTemplatesInfo = getCompleteTemplatesInfo();
        for (HashMap<String, String> templateInfo : completeTemplatesInfo) {
            String jsonConsumingMap = templateInfo.get("jsonConsumingMap");
            String jsonProcessMap = templateInfo.get("jsonProcessMap");
            if (sendSingleTemplateInfo(url1, jsonConsumingMap) && sendSingleTemplateInfo(url1, jsonProcessMap)) {
                // TODO: 清空记录
                System.out.println("success");
            }
        }
//        //服务器返回true, 说明已经获取成功, 清空当前记录的信息
//        if (success) {
//            System.out.println("success");
//        } else {
//            System.out.println("fail");
//        }
    }

    private boolean sendSingleTemplateInfo(String url, String content) {
        HashMap<String, String> para = new HashMap<>();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        para.put("token", CodeUtils.md5Encode(date, "", "MD5"));
        para.put("content", content);
//        para.put("content", "{name:3, age:3}");
//        HttpClient httpClient = new HttpClient("http://cloud.fanruan.com/api/monitor/record_of_make_reports/single", para, true);
        HttpClient httpClient = new HttpClient(url, para, true);

        //httpClient.setContent(getCompleteTemplatesInfo());
        httpClient.setTimeout(5000);
        httpClient.asGet();

        if (!httpClient.isServerAlive()) {
            return false;
        }

        String res =  httpClient.getResponseText();
        boolean success = ComparatorUtils.equals(new JSONObject(res).get("status"), "success");
        return success;
    }

    // 返回已完成的模板信息
    @SuppressWarnings("unchecked")
    private ArrayList<HashMap<String, String>> getCompleteTemplatesInfo() {
        ArrayList<HashMap<String, String>> completeTemplatesInfo = new ArrayList<>();
        for (String key : templateInfoList.keySet()) {
            HashMap<String, String> templateInfo = new HashMap<>();
            HashMap<String, Object> consumingMap = (HashMap<String, Object>) templateInfoList.get(key).get("consumingMap");
            HashMap<String, Object> processMap = (HashMap<String, Object>) templateInfoList.get(key).get("processMap");
            String jsonConsumingMap = new JSONObject(consumingMap).toString();
            String jsonProcessMap = new JSONObject(processMap).toString();
            templateInfo.put("jsonConsumingMap", jsonConsumingMap);
            templateInfo.put("jsonProcessMap", jsonProcessMap);
            templateInfo.put("reportletsid", key);
            completeTemplatesInfo.add(templateInfo);  // TODO 暂未添加筛选条件
        }
        return completeTemplatesInfo;
    }



    public static void main(String[] args) {
        TemplateInfoCollector tic = TemplateInfoCollector.getInstance();
//        tic.getInfoList();
        tic.sendTemplateInfo();

    }
}
