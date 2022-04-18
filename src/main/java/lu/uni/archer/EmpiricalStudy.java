package lu.uni.archer;

import lu.uni.archer.files.LibrariesManager;
import lu.uni.archer.files.MethodsManager;
import lu.uni.archer.utils.CommandLineOptions;
import lu.uni.archer.utils.RedisManager;
import lu.uni.archer.utils.Utils;
import org.apache.commons.io.FilenameUtils;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;

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

public class EmpiricalStudy {

    private final RedisManager rm;
    private final String fileUnderStudyName;
    private final MethodsManager mm;

    public EmpiricalStudy(String redisServer, String redisPort, String redisPwd) {
        this.rm = new RedisManager(redisServer, redisPort, redisPwd);
        this.fileUnderStudyName = FilenameUtils.getBaseName(CommandLineOptions.v().getApk());
        this.mm = MethodsManager.v();
    }

    public void run() {
        Stmt stmt;
        InvokeExpr ie;
        SootMethod callee;
        String redisList = "empirical:results:methods";
        for (SootClass sc : Scene.v().getApplicationClasses()) {
            if (!Utils.isSystemClass(sc) && !LibrariesManager.v().isLibrary(sc)) {
                // Case 2: sm is executee, e.g., doWork
                List<SootClass> superClassesAndInterfaces = this.getSuperClassesAndInterfaces(sc);
                List<String> superClassesAndInterfacesNames = Utils.getNames(superClassesAndInterfaces);
                if (superClassesAndInterfaces.size() > 0) {
                    this.rm.send(redisList, String.format("%s|2|%s|%s", this.fileUnderStudyName, sc.getName(), String.join("&", superClassesAndInterfacesNames)));
                }
                for (SootMethod sm : sc.getMethods()) {
                    if (sm.isConcrete()) {
                        for (Unit u : sm.retrieveActiveBody().getUnits()) {
                            stmt = (Stmt) u;
                            if (stmt.containsInvokeExpr()) {
                                ie = stmt.getInvokeExpr();
                                callee = ie.getMethod();
                                // Case 1: callee is an executor, e.g., schedule
                                if (mm.isExecutor(callee)) {
                                    this.rm.send(redisList, String.format("%s|1|%s|%s|%s", this.fileUnderStudyName, callee.getSignature(), sm.getSignature(), sc.getName()));
                                }
                                // Case 3: callee is a helper method, e.g., setRequiresCharging
                                else if (mm.isHelper(callee)) {
                                    this.rm.send(redisList, String.format("%s|3|%s|%s|%s", this.fileUnderStudyName, callee.getSignature(), sm.getSignature(), sc.getName()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private List<SootClass> getSuperClassesAndInterfaces(SootClass sc) {
        List<SootClass> classes = new ArrayList<>();
        this.populateExecutees(Utils.getAllSuperClasses(sc), classes);
        this.populateExecutees(Utils.getAllInterfaces(sc), classes);
        return classes;
    }

    private void populateExecutees(List<SootClass> l, List<SootClass> classes) {
        for (SootClass clazz : l) {
            if (mm.isExecuteeClass(clazz)) {
                classes.add(clazz);
            }
        }
    }
}
