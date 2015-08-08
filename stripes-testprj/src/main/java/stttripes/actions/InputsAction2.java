package stttripes.actions;

import net.sourceforge.stripes.action.*;
import stttripes.templates.InputsTemplate;
import stttripes.templates.InputsTemplate2;

import java.util.List;

public class InputsAction2 extends ActionBase {

    private String text;

    @DefaultHandler
    @DontBind
    public Resolution display() {
        return resolution("inputs demo", new InputsTemplate2(this));
    }

    public Resolution doStuff() {
        List<Message> messages = getContext().getMessages();
        messages.add(new SimpleMessage("Stuff's been done."));
        return new RedirectResolution(InputsAction2.class).flash(this);
    }

    public Resolution reset() {
        List<Message> messages = getContext().getMessages();
        messages.add(new SimpleMessage("Reset !"));
        return new RedirectResolution(InputsAction2.class);
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
