package com.fr.design.mainframe.templateinfo;

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
import java.util.HashMap;

/**
 * 做模板的过程和耗时收集，辅助类
 * Created by plough on 2017/2/21.
 */
public class TemplateInfoCollector<T extends IOFile> implements Serializable {
    private static final String FILE_NAME = "tplInfo.ser";
    private static TemplateInfoCollector instance;
    private HashMap<String, HashMap<String, Object>> templateInfoList;
    private String designerOpenDate;  //设计器最近一次打开日期

    @SuppressWarnings("unchecked")
    private TemplateInfoCollector() {
        templateInfoList = new HashMap<>();
        setDesignerOpenDate();
    }

    /**
     * 把设计器最近打开日期设定为当前日期
     */
    private void setDesignerOpenDate() {
        designerOpenDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    /**
     * 判断今天是否第一次打开设计器
     */
    private boolean designerOpenFirstTime() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        return !ComparatorUtils.equals(today, designerOpenDate);
    }

    /**
     * 获取缓存文件存放路径
     */
    private static File getInfoFile() {
        return new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), FILE_NAME));
    }

    public static TemplateInfoCollector getInstance() {
        if (instance == null) {
            // 先尝试从文件读取
            try{
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(getInfoFile()));
                instance = (TemplateInfoCollector) is.readObject();
            } catch (FileNotFoundException ex) {
                // 如果之前没有存储过，则创建新对象
                instance = new TemplateInfoCollector();
            } catch (Exception ex) {
                FRLogger.getLogger().error(ex.getMessage(), ex);
            }
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

    /**
     * 将包含所有信息的对象保存到文件
     */
    private void saveInfo() {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(getInfoFile()));
//            System.out.println("写入：" + instance.templateInfoList);
            os.writeObject(instance);
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 更新 day_count：打开设计器却未编辑模板的连续日子
     */
    private void addDayCount() {
        if (designerOpenFirstTime()) {
            for (String key : templateInfoList.keySet()) {
                HashMap<String, Object> templateInfo = templateInfoList.get(key);
                int dayCount = (int)templateInfo.get("day_count") + 1;
                templateInfo.put("day_count", dayCount);
            }
            setDesignerOpenDate();
        }
    }

    /**
     * 收集模板信息。如果之前没有记录，则新增；如果已有记录，则更新。
     * 同时将最新数据保存到文件中。
     */
    @SuppressWarnings("unchecked")
    public void collectInfo(T t, JTemplate jt, long openTime, long saveTime) {
        HashMap<String, Object> templateInfo;

        long timeConsume = ((saveTime - openTime) / 1000);  // 制作模板耗时（单位：s）
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

        // 保存模板时，让 day_count 归零
        templateInfo.put("day_count", 0);


        templateInfoList.put(reportletsid, templateInfo);

        saveInfo();  // 每次更新之后，都同步到暂存文件中
    }

    private HashMap<String, Object> getNewConsumingMap(String reportletsid, long openTime, long timeConsume) {
        HashMap<String, Object> consumingMap = new HashMap<>();

        String username = DesignerEnvManager.getEnvManager().getBBSName();
        String uuid = DesignerEnvManager.getEnvManager().getUUID();
        String activitykey = DesignerEnvManager.getEnvManager().getActivationKey();
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
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
        addDayCount();
        String consumingUrl = "http://cloud.fanruan.com/api/monitor/record_of_reports_consuming/single";
        String processUrl = "http://cloud.fanruan.com/api/monitor/record_of_reports_process/single";
        ArrayList<HashMap<String, String>> completeTemplatesInfo = getCompleteTemplatesInfo();
        for (HashMap<String, String> templateInfo : completeTemplatesInfo) {
            String jsonConsumingMap = templateInfo.get("jsonConsumingMap");
            String jsonProcessMap = templateInfo.get("jsonProcessMap");
            if (sendSingleTemplateInfo(consumingUrl, jsonConsumingMap) && sendSingleTemplateInfo(processUrl, jsonProcessMap)) {
                // 清空记录
//                System.out.println("success");
                templateInfoList.remove(templateInfo.get("reportletsid"));
            }
        }
        saveInfo();
    }

    private boolean sendSingleTemplateInfo(String url, String content) {
        HashMap<String, String> para = new HashMap<>();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        para.put("token", CodeUtils.md5Encode(date, "", "MD5"));
        para.put("content", content);
        HttpClient httpClient = new HttpClient(url, para, true);
        httpClient.setTimeout(5000);
        httpClient.asGet();

        if (!httpClient.isServerAlive()) {
            return false;
        }

        String res =  httpClient.getResponseText();
        boolean success = ComparatorUtils.equals(new JSONObject(res).get("status"), "success");
        return success;
    }

    /**
     * 返回已完成的模板信息
     */
    @SuppressWarnings("unchecked")
    private ArrayList<HashMap<String, String>> getCompleteTemplatesInfo() {
        ArrayList<HashMap<String, String>> completeTemplatesInfo = new ArrayList<>();
        ArrayList<String> testTemplateKeys = new ArrayList<>();  // 保存测试模板的key
        for (String key : templateInfoList.keySet()) {
            HashMap<String, Object> templateInfo = templateInfoList.get(key);
            if ((int)templateInfo.get("day_count") <= 15) {  // 未完成模板
                continue;
            }
            if (isTestTemplate(templateInfo)) {
                testTemplateKeys.add(key);
                continue;
            }
            HashMap<String, Object> consumingMap = (HashMap<String, Object>) templateInfo.get("consumingMap");
            HashMap<String, Object> processMap = (HashMap<String, Object>) templateInfo.get("processMap");
            String jsonConsumingMap = new JSONObject(consumingMap).toString();
            String jsonProcessMap = new JSONObject(processMap).toString();
            HashMap<String, String> jsonTemplateInfo = new HashMap<>();
            jsonTemplateInfo.put("jsonConsumingMap", jsonConsumingMap);
            jsonTemplateInfo.put("jsonProcessMap", jsonProcessMap);
            jsonTemplateInfo.put("reportletsid", key);
            completeTemplatesInfo.add(jsonTemplateInfo);
        }
        // 删除测试模板
        for (String key : testTemplateKeys) {
            templateInfoList.remove(key);
//            System.out.println(key + " is removed...");
        }
        return completeTemplatesInfo;
    }

    @SuppressWarnings("unchecked")
    private boolean isTestTemplate(HashMap<String, Object> templateInfo) {
        HashMap<String, Object> processMap = (HashMap<String, Object>) templateInfo.get("processMap");
        int reportType = (int)processMap.get("report_type");
        int cellCount = (int)processMap.get("cell_count");
        int floatCount = (int)processMap.get("float_count");
        int blockCount = (int)processMap.get("block_count");
        int widgetCount = (int)processMap.get("widget_count");
        boolean isTestTemplate = false;
        if (reportType == 0) {  // 普通报表
            isTestTemplate = cellCount <= 5 && floatCount <= 1 && widgetCount <= 5;
        } else if (reportType == 1) {  // 聚合报表
            isTestTemplate = blockCount <= 1 && widgetCount <= 5;
        } else {  // 表单(reportType == 2)
            isTestTemplate = widgetCount <= 1;
        }
        return isTestTemplate;
    }

    public static void main(String[] args) {
//        TemplateInfoCollector tic = TemplateInfoCollector.getInstance();
//        tic.sendTemplateInfo();
    }
}
