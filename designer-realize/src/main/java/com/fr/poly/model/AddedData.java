/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.model;

import java.util.ArrayList;

import com.fr.general.ComparatorUtils;
import com.fr.poly.PolyDesigner;
import com.fr.poly.creator.BlockCreator;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.StringUtils;

/**
 * @author richer
 * @since 6.5.4 创建于2011-4-2 用来保存已经添加了的设计块
 */
public class AddedData {

	private java.util.List<BlockCreator> addedList = new ArrayList<BlockCreator>();
	private PolyDesigner designer;
	private int num = 1;

	public AddedData(PolyDesigner designer) {
		this.designer = designer;
	}

	public void addBlockCreator(BlockCreator creator) {
		creator.setDesigner(designer);
		TemplateBlock block = creator.getValue();
		if (StringUtils.isEmpty(block.getBlockName())) {
			block.setBlockName(createUnRepeatedName());
		}
		addedList.add(creator);
	}

	private String createUnRepeatedName() {
		String new_name = "block" + num;
		for (int i = 0, len = getAddedCount(); i < len; i++) {
			TemplateBlock block = getAddedAt(i).getValue();
			if (ComparatorUtils.equals(new_name, block.getBlockName())) {
				num++;
				return createUnRepeatedName();
			}
		}
		return new_name;
	}

	public void removeBlockCreator(BlockCreator creator) {
		addedList.remove(creator);
	}

	public int getAddedCount() {
		return addedList.size();
	}

	public BlockCreator getAddedAt(int idx) {
		if (idx < 0 || idx > addedList.size() - 1) {
			return null;
		}
		return addedList.get(idx);
	}

}