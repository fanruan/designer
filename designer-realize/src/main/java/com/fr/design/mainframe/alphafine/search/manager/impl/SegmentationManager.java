package com.fr.design.mainframe.alphafine.search.manager.impl;

import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.stable.StringUtils;
import com.fr.third.ibm.icu.text.BreakIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alex.sung on 2018/8/3.
 */
public class SegmentationManager {
    private static final int MAX_CHINESE_CHARACTERS_NUM = 4;

    private SegmentationManager() {

    }

    public static SegmentationManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final SegmentationManager INSTANCE = new SegmentationManager();
    }

    /**
     * 判断是否需要分词
     *
     * @param searchText
     * @return
     */
    public boolean isNeedSegmentation(String searchText) {
        int count = 0;
        Pattern p = Pattern.compile(AlphaFineConstants.CHINESE_CHARACTERS);
        Matcher m = p.matcher(searchText);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                count = count + 1;
            }
        }
        if (count >= MAX_CHINESE_CHARACTERS_NUM) {
            return true;
        }
        return false;
    }

    /**
     * 对字符串进行分词
     *
     * @param searchText
     * @return
     */
    public String[] startSegmentation(String searchText) {
        Pattern p = Pattern.compile(AlphaFineConstants.SPECIAL_CHARACTER_REGEX);
        Matcher m = p.matcher(searchText);
        searchText = m.replaceAll(StringUtils.EMPTY).trim().replaceAll(StringUtils.BLANK, StringUtils.EMPTY);
        if (StringUtils.isEmpty(searchText)) {
            return null;
        }
        if (!isNeedSegmentation(searchText)) {
            return new String[]{searchText};
        }
        List<String> result = new ArrayList<>();
        result.add(searchText);
        BreakIterator itor = BreakIterator.getWordInstance();
        itor.setText(searchText);
        int start = itor.first();
        for (int end = itor.next(); end != BreakIterator.DONE; start = end, end = itor.next()) {
            String temp = searchText.substring(start, end);
            //去掉空和连词
            if (StringUtils.isNotEmpty(temp) && !AlphaFineConstants.CONJUNCTION.contains(temp)) {
                result.add(temp);
            }
        }
        String[] strings = new String[result.size()];
        result.toArray(strings);
        return strings;
    }
}
