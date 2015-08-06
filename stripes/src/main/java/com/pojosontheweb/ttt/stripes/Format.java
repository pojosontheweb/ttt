package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.Template;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.format.Formatter;
import net.sourceforge.stripes.format.FormatterFactory;

import java.io.IOException;
import java.io.Writer;

public class Format extends Template {

    private final Object value;
    private String formatType;
    private String formatPattern;

    public Format(Object value) {
        this.value = value;
    }

    public Format setFormatType(String formatType) {
        this.formatType = formatType;
        return this;
    }

    public Format setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
        return this;
    }

    @Override
    public void render(Writer out) throws IOException {
        write(out, build());
    }

    @SuppressWarnings("unchecked")
    public String build() {
        if (value == null)
            return "";

        FormatterFactory factory = StripesFilter.getConfiguration().getFormatterFactory();
        Formatter formatter = factory.getFormatter(value.getClass(),
            StripesTags.getRequest().getLocale(),
            this.formatType,
            this.formatPattern);
        if (formatter == null)
            return String.valueOf(value);
        else
            return formatter.format(value);
    }

}
