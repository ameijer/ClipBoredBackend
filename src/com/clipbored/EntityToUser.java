/********************************************************************************
 * Copyright (c) 2013, Alexander Meijer, All Rights Reserved
 * Filename: EntiyToUser.java
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

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;



/**
 * The Class EntityToUser. A class providing translation from Entities to User objects
 */
public class EntityToUser {
	
	/**
	 * Convert the given datastore entity object to a User object
	 *
	 * @param entity The entity to convert to a user. If this method is called on an Entity that does not correspond to a user, all the fields of the created user will be null
	 * @return the user The User created from the entity. Will return a user even if there is no matching entity
	 */
	public static User convert(Entity entity){
		final Logger log = Logger.getLogger(EntityToUser.class.getName());
		User user = new User();
		
		@SuppressWarnings("unchecked")
		
		//retrieve values from entity object
		ArrayList<String> categories = (ArrayList<String>)  entity.getProperty("categories");
		ArrayList<String> favorites = (ArrayList<String>)  entity.getProperty("favorites");
		ArrayList<String> seen_vids = (ArrayList<String>)  entity.getProperty("seen_videos");
		String uname = (String) entity.getProperty("name");
		String email= (String) entity.getProperty("email");
		String pass = (String) entity.getProperty("password");
		Date lastSeen = (Date) entity.getProperty("last_seen");
		
		//datastore's DateTime format casts to dates with no problems
		Date signUpDate = (Date) entity.getProperty("sign_up_date");
		Key key = (Key) entity.getKey();
		
		//set returned user's values
		user.setCategories(categories);
		user.setEmail(email);
		user.setFavorites(favorites);
		user.setKey(key);
		user.setLast_seen(lastSeen);
		user.setName(uname);
		user.setPassword(pass);
		user.setSeen_videos(seen_vids);
		user.setSign_up_date(signUpDate);
		
		return user; //the converted user object
		
		
	}
}
