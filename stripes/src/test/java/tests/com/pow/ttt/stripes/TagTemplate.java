package tests.com.pow.ttt.stripes;

import com.mockobjects.servlet.MockPageContext;
import com.pojosontheweb.ttt.jsptags.TttJspWriter;
import net.sourceforge.stripes.controller.DispatcherServlet;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.mock.MockHttpServletRequest;
import net.sourceforge.stripes.mock.MockServletContext;
import net.sourceforge.stripes.tag.FormatTag;

import javax.servlet.jsp.JspException;

import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class TagTemplate {

    private static MockServletContext createMockServletContext() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("ActionResolver.Packages", "woko.actions");
        params.put("Extension.Packages", "woko.actions.nestedvalidation");
        MockServletContext mockServletContext = new MockServletContext("nestedvalidation");
        mockServletContext.addFilter(StripesFilter.class, "StripesFilter", params);
        mockServletContext.setServlet(DispatcherServlet.class, "DispatcherServlet", null);
        return mockServletContext;
    }

    public static void main(String[] args) throws JspException {

        MockServletContext servletContext = createMockServletContext();
        MockHttpServletRequest request = new MockHttpServletRequest("/foo", "/bar");
        MockPageContext pageContext = new MockPageContext();
        pageContext.setServletContext(servletContext);
        pageContext.setRequest(request);

        try ( PrintWriter out = new PrintWriter(System.out)) {

            TttJspWriter jspWriter = new TttJspWriter(out);
            pageContext.setJspWriter(jspWriter);

            FormatTag fmt = new FormatTag();
            fmt.setValue(1234.333333);
            fmt.setFormatType("number");
            fmt.setFormatPattern("#,##0.## brouzoufs");
            fmt.setPageContext(pageContext);

            // start tag
            int res = fmt.doStartTag();

            switch (res) {
                case Tag.SKIP_BODY :
                    // don't eval body, call end tag
                    int res2 = fmt.doEndTag();
                    switch (res2) {
                        case Tag.SKIP_PAGE :
                            // supposed to skip the rest of the page...
                            // TODO
                            System.out.println("skip");
                            break;
                        case Tag.EVAL_PAGE :
                            // continue normally
                            noop();
                            break;
                    }
                    break;

                case Tag.EVAL_BODY_INCLUDE:
                    noop();
                    break;

                case BodyTag.EVAL_BODY_BUFFERED:
                    noop();
                    break;
            }
        }
    }

    private static void noop() {
        String s = "";
    }

    private void doTag(Tag tag) throws Exception {
        switch (tag.doStartTag()) {
            case Tag.SKIP_BODY :
                switch(tag.doEndTag()) {
                    case Tag.SKIP_PAGE :
                        // nothing to do
                        break;
                }
                break;
            case Tag.EVAL_BODY_INCLUDE:
        }
    }



}
