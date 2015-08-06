package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.Template;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.format.Formatter;
import net.sourceforge.stripes.format.FormatterFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.function.Supplier;

public class Format extends Template implements Supplier<String> {

    private final Object value;
    private final String formatType;
    private final String formatPattern;
    private final String formmatted;

    public Format(Object value, String formatType, String formatPattern) {
        this.value = value;
        this.formatType = formatType;
        this.formatPattern = formatPattern;
        formmatted = format();
    }

    @SuppressWarnings("unchecked")
    private String format() {
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

    @Override
    public String get() {
        return formmatted;
    }

    @Override
    public void render(Writer out) throws IOException {
        write(out, get());
    }
}
