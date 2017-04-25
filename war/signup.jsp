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
<%@ page import="com.clipbored.User"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<html>
<body>
	<p>
		<b>Sign Up for ClipBored!</b>
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
			<input type="submit" value="Sign In" />
		</div>
	</form>



</body>
</html>