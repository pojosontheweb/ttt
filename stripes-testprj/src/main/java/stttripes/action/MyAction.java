package stttripes.action;

import com.pojosontheweb.ttt.stripes.TemplateResolution;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import stttripes.templates.MyTemplate;
import stttripes.templates.MyTemplateForm;

@UrlBinding("/my")
public class MyAction extends ActionBase {

    @Validate(required = true)
    private String myProp;

    public String getMyProp() {
        return myProp;
    }

    public void setMyProp(String myProp) {
        this.myProp = myProp;
    }

    @DefaultHandler
    @DontBind
    public Resolution display() {
        return new StreamingResolution("text/plain", "It Works!");
    }

    @DontBind
    public Resolution template() {
        return new TemplateResolution(new MyTemplate("hey there", 123));
    }

    public Resolution form() {
        return new TemplateResolution(new MyTemplateForm(myProp));
    }

}
