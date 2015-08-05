package stttripes.actions;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import stttripes.templates.HomeTemplate;

@UrlBinding("/")
public class HomeAction extends ActionBase {

    @DefaultHandler
    public Resolution home() {
        return resolution("home", new HomeTemplate());
    }
}
