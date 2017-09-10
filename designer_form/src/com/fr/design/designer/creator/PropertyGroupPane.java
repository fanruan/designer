package com.fr.design.designer.creator;

import com.fr.base.FRContext;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.FreeButton;
import com.fr.form.ui.Widget;

import javax.swing.*;
import java.awt.*;

/**
 * Created by kerry on 2017/9/7.
 */
public class PropertyGroupPane extends BasicPane {
    private CRPropertyDescriptor[] crPropertyDescriptors;
    private CRPropertyDescriptorPane[] crPropertyDescriptorPanes;
    private XCreator xCreator;

    public PropertyGroupPane(CRPropertyDescriptor[] crPropertyDescriptors, XCreator xCreator) {
        this.crPropertyDescriptors = crPropertyDescriptors;
        this.xCreator = xCreator;
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        initComponent();
    }

    public void initComponent() {
        int count = crPropertyDescriptors.length;
        crPropertyDescriptorPanes = new CRPropertyDescriptorPane[count];
        Component[][] components = new Component[count][];
        for (int i = 0; i < count; i++) {
            crPropertyDescriptorPanes[i] = new CRPropertyDescriptorPane(crPropertyDescriptors[i], xCreator);
            components[i] = crPropertyDescriptorPanes[i].createTableLayoutComponent();
        }

        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        this.add(panel, BorderLayout.CENTER);
    }

    public void populate(Widget widget){
        for(int i = 0; i< crPropertyDescriptorPanes.length; i++){
            crPropertyDescriptorPanes[i].populate(widget);
        }
    }

    @Override
    protected String title4PopupWindow() {
        return "PropertyGroupPane";
    }

    public static void main(String[] args) {
      try{
          XCreator xCreator = new XButton(new FreeButton(),new Dimension(100, 100));
          PropertyGroupPane propertyGroupPane = new PropertyGroupPane(xCreator.supportedDescriptor(), xCreator);
          JFrame jf = new JFrame("jFrame");
          jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          JPanel content = (JPanel) jf.getContentPane();
          content.setLayout(new BorderLayout());
          content.add(propertyGroupPane, BorderLayout.CENTER);
          jf.setSize(439, 400);
          jf.setVisible(true);
      }catch (Exception e){
          FRContext.getLogger().error(e.getMessage());
      }

    }

}
