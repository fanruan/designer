package com.fr.design.mainframe.alphafine.cell.render;

import com.bulenkov.iconloader.IconLoader;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.model.BottomModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.cell.model.RobotModel;
import com.fr.stable.StringUtils;


import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Component;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class ContentCellRender implements ListCellRenderer<Object> {
    private static final int OFFSET = 45;
    private static final String SELECTED_PATH = AlphaFineConstants.IMAGE_URL + "selected";
    private static final String CELL_PATH = AlphaFineConstants.IMAGE_URL + "alphafine";
    private static final String SUFFIX = ".png";

    private String searchText;
    private String[] segmentationResult;

    public ContentCellRender(String searchText, String[] segmentationResult) {
        this.searchText = searchText;
        this.segmentationResult = segmentationResult;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        UILabel titleLabel = new UILabel();
        UILabel detailLabel = new UILabel();
        if (value instanceof MoreModel) {
            return new TitleCellRender().getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
        if (value instanceof BottomModel) {
            return new BottomCellRender().getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
        AlphaCellModel model = (AlphaCellModel) value;
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(null);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        if (model.hasAction()) {
            if (isSelected) {
                titleLabel.setText(" " + model.getName());
                String iconUrl = SELECTED_PATH + model.getType().getTypeValue() + SUFFIX;
                panel.setBackground(AlphaFineConstants.BLUE);
                titleLabel.setForeground(Color.WHITE);
                if (value instanceof RobotModel && ((RobotModel) value).isHotItemModel()) {
                    titleLabel.setIcon(null);
                } else {
                    titleLabel.setIcon(IconLoader.getIcon(iconUrl));
                }
            } else {

                titleLabel.setText(dealWithModelName(model.getName(), segmentationResult));
                String iconUrl = CELL_PATH + model.getType().getTypeValue() + SUFFIX;
                if (value instanceof RobotModel && ((RobotModel) value).isHotItemModel()) {
                    titleLabel.setIcon(null);
                } else {
                    titleLabel.setIcon(IconLoader.getIcon(iconUrl));
                }
            }

        } else {
            titleLabel.setIcon(null);
            titleLabel.setForeground(AlphaFineConstants.MEDIUM_GRAY);
        }
        titleLabel.setFont(AlphaFineConstants.MEDIUM_FONT);
        String description = model.getDescription();
        if (StringUtils.isNotBlank(description)) {
            detailLabel.setText("-" + description);
            detailLabel.setForeground(AlphaFineConstants.LIGHT_GRAY);
            panel.add(detailLabel, BorderLayout.CENTER);
            int width = (int) (titleLabel.getPreferredSize().getWidth() + detailLabel.getPreferredSize().getWidth());
            if (width > AlphaFineConstants.LEFT_WIDTH - OFFSET) {
                int nameWidth = (int) (AlphaFineConstants.LEFT_WIDTH - detailLabel.getPreferredSize().getWidth() - OFFSET);
                titleLabel.setPreferredSize(new Dimension(nameWidth, AlphaFineConstants.CELL_HEIGHT));
            }
        } else {
            titleLabel.setPreferredSize(new Dimension(AlphaFineConstants.LEFT_WIDTH - OFFSET, AlphaFineConstants.CELL_HEIGHT));
        }

        panel.add(titleLabel, BorderLayout.WEST);
        panel.setPreferredSize(new Dimension(list.getFixedCellWidth(), AlphaFineConstants.CELL_HEIGHT));
        return panel;
    }

    /**
     * 处理model的显示颜色，将搜索词高亮
     *
     * @param modelName
     * @param strings
     * @return
     */
    public String dealWithModelName(String modelName, String[] strings) {
        if (strings == null) {
            return modelName;
        }
        for (int i = 0; i < strings.length; i++) {
            String primaryStr = getReplacedString(modelName, strings[i]);
            modelName = modelName.replaceAll("(?i)" + strings[i], "|<font color=" + AlphaFineConstants.HIGH_LIGHT_COLOR + ">" + strings[i] + "</font>|");
            if (StringUtils.isNotEmpty(primaryStr)) {
                modelName = modelName.replaceAll(strings[i], primaryStr);
            }
        }
        modelName = "<HTML>" + modelName.replaceAll("\\|", "") + "</HTML>";
        return modelName;
    }

    private String getReplacedString(String modelName, String string) {
        int index = modelName.toLowerCase().indexOf(string.toLowerCase());
        if (index == -1) {
            return StringUtils.EMPTY;
        }
        return modelName.substring(index, index + string.length());

    }
}
