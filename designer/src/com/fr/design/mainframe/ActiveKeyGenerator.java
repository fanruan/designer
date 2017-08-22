package com.fr.design.mainframe;
import com.fr.base.ConfigManager;
import com.fr.design.DesignerEnvManager;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.core.UUID;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 	限定以"-"分割得到数组长度=4;
 *	至少包含10个以上不同的字符
 *	每7位字符, 要求可以被7整除
 *	限定同一个字符, 不可以出现10次以上
 *	限定不能出现7个字符内全是字母的情况
 * 
 * @author neil
 *
 * @date: 2015-4-8-上午8:49:05
 */
public class ActiveKeyGenerator {
	
	//没网的情况, 返回认证错误, 下次启动再去认证
	public static final int AUTH_ERROR = -1;
	//认证成功
	public static final int AUTH_SUCCESS = 0;
	//认证失败
	public static final int AUTH_FAILED = 1;

	private static final int CONNECT_LEN = 4;
	private static final int KEY_LEN = 5;
	private static final int MIN_NUM_COUNT = 10;
	private static final int MAGIC_NUM = 7;
	private static final String SPLIT_CHAR = "-";
	private static final int MAX_TRY_COUNT = 100;
	
	/**
	 * 生成激活码, 用于从老的激活码生成新的
	 * 
	 * @return 8.0新的激活码
	 * 
	 */
	public static String generateActiveKey(){
		for (int i = 0; i < MAX_TRY_COUNT; i++) {
			String key = UUID.randomUUID().toString();
			
			char[] keyChar = key.toCharArray();
			int len = keyChar.length;
			int[] numArray = new int[len];
			
			if(invalidEachCharCount(len, keyChar, numArray)){
				continue;
			}
			
			if(isCharAllNum(len, numArray, keyChar)){
				continue;
			}
			
			String activeKey = new String(keyChar);
			//跑出来的key, 自己再去认证下
			if(!localVerify(activeKey)){
				continue;
			}
			return activeKey;
		}
		
		return StringUtils.EMPTY;
	}
	
	/**
	 * 验证key合法性, 包括本地和在线验证
	 * 
	 * @param key 激活码
	 * @param timeout 验证超时时间
	 * 
	 * @return 是否合法
	 * 
	 */
	public static boolean verify(String key, int timeout){
		return localVerify(key) && onLineVerify(key, timeout);
	}
	
	/**
	 * 在线校验激活码(超时也算验证通过, 但是会在下次联网时继续验证)
	 * 
	 * @param key 激活码
	 * 
	 * @return 是否验证通过
	 * 
	 */
	public static boolean onLineVerify(String key){
		return onLineVerify(key, -1);
	}
	
	//准备验证的HttpClient
	private static HttpClient prepareVerifyConnect(DesignerEnvManager envManager, int timeout, String key){
		HashMap<String, String> para = new HashMap<String, String>();
		para.put("uuid", envManager.getUUID());
		para.put("key", key);
		para.put("username", ConfigManager.getProviderInstance().getBbsUsername());
		HttpClient hc = new HttpClient(SiteCenter.getInstance().acquireUrlByKind("verify.code"), para);
		if (timeout != -1) {
			hc.setTimeout(timeout);
		}
		
		return hc;
	}
	
	/**
	 * 在线校验激活码(超时也算验证通过, 但是会在下次联网时继续验证)
	 * 
	 * @param key 激活码
	 * @param timeout 超时时间
	 * 
	 * @return 是否验证通过
	 * 
	 */
	public static boolean onLineVerify(String key, int timeout){
		DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
		HttpClient hc = prepareVerifyConnect(envManager, timeout, key);

        //先关闭掉在线验证, 服务器扛不住, 先内网跑并发测试下性能, 确定了问题在开放.
		if (true || !hc.isServerAlive()) {
			//连不上服务器的话, 先算通过, 下次重启还会继续在线验证
			return true;
		}
		
		boolean res = Boolean.valueOf(hc.getResponseText());
		if (res) {
			//联网验证通过了, 就把验证通过的状态存起来, 下次就不用联网验证了.
			envManager.setActiveKeyStatus(AUTH_SUCCESS);
		} else {
			//如果没验证通过, 清空掉当前activekey.当他重启后, 重新输入激活码
			envManager.setActivationKey(StringUtils.EMPTY);
		}
		
		return res;
	}
	
	/**
	 * 本地校验激活码
	 * 
	 * @param key 激活码
	 * 
	 * @return 是否验证通过
	 * 
	 */
	public static boolean localVerify(String key){
		if(StringUtils.isEmpty(key) || invalidSplitLength(key)){
			return false;
		}
		char[] keyChar = key.toCharArray();
		int len = keyChar.length;
		int[] numArray = new int[len];
		
		if(invalidEachCharCount(len, keyChar, numArray)){
			return false;
		}
		
		int count = len / MAGIC_NUM;
		if (count != KEY_LEN) {
			return false;
		}
		//检测余数
		return validRemain(count, numArray);
	}
	
	//是否全字母
	private static boolean isCharAllNum(int len, int[] numArray, char[] keyChar){
		int count = len / MAGIC_NUM;
		for (int j = 0; j < count; j++) {
			int temp = 0;
			for (int k = 0; k < MAGIC_NUM; k++) {
				temp = temp + numArray[k + j * MAGIC_NUM];
			}
			if(temp == 0){
				return true;
			}
			
			updateRemain(temp, numArray, j, keyChar);
		}
		
		return false;
	}
	
	//是否符合规定的split格式
	private static boolean invalidSplitLength(String key){
		return key.split(SPLIT_CHAR).length != CONNECT_LEN;
	}
	
	//获取char对应的int值
	private static int getCharIntValue(char charStr){
		if (!StableUtils.isNum(charStr)) {
			return 0;
		}

		return Character.getNumericValue(charStr);
	}
	
	//校验余数
	private static boolean validRemain(int count, int[] numArray){
		for (int j = 0; j < count; j++) {
			int temp = 0;
			for (int k = 0; k < MAGIC_NUM; k++) {
				temp = temp + numArray[k + j * MAGIC_NUM];
			}
			if (temp == 0){
				return false;
			}
			
			if(temp % MAGIC_NUM != 0){
				return false;
			}
		}
		
		return true;
	}
	
	//判断是否存在不合法(过多)的某一个字符
	private static boolean invalidEachCharCount(int len, char[] keyChar, int[] numArray){
		HashMap<Character, Integer> hs = new HashMap<Character, Integer>();
		for (int j = 0; j < len; j++) {
			int count = hs.containsKey(keyChar[j]) ? hs.get(keyChar[j]) + 1 : 1;
			hs.put(keyChar[j], count);
			numArray[j] = getCharIntValue(keyChar[j]);
		}
		
		if (hs.size() <= MIN_NUM_COUNT) {
			return true;
		}
		
		Iterator<Entry<Character, Integer>> it = hs.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Character, Integer> entry = it.next();
			if(entry.getValue() > MIN_NUM_COUNT){
				return true;
			}
		}
		
		return false;
	}
	
	//更新余数
	private static void updateRemain(int temp, int[] numArray, int j, char[] keyChar){
		//余数
		int remain = temp % MAGIC_NUM;
		int lastMagicIndex = MAGIC_NUM - 1 + j * MAGIC_NUM;
		int newNum = numArray[lastMagicIndex] - remain;
		while(newNum <= 0){
			newNum += MAGIC_NUM;
		}
		
		keyChar[lastMagicIndex] = (char)(newNum + '0');
	}

    public static void main(String[] args) {
        String a = "671b3b43-cfb4-40e7-9ca8-fe71ba33b8e3";

        String key = generateActiveKey();

        boolean verify = localVerify(key);
        System.out.println(verify);
    }
}