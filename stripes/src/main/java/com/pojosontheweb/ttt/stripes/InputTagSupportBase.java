package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TagTemplate;
import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputTagSupport;
import net.sourceforge.stripes.validation.ValidationError;

import javax.servlet.jsp.PageContext;
import java.util.List;

import static com.pojosontheweb.ttt.Util.toRtEx;
import static com.pojosontheweb.ttt.Util.withEmptyListIfNull;

public abstract class InputTagSupportBase<T extends InputTagSupport, V extends InputTagSupportBase<T, V>> extends TagTemplate<T,FormTag> implements HasAttributes<V>{

    public InputTagSupportBase(PageContext pageContext, T tag, FormTag parent) {
        super(pageContext, tag, parent);
    }

    public List<ValidationError> getErrors() {
        return withEmptyListIfNull(toRtEx(tag::getFieldErrors));
    }

    @Override
    public V set(String name, Object value) {
        toRtEx(() -> tag.setDynamicAttribute(null, name, value));
        @SuppressWarnings("unchecked") V v = (V)this;
        return v;
    }


}
