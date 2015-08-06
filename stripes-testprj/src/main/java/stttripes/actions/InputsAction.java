package stttripes.actions;

import net.sourceforge.stripes.action.*;
import stttripes.templates.InputsTemplate;

import java.util.List;

public class InputsAction extends ActionBase {

    private String text;
    private String password;
    private boolean checkbox;

    @DefaultHandler
    @DontBind
    public Resolution display() {
        return resolution("inputs demo", new InputsTemplate(this));
    }

    public Resolution doStuff() {
        List<Message> messages = getContext().getMessages();
        messages.add(new SimpleMessage("Stuff's been done."));
        messages.add(new SimpleMessage("text=" + text));
        messages.add(new SimpleMessage("password=" + password));
        messages.add(new SimpleMessage("checkbox=" + checkbox));
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

    public boolean getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }
}
