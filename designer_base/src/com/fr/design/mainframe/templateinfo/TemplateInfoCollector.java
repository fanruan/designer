package com.fr.design.mainframe.templateinfo;

import com.fr.base.io.IOFile;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;

import java.io.*;
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
//        System.out.println(log);
        // 获取当前编辑的模板
        JTemplate jt = DesignerContext.getDesignerFrame().getSelectedJTemplate();
        // 追加过程记录
        jt.appendProcess(log);
    }

    /**
     * 加载已经存储的模板过程
     */
    public String loadProcess(T t) {
//        return "";
        return (String)templateInfoList.get(t.getReportletsid()).get("process");
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
            System.out.println("写入：" + instance.templateInfoList);
            os.writeObject(instance.templateInfoList);
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

        if (inList(t)) {  // 已有记录
            templateInfo = templateInfoList.get(t.getReportletsid());
        } else {  // 新增
            templateInfo = new HashMap<>();

            String username = DesignerEnvManager.getEnvManager().getBBSName();
            String uuid = DesignerEnvManager.getEnvManager().getUUID();
            String activitykey = DesignerEnvManager.getEnvManager().getActivationKey();
            String reportletsid = t.getReportletsid();
            String createTime = new Date(openTime).toString();
            int reportType = jt.getReportType();
            String jarTime = GeneralUtils.readBuildNO();
            String version = ProductConstants.VERSION;
            templateInfo.put("username", username);
            templateInfo.put("uuid", uuid);
            templateInfo.put("activitykey", activitykey);
            templateInfo.put("reportletsid", reportletsid);
            templateInfo.put("create_time", createTime);
            templateInfo.put("report_type", reportType);
            templateInfo.put("jar_time", jarTime);
            templateInfo.put("version", version);
        }

        long timeConsume = saveTime - openTime;
        // 如果已存有数据，则加上之前的累计编辑时间
        if (templateInfo.get("time_consume") != null) {
            timeConsume += (long)templateInfo.get("time_consume");
        }

        String process = jt.getProcess();
        int cellCount = jt.getCellCount();
        int floatCount = jt.getFloatCount();
        int blockCount = jt.getBlockCount();
        int widgetCount = jt.getWidgetCount();
        templateInfo.put("time_consume", timeConsume);
        templateInfo.put("process", process);
        templateInfo.put("cell_count", cellCount);
        templateInfo.put("float_count", floatCount);
        templateInfo.put("block_count", blockCount);
        templateInfo.put("widget_count", widgetCount);
        templateInfoList.put(t.getReportletsid(), templateInfo);

        saveInfo();  // 每次更新之后，都同步到暂存文件中
    }

    public static void main(String[] args) {
        TemplateInfoCollector tic = TemplateInfoCollector.getInstance();
        tic.getInfoList();
    }
}
