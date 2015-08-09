package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TagTemplate;
import net.sourceforge.stripes.tag.InputOptionsCollectionTag;
import net.sourceforge.stripes.tag.InputSelectTag;

import javax.servlet.jsp.PageContext;

public class OptionsCollection extends TagTemplate<InputOptionsCollectionTag,InputSelectTag> {

    public OptionsCollection(PageContext pageContext, InputOptionsCollectionTag tag, InputSelectTag parent) {
        super(pageContext, tag, parent);
    }

    public OptionsCollection setValue(String value) {
        tag.setValue(value);
        return this;
    }

    public OptionsCollection setLabel(String label) {
        tag.setLabel(label);
        return this;
    }
}
