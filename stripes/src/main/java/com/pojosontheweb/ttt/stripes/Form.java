package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.BodyTagTemplate;
import com.pojosontheweb.ttt.jsptags.TttPageContext;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.tag.*;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Form extends BodyTagTemplate<FormTag> {

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

    public Select select(InputSelectTag selectTag) {
        Select s = new Select(pageContext, selectTag);
        s.open(out);
        return s;
    }

    public Select select(String name) {
        InputSelectTag inputSelectTag = new InputSelectTag();
        inputSelectTag.setName(name);
        inputSelectTag.setParent(getBodyTag());
        return select(inputSelectTag);
    }

    public List<ValidationError> errors(String fieldName) {
        ExecutionContext ec = ExecutionContext.currentContext();
        ActionBeanContext abc = ec.getActionBeanContext();
        List<ValidationError> res = null;
        if (abc != null) {
            ValidationErrors errors = abc.getValidationErrors();
            if (errors != null) {
                res = errors.get(fieldName);
            }
        }
        return res == null ? Collections.emptyList() : res;
    }

}