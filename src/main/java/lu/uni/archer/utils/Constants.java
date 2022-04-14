package lu.uni.archer.utils;

/*-
 * #%L
 * Archer
 *
 * %%
 * Copyright (C) 2022 Jordan Samhi
 * University of Luxembourg - Interdisciplinary Centre for
 * Security Reliability and Trust (SnT) - TruX - All rights reserved
 *
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

/**
 * Different constants for the application
 * @author Jordan Samhi
 *
 */
public class Constants {
	
	/**
	 * Misc names
	 */
	public static final String TOOL_NAME = "Archer";

	/**
	 * Files
	 */
	public static final String CLASSES_TO_METHODS = "/classes_to_methods.txt";
	public static final String LIBRARIES_FILE = "/libraries.txt";

	/**
	 * Classes
	 */
	public static final String PERIODIC_WORK_REQUEST = "androidx.work.PeriodicWorkRequest$Builder";
	public static final String ONE_TIME_WORK_REQUEST = "androidx.work.OneTimeWorkRequest$Builder";
	public static final String WORK_REQUEST = "androidx.work.WorkRequest$Builder";

	/**
	 * Analysis names
	 */
	public static final String CLASS_CONSTANT_PROPAGATION = "ClassConstantPropagation";
}