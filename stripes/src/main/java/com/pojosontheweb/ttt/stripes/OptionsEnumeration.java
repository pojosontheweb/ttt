package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TagTemplate;
import net.sourceforge.stripes.tag.InputOptionsEnumerationTag;
import net.sourceforge.stripes.tag.InputSelectTag;

import javax.servlet.jsp.PageContext;

public class OptionsEnumeration extends TagTemplate<InputOptionsEnumerationTag,InputSelectTag> {

    public OptionsEnumeration(PageContext pageContext, InputOptionsEnumerationTag tag, InputSelectTag parent) {
        super(pageContext, tag, parent);
    }


}
