$(function(){
  $('#mainArea').load(function(){
    var mh = 800;
        this.height = mh + "px";

    var h = this.contentWindow.document.body.scrollHeight;
        var w = this.contentWindow.document.body.scrollWidth;

        h += 150;

        if(h < mh){
          h = mh;
        }

        this.height = h + "px";
    });

  $("#style-switcher").hide();
 /* $('.timepicker').timepicker({
    template : 'dropdown',
    showMeridian : false,
    minuteStep : 5
  });
*/
  $('.datepicker').datepicker({
	   language: "kr",
	   todayBtn:"linked",
	   defaultViewDate: "today",
	   autoclose: true,
	   dateFormat : "yy-mm-dd",
	   format : "yyyy-mm-dd",
	});

  $("form.from-submit").each(function(){
    $(this).ajaxForm(function(r) {
      var rc = r.resultCode;

      if(rc != '0000'){
        alert(r.resultMsg);
        return;
      }
    });
  });
});

/*--------------------------------------*/
/* Spinner 설정 2017-09-20 Yoon 추가		*/
/*--------------------------------------*/
var __SUBMIT_FLAG__ = true; //SUBMIT 가능여부
var __SPINNER__;
function startSpinner(targetId) {
	var opts = {
            lines: 13 // The number of lines to draw
          , length: 28 // The length of each line
          , width: 14 // The line thickness
          , radius: 42 // The radius of the inner circle
          , scale: 0.5 // Scales overall size of the spinner
          , corners: 1 // Corner roundness (0..1)
          , color: '#000' // #rgb or #rrggbb or array of colors
          , opacity: 0.25 // Opacity of the lines
          , rotate: 0 // The rotation offset
          , direction: 1 // 1: clockwise, -1: counterclockwise
          , speed: 1 // Rounds per second
          , trail: 60 // Afterglow percentage
          , fps: 20 // Frames per second when using setTimeout() as a fallback for CSS
          , zIndex: 2e9 // The z-index (defaults to 2000000000)
          , className: 'spinner' // The CSS class to assign to the spinner
          , top: '50%' // Top position relative to parent
          , left: '50%' // Left position relative to parent
          , shadow: false // Whether to render a shadow
          , hwaccel: false // Whether to use hardware acceleration
          , position: 'absolute' // Element positioning
          }
	__SPINNER__ = new Spinner(opts).spin(document.getElementById(targetId));
}

function stopSpinner() {
	__SPINNER__.stop();
}

function dotcodeAutocomplete(f, func){
	$(f).find("select").change(function(){func.call(this, this)});
	$(f).find("input").keyup(function(){func.call(this, this)});
}

function f_popup_reload(url, data, l){
  var p = $("#popupArea");
  $("#popupArea div.modal-body").load(url, data, function(){
    p.find(".popup-form").submit(function(){
      f_popup_reload(url, $(this).so(), l);
      return false;
    });

    if(l)
      l.call(p);
  });
}

function f_popup(title, url, data, btns, w, l){
  var p = $("#popupArea");
  $("#popupArea span.model-title").empty().html(title);
  $("#popupArea div.modal-body").empty().load(url, data, function(){
    p.find(".popup-form").submit(function(){
      f_popup_reload(url, $(this).so(), l);
      return false;
    });

    if(l)
      l.call(p);
  });
  $("#popupArea div.modal-footer").empty();

  if(w){
    $("#popupArea div.modal-dialog").each(function(){
      this.style.setProperty("width", w + "px", "important");
    });
  }

  p.modal('show');

  btns = btns || [];
  btns.push({ title : '닫기', onclick : function(){ p.modal('hide'); } });

  $(btns).each(function(k,v){
    $('<button class="btn" type="submit">').appendTo($("#popupArea div.modal-footer"))
      .html(v.title)
      .click(v.onclick);
  });
};

function f_popup_close(){
  var p = $("#popupArea");
  p.modal('hide');
}

function f_submit(f, l){
  $(f).ajaxForm(function(r) {
    var rc = r.resultCode;

    if(rc != '0000'){
      alert(r.resultMsg);
      return;
    }

    l.call(f, r, r.resultData.data);
  });
}


function f_checkedValues(cb){
  var vs = [];
  cb.each(function(){
    if($(this).is(":checked"))
      vs.push(this.value);
  });

  return vs;
}

function f_ajaxSubmit(url, formId){
	
	if(!__SUBMIT_FLAG__) { return; } __SUBMIT_FLAG__ = false; 
	  startSpinner('__CONTENT_BODY__');
	formId.ajaxSubmit({
		url: url,
		type: "post",
		data: formId.serialize(),
		dataType: "json",
		success: function(response, statusText, xhr) {
			__SUBMIT_FLAG__ = true;
	    	if(typeof __SPINNER__ != "undefined") { __SPINNER__.stop(); }
	    	
			if ("success" == statusText) {
				alert(response.resultMsg);
				location.replace("list.html");
			}
		},
		error: function(response, statusText, error) {
			__SUBMIT_FLAG__ = true;
	    	if(typeof __SPINNER__ != "undefined") { __SPINNER__.stop(); }
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error); 
		    alert("저장에 실패 하였습니다.");
		}
	});
}

function f_multiChkVal(chkUnit, val){
	
	var value = '';
	
	
	for(var i = 0; i < chkUnit.lenght; i++){
		value += val + '|';
	}
	
	return value.substring(0, value.length -1);
	
}

function f_ajax(url, data, succ){
  if($(data).is("form")){
    data = $(data).so();
  }

  if($("#plupload_list").length){
    var name = $("#plupload_list input[name='plupload_name']").val();
    var maxlength = $("#plupload_list input[name='plupload_maxlength']").val();
    var files = pl_files();
    if(files.length > 0){
    	data[name] = maxlength == 1 ? files[0] : files;
    }
  }

  if(!__SUBMIT_FLAG__) { return; } __SUBMIT_FLAG__ = false; 
  startSpinner('__CONTENT_BODY__');
  $.ajax({
      url: url,
      dataType: "JSON",
      data: JSON.stringify(data),
      type: "POST",

      beforeSend: function(xhr) {
          xhr.setRequestHeader("Accept", "application/json");
          xhr.setRequestHeader("Content-Type", "application/json");
          xhr.setRequestHeader("AJAX", true);
          
          //console.log("url : "+$(location).attr('protocol')+"//"+$(location).attr('host')+""+$(location).attr('pathname')+""+$(location).attr('search'));
      },

      success: function(r) {
    	__SUBMIT_FLAG__ = true;
    	if(typeof __SPINNER__ != "undefined") { __SPINNER__.stop(); }
		var rc = r.resultCode;

		if(typeof rc !== 'undefined'
			&& rc != '0000'){
			alert(r.resultMsg);
			return;
		}

        if(typeof succ !== 'undefined')
        	succ.call(this, r);
      },
      error: function(xhr, status, err) {
    	  __SUBMIT_FLAG__ = true;
    	  if(typeof __SPINNER__ != "undefined") { __SPINNER__.stop(); }    	  
    	  //console.log("xhr.status :"+ xhr.status);
          if (xhr.status == 403) {
        	  alert("세션이 끊겼습니다. 다시 로그인 하세요.");
        	  
        	  //if(typeof(parent.opener) != undefined && typeof(parent.opener) != null){
        	  if(opener){
        		  window.opener.location.href=$(location).attr('protocol')+"//"+$(location).attr('host')+"/admin/index.jsp";
        		  window.close();
        	  }
        	  location.href=$(location).attr('protocol')+"//"+$(location).attr('host')+"/admin/index.jsp";
          } 
      }
  });
}


/**
 * Array.indexOf
 */
Array.prototype.indexOf = function(item, i) {
  i || (i = 0);
  var length = this.length;
  if (i < 0)
    i = length + i;
  for(; i < length; i++)
    if (this[i] === item) return i;
  return -1;
};

/**
 * Array.lastIndexOf
 */
Array.prototype.lastIndexOf = function(item, i) {
  i = isNaN(i) ? this.length : (i < 0 ? this.length + i : i) + 1;
  var n = this.slice(0, i).reverse().indexOf(item);
  return (n < 0) ? n : i - n - 1;
};

/**
 * Array.remove
 * @param from
 * @param to
 * @returns
 */
Array.prototype.remove = function(from, to) {
  var rest = this.slice((to || from) + 1 || this.length);
  this.length = from < 0 ? this.length + from : from;
  return this.push.apply(this, rest);
};

/**
 *
 * @param item
 */
Array.prototype.removeItem = function(item) {
  for (var i=0; i<this.length; i++) {
    if (this[i] == item)
      this.remove(i,i);
  }
};

/**
 * String.trim()
 */
String.prototype.trim = function(){
  var pattern = /(^\s*)|(\s*$)/g;
    return this.replace(pattern, "");
};

/*
 * Date Format 1.2.3
 * (c) 2007-2009 Steven Levithan <stevenlevithan.com>
 * MIT license
 *
 * Includes enhancements by Scott Trenda <scott.trenda.net>
 * and Kris Kowal <cixar.com/~kris.kowal/>
 *
 * Accepts a date, a mask, or a date and a mask.
 * Returns a formatted version of the given date.
 * The date defaults to the current date/time.
 * The mask defaults to dateFormat.masks.default.
 */

var dateFormat = function () {
  var	token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
    timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
    timezoneClip = /[^-+\dA-Z]/g,
    pad = function (val, len) {
      val = String(val);
      len = len || 2;
      while (val.length < len) val = "0" + val;
      return val;
    };

  // Regexes and supporting functions are cached through closure
  return function (date, mask, utc) {
    var dF = dateFormat;

    // You can't provide utc if you skip other args (use the "UTC:" mask prefix)
    if (arguments.length == 1 && Object.prototype.toString.call(date) == "[object String]" && !/\d/.test(date)) {
      mask = date;
      date = undefined;
    }

    // Passing date through Date applies Date.parse, if necessary
    date = date ? new Date(date) : new Date;
    if (isNaN(date)) throw SyntaxError("invalid date");

    mask = String(dF.masks[mask] || mask || dF.masks["default"]);

    // Allow setting the utc argument via the mask
    if (mask.slice(0, 4) == "UTC:") {
      mask = mask.slice(4);
      utc = true;
    }

    var	_ = utc ? "getUTC" : "get",
      d = date[_ + "Date"](),
      D = date[_ + "Day"](),
      m = date[_ + "Month"](),
      y = date[_ + "FullYear"](),
      H = date[_ + "Hours"](),
      M = date[_ + "Minutes"](),
      s = date[_ + "Seconds"](),
      L = date[_ + "Milliseconds"](),
      o = utc ? 0 : date.getTimezoneOffset(),
      flags = {
        d:    d,
        dd:   pad(d),
        ddd:  dF.i18n.dayNames[D],
        dddd: dF.i18n.dayNames[D + 7],
        m:    m + 1,
        mm:   pad(m + 1),
        mmm:  dF.i18n.monthNames[m],
        mmmm: dF.i18n.monthNames[m + 12],
        yy:   String(y).slice(2),
        yyyy: y,
        h:    H % 12 || 12,
        hh:   pad(H % 12 || 12),
        H:    H,
        HH:   pad(H),
        M:    M,
        MM:   pad(M),
        s:    s,
        ss:   pad(s),
        l:    pad(L, 3),
        L:    pad(L > 99 ? Math.round(L / 10) : L),
        t:    H < 12 ? "a"  : "p",
        tt:   H < 12 ? "am" : "pm",
        T:    H < 12 ? "A"  : "P",
        TT:   H < 12 ? "AM" : "PM",
        Z:    utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
        o:    (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
        S:    ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
      };

    return mask.replace(token, function ($0) {
      return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
    });
  };
}();

// Some common format strings
dateFormat.masks = {
  "default":      "ddd mmm dd yyyy HH:MM:ss",
  shortDate:      "m/d/yy",
  mediumDate:     "mmm d, yyyy",
  longDate:       "mmmm d, yyyy",
  fullDate:       "dddd, mmmm d, yyyy",
  shortTime:      "h:MM TT",
  mediumTime:     "h:MM:ss TT",
  longTime:       "h:MM:ss TT Z",
  isoDate:        "yyyy-mm-dd",
  isoTime:        "HH:MM:ss",
  isoDateTime:    "yyyy-mm-dd'T'HH:MM:ss",
  isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
};

// Internationalization strings
dateFormat.i18n = {
  dayNames: [
    "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
    "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
  ],
  monthNames: [
    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
    "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
  ]
};

// For convenience...
Date.prototype.format = function (mask, utc) {
  return dateFormat(this, mask, utc);
};


/********************************
 * fillZero() : 0 채워넣는 함수
 ********************************/
function fillZero(value, length) {
    var result = "" + value;

    for ( var step = result.length; step < length; step++) {
        result = "0" + result;
    }

    return result;
}

/* *****************************************
 * Date Range 체크 함수
/* ***************************************** */
function fn_dateRangeSet(stId,edId,setVal){
  var setValue = setVal;
  $(stId).datepicker({
       language: "kr",
       todayBtn:"linked",
       autoclose: true
    });

  if(setValue == "setStartDate"){
      $(stId).on('changeDate', function(selected){
        startDate = new Date(selected.date.valueOf()); // startDate = 선택한 날짜
        startDate.setDate(startDate.getDate(new Date(selected.date.valueOf()))); // Date형으로 변환후 일자만 Get
        $(edId).datepicker('setStartDate', startDate); // 종료일자의 startDate변수에 Set
      })
  }else{
    $(stId).on('changeDate', function(selected){
         FromEndDate = new Date(selected.date.valueOf()); // FromEndDate = 선택한 날짜
         FromEndDate.setDate(FromEndDate.getDate(new Date(selected.date.valueOf()))); // Date형으로 변환후 일자만 Get
         $(edId).datepicker('setEndDate', FromEndDate); // 시작일자의  endDate 변수에 Set
      })
  }
}


(function($){
  '$:nomunge';

  /**
   * serializeObject
   */
  $.fn.so = function(){
    var obj = {};

    $.each( this.serializeArray(), function(i,o){
      var n = o.name,
      v = o.value;

      obj[n] = obj[n] === undefined ? v
        : $.isArray( obj[n] ) ? obj[n].concat( v )
        : [ obj[n], v ];
    });

    this.each(function(){
		if($(this).hasClass("plfile")){
			obj[this.name] = $(this).data();
		}
    });

    return obj;
  };
})(jQuery);


sprintfWrapper = {

		init : function () {

			if (typeof arguments == "undefined") { return null; }
			if (arguments.length < 1) { return null; }
			if (typeof arguments[0] != "string") { return null; }
			if (typeof RegExp == "undefined") { return null; }

			var string = arguments[0];
			var exp = new RegExp(/(%([%]|(\-)?(\+|\x20)?(0)?(\d+)?(\.(\d)?)?([bcdfosxX])))/g);
			var matches = new Array();
			var strings = new Array();
			var convCount = 0;
			var stringPosStart = 0;
			var stringPosEnd = 0;
			var matchPosEnd = 0;
			var newString = '';
			var match = null;

			while (match = exp.exec(string)) {
				if (match[9]) { convCount += 1; }

				stringPosStart = matchPosEnd;
				stringPosEnd = exp.lastIndex - match[0].length;
				strings[strings.length] = string.substring(stringPosStart, stringPosEnd);

				matchPosEnd = exp.lastIndex;
				matches[matches.length] = {
					match: match[0],
					left: match[3] ? true : false,
					sign: match[4] || '',
					pad: match[5] || ' ',
					min: match[6] || 0,
					precision: match[8],
					code: match[9] || '%',
					negative: parseInt(arguments[convCount]) < 0 ? true : false,
					argument: String(arguments[convCount])
				};
			}
			strings[strings.length] = string.substring(matchPosEnd);

			if (matches.length == 0) { return string; }
			if ((arguments.length - 1) < convCount) { return null; }

			var code = null;
			var match = null;
			var i = null;

			for (i=0; i<matches.length; i++) {

				if (matches[i].code == '%') { substitution = '%'; }
				else if (matches[i].code == 'b') {
					matches[i].argument = String(Math.abs(parseInt(matches[i].argument)).toString(2));
					substitution = sprintfWrapper.convert(matches[i], true);
				}
				else if (matches[i].code == 'c') {
					matches[i].argument = String(String.fromCharCode(parseInt(Math.abs(parseInt(matches[i].argument)))));
					substitution = sprintfWrapper.convert(matches[i], true);
				}
				else if (matches[i].code == 'd') {
					matches[i].argument = String(Math.abs(parseInt(matches[i].argument)));
					substitution = sprintfWrapper.convert(matches[i]);
				}
				else if (matches[i].code == 'f') {
					matches[i].argument = String(Math.abs(parseFloat(matches[i].argument)).toFixed(matches[i].precision ? matches[i].precision : 6));
					substitution = sprintfWrapper.convert(matches[i]);
				}
				else if (matches[i].code == 'o') {
					matches[i].argument = String(Math.abs(parseInt(matches[i].argument)).toString(8));
					substitution = sprintfWrapper.convert(matches[i]);
				}
				else if (matches[i].code == 's') {
					matches[i].argument = matches[i].argument.substring(0, matches[i].precision ? matches[i].precision : matches[i].argument.length);
					substitution = sprintfWrapper.convert(matches[i], true);
				}
				else if (matches[i].code == 'x') {
					matches[i].argument = String(Math.abs(parseInt(matches[i].argument)).toString(16));
					substitution = sprintfWrapper.convert(matches[i]);
				}
				else if (matches[i].code == 'X') {
					matches[i].argument = String(Math.abs(parseInt(matches[i].argument)).toString(16));
					substitution = sprintfWrapper.convert(matches[i]).toUpperCase();
				}
				else {
					substitution = matches[i].match;
				}

				newString += strings[i];
				newString += substitution;

			}
			newString += strings[i];

			return newString;

		},

		convert : function(match, nosign){
			if (nosign) {
				match.sign = '';
			} else {
				match.sign = match.negative ? '-' : match.sign;
			}
			var l = match.min - match.argument.length + 1 - match.sign.length;
			var pad = new Array(l < 0 ? 0 : l).join(match.pad);
			if (!match.left) {
				if (match.pad == "0" || nosign) {
					return match.sign + pad + match.argument;
				} else {
					return pad + match.sign + match.argument;
				}
			} else {
				if (match.pad == "0" || nosign) {
					return match.sign + match.argument + pad.replace(/0/g, ' ');
				} else {
					return match.sign + match.argument + pad;
				}
			}
		}
	};

	sprintf = sprintfWrapper.init;
	
	/*--------------------------------------*/
	/* keyup 컴마 넣기  							*/
	/*--------------------------------------*/
    function set_comma(str) {
        var code = window.event.keyCode;

        if(code >= 48 && code <= 57){

            num = str.value;
            num = num.split(',').join('');
            var arr = num.split('.');
            var num = new Array();
            for (i = 0; i <= arr[0].length-1; i++) {
                num[i] = arr[0].substr(arr[0].length-1-i,1);
            if(i%3 == 0 && i != 0) num[i] += ',';
            }
            num = num.reverse().join('');
            if (!arr[1]) str.value = num; else str.value = num+'.'+arr[1];

            window.event.returnValue = true;
            return;
        }
        window.event.returnValue = false;
    }
    
    /*--------------------------------------*/
	/* KeyDown 숫자만 입력						*/
	/*--------------------------------------*/
    function fn_KeyDownOnlyNumber() {
    	var code = window.event.keyCode;

    	if ((code >= 35 && code <= 40) || (code >= 48 && code <= 57)
    			|| (code >= 96 && code <= 105) || code == 8 || code == 9
    			|| code == 13 || code == 46) {
    		window.event.returnValue = true;
    		return;
    	}
    	window.event.returnValue = false;
    }    

    /*--------------------------------------*/
	/* KeyPress 숫자만 입력(shift+숫자를 제외시킴)		*/
	/*--------------------------------------*/
    function fn_KeyPressOnlyNumber(){

    	var code = window.event.keyCode;

    	if(code >= 48 && code <= 57){
    		window.event.returnValue = true;
    		return;
    	}
    	window.event.returnValue = false;
    }
