package stttripes.actions;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import stttripes.templates.InputsTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputsAction extends ActionBase implements ValidationErrorHandler {

    @Validate(required = true)
    private String text;
    private String password;
    private boolean checkbox1;
    private boolean checkbox2;
    private String textFromSelect;
    private Long myObjId;
    private MyEnum myEnum;
    private Integer fromSelectMap;

    @DefaultHandler
    @DontValidate
    public Resolution display() {
        return resolution("inputs demo", new InputsTemplate(this));
    }

    public Resolution doStuff() {
        List<Message> messages = getContext().getMessages();
        messages.add(new SimpleMessage("Stuff's been done."));
        messages.add(new SimpleMessage("text=" + text));
        messages.add(new SimpleMessage("password=" + password));
        messages.add(new SimpleMessage("checkbox1=" + checkbox1));
        messages.add(new SimpleMessage("checkbox2=" + checkbox2));
        messages.add(new SimpleMessage("textFromSelect=" + textFromSelect));
        messages.add(new SimpleMessage("myObjId=" + myObjId));
        messages.add(new SimpleMessage("myEnum=" + myEnum));
        messages.add(new SimpleMessage("fromSelectMap=" + fromSelectMap));
        return new RedirectResolution(InputsAction.class).flash(this);
    }

    @DontBind
    public Resolution reset() {
        List<Message> messages = getContext().getMessages();
        messages.add(new SimpleMessage("Reset !"));
        return new RedirectResolution(InputsAction.class);
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCheckbox1() {
        return checkbox1;
    }

    public void setCheckbox1(boolean checkbox1) {
        this.checkbox1 = checkbox1;
    }

    public boolean isCheckbox2() {
        return checkbox2;
    }

    public void setCheckbox2(boolean checkbox2) {
        this.checkbox2 = checkbox2;
    }

    @Override
    public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
        return display();
    }

    public List<String> getStrings() {
        return Arrays.asList("foo", "bar", "baz");
    }

    public List<MyObj> getMyObjects() {
        return Arrays.asList(
            new MyObj(1L, "foo"),
            new MyObj(2L, "bar"),
            new MyObj(3L, "baz")
        );
    }

    public Map<Integer,String> getMyMap() {
        HashMap<Integer,String> m = new HashMap<>();
        m.put(1, "foo");
        m.put(2, "bar");
        m.put(3, "baz");
        return m;
    }

    public String getTextFromSelect() {
        return textFromSelect;
    }

    public void setTextFromSelect(String textFromSelect) {
        this.textFromSelect = textFromSelect;
    }

    public Long getMyObjId() {

        return myObjId;
    }

    public void setMyObjId(Long myObjId) {
        this.myObjId = myObjId;
    }

    public MyEnum getMyEnum() {
        return myEnum;
    }

    public void setMyEnum(MyEnum myEnum) {
        this.myEnum = myEnum;
    }

    public Integer getFromSelectMap() {
        return fromSelectMap;
    }

    public void setFromSelectMap(Integer fromSelectMap) {
        this.fromSelectMap = fromSelectMap;
    }
}
