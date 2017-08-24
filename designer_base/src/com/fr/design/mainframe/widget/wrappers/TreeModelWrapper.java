package com.fr.design.mainframe.widget.wrappers;

import com.fr.base.TemplateUtils;
import com.fr.data.impl.TreeNodeAttr;
import com.fr.data.impl.TreeNodeWrapper;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.StringUtils;

public class TreeModelWrapper implements Encoder, Decoder {

    @Override
    public String encode(Object v) {
        if (v == null) {
            return StringUtils.EMPTY;
        }
        if (v instanceof TreeNodeAttr[]) {
            return TemplateUtils.render(Inter.getLocText("FR-Designer_Total_N_Grade"), new String[]{"N"}, new String[]{((TreeNodeAttr[]) v).length + ""});
        } else if (v instanceof TreeNodeWrapper) {
            TreeNodeAttr[] treeNodeAttrs = ((TreeNodeWrapper) v).getTreeNodeAttrs();
            return TemplateUtils.render(Inter.getLocText("FR-Designer_Total_N_Grade"), new String[]{"N"}, new String[]{treeNodeAttrs.length + ""});
        } else if (v instanceof NameObject) {
            return Inter.getLocText("FR-Designer_DataTable-Build");
        } else {
            return Inter.getLocText("FR-Designer_Auto-Build");
        }
    }

    @Override
    public Object decode(String txt) {
        return null;
    }

    @Override
    public void validate(String txt) throws ValidationException {
    }
}