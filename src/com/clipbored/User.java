/********************************************************************************
 * Copyright (c) 2013, Alexander Meijer, All Rights Reserved
 * Filename: User.java
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


import com.google.appengine.api.datastore.Key;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * User entity designed to represent relevant user information about users of the ClipBored Service
 */
@Entity
public class User {

	/** The key. Used to sort and partially authenticate users*/
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Key key;
  
  /**
   * Sets the key.
   *
   * @param key the key. Google's KeyFactory class should be used to generate this.
   * @return the key The key added for this user
   */
  public Key setKey(Key key){
	  this.key = key;
	  //return the key. Used for checking/copying
	  return this.key;
  }

  /** The username. */
  private String name;
  
  /** The user's email. */
  private String email;

  /** The favorites. Represented as an arraylist of Youtube video tags in string format*/
  private ArrayList<String> favorites;
  
  /** The Videos that this user has seen. Represented as an arraylist of Youtube video tags in string format */
  private ArrayList<String> seen_videos;
  
  /** The user's password. This is double MD5 hashed in the backend, so if it has been retrieved via the API it will be encryped. It is stored in plaintext locally.*/
  private String password;
  
  /** The date & time that the user was last seen. note that this is stored as a google object, so use com.google.api.client.util.DateTime in clients */
  private Date last_seen;
  
  /** The date & time that the user signed up for ClipBored. note that this is stored as a google object, so use com.google.api.client.util.DateTime in clients */
  private Date sign_up_date;
  
  /** The categories that a user has selected. Stored as an arraylist of strings. This will be used eventually when users can select what categories are displayed on the clipbored home*/
  private ArrayList<String> categories;
  
  /**
   * Gets the user name.
   *
   * @return the name The username.
   */
  public String getName() {
	return name;
}

  /**
   * Sets the user name.
   *
   * @param name the new username
   */
public void setName(String name) {
	this.name = name;
}

/**
 * Gets the user's email.
 *
 * @return the email The user's email. 
 */
public String getEmail() {
	return email;
}

/**
 * Sets the user's email.
 *
 * @param email the new user email
 */
public void setEmail(String email) {
	this.email = email;
}

/**
 * Gets the user's favorites as an arraylist of youtube tag strings. 
 *
 * @return the favorites Arraylist of youtube strings the user has favorited. 
 */
public ArrayList<String> getFavorites() {
	return favorites;
}

/**
 * Sets the user's favorites as an arraylist of youtube tag strings. 
 *
 * @param favorites the new favorites to give this user
 */
public void setFavorites(ArrayList<String> favorites) {
	this.favorites = favorites;
}

/**
 * Gets the youtube tags of videos that this user has seen as an arraylist of youtube tags.  
 *
 * @return the seen_videos An arraylist of youtube tags that correspond to videos that this user has seen
 */
public ArrayList<String> getSeen_videos() {
	return seen_videos;
}

/**
 * Sets the youtube tags of videos that this user has seen as an arraylist of youtube tags.  
 *
 * @param seen_videos the new seen_videos An arraylist of youtube tags that correspond to videos that this user has seen.
 */
public void setSeen_videos(ArrayList<String> seen_videos) {
	this.seen_videos = seen_videos;
}

/**
 * Gets the user's password. Note that if this user has been retrieved via the API, this will be a double MD5 hash. 
 * However, if the user is stored locally, it will be the user's password in plaintext
 *
 * @return the password A string of either the MD5 hash of the password, or the password in plaintext. Note the MD5 will be in UTF-16, so the chars might be unintelligible
 */
public String getPassword() {
	return password;
}

/**
 * Sets the password. This will be hashed with a double round of MD5 by the backend. 
 * Leave this null to when updating a user to keep their old password
 *
 *DO NOT LOAD THE MD5 in here - it will be digested again!
 *
 * @param password the new password. Leave null or set to the null string ("") when updating the user to keep the user's old password. 
 */
public void setPassword(String password) {
	this.password = password;
}

/**
 * Gets the date & time that the user signed up for ClipBored. Note that this is best viewed a a google DateTime, use class com.google.api.client.util.DateTime in clients
 *
 * @return the sign_up_date The date the user signed up. Might be a DateTime object, maybe a checking function is necessary? 
 */
public Date getSign_up_date() {
	return sign_up_date;
}

/**
 * Sets the sign_up_date. Will be converted to a DateTime object when stored in the backend
 *
 * @param sign_up_date the new sign_up_date as a date object. This will remain a date until pushed via the API
 */
public void setSign_up_date(Date sign_up_date) {
	this.sign_up_date = sign_up_date;
}

/**
 * Gets the date & time that the user last used either the web UI or the app on ClipBored. Note that this is best viewed a a google DateTime, use class com.google.api.client.util.DateTime in clients
 *
 * @return the last_seen The date that the user last used Clipbored. Might be a DateTime object, maybe a checking function is necessary? 
 */
public Date getLast_seen() {
	return last_seen;
}

/**
 * Sets the last_seen date for the user. Will be converted to a DateTime object when stored in the backend
 *
 * @param last_seen the new last_seen as a date object. This will remain a date object until pushed via the API
 */
public void setLast_seen(Date last_seen) {
	this.last_seen = last_seen;
}

/**
 * Gets the categories that the user has chosen to display on their clipbored app.
 *
 * @return the categories A string of categories that the user wants to display on their home screen 
 */
public ArrayList<String> getCategories() {
	return categories;
}

/**
 * Sets the categories  that the user has chosen to display on their clipbored app.
 *
 * @param categories the new categories to be displayed for the particular user. The contents of this collection should be strings that correspond to category names that the user wants. 
 */
public void setCategories(ArrayList<String> categories) {
	this.categories = categories;
}



  /**
   * Gets the key. Uses android key format. 
   *
   * @return the key an android KeyFactory key. 
   */
  public Key getKey() {
    return key;
  }
  
  /**
   * Clear key. This will force a re-hash of the key by the backend.
   * NOTE - if this method is called the password and username must be filled. 
   */
  void clearKey() {
    key = null;
  }


}
