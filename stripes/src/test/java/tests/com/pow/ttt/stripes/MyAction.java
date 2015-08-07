package tests.com.pow.ttt.stripes;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/my")
public class MyAction implements ActionBean {

    private String myProp;
    private String myProp2;

    private ActionBeanContext context;

    @Override
    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    @Override
    public ActionBeanContext getContext() {
        return context;
    }

    public String getMyProp() {
        return myProp;
    }

    public void setMyProp(String myProp) {
        this.myProp = myProp;
    }

    public String getMyProp2() {
        return myProp2;
    }

    public void setMyProp2(String myProp2) {
        this.myProp2 = myProp2;
    }
}
