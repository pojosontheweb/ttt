package com.pojosontheweb.ttt.stripes;

public class Checkbox extends HtmlTag.InputWithoutBody {

    public Checkbox(Form form, String name) {
        this(form, name, false);
    }

    public Checkbox(Form form, String name, boolean checked) {
        super(new InputDelegate("input", form, "checkbox", name), null);
        // TODO we need to remove the "value" attribute as
        // once it is bound it is not modified by
        // checking/unchecking...
        this.attributes = this.attributes.unset("value");
        if (delegate.isItemSelected() || checked) {
            this.attributes = this.attributes.set("checked", "checked");
        }
    }

    public Checkbox setChecked(boolean checked) {
        return new Checkbox(delegate.form, delegate.name, checked);
    }

}
