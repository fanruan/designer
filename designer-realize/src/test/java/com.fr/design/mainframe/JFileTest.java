package com.fr.design.mainframe;

import com.fr.config.dao.DaoContext;
import com.fr.config.dao.impl.LocalClassHelperDao;
import com.fr.config.dao.impl.LocalEntityDao;
import com.fr.config.dao.impl.LocalXmlEntityDao;
import com.fr.form.main.Form;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.general.ImageWithSuffix;
import com.fr.general.ModuleContext;
import com.fr.main.impl.WorkBook;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellImage;
import com.fr.report.restriction.CellCountRestriction;
import com.fr.report.restriction.ReportRestrictionScene;
import com.fr.report.worksheet.FormElementCase;
import com.fr.report.worksheet.WorkSheet;
import com.fr.restriction.Restrictions;
import com.fr.stable.module.Module;
import com.fr.start.Designer;
import com.fr.start.MainDesigner;
import junit.framework.TestCase;
import org.junit.Assert;

import java.awt.image.BufferedImage;

public class JFileTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        DaoContext.setEntityDao(new LocalEntityDao());
        DaoContext.setClassHelperDao(new LocalClassHelperDao());
        DaoContext.setXmlEntityDao(new LocalXmlEntityDao());
        Restrictions.register(ReportRestrictionScene.CELL_COUNT, new CellCountRestriction());
        ModuleContext.startModule(Module.PAGE_MODULE);
        ModuleContext.startModule(Module.VIEW_MODULE);
        MainDesigner designer = new MainDesigner(new String[0]);
    }

    public void testJWorkBookSetPicture() {
        WorkBook workBook = new WorkBook();
        WorkSheet workSheet = new WorkSheet();
        workBook.addReport("sheet1", workSheet);
        TemplateCellElement cellElement = new DefaultTemplateCellElement();
        workSheet.addCellElement(cellElement);
        ImageWithSuffix imageWithSuffix = ImageWithSuffix.build(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB), "jpg");
        CellImage cellImage = new CellImage();
        cellImage.setImage(imageWithSuffix);
        JWorkBook jWorkBook = new JWorkBook(workBook, "text");
        jWorkBook.setPictureElem(cellElement, cellImage);
        Assert.assertEquals(imageWithSuffix, cellElement.getValue());
    }

    public void testJFormSetPicture() {
        Form form = new Form();
        ElementCaseEditor editor = new ElementCaseEditor();
        FormElementCase elementCase = new FormElementCase();
        TemplateCellElement cellElement = new DefaultTemplateCellElement();
        elementCase.addCellElement(cellElement);
        editor.setElementCase(elementCase);
        form.getContainer().addWidget(editor);
        ImageWithSuffix imageWithSuffix = ImageWithSuffix.build(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB), "jpg");
        CellImage cellImage = new CellImage();
        cellImage.setImage(imageWithSuffix);
        JForm jForm = new JForm();
        jForm.setTarget(form);
        jForm.setPictureElem(cellElement, cellImage);
        Assert.assertEquals(imageWithSuffix, cellElement.getValue());
    }
}