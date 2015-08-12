package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TttPageContext;
import net.sourceforge.stripes.tag.InputButtonTag;

public class Button extends BodyTagTemplateAttributed<InputButtonTag, Button> {

    public Button(TttPageContext pageContext, InputButtonTag bodyTag) {
        super(pageContext, bodyTag);
    }

}
