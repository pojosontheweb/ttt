package com.pojosontheweb.ttt;

import java.io.IOException;
import java.io.Writer;

public interface ITemplate {

    void render(Writer out) throws IOException;

}
