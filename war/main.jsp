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
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@ page import="com.clipbored.User"%>
<%@ page import="com.clipbored.Video"%>
<%@ page import="com.clipbored.EntityToVideo"%>
<%@ page import="com.clipbored.VideoToEntity"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.google.appengine.api.datastore.PreparedQuery"%>
<%@ page import="com.clipbored.EntityToUser"%>
<%@ page import="java.util.logging.Logger"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
	final Logger log = Logger.getLogger("main");
	String currentUserKey = (String) request.getParameter("id");
	if (request.getAttribute("id") != null) {
		currentUserKey = (String) request.getAttribute("id");
	}

	ArrayList<Video> toPlay = (ArrayList<Video>) request.getAttribute("toPlay");

	if (toPlay == null) {
		toPlay = new ArrayList<Video>();
	}
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	log.info("key received by main: " + currentUserKey);
	User currentUser = null;
	if (currentUserKey != null && currentUserKey.length() > 7) {

		Key key = KeyFactory.stringToKey(currentUserKey);
		Entity usrEntity = datastore.get(key);
		currentUser = EntityToUser.convert(usrEntity);

		log.info("got user: " + currentUser.getName());
	}

	if (currentUser == null) {
%>
<%
	} else {
		pageContext.setAttribute("username", currentUser.getName());
%>
<%
	}

	if (currentUser == null || currentUser.getCategories() == null) {
		currentUser = new User();
		log.info("loading default categories");
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add("Sports");
		defaults.add("Health & Beauty");
		defaults.add("Arts");
		defaults.add("Sciences");
		currentUser.setCategories(defaults);
%>

<%
	} else {
%>
<%
	}
	for (String str : currentUser.getCategories()) {
%>
<%
	} //for( String str:currentUser.getCategories()){
%>

    /**
     * Put your video IDs in this array
     */
    var videoIDs = [
          <%for (int i = 0; i < toPlay.size() - 1; i++) {
				toPlay.get(i).setViewCount(toPlay.get(i).getViewCount() + 1);
				datastore.put(VideoToEntity.convert(toPlay.get(i)));
				log.info("new view count: " + toPlay.get(i).getViewCount());%>
            '<%=toPlay.get(i).getTag().substring(2)%>',        
                    
          <%} //for(Video vid : toPlay)

			if (toPlay.size() >= 1) {
				toPlay.get(toPlay.size() - 1).setViewCount(toPlay.get(toPlay.size() - 1).getViewCount() + 1);
				datastore.put(VideoToEntity.convert(toPlay.get(toPlay.size() - 1)));%>
           '<%=toPlay.get(toPlay.size() - 1).getTag().substring(2)%>' 
        <%} //if(toPlay.size() >= 1){
			else {//nothing to play. get the top 20 videos

				Query q = new Query("Video");

				// Use PreparedQuery interface to retrieve results
				PreparedQuery pq = datastore.prepare(q);

				for (Entity e : pq.asIterable());

				ArrayList<Video> vids = new ArrayList<Video>();
				for (Entity e : pq.asIterable()) {
					vids.add(EntityToVideo.convert(e));
					log.info("***********Video queued: " + EntityToVideo.convert(e).getTag());
				}
				for (int j = 0; j < 20 && j < vids.size() - 1; j++) {
					vids.get(j).setViewCount(vids.get(j).getViewCount() + 1);
					datastore.put(VideoToEntity.convert(vids.get(j)));%>
        '<%=vids.get(j).getTag().substring(2)%>',        
        
        <%} //for(Video vid : toPlay)

				if (toPlay.size() >= 1) {%>
         '<%=vids.get(vids.size() - 1).getTag().substring(2)%>' 
        
        
        <%}
			} //else%>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="css/mainPage.css"></link>
<title>ClipBored</title>

<script src="http://www.google.com/jsapi" type="text/javascript"></script>
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script type="text/javascript">
	google.load("swfobject", "2.2");
</script>
<script src="js/misc.js" type="text/javascript"></script>
<script src="js/player.js" type="text/javascript"></script>
<script src="js/buttons.js" type="text/javascript"></script>
<script type="text/javascript">
	google.setOnLoadCallback(InitVideoPlayer);
</script>


</head>
<body>
	<table>
		<thead>
			<th></th>
			<th class="title"><img src="img/misc/title.png" id="title" /></th>
			<th><img src="img/misc/navBar.png" id="navBar"
				onclick="toggleLoginClean()" />
				<table class="login" id="loginFields">
					<tbody>
						<tr>
							<td><input id="userField" type="text" placeholder="Username">
							</td>
						</tr>
						<tr>
							<td><input id="passField" type="password"
								placeholder="Password"></td>
						</tr>
					</tbody>
				</table></th>
		</thead>
		<tbody>
			<!-- Will make 1 row with three columns, the left and right one will have three rows each -->
			<tr>
				<!-- Themes and player -->
				<td>
					<!-- Left themes -->
					<table class="themeCol">
						<tbody>
							<tr>
								<td><img src="img/themes/football.png" id="football" /></td>
							</tr>
							<tr>
								<td><img src="img/themes/lightbulb.png" id="lightbulb" /></td>
							</tr>
							<tr>
								<td><img src="img/themes/palette.png" id="palette" /></td>
							</tr>
						</tbody>
					</table>
				</td>
				<td id="videoBox">
					<!--Video player in here! -->
				</td>
				<td>
					<!-- Right themes -->
					<table class="themeCol">
						<tbody>
							<tr>
								<td><img src="img/themes/cat.png" id="cat" /></td>
							</tr>
							<tr>
								<td><img src="img/themes/lotus.png" id="lotus" /></td>
							</tr>
							<tr>
								<td><img src="img/themes/music.png" id="music" /></td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<!-- Controls -->
				<td colspan="3" class="controlBar"><img
					src="img/commands/fastBackwards.png" id="fastBackwards" /> <img
					src="img/commands/play.png" id="playPause"
					onclick="togglePlayback(ytplayer)" /> <img
					src="img/commands/star.png" id="star"> <img
					src="img/commands/fastForwards.png" id="fastForwards" /></td>
			</tr>
		</tbody>
	</table>
</body>
</html>