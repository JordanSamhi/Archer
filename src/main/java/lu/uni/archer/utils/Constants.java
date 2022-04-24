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
 *
 * @author Jordan Samhi
 */
public class Constants {

    /**
     * Misc names
     */
    public static final String TOOL_NAME = "Archer";
    public static final String SOURCE = "SOURCE";
    public static final String SINK = "SINK";

    /**
     * Files
     */
    public static final String CLASSES_TO_METHODS = "/classes_to_methods.txt";
    public static final String LIBRARIES_FILE = "/libraries.txt";
    public static final String SOURCES_SINKS_FILE = "/sourcesAndSinks.txt";
    public static final String CLASS_CONSTANT_PROPAGATION_METHODS = "/classConstant_propagation_methods.txt";
    public static final String METHOD_CALL_PROPAGATION_METHODS = "/methodCall_propagation_methods.txt";
    public static final String TO_REMOVE_CG_METHODS = "/call_graph_methods_to_remove.txt";
    public static final String COLLECTION_METHODS = "/collection_methods.txt";

    /**
     * Analysis names
     */


    public static final String POSSIBLE_TYPES = "PossibleTypes";
    public static final String INTRA_POSSIBLE_TYPES = "INTRAPossibleTypes";
    public static final String IFDS_POSSIBLE_TYPES = "IFDSPossibleTypes";
    public static final String CLASS_CONSTANT_PROPAGATION = "ClassConstantPropagation";
    public static final String INTRA_CLASS_CONSTANT_PROPAGATION = "INTRAClassConstantPropagation";
    public static final String IFDS_CLASS_CONSTANT_PROPAGATION = "IFDSClassConstantPropagation";
    public static final String METHODS_CALLED_PROPAGATION = "MethodsCalledPropagation";
    public static final String INTRA_METHODS_CALLED_PROPAGATION = "INTRAMethodsCalledPropagation";
    public static final String IFDS_METHODS_CALLED_PROPAGATION = "IFDSMethodsCalledPropagation";
    public static final String FIELD_PROPAGATION = "FieldPropagation";
    public static final String INTRA_FIELD_PROPAGATION = "INTRAFieldPropagation";
    public static final String IFDS_FIELD_PROPAGATION = "IFDSFieldPropagation";


    /**
     * Methods
     */
    public static final String INIT = "void <init>()";
}