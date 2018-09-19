package com.fr.design.mainframe.alphafine.cell.model;

import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class ModelTest {
    @Test
    public void documentModelTest(){
        DocumentModel documentModel = new DocumentModel("name","content",1);
        Assert.assertEquals("name",documentModel.getName());
        Assert.assertEquals("content",documentModel.getContent());
        Assert.assertEquals(1,documentModel.getDocumentId());
        Assert.assertEquals(AlphaFineConstants.DOCUMENT_DOC_URL + documentModel.getDocumentId() + ".html", documentModel.getDocumentUrl());
        Assert.assertEquals(AlphaFineConstants.DOCUMENT_INFORMATION_URL + documentModel.getDocumentId(), documentModel.getInformationUrl());
        Assert.assertEquals(documentModel.getStoreInformation(), documentModel.getInformationUrl());
        Assert.assertEquals(CellType.DOCUMENT, documentModel.getType());
        Assert.assertEquals(true, documentModel.hasAction());
        Assert.assertEquals(true, documentModel.isNeedToSendToServer());

        documentModel.setDescription("test");
        Assert.assertEquals("test", documentModel.getDescription());

        DocumentModel another = new DocumentModel("another","another",1);
        documentModel.doAction();
        Assert.assertTrue(another.equals(documentModel));
        Assert.assertNotNull(documentModel.modelToJson());
    }

    @Test
    public void bottomModelTest(){
        BottomModel bottomModel = new BottomModel();
        Assert.assertEquals(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Internet_Forum"), bottomModel.getGoToWeb());
        Assert.assertEquals(CellType.BOTTOM, bottomModel.getType());
        try {
            Assert.assertEquals(JSONObject.EMPTY, bottomModel.modelToJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(true, bottomModel.hasAction());
        bottomModel.doAction();

    }

    @Test
    public void fileModelTest(){
        FileModel fileModel = new FileModel("name", "test\\\\");
        Assert.assertEquals(CellType.FILE, fileModel.getType());
        Assert.assertNotNull(fileModel.modelToJson());
        FileModel anotherFileModel = new FileModel("anotherFileModel", "test\\\\");
        Assert.assertTrue(anotherFileModel.equals(fileModel));
    }

    @Test
    public void moreModelTest(){
        MoreModel moreModel = new MoreModel("name");
        Assert.assertTrue(!moreModel.hasAction());
        Assert.assertTrue(!moreModel.isNeedToSendToServer());
        Assert.assertEquals(CellType.MORE, moreModel.getType());
    }

    @Test
    public void noResultModelTest(){
        NoResultModel noResultModel = new NoResultModel("test");
        Assert.assertTrue(!noResultModel.hasAction());
        Assert.assertTrue(!noResultModel.isNeedToSendToServer());
        Assert.assertEquals(CellType.NO_RESULT, noResultModel.getType());
    }

    @Test
    public void pluginModelTest(){
//        PluginModel pluginModel = new PluginModel("name","content");
    }
}
