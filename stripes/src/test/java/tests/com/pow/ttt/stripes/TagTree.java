package tests.com.pow.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TttTagTree;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.DispatcherServlet;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.mock.MockServletContext;
import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputTextTag;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vankeisb on 07/08/15.
 */
public class TagTree {

    public static void main(String[] args)  throws Exception {
        MockServletContext servletContext = createMockServletContext();
        try ( Writer out = new OutputStreamWriter(System.out)) {

            FormTag form = new FormTag();
            form.setBeanclass(MyAction.class);

            TttTagTree.Node formNode = new TttTagTree.Node(form, null, null);
            TttTagTree t = new TttTagTree(servletContext, out, formNode);

            InputTextTag input = new InputTextTag();
            input.setName("myProp");
            formNode.addChild(input);

            InputTextTag input2 = new InputTextTag();
            input2.setName("myProp2");
            formNode.addChild(input2);

            t.render();

        }

    }

    private static MockServletContext createMockServletContext() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("ActionResolver.Packages", "test.com.pow.ttt.stripes");
        params.put("Extension.Packages", "woko.actions.nestedvalidation");
        MockServletContext mockServletContext = new MockServletContext("nestedvalidation");
        mockServletContext.addFilter(StripesFilter.class, "StripesFilter", params);
        mockServletContext.setServlet(DispatcherServlet.class, "DispatcherServlet", null);
        return mockServletContext;
    }

}
