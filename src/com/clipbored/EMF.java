/********************************************************************************
 * Copyright (c) 2013, Alexander Meijer, All Rights Reserved
 * Filename: EMF.java
 * Author: Alexander Meijer
 * 
 * Located in package: com.clipbored
 * Project: ClipBored-AppEngine
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

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * The Class EMF. This is used to manage Google App Engine's persistence datastore DB
 */
public final class EMF {
	
	/** The Constant emfInstance. */
	private static final EntityManagerFactory emfInstance = Persistence
			.createEntityManagerFactory("transactions-optional");

	/**
	 * Instantiates a new emf.
	 */
	private EMF() {
	}

	/**
	 * Gets the entitymanager factory - this stays constant for all apps using the datastore.
	 *
	 * @return the entity manager factory
	 */
	public static EntityManagerFactory get() {
		return emfInstance;
	}
}