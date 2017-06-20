/*******************************************************************************
 * Copyright 2014 Federal Chancellery Austria
 * MOA-ID has been developed in a cooperation between BRZ, the Federal
 * Chancellery Austria - ICT staff unit, and Graz University of Technology.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 *******************************************************************************/
function gup(name) {
	name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
  	var regexS = "[\\?&]"+name+"=([^&#]*)";
  	var regex = new RegExp( regexS );
  	var results = regex.exec( window.location.href );
  	if( results == null )
    	return "";
  	else
    	return results[1];
}
function jumpToAnker() {
	var jump = gup("jump");
	if (jump != "") {
		location.hash="#"+jump;
	}
}
function PVP2LoginIframe(url) {
	var el = document.getElementById("demonstrator_leftcontent");
	
	var iframe = document.createElement("iframe");
	iframe.setAttribute("src", url + "?referrer=" + window.location.href);
	iframe.setAttribute("width", "240");
	iframe.setAttribute("height", "220");
	iframe.setAttribute("frameborder", "0");
	iframe.setAttribute("scrolling", "no");
	iframe.setAttribute("title", "Login");

	var button = document.getElementById("submitbutton");
	button.parentNode.removeChild(button);
	
	el.appendChild(iframe, el);
}
