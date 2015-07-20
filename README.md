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

## Writing templates

The signature must appear first, enclosed by `<%(` and `)%>`.

The rest is pretty much like JSP scriptlets and expressions.

## Example

Here's a simple example - `com/xyz/myapp/MyTemplate.ttt` :

	<%(foo: com.xyz.myapp.Foo)%>
	<div class="foo">
		<%= foo.getBar() %> 	
	</div>
	
The compiler will generate a `com.xyz.myapp.MyTemplate` class with one constructor
that you can use in your Java code :

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

	