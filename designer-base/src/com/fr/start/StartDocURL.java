package com.fr.start;

import java.awt.Desktop;
import java.net.URI;

import com.fr.base.FRContext;
import com.fr.stable.StableUtils;


/**
 * 从windows开始菜单点击exe文件启动html格式的帮助文档,
 * 主要是为了install4j生成.exe文件用的.
 * 为什么不直接在install4j里面启动*.htm文件呢？
 * 主要是在install4j里面启动*.htm只有第一次会新开一个浏览器，以后的会直接改变
 * 第一次新开的浏览器的内容，这样由于我们目前的文档是多个分开的，用户如果想同时
 * 看多个文档，就没有办法了，所以就写了这个.class文件.
 */
public class StartDocURL {
	//
	public static void main(String[] args) {
		//p:必须有路径的URL存在
		if(args == null || args.length < 1) {
    		FRContext.getLogger().error(
			"Can not find the install home, please check it.");
    		return;			
		}

    	try {
    		//p: 判断是否是http协议.
    		if(args[0].toLowerCase().trim().startsWith("http")) {
				//p:启动浏览器
				Desktop.getDesktop().browse(new URI(args[0]));
			} else {
	    		String iHome = StableUtils.getInstallHome();
	        	if (iHome == null) {
	        		FRContext.getLogger().error(
	        				"Can not find the install home, please check it.");
	        		return;
	        	}

				//p:启动浏览器,看本地文件
	    		Desktop.getDesktop().open(new java.io.File(iHome + args[0]));
			}    		
    	} catch (Exception e) {
    		FRContext.getLogger().error(e.getMessage(), e);
    	}
	}
}