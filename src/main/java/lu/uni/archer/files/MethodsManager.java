package lu.uni.archer.files;

import lu.uni.archer.utils.Constants;
import lu.uni.archer.utils.Utils;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

import java.util.*;

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

public class MethodsManager extends FileLoader {

    private final Map<String, String> executorsToExecutees;
    private final List<String> executees;
    private final List<String> helpers;
    private final List<SootClass> interfaces;
    private final List<SootClass> classes;
    private final List<SootClass> executeeClasses;

    public MethodsManager() {
        executorsToExecutees = new HashMap<>();
        executees = new ArrayList<>();
        helpers = new ArrayList<>();
        interfaces = new ArrayList<>();
        classes = new ArrayList<>();
        executeeClasses = new ArrayList<>();
        this.loadMethods();
    }

    protected String getFile() {
        return Constants.CLASSES_TO_METHODS;
    }

    protected void loadMethods() {
        for (String line : this.items) {
            String[] split = line.split("\\|");
            if (split.length == 4) {
                String clazz = split[0];
                int type = Integer.parseInt(split[1]);
                int classOrInterface = Integer.parseInt(split[2]);
                String methods = split[3];
                String[] sms;
                SootClass sc = Scene.v().getSootClass(clazz);
                if (classOrInterface == 1) {
                    Utils.add(this.classes, sc);
                } else if (classOrInterface == 2) {
                    Utils.add(this.interfaces, sc);
                }
                sms = methods.split("&");
                if (type == 1) {
                    for (String s : sms) {
                        String[] splitSrcTgt = s.split("@");
                        String src = splitSrcTgt[0];
                        String tgt = splitSrcTgt[1];
                        this.executorsToExecutees.put(src, tgt);
                    }
                } else {
                    if (type == 2) {
                        Utils.addAll(Arrays.asList(sms), this.executees);
                        this.executeeClasses.add(sc);
                    } else if (type == 3) {
                        Utils.addAll(Arrays.asList(sms), this.helpers);
                    }
                }
            }
        }
    }

    public Map<String, String> getExecutorsToExecutees() {
        return executorsToExecutees;
    }

    public List<String> getExecutees() {
        return executees;
    }

    public List<String> getHelpers() {
        return helpers;
    }

    public List<SootClass> getInterfaces() {
        return interfaces;
    }

    public List<SootClass> getClasses() {
        return classes;
    }

    public boolean isExecutee(SootMethod sm) {
        return this.executees.contains(sm.getSignature());
    }

    public boolean isHelper(SootMethod sm) {
        return this.helpers.contains(sm.getSignature());
    }

    public boolean isExecutor(SootMethod sm) {
        return this.executorsToExecutees.containsKey(sm.getSignature());
    }

    public List<SootClass> getExecuteeClasses() {
        return executeeClasses;
    }

    public boolean isExecuteeClass(SootClass sc) {
        return this.executeeClasses.contains(sc);
    }
}