package lu.uni.archer.utils;

import soot.SootClass;
import soot.util.Chain;

import java.util.ArrayList;
import java.util.List;

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

public class Utils {

    public static void add(List<SootClass> l, SootClass o) {
        if (!l.contains(o)) {
            l.add(o);
        }
    }

    public static void add(List<String> l, String o) {
        if (!l.contains(o)) {
            l.add(o);
        }
    }

    public static void addAll(List<String> l1, List<String> l2) {
        for (String s : l1) {
            Utils.add(l2, s);
        }
    }

    public static boolean isSystemClass(SootClass clazz) {
        String className = clazz.getName();
        return (className.startsWith("android.") || className.startsWith("java.") || className.startsWith("javax.")
                || className.startsWith("sun.") || className.startsWith("org.omg.")
                || className.startsWith("org.w3c.dom.") || className.startsWith("com.google.")
                || className.startsWith("com.android.") || className.startsWith("androidx."));
    }

    public static List<SootClass> getAllSuperClasses(SootClass sootClass) {
        List<SootClass> classes = new ArrayList<>();
        SootClass superClass;
        if (sootClass.hasSuperclass()) {
            superClass = sootClass.getSuperclass();
            classes.add(superClass);
            classes.addAll(getAllSuperClasses(superClass));
        }
        return classes;
    }

    public static List<SootClass> getAllInterfaces(SootClass sootClass) {
        List<SootClass> interfaces = new ArrayList<>();
        Chain<SootClass> interfacesImplemented;
        if (sootClass.getInterfaceCount() > 0) {
            interfacesImplemented = sootClass.getInterfaces();
            interfaces.addAll(interfacesImplemented);
            for (SootClass i : interfacesImplemented) {
                interfaces.addAll(getAllInterfaces(i));
            }
        }
        return interfaces;
    }

    public static List<String> getNames(List<SootClass> classes) {
        List<String> names = new ArrayList<>();
        for (SootClass sc : classes) {
            names.add(sc.getName());
        }
        return names;
    }

    public static String getClassNameFromSignature(String sig) {
        String tmp = sig.split(" ")[0];
        return tmp.substring(1, tmp.length() - 1);
    }

    public static String getMethodNameFromSignature(String sig) {
        String tmp = sig.split(" ")[2];
        return tmp.substring(0, tmp.indexOf("("));
    }

    public static String getReturnNameFromSignature(String sig) {
        return sig.split(" ")[1];
    }

    public static List<String> getParametersNamesFromSignature(String sig) {
        String tmp = sig.split(" ")[2];
        String params = tmp.substring(tmp.indexOf("(") + 1, tmp.indexOf(")"));
        String[] paramsArray = params.split(",");
        List<String> parameters = new ArrayList<String>();
        String p = null;
        for (int i = 0; i < paramsArray.length; i++) {
            p = paramsArray[i];
            if (!p.isEmpty()) {
                parameters.add(p);
            }
        }
        return parameters;
    }
}