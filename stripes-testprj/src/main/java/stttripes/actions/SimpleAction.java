package stttripes.actions;

import net.sourceforge.stripes.action.*;
import stttripes.templates.SimpleTemplate;

@UrlBinding("/simple")
public class SimpleAction extends ActionBase {

    private String myProp;

    public String getMyProp() {
        return myProp;
    }

    public void setMyProp(String myProp) {
        this.myProp = myProp;
    }

    public Resolution display() {
        return resolution("simple", new SimpleTemplate(myProp));
    }

}
