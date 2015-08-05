package stttripes.actions;

import com.pojosontheweb.ttt.ITemplate;
import com.pojosontheweb.ttt.stripes.TemplateResolution;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import stttripes.templates.PageTemplate;

public abstract class ActionBase implements ActionBean {

    private ActionBeanContext context;

    @Override
    public ActionBeanContext getContext() {
        return context;
    }

    @Override
    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    protected Resolution resolution(String pageTitle, ITemplate pageBody) {
        return new TemplateResolution(
            new PageTemplate(pageTitle, pageBody)
        );
    }
}
