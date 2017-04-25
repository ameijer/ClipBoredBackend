/********************************************************************************
 * Copyright (c) 2013, Alexander Meijer, All Rights Reserved
 * Filename: UserToEntity.java
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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class UserToEntity {
	
	public static Entity convert(User user) throws UnsupportedEncodingException{
		
		final Logger log = Logger.getLogger(UserToEntity.class.getName());
		
		
		Key k;
		if(user.getKey() == null){

			byte[] asBytes;

			//MD5, round 1...
			try {
				asBytes = user.getPassword().getBytes("UTF-16");
			} catch (UnsupportedEncodingException e) {
				//try UTF-8...
				asBytes = user.getPassword().getBytes("UTF-8");
			}

			MessageDigest md1 = null;
			try {
				md1 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e2) {
				e2.printStackTrace();
			}
			byte[] digest1 = md1.digest(asBytes);


			//MD5, round 2...
			try {
				MessageDigest md2 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
			byte[] digest2 = md1.digest(digest1);

			log.info("Double MD5 hash calculated: " + digest2);


			log.info("looking for key " + digest2);


			//make the key from a combination of the hashed password and the username. This will only let somebody with the right username and password access the corresponding user. 
		
			try {
				k = new KeyFactory.Builder("User", user.getName()).addChild("User", new String(digest2, "UTF-16")).getKey();
			} catch (UnsupportedEncodingException e) {
				//try UTF-8...
				k = new KeyFactory.Builder("User", user.getName()).addChild("User", new String(digest2, "UTF-8")).getKey();
			}
		}else {
			k = user.getKey();
		}

		//create the new entity
			Entity result = new Entity(k);
			
			result.setProperty("categories", user.getCategories());
			result.setProperty("favorites", user.getFavorites());
			result.setProperty("seen_videos", user.getSeen_videos());
			result.setProperty("name", user.getName());
			result.setProperty("email", user.getEmail());
			result.setProperty("password", user.getPassword());
			result.setProperty("last_seen", user.getLast_seen());
			result.setProperty("sign_up_date", user.getSign_up_date());

			return result;


		}
	}
