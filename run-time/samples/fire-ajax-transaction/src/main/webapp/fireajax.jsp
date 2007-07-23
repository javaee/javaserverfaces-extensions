<!--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.

 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"

 Contributor(s):

 If you wish your version of this file to be governed by only the CDDL or
 only the GPL Version 2, indicate your decision by adding "[Contributor]
 elects to include this software in this distribution under the [CDDL or GPL
 Version 2] license."  If you don't indicate a single choice of license, a
 recipient has the option to distribute your version of this file under
 either the CDDL, the GPL Version 2 or to extend the choice of license to
 its licensees as provided above.  However, if you add GPL Version 2 code
 and therefore, elected the GPL Version 2 license, then the option applies
 only if the new code is made subject to such option by the copyright
 holder.
-->

<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="g" uri="http://java.sun.com/jsf/fireAjaxTrans" %>

<h:panelGrid border="1" columns="2" cellspacing="10" headerClass="keytitle"> 
    <f:facet name="header">
        <h:outputText value="Command"/>
    </f:facet>
    <h:commandButton id="ajax1"
                     value="fireAjaxTransaction [ajax1]"
                     onclick="DynaFaces.fireAjaxTransaction(this,
                     {execute:'ajax1,_1'}); return false;"
                     actionListener="#{bean.getOptions}"/>  
    <h:commandButton id="ajax2"
                     value="fireAjaxTransaction [ajax2]"
                     onclick="DynaFaces.fireAjaxTransaction(this,
                     {execute:'ajax2,_6,_10'}); return false;"
                     actionListener="#{bean.getOptions}"/> 
    <h:commandButton id="ajax3"
                     value="fireAjaxTransaction [ajax3]"
                     onclick="DynaFaces.fireAjaxTransaction(this,
                     {execute:'ajax3,_5'}); return false;"
                     actionListener="#{bean.getOptions}"/> 
    <h:commandButton id="ajax4"
                     value="fireAjaxTransaction [ajax4]"
                     onclick="DynaFaces.fireAjaxTransaction(this,
                     {execute:'ajax4,_4,_9'}); return false;"
                     actionListener="#{bean.getOptions}"/>
    <h:commandButton id="ajax5"
                     value="fireAjaxTransaction [ajax5]"
                     onclick="DynaFaces.fireAjaxTransaction(this,
                     {execute:'ajax5',render:'_1,optionsTitle,fireOptions'}); return false;"
                     actionListener="#{bean.getOptions}"/>
    <h:commandButton id="ajax6"
                     value="fireAjaxTransaction [ajax6]"
                     onclick="DynaFaces.fireAjaxTransaction(this,
                     {execute:'ajax6',render:'_6,optionsTitle,fireOptions'}); return false;"
                     actionListener="#{bean.getOptions}"/>
    <h:commandButton id="ajax7"
                     value="fireAjaxTransaction [ajax7]"
                     onclick="DynaFaces.fireAjaxTransaction(this,
                     {execute:'ajax7,_10',render:'_5,optionsTitle,fireOptions'}); return false;"
                     actionListener="#{bean.getOptions}"/> 
    <h:commandButton id="ajax8"
                     value="fireAjaxTransaction [ajax8]"
                     onclick="DynaFaces.fireAjaxTransaction(this,
                     {execute:'ajax8,_1,_2,_3,_4',render:'_1,_2,_3,_4,optionsTitle,fireOptions'}); return false;"
                     actionListener="#{bean.getOptions}"/> 
</h:panelGrid>

