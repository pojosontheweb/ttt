# Typed Text Templates (aka TTT)

Static-typed text templates for Java.

## The problem

Most text templating libs do not provide strong, static typing. 

JSP, for instance, can be statically typed and checked at compile-type, 
but they don't provide any signature. Therefore, you have no context, 
and no way to know what the page arguments can be. You usually get
values from a non typed structure, like a Map<String,Object> (think 
about those casts when calling `request.getAttribute()` etc.).
Same applies to Groovy templates and others.
 
Also, we'd like to be able to compose templates easily. 
And again, compile-time checked.

## The idea
 
Just like a JSP, a TTT template is a mix of text and java code.
 
TTT uses a subset of the JSP syntax (see next section) : the templates can use 
script and/or expression blocks, mixed with plain text.

Most importantly, TTT templates define their *signature*, as typed arguments, 
just like a function would.  The signature defines the variables available inside the 
template. 

The templates are compiled at design-time to strong-typed Java code, that you 
use when you want to render templates. 

This provides for real compile-time error checking. The signature is the template's context : 
only template args can be used when designing the template, and you need to pass those args 
when rendering the template.

## Writing templates

Here's a simple template example from file `Foo.ttt` :

	<%!
		String foo;
		int bar;
	%>
	<ul class="foo">
	<% for (int i = 0 ; i < bar ; i++) { %>
		<div class="blah">
			hey, <%= foo %>
		</div>
		<li>
			<span>
				<%= i %>
			</span>
		</li>
	<% } %>
	</ul>
	
First declaration block is the template's signature, between `<%!` 
and `%>`. Arguments are declared like you declare local variables 
for a JSP. You don't need to set a value. 

Our template therefore has 2 arguments here :

1. foo (String)
2. bar (int)

Following is some text, with some scriptlets and expressions, again, just like a regular JSP.

### Comments

You may add a javadoc to the arguments of your template, it will be used in
the generated classes. The Ttt compiler will use the javadocs just 
before the arguments :

	<%!
		/**
		 * the foo for this template 
		 * multi-line if you want
		 */
		Foo foo;
		
		...
	%>
	
Apart from this, you can use JSP-like comments (enclosed in `<%--` and `--%>`) in the text, 
and code comments (`// ...` or `/* ... */`) in scriptlets or expressions.

### Nested Templates (composition)

You can nest templates easily by defining arguments that are templates themselves.

For example, here's an outer page template that "includes" a nested template (`Wrapper.ttt`):

	<%!
		/**
		 * the page title (header)
		 */
		String title;
		
		/**
		 * the body of the page, rendered in body tag
		 */
		com.pojosontheweb.ttt.ITemplate nested;
	%>
	<html>
		<head>
			<%-- title arg used here --%>
			<title><%= title %></title>
		</head>
		<body>
			<%-- body is another template --%>
			<%= nested %>
		</body>
	</html>

As you see, the `nested` arg of this template is of type 
`com.pojosontheweb.ttt.ITemplate`. This is the base type 
of all Ttt templates. 

You can now pass any other template to 
this "wrapper", and render the whole thing :

	new Wrapper("My Title", new MyOtherTemplate(...)).render(out);


### Polymorphism

You don't have to use the `ITemplate` base interface when passing templates
as arguments to other templates. For example, our `Wrapper.ttt` above might 
want to allow only certain templates as the body.

In order to do so, first define an empty interface for the 
template, right in your sources, that extends `ITemplate` :

	public interface BodyTemplate extends ITemplate { 
	}
	
Then, you can change the signature of the `Wrapper.ttt` template so that 
it accepts only `BodyTemplate` instances :

	<%!
        ...
		
		/**
		 * the body of the page, rendered in body tag 
		 * (must be a BodyTemplate !)
		 */
		com.myco.myapp.BodyTemplate nested;
	%>
	
Then, you may choose to implement this interface in 
any template by using the `extends` page directive (`MyBodyTemplate.ttt`) :

	<%@ page extends="com.myco.myapp.BodyTemplate" %>
	<%!
		// template args here
		...
	%>
	Template text...
	
The generated template class (`com.myco.myapp.MyBodyTemplate`) will implement 
your `BodyTemplate` interface.

When a template is passed to a scriptlet, the template is evaluated automatically
and spit out int the output.	

## Compiling templates
	
`.ttt` files are compiled to Java source code. You use your own IDE/build system in order to compile those sources
to Java bytecode. 

> Just like for any generated source code, you should put the generated sources into a specific folder, and will probably not even commit them into your VCS.
	
Templates can be compiled in several ways :
 
* command-line
* maven
* IntelliJ IDEA plugin

Those are detailed below.

The generated Java class is an immutable class with :

* a unique constructor which has the signature defined in the template
* a `void render(Writer out)` method that spits out the template to a writer
* possibly implementing an interface of your own (if you use the `extends` directive)
 
## Rendering templates

Compiled templates are regular Java classes with a constructor and a `render`method. :
	
	// create a template instance by passing all required 
	// args to the constructor
	MyTemplate t = new MyTemplate(arg1, arg2, ..., argN);
	
	Writer out = ... ;

	// spit out the text into a writer 
	t.render(out);

Very easy, and again, fully ststic typed.

## Build

The TTT Compiler is written in Java. You can use it directly via API, or use 
build tools described below.

### CLI

TTT Compiler provides a main class that can be used for CLI integration :

	java -jar ttt-core-<version>-jar-with-dependencies.jar -src <srd_dir> -target <code_gen_dir> -clean
 
### Maven plugin

The `ttt-compiler-plugin` can be used to compile templates as part of your maven build :

	<plugin>
		<groupId>com.pojosontheweb</groupId>
		<artifactId>ttt-maven-plugin</artifactId>
		<version>VERSION GOES HERE</version>
		<executions>
			<execution>
				<id>ttt-compile</id>
				<phase>process-resources</phase>
				<goals>
					<goal>ttt</goal>
				</goals>
			</execution>
		</executions>
	</plugin>
 
### IntelliJ IDEA Plugin

The ttt-idea plugin compiles your `.ttt` files, with a pretty useful "compile-on-save" feature.

IDEA also has great support for JSP, so you probably want to associate the `.ttt` file type 
with the JSP editor. This way you'll have full code assist, refactor etc, in a transparent way.	