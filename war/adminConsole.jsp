<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>

<%@ page
	import="com.google.appengine.api.datastore.DatastoreServiceFactory"%>
<%@ page import="com.google.appengine.api.datastore.DatastoreService"%>
<%@ page import="com.google.appengine.api.datastore.Query"%>
<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ page import="com.google.appengine.api.datastore.FetchOptions"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="com.google.appengine.api.datastore.Query.Filter"%>
<%@ page
	import="com.google.appengine.api.datastore.Query.FilterPredicate"%>
<%@ page
	import="com.google.appengine.api.datastore.Query.FilterOperator"%>
<%@ page
	import="com.google.appengine.api.datastore.Query.CompositeFilter"%>
<%@ page
	import="com.google.appengine.api.datastore.Query.CompositeFilterOperator"%>
<%@ page import="com.google.appengine.api.datastore.Query"%>
<%@ page import="com.google.appengine.api.datastore.PreparedQuery"%>
<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ page import="com.clipbored.User"%>
<%@ page import="com.clipbored.Video"%>
<%@ page import="com.clipbored.EntityToVideo"%>
<%@ page import="com.clipbored.EntityToUser"%>
<%@ page import="java.util.logging.Logger"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<%
	final Logger log = Logger.getLogger("admin console JSP");
%>
<html>
<body>
	<p>
		<b>ClipBored Cloud Administration Console</b>
	<p>
	<p>
	<p>
	<p>Add a video:
	<p>
	<form action="/admin" method="post">
		<div>
			<textarea name="vidURL" rows="1" cols="20" line-height="20px">Enter complete Video URL</textarea>
		</div>
		<div>
			<textarea name="category" rows="1" cols="20">Enter category</textarea>
		</div>
		<div>
			<textarea name="FFWD" rows="1" cols="20">Milliseconds to Fast Forward to in video</textarea>
		</div>
		<div>
			<input type="submit" value="Add Video" />
		</div>
	</form>

	<%
		//if the listVids parameter is set, then we want to list the videos for the specified category
		String vidadded = request.getParameter("vidadded");
		if (vidadded != null) {
	%>

	<p>
		<b>Video added to ClipBored Successfully</b>
	</p>
	<%
		} //if(listCatVids != null)
	%>


	<p>Add a User:
	<p>
	<form action="/signup" method="post">
		<div>
			<textarea name="username" rows="1" cols="20" line-height="20px">Username</textarea>

		</div>
		<div>
			<textarea name="password" rows="1" cols="20">Password</textarea>
		</div>
		<div>
			<textarea name="email" rows="1" cols="20">Email</textarea>
		</div>
		<div>
			<textarea name="cats" rows="1" cols="20">Categories of interest seperated by a space</textarea>
		</div>
		<input type="hidden" name="isAdmin" value="true">
		<div>
			<input type="submit" value="Sign up user" />
		</div>
	</form>

	<p>List Videos from a category:
	<p>
	<form action="/adminConsole.jsp" method="post">
		<div>
			<textarea name="category" rows="1" cols="20" line-height="20px">Category to list</textarea>
		</div>
		<input type="hidden" name="listCatVids" value="true">
		<div>
			<input type="submit" value="List videos" />
		</div>
	</form>

	<p>List all Videos:
	<p>
	<form action="/adminConsole.jsp" method="post">
		<input type="hidden" name="listAllVids" value="true">
		<div>
			<input type="submit" value="List ALL videos" />
		</div>
	</form>

	<p>List Users:
	<p>
	<form action="/adminConsole.jsp" method="post">
		<input type="hidden" name="listAllUsers" value="true">
		<div>
			<input type="submit" value="List Users" />
		</div>
	</form>

	<p>Search for a specific user:
	<p>
	<form action="/adminConsole.jsp" method="post">
		<div>
			<textarea name="Username" rows="1" cols="20" line-height="20px">Username</textarea>
		</div>
		<input type="hidden" name="listUname" value="true">
		<div>
			<input type="submit" value="Find User(s)" />
		</div>
	</form>

	<%
		//if the listVids parameter is set, then we want to list the videos for the specified category
		String listCatVids = request.getParameter("listCatVids");
		String listAllVids = request.getParameter("listAllVids");
		String listAllUsers = request.getParameter("listAllUsers");
		String listUname = request.getParameter("listUname");
		String category = request.getParameter("category");

		if (listCatVids != null) {

			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			Filter categoryFilter = new FilterPredicate("category", Query.FilterOperator.EQUAL, category);
			Query q = new Query("Video").setFilter(categoryFilter);

			// Use PreparedQuery interface to retrieve results
			PreparedQuery pq = datastore.prepare(q);
	%>
	<table>
		<%
			for (Entity result : pq.asIterable()) {
					Video curVid = EntityToVideo.convert(result);
		%>
		<tr>

			<td>Video tag: <%=curVid.getTag()%></td>
			<td>Video Category: <%=curVid.getCategory()%></td>
			<td>Number of youtube views: <%=curVid.getYTViewCount()%></td>
			<td>Number of clipbored views: <%=curVid.getViewCount()%></td>
		</tr>

		<%
			} //for (Entity result : pq.asIterable())
				request.setAttribute("listCatVids", null);
			} //if listvids
			if (listAllVids != null) {

				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

				Query q = new Query("Video");

				// Use PreparedQuery interface to retrieve results
				PreparedQuery pq = datastore.prepare(q);
		%>
		<table>
			<%
				for (Entity result : pq.asIterable()) {
						Video curVid = EntityToVideo.convert(result);
			%>
			<tr>

				<td>Video tag: <%=curVid.getTag()%></td>
				<td>Video Category: <%=curVid.getCategory()%></td>
				<td>Number of youtube views: <%=curVid.getYTViewCount()%></td>
				<td>Number of clipbored views: <%=curVid.getViewCount()%></td>
			</tr>

			<%
				} //for (Entity result : pq.asIterable())
					request.setAttribute("listAllVids", null);
				} //if listAllvids
				if (listAllUsers != null) {

					DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

					Query q = new Query("User");

					// Use PreparedQuery interface to retrieve results
					PreparedQuery pq = datastore.prepare(q);
			%>
			<table>
				<%
					for (Entity result : pq.asIterable()) {
							User curUser = EntityToUser.convert(result);
				%>
				<tr>

					<td>Username: <%=curUser.getName()%></td>
					<td>Password: <%=curUser.getPassword()%></td>
					<td>Email: <%=curUser.getEmail()%></td>
					<td>Last seen: <%=curUser.getLast_seen()%></td>

				</tr>

				<%
					} //for (Entity result : pq.asIterable())
						request.setAttribute("listAllUsers", null);
					} //else if listAllvids

					//USER SEARCH

					if (listUname != null) {
						String Username = request.getParameter("Username");
						DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

						Filter categoryFilter = new FilterPredicate("name", Query.FilterOperator.EQUAL, Username);
						Query q = new Query("User").setFilter(categoryFilter);

						// Use PreparedQuery interface to retrieve results
						PreparedQuery pq = datastore.prepare(q);
				%>
				<table>
					<%
						for (Entity result : pq.asIterable()) {
								User curUser = EntityToUser.convert(result);
					%>
					<tr>

						<td>Username: <%=curUser.getName()%></td>
						<td>Password: <%=curUser.getPassword()%></td>
						<td>Email: <%=curUser.getEmail()%></td>
						<td>Last seen: <%=curUser.getLast_seen()%></td>
					</tr>

					<%
						} //for (Entity result : pq.asIterable())
							request.setAttribute("listUname", null);
						} //if listvids
					%>

				</table>
</body>
</html>