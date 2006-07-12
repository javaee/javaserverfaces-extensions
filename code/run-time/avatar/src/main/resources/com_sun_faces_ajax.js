/*
 * $Id: devtime.js,v 1.5 2006/01/13 16:05:28 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

var g_zones = [];
var gFacesPrefix = "com.sun.faces.avatar.";
var gPartial = gFacesPrefix + "Partial";
var gExecute = gFacesPrefix + "Execute";
var gRender = gFacesPrefix + "Render";
var gEvent = gFacesPrefix + "Event";
var gSuppressXML = gFacesPrefix + "SuppressXML";
var gViewState = "javax.faces.ViewState";
var gGlobalScope = this;

dojo.require("dojo.io.*");
dojo.require("dojo.event.*");
dojo.require("dojo.string.*");

function ajaxifyChildren(target, eventType, eventHook, inspectElementHook) {
    if (null == target.isAjaxified && 
        target.hasChildNodes()) {
	for (var i = 0; i < target.childNodes.length; i++) {
	    takeActionAndTraverseTree(target, target.childNodes[i], 
				      moveAsideEventType, eventType, 
				      eventHook, inspectElementHook);
	}
    }
    target.isAjaxified = true;
    return false;
}

function moveAsideEventType(ajaxZone, element, eventType, eventHook) {
    if ('on' == eventType.substring(0,2)) {
	eventType = eventType.substring(2);
    }
    var c = new Faces.Command(element, eventType, { render: g_zones.join(','),
                                                    ajaxZone: ajaxZone,
						    eventHook: eventHook });
}

function takeActionAndTraverseTree(target, element, action, eventType, eventHook, inspectElementHook) {
    var takeAction = false;

    // If the user defined an "inspectElement" function, call it.
    if (!(typeof inspectElementHook == 'function')) {
	if (typeof gGlobalScope[inspectElementHook] == 'function') {
	    inspectElementHook = gGlobalScope[inspectElementHook];
	}
    }
    if (null != inspectElementHook) {
	takeAction = inspectElementHook(element);
    }
    // If the function returned false or null, or was not defined...
    if (null == takeAction || !takeAction) {
      // if this element has a handler for the eventType
      if (null != element[eventType] &&
  	  null != element.getAttribute(eventType)) {
        takeAction = true;
      }
    }
    if (takeAction) {
	// take the action on this element.
	action(target, element, eventType, eventHook);
    }
    if (element.hasChildNodes()) {
	for (var i = 0; i < element.childNodes.length; i++) {
	    takeActionAndTraverseTree(target, element.childNodes[i], action, 
				      eventType, eventHook, inspectElementHook);
	}
    }
    return false;
}

function prepareRequest(ajaxZone, extraParams) {
    var stateFieldName = "javax.faces.ViewState";
    var stateElements = window.document.getElementsByName(stateFieldName);
    // In the case of a page with multiple h:form tags, there will be
    // multiple instances of stateFieldName in the page.  Even so, they
    // all have the same value, so it's safe to use the 0th value.
    var stateValue = stateElements[0].value;
    // We must carefully encode the value of the state array to ensure
    // it is accurately transmitted to the server.  The implementation
    // of encodeURI() in mozilla doesn't properly encode the plus
    // character as %2B so we have to do this as an extra step.
    var uriEncodedState = encodeURI(stateValue);
    var rexp = new RegExp("\\+", "g");
    var encodedState = uriEncodedState.replace(rexp, "\%2B");
    // A truly robust implementation would discern the form number in
    // which the element named by "clientId" exists, and use that as the
    // index into the forms[] array.
    var formName = window.document.forms[0].id;
    // build up the post data
    extraParams[stateFieldName] = encodedState;
    extraParams[formName] = formName;
    if (null != ajaxZone.id) {
	extraParams[ajaxZone.id] = ajaxZone.id;
    }
}

/**
 * If the "nodeName" property of argument "element" contains the string
 * "input" (case-insensitive), extract the name (or id) and value of
 * that element and the corresponding value and store it in the "props"
 * associative array.  Otherwise, recurse over the children of
 * "element".
 */

function collectParamsFromInputChildren(element, props) {
    collectParamsFromChildrenOfType(element, "input", props);
    return;
}

/**
 * If the "nodeName" property of argument "element" contains the string
 * argument "nodeName" (case-insensitive), extract the name (or id) and
 * value of that element and the corresponding value and store it in the
 * "props" associative array.  Otherwise, recurse over the children of
 * "element".
 */
function collectParamsFromChildrenOfType(element, nodeName, props) {
    var elementNodeName = element.nodeName;
    var name = null;
    var i = 0;
    var result = null;
    if (null != elementNodeName) {
	if (-1 != elementNodeName.toLowerCase().indexOf(nodeName)) {
	    if (null == (name = element.name)) {
		name = element.id;
	    }
	    if (null != name) {
		props[name] = element.value;
	    }
	}
    }
    if (element.hasChildNodes()) {
	for (i = 0; i < element.childNodes.length; i++) {
	    collectParamsFromChildrenOfType(element.childNodes[i], 
					    nodeName, props);
	}
    }
    return;
}


/* Object Extensions
 ***********************************************************/
Object.extend(String.prototype, {
	trim: function() {
		var s = this.replace( /^\s+/g, "" );
  		return s.replace( /\s+$/g, "" );
	}
});
Object.extend(Element, {
  replace: function(dest, src)  {
    var d = $(dest);
    if (!d) alert(dest + " not found");
	var parent = d.parentNode;
	var temp = document.createElement('div');
	var result = null;
	temp.id = d.id;
	temp.innerHTML = src;

	result = temp.firstChild;
	parent.replaceChild(temp.firstChild,d);
	return result;

  },
  serialize: function(e) {
	  var str = (e.xml) ? this.serializeIE(e) : this.serializeMozilla(e);
	  return str;
  },
  serializeIE: function(e) {
	  return e.xml;
  },
  serializeMozilla: function(e) {
	  return new XMLSerializer().serializeToString(e);
  },
  firstElement: function(e) {
	  var first = e.firstChild;
	  while (first && first.nodeType != 1) {
		  first = first.nextSibling;
	  }
	  return first;
  }
});
function $Error(e) {
	if (e.message) {
		return '['+e.lineNumber+'] ' + e.message;
	} else {
		return e.toString();
	}
};
function $Form(src) {
	if (src) {
		var form = $(src);
		while (form && form.tagName && form.tagName.toLowerCase() != 'form') {
			if (form.form) return form.form;
			if (form.parentNode) {
				form = form.parentNode;
			} else {
				form = null;
			}
		}
		if (form) return form;
	}
	return document.forms[0];
};

/* Facelet Utility Class
 ***********************************************************/
var Faces = {
	initialized: false,
	create: function() {
		return function() {
	  		if (Faces.initialized) {
      			this.initialize.apply(this, arguments);
	  		} else {
				var args = arguments;
				var me = this;
	  			Event.observe(window,'load',function() {
					Faces.initialized = true;
					me.initialize.apply(me, args);
				},false);
			}
		}
	},
	toArray: function(s,e) {
		if (typeof s == 'string') {
			return s.split((e)?e:' ').map(function(p) { return p.trim(); });
		}
		return s;
	},
	toString: function() {
		return "Faces Agent v. 1.0";
	}
};

/* ViewState Hash over a given Form
 ***********************************************************/
Faces.ViewState = Class.create();
Faces.ViewState.CommandType = ['button','submit','reset'];
// concat?
Faces.ViewState.Ignore = ['button','submit','reset','image'];
Faces.ViewState.prototype = {
    setOptions: function(options) {
	this.options= {};
	Object.extend(this.options, options || {});
    },
    initialize: function(form, options) {
	this.setOptions(options);

	// Skip the traversal if the user elected to have more control over
	// the post data.
	var eventHookType = typeof this.options.eventHook;
	var inputsType = typeof this.options.inputs;
	if (('void' != eventHookType && 'undefined' != eventHookType) ||
	    ('void' != inputsType && 'undefined' != eventHookType)) {
	    // Just get the state data.
	    var viewState = $(gViewState);
	    t = viewState.tagName.toLowerCase();
	    p = Form.Element.Serializers[t](viewState);
	    if (p && p[0].length != 0) {
		if (p[1].constructor != Array) p[1] = [p[1]];
		if (this[p[0]]) { this[p[0]] = this[p[0]].concat(p[1]); }
		else this[p[0]] = p[1];
	    }
	    
	    return;
	}
	var e = Form.getElements($(form));
	var t,p;
	// Traverse the elements of the form and slam all of them into this 
	// element's property set.
	for (var i = 0; i < e.length; i++) {
	    if (Faces.ViewState.Ignore.indexOf(e[i].type) == -1) {
		t = e[i].tagName.toLowerCase();
		p = Form.Element.Serializers[t](e[i]);
		if (p && p[0].length != 0) {
		    if (p[1].constructor != Array) p[1] = [p[1]];
		    if (this[p[0]]) { this[p[0]] = this[p[0]].concat(p[1]); }
		    else this[p[0]] = p[1];
		}
	    }
	};
	// For good measure,
	var source = this.options.source;
	// add source
	var action = $(source);
	if (action && action.form) {
	    this[action.name] = action.value || 'x';
	}
	else {
	    this[source] = source;
	}
	
    },
    toQueryString: function() {
	var q = new Array();
	var i,j,p,v;

	if (this.options.inputs) {
	    if (this[gViewState]) {
		p = encodeURIComponent(gViewState);
		v = encodeURIComponent(this[gViewState]);
		q.push(p+'='+v);
	    }
	    var inputs = this.options.inputs.split(",");
	    if (inputs) {
		for (i = 0; i < inputs.length; i++) {
		    if (this[inputs[i]]) {
			p = encodeURIComponent(inputs[i]);
			if (this[inputs[i]].constructor == Array) {
			    for (j = 0; j < this[inputs[i]].length; j++){
				v = this[inputs[i]][j];
				if (v) {
				    v = encodeURIComponent(v);
				    q.push(p+'='+v);
				}
			    }
			} else {
			    v = encodeURIComponent(this[inputs[i]]);
			    q.push(p+'='+v);
			}
		    }
		}
	    }
	}
	else {
	    for (property in this) {
		if (this[property]) {
		    if (typeof this[property] == 'function') continue;
		    p = encodeURIComponent(property);
		    if (this[property].constructor == Array) {
			for (j = 0; j < this[property].length; j++) {
			    v = this[property][j];
			    if (v) {
				v = encodeURIComponent(v);
				q.push(p+'='+v);
			    }
			}
		    } else {
			v = encodeURIComponent(this[property]);
			q.push(p+'='+v);
		    }
		}
	    }
	}
	if (this.options.parameters) {
	    q.push(this.options.parameters);
	}
	if (typeof this.options.eventHook == 'function') {
	    this.options.eventHook(this.options.ajaxZone, this.options.source,
				   q);
	}
	else if (typeof gGlobalScope[this.options.eventHook] == 'function') {
	    gGlobalScope[this.options.eventHook](this.options.ajaxZone, 
						 this.options.source, q);
	}
	    
	return q.join('&');
    }
};

/* Handles Sending Events to the Server
 ***********************************************************/
Faces.Event = Class.create();
Object.extend(Object.extend(Faces.Event.prototype, Ajax.Request.prototype), {
  initialize: function(source, options) {
    this.transport = Ajax.getTransport();
    this.setOptions(options);
	
	// make sure we are posting
	this.options.method = 'post';
	
	// get form
	this.form = $Form(source);
	this.url = this.form.action;
	
	// create viewState
	var viewState = null;

	if (this.options.inputs) {
	    viewState = new Faces.ViewState(this.form,
				 { inputs: this.options.inputs,
				   source: source,
				   ajaxZone: this.options.ajaxZone,
				   eventHook: this.options.eventHook });
	}
	else {
	    viewState = new Faces.ViewState(this.form,
					    { source: source,
					      ajaxZone: this.options.ajaxZone,
					      eventHook: this.options.eventHook });
	}
	
	// add passed parameters
	viewState.options.parameters = this.options.parameters;
	
	// initialize headers
	this.options.requestHeaders = this.options.requestHeaders || [];
	
	// guarantee our header
	this.options.requestHeaders.push(gPartial);
	this.options.requestHeaders.push('true');
	
	// add event
	if (this.options.event) {
	    var sourceId = $(source).id || $(source).name;
		sourceId += "," + this.options.event;
		if (this.options.immediate) {
			sourceId += ",immediate";
		}
		this.options.requestHeaders.push(gEvent);
		this.options.requestHeaders.push(sourceId);
	}

	if (this.options.execute) {
		this.options.requestHeaders.push(gExecute);
		this.options.requestHeaders.push(Faces.toArray(this.options.execute,',').join(','));
	}
	
	if (this.options.render) {
		this.options.requestHeaders.push(gRender);
		this.options.requestHeaders.push(Faces.toArray(this.options.render,',').join(','));
	}

	if (this.options.suppressXML) {
	    this.options.requestHeaders.push(gSuppressXML);
	    this.options.requestHeaders.push("true");
	}
	
	this.options.postBody = viewState.toQueryString();

    var onComplete = this.options.onComplete;
    this.options.onComplete = (function(transport, object) {
      this.renderView();
      if (onComplete) {
	  onComplete(transport, object);
      } else if (this.doEvalResponse) {
	  this.evalResponse();
      }
    }).bind(this);

	if (this.options.onException == null) {
		this.options.onException = this.onException;
	}
	
	// send request
    this.request(this.url);
  },
  renderView: function() {
     var xml = this.transport.responseXML;
     if (null == xml || typeof xml == 'undefined') {
	 return;
     }    
     var components = xml.getElementsByTagName('components')[0];
     var render = components.getElementsByTagName('render');
     var id, content, markup, str;
     for (var i = 0; i < render.length; i++) {
	 id = render[i].getAttribute('id');
	 content = render[i].firstChild.firstChild;
	 markup = content.text || content.data;
	 str = markup.stripScripts();
	 this.doEvalResponse = false;
	 if (typeof gGlobalScope.postInstallHook != 'undefined') {
	     Element.replace(id, str);
	     gGlobalScope.postInstallHook($(id), markup);
	 }
	 else {
	     Element.replace(id, str);
	     markup.evalScripts();
	 }
     }
     
     var state = state || xml.getElementsByTagName('state')[0].firstChild;
     if (state) {
	 var hf = $(gViewState);
	 if (hf) {
	     hf.value = state.text || state.data;
	 }
     }
  },
  evalResponse: function() {
	  if (this.responseIsSuccess()) {
	      var text = this.transport.responseText;
	      //alert(text);
	      if (text) {
		  try {
		      text.evalScripts();
		  } catch (e) {}
	      }
	  }
  },
  onException: function(o,e) {
	  throw e;
  }
});

/* Turn any Element into a Faces.Command
 ***********************************************************/
Faces.Command = Faces.create();
Faces.Command.prototype = {
	initialize: function(action, event, options) {
		var event = (event) || 'click';
		var options = options;
		var facesObserver = function(e) {
			new Faces.Event(action,options);
			// if this element can cause a form submit...
			if (Faces.ViewState.CommandType.indexOf(action.type) != -1){
			    Event.stop(e);
			}
			return false;
		};
		// If the element had no existing facesObserver
		if (typeof action.facesObserver == 'undefined') {
		    // store one here so we can remove the observer later.
		    action.facesObserver = facesObserver;
		}
		else {
		    Event.stopObserving(action,event,action.facesObserver,true);
		}
		Event.observe(action,event,facesObserver,true);
	}
};

/* Take any Event and delegate it to an Observer
 ***********************************************************/
Faces.Observer = Faces.create();
Faces.Observer.prototype = {
	initialize: function(trigger,events,options) {
		this.options = {};
		Object.extend(this.options, options || {});
		var source = this.options.source;
		var fn = function(e) {
			new Faces.Event((source || Event.element(e)), options);
			Event.stop(e);
			return false;
		};
		var event = events || 'click';
		var ta = Faces.toArray(trigger);
		var ea = Faces.toArray(events);
		for (var i = 0; i < ta.length; i++) {
			for (var j = 0; j < ea.length; j++) {
				Event.observe($(ta[i]),ea[j],fn,true);
			}
		}
	}
};
