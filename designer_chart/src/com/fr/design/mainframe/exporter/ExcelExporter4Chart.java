package com.fr.design.mainframe.exporter;

import com.fr.base.ExcelUtils;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.design.mainframe.JChart;
import com.fr.general.FRLogger;
import com.fr.general.IOUtils;
import com.fr.stable.CoreGraphHelper;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-22
 * Time: 上午10:47
 */
public class ExcelExporter4Chart extends ImageExporter4Chart {
    private static final int PICTURE_TYPE_PNG = 6 ;
    private static final int DEFAULT_COLUMN_SPAN = 12;
    private static final int DEFAULT_ROW_SPAN = 26;
    private Workbook workbook;
    private ClientAnchor anchor;



    /**
     * 导出
     *
     * @param out   输出流
     * @param chart 图表文件
     * @throws Exception 异常
     */
    public void export(OutputStream out, JChart chart) throws Exception {
        try {
            ChartDesigner designer = chart.getChartDesigner();
            int imageWidth = designer.getArea().getCustomWidth();
            int imageHeight = designer.getArea().getCustomHeight();
            BufferedImage image = CoreGraphHelper.createBufferedImage(imageWidth, (int) imageHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            paintGlyph(g2d,imageWidth,imageHeight,designer);
         	g2d.dispose();
            if (checkExcelExportVersion()) {
                workbook = new XSSFWorkbook();
            }else{
                workbook = new HSSFWorkbook();
            }
            Sheet sheet = workbook.createSheet();
            if(checkExcelExportVersion()){
                anchor = new XSSFClientAnchor(0,0,0,0,1,1,DEFAULT_COLUMN_SPAN,DEFAULT_ROW_SPAN);
            }else{
                anchor = new HSSFClientAnchor(0,0,0,0,(short)1,1,(short)DEFAULT_COLUMN_SPAN,DEFAULT_ROW_SPAN);
            }
            Drawing patriarch = sheet.createDrawingPatriarch();
            patriarch.createPicture(anchor,loadPicture(image));
            workbook.write(out);
          	out.flush();
        }catch (Exception e){
            FRLogger.getLogger().error(e.getMessage());
        }
    }

    private boolean checkExcelExportVersion() {
        return ExcelUtils.checkPOIJarExist();
    }
    	// 加载图片.
	private int loadPicture(BufferedImage bufferedImage)throws IOException {
		ByteArrayOutputStream bos = null;
		try {
			bos = new ByteArrayOutputStream();
			IOUtils.writeImage(bufferedImage, "png", bos);
			//引用这个参数是jdk1.5的版本 XSSFWorkbook.PICTURE_TYPE_PNG, 在1.4下无法编译, 所有手动去掉这个参数.
            return workbook.addPicture(bos.toByteArray(), PICTURE_TYPE_PNG);
		} finally {
			if (bos != null) {
				bos.flush();
				bos.close();
			}
            bufferedImage.flush();
		}
	}
}