# TTT Stripes Integration

TTT templates can be used with Stripes very easily. They integrate 
smoothly, and fill in the gap between ActionBeans and JSPs.

## Configuration (maven)

In order to use TTT with Stripes you first need to add the dependency
on `ttt-stripes` :

```xml
<dependency>
    <groupId>com.pojosontheweb</groupId>
    <artifactId>ttt-stripes</artifactId>
    <version>0.1-beta</version>
</dependency>
```
        
Then, add the TTT compiler plugin :

```xml
<plugin>
	<groupId>com.pojosontheweb</groupId>
	<artifactId>ttt-maven-plugin</artifactId>
	<version>0.1-beta</version>
	<executions>
		<execution>
			<id>ttt-compile</id>
			<phase>generate-sources</phase>
			<goals>
				<goal>ttt</goal>
			</goals>
			<configuration>
				<sourceDirectory>${project.basedir}/src/main/java</sourceDirectory>
			</configuration>
		</execution>
	</executions>
</plugin>
```

> We use `src/main/java` but of course you can put your templates 
> anywere you see fit. It's just easier to have them all under the same 
> source root.

And that's it ! You're ready to ride. 

## Writing Templates

Writing templates is basically like writing any TTT template. 

Nevertheless, Stripes' tag library is not directly available 
for TTT templates (remember, they ain't no JSPs...).

Therefore, TTT-Stripes provides the same features as the JSP tags : `<s:form>`,
`<s:text>` etc, but packaged differently, so that they are easy to use 
from TTT templates. 

The "tag lib" is detailed in [this section](#tag-lib)  
  
Have a look at the templates in the 
[stripes-testprj](https://github.com/pojosontheweb/ttt/tree/master/stripes-testprj/src/main/java/stttripes/templates) module
for examples.

## Rendering Templates

Cannot be easier :

```java
return new TttResolution( template );
```

The class `TttResolution` is a dedicated Stripes resolution that renders the 
template into the HTTP response.
 
## Example

The TTT template `MyTemplate.ttt` :

```jsp
<%!
	// gimmme the action !
	MyActionBean myAction;
%>
<html>
...
<body>
	...
	
	<%-- spit out a prop of the ActionBean... --%>
	<%= myAction.getMyValue() %>
	...
</body>
</html>
```

And the ActionBean :

```java
@UrlBinding("/my")
public class MyActionBean implements ActionBean {

	...
			
	@Validate(required = true)
	private String value; 
	
	...
			
	public Resolution display() {
		...			
		// render the 'MyTemplate' as the resolution
		return new TttResolution( new MyTemplate(this) ); 		
	}

}
```

As you see here, the template is bound to the action by static 
type checking. The action creates the template by calling the 
constructor, passing itself as the argument. Simple, and yet 
very effective.

> More examples are available in the [quickstart app](https://github.com/pojosontheweb/ttt-stripes-quickstart).

<a name="tag-lib"></a>
## Tag Library

The taglib's entry point is the class `StripesTags`. In order to 
use it, you just have to create it in your template :

```jsp
<%
	// create the stripes taglib
	StripesTags stripes = new StripesTags(out);
%>
<div class="foo">
	<%= stripes.link(MyActionBean.class).setText("my link") %>
</div>
```
	
The tags try to expose a comprehensive and concise API, and make extensive 
use of chained method calls :

```jsp
<%= 
	stripes.link(MyAction.class)
		.addParameter("foo", "bar")
		.setEvent("doIt")
		.setText("do it !")
%>
```

Tags are grouped in 3 major categories :

* HTML Tags with a body, like `s:form`
* HTML Tags without a body, like `s:text`
* Helpers, like `s:format` or `s:url` 

### Tags with body

Those produce HTML tags that (optionally) have a body, like FORMs. They have 
to be used in scriptlets, along with try-resource statement :


```jsp
<%-- stripes FORM --%>
<% try ( Form f = stripes.form(MyAction.class) ) { %>

	<%-- form body --%>
	<div class="foo">
		<%= f.text("myProp") %>
	</div>
 
<% } %>
```

The `try-resource` statement provides the scope of the tag. Everything
inside the try block is rendered between the opening and closing tags.

The declared variable in the `try` statement serves an as entry point 
for nested tag(s) (here `f.text(...)` creates a text input for the form.
	
### Body-less tags (void elements) and Helpers

Body-less tags produce HTML tags that have no body (void elements), or just plain text. 
They are used in expression blocks :

```jsp
<a href="<%= stripes.url(MyAction.class) %>">
	my action !
</a>
```

## Tag Reference

### s:format

Formats a value using registered Stripes Formatters.

```jsp
<span class="foo">	
	<%= stripes.format( myObj ) %>
</span>
```

Supports formatting options just like `s:format` :

```jsp
<%=
  	stripes.format( myNumber )
  		.setFormatType( "number" )
  		.setFormatPattern ( "#,##0.## Kg" )
%>
```

### s:url	

Return an URL to an action bean.

```jsp
<%
	Url myHref = stripes.url( MyAction.class );
%>
<a href="<%= myHref %>">
	my link
</a>
```

### s:form

Generates a FORM to an action, and provides an entry point 
to input field tags.

```jsp
<% try (Form f = s.form(MyAction.class)) { %>
	<%-- form body --%>
<% } %>
```

### s:text

Generates a text input for a given FORM.

```jsp
<% try (Form form = s.form(MyAction.class)) { %>
	...	
	<%= form.text("myProp") %>
	...
<% } %>
```

The reference to the field can be used for further processing :

```jsp
<%
	// open a stripes form
	try (Form form = s.form(MyAction.class)) {
	
		// create the input but don't render now...
		Text myInput = form.text("myProp");
%>
    		<%-- render the input --%>
    		<%= myInput %>
    		
    		<%-- custom error handling, using the myInput object --%>
    		<%
    			if ( myInput.hasErrors() ) {
    		%>
	    			<div class="errors">
	    				<% for (ValidationError e : myInput.getErrors()) { %>
						<li>
							<%= e.getMessage( locale )%>
						</li>
	    				<% } %>
	    			</div>
    		<%
    			}
    		%>

<% 	
	} 
%>
```
