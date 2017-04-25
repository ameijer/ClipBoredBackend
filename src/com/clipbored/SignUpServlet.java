/********************************************************************************
 * Copyright (c) 2013, Alexander Meijer, All Rights Reserved
 * Filename: SignUpServlet.java
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
import java.util.Scanner;
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
 * The Class SignUpServlet. This class provides the average user with a way to
 * register if they are not located in the DB.
 */
public class SignUpServlet extends HttpServlet {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse) receives a
	 * POST request from either the signup JSP or the admin console
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		final Logger log = Logger.getLogger(SignUpServlet.class.getName());
		// Cookie[] cookies = req.getCookies();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		// get the parameters from the request. these will be used to create the
		// new user
		String uname = req.getParameter("username");
		String pass = req.getParameter("password");
		String email = req.getParameter("email");
		String adminSignup = req.getParameter("isAdmin");
		String cats = req.getParameter("cats");

		/// print some debug
		log.info("username passed from login screen: " + uname);
		log.info("password passed from login screen: " + pass);

		ArrayList<String> strs = null;
		if (cats != null) {
			strs = new ArrayList<String>();
			Scanner scan = new Scanner(cats);
			while (scan.hasNext()) {
				strs.add(scan.next());
			}

		}

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
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		byte[] digest1 = md1.digest(asBytes);

		// MD5, round 2...
		try {
			MessageDigest md2 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
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

		Entity newUser = new Entity(k);

		newUser.setProperty("categories", strs);
		newUser.setProperty("seen_videos", null);
		newUser.setProperty("favorites", null);
		newUser.setProperty("email", email);
		newUser.setProperty("sign_up_date", new Date(System.currentTimeMillis()));
		newUser.setProperty("name", uname);
		newUser.setProperty("password", new String(digest2, "UTF-16"));
		newUser.setProperty("last_seen", new Date(System.currentTimeMillis()));

		datastore.put(newUser);

		if (adminSignup == null) {
			// back to the clipbored screen
			resp.sendRedirect("/main.jsp");
		} else {
			resp.sendRedirect("/adminConsole.jsp");

		}

	}
}