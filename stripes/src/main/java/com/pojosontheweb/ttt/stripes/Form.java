package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TttPageContext;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.tag.*;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.Collections;
import java.util.List;

import static com.pojosontheweb.ttt.Util.toRtEx;

public class Form extends BodyTagTemplateAttributed<FormTag,Form> {

    public Form(TttPageContext pageContext, FormTag formTag) {
        super(pageContext, formTag);
    }

    public Text text(InputTextTag textTag) {
        return new Text(pageContext, textTag, bodyTag);
    }

    public Text text(String name) {
        InputTextTag t = new InputTextTag();
        t.setName(name);
        return text(t);
    }

    public Checkbox checkbox(InputCheckBoxTag checkBoxTag) {
        return new Checkbox(pageContext, checkBoxTag, bodyTag);
    }

    public Checkbox checkbox(String name) {
        InputCheckBoxTag checkBoxTag = new InputCheckBoxTag();
        checkBoxTag.setName(name);
        return checkbox(checkBoxTag);
    }

    public Submit submit(InputSubmitTag submitTag) {
        return new Submit(pageContext, submitTag, bodyTag);
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
        return new Password(pageContext, inputPasswordTag, bodyTag);
    }

    public Password password(String name) {
        InputPasswordTag p = new InputPasswordTag();
        p.setName(name);
        return password(p);
    }

    public Select select(InputSelectTag selectTag) {
        Select s = new Select(pageContext, selectTag, bodyTag);
        s.open(out);
        return s;
    }

    public Select select(String name) {
        InputSelectTag inputSelectTag = new InputSelectTag();
        inputSelectTag.setName(name);
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

    @Override
    public Form set(String name, Object value) {
        toRtEx(() -> bodyTag.setDynamicAttribute(null, name, value));
        return this;
    }

    public Radio radio(InputRadioButtonTag r) {
        return new Radio(pageContext, r, bodyTag);
    }

    public Radio radio(String name) {
        InputRadioButtonTag r = new InputRadioButtonTag();
        r.setName(name);
        return radio(r);
    }

    public File file(InputFileTag t) {
        return new File(pageContext, t, bodyTag);
    }

    public File file(String name) {
        InputFileTag t = new InputFileTag();
        t.setName(name);
        return file(t);
    }

    public Hidden hidden(InputHiddenTag t) {
        return new Hidden(pageContext, t, bodyTag);
    }

    public Hidden hidden(String name) {
        InputHiddenTag t = new InputHiddenTag();
        t.setName(name);
        return hidden(t);
    }

    public TextArea textarea(String name) {
        InputTextAreaTag t = new InputTextAreaTag();
        t.setName(name);
        return textarea(t);
    }

    public TextArea textarea(InputTextAreaTag t) {
        TextArea ta = new TextArea(pageContext, t, bodyTag);
        ta.open(out);
        return ta;
    }
}