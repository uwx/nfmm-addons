(function() {

if (!window.JSON) {
    window.JSON = {
        parse: function (sJson) { return eval("(" + sJson + ")"); }
    };
}

var browser = function () {
    var n = navigator.userAgent.toLowerCase();
    var b = {
    	webkit: /webkit/.test(n),
    	mozilla: (/mozilla/.test(n)) && (!/(compatible|webkit|trident)/.test(n)),
    	chrome: /chrome/.test(n) && !(/edge/.test(n)),
    	msie: (/msie/.test(n) || /trident/.test(n)) && (!/opera/.test(n)) && !(/edge/.test(n)),
    	edge: /edge/.test(n),
    	firefox: /firefox/.test(n) && !(/edge/.test(n)),
    	safari: (/safari/.test(n) && !(/chrome/.test(n)) && !(/edge/.test(n))),
    	opera: /opera/.test(n)
    };
    b.version = (b.safari) ? (n.match(/.+(?:ri)[\/: ]([\d.]+)/) || [])[1] : (n.match(/.+(?:ox|me|ra|ie|rv)[\/: ]([\d.]+)/) || [])[1];
    try {
        b.majorVersion = b.version.split('.')[0];
    } catch (e) {
        b.majorVersion = null;
    }
    

    b.getXmlHttp = function() {
        var xmlhttp;
        try {
            xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
        } catch(e) {
            try {
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            } catch(e) {
                xmlhttp = false;
            }
        }
        if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
            xmlhttp = new XMLHttpRequest();
        }
        return xmlhttp;
    };

    return b;
}();
    
// IE trim function fix
if (typeof String.prototype.trim !== 'function') {
    String.prototype.trim = function() {
        return this.replace(/^\s+|\s+$/g, '');
    };
}

var cookies = new (function() {
    this.set = function (value) {
        document.cookie = value;
    };    

    this.get = function (key) {
        var keyValuePairs = document.cookie.split(';');
        for (var i = 0; i < keyValuePairs.length; i++) {
            var keyValuePair = keyValuePairs[i].trim();
            if (keyValuePair.length == 0) {
                continue;
            }

            var keyValueArray = keyValuePair.split('=');
            if (keyValueArray.length != 2) {
                continue;
            }
            
            if (keyValueArray[0] == key) {
                return keyValueArray[1];
            }
        }

        return null;
    };
})();

var events = new (function() {
    this.attach = function(name, handler, useCapture) {
        if (document.addEventListener) {
            document.addEventListener(name, handler, useCapture);
        } else {
            document.attachEvent("on" + name, handler);
        }
    };
})();

var popIsOnSameDomain = true;
var popOpenMode = {
	flash: 1,
	pdf: 2,
	backgroundTab: 3,
	blurOnOpen: 4,
	standard: 5,
	tempTab: 6
};

// Important: keep in sync with full page script
var popUnder = function (url, options) {
	var skipOpen = false;
    var topWindow = (top != self && typeof (top.document.location.toString()) === 'string') ? top : self;
    var windowConfig = 'toolbar=no,scrollbars=yes,location=yes,statusbar=yes,menubar=no,resizable=1,width=' + options.width.toString() + ',height=' + options.height.toString() + ',screenX=' + options.left.toString() + ',screenY=' + options.top.toString();

    this.popMode = getPopOpenMode(options);
    if (this.popMode === popOpenMode.flash) {
    	initFlashPop(options.swfUrl);
    }

	var thisRef = this;
	this.open = function () {
		if (skipOpen === true) {
			skipOpen = false;
			return;
		}

        if (options.callback) {
            options.callback();
        }

        if (thisRef.popMode === popOpenMode.flash) {
        	var target = arguments[0].target;
        	window['showPopClickTarget'] = target;
        	popViaFlash();
            return;
        }

		if (thisRef.popMode === popOpenMode.pdf) {
			var target = arguments[0].target;
			window['showPopClickTarget'] = target;
			popViaPdf(arguments[0]);
			return;
		}

        // Chrome 30 and above use tabs
		if (thisRef.popMode === popOpenMode.backgroundTab) {
            openInBackgroundTab();
            return;
        }

        if (thisRef.popMode === popOpenMode.blurOnOpen) {
        	popViaBlurOnOpen(arguments[0] || window.event);
	        return;
        }

		if (thisRef.popMode === popOpenMode.tempTab) {
			var ghost = window.open('about:blank');
			ghost.open(url, generateName(), windowConfig);
			setTimeout(function () {
				ghost.focus();
				ghost.close();
			}, 100);
			return;
		}
		
		var popunder = topWindow.window.open(url, generateName(), windowConfig);
        if (popunder) {
            try {
                popunder.blur();
                popunder.opener.window.focus();
                window.self.window.focus();
                window.focus();

                if (browser.firefox) openCloseWindow();
                if (browser.webkit && !browser.edge) openCloseTab();
            } catch (e) { }
        }
    }

    function getPopOpenMode() {
    	var flashEnabled = typeof (navigator.mimeTypes["application/x-shockwave-flash"]) !== 'undefined';
    	var pdfEnabled = typeof (navigator.mimeTypes["application/pdf"]) !== 'undefined';

    	if (browser.firefox == true && browser.majorVersion >= 48) {
    		// Starting from firefox 48 mozilla added support for multiprocess windows feature which is enabled when all not compatible add-ons are disabled
    		// With multiprocess windows being ON standard approach leads to pop over. 
    		// It was found that flash based approach works with FF but FF will stop flash support in the end of 2016.
		    return popOpenMode.tempTab;
	    }

    	if (browser.chrome == true
			&& browser.majorVersion >= 51
			&& pdfEnabled) {
    		return popOpenMode.pdf;
    	}

    	if (browser.chrome == true
			&& browser.majorVersion >= 35
			&& browser.majorVersion < 51
			&& navigator.userAgent.toLowerCase().indexOf('mac os') == -1
			&& flashEnabled
			&& options.swfUrl) {
			// In chrome 51 flash approach doesn't work anymore if browser is not in a maximized state
    		return popOpenMode.flash;
    	}

    	if (browser.chrome == true && browser.majorVersion >= 30) {
    		return popOpenMode.backgroundTab;
    	}

    	if (browser.msie || browser.edge) {
		    return popOpenMode.blurOnOpen;
	    }

    	return popOpenMode.standard;
    }

    function openInBackgroundTab() {
        var a = document.createElement("a");
        a.href = url;
        a.target = generateName();
        
        var evt = document.createEvent("MouseEvents");
        // The tenth parameter of initMouseEvent sets ctrl key
        evt.initMouseEvent("click", true, true, window, 0, 0, 0, 0, 0, true, false, false, true, 0, null);
        a.dispatchEvent(evt);
    }

    function initFlashPop(swfUrl) {
        flashObj = document.createElement("object");
        flashObj.setAttribute("type", "application/x-shockwave-flash");
        flashObj.setAttribute("id", "flash_pop");
        flashObj.setAttribute("data", swfUrl);
        flashObj.setAttribute("style", "position:fixed;visibility:visible;left:0;top:0;width:1px;height:1px;z-index:99999");

        var winTransParam = document.createElement("param");
        winTransParam.setAttribute("name", "wmode");
        winTransParam.setAttribute("value", "transparent");
        flashObj.appendChild(winTransParam);

        var menuParam = document.createElement("param");
        menuParam.setAttribute("name", "menu");
        menuParam.setAttribute("value", "false");
        flashObj.appendChild(menuParam);

        var accessParam = document.createElement("param");
        accessParam.setAttribute("name", "allowscriptaccess");
        accessParam.setAttribute("value", "always");
        flashObj.appendChild(accessParam);

        var screenParam = document.createElement("param");
        screenParam.setAttribute("name", "allowfullscreen");
        screenParam.setAttribute("value", "true");
        flashObj.appendChild(screenParam);

        document.body.insertBefore(flashObj, document.body.firstChild);
        flashObj.focus();

        window['showPop'] = function () {
        	flashObj.style.width = "0px";
            flashObj.style.height = "0px";
            flashObj.style.visibility = "hidden";

            topWindow.window.open(url, generateName(), windowConfig);

	        propagateClick();
        };
    }

	function generateName() {
		return 'ad_' + Math.floor(89999999 * Math.random() + 10000000);
	}

    function propagateClick() {
    	if (window['showPopClickTarget']) {
    		skipOpen = true;

			var event = new MouseEvent('click', {
				'view': window,
				'bubbles': true,
				'cancelable': true
			});
			
			window['showPopClickTarget'].dispatchEvent(event);
		}
	}

    function popViaBlurOnOpen(event) {
		var popunder;
    	if (popIsOnSameDomain === false) {
    		// This part is full page script only as intermission loads pops from same domain
    		// In IE opening url from another domain pop under appears as a pop up. So open pop with url from the same domain and switch to the required url
    		popunder = topWindow.window.open(document.location.href, generateName(), windowConfig);
    		popunder.window.document.location = url;
    	} else {
    		popunder = topWindow.window.open(url, generateName(), windowConfig);
    	}

    	if (popunder) {
    		try {
    			popunder.blur();
    			popunder.opener.window.focus();
    			window.self.window.focus();
    			window.focus();

    			var target = event.target;
    			
    			event.preventDefault();
    			event.stopImmediatePropagation();
    			event.stopPropagation(); 

			    var interval = setInterval(function() {
				    try {
					    popunder.blur();
					    popunder.opener.window.focus();
					    window.self.window.focus();
					    window.focus();
				    } catch (eInr) {
				    }

				    if (document.hasFocus()) {
				    	clearInterval(interval);

					    skipOpen = true;
					    target.click();
				    }

			    }, browser.edge ? 500 : 100); // Edge requires more time to open window
    		} catch (e) { }
    	}
	}

	function popViaPdf(event) {
    	var smallWindowConfig = 'toolbar=no,scrollbars=yes,location=no,statusbar=yes,menubar=no,resizable=1,width=20,height=20,screenX=0,screenY=0,top=10000';
    	var popunder = topWindow.window.open('about:blank', generateName(), smallWindowConfig);

    	event.preventDefault();
    	event.stopImmediatePropagation();
    	event.stopPropagation();

	    insertPdf(function() {
	    	popunder.moveTo(0, 0);
	    	popunder.resizeTo(options.width, options.height);
	    	popunder.location = url;

	    	propagateClick();
	    });
    }

    function insertPdf(focusedCallback) {
    	var showPdf = "data:application/pdf;base64,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8JXNlw4XLN8J4Z8IoqYkUULQzi6nKz1vSLYkMJ97DvKA9GR+t3Q83GU1+I7Ab2Br6rPoCAD/BMgarnhL7WaBuN/0qf7acq/dU0raEprcK2TZIdla/fChLrOBFuc6jp0MBrknewEe73KH2+rYiwBPTJanbC6vYHzWN2fBQlE5in+nFlYQ9qnVTtMW190pyWrlM5VDFEpnSueI+ygA+jaTvpHcBM3AiHaAD5mPikqHQdPYJojBgB3fj9yzdGdyRBvV5YwH4UozSXtQXAasc+B6z5hHuROJHKGr5fQ9q+J+c8IiSbkBauupbtXTt5RDBVKdK7lxraBGVsfeekEX56Q/Xo8nvW/anAh0Zmki7TrdMylcyHsHwKSbogMxX7r/txtC63+gdAMbhZT2fu24HATEgUoN0DcaHCa1kzPjP4xZ3ktGI2Agy+szsSvbfwtmYn6EkfE0PQoD6FnH48is91eoDxayE8hJ/QrO/S4J7Meoqa122eEc44YiZI4Ym/ev8f+ARcDGylS6wqSkdVoiw1lyfJsNCmVuZHN0cmVhbQ1lbmRvYmoNNCAwIG9iag08PC9GaWx0ZXIvRmxhdGVEZWNvZGUvRmlyc3QgNS9MZW5ndGggODAvTiAxL1R5cGUvT2JqU3RtPj5zdHJlYW0NCoZByo5qIKTWkT0EsOrwTNaetewmRJyWO3WrqIJ6JkhapOIkOBXm6Kty1h1c0lkwtdUIuOP2IGwjcbmLOADc/lGUQDnAECeKk+ka8CInWOegDQplbmRzdHJlYW0NZW5kb2JqDTUgMCBvYmoNPDwvRmlsdGVyL0ZsYXRlRGVjb2RlL0ZpcnN0IDUvTGVuZ3RoIDE0NC9OIDEvVHlwZS9PYmpTdG0+PnN0cmVhbQ0K1wo+Snf8AlgtuOQBso4dq7qTBagU6Bcv2WCA3Bl1DJr7pSqmZ8tLmkruLIjVWgxvVgTmDP5BxV2741g5WfMw6Ooo82x1GyOpUks28fcb7ZvIDnUIy5q5nj5KtTYeHpzo6OMvfN2gMU/oHl6WWcl0XfExgmkozEnKTYtBZZWlNHXndkqJwN5JC3iw3JhZ4rJ3DQplbmRzdHJlYW0NZW5kb2JqDTYgMCBvYmoNPDwvRGVjb2RlUGFybXM8PC9Db2x1bW5zIDQvUHJlZGljdG9yIDEyPj4vRW5jcnlwdCAxNiAwIFIvRmlsdGVyL0ZsYXRlRGVjb2RlL0lEWzwxOEY1NTNGQ0I5ODZEQjQxOEYzMTFDQUEyMUU4NjhDNz48QUQ2Nzk1QzREQjMyRDk0N0FGQ0U5MzEyNzhGRTM4MjU+XS9JbmZvIDE0IDAgUi9MZW5ndGggNTQvUm9vdCAxNyAwIFIvU2l6ZSAxNS9UeXBlL1hSZWYvV1sxIDIgMV0+PnN0cmVhbQ0KaN5iYgACJka5+QxMDIzvgAQziOC9C+KuARIMb4GyFwRALAZGGMH4D4XLBOIyMgAEGABZSQgmDQplbmRzdHJlYW0NZW5kb2JqDXN0YXJ0eHJlZg0KMTE2DQolJUVPRg0K";
    	var blankPdf = "data:application/pdf;base64, JVBERi0xLj";

    	var div = document.createElement("div");
    	div.setAttribute("style", "visibility:hidden;width:0px;height:0px;opacity:0;position:absolute;top:100%;left:0;pointer-events:none;overflow:hidden;");
    	var obj = document.createElement("object");
    	obj.setAttribute("data", showPdf);
    	div.appendChild(obj);
    	document.body.appendChild(div);

    	var deletePdfElement = function () {
    		obj.setAttribute("data", blankPdf);
    		document.body.removeChild(div);
    		window.removeEventListener('focus', deletePdfElement);

		    focusedCallback();
	    };
    	window.addEventListener('focus', deletePdfElement);
    }

    function popViaFlash() {
        flashObj.style.width = "100%";
        flashObj.style.height = "100%";
    }

    function openCloseWindow() {
        var ghost = window.open('about:blank');
        ghost.focus();
        ghost.close();
    }

    function openCloseTab() {
        var nothing = '';
        var ghost = document.createElement("a");
        ghost.href = "data:text/html,<scr" + nothing + "ipt>window.close();</scr" + nothing + "ipt>";
        document.getElementsByTagName("body")[0].appendChild(ghost);

        var clk = document.createEvent("MouseEvents");
        clk.initMouseEvent("click", false, true, window, 0, 0, 0, 0, 0, true, false, false, true, 0, null);
        ghost.dispatchEvent(clk);

        ghost.parentNode.removeChild(ghost);
    }
};

function bindPopUnder(url, options) {
    var pop = new popUnder(url, options);
	
    if (pop.popMode == popOpenMode.flash) {
    	// mousedown is used instead of click because it is a pre condition for chrome pop via flash
    	events.attach('mousedown', pop.open, false);
    } else if (pop.popMode == popOpenMode.pdf || pop.popMode == popOpenMode.blurOnOpen) {
    	events.attach('click', pop.open, true);
    }
    else {
    	// click is needed for ie and firefox, otherwise pop up will be blocked
    	events.attach('click', pop.open, false);
    }
}

var Lbjs = {
    IsMouse: false,
    Flipped: false,
    UrlEncoded : 0,
    AdType: 0,
    ContentType: 0,
    Token: "",
    Key: null,
    AdUrl: "",
	KeyUrl: "",
    AdPop: false,
    Preview: false,
    AdPopUrl: "",
    TargetUrl: "",
    Countdown: 0,
    Initialized: false,
    UnloadTracked: false,
    UserId: null,
    AdBlockSpotted: false,
    UnloadRequested: false,
    SwfUrl : null,

    Init: function (num, params) {
        this.Token = params.Token;
        this.AuthKey = params.AuthKey;
        this.UrlEncoded = params.UrlEncoded;
        this.AdType = params.AdType;
        this.ContentType = params.ContentType;
        this.AdUrl = params.AdUrl;
        this.Preview = params.Preview;
        this.AdPopUrl = params.AdPopUrl;
        this.KeyUrl = params.KeyUrl;
        this.AdPop = params.AdPop;
        this.TargetUrl = params.TargetUrl;
        this.Countdown = params.Countdown;
        this.UserId = params.UserId;
        this.SwfUrl = params.SwfUrl;

        this.InitJsKey(num, this.Token);
        this.Content();
        this.Resize();
        
        // Restore url to normal if encoded
        if (this.UrlEncoded == true) {
            this.TargetUrl = this.Encode(this.ConvertFromHex(this.TargetUrl));
        }
        
        if (this.Preview == false) {
            this.Pop();
        }
        
        if (this.Gate.Exists(this.TargetUrl)) {
            this.TargetUrl = this.Gate.InsertGateKey(this.TargetUrl);
        }

        document.onmouseover = Lbjs.MouseOverHandler;
        window.onbeforeunload = this.UnloadHandler;
        
        document.getElementById('refLink').onclick = this.UnblockNavigation;
        document.getElementById('advertise').onclick = this.UnblockNavigation;
        document.getElementById('advertise2').onclick = this.UnblockNavigation;
    },
    
    isOldFormatLinkRequest: function () {
        // Link can be in two formats linkcode.domain.com or domain.com/linkcode. Check if link is of format linkcode.domain.com
        var parts = window.location.hostname.split('.');
        return parts.length > 2 && /[a-zA-Z0-9]{8}/.test(parts[0]);
    },
    
    storePopOpened: function(cookieName) {
        var cookiePath;
        if (this.isOldFormatLinkRequest()) {
            cookiePath = '/';
        } else {
            var locationParts = window.location.toString().split('//')[1].split('/');
            var codeMatch = locationParts[1].match(/^([a-zA-Z0-9]{1,8})/g);
            if (codeMatch.length > 0) {
                cookiePath = '/' + codeMatch[0];
            } else {
                cookiePath = '/';
            }
        }

        var now = new Date();
        var expires = new Date(now.getTime() + 24 * 60 * 60 * 1000);

        cookies.set(cookieName + '=1;expires=' + expires.toGMTString() + ';path=' + cookiePath);
    },
    
    Pop: function () {
        if (this.AdPop == false) {
            return;
        }
        
        var thisRef = this;
        bindPopUnder(this.AdPopUrl, {
            width: window.screen.availWidth,
            height: window.screen.availHeight,
            top: 0,
            left: 0,
            swfUrl: this.SwfUrl,
            callback: function () {
                var cookieName = "lb_popunder_" + thisRef.AdType + "_" + thisRef.ContentType;
                thisRef.storePopOpened(cookieName);
            }
        });
    },
    
    Util : {
        getQueryParam: function(targetStr, name) {
            var uriParts = targetStr.split('?');
            if (uriParts.length != 2) {
                return null;
            }

            var queryString = uriParts[1];

            var query = (function (keyValuePairs) {
                if (keyValuePairs == null || keyValuePairs.length == 0) {
                    return {};
                }
        
                var result = {};
                for (var i = 0; i < keyValuePairs.length; ++i) {
                    var keyValue = keyValuePairs[i].split('=');
                    if (keyValue.length != 2) continue;
                    result[keyValue[0]] = decodeURIComponent(keyValue[1].replace(/\+/g, " "));
                }
                return result;
            })(queryString.split('&'));

            return query[name] || null;
        },

        replaceQueryParam: function (targetStr, name, value) {
            var paramStart = targetStr.indexOf('&' + name + '=');
            if (paramStart == -1) {
                paramStart = targetStr.indexOf('?' + name + '=');
            }
            if (paramStart == -1) {
                throw 'Parameter not found';
            }

            var paramEnd = targetStr.indexOf('&', paramStart + 1);
            var paramValueStart = targetStr.indexOf('=', paramStart);

            var result = targetStr.substr(0, paramValueStart) + '=' + value;
            if (paramEnd != -1) {
                result += targetStr.substr(paramEnd, targetStr.length - paramEnd);
            }
            return result;
        },
        
        frameLoadedAndVisible: function (element, srcPattern) {
        	if (element == null || element.tagName.toLowerCase() !== 'iframe') {
                return false;
            }

            var currentElement = element;
            while (currentElement != null && currentElement !== document.body) {
                if (currentElement.style.visibility === 'hidden' && currentElement.style.display === 'none') {
                    return false;
                }

                currentElement = currentElement.parentNode;
            }
            
            if (element.clientHeight <= 0 || element.clientWidth <= 0) {
                return false;
            }
            
            if (element.src == null || element.src != srcPattern) {
                return false;
            }

            if (window.getComputedStyle) {
                var style = window.getComputedStyle(element, null);
                if (style && style.getPropertyValue && style.getPropertyValue('display') == 'none') {
                    return false;
                }
            } else if (element.offsetWidth == 0) {
                return false;
            }
            
            return true;
        }
    },
    
    Gate : {
        Exists: function(url) {
            var gate = Lbjs.Util.getQueryParam(url, "_lbGate");
            return gate != null && /^[0-9]{6}$/.test(gate);
        },
        
        InsertGateKey: function (url) {
            if (!this.Exists(url)) {
                return url;
            }

            var gate = Lbjs.Util.getQueryParam(url, "_lbGate");
            return Lbjs.Util.replaceQueryParam(url, "_lbGate", this.CalculateGateCode(gate).toString());
        },
        
        CalculateGateCode: function (gateId) {
            var result = Lbjs.UserId;
            var gateIdStr = gateId.toString();
            for (var i = 0; i < gateIdStr.length; i++) {
                var num = parseInt(gateIdStr.charAt(i));

                var navigatorLen = window.navigator.userAgent.length;

                num = num % (navigatorLen - 1);
                result += num;
                result += window.navigator.userAgent.charCodeAt(num);
            }
            return result;
        }
    },

    SetKey: function(key) {
        this.Key = key;
    },

    Timer: function() {
        var timer = document.getElementById("timer");
        var skipDisabled = document.getElementById("skip_disabled");

        timer.innerHTML = this.Countdown;

        if (--this.Countdown >= 0) {
            setTimeout(function() { Lbjs.Timer(); }, 1000);
        } else {
            if (this.TargetUrl == null || this.TargetUrl == '') {
                if (!this.Util.frameLoadedAndVisible(document.getElementById('content'), this.AdUrl)) {
                    this.AdBlockSpotted = true;
                } else {
                    this.LoadTargetUrl();
                }
            }

            this.Fader.FadeOut(timer, 500, null);
            this.Fader.FadeOut(skipDisabled, 750, function() { Lbjs.Skip(); });
            this.OnTimerComplete();
        }
    },
    
    LoadTargetUrl: function () {
    	var request = browser.getXmlHttp();
	    var adBlock = typeof (window['__noAdBlock']) === "undefined" || window['__noAdBlock'] !== true;
        request.open('GET', '/intermission/loadTargetUrl?t=' + this.Token + '&aK=' + this.AuthKey + '&a_b=' + adBlock, false);
        request.send(null);

        if (request.status === 200) {
            var response = JSON.parse(request.responseText);
            if (response.Success == true) {
                if (response.AdBlockSpotted == true) {
                    this.AdBlockSpotted = true;
                } else {
                    this.TargetUrl = response.Url;
                }
            } else if (typeof (response.Errors) !== "undefined" && response.Errors.length > 0) {
                this.TargetUrl = '';
            }
        }
    },

    OnTimerComplete: function () {},

    MouseOverHandler: function() {
        Lbjs.IsMouse = true;
    },

    UnloadHandler: function () {
        Lbjs.UnloadRequested = true;
        
        if (Lbjs.Key != null && Lbjs.UnloadTracked == false) {
            Lbjs.Track(6);
            Lbjs.UnloadTracked = true;
        }

        return "You are going to be redirected away from the current page. Are you sure you want to continue?\n\nNote: To arrive at your intended destination, you must click to stay on the page and then click 'Skip Ad'";
    },

    Resize: function() {

        var resizeWindow = false;
        var minWindowWidth = 1024;
        var minWindowHeight = 768;
        var windowWidth = this.GetWindowWidth();
        var windowHeight = this.GetWindowHeight();

        if (windowWidth < minWindowWidth) {
            if (screen.width < minWindowWidth)
                windowWidth = screen.width;
            else
                windowWidth = minWindowWidth;

            resizeWindow = true;
        }

        if (windowHeight < minWindowHeight) {
            if (screen.height < minWindowHeight)
                windowHeight = screen.height;
            else
                windowHeight = minWindowHeight;

            resizeWindow = true;
        }

        if (resizeWindow) {
            window.resizeTo(windowWidth, windowHeight);
            window.focus();
        }
    },

    Content: function() {

        var content = document.getElementById("content");
        var parent = content.parentNode;

        parent.removeChild(content);
        parent.appendChild(content);

        content.style.display = "";
        content.src = this.AdUrl;
    },
    
    UnblockNavigation: function () {
        // In some browsers timer execution in iframe can overwrite window.top.location in the middle of redirect
        // Code below prevents this
        if (Lbjs.UnloadRequested == true) {
            var content = document.getElementById("content");
            var parent = content.parentNode;
            parent.removeChild(content);
        }

        window.onbeforeunload = null;
    },

    Skip: function() {
        var skip = document.getElementById("skiplink");
        skip.style.display = "";
        skip.href = '#';
        
        /*if (this.AdBlockSpotted) {
            skip.onclick = function() {
                alert('You are not allowed to access the destination web page until you view an advertisement. Please make sure that all Ad blocking software is turned off and refresh the page.');
                return false;
            };
        } else */if (this.TargetUrl == null || this.TargetUrl == '') {
            skip.onclick = function () {
                alert('There was an error obtaining a link to the destination web page, please refresh the page.');
                return false;
            };
        } else {
        	skip.onclick = function (e) {
                Lbjs.UnblockNavigation();

                // Sctrip script to avoid XSS
                var url = Lbjs.TargetUrl;
                while (url.indexOf('javascript:') != -1) {
                    url = url.replace(/javascript:/g, '');
                }

                var evt = !e ? window.event : e;
                var which = evt.which || evt.keyCode;
                if (typeof which != 'undefined' && which != null) {
                    window.location.href = url;
                    // On safari mobile pop under prevents navigation if link is not set
                    skip.href = url;
                }
            };
        }

        skip.onmousedown = function () {
        	Lbjs.Track(4);
        };

        var height = screen.height;
        var width = screen.width;
        var browserHeight = document.documentElement.clientHeight || document.body.clientHeight;
        var browserWidth = document.documentElement.clientWidth || document.body.clientWidth;
        Lbjs.Track(1, "h=" + height + "&w=" + width + "&m=" + (Lbjs.IsMouse == true ? '1' : '0') + "&bh=" + browserHeight + "&bw=" + browserWidth);
    },

    Track: function(type, params) {
        var img = new Image();
        var rnd = Math.random();

        var url = "/track/" + type + "/?t=" + this.Token + "&rnd=" + rnd + "&k=" + this.Key + (params ? '&' + params : '');

        img.src = url;
    },

    TrackSync: function(type, params) {

        var rnd = Math.random();
        var url = "/track/" + type + "/?t=" + this.Token + "&rnd=" + rnd + "&k=" + this.Key + (params ? '&' + params : '');

        var xmlhttp = browser.getXmlHttp();
        xmlhttp.open('GET', url, false);
        xmlhttp.send(null);
    },

    AddScript: function(scriptUrl, scriptId) {

        var s1 = document.createElement("script");

        s1.type = "text/javascript";
        s1.id = scriptId;
        s1.async = true;
        s1.src = scriptUrl;

        var s2 = document.getElementsByTagName("script")[0];
        s2.parentNode.insertBefore(s1, s2);
    },

    CreateScriptElement: function(url) {
        var script = document.createElement("script");
        script.setAttribute("type", "text/javascript");

        var rand = Math.floor(89999999 * Math.random() + 10000000);
        script.src = (url.indexOf("?") > -1) ? url + "&" + rand : url + "?" + rand;

        return script;
    },

    AddScriptWithCallback: function(scriptUrl, callback) {
        var head = document.getElementsByTagName("head")[0];
        var script = this.CreateScriptElement(scriptUrl);

        var done = false;
        script.onload = script.onreadystatechange = function() {
            // Some browser has support of both ways and some might fire loaded event more than once (e.g. Opera)
            // So use done flag to prevent multiple callback calls
            if (done == false && (!this.readyState || this.readyState == "loaded" || this.readyState == "complete")) {
                done = true;
                script.onload = script.onreadystatechange = null;

                if (typeof callback == "function") {
                    callback();
                }
            }
        };

        // Use insertBefore to prevent an IE6 bug.
        head.insertBefore(script, head.firstChild);
    },

    AddEvent: function(target, eventName, handlerName) {

        if (target.addEventListener) {
            target.addEventListener(eventName, eval(handlerName), false);
        } else if (target.attachEvent) {
            target.attachEvent("on" + eventName, eval(handlerName));
        } else {
            var originalHandler = target["on" + eventName];
            if (originalHandler) {
                target["on" + eventName] = eval(handlerName);
            } else {
                target["on" + eventName] = eval(handlerName);
            }
        }
    },

    Encode: function(str) {

        var s = [], j = 0, x, res = '', k = arguments.callee.toString().replace(/\s+/g, "");
        for (var i = 0; i < 256; i++) {
            s[i] = i;
        }
        for (i = 0; i < 256; i++) {
            j = (j + s[i] + k.charCodeAt(i % k.length)) % 256;
            x = s[i];
            s[i] = s[j];
            s[j] = x;
        }
        i = 0;
        j = 0;
        for (var y = 0; y < str.length; y++) {
            i = (i + 1) % 256;
            j = (j + s[i]) % 256;
            x = s[i];
            s[i] = s[j];
            s[j] = x;
            res += String.fromCharCode(str.charCodeAt(y) ^ s[(s[i] + s[j]) % 256]);
        }
        return res;
    },

    ConvertFromHex: function(str) {

        var result = [];

        while (str.length >= 2) {
            result.push(String.fromCharCode(parseInt(str.substring(0, 2), 16)));
            str = str.substring(2, str.length);
        }

        return result.join("");
    },

    GetWindowHeight: function() {
        var myHeight = 0;

        if (typeof(window.innerHeight) == 'number') {
            myHeight = window.innerHeight;
        } else if (document.documentElement && document.documentElement.clientHeight) {
            myHeight = document.documentElement.clientHeight;
        } else if (document.body && document.body.clientHeight) {
            myHeight = document.body.clientHeight;
        }

        return myHeight;

    },

    GetWindowWidth: function() {
        var myWidth = 0;

        if (typeof(window.innerWidth) == 'number') {
            myWidth = window.innerWidth;
        } else if (document.documentElement && document.documentElement.clientWidth) {
            myWidth = document.documentElement.clientWidth;
        } else if (document.body && document.body.clientWidth) {
            myWidth = document.body.clientWidth;
        }

        return myWidth;
    },

    SetStyle: function(element, text) {
        element.setAttribute("style", text);
        element.style.cssText = text;
    },

    Fader: {
        FadeOut: function(element, duration, callback) {

            var params = {
                State: 0,
                Element: element,
                Duration: duration,
                Callback: callback,
                Remaining: 0,
                LastTick: 0
            };

            this.Init(params);
            this.Animate(params);
        },

        FadeIn: function(element, duration, callback) {

            var params = {
                State: 1,
                Element: element,
                Duration: duration,
                Callback: callback,
                Remaining: 0,
                LastTick: 0
            };

            this.Init(params);
            this.Animate(params);
        },

        Init: function(params) {

            var display = params.Element.style.display;

            if (display === "inline") {
                params.Element.style.display = "inline-block";
            } else {
                params.Element.style.display = "inline";
                params.Element.style.zoom = 1;
            }

            params.Remaining = params.Duration;
            params.LastTick = new Date().getTime();
        },

        Animate: function(params) {

            var newTick = new Date().getTime();
            var scope = this;
            var opacity;

            params.Remaining -= newTick - params.LastTick;
            opacity = params.Remaining / params.Duration;
            opacity = (params.State == 0) ? opacity : (1 - opacity);

            var setOpacity = function(opacity) {
                params.Element.style.opacity = opacity;
                params.Element.style.filter = 'alpha(opacity = ' + (opacity * 100) + ')';
            };

            if (params.Remaining <= 0) {

                if (params.State == 0) {
                    opacity = 0;
                    params.Element.style.display = "none";
                } else {
                    opacity = 1;
                    params.Element.style.display = "";
                }

                setOpacity(opacity);

                if (typeof params.Callback == "function")
                    params.Callback();

                return;
            } else {
                setOpacity(opacity);
            }

            params.LastTick = new Date().getTime();
            setTimeout(function() { scope.Animate(params); }, 50);
        }
    },

    InitJsKey: function(num, token) {
        window['_sK' + num] = function(v) {
            Lbjs.SetKey(v);
        };
        var thisRef = this;
        var onLoad = this.AdType == 2 ? function() { thisRef.Timer(); } : null;
        this.AddScriptWithCallback(this.KeyUrl, onLoad);
    }
};

window.initLbjs = function (authKey, params) {
    if (typeof(authKey) !== "string") {
        return;
    }
    
    Lbjs.Init(authKey, params);
};
})();
