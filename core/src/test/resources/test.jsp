<%@ page import="java.util.List"%>
<%@ page extends="Number" %>
<%@ page extends="java.util.Collection<String>" %>
<%!
    List l;
%>
<ul>
    <% for (Object o : l) { %>
        <li><%= o %></li>
    <% } %>
</ul>
