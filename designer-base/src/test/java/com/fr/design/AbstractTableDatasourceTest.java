package com.fr.design;

import com.fr.base.AbstractTableData;
import com.fr.base.TableData;
import com.fr.cert.token.lang.Assert;
import com.fr.data.AbstractTableDataSource;
import com.fr.data.TableDataSource;
import com.fr.general.data.TableDataException;
import com.fr.script.Calculator;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;
import com.fr.third.javax.xml.stream.XMLStreamException;
import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class AbstractTableDatasourceTest extends TestCase {

    public void testAbstractTableDataSourceReadAndWrite() throws XMLStreamException, TableDataException, UnsupportedEncodingException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<TableDataMap xmlVersion=\"20170720\" releaseVersion=\"10.0.0\">\n" +
                "<TableData name=\"新老用户对比\" class=\"com.fr.data.impl.EmbeddedTableData\">\n" +
                "<Parameters/>\n" +
                "<DSName>\n" +
                "<![CDATA[]]></DSName>\n" +
                "<ColumnNames>\n" +
                "<![CDATA[用户,,.,,数量]]></ColumnNames>\n" +
                "<ColumnTypes>\n" +
                "<![CDATA[java.lang.String,java.lang.String]]></ColumnTypes>\n" +
                "<RowData ColumnTypes=\"java.lang.String,java.lang.String\">\n" +
                "<![CDATA[HT#`YdrgS?F_p4t!s5W<e!EG4`l?\"F!!~\n" +
                "]]></RowData>\n" +
                "</TableData>\n" +
                "<TableData name=\"保费地区排名\" class=\"com.fr.data.impl.EmbeddedTableData\">\n" +
                "<Parameters/>\n" +
                "<DSName>\n" +
                "<![CDATA[]]></DSName>\n" +
                "<ColumnNames>\n" +
                "<![CDATA[地区,,.,,保费,,.,,flag]]></ColumnNames>\n" +
                "<ColumnTypes>\n" +
                "<![CDATA[java.lang.String,java.lang.String,java.lang.String]]></ColumnTypes>\n" +
                "<RowData ColumnTypes=\"java.lang.String,java.lang.String,java.lang.String\">\n" +
                "<![CDATA[Hb+:uP9\"^(N=64(3\"<EiD4LX#]A:[R!*5^Gk'.CY1BPIb24^H\\8FPgD\"6PG:2VThn`VfNap_K\n" +
                "e;n3PjDL~\n" +
                "]]></RowData>\n" +
                "</TableData>\n" +
                "<TableData name=\"年度累计总保费\" class=\"com.fr.data.impl.EmbeddedTableData\">\n" +
                "<Parameters/>\n" +
                "<DSName>\n" +
                "<![CDATA[]]></DSName>\n" +
                "<ColumnNames>\n" +
                "<![CDATA[年份,,.,,其他业务,,.,,价值业务]]></ColumnNames>\n" +
                "<ColumnTypes>\n" +
                "<![CDATA[java.lang.String,java.lang.String,java.lang.String]]></ColumnTypes>\n" +
                "<RowData ColumnTypes=\"java.lang.String,java.lang.String,java.lang.String\">\n" +
                "<![CDATA[4j(#=!$DgS^`'*Or^HRR9OhLVadFnN_OJGB+o,e>7.nI9\\uimQ~\n" +
                "]]></RowData>\n" +
                "</TableData>\n" +
                "<TableData name=\"销售数据地图\" class=\"com.fr.data.impl.EmbeddedTableData\">\n" +
                "<Parameters/>\n" +
                "<DSName>\n" +
                "<![CDATA[]]></DSName>\n" +
                "<ColumnNames>\n" +
                "<![CDATA[地区,,.,,金额]]></ColumnNames>\n" +
                "<ColumnTypes>\n" +
                "<![CDATA[java.lang.String,java.lang.String]]></ColumnTypes>\n" +
                "<RowData ColumnTypes=\"java.lang.String,java.lang.String\">\n" +
                "<![CDATA[<KP$pK.mjkccsoa\"<Od.f'0,sg8S&+Q6*+K4>9UT+>AM9kpGY!\\4\\SXO$EO1[i^/XKmIMmdu\n" +
                "!\"Q5sC\"9Ji(qlAa_kVTnCC/+p:XVF.KGR?GMsmI)2\">1s>(/T71G7cFsfA\"UpjYr4R)A14-*\n" +
                "qc6ri_1*%/XSsf!PM]ARc+ojE:eo:_gma!ut7eZ6BoFc2b5hu4@TUtuOT);aq6l4Vj<jM_Ku*\n" +
                "\\lKh?<s9,PbluqGk84HlDg]Aq,#MDJrl^\\mAq.\\L)&\"oJcSIq!T%do(%#I!VZc`L$ri-QMpFZ\n" +
                "u68s(6@m8kLKWBrPWS&l`~\n" +
                "]]></RowData>\n" +
                "</TableData>\n" +
                "<TableData name=\"表单联动 柱形图\" class=\"com.fr.data.impl.EmbeddedTableData\">\n" +
                "<Parameters/>\n" +
                "<DSName>\n" +
                "<![CDATA[]]></DSName>\n" +
                "<ColumnNames>\n" +
                "<![CDATA[地区,,.,,金额]]></ColumnNames>\n" +
                "<ColumnTypes>\n" +
                "<![CDATA[java.lang.String,java.lang.String]]></ColumnTypes>\n" +
                "<RowData ColumnTypes=\"java.lang.String,java.lang.String\">\n" +
                "<![CDATA[Ha[qnMsgT-EuI&u9Y$a3Sb[*^Hk=?NPj@SFcDUR5JiJK3>YfS3J$NPo=IB3AP?eiop.bk9Qu\n" +
                "P:F\\.L&t$31~\n" +
                "]]></RowData>\n" +
                "</TableData>\n" +
                "<TableData name=\"地区保费构成\" class=\"com.fr.data.impl.EmbeddedTableData\">\n" +
                "<Parameters/>\n" +
                "<DSName>\n" +
                "<![CDATA[]]></DSName>\n" +
                "<ColumnNames>\n" +
                "<![CDATA[地区名称,,.,,分类,,.,,金额,,.,,flag]]></ColumnNames>\n" +
                "<ColumnTypes>\n" +
                "<![CDATA[java.lang.String,java.lang.String,java.lang.String,java.lang.String]]></ColumnTypes>\n" +
                "<RowData ColumnTypes=\"java.lang.String,java.lang.String,java.lang.String,java.lang.String\">\n" +
                "<![CDATA[Fj\\SDJhRmhiTp.nl8F((mNOIp$6h1jU+Qg/&hD+kfnR40k\"^#YZeKnes-ug@.TN@I?6#Eaf^\n" +
                "'rbg9T)sQp9#\"<-_H((dqbGQ:6.4dS/KkN4Vt(j[Z%?bW2`a3UI)'Zm1]ApNcYFcFola@ZoH^\n" +
                "C19_8ph'[:I3q*?OWI@!E-]AgkiLLVJJ&NTF<LNaEm(Hl!O%W#m6F94##isKmWGEQ:Qa7?-lo\n" +
                "K']A:pj_-~\n" +
                "]]></RowData>\n" +
                "</TableData>\n" +
                "<TableData name=\"热销产品\" class=\"com.fr.data.impl.EmbeddedTableData\">\n" +
                "<Parameters/>\n" +
                "<DSName>\n" +
                "<![CDATA[]]></DSName>\n" +
                "<ColumnNames>\n" +
                "<![CDATA[名称,,.,,承保数量]]></ColumnNames>\n" +
                "<ColumnTypes>\n" +
                "<![CDATA[java.lang.String,java.lang.String]]></ColumnTypes>\n" +
                "<RowData ColumnTypes=\"java.lang.String,java.lang.String\">\n" +
                "<![CDATA[HeuF/jckKI]A9Y[Pgh=cCBPLqjoc-QELssEF9ur^NciQq_\"KW18~\n" +
                "]]></RowData>\n" +
                "</TableData>\n" +
                "<TableData name=\"增长折线\" class=\"com.fr.data.impl.EmbeddedTableData\">\n" +
                "<Parameters/>\n" +
                "<DSName>\n" +
                "<![CDATA[]]></DSName>\n" +
                "<ColumnNames>\n" +
                "<![CDATA[季度,,.,,增长数]]></ColumnNames>\n" +
                "<ColumnTypes>\n" +
                "<![CDATA[java.lang.String,java.lang.String]]></ColumnTypes>\n" +
                "<RowData ColumnTypes=\"java.lang.String,java.lang.String\">\n" +
                "<![CDATA[Ha9F*l.X*PG:hi3jEe,4#KesaHEs:Z0)ftdCm[0^~\n" +
                "]]></RowData>\n" +
                "</TableData>\n" +
                "<TableData name=\"最新交易数据\" class=\"com.fr.data.impl.EmbeddedTableData\">\n" +
                "<Parameters/>\n" +
                "<DSName>\n" +
                "<![CDATA[]]></DSName>\n" +
                "<ColumnNames>\n" +
                "<![CDATA[地区,,.,,渠道,,.,,成交信息,,.,,费用]]></ColumnNames>\n" +
                "<ColumnTypes>\n" +
                "<![CDATA[java.lang.String,java.lang.String,java.lang.String,java.lang.Integer]]></ColumnTypes>\n" +
                "<RowData ColumnTypes=\"java.lang.String,java.lang.String,java.lang.String,java.lang.Integer\">\n" +
                "<![CDATA[Kp'VSK0U8Cf@-XK?F2?>5++,D>0thk;G<`mPXn.b)QZb5j^mQri%8Ji5q\\<QfU[CMF+1@.+F\n" +
                "=V2:B<sQd@CPt$SPQYC#d)m%W3UQG_:cE`,cpMKA/6nFt!<nYG1)TJ86<kT1X@Ni-l#s[@YI\n" +
                "c#[Y!&A0DMK`q$3]Aa!hsc9COj`!fhS:\\?n6=/VkT*$_j&K@:h6gXHXSYM4[dXQq?es!Y/Yhi\n" +
                "nj$:0r2r]A#gODY6&9qD$ho,Y3_[0s:u0S9kEH]A^ajp,mj0&rL$d*3B'eUJ8a7$U[+GFfaksD\n" +
                ",8XnK@&V;!H!NU*c`rtX)(^A~\n" +
                "]]></RowData>\n" +
                "</TableData>\n" +
                "</TableDataMap>\n";

        AbstractTableDataSource source = new AbstractTableDataSource() {
        };
        XMLableReader.createXMLableReader(xml).readXMLObject(source);
        TableData tableData = source.getTableData("热销产品");
        Assert.notNull(tableData);
        Assert.isTrue(tableData.createDataModel(Calculator.createCalculator()).getRowCount() == 4);
        Assert.isTrue(tableData.createDataModel(Calculator.createCalculator()).getColumnCount() == 2);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLPrintWriter writer = XMLPrintWriter.create(new OutputStreamWriter(out,"UTF-8"));
        source.writeXML(writer);
        writer.close();
        TableDataSource another = new AbstractTableDataSource(){};
        XMLableReader.createXMLableReader(new String(out.toByteArray(),"UTF-8")).readXMLObject(another);
        tableData = another.getTableData("热销产品");
        Assert.notNull(tableData);
        Assert.isTrue(tableData.createDataModel(Calculator.createCalculator()).getRowCount() == 4);
        Assert.isTrue(tableData.createDataModel(Calculator.createCalculator()).getColumnCount() == 2);
    }
}
