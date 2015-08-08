package com.pojosontheweb.ttt.stripes2;

import com.pojosontheweb.ttt.jsptags.BodyTagSubTemplate;
import com.pojosontheweb.ttt.jsptags.TttPageContext;
import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputSubmitTag;
import net.sourceforge.stripes.tag.InputTextTag;

public class Form extends BodyTagSubTemplate<FormTag> {

    public Form(TttPageContext pageContext, FormTag bodyTag) {
        super(pageContext, bodyTag);
    }

    public Text text(InputTextTag textTag) {
        return new Text(pageContext, textTag, this.bodyTag);
    }

    public Text text(String name) {
        InputTextTag t = new InputTextTag();
        t.setName(name);
        return text(t);
    }

    public Submit submit(InputSubmitTag submitTag) {
        return new Submit(pageContext, submitTag, this.bodyTag);
    }

    public Submit submit(String name) {
        InputSubmitTag s = new InputSubmitTag();
        s.setName(name);
        return submit(s);
    }

    public Submit submit(String name, String value) {
        Submit s = submit(name);
        s.getTag().setValue(value);
        return s;
    }

}
