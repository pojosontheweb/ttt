# Typed Text Templates (aka TTT)

Static-typed text templates for Java.

## The problem

Most text templating libs do not provide strong, static typing. 

JSP, for instance, can be statically typed and checked at compile-type, 
but they don't provide any signature. Therefore, you have no context, 
and no way to know what the page arguments can be. You usually get
values from a non typed structure, like a Map<String,Object>. Same applies to Groovy 
templates and others.
 
Also, we'd like to be able to compose templates easily. And again, in 
a fully static, compile-time checked fashion.

## The idea

Ttt is a simple templating language, and a compiler.
 
You write templates using a subset of the JSP syntax (see next section). 
Templates can use script and/or expression blocks.

Most importantly, templates define their signature, as typed argyments, just like a function would. 
The signature defines the variables available inside the template.

Templates are compiled to .java source files at design or build-time.  

You can then use the Java classes from your code, in a static-typed fashion.

Ttt is inspired from Play! 2 Scala Templates.

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
	
First declaration block is the template's signature, between `<%!` and `%>`. Arguments are declared like you declare local variables for a JSP. You don't need to set a value.

Following is some text, with some scriptlets and expressions, again, just like a regular JSP.

TODO link to full language reference

### Nested Templates (composition)

TODO

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
 
## Rendering templates

Compiled templates are regular Java classes that you can use anywhere:

    TODO

## Build

### CLI
 
### Maven plugin
 
### IntelliJ IDEA Plugin

	