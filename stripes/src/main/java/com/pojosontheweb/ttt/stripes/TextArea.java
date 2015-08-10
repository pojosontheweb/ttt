package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TttPageContext;
import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputTextAreaTag;

public class TextArea extends BodyTagTemplateAttributed<InputTextAreaTag,TextArea> {

    public TextArea(TttPageContext pageContext, InputTextAreaTag bodyTag, FormTag formTag) {
        super(pageContext, bodyTag);
        bodyTag.setParent(formTag);
    }

}
