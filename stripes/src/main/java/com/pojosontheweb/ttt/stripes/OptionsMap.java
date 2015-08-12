package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TagTemplate;
import net.sourceforge.stripes.tag.InputOptionsMapTag;
import net.sourceforge.stripes.tag.InputSelectTag;

import javax.servlet.jsp.PageContext;

public class OptionsMap extends TagTemplate<InputOptionsMapTag,InputSelectTag> {

    public OptionsMap(PageContext pageContext, InputOptionsMapTag tag, InputSelectTag parent) {
        super(pageContext, tag, parent);
    }

}
