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
    private final String formatted;

    Format(Object value, String formatType, String formatPattern) {
        this.value = value;
        this.formatType = formatType;
        this.formatPattern = formatPattern;
        if (value == null) {
            this.formatted = "";
        } else {
            this.formatted = format();
        }
    }

    @SuppressWarnings("unchecked")
    private String format() {
        FormatterFactory factory = StripesFilter.getConfiguration().getFormatterFactory();
        Formatter formatter = factory.getFormatter(value.getClass(),
            StripesTags.getRequest().getLocale(),
            this.formatType,
            this.formatPattern);
        return formatter == null ? String.valueOf(value) : formatter.format(value);
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
        return new Format(value, formatType, formatPattern);
    }

    public Format setFormatType(String formatType) {
        return new Format(value, formatType, formatPattern);
    }

    public Format setFormatPattern(String formatPattern) {
        return new Format(value, formatType, formatPattern);
    }

}
