/********************************************************************************
 * Copyright (c) 2013, Alexander Meijer, All Rights Reserved
 * Filename: UserEndpoint.java
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

import com.clipbored.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * The Class UserEndpoint. Provides API methods to get/add users from the
 * clipbored backend
 */
@Api(name = "userendpoint", namespace = @ApiNamespace(ownerDomain = "google.com", ownerName = "google.com", packagePath = "samplesolutions.mobileassistant"))
public class UserEndpoint {

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET
	 * method. It requires only the username and password to get the user
	 *
	 * @param name
	 *            the name The username.
	 * @param password
	 *            the password The user's password, in plain text.
	 * @return The User entity that matches both the username and apssword. null
	 *         otherwise (i.e. incorrect password, incorrect username, not
	 *         matching username, password)
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception If this is thrown, then
	 *             neither UTF-16 or UTF-8 are supported, and we are basically
	 *             screwed.
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception Thrown if MD5 suddenly stops
	 *             existing.
	 */
	@ApiMethod(name = "getUser")
	public User getUser(@Named("name") String name, @Named("pass") String password)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {

		// jetty logger
		final Logger log = Logger.getLogger(UserEndpoint.class.getName());

		byte[] asBytes;

		// MD5, round 1...
		try {
			asBytes = password.getBytes("UTF-16");
		} catch (UnsupportedEncodingException e) {
			// try UTF-8...
			asBytes = password.getBytes("UTF-8");
		}

		MessageDigest md1 = MessageDigest.getInstance("MD5");
		byte[] digest1 = md1.digest(asBytes);

		// MD5, round 2...
		MessageDigest md2 = MessageDigest.getInstance("MD5");
		byte[] digest2 = md1.digest(digest1);

		log.info("Double MD5 hash calculated: " + digest2);

		log.info("looking for key " + digest2);

		// make the key from a combination of the hashed password and the
		// username. This will only let somebody with the right username and
		// password access the corresponding user.
		Key k = null;
		try {
			k = new KeyFactory.Builder("User", name).addChild("User", new String(digest2, "UTF-16")).getKey();
		} catch (UnsupportedEncodingException e) {
			// try UTF-8...
			k = new KeyFactory.Builder("User", name).addChild("User", new String(digest2, "UTF-8")).getKey();
		}

		EntityManager mgr = getEntityManager();
		User user = null;

		// persist the user
		try {
			user = mgr.find(User.class, k);
		} finally {
			mgr.close();
		}
		return user;
	}

	/**
	 * This inserts a new User entity into App Engine datastore. If the user
	 * already exists (i.e. the user's key matches one already in the DB), then
	 * a log message will be printed and nothing else will happen It uses HTTP
	 * POST method.
	 *
	 * @param user
	 *            the User to be inserted.
	 * @return The inserted User.
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception If this is thrown, then
	 *             neither UTF-16 or UTF-8 are supported, and we are basically
	 *             screwed.
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception Thrown if MD5 suddenly stops
	 *             existing.
	 */
	@ApiMethod(name = "insertUser")
	public User insertUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		EntityManager mgr = getEntityManager();

		final Logger log = Logger.getLogger(UserEndpoint.class.getName());

		try {

			if (user.getKey() != null) {

				log.severe(" user key non-null: " + user.getKey());
			}

			// if the user has a key (i.e. it has been through the backend) the
			// check the DB
			if (user.getKey() != null && containsUser(user)) {
				// if the user is already there, print an error msg and do
				// nothing
				log.severe(" user already exists.... ignoring");
			} else {
				// user does not exist. Encrypt the password
				byte[] asBytes;

				// MD5, round 1...
				try {
					asBytes = user.getPassword().getBytes("UTF-16");
				} catch (UnsupportedEncodingException e) {
					// try UTF-8...
					asBytes = user.getPassword().getBytes("UTF-8");
				}

				MessageDigest md1 = MessageDigest.getInstance("MD5");
				byte[] digest1 = md1.digest(asBytes);

				// MD5, round 2...
				MessageDigest md2 = MessageDigest.getInstance("MD5");
				byte[] digest2 = md1.digest(digest1);

				log.info("Double MD5 hash being stored: " + digest2);

				try {
					user.setPassword(new String(digest2, "UTF-16"));
				} catch (UnsupportedEncodingException e) {
					// try UTF-8...
					user.setPassword(new String(digest2, "UTF-8"));
				}

				// now password is sufficiently safe to be stored
				// make the key from a combination of the hashed password and
				// the username.
				log.severe(" user key is null: " + user.getKey());
				Key k = new KeyFactory.Builder("User", user.getName()).addChild("User", user.getPassword()).getKey();
				user.setKey(k);
				log.severe(" user key changed to be: " + user.getKey());

				mgr.persist(user);
			}
		} finally {
			mgr.close();
		}
		return user;
	}

	/**
	 * This method is used for updating an existing user entity. If the user
	 * does not exist then null is returned. It uses HTTP PUT method.
	 *
	 * @param user
	 *            the user entity to be updated.
	 * @return The updated entity. NULL is returned if the user being updated
	 *         does not exist in the database.
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception If this is thrown, then
	 *             neither UTF-16 or UTF-8 are supported, and we are basically
	 *             screwed.
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception Thrown if MD5 suddenly stops
	 *             existing.
	 */
	@ApiMethod(name = "updateUser")
	public User updateUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {

		EntityManager mgr = getEntityManager();

		final Logger log = Logger.getLogger(UserEndpoint.class.getName());

		try {

			// this searches the DB based on the key
			if (!containsUser(user)) {
				log.severe(" no user with key: " + user.getKey() + " exists");
				return null;
			}
			
			// the password for the user will be non-null if it needs to be
			// updated
			if ((user.getPassword() != null || !user.getPassword().equalsIgnoreCase(""))
					&& !user.getPassword().equalsIgnoreCase(mgr.find(User.class, user.getKey()).getPassword())) {
				// then the password needs to be changed
				log.info("updating password");
				// re-add the user. Encrypt the password
				byte[] asBytes;

				// MD5, round 1...
				try {
					asBytes = user.getPassword().getBytes("UTF-16");
				} catch (UnsupportedEncodingException e) {
					// try UTF-8...
					asBytes = user.getPassword().getBytes("UTF-8");
				}

				MessageDigest md1 = MessageDigest.getInstance("MD5");
				byte[] digest1 = md1.digest(asBytes);

				// MD5, round 2...
				MessageDigest md2 = MessageDigest.getInstance("MD5");
				byte[] digest2 = md1.digest(digest1);

				log.info("Double MD5 hash being stored: " + digest2);

				try {
					user.setPassword(new String(digest2, "UTF-16"));
				} catch (UnsupportedEncodingException e) {
					// try UTF-8...
					user.setPassword(new String(digest2, "UTF-8"));
				}

				// re-calculate key using new password
				log.severe(" user key is null: " + user.getKey());
				Key k = new KeyFactory.Builder("User", user.getName()).addChild("User", user.getPassword()).getKey();
				user.setKey(k);
				log.severe(" user key changed to be: " + user.getKey());
			}

			mgr.persist(user);

		} finally {
			mgr.close();
		}
		return user;
	}

	/**
	 * This method removes the User entity with primary key id. A removal
	 * requires the correct username and password to prevent unauthorized
	 * deletions It uses HTTP DELETE method.
	 *
	 * @param uname_rem
	 *            the uname_rem The username of the user to remove.
	 * @param pass_rem
	 *            the pass_rem The password (in plaintext) of the user to remove
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception If this is thrown, then
	 *             neither UTF-16 or UTF-8 are supported, and we are basically
	 *             screwed.
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception Thrown if MD5 suddenly stops
	 *             existing.
	 */
	@ApiMethod(name = "removeUser")
	public void removeUser(@Named("username_rem") String uname_rem, @Named("password_rem") String pass_rem)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		final Logger log = Logger.getLogger(UserEndpoint.class.getName());
		log.info("IN REMOVE USER CALL");

		// generate the key from scratch to authenticate user

		byte[] asBytes;

		// MD5, round 1...
		try {
			asBytes = pass_rem.getBytes("UTF-16");
		} catch (UnsupportedEncodingException e) {
			// try UTF-8...
			asBytes = pass_rem.getBytes("UTF-8");
		}

		MessageDigest md1 = MessageDigest.getInstance("MD5");
		byte[] digest1 = md1.digest(asBytes);

		// MD5, round 2...
		MessageDigest md2 = MessageDigest.getInstance("MD5");
		byte[] digest2 = md1.digest(digest1);

		log.info("Double MD5 hash being stored: " + digest2);

		Key k;
		try {

			k = new KeyFactory.Builder("User", uname_rem).addChild("User", new String(digest2, "UTF-16")).getKey();
		} catch (UnsupportedEncodingException e) {
			// try UTF-8...
			k = new KeyFactory.Builder("User", uname_rem).addChild("User", new String(digest2, "UTF-8")).getKey();
		}

		log.severe(" user key to delete: " + k);

		EntityManager mgr = getEntityManager();
		try {
			User user = mgr.find(User.class, k);
			// if user is null, then the credentials are invalid. Cancel the
			// deletion
			if (user != null) {
				log.warning("ATTENTION: REMOVAL OF USER: " + user.getName() + " AUTHORIZED");
				mgr.remove(user);
			} else {
				log.warning("no users deleted  - invalid credentials supplied");
			}
		} finally {
			mgr.close();
		}
	}

	/**
	 * Contains user. an internal private method used to check the datastore.
	 * Note that this uses the Key to search.
	 * 
	 *
	 * @param user
	 *            the user to look for.
	 * @return true, if the user is found in the DB
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception If this is thrown, then
	 *             neither UTF-16 or UTF-8 are supported, and we are basically
	 *             screwed.
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception Thrown if MD5 suddenly stops
	 *             existing.
	 */
	private boolean containsUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {

		// logger for debug
		final Logger log = Logger.getLogger(UserEndpoint.class.getName());
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			User item = null;
			try {
				item = mgr.find(User.class, user.getKey());

			} catch (NullPointerException e) {
				// this could conceivable happen if a new user is updating their
				// stats. the key is hashed remotely, so the local copy has no
				// knowledge of the key
				// instead, generate the key here in real time and search using
				// that
				// this is an edge case so the inefficiency of this approach is
				// marginal

				// figure out the password
				byte[] asBytes;

				// MD5, round 1...
				try {
					asBytes = user.getPassword().getBytes("UTF-16");
				} catch (UnsupportedEncodingException e2) {
					// try UTF-8...
					asBytes = user.getPassword().getBytes("UTF-8");
				}

				MessageDigest md1 = MessageDigest.getInstance("MD5");
				byte[] digest1 = md1.digest(asBytes);

				// MD5, round 2...
				MessageDigest md2 = MessageDigest.getInstance("MD5");
				byte[] digest2 = md1.digest(digest1);

				log.info("Double MD5 hash being stored: " + digest2);

				try {
					user.setPassword(new String(digest2, "UTF-16"));
				} catch (UnsupportedEncodingException e1) {
					// try UTF-8...
					user.setPassword(new String(digest2, "UTF-8"));
				}

				// now password is sufficiently safe to be stored

				Key k = new KeyFactory.Builder("User", user.getName()).addChild("User", user.getPassword()).getKey();
				user.setKey(k);
				log.severe(" containsUser real time key computation: user key changed to be: " + user.getKey());

				// now try the search again. should have no more NPEs in this
				// scenario
				item = mgr.find(User.class, user.getKey());
			}

			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}

	/**
	 * Gets the entity manager.
	 *
	 * @return the entity manager
	 */
	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
