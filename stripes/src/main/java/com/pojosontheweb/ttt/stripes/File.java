package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputFileTag;

import javax.servlet.jsp.PageContext;

public class File extends InputTagSupportBase<InputFileTag,File> {

    public File(PageContext pageContext, InputFileTag tag, FormTag parent) {
        super(pageContext, tag, parent);
    }
}
