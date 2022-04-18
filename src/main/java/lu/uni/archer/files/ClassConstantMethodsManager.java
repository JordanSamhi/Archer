package lu.uni.archer.files;

import lu.uni.archer.utils.Constants;
import soot.SootMethod;
import soot.Value;
import soot.jimple.InvokeExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class ClassConstantMethodsManager extends FileLoader {

    private final Map<String, Integer> methodsThatGenerateToBaseToParameterPosition;
    private final List<String> methodsThatPropagateBaseToReceiver;

    private static ClassConstantMethodsManager instance;

    private ClassConstantMethodsManager() {
        super();
        this.methodsThatGenerateToBaseToParameterPosition = new HashMap<>();
        this.methodsThatPropagateBaseToReceiver = new ArrayList<>();
        this.loadMethods();
    }

    public static ClassConstantMethodsManager v() {
        if (instance == null) {
            instance = new ClassConstantMethodsManager();
        }
        return instance;
    }

    protected String getFile() {
        return Constants.CLASS_CONSTANT_PROPAGATION_METHODS;
    }

    protected void loadMethods() {
        for (String line : this.items) {
            String[] split = line.split("\\|");
            if (split.length <= 1) {
                continue;
            }
            String method = split[0];
            int type = Integer.parseInt(split[1]);
            int paramPosition;
            switch (type) {
                case 1:
                    paramPosition = Integer.parseInt(split[2]);
                    this.methodsThatGenerateToBaseToParameterPosition.put(method, paramPosition);
                    break;
                case 2:
                    this.methodsThatPropagateBaseToReceiver.add(method);
                    break;
            }
        }
    }

    public boolean isInMethodsThatGenerateToBase(SootMethod sm) {
        return this.methodsThatGenerateToBaseToParameterPosition.containsKey(sm.getSignature());
    }

    public boolean isInMethodsThatPropagateBaseToReceiver(SootMethod sm) {
        return this.methodsThatPropagateBaseToReceiver.contains(sm.getSignature());
    }

    public Value getClassConstant(InvokeExpr ie) {
        SootMethod sm = ie.getMethod();
        if (this.isInMethodsThatGenerateToBase(sm)) {
            return ie.getArg(this.methodsThatGenerateToBaseToParameterPosition.get(sm.getSignature()));
        }
        return null;
    }
}