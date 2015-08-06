package stttripes.actions;

import net.sourceforge.stripes.action.*;
import stttripes.templates.InputsTemplate;

import java.util.List;

public class InputsAction extends ActionBase {

    private String text;
    private String password;

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
}
