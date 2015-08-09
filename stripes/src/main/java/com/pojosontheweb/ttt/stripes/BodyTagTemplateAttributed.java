package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.BodyTagTemplate;
import com.pojosontheweb.ttt.jsptags.TttPageContext;
import net.sourceforge.stripes.tag.HtmlTagSupport;

import javax.servlet.jsp.tagext.BodyTag;

import static com.pojosontheweb.ttt.Util.toRtEx;

public abstract class BodyTagTemplateAttributed<T extends BodyTag, V extends BodyTagTemplateAttributed<T,V>>
    extends BodyTagTemplate<T> implements HasAttributes<V> {

    public BodyTagTemplateAttributed(TttPageContext pageContext, T bodyTag) {
        super(pageContext, bodyTag);
    }

    @Override
    public V set(String name, Object value) {
        HtmlTagSupport hts = (HtmlTagSupport)bodyTag;
        toRtEx(() -> hts.setDynamicAttribute(null, name, value));
        @SuppressWarnings("unchecked") V v = (V)this;
        return v;
    }
}
