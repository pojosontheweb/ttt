package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TagTemplate;
import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputTagSupport;
import net.sourceforge.stripes.validation.ValidationError;

import javax.servlet.jsp.PageContext;
import java.util.List;

import static com.pojosontheweb.ttt.Util.toRtEx;
import static com.pojosontheweb.ttt.Util.withEmptyListIfNull;

public abstract class InputTagSupportBase<T extends InputTagSupport> extends TagTemplate<T,FormTag> {

    public InputTagSupportBase(PageContext pageContext, T tag, FormTag parent) {
        super(pageContext, tag, parent);
    }

    public List<ValidationError> getErrors() {
        return withEmptyListIfNull(toRtEx(tag::getFieldErrors));
    }
}
