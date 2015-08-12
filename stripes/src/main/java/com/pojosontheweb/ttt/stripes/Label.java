package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TttPageContext;
import net.sourceforge.stripes.tag.InputLabelTag;

public class Label extends BodyTagTemplateAttributed<InputLabelTag,Label> {

    public Label(TttPageContext pageContext, InputLabelTag bodyTag) {
        super(pageContext, bodyTag);
    }
}
