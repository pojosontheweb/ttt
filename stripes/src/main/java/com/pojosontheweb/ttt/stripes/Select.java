package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.BodyTagTemplate;
import com.pojosontheweb.ttt.jsptags.TttPageContext;
import net.sourceforge.stripes.tag.InputOptionsCollectionTag;
import net.sourceforge.stripes.tag.InputOptionsEnumerationTag;
import net.sourceforge.stripes.tag.InputSelectTag;

import java.util.Collection;

public class Select extends BodyTagTemplate<InputSelectTag> {

    public Select(TttPageContext pageContext, InputSelectTag bodyTag) {
        super(pageContext, bodyTag);
    }

    public OptionsCollection options(InputOptionsCollectionTag ioct) {
        return new OptionsCollection(pageContext, ioct, getBodyTag());
    }

    public OptionsCollection options(Collection<?> collection) {
        InputOptionsCollectionTag i = new InputOptionsCollectionTag();
        i.setCollection(collection);
        return options(i);
    }

    public OptionsEnumeration options(InputOptionsEnumerationTag ioet) {
        return new OptionsEnumeration(pageContext, ioet, getBodyTag());
    }

    public OptionsEnumeration options(Class<?> enumClass) {
        InputOptionsEnumerationTag i = new InputOptionsEnumerationTag();
        i.setEnum(enumClass.getName());
        return options(i);
    }
}
