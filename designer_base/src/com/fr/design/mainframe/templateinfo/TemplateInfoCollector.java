package com.fr.design.mainframe.templateinfo;

import com.fr.base.FRContext;
import com.fr.base.io.IOFile;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.env.RemoteEnv;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.stable.*;
import org.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 做模板的过程和耗时收集，辅助类
 * Created by plough on 2017/2/21.
 */
public class TemplateInfoCollector<T extends IOFile> implements Serializable {
    private static final String FILE_NAME = "tplInfo.ser";
    private static TemplateInfoCollector instance;
    private HashMap<String, HashMap<String, Object>> templateInfoList;
    private Set<String> removedTemplates;  // 已经从 templateInfoList 中删除过的 id 列表，防止重复收集数据
    private String designerOpenDate;  //设计器最近一次打开日期
    private static final int VALID_CELL_COUNT = 5;  // 有效报表模板的格子数
    private static final int VALID_WIDGET_COUNT = 5;  // 有效报表模板的控件数
    private static final int COMPLETE_DAY_COUNT = 15;  // 判断模板是否完成的天数
    private static final int ONE_THOUSAND = 1000;

    @SuppressWarnings("unchecked")
    private TemplateInfoCollector() {
        templateInfoList = new HashMap<>();
        removedTemplates = new ListSet<>();
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
            } catch (InvalidClassException ex) {
                // 如果 TemplateInfoCollecor 类结构有改动，则放弃之前收集的数据（下次保存时覆盖）
                // 这种情况主要在开发、测试过程中遇到，正式上线后不应该出现
                FRLogger.getLogger().error(ex.getMessage());
                FRLogger.getLogger().info("use a new instance");
                instance = new TemplateInfoCollector();
            }
            catch (Exception ex) {
                FRLogger.getLogger().error(ex.getMessage(), ex);
            }
        }
        return instance;
    }

    private boolean shouldCollectInfo(T t) {
        if (FRContext.getCurrentEnv() instanceof RemoteEnv  // 远程设计不收集数据
                || t.getTemplateID() == null
                || instance.removedTemplates.contains(t.getTemplateID())) {  // 旧模板
            return false;
        }
        return DesignerEnvManager.getEnvManager().isJoinProductImprove() && FRContext.isChineseEnv();
    }

    public void appendProcess(T t, String log) {
        if (!shouldCollectInfo(t)) {
            return;
        }
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
        HashMap<String, Object> processMap = (HashMap<String, Object>) templateInfoList.get(t.getTemplateID()).get("processMap");
        return (String)processMap.get("process");
    }

    /**
     * 根据模板ID是否在收集列表中，判断是否需要收集当前模板的信息
     */
    public boolean inList(T t) {
        return templateInfoList.containsKey(t.getTemplateID());
    }

    /**
     * 将包含所有信息的对象保存到文件
     */
    private void saveInfo() {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(getInfoFile()));
            String log = "";
            int count = 1;
            for (String key : templateInfoList.keySet()) {
                String createTime = ((HashMap)templateInfoList.get(key).get("consumingMap")).get("create_time").toString();
                log += (count + ". id: " + key + " " + createTime + "\n" + templateInfoList.get(key).toString() + "\n");
                count ++;
            }
            FRLogger.getLogger().info("writing tplInfo: \n" + log);
            os.writeObject(instance);
            os.close();
        } catch (Exception ex) {
            FRLogger.getLogger().error(ex.getMessage());
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
        if (!shouldCollectInfo(t)) {
            return;
        }

        HashMap<String, Object> templateInfo;

        long timeConsume = ((saveTime - openTime) / ONE_THOUSAND);  // 制作模板耗时（单位：s）
        String templateID = t.getTemplateID();

        if (inList(t)) { // 已有记录
            templateInfo = templateInfoList.get(t.getTemplateID());
            // 更新 conusmingMap
            HashMap<String, Object> consumingMap = (HashMap<String, Object>) templateInfo.get("consumingMap");
            timeConsume += (long)consumingMap.get("time_consume");  // 加上之前的累计编辑时间
            consumingMap.put("time_consume", timeConsume);
        }
        else {  // 新增
            templateInfo = new HashMap<>();
            templateInfo.put("consumingMap", getNewConsumingMap(templateID, openTime, timeConsume));
        }

        // 直接覆盖 processMap
        templateInfo.put("processMap", getProcessMap(templateID, jt));

        // 保存模板时，让 day_count 归零
        templateInfo.put("day_count", 0);


        templateInfoList.put(templateID, templateInfo);

        saveInfo();  // 每次更新之后，都同步到暂存文件中
    }

    private HashMap<String, Object> getNewConsumingMap(String templateID, long openTime, long timeConsume) {
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
        consumingMap.put("templateID", templateID);
        consumingMap.put("create_time", createTime);
        consumingMap.put("time_consume", timeConsume);
        consumingMap.put("jar_time", jarTime);
        consumingMap.put("version", version);

        return consumingMap;
    }

    private HashMap<String, Object> getProcessMap(String templateID, JTemplate jt) {
        HashMap<String, Object> processMap = new HashMap<>();

        processMap.put("templateID", templateID);
        processMap.put("process", jt.getProcess());

        TemplateProcessInfo info = jt.getProcessInfo();
        processMap.put("report_type", info.getReportType());
        processMap.put("cell_count", info.getCellCount());
        processMap.put("float_count", info.getFloatCount());
        processMap.put("block_count", info.getBlockCount());
        processMap.put("widget_count", info.getWidgetCount());

        return processMap;
    }

    /**
     * 发送本地模板信息到服务器
     */
    public void sendTemplateInfo() {
        addDayCount();
        String consumingUrl = SiteCenter.getInstance().acquireUrlByKind("tempinfo.consuming") + "/single";
        String processUrl = SiteCenter.getInstance().acquireUrlByKind("tempinfo.process") + "/single";
        ArrayList<HashMap<String, String>> completeTemplatesInfo = getCompleteTemplatesInfo();
        for (HashMap<String, String> templateInfo : completeTemplatesInfo) {
            String jsonConsumingMap = templateInfo.get("jsonConsumingMap");
            String jsonProcessMap = templateInfo.get("jsonProcessMap");
            if (sendSingleTemplateInfo(consumingUrl, jsonConsumingMap) && sendSingleTemplateInfo(processUrl, jsonProcessMap)) {
                // 清空记录
                FRLogger.getLogger().info("successfully send " + templateInfo.get("templateID"));
                removeFromTemplateInfoList(templateInfo.get("templateID"));
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
            if ((int)templateInfo.get("day_count") <= COMPLETE_DAY_COUNT) {  // 未完成模板
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
            jsonTemplateInfo.put("templateID", key);
            completeTemplatesInfo.add(jsonTemplateInfo);
        }
        // 删除测试模板
        for (String key : testTemplateKeys) {
            removeFromTemplateInfoList(key);
        }
        return completeTemplatesInfo;
    }

    private void removeFromTemplateInfoList(String key) {
        templateInfoList.remove(key);
        removedTemplates.add(key);
        FRLogger.getLogger().info(key + " is removed...");
        FRLogger.getLogger().info("removedTemplates: " + removedTemplates);
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
            isTestTemplate = cellCount <= VALID_CELL_COUNT && floatCount <= 1 && widgetCount <= VALID_WIDGET_COUNT;
        } else if (reportType == 1) {  // 聚合报表
            isTestTemplate = blockCount <= 1 && widgetCount <= VALID_WIDGET_COUNT;
        } else {  // 表单(reportType == 2)
            isTestTemplate = widgetCount <= 1;
        }
        return isTestTemplate;
    }

    public static void main(String[] args) {
        TemplateInfoCollector tic = TemplateInfoCollector.getInstance();
        tic.sendTemplateInfo();
    }
}
