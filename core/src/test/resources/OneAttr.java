import com.pojosontheweb.ttt.Template;

import java.io.IOException;

public class OneAttr extends Template {

    private final com.xyz.myapp.Foo foo;

    public OneAttr(
        com.xyz.myapp.Foo foo
    ) {
        this.foo = foo;
    }

    // generated
    public void render(Writer out) throws IOException {
        out.write("this is a ");
        out.write( foo.getBar() );
        out.write(" template")
    }

}