# Typed Text Templates

Static-typed text templates for Java.

## The problem

Most text templating libs do not provide strong, static typing. 

JSP, for instance, can be statically typed and checked at compile-type, 
but they don't provide any "signature". Same applies to Groovy templates
and others. 

Also, we need easy composition of nested templates.

## The idea

Ttt is a simple templating language, and a compiler.
 
Templates are text files (usually with a .ttt extension) that 
use script and/or expression blocks, like JSPs. Inside expressions 
and scripts is Java code.

Most importantly, templates define their signature. Like a function, 
the signature defines the variables available inside the template.

The compiler transforms templates into Java classes, which can 
then be used from any Java program in a static-typed fashion.

The generated Java class for a template extends a `com.pojosontheweb.ttt.Template` 
base class. It defines one constructor with all required parameters, from the 
template signature. 

Ttt is inspired from Play! 2 Scala Templates.

## Writing templates

The signature must appear first, enclosed by `<%(` and `)%>`.

The rest is pretty much like JSP scriptlets and expressions.

Here's a simple example - `com/xyz/myapp/MyTemplate.ttt` :

	<%(foo: com.xyz.myapp.Foo)%>
	<div class="foo">
		<%= foo.getBar() %> 	
	</div>

### Compiling templates
	
Before you build your app the way you usually do, you need to 
compile the templates to Java code. This can be done in several
ways (see the last section).
	
For the example above, the compiler will generate a `com.xyz.myapp.MyTemplate` 
class with one constructor that accepts the args as defined in the 
template's signature, and a `render` method :



### Rendering templates

Compiled templates are regular Java classes that you can use anywhere:

	Foo foo = ... ;
	try (Writer out = new PrintWriter(System.out)) {
		new MyTemplate(foo).render(out);
	}
	
## Composition	

Here's a wrapper template using the previous example - `com.xyz.myapp.MyWrapper` :
 
	<%( title: String, body: com.xyz.myapp.MyTemplate )%>
	<html>
	<head>
		<title><%= title %></title>
	</head>
	<body>
		<%= body %>
	</body>
	</html>
	
As you see, the signature defines two parameters :

 * title : a String title
 * body : the body of the page : a Template itself. Can be one of your own templates, or the base class `com.pojosontheweb.ttt.Template`.

Here's how you'd render the whole thing :

	Foo foo = ...;
	try (Writer out = new PrintWriter(System.out)) {
		new MyWrapper("hey there", new MyTemplate(foo)).render(out);
	}
	
## Build integration

### Java API

### Command line

### Maven


	