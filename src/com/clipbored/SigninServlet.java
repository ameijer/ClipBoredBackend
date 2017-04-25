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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;

/**
 * The Class SigninServlet. This class gets the POST data from the from on
 * main.jsp and checks the DB for the user. If no user is found, this servlet
 * will redirect the user to the signUpservlet. A good example of a JSP printing
 * HTML to the output stream.
 */
public class SigninServlet extends HttpServlet {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse) This method
	 * handles POST requests from the form on main.jsp
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		// the debug logger for this class
		final Logger log = Logger.getLogger(SigninServlet.class.getName());

		// eventually, support for cookies will be added
		// Cookie[] cookies = req.getCookies();

		// obtain access to the datastore
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		// get the login details from the Post request from main.jsp
		String uname = req.getParameter("username");
		String pass = req.getParameter("password");

		// for debug
		log.info("username passed from login screen: " + uname);
		log.info("password passed from login screen: " + pass);

		// hash the user password to be used in the key
		byte[] asBytes;

		// MD5, round 1...
		try {
			asBytes = pass.getBytes("UTF-16");
		} catch (UnsupportedEncodingException e) {
			// try UTF-8...
			asBytes = pass.getBytes("UTF-8");
		}

		MessageDigest md1 = null;
		try {
			md1 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e2) {
			e2.printStackTrace();
		}
		byte[] digest1 = md1.digest(asBytes);

		// MD5, round 2...
		try {
			MessageDigest md2 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		byte[] digest2 = md1.digest(digest1);

		log.info("Double MD5 hash calculated: " + digest2);

		log.info("looking for key " + digest2);

		// make the key from a combination of the hashed password and the
		// username. This will only let somebody with the right username and
		// password access the corresponding user.
		Key k = null;
		try {
			k = new KeyFactory.Builder("User", uname).addChild("User", new String(digest2, "UTF-16")).getKey();
		} catch (UnsupportedEncodingException e) {
			// try UTF-8...
			k = new KeyFactory.Builder("User", uname).addChild("User", new String(digest2, "UTF-8")).getKey();
		}

		User currentUser = null;
		log.info("key created: " + k.toString());
		try {
			Entity userEntity = datastore.get(k);
			if (userEntity != null) {
				currentUser = EntityToUser.convert(userEntity);

				if (0 > currentUser.getLast_seen().compareTo(new Date(System.currentTimeMillis()))) {
					// update last seen
					currentUser.setLast_seen(new Date(System.currentTimeMillis()));
				}
			}
			String websafe = KeyFactory.keyToString(k);
			log.info("key as string retrieved: " + websafe);

			// append the key to the redirect request. This will allow main.jsp
			// to identify the user and display user-specific data
			resp.sendRedirect("/main.jsp?id=" + websafe);

		} catch (EntityNotFoundException e) {
			log.warning("User not found in DB");
		}

		if (currentUser == null) {
			// new user, needs to authenticate
			resp.setContentType("text/html");
			resp.getWriter().println("<p>Error - user not found</p>");
			resp.getWriter().println("<p>Would you like to: <a href=/signup.jsp>sign up?</a></p>");

		}
	}
}