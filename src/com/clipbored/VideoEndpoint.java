/********************************************************************************
 * Copyright (c) 2013, Alexander Meijer, All Rights Reserved
 * Filename: VideoEndpoint.java
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
import java.util.logging.Logger;

import com.clipbored.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.mortbay.log.Log;


/**
 * The Class VideoEndpoint. Used to get/send videos via the API 
 */
@Api(name = "videoendpoint", namespace = @ApiNamespace(ownerDomain = "google.com", ownerName = "google.com", packagePath = "samplesolutions.mobileassistant"))
public class VideoEndpoint {

	/**
	 * This method lists all the videos in the DB.
	 * It uses HTTP GET method and paging support.
	 *
	 * @param cursorString the cursor string
	 * @param limit the limit The number of results to return. This svaes bandwidth + write OPS, so if the DB gets large this should be used. 
	 * @return A CollectionResponse class containing the list of all videos
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listVideo")
	public CollectionResponse<Video> listVideo(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Video> execute = null;

		try {
			mgr = getEntityManager();
			
			Query query = mgr.createQuery("select from Video as Video");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Video>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Video obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Video> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method lists all the videos inserted in datastore with a category matching the one the user specified. 
	 * It uses HTTP GET method and paging support.
	 *
	 * @param cursorString the cursor string
	 * @param limit the limit The number of results to return. This svaes bandwidth + write OPS, so if the DB gets large this should be used. 
	 * @param category the category A string specifying the category of videos to get
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listVideo_category", path="listVid_category")
	public CollectionResponse<Video> listVideo_category(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit, @Named("category") String category) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Video> execute = null;
		List<Video> execute_stripped = new ArrayList<Video>();
		try {
			mgr = getEntityManager();

			Query query = mgr.createQuery("select from Video as Video");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Video>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();
			
			//remove all videos not matching the category
			for (int i = 0; i < execute.size(); i++){
				if(execute.get(i).getCategory().equalsIgnoreCase(category)){
					execute_stripped.add(execute.get(i));
					
				}
			}
		} finally {
			mgr.close();
		}

		//return the list of results
		return CollectionResponse.<Video> builder().setItems(execute_stripped)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The Video with primary key id.
	 */
	@ApiMethod(name = "getVideo")
	public Video getVideo(@Named("id") String Key) {
		EntityManager mgr = getEntityManager();
		Video video = null;
		try {
			video = mgr.find(Video.class, Key);
		} finally {
			mgr.close();
		}
		return video;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, a log message is printed and nothing more is done.
	 * It uses HTTP POST method.
	 *
	 * @param video the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertVideo")
	public Video insertVideo(Video video) {
		final Logger log = Logger.getLogger(VideoEndpoint.class.getName());
		EntityManager mgr = getEntityManager();
		try {
			
			
			if(video.getKey() == null){
				log.severe(" video key is null: " + video.getKey());
				Key k = new KeyFactory.Builder("Video", video.getTag()).getKey();
				video.setKey(k);
				log.severe(" video key changed to be: " + video.getKey());
			} else {
				log.severe(" video key non-null: " + video.getKey());
			}
			
			
			//do nothing
			if (video.getKey() != null) {
				mgr.persist(video);
			}else {
			
			}
		} finally {
			mgr.close();
		}
		return video;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, null is returned. 
	 * It uses HTTP PUT method.
	 *
	 * @param video the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateVideo")
	public Video updateVideo(Video video) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsVideo(video)) {
				return null;
			}
			mgr.persist(video);
		} finally {
			mgr.close();
		}
		return video;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeVideo")
	public void removeVideo(@Named("id") String Key) {
		EntityManager mgr = getEntityManager();
		try {
			Video video = mgr.find(Video.class, Key);
			mgr.remove(video);
		} finally {
			mgr.close();
		}
	}

	/**
	 * Contains video. An internal method used to check the existence of a specific video in the DB. 
	 *
	 * @param video the video to search for in the database
	 * @return true, if the video exists in the DB
	 */
	private boolean containsVideo(Video video) {
		EntityManager mgr = getEntityManager();
		 final Logger log = Logger.getLogger(VideoEndpoint.class.getName());

		boolean contains = true;
		try {
			log.severe(" video key that is being looked for: " + video.getKey());
			Video item = mgr.find(Video.class, video.getKey());
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
