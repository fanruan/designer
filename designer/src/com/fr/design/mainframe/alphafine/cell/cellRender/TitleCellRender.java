package com.fr.design.mainframe.alphafine.cell.cellRender;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.cell.cellModel.MoreModel;
import javax.swing.*;
import java.awt.*;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class TitleCellRender implements ListCellRenderer<Object> {
    private UILabel name;
    private UILabel more;

    public TitleCellRender() {
        this.name = new UILabel();
        this.more = new UILabel();
    }
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        MoreModel moreModel = (MoreModel)value;
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0xf9f9f9));
        panel.setBorder(BorderFactory.createEmptyBorder(0,15,0,0));
        name.setText(moreModel.getName());
        name.setFont(new Font("Song_TypeFace",0,10));
        more.setFont(new Font("Song_TypeFace",0,10));
        more.setText(moreModel.getContent());
        name.setForeground(new Color(0x666666));
        more.setForeground(new Color(0x666666));
        panel.add(name, BorderLayout.WEST);
        if (moreModel.isNeedMore()) {
            this.more.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
            panel.add(this.more, BorderLayout.EAST);
        }
        if (moreModel.isLoading()) {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/com/fr/design/mainframe/alphafine/images/loading.gif"));
            //设置cell的加载动画
            imageIcon.setImageObserver(list);
            UILabel loadingLabel = new UILabel(imageIcon);
            panel.add(loadingLabel, BorderLayout.SOUTH);
        }
        return panel;
    }
}
