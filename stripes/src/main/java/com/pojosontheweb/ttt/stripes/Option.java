package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TttPageContext;
import net.sourceforge.stripes.tag.InputOptionTag;

public class Option extends BodyTagTemplateAttributed<InputOptionTag,Option> {

    public Option(TttPageContext pageContext, InputOptionTag bodyTag) {
        super(pageContext, bodyTag);
    }

    public Option setValue(Object value) {
        bodyTag.setValue(value);
        return this;
    }

    public Option setLabel(String label) {
        bodyTag.setLabel(label);
        return this;
    }

    public Option setSelected(String selected) {
        bodyTag.setSelected(selected);
        return this;
    }

}
