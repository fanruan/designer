package com.fr.design.gui.date;

import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

/**
 */
public class JDateDocument extends PlainDocument{
    private JTextComponent textComponent; //日期输入文本框
    private SimpleDateFormat dateFormat;

    /******************************************************************
     ** 函数名称：JDateDocument
     ** 功能描述：设置此文本框显示的默认值、格式限制和当前日期
     ** 入口参数：
     ** tc : JTextComponent类型,当前操作的文本框
     ** dateFormat : SimpleDateFormat类型,当前操作文本框的格式限制
     ** initDateTime : String类型,当前日期
     ** 返回值：无
     ** 调用者：类JDateDocument
     *******************************************************************/
    public JDateDocument(JTextComponent tc, SimpleDateFormat dateFormat) throws
        UnsupportedOperationException{
        //当前日期构造
        this(tc, dateFormat, getCurrentDate(dateFormat));
    }

    public JDateDocument(JTextComponent tc,
                         SimpleDateFormat dateFormat,
                         String initDateTime) throws
        UnsupportedOperationException{
        //设置当前日期格式
        setDateFormat(dateFormat);
        //保存操作的文本框
        textComponent = tc;
        //设置显示为当前日期,同时完成显示的格式化
        try{
            insertString(0, initDateTime, null);
        } catch(BadLocationException ex){
            throw new UnsupportedOperationException(ex.getMessage());
        }
    }

    /**
     * 设置当前日期格式
     * @param dateFormat SimpleDateFormat
     */
    public void setDateFormat(SimpleDateFormat dateFormat){
        this.dateFormat = dateFormat;
    }

    /**
     * 取得当前日期格式
     * @return SimpleDateFormat
     */
    public SimpleDateFormat getDateFormat(){
        return dateFormat;
    }

    /**
     * 取得当前系统日时
     * @return String
     */
    public static String getCurrentDate(SimpleDateFormat smFormat){
        return smFormat.format(new Date());
    }

    /******************************************************************
     ** 函数名称：public void insertString(int offset, String s,
     **             AttributeSet attributeSet) throws BadLocationException
     ** 功能描述：重载原方法,限制用户插入格式为日期格式
     ** 入口参数：offset: int型,插入位置
     **            s: String型,插入字符串
     **            attributeSet: AttributeSet型,属性集
     ** 返回值：无
     ** 调用者：类JDateDocument
     *******************************************************************/
    public void insertString(int offset, String s,
                             AttributeSet attributeSet) throws BadLocationException{
        String toTest; //用于测试输入合法性的字符串
        //判断插入字符串长度
        if(s.length() == 1){
            //长度为1
            try{
                //限制输入为整数
                Integer.parseInt(s);
            } catch(Exception ex){
                //错误则提示并返回
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            //取得原始插入位置
            int newOffset = offset;
            //如果插入位置为"/"," ","-"符号的前面,则移动到其后面插入(改变newOffset的值)
            if(offset == 4 || offset == 7 ||
               offset == 10 || offset == 13 ||
               offset == 16){
                newOffset++;
                textComponent.setCaretPosition(newOffset);
            }
            //如果插入位置为最后,则不插入
            if(offset == dateFormat.toPattern().length()){
                return;
            }
            //取得显示的时间,处理后得到要显示的字符串
            toTest = textComponent.getText();
            toTest = toTest.substring(0, newOffset) + s +
                toTest.substring(newOffset + 1);
            //如果要显示的字符串合法,则显示,否则给出提示并退出
            boolean isValid = isValidDate(toTest);
            if(!isValid){
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            //插入字符串
            super.remove(newOffset, 1);
            super.insertString(newOffset, s, attributeSet);
        }
        //如果插入长度10
        else if(s.length() == 10 || s.length() == 19){
            //合法则显示,否则给出提示退出
            if(!isValidDate(s)){
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            //插入字符串
            super.remove(0, getLength());
            super.insertString(0, s, attributeSet);
        }
    }

    /**********************************************************************************
     ** 函数名称：public void remove(int offset, int length) throws BadLocationException
     ** 功能描述：重载原方法,删除合适位置的字符串
     ** 入口参数：offset: int型,插入位置
     **            length: int型,删除长度
     ** 返回值：无
     ** 调用者：insertString(int, String,AttributeSet)
     ***********************************************************************************/
    public void remove(int offset, int length) throws BadLocationException{
        //如果插入位置在"-"前,则回退一个光标位置
        //yyyy-MM-dd HH:mm:ss
        if(offset == 4 || offset == 7 ||
           offset == 10 || offset == 13 ||
           offset == 16)
            textComponent.setCaretPosition(offset - 1);
        else
            textComponent.setCaretPosition(offset);
    }

    /**********************************************************************************
     ** 函数名称：public boolean isLegalDate(String strDate)
     ** 功能描述：判断插入的长度为10的字符串是否合法
     ** 入口参数：intY: int型,年的值
     **            intM: int型,月的值
     **            intD: int型,日的值
     ** 返回值：boolean型,真,表示是合法的,假,表示不合法
     ** 调用者：insertString(int, String,AttributeSet)
     ***********************************************************************************/
    private boolean isValidDate(String strDate){
        int intY, intM, intD; //年,月,日,时,分,秒的值
        int intH = 0, intMi = 0, intS = 0;
        int iCaretPosition; //光标位置
        int iPatternLen = getDateFormat().toPattern().length();
        //获取字符串
        if(strDate == null){
            return false;
        }
        strDate = strDate.trim();
        //如果为空,长度不对,则为非法,返回false
        if(strDate.length() != iPatternLen){
            return false;
        }
        //如果是全角字符,则返回false
        for(int i = 0; i < 10; i++){
            if(((int)strDate.charAt(i)) > 255){
                return false;
            }
        }
        //取年,月,日的值
        try{
            intY = Integer.parseInt(strDate.substring(0, 4));
            intM = Integer.parseInt(strDate.substring(5, 7));
            intD = Integer.parseInt(strDate.substring(8, 10));
        } catch(Exception e){
            //失败则返回false
            return false;
        }
//        System.err.println("int:intY="+intY+",intM="+intM+",intD="+intD);
        iCaretPosition = textComponent.getCaretPosition();
        boolean isValid = true;

        //月越界
        if(intM > 12 || intM < 1){
            intM = Math.min(12, Math.max(1, intM));
            isValid = false;
        }
        //根据月份,判断日期输入,如越界,则修改
        if(intD < 1){
            intD = 1;
            isValid = false;
        }
        switch(intM){
            case 4:
            case 6:
            case 9:
            case 11: //最大天数为30天

                //如果输入大于30,则修改为30
                if(intD > 30){
                    intD = 30;
                    isValid = false;
                }
                break;
            case 2: //2月份

                //区别闰年
                if((intY % 4 == 0 && intY % 100 != 0) || intY % 400 == 0){
                    //如果输入大于29,则修改为29
                    if(intD > 29){
                        intD = 29;
                        isValid = false;
                    }
                } else{
                    //如果输入大于28,则修改为28
                    if(intD > 28){
                        intD = 28;
                        isValid = false;
                    }
                }
                break;
            default: //最大天数为31天

                //如果输入大于31,则修改为31
                if(intD > 31){
                    intD = 31;
                    isValid = false;
                }
        }
//        System.err.println("out:intY="+intY+",intM="+intM+",intD="+intD);

        //yyyy-MM-dd HH:mm:ss
        if(iPatternLen > 10){
            try{
                intH = Integer.parseInt(strDate.substring(11, 13));
                intMi = Integer.parseInt(strDate.substring(14, 16));
                intS = Integer.parseInt(strDate.substring(17));
            } catch(Exception e){
                return false;
            }
            //时越界
            if(intH > 23 || intH < 0){
                intH = Math.min(23, Math.max(0, intH));
                isValid = false;
            }
            //分越界
            if(intMi > 59 || intMi < 0){
                intMi = Math.min(59, Math.max(0, intMi));
                isValid = false;
            }
            //秒越界
            if(intS > 59 || intS < 0){
                intS = Math.min(59, Math.max(0, intS));
                isValid = false;
            }
        }
        if(!isValid){
            textComponent.setText(toDateString(intY, intM, intD, intH, intMi,
                                               intS));
            textComponent.setCaretPosition(iCaretPosition + 1);
        }
        return isValid;
    }

    private String toDateString(int y, int m, int d, int h, int mi, int s){
        m = Math.max(1, Math.min(12, m));
        //最大天数为31天
        d = Math.max(1, Math.min(31, d));
        switch(m){
            case 4:
            case 6:
            case 9:
            case 11:
                d = Math.min(30, d); //最大天数为30天
                break;
            case 2:

                //润年
                if((y % 4 == 0 && y % 100 != 0) || y % 400 == 0){
                    d = Math.min(29, d); //最大天数为29天
                } else{
                    d = Math.min(28, d); //最大天数为28天
                }
                break;
        }
        h = Math.max(1, Math.min(24, h));
        mi = Math.max(1, Math.min(59, mi));
        s = Math.max(1, Math.min(59, s));

        String strPattern = getDateFormat().toPattern();
        String strY = rPad0(4, "" + y);
        String strM = rPad0(2, "" + m);
        String strD = rPad0(2, "" + d);

        String strDate;
        strDate = strY + strPattern.substring(4, 5)
            + strM + strPattern.substring(7, 8) + strD;
        if(strPattern.length() == 19){
            strDate += strPattern.substring(10, 11)
                + rPad0(2, "" + h) + strPattern.substring(13, 14)
                + rPad0(2, "" + mi) + strPattern.substring(16, 17)
                + rPad0(2, "" + s);
        }
        return strDate;
    }

    private String rPad0(int maxLen, String str){
        if(str.length() < maxLen){
            str = "0" + str;
        }
        return str;
    }
}