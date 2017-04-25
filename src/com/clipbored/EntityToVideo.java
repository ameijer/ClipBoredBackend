/********************************************************************************
 * Copyright (c) 2013, Alexander Meijer, All Rights Reserved
 * Filename: EntiyToVideo.java
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

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * The Class EntityToVideo. A class providing translation from Entities to Video objects
 */
public class EntityToVideo {
	
	/**
	 * Convert the given datastore entity object to a User object
	 *
	 * @param entity The entity to convert to a Video. If this method is called on an Entity that does not correspond to a Video, all the fields of the created Video will be null
	 * @return the video The converted Video. Should perform null checking to make sure the video was found in the Datastore
	 */
	public static Video convert(Entity entity){
		final Logger log = Logger.getLogger(EntityToVideo.class.getName());
		Video result = new Video();
		
		//get Video data values from entity parameter
		String tag = (String) entity.getProperty("tag");
		long ffwdTime = (Long) entity.getProperty("FFWDtime");
		long Ytviews = (Long) entity.getProperty("YTViewCount");
		String category = (String) entity.getProperty("category");
		double property = (double) entity.getProperty("popularity");
		long cbviews = (long) entity.getProperty("viewCount");
		Key key = (Key) entity.getKey();
		
		//set the fields of the output video using the values obtained from the entity above
		result.setCategory(category);
		result.setTag(tag);
		result.setPopularity(property);
		result.setViewCount((int)cbviews);
		result.setKey(key);
		result.setFFWDtime((int)ffwdTime);
		result.setYTViewCount((int)Ytviews);
		
		return result; //the converted Video object
		
		
	}
}
