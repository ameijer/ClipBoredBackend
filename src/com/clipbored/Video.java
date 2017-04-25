/********************************************************************************
 * Copyright (c) 2013, Alexander Meijer, All Rights Reserved
 * Filename: Video.java
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

import com.google.appengine.api.datastore.Key;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * video entity used to represent information about Clipbored videos.
 */
@Entity
public class Video {

	/**
	 * The key. This is how videos are sorted in the database. Note that the key
	 * is generated from the tag of the video
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;

	/**
	 * The Youtube tag. (it is the last part of the URL in youtube:
	 * http://www.youtube.com/watch?v=XXXXXXXXX
	 */
	private String tag;

	/**
	 * The view count (Clibored-specific) for the video. This could be used in
	 * the Clipbored ordering algorithm
	 */
	private int viewCount;

	/**
	 * The view count (from youtube) for the video. This could be used in the
	 * Clipbored ordering algorithm
	 */
	private int YTViewCount;

	/**
	 * The FFWd time. This is used for the clipbored "fast-forward to the good
	 * part" functionality
	 */
	private int FFWDtime;

	/** The category of the video. Used for building user-specific playlists */
	private String category;

	/**
	 * The popularity of the video. This is obtained via the youtube API, and
	 * can be sued for the clipbored ordering algorithm
	 */
	private double popularity;

	/**
	 * Gets the Youtube tag.
	 *
	 * @return the tag of the youtube video
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Sets the key. This will be automatically generated using the android
	 * KeyFactory
	 *
	 * @param key
	 *            the key The output of the android KeyFactory class
	 * @return the key The key assigned to the video. used for checking and
	 *         copying of keys.
	 */
	public Key setKey(Key key) {
		this.key = key;
		return this.key;
	}

	/**
	 * Sets the tag. this will be used with the key auto-genration with
	 * keyfactory.
	 *
	 * @param tag
	 *            the new tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Gets the view count. Used to calculate the order of this video in the
	 * Clipbored playlist
	 *
	 * @return the view count
	 */
	public int getViewCount() {
		return viewCount;
	}

	/**
	 * Sets the view count. Will be updated often since this is
	 * clipbored-specific
	 *
	 * @param viewCount
	 *            the new view count
	 */
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	/**
	 * Gets the yT view count.
	 *
	 * @return the yT view count
	 */
	public int getYTViewCount() {
		return YTViewCount;
	}

	/**
	 * Sets the yT view count. This will be used with a youtube API call to help
	 * calculate the order of the video in the clipbored playlist
	 *
	 * @param yTViewCount
	 *            the new yT view count
	 */
	public void setYTViewCount(int yTViewCount) {
		YTViewCount = yTViewCount;
	}

	/**
	 * Gets the fFWd time.
	 *
	 * @return the fFW dtime
	 */
	public int getFFWDtime() {
		return FFWDtime;
	}

	/**
	 * Sets the fFWd time. This will need to be updated according to clipbored
	 * algorithms
	 *
	 * @param fFWDtime
	 *            the new fFW dtime
	 */
	public void setFFWDtime(int fFWDtime) {
		FFWDtime = fFWDtime;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 *
	 * @param category
	 *            the new category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Gets the popularity
	 *
	 * @return the popularity
	 */
	public double getPopularity() {
		return popularity;
	}

	/**
	 * Sets the popularity of the video.
	 *
	 * @param popularity
	 *            the new popularity
	 */
	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key Android KeyFactory product
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * Clear key. This will most likely break the DB call
	 */
	void clearKey() {
		key = null;
	}

}
