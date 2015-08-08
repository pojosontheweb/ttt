package com.pojosontheweb.ttt;

import java.io.Writer;

public interface ITemplate {

    String getContentType();

    void render(Writer out);

}
