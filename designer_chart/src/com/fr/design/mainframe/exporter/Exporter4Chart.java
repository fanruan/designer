package com.fr.design.mainframe.exporter;
import com.fr.design.mainframe.JChart;

import java.io.OutputStream;

/**
 * 图表设计器crt文件的导出成其他类型文件的接口
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-21
 * Time: 下午7:14
 */
public interface Exporter4Chart {

    /**
   	 * 将结果crt导出成目标文件
   	 *
   	 * @param out      输出流
   	 * @param chart    chart文件
   	 * @throws Exception 导出失败则抛出此异常
   	 */
   	public void export(OutputStream out, JChart chart) throws Exception;

}