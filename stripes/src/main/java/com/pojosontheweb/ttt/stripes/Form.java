package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.BodyTagSubTemplate;
import com.pojosontheweb.ttt.jsptags.TttPageContext;
import net.sourceforge.stripes.tag.*;

public class Form extends BodyTagSubTemplate<FormTag> {

    public Form(TttPageContext pageContext, FormTag formTag) {
        super(pageContext, formTag);
    }

    public Text text(InputTextTag textTag) {
        return new Text(pageContext, textTag, this.bodyTag);
    }

    public Text text(String name) {
        InputTextTag t = new InputTextTag();
        t.setName(name);
        return text(t);
    }

    public Checkbox checkbox(InputCheckBoxTag checkBoxTag) {
        return new Checkbox(pageContext, checkBoxTag, this.bodyTag);
    }

    public Checkbox checkbox(String name) {
        InputCheckBoxTag checkBoxTag = new InputCheckBoxTag();
        checkBoxTag.setName(name);
        return checkbox(checkBoxTag);
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

    public Password password(InputPasswordTag inputPasswordTag) {
        return new Password(pageContext, inputPasswordTag, this.bodyTag);
    }

    public Password password(String name) {
        InputPasswordTag p = new InputPasswordTag();
        p.setName(name);
        return password(p);
    }
}
