package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.Template;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.format.Formatter;
import net.sourceforge.stripes.format.FormatterFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.function.Supplier;

public class Format extends Template implements Supplier<String> {

    private Object value;
    private String formatType;
    private String formatPattern;
    private String formatted;

    public Format(Object value) {
        this.value = value;
        format();
    }

    @SuppressWarnings("unchecked")
    private void format() {
        if (value == null) {
            formatted = "";
        } else {
            FormatterFactory factory = StripesFilter.getConfiguration().getFormatterFactory();
            Formatter formatter = factory.getFormatter(value.getClass(),
                StripesTags.getRequest().getLocale(),
                this.formatType,
                this.formatPattern);
            formatted = formatter == null ? String.valueOf(value) : formatter.format(value);
        }
    }

    @Override
    public String get() {
        format();
        return formatted;
    }

    @Override
    public void render(Writer out) throws IOException {
        write(out, get());
    }

    public Object getValue() {
        return value;
    }

    public String getFormatType() {
        return formatType;
    }

    public String getFormatPattern() {
        return formatPattern;
    }

    public Format setValue(Object value) {
        this.value = value;
        return this;
    }

    public Format setFormatType(String formatType) {
        this.formatType = formatType;
        return this;
    }

    public Format setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
        return this;
    }

}
