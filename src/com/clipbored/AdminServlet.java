/********************************************************************************
 * Copyright (c) 2013, Alexander Meijer, All Rights Reserved
 * Filename: AdminServlet.java
 * Author: Alexander Meijer
 * 
 * Located in package: com.clipbored
 * Project: ClipBored-AppEngine
 * 
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
 * The Class AdminServlet. This class is designed handle POSTs from the video add form. It adds the video to the database.
 */
public class AdminServlet extends HttpServlet {
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException{
		//Logger
		final Logger log = Logger.getLogger(AdminServlet.class.getName());
		
		//Eventually, support for cookies will be added to store session data
		//Cookie[] cookies = req.getCookies();
		
		//obtain access to datastore
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		
		//Retrieve the relevant parameters
		String URL = req.getParameter("vidURL");
		String cat = req.getParameter("category");
		String FFWD = req.getParameter("FFWD");

		//strip the other URL data away - keep the tag only
		int tagIndex = URL.indexOf('?');
		String URLtag = URL.substring(tagIndex + 1);
		
		//create the Video JDO 
		Video vid = new Video();
		vid.setCategory(cat);
		vid.setFFWDtime(Integer.parseInt(FFWD));
		vid.setTag(URLtag);
		
		Entity newVid = VideoToEntity.convert(vid);
		
		//store
		datastore.put(newVid);
		
		//back to the clipbored screen - add a parameter so that we know the video persisted in the datastore correctly
		resp.sendRedirect("/adminConsole.jsp?vidadded=true");
		
				
				
	}
}