package com.pojosontheweb.ttt.stripes2;

import com.pojosontheweb.ttt.TttWriter;
import com.pojosontheweb.ttt.jsptags.TttPageContext;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.tag.FormTag;

import java.io.Writer;

import static com.pojosontheweb.ttt.Util.toRtEx;

public class Tags {

    private final TttPageContext pageContext;
    private final TttWriter out;

    public Tags(Writer out) {
        this.out = (TttWriter)out;
        ExecutionContext ec = ExecutionContext.currentContext();
        ActionBeanContext context = ec.getActionBeanContext();
        pageContext = new TttPageContext(out, context.getServletContext(), context.getRequest(), context.getResponse());
    }

    public Form form(FormTag formTag) {
        Form f = new Form(pageContext, formTag);
        f.open(out);
        return f;
    }

    public Form form(Class<? extends ActionBean> beanClass) {
        return toRtEx(() -> {
            FormTag ft = new FormTag();
            ft.setBeanclass(beanClass);
            return form(ft);
        });
    }

}
