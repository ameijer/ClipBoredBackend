/********************************************************************************
 * Copyright (c) 2013, Alexander Meijer, All Rights Reserved
 * Filename: PlaylistServlet.java
 * Author: Alexander Meijer
 * 
 * Located in package: com.clipbored
 * Project: ClipBored-AppEngine
 * 
 * 
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clipbored;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;

/**
 * The Class PlaylistServlet. This class is called to build the playlist for the
 * user based on the type of video requested. The user is then forwarded back to
 * main.jsp with the list of videos to play attached to the request. Also the
 * user's ID is attached so we can keep them logged in.
 */
public class PlaylistServlet extends HttpServlet {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse) Only POST
	 * implemented here. GET methods will result in 404
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		final Logger log = Logger.getLogger(PlaylistServlet.class.getName());

		// eventually, support for cookies will be added so the users's seen
		// videos can be retrieved quickly and can be excluded from the playlist
		// Cookie[] cookies = req.getCookies();

		// obtain access to datastore
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		// get what category the user wants from the HTTP request object
		String category = req.getParameter("category");
		String id = req.getParameter("id");

		// debugging code
		log.info("category: " + category);
		log.info("id: " + id);

		// create an arraylist of video objects to be passed back to main to be
		// used in playlist
		ArrayList<Video> vids = new ArrayList<Video>();

		// the Datastore query
		Filter categoryFilter = new FilterPredicate("category", Query.FilterOperator.EQUAL, category);
		Query q = new Query("Video").setFilter(categoryFilter);

		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);

		// convert the returned entities to videos for the arraylist
		for (Entity result : pq.asIterable()) {
			Video curVid = EntityToVideo.convert(result);
			vids.add(curVid);

		}

		// add the users ID and the arraylist of videos to the response
		req.setAttribute("toPlay", vids);
		req.setAttribute("id", id);

		RequestDispatcher view = req.getRequestDispatcher("/main.jsp");
		view.forward(req, resp);
	}
}