package com.fr.design.utils;

import com.fr.base.BaseUtils;
import com.fr.base.frpx.util.ImageIOHelper;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.ImageWithSuffix;
import com.fr.stable.CoreGraphHelper;
import com.fr.stable.StringUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 * 设计器部分的图片处理工具类
 * Created by zack on 2018/3/8.
 */
public class ImageUtils {
    public static final String TYPE_JPEG = "JPEG";
    public static final String TYPE_PNG = "png";

    /**
     * 默认压缩算法,采用75%质量压缩,带透明度的png默认使用缩放的方式实现压缩尺寸压缩50%,大小大约为1/4
     *
     * @param imageFile 原文件
     * @return 压缩后的BufferedImage对象
     */
    public static Image defaultImageCompress(File imageFile) {
        if (imageFile == null || !imageFile.exists()) {
            return null;
        }
        try {
            BufferedImage srcImg = BaseUtils.readImage(imageFile.getPath());
            if (canbeCompressedToJPEG(imageFile)) {
                return jpegCompress(srcImg, 0.75f);
            } else if (isPNGType(imageFile)) {
                //带透明度的采用缩放的方式
                return scale(srcImg, 0.5f, true);
            }
        } catch (IOException e) {
            FRLogger.getLogger().info("image compress failed!");

        }
        return BaseUtils.readImage(imageFile.getPath());
    }

    /**
     * 默认压缩算法,返回带格式的image
     *
     * @param imageFile 原文件
     * @return 压缩后的BufferedImage对象
     */
    public static ImageWithSuffix defaultImageCompWithSuff(File imageFile) {
        if (imageFile == null || !imageFile.exists()) {
            return null;
        }
        BufferedImage srcImg = BaseUtils.readImage(imageFile.getPath());
        BufferedImage desImg = srcImg;
        try {

            if (canbeCompressedToJPEG(imageFile)) {
                return new ImageWithSuffix(jpegCompress(srcImg, 0.75f), TYPE_JPEG);
            } else if (isPNGType(imageFile)) {
                //带透明度的采用缩放的方式
                desImg = scale(srcImg, 0.5f, true);
            }
        } catch (IOException e) {
            FRLogger.getLogger().info("image compress failed!");

        }
        return new ImageWithSuffix(desImg, TYPE_PNG);
    }

    public static boolean canbeCompressedToJPEG(File imageFile) {
        String imageType = getImageType(imageFile);
        if (ComparatorUtils.equals(imageType, TYPE_JPEG)) {//JPEG大写
            return true;
        }
        if (ComparatorUtils.equals(imageType, TYPE_PNG)) {//png小写
            return !isAlphaAreaOverload(imageFile);//少量透明度系数的png直接压缩jpg
        }
        return false;
    }

    /**
     * 判断图片是否是png类型
     *
     * @param imageFile
     * @return
     */
    public static boolean isPNGType(File imageFile) {
        if (ComparatorUtils.equals(getImageType(imageFile), TYPE_PNG)) {
            return true;
        }
        return false;
    }

    /**
     * JPEG格式图片压缩
     *
     * @param image   压缩源图片
     * @param quality 压缩质量，在0-1之间，
     * @return 返回的字节数组
     */
    public static BufferedImage jpegCompress(BufferedImage image, float quality) throws IOException {
        if (image == null) {
            return null;
        }
        // 得到指定Format图片的writer
        Iterator<ImageWriter> iter = ImageIO
                .getImageWritersByFormatName("jpeg");// 得到迭代器
        ImageWriter writer = iter.next();

        // 得到指定writer的输出参数设置(ImageWriteParam )
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩
        iwp.setCompressionQuality(quality); // 设置压缩质量参数
        iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);

        // 开始打包图片，写入byte[]
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageOutputStream ios = null;
        ByteArrayInputStream in = null;
        try {
            image = copy(image, BufferedImage.TYPE_INT_RGB);
            ios = ImageIO.createImageOutputStream(byteArrayOutputStream);
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), iwp);
            in = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            if (ios == null || in == null) {
                throw new IOException("The image file cannot be compressed");
            }
            return ImageIO.read(in);
        } finally {
            writer.dispose();
            byteArrayOutputStream.close();
            if (ios != null) {
                ios.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * 判断图片中是否包含多于5%的透明区域,这个5%随便定的
     *
     * @param imageFile 目标图片
     * @return 含透明度像素占比
     */
    public static boolean isAlphaAreaOverload(File imageFile) {
        if (imageFile == null || !imageFile.isFile()) {
            return false;
        }
        BufferedImage bufferedImage = BaseUtils.readImage(imageFile.getPath());
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        long alphaCount = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = bufferedImage.getRGB(i, j);
                int a = (0xff & (rgb >> 24));
                if (a != 0xff) {
                    alphaCount++;
                }
            }
        }
        return alphaCount / (double) (width * height) > 0.05;
    }

    /**
     * 获取图片类型
     * 先根据ImageReader获取，ImageReader获取不到就拿后缀
     *
     * @param imageFile 图片文件
     * @return 图片类型(JPEG, PNG, GIF)
     */
    public static String getImageType(File imageFile) {
        String imageType = getImageTypeByImageReader(imageFile);
        return StringUtils.EMPTY.equals(imageType) ? ImageIOHelper.getSuffix(imageFile) : imageType;
    }

    /**
     * 根据ImageReader获取图片类型
     *
     * @param imageFile 图片文件
     * @return 图片类型(JPEG, PNG, GIF)
     */
    public static String getImageTypeByImageReader(File imageFile) {
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(imageFile);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                return StringUtils.EMPTY;
            }
            ImageReader reader = iter.next();
            iis.close();
            return reader.getFormatName();
        } catch (IOException ignore) {
        }
        return StringUtils.EMPTY;
    }

    private static BufferedImage copy(BufferedImage img, int imageType) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage newImage = CoreGraphHelper.createBufferedImage(width, height, imageType);
        Graphics g = newImage.createGraphics();

        g.drawImage(img, 0, 0, null);

        g.dispose();

        return newImage;
    }

    /**
     * 缩放图像（按比例缩放）
     *
     * @param srcImg            源图像来源流
     * @param scale             缩放比例。比例大于1时为放大，小于1大于0为缩小
     * @param opacityCompatible 是否处理背景透明
     */
    public static BufferedImage scale(BufferedImage srcImg, float scale, boolean opacityCompatible) {
        int scaleType;
        if (scale < 0) {
            // 自动修正负数
            scale = -scale;
        }

        int width = mul(Integer.toString(srcImg.getWidth(null)), Float.toString(scale)).intValue(); // 得到源图宽
        int height = mul(Integer.toString(srcImg.getHeight(null)), Float.toString(scale)).intValue(); // 得到源图长
        int srcHeight = srcImg.getHeight(null);
        int srcWidth = srcImg.getWidth(null);
        if (srcHeight < height || srcWidth < width) {
            // 放大图片使用平滑模式
            scaleType = Image.SCALE_SMOOTH;
        } else {
            scaleType = Image.SCALE_DEFAULT;
        }
        return CoreGraphHelper.toBufferedImage(scale(srcImg, width, height, opacityCompatible, scaleType));
    }

    private static BigDecimal mul(String v1, String v2) {
        return mul(new BigDecimal(v1), new BigDecimal(v2));
    }

    private static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        return v1.multiply(v2);
    }

    /**
     * 缩放图像（按长宽缩放）
     * 目标长宽与原图不成比例会变形
     *
     * @param srcImg            源图像来源流
     * @param width             目标宽度
     * @param height            目标高度
     * @param opacityCompatible 是否处理背景透明
     * @return {@link Image}
     */
    public static Image scale(BufferedImage srcImg, int width, int height, boolean opacityCompatible, int scaleType) {
        int srcHeight = srcImg.getHeight(null);
        int srcWidth = srcImg.getWidth(null);
        if (srcHeight == height && srcWidth == width) {
            // 源与目标长宽一致返回原图
            return srcImg;
        }
        if (opacityCompatible) {//需要保留透明度背景
            BufferedImage toImg = CoreGraphHelper.createBufferedImage(width, height, srcImg.getType());
            Graphics2D g2d = toImg.createGraphics();
            toImg = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            g2d.dispose();

            g2d = toImg.createGraphics();
            Image from = srcImg.getScaledInstance(width, height, scaleType);
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose();
            return toImg;
        }
        return srcImg.getScaledInstance(width, height, scaleType);
    }
}
