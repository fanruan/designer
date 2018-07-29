package com.fr.design.mainframe.alphafine.search.manager.impl;

import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.CellModelHelper;
import com.fr.design.mainframe.alphafine.cell.model.ActionModel;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.alphafine.search.manager.fun.AlphaFineSearchProvider;
import com.fr.design.mainframe.toolbar.UpdateActionManager;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ProductConstants;
import com.fr.third.org.apache.lucene.analysis.Analyzer;
import com.fr.third.org.apache.lucene.analysis.standard.StandardAnalyzer;
import com.fr.third.org.apache.lucene.document.Document;
import com.fr.third.org.apache.lucene.document.Field;
import com.fr.third.org.apache.lucene.document.LongField;
import com.fr.third.org.apache.lucene.document.StringField;
import com.fr.third.org.apache.lucene.index.DirectoryReader;
import com.fr.third.org.apache.lucene.index.IndexReader;
import com.fr.third.org.apache.lucene.index.IndexWriter;
import com.fr.third.org.apache.lucene.index.IndexWriterConfig;
import com.fr.third.org.apache.lucene.index.Term;
import com.fr.third.org.apache.lucene.search.IndexSearcher;
import com.fr.third.org.apache.lucene.search.Query;
import com.fr.third.org.apache.lucene.search.ScoreDoc;
import com.fr.third.org.apache.lucene.search.Sort;
import com.fr.third.org.apache.lucene.search.SortField;
import com.fr.third.org.apache.lucene.search.TermQuery;
import com.fr.third.org.apache.lucene.search.TopFieldDocs;
import com.fr.third.org.apache.lucene.store.Directory;
import com.fr.third.org.apache.lucene.store.FSDirectory;
import com.fr.third.org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by XiaXiang on 2018/1/22.
 */
public class RecentSearchManager implements AlphaFineSearchProvider {
    private static final int MAX_SIZE = 3;
    private static RecentSearchManager instance;
    IndexReader indexReader = null;
    IndexSearcher indexSearcher = null;
    //索引存储路径
    private String path = ProductConstants.getEnvHome() + File.separator + "searchIndex";
    //分词器，暂时先用这个
    private Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
    // 存储目录
    private Directory directory = null;
    private IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
    private IndexWriter indexWriter = null;
    private SearchResult recentModelList;
    private SearchResult modelList;

    public static RecentSearchManager getInstance() {
        if (instance == null) {
            synchronized (RecentSearchManager.class) {
                if (instance == null) {
                    instance = new RecentSearchManager();
                    instance.initWriter();
                }
            }
        }
        return instance;
    }

    @Override
    public SearchResult getLessSearchResult(String searchText) {
        this.modelList = new SearchResult();
        recentModelList = getRecentModelList(searchText);
        if (recentModelList != null && recentModelList.size() > 0) {
            modelList.add(new MoreModel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_AlphaFine_Latest")));
        }
        modelList.addAll(recentModelList.subList(0, AlphaFineConstants.LATEST_SHOW_SIZE));
        return modelList;
    }

    @Override
    public SearchResult getMoreSearchResult(String searchText) {
        return new SearchResult();
    }

    public synchronized SearchResult getRecentModelList(String searchText) {
        return searchBySort(searchText);
    }

    public List<AlphaCellModel> getRecentModelList() {
        return recentModelList;
    }

    /**
     * 初始化indexWriter
     */
    private void initWriter() {
        try {
            directory = FSDirectory.open(new File(path));
            indexWriter = new IndexWriter(directory, config);
        } catch (IOException e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }

    }

    /**
     * 初始化indexReader
     */
    private void initReader() {
        try {
            indexWriter.close();
            indexReader = DirectoryReader.open(directory);
            indexSearcher = new IndexSearcher(indexReader);
        } catch (IOException e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
    }

    /**
     * 添加模型
     *
     * @param searchKey
     * @param cellModel
     */
    public void addModel(String searchKey, AlphaCellModel cellModel) {
        try {
            initWriter();
            Document doc = new Document();
            doc.add(new StringField("searchKey", searchKey, Field.Store.YES));
            doc.add(new StringField("cellModel", cellModel.ModelToJson().toString(), Field.Store.YES));
            doc.add(new LongField("time", System.currentTimeMillis(), Field.Store.YES));
            writeDoc(doc);
        } catch (JSONException e) {
            FineLoggerFactory.getLogger().error("add document error: " + e.getMessage());
        }
    }

    /**
     * 写文档，建立索引
     *
     * @param doc
     */
    private void writeDoc(Document doc) {
        try {
            indexWriter.addDocument(doc);
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            FineLoggerFactory.getLogger().error("write document error: " + e.getMessage());
        }
    }

    /**
     * 按序搜索
     *
     * @param key
     * @return
     */
    private synchronized SearchResult searchBySort(String key) {
        recentModelList = new SearchResult();
        try {

            initReader();
            IndexSearcher searcher = new IndexSearcher(indexReader);
            //构建排序字段
            SortField[] sortField = new SortField[1];
            sortField[0] = new SortField("time", SortField.Type.LONG, true);
            Sort sortKey = new Sort(sortField);
            String searchField = "searchKey";
            Term term = new Term(searchField, key);
            Query query = new TermQuery(term);
            TopFieldDocs docs = searcher.search(query, 100, sortKey);
            ScoreDoc[] scores = docs.scoreDocs;
            this.recentModelList = new SearchResult();
            //遍历结果
            for (ScoreDoc scoreDoc : scores) {
                Document document = searcher.doc(scoreDoc.doc);
                AlphaCellModel model = CellModelHelper.getModelFromJson(new JSONObject(document.get("cellModel")));
                if (model.getType() == CellType.ACTION) {
                    UpdateAction action = UpdateActionManager.getUpdateActionManager().getActionByName(model.getName());
                    if (action != null) {
                        ((ActionModel) model).setAction(action);
                        if (recentModelList.contains(model)) {
                            continue;
                        }
                        recentModelList.add(model);
                    }
                } else {
                    if (recentModelList.contains(model)) {
                        continue;
                    }
                    recentModelList.add(model);
                }


            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error("local search error: " + e.getMessage());
            return recentModelList;
        }
        return recentModelList;
    }


}
