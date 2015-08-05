package stttripes.actions;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontBind;
import net.sourceforge.stripes.action.Resolution;
import stttripes.templates.InputsTemplate;

public class InputsAction extends ActionBase {

    @DefaultHandler
    @DontBind
    public Resolution display() {
        return resolution("inputs demo", new InputsTemplate());

    }

}
