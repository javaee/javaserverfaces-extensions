/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at
 * https://woodstock.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at https://woodstock.dev.java.net/public/CDDLv1.0.html.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 */

package com.sun.faces.mirror.generator;

import com.sun.faces.mirror.DeclaredComponentInfo;
import java.util.Set;

/**
 * Base generator for the JSP tag library descriptor file. In order to generate
 * full TLD docs, descriptions are used in the template.
 */
abstract public class TldFileGenerator extends FileGenerator {

    private Set<DeclaredComponentInfo> declaredComponentInfoSet;

    /**
     * Protected getter for property declaredComponentInfoSet.
     */
    protected Set<DeclaredComponentInfo> getDeclaredComponentInfoSet() {
        return this.declaredComponentInfoSet;
    }

    /**
     * Setter for property declaredComponentInfoSet.
     */
    public void setDeclaredComponentInfoSet(Set<DeclaredComponentInfo> declaredComponentInfoSet) {
        this.declaredComponentInfoSet = declaredComponentInfoSet;
    }
    
    private String namespace;

    /**
     * Protected getter for property namespace.
     */
    protected String getNamespace() {
        return this.namespace;
    }

    /**
     * Setter for property namespace.
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    private String namespacePrefix;

    /**
     * Protected getter for property namespacePrefix.
     * @return Value of property namespacePrefix.
     */
    protected String getNamespacePrefix() {
        return this.namespacePrefix;
    }

    /**
     * Setter for property namespacePrefix.
     */
    public void setNamespacePrefix(String namespacePrefix) {
        this.namespacePrefix = namespacePrefix;
    }
    
    @Override
    protected Escaper getEscaper() {
      return new XmlEscaper();
    }
        
}
