package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.Util;
import com.pojosontheweb.ttt.jsptags.TagTemplate;
import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputSubmitTag;

import javax.servlet.jsp.PageContext;

import static com.pojosontheweb.ttt.Util.toRtEx;

public class Submit extends TagTemplate<InputSubmitTag,FormTag> implements HasAttributes<Submit> {

    public Submit(PageContext pageContext, InputSubmitTag tag, FormTag parent) {
        super(pageContext, tag, parent);
    }

    @Override
    public Submit set(String name, Object value) {
		Util.toRtExNoResult(() -> tag.setDynamicAttribute(null, name, value));
        return this;
    }
}
