/********************************************************************************
 * Copyright (c) 2013, Alexander Meijer, All Rights Reserved
 * Filename: VideoToEntity.java
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

import java.util.logging.Logger;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * The Class VideoToEntity. This class provides a method to convert video objects into JDO entities, that can be added to 
 * the google app engine high replication datastore
 */
public class VideoToEntity {
	
	/**
	 * Convert the given video into an Entity. The only element of the video to be converted that is absolutely necessary is 
	 * the tag field, since this is how the datastore Key is generated
	 *
	 * @param video The video object to be converted to an Entity for datastore storage
	 * @return The Entity that has been converted from a video object
	 */
	public static Entity convert(Video video){
		final Logger log = Logger.getLogger(EntityToVideo.class.getName());
		//if the video does not have a key (i.e. a new video to add), then generate one
		Key k;
				if(video.getKey() == null){
					//generate datastore key from video tag, since that should be unique
					k = new KeyFactory.Builder("Video", video.getTag()).getKey();
					
				}else {
					
					//otherwise just re-use the old tag
					k = video.getKey();
				}
		Entity result = new Entity(k);
		
		//get the stuff to store from the video object... 
		String tag =  video.getTag();
		long ffwdTime = video.getFFWDtime();
		long Ytviews =  video.getYTViewCount();
		String category = video.getCategory();
		double popularity = video.getPopularity();
		long cbviews = video.getViewCount();
		
		//...and load it into the entity, with the field corresponding to what is laready in the DB
		result.setProperty("tag", tag);
		result.setProperty("FFWDtime", ffwdTime);
		result.setProperty("YTViewCount", Ytviews);
		result.setProperty("category", category);
		result.setProperty("popularity", popularity);
		result.setProperty("viewCount", cbviews);
		
		return result; // the entity, ready for storage
		
		
	}
}
