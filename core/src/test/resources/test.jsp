<%@ page import="java.util.List"%>
<%!
    List l;
%>
<ul>
    <% for (Object o : l) { %>
        <li><%= o %></li>
    <% } %>
</ul>
