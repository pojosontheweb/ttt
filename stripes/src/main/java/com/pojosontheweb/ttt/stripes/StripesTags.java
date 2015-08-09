package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TagLib;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.format.Formatter;
import net.sourceforge.stripes.format.FormatterFactory;
import net.sourceforge.stripes.tag.*;

import java.io.Writer;
import java.util.Locale;

import static com.pojosontheweb.ttt.Util.toRtEx;

public class StripesTags extends TagLib {

    public StripesTags(Writer out) {
        super(out);
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

    public Link link(LinkTag linkTag) {
        Link l = new Link(pageContext, linkTag);
        l.open(out);
        return l;
    }

    public Link link(Class<? extends ActionBean> beanClass) {
        LinkTag linkTag = new LinkTag();
        linkTag.setBeanclass(beanClass);
        return link(linkTag);
    }

    public Link link(String url) {
        LinkTag linkTag = new LinkTag();
        linkTag.setUrl(url);
        return link(linkTag);
    }

    public Url url(UrlTag urlTag) {
        return new Url(pageContext, urlTag);
    }

    public Url url(Class<? extends ActionBean> beanClass) {
        UrlTag urlTag = new UrlTag();
        urlTag.setBeanclass(beanClass);
        return url(urlTag);
    }

    public Messages messages(MessagesTag messagesTag) {
        return new Messages(pageContext, messagesTag);
    }

    public Messages messages() {
        return messages(new MessagesTag());
    }

    public Errors errors(ErrorsTag errorsTag) {
        return new Errors(pageContext, errorsTag);
    }

    public Errors errors() {
        return errors(new ErrorsTag());
    }

    @SuppressWarnings("unchecked")
    public String format(Object value, String formatType, String formatPattern) {
        if (value == null)
            return "";

        Locale locale = ExecutionContext.currentContext().getActionBeanContext().getLocale();
        FormatterFactory factory = StripesFilter.getConfiguration().getFormatterFactory();
        Formatter formatter = factory.getFormatter(
            value.getClass(),
            locale,
            formatType,
            formatPattern);
        if (formatter == null)
            return String.valueOf(value);
        else
            return formatter.format(value);
    }

    public String format(Object value, String formatType) {
        return format(value, formatType, null);
    }

    public String format(Object value) {
        return format(value, null, null);
    }

    public Label label(InputLabelTag t) {
        Label label = new Label(pageContext, t);
        label.open(out);
        return label;
    }

    public Label label(String forId) {
        InputLabelTag l = new InputLabelTag();
        l.setFor(forId);
        return label(l);
    }
}
