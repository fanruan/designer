package com.fr.design.mainframe.messagecollect.impl;

import com.fr.design.mainframe.messagecollect.entity.FileEntityBuilder;
import com.fr.design.mainframe.messagecollect.utils.MessageCollectUtils;
import com.fr.intelli.record.MetricRegistry;
import com.fr.json.JSONArray;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.query.QueryFactory;
import com.fr.stable.query.condition.QueryCondition;
import com.fr.stable.query.data.DataList;
import com.fr.stable.query.restriction.RestrictionFactory;
import com.fr.stable.xml.XMLTools;
import com.fr.stable.xml.XMLable;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author alex sung
 * @date 2019/3/22
 */
public abstract class AbstractSendDataToCloud implements XMLable {
    private static final String FILE_NAME = "messagecollect.info";
    private static final String COLUMN_TIME = "time";

    protected String lastTime;
    private static final int PAGE_SIZE = 200;
    private long totalCount = -1;
    private FileEntityBuilder fileEntityBuilder;

    public FileEntityBuilder getFileEntityBuilder() {
        return fileEntityBuilder;
    }

    public void setFileEntityBuilder(FileEntityBuilder fileEntityBuilder) {
        this.fileEntityBuilder = fileEntityBuilder;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public void saveLastTime() {
        setLastTime(MessageCollectUtils.dateToString());
        try {
            FileOutputStream out = new FileOutputStream(getLastTimeFile());
            XMLTools.writeOutputStreamXML(this, out);
        } catch (Exception ex) {
            FineLoggerFactory.getLogger().error(ex.getMessage());
        }
    }

    public static File getLastTimeFile() {
        return new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), FILE_NAME));
    }

    public <T> void queryData(long currentTime, long lastTime, Class<T> tClass) {
        queryAndSendOnePageFunctionContent(currentTime, lastTime, 0, tClass);
        long page = (totalCount / PAGE_SIZE) + 1;
        for (int i = 1; i < page; i++) {
            queryAndSendOnePageFunctionContent(currentTime, lastTime, i, tClass);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private <T> void queryAndSendOnePageFunctionContent(long current, long last, int page, Class<T> tClass) {
        QueryCondition condition = QueryFactory.create()
                .skip(page * PAGE_SIZE)
                .count(PAGE_SIZE)
                .addSort(COLUMN_TIME, true)
                .addRestriction(RestrictionFactory.lte(COLUMN_TIME, current))
                .addRestriction(RestrictionFactory.gte(COLUMN_TIME, last));
        try {
            DataList<T> points = MetricRegistry.getMetric().find(tClass, condition);
            //第一次查询获取总记录数
            if (page == 0) {
                totalCount = points.getTotalCount();
            }
            dealWithData(points);

        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    public <T> void dealWithData(DataList<T> tDataList) throws Exception {
        generateThisPageFile(tDataList);
    }

    private <T> void generateThisPageFile(DataList<T> points) {
        File file = null;
        try {
            JSONArray jsonArray = dealWithSendFunctionContent(points);
            //生成json文件
            fileEntityBuilder.generateFile(jsonArray, getFileEntityBuilder().getPathName());
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    public abstract <T> JSONArray dealWithSendFunctionContent(DataList<T> focusPoints);

    /**
     * 生成zip并发送zip文件
     * @param pathName zip文件路径
     */
    protected void sendZipFile(String pathName) {

        File file = null;
        try {
            file = fileEntityBuilder.generateZipFile(pathName);
            if (file != null) {
                fileEntityBuilder.uploadFile(file, file.getName());
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return;
        }
        fileEntityBuilder.deleteFileAndZipFile(file, pathName);
    }


}
