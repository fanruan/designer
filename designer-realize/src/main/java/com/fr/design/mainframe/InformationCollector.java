/**
 *
 */
package com.fr.design.mainframe;

import com.fr.base.FRContext;
import com.fr.config.MarketConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.errorinfo.ErrorInfoUploader;
import com.fr.design.mainframe.templateinfo.TemplateInfoCollector;
import com.fr.general.CloudCenter;
import com.fr.general.ComparatorUtils;
import com.fr.general.DateUtils;
import com.fr.general.DesUtils;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;
import com.fr.general.http.HttpToolbox;
import com.fr.intelli.record.FocusPoint;
import com.fr.intelli.record.MetricRegistry;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.EncodeConstants;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.query.QueryFactory;
import com.fr.stable.query.condition.QueryCondition;
import com.fr.stable.query.data.DataList;
import com.fr.stable.query.restriction.RestrictionFactory;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLTools;
import com.fr.stable.xml.XMLWriter;
import com.fr.stable.xml.XMLableReader;
import com.fr.third.javax.xml.stream.XMLStreamException;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author neil
 *
 * @date: 2015-4-8-下午5:11:46
 */
public class InformationCollector implements XMLReadable, XMLWriter {

	// 24小时上传一次
	private static final long DELTA = 24 * 3600 * 1000L;
	private static final long SEND_DELAY = 300 * 1000L;
	private static final String FILE_NAME = "fr.info";
	private static final String XML_START_STOP_LIST = "StartStopList";
	private static final String XML_START_STOP = "StartStop";
	private static final String XML_LAST_TIME = "LastTime";
	private static final String ATTR_START = "start";
	private static final String ATTR_STOP = "stop";
	private static final String XML_JAR = "JarInfo";
	private static final String XML_VERSION = "Version";
	private static final String XML_USERNAME = "Username";
	private static final String XML_UUID = "UUID";
	private static final String XML_KEY = "ActiveKey";
	private static final String XML_OS = "OS";

    public static final String TABLE_NAME = "fr_functionrecord";
    public static final String FUNC_COLUMNNAME = "func";
    public static final String COLUMN_TIME = "time";
    public static final String TABLE_FUNCTION_RECORD = "function.record";
    private static final String ATTR_ID = "id";
    private static final String ATTR_TEXT = "text";
    private static final String ATTR_SOURCE = "source";
    private static final String ATTR_TIME = "time";
    private static final String ATTR_TITLE = "title";
    private static final String ATTR_USER_NAME = "username";
    private static final String ATTR_UUID = "uuid";
	private static final String ATTR_FUNCTION_ARRAY = "functionArray";
	private static final int MAX_EACH_REQUEST_RECORD_COUNT = 200;

	private static InformationCollector collector;

	//启动时间与关闭时间列表
	private List<StartStopTime> startStop = new ArrayList<StartStopTime>();
	//上一次的发送时间
	private String lastTime;
	private StartStopTime current = new StartStopTime();

	public static InformationCollector getInstance(){
		if (collector == null) {
			collector = new InformationCollector();

            readEncodeXMLFile(collector, collector.getInfoFile());
		}

		return collector;
	}

	private static void readEncodeXMLFile(XMLReadable xmlReadable, File xmlFile){
		if (xmlFile == null || !xmlFile.exists()) {
			return;
		}
		String charset = EncodeConstants.ENCODING_UTF_8;
		try {
			String decodeContent = getDecodeFileContent(xmlFile);
			InputStream xmlInputStream = new ByteArrayInputStream(decodeContent.getBytes(charset));
			InputStreamReader inputStreamReader = new InputStreamReader(xmlInputStream, charset);

			XMLableReader xmlReader = XMLableReader.createXMLableReader(inputStreamReader);

			if (xmlReader != null) {
				xmlReader.readXMLObject(xmlReadable);
			}
			xmlInputStream.close();
		} catch (FileNotFoundException e) {
			FRContext.getLogger().error(e.getMessage(), e);
		} catch (IOException e) {
			FRContext.getLogger().error(e.getMessage(), e);
		} catch (XMLStreamException e) {
			FRContext.getLogger().error(e.getMessage(), e);
		}

	}

	private static String getDecodeFileContent(File xmlFile) throws FileNotFoundException, UnsupportedEncodingException{
		InputStream encodeInputStream = new FileInputStream(xmlFile);
		String encodeContent = IOUtils.inputStream2String(encodeInputStream);
		return DesUtils.getDecString(encodeContent);
	}

	private long getLastTimeMillis(){
		if (StringUtils.isEmpty(this.lastTime)) {
			return 0;
		}

		try {
			return DateUtils.string2Date(this.lastTime, true).getTime();
		} catch (Exception e) {
			return -1;
		}

	}

	private JSONObject getJSONContentAsByte(){
		JSONObject content = new JSONObject();

		JSONArray startStopArray = new JSONArray();
		for (int i = 0; i < startStop.size(); i++) {
			JSONObject jo = new JSONObject();
			try {
				jo.put(ATTR_START, startStop.get(i).getStartDate());
				jo.put(ATTR_STOP, startStop.get(i).getStopDate());
				startStopArray.put(jo);
				DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
				content.put(XML_START_STOP, startStopArray);
				content.put(XML_UUID, envManager.getUUID());
				content.put(XML_JAR, GeneralUtils.readBuildNO());
				content.put(XML_VERSION, ProductConstants.RELEASE_VERSION);
				content.put(XML_USERNAME, MarketConfig.getInstance().getBbsUsername());
				content.put(XML_KEY, envManager.getActivationKey());
				content.put(XML_OS, System.getProperty("os.name"));
			} catch (JSONException e) {
				FRContext.getLogger().error(e.getMessage(), e);
			}
		}
		return content;
	}

	private void sendUserInfo(){
		long currentTime = new Date().getTime();
		long lastTime = getLastTimeMillis();

		if (currentTime - lastTime <= DELTA) {
			return;
		}
		JSONObject content = getJSONContentAsByte();
		String url = CloudCenter.getInstance().acquireUrlByKind("user.info.v10");
		boolean success = false;
		try {
			HashMap<String, Object> para = new HashMap<>();
			para.put("token", SiteCenterToken.generateToken());
			para.put("content", content);
			String res = HttpToolbox.post(url, para);
			success = ComparatorUtils.equals(new JSONObject(res).get("status"), "success");
		} catch (Exception e) {
			FineLoggerFactory.getLogger().error(e.getMessage(), e);
		}
		if (success){
			this.reset();
		}
	}

    private void sendFunctionsInfo(){
		Date current = new Date();
		long lastTime = getLastTimeMillis();
		long currentTime = current.getTime();
        if (currentTime - lastTime <= DELTA) {
            return;
        }
		JSONArray content = getFunctionsContent(currentTime, lastTime);
		boolean success = false;
		FineLoggerFactory.getLogger().info("Start sent function records to the cloud center...");
		String url = CloudCenter.getInstance().acquireUrlByKind(TABLE_FUNCTION_RECORD);
		try {
			for(int i=0;i<content.length();i++){
				JSONArray functionArray = content.getJSONArray(i);
				if(functionArray.length() > 0){
					success = sendFunctionRecord(url, functionArray);
				}
			}
			//服务器返回true, 说明已经获取成功, 更新最后一次发送时间
			if (success) {
				this.lastTime = dateToString();
				FineLoggerFactory.getLogger().info("Function records successfully sent to the cloud center.");
			}
		}catch (Exception e) {
			FineLoggerFactory.getLogger().error(e.getMessage(), e);
		}

//      //先将发送压缩文件这段代码注释，之后提任务
		//大数据量下发送压缩zip数据不容易丢失
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//			String contentStr = objectMapper.writeValueAsString(content);
//			InputStream inputStream = new ByteArrayInputStream(contentStr.getBytes("UTF-8"));
//			String recordUrl = url+"?token=" + SiteCenterToken.generateToken() + "&content="+ IOUtils.inputStream2Bytes(IOUtils.toZipIn(inputStream));
//
//			String res = HttpToolbox.get(recordUrl);
//			success = ComparatorUtils.equals(new JSONObject(res).get("status"), "success");
//		} catch (Exception e) {
//			FineLoggerFactory.getLogger().error(e.getMessage(), e);
//		}
//		if (success) {
//			deleteFunctionRecords(currentTime);
//		}
    }

    private boolean sendFunctionRecord(String url, JSONArray record) {
        boolean success = false;
        try {
			HashMap<String, Object> para = new HashMap<>();
			para.put("token", SiteCenterToken.generateToken());
			para.put("content", record);
			String res = HttpToolbox.post(url, para);
			success = ComparatorUtils.equals(new JSONObject(res).get("status"), "success");
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return success;
    }

    /**
     * 收集开始使用时间，发送信息
     */
	public void collectStartTime(){
		this.current.setStartDate(dateToString());

		sendUserInfoInOtherThread();
	}

	private void sendUserInfoInOtherThread(){
		if (!DesignerEnvManager.getEnvManager().isJoinProductImprove() || !FRContext.isChineseEnv()) {
			return;
		}

    	Thread sendThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					//读取XML的5分钟后开始发请求连接服务器.
					Thread.sleep(SEND_DELAY);
				} catch (InterruptedException e) {
					FRContext.getLogger().error(e.getMessage(), e);
				}
                sendUserInfo();
				sendFunctionsInfo();
				TemplateInfoCollector.getInstance().sendTemplateInfo();
				ErrorInfoUploader.getInstance().sendErrorInfo();
			}
		});
    	sendThread.start();
	}

    /**
     * 收集结束使用时间
     */
	public void collectStopTime(){
		this.current.setStopDate(dateToString());
	}

	private String dateToString(){
		DateFormat df = FRContext.getDefaultValues().getDateTimeFormat();
		return df.format(new Date());
	}

	private void reset(){
		this.startStop.clear();
		this.lastTime = dateToString();
	}

    private File getInfoFile() {
    	return new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), FILE_NAME));
    }

    /**
     * 保存xml文件
     */
    public void saveXMLFile() {
    	File xmlFile = this.getInfoFile();
    	try{
    		ByteArrayOutputStream out = new ByteArrayOutputStream();
			XMLTools.writeOutputStreamXML(this, out);
			out.flush();
			out.close();
			String fileContent = new String(out.toByteArray(), EncodeConstants.ENCODING_UTF_8);
			String encodeCotent = DesUtils.getEncString(fileContent);
			writeEncodeContentToFile(encodeCotent, xmlFile);
    	}catch (Exception e) {
    		FRContext.getLogger().error(e.getMessage(), e);
		}
    }


	/**
	 * 将文件内容写到输出流中
	 */
	private static void writeEncodeContentToFile(String fileContent, File file){
		BufferedWriter bw = null;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, EncodeConstants.ENCODING_UTF_8);
			bw = new BufferedWriter(osw);
			bw.write(fileContent);
		} catch (Exception e) {
			FRContext.getLogger().error(e.getMessage(), e);
		} finally {
			if(bw != null){
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
		}

	}

	@Override
	public void writeXML(XMLPrintWriter writer) {
		startStop.add(current);
		writer.startTAG("Info");
		//启停信息
		writeStartStopList(writer);
		//上一次更新的时间
		writeTag(XML_LAST_TIME, this.lastTime, writer);

		writer.end();
	}

	private void writeStartStopList(XMLPrintWriter writer){
		//启停
    	writer.startTAG(XML_START_STOP_LIST);
    	for (int i = 0; i < startStop.size(); i++) {
    		startStop.get(i).writeXML(writer);
		}
    	writer.end();
	}

	private void writeTag(String tag, String content, XMLPrintWriter writer){
		if (StringUtils.isEmpty(content)) {
			return;
		}

    	writer.startTAG(tag);
    	writer.textNode(content);
    	writer.end();
	}

	@Override
	public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
        	String name = reader.getTagName();
            if (XML_START_STOP_LIST.equals(name)) {
            	readStartStopList(reader);
            } else if(XML_LAST_TIME.equals(name)){
            	readLastTime(reader);
			}
        }
	}

	private void readLastTime(XMLableReader reader){
		String tmpVal;
		if (StringUtils.isNotBlank(tmpVal = reader.getElementValue())) {
			this.lastTime = tmpVal;
		}
	}

	private void readStartStopList(XMLableReader reader){
    	startStop.clear();

		reader.readXMLObject(new XMLReadable() {
            public void readXML(XMLableReader reader) {
                if (XML_START_STOP.equals(reader.getTagName())) {
                    StartStopTime startStopTime = new StartStopTime();
                    reader.readXMLObject(startStopTime);
                    startStop.add(startStopTime);
                }
            }
        });
	}

	public JSONArray getFunctionsContent(long current, long last) {
		//记录当前条数，达到200条合并成一个请求
		int count = 0;
		JSONArray functionArray = new JSONArray();
		QueryCondition condition = QueryFactory.create()
				.addRestriction(RestrictionFactory.lte(COLUMN_TIME, current))
				.addRestriction(RestrictionFactory.gte(COLUMN_TIME, last));
		try {
			DataList<FocusPoint> focusPoints = MetricRegistry.getMetric().find(FocusPoint.class,condition);
			TreeSet<FunctionRecord> focusPointsList = new TreeSet<>();
			if(!focusPoints.isEmpty()){
				for(int i=0;i<  focusPoints.getList().size();i++){
					FocusPoint focusPoint = focusPoints.getList().get(i);
					if(focusPoint != null){
						if((++count <= MAX_EACH_REQUEST_RECORD_COUNT)){
							focusPointsList.add(getOneRecord(focusPoint));
						} else {
							count = 0;
							functionArray.put(setToJSONArray(focusPointsList));
							focusPointsList.add(getOneRecord(focusPoint));
						}
						if(i == (focusPoints.getList().size() -1)){
							functionArray.put(setToJSONArray(focusPointsList));
						}
					}
				}
			}

		} catch (Exception e) {
			FineLoggerFactory.getLogger().error(e.getMessage(), e);
		}
		return functionArray;
	}

	private FunctionRecord getOneRecord(FocusPoint focusPoint) {
		FunctionRecord functionRecord = new FunctionRecord();
		functionRecord.setId(focusPoint.getId() == null?StringUtils.EMPTY : focusPoint.getId());
		functionRecord.setText(focusPoint.getText() == null?StringUtils.EMPTY : focusPoint.getText());
		functionRecord.setSource(focusPoint.getSource());
		functionRecord.setTime(focusPoint.getTime().getTime());
		functionRecord.setTitle(focusPoint.getTitle() == null?StringUtils.EMPTY : focusPoint.getTitle());
		functionRecord.setUsername(MarketConfig.getInstance().getBbsUsername() == null?StringUtils.EMPTY : MarketConfig.getInstance().getBbsUsername());
		functionRecord.setUuid(DesignerEnvManager.getEnvManager().getUUID() == null?StringUtils.EMPTY : DesignerEnvManager.getEnvManager().getUUID());
		return functionRecord;
	}

	private JSONArray setToJSONArray(Set set) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		for(Iterator iter = set.iterator(); iter.hasNext(); ) {
			FunctionRecord functionRecord = (FunctionRecord)iter.next();
			com.fr.json.JSONObject record = new com.fr.json.JSONObject();
			record.put(ATTR_ID, functionRecord.getId());
			record.put(ATTR_TEXT, functionRecord.getText());
			record.put(ATTR_SOURCE, functionRecord.getSource());
			record.put(ATTR_TIME, functionRecord.getTime());
			record.put(ATTR_TITLE, functionRecord.getTitle());
			record.put(ATTR_USER_NAME, functionRecord.getUsername());
			record.put(ATTR_UUID, functionRecord.getUuid());
			jsonArray.put(record);
		}
		return jsonArray;
	}

	private class StartStopTime implements XMLReadable, XMLWriter {

		private String startDate;
		private String stopDate;

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getStopDate() {
			return stopDate;
		}

		public void setStopDate(String endDate) {
			this.stopDate = endDate;
		}

		public void writeXML(XMLPrintWriter writer) {
        	writer.startTAG(XML_START_STOP);
        	if (StringUtils.isNotEmpty(startDate)) {
        		writer.attr(ATTR_START, this.startDate);
			}
        	if (StringUtils.isNotEmpty(stopDate)) {
        		writer.attr(ATTR_STOP, this.stopDate);
			}
        	writer.end();
		}

		public void readXML(XMLableReader reader) {
			this.startDate = reader.getAttrAsString(ATTR_START, StringUtils.EMPTY);
			this.stopDate = reader.getAttrAsString(ATTR_STOP, StringUtils.EMPTY);
		}

	}

	private class FunctionRecord implements Comparable{
		private String id;
		private String text;
		private int source;
		private long time;
		private String title;
		private String username;
		private String uuid;

		public FunctionRecord(){

		}
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public int getSource() {
			return source;
		}

		public void setSource(int source) {
			this.source = source;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

		@Override
		public int compareTo(Object o) {
			FunctionRecord functionRecord = (FunctionRecord) o;
			if(this.getId().equals((functionRecord.getId())) && this.getText().equals(functionRecord.getText())
					&& this.getSource() == functionRecord.getSource() && this.getTime() == functionRecord.getTime()
					&& this.getTitle().equals(functionRecord.getTitle()) && this.getUsername().equals(functionRecord.getUsername())
					&& this.getUuid().equals(functionRecord.getUuid())){
				return 0;
			}
			return 1;
		}
	}
}