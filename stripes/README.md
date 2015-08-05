# TTT Stripes Integration

TTT templates can be used with Stripes very easily. They integrate 
smoothly, and fill in the gap between ActionBeans and JSPs.

## Configuration (maven)

In order to use TTT with Stripes you first need to add the dependency
on `ttt-stripes` :

        <dependency>
            <groupId>com.pojosontheweb</groupId>
            <artifactId>ttt-stripes</artifactId>
            <version>0.1-beta</version>
        </dependency>
        
Then, add the TTT compiler plugin :

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

> We use `src/main/java` but of course you can put your templates 
> anywere you see fit. It's just easier to have them all under the same 
> source root.

And that's it ! You're ready to ride. 

## Writing Templates

Writing templates is basically like writing any TTT template. Nevertheless,
Stripes has pretty good tag library in the JSP world, which is not directly 
available for TTT templates (remember, they ain't no JSPs...).

So instead of using JSP tags, you simply use... good old Java !

TTT-Stripes provides the same features as the JSP tags : `<s:form>`,
`<s:text>` etc.
 
The entry point for those helpers is the class `com.pojosontheweb.ttt.stripes.StripesTags`.
 
Have a look at the templates in the 
[stripes-testprj](https://github.com/pojosontheweb/ttt/tree/master/stripes-testprj/src/main/java/stttripes/templates) module
for examples.

## Rendering Templates

Cannot be easier :

	return new TttResolution( template );

The class `TttResolution` is a dedicated Stripes resolution that renders the 
template into the HTTP response.
 
## Example

The TTT template `MyTemplate.ttt` :

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

And the ActionBean :

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

As you see here, the template is bound to the action by static 
type checking. The action creates the template by calling the 
constructor, passing itself as the argument. Simple, and yet 
very effective.

More examples are available in the 
[stripes-testprj](https://github.com/pojosontheweb/ttt/tree/master/stripes-testprj) 
module.

> If you're using IntelliJ IDEA you might find the [ttt-idea](https://github.com/pojosontheweb/ttt/tree/master/idea-ttt-plugin) plugin useful.
