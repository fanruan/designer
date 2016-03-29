package com.fr.design.constants;

/**
 * Created with IntelliJ IDEA.
 * User: richie
 * Date: 13-11-13
 * Time: 下午6:50
 */
public class KeyWords {
    private KeyWords() {

    }

    
/*    parameterEl ：object
    返回对象参数界面
    例如：
    contentPane.parameterEl
    currentPageIndex
    curLGP
    reportTotalPage
    zoom
*/
    public static final String[] JAVASCRIPT = new String[]{
    		// normal js
    		"Array",
    		"attr",
            "break",
            "case",
            "catch",
            "children",
            "close",
            "concat",
            "continue",
            "default",
            "delete",
            "do",
            "document",
            "else",
            "event",
            "finally",
            "fireEvent",
            "for",
            "function",
            "getElementById",
            "getText",
            "html",
            "if",
            "in",
            "instanceof",
            "length",
            "location",
            "new",
            "open",
            "parent",
            "parseFloat",
            "parseInt",
            "replace",
            "return",
            "search",
            "setInterval",
            "split",
            "substr",
            "switch",
            "this",
            "throw",
            "tostring",
            "try",
            "typeof",
            "var",
            "void",
            "while",
            "window",
            
            // contentPane
            "cellselect",
            "contentPane",
            "curLGP",
            "currentPageIndex",
            "lgps",
            "options",
            "parameterEl",
            "reportTotalPage",
            "selectedIndex", //当前选中的sheet号
            "zoom",
            
            "appendReportRC()",
            "appletPrint()",
            "deleteReportRC()",
            "deleteRows()",
            "emailReport()",
            "exportReportToExcel(mode)",
            "exportReportToImage(pattern)",
            "exportReportToPDF(ori)",
            "exportReportToWord()",
            "flashPrint()",
            "getCellValue(reportIndex, columnIndex, rowIndex)",
            "getWidgetByCell(cell)",
            "getWidgetByName(name)",
            "getWidgetsByName(name)",
            "gotoFirstPage()",
            "gotoLastPage()",
            "gotoNextPage()",
            "gotoPage(n)",
            "gotoPreviousPage()",
            "importExcelData()",
            "makeHighlight(color, op)",
            "on()",
            "pageSetup()",
            "parameterCommit()",
            "pdfPrint()",
            "printPreview()",
            "scale()",
            "setCellValue([reportIndex], columnIndex, rowIndex, cv)",
            "stopEditing()",
            "verifyAndWriteReport(true)", //空或false为提交所有sheet，true为提交当前sheet
            "verifyReport()",
            "writeReportIgnoreVerify(true)",
            "writeReport(index)",
              
            // curLGP
            "currentTDCell",
            "dirtyCell",
            "write",
            "form",
            
            "cut2ColumnRowString(td)",
            "displayTDCell(tdCell, cv, presentValue)",
            "getColumnWidth(i)",
            "getRowHeight(i)",
            "getTDCell()",
            "getTDCol(td)",
            "getTDRow(td)",
            "getWidgetCell(widget)",
            "resetCellValue(widget)",
            "stopCellEditing()",
            
            // parameterEl
            //getWidgetByName
            "name_widgets",
            
            // widget
            //"fireEvent",
            "getValue()",
            "isEnabled()",
            "isVisible()",
            "reset()",
            "setValue(value)",
            "setEnable()",
            "setVisible()",
            
            // other
            "${servletURL}?reportlet="
      
    };
    
    public static final String[][] JAVASCRIPT_SHORT = new String[][]{
    		/*
    		 * FR.Msg
    		 */
            new String[]{"alert", "FR.Msg.alert(title, message, callback)"},
            new String[]{"confirm", "FR.Msg.confirm(title, message, callback)"},
            new String[]{"prompt", "FR.Msg.prompt(title, message, value, callback)"},
            new String[]{"toast", "FR.Msg.toast(message)"},
            
            /*
             * FR
             */
            new String[]{"ajax","FR.ajax(o)"},
            new String[]{"cellStr2ColumnRow","FR.cellStr2ColumnRow(name)"},
            new String[]{"chart_Change_Parameter","FR.Chart.WebUtils.chart_Change_Parameter(pos, id, para)"},
            new String[]{"chart_Refresh()","FR.Chart.WebUtils.chart_Refresh(str)"},
            new String[]{"cjkDecode","FR.cjkDecode(str)"},
            new String[]{"cjkEncode","FR.cjkEncode(str)"},
            new String[]{"closeDialog","FR.closeDialog()"},
            new String[]{"columnRow2CellStr","FR.columnRow2CellStr(cr)"},
            new String[]{"destroyDialog","FR.destroyDialog()"},
            new String[]{"digit2Letter","FR.digit2Letter(k)"},
            new String[]{"doAppletPrint", "FR.doAppletPrint(sessionID)"},
            new String[]{"doFlashPrint", "FR.doFlashPrint(sessionID, currentPageIndex)"},
            new String[]{"doHyperlinkByGet","FR.doHyperlinkByGet(url, para, target, feature)"},
            new String[]{"doHyperlinkByPost","FR.doHyperlinkByPost(url, para, target, feature)"},
            new String[]{"doHyperlinkByGet4Reportlet","FR.doHyperlinkByGet4Reportlet(url,para,target,featrue)"},
            new String[]{"doPDFPrint", "FR.doPDFPrint(sessionID, popupSetup)"},
            new String[]{"doURLAppletPrint","FR.doURLAppletPrint(printurl,isPopUp,config)"},
            new String[]{"doURLFlashPrint","FR.doURLFlashPrint(printurl,isPopUp,config)"},
            new String[]{"doURLPDFPrint","FR.doURLPDFPrint(printurl,isPopUp,config)"},
            new String[]{"htmlDecode","FR.htmlDecode(text)"},
            new String[]{"htmlEncode","FR.htmlEncode(text)"},
            new String[]{"id2ColumnRow","FR.id2ColumnRow(id)"},
            new String[]{"id2ColumnRowStr","FR.id2ColumnRowStr(id)"},
            new String[]{"id2Location","FR.id2Location(id)"},
            new String[]{"isEmptyArray","FR.isEmptyArray(array)"},
            new String[]{"letter2Digit","FR.letter2Digit(abc)"},
            new String[]{"showDialog","FR.showDialog(title, width, height, innerContent)"},
            new String[]{"showIframeDialog","FR.showIframeDialog(o)"},
            new String[]{"showHyperlinkDialog","FR.showHyperlinkDialog(url,featur)"},
            
            /*
             * FS
             */
            new String[]{"signOut","FS.Trans.signOut()"},
            new String[]{"closeActiveTab","FS.tabPane.closeActiveTab()"},
            new String[]{"addItem","FS.tabPane.addItem(o)"}          
    };
}