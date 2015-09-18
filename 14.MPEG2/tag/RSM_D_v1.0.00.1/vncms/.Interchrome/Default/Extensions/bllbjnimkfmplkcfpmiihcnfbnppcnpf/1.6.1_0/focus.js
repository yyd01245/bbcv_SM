try{
var BBCV_BROWSER = {
	FOCUS: { 
	getX : function(obj){   
		return obj.offsetLeft + (obj.offsetParent ? BBCV_BROWSER.FOCUS.getX(obj.offsetParent) : obj.x ? obj.x : 0);   
	},         
	getY : function(obj){   
		return (obj.offsetParent ? obj.offsetTop + BBCV_BROWSER.FOCUS.getY(obj.offsetParent) : obj.y ? obj.y : 0);   
	},
	onloadImages : function(){  
		var map, areas, j, i = 0, imgs = document.images;
		for (; i < imgs.length; i++) {  
			map = imgs[i].useMap;  
			if (map) if (map = document.getElementsByName(map.slice(1))[0]) {  
				areas = map.getElementsByTagName('area');
				for (j = 0; j < areas.length; j++){
					  var id = '_$area$_', pos = areas[j].coords.split(','), area = document.getElementById(id);  
					  areaA = document.createElement('a');  
					  if(areas[j].onclick!=null) areaA.href="javascript:"+areas[j].onclick.toString().replace(/^.*?\{([^\}]*?)\}/,"$1");
					  else areaA.href = areas[j].href;
					  
					  //alert(areaA.href)
					  
						areaA.id = id, areaA.target = areas[j].target, areaA._area = this;  
						pos[0] -= 0, pos[1] -= 0, pos[2] -= pos[0], pos[3] -= pos[1];
						//alert(pos[0]+";"+pos[1]);  
						with (areaA.style) {  
							position = 'absolute';  
							left = BBCV_BROWSER.FOCUS.getX(imgs[i]) + pos[0]+"px";  
							top = BBCV_BROWSER.FOCUS.getY(imgs[i]) + pos[1]+"px";  
							width = pos[2]+"px";  
							height = pos[3]+"px";  
						}
						areas[j].parentNode.appendChild(areaA);
				}
			}
		};
	   
	},
	init : function() {
		BBCV_BROWSER.FOCUS.onloadImages()
		BBCV_BROWSER.FOCUS.aLink  = document.getElementsByTagName("a"); //所有A标签对象
		for(var i =0;i<BBCV_BROWSER.FOCUS.aLink.length;i++) {
			var aPosi = BBCV_BROWSER.FOCUS.getRealStyle(BBCV_BROWSER.FOCUS.aLink[i],"position");
			if(aPosi!="absolute") BBCV_BROWSER.FOCUS.aLink[i].style.position = "relative"
			BBCV_BROWSER.FOCUS.aLink[i].style.display = "inline-block"
		}
		BBCV_BROWSER.FOCUS.inputLink  = document.getElementsByTagName("input"); //所有INPUT标签对象
		for(var j =0;j<BBCV_BROWSER.FOCUS.inputLink.length;j++) {
			var inputPosi = BBCV_BROWSER.FOCUS.getRealStyle(BBCV_BROWSER.FOCUS.inputLink[j],"position");
			if(inputPosi!="absolute") BBCV_BROWSER.FOCUS.inputLink[j].style.position = "relative";			
		}
		BBCV_BROWSER.FOCUS.selectLink  = document.getElementsByTagName("select"); //所有SELECT标签对象
		for(var m =0;m<BBCV_BROWSER.FOCUS.selectLink.length;m++) {
			var selectPosi = BBCV_BROWSER.FOCUS.getRealStyle(BBCV_BROWSER.FOCUS.selectLink[m],"position");
			if(selectPosi!="absolute") BBCV_BROWSER.FOCUS.selectLink[m].style.position = "relative";	
		}
		
		BBCV_BROWSER.FOCUS.aLinkCss =[];
		
		BBCV_BROWSER.FOCUS.linkFOCUS = 0;
		BBCV_BROWSER.FOCUS.cssInit();
		BBCV_BROWSER.FOCUS.showFOCUS();
		
		BBCV_BROWSER.FOCUS.moveFOCUS(BBCV_BROWSER.FOCUS.linkFOCUS);
		//alert(BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][2])
  
	},
	//获取style和外链CSS的值
	getRealStyle : function(element,styleName) {
		var realStyle = null;
		if(element.currentStyle){
			realStyle = element.currentStyle[styleName];
		}
		else if(window.getComputedStyle){
			realStyle = window.getComputedStyle(element,null)[ styleName];
		}
		return realStyle;
	},
	cssInit : function() {
		for(var i = 0;i<BBCV_BROWSER.FOCUS.aLink.length; i++) {
			var parent = BBCV_BROWSER.FOCUS.aLink[i];
			
			var left = BBCV_BROWSER.FOCUS.aLink[i].offsetLeft;
			var top = BBCV_BROWSER.FOCUS.aLink[i].offsetTop;
			while(parent.nodeName!="BODY") {
				parent = parent.parentNode;
				if(BBCV_BROWSER.FOCUS.getRealStyle(parent,"position")=="absolute" || BBCV_BROWSER.FOCUS.getRealStyle(parent,"position")=="relative") {
					left+=parent.offsetLeft;
					top+=parent.offsetTop;
				}
			}
			BBCV_BROWSER.FOCUS.aLinkCss[i] = new Array();
			BBCV_BROWSER.FOCUS.aLinkCss[i][0] = i;
			BBCV_BROWSER.FOCUS.aLinkCss[i][1] = BBCV_BROWSER.FOCUS.getX(BBCV_BROWSER.FOCUS.aLink[i]);
			BBCV_BROWSER.FOCUS.aLinkCss[i][2] = BBCV_BROWSER.FOCUS.getY(BBCV_BROWSER.FOCUS.aLink[i]);
			BBCV_BROWSER.FOCUS.aLinkCss[i][3] = BBCV_BROWSER.FOCUS.aLink[i].offsetWidth;
			BBCV_BROWSER.FOCUS.aLinkCss[i][4] = BBCV_BROWSER.FOCUS.aLink[i].offsetHeight;
			BBCV_BROWSER.FOCUS.aLinkCss[i][5] = "a";
			BBCV_BROWSER.FOCUS.aLinkCss[i][6] = i;
		}
		for(var j=0;j<BBCV_BROWSER.FOCUS.inputLink.length;j++) {
			var parentInput = BBCV_BROWSER.FOCUS.inputLink[j];
			
			var leftInput = BBCV_BROWSER.FOCUS.inputLink[j].offsetLeft;
			var topInput = BBCV_BROWSER.FOCUS.inputLink[j].offsetTop;
			while(parentInput.nodeName!="BODY") {
				parentInput = parentInput.parentNode;
				if(BBCV_BROWSER.FOCUS.getRealStyle(parentInput,"position")=="absolute" || BBCV_BROWSER.FOCUS.getRealStyle(parentInput,"position")=="relative") {
					leftInput+=parentInput.offsetLeft;
					topInput+=parentInput.offsetTop;
				}
			}
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+j] = new Array();
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+j][0] = BBCV_BROWSER.FOCUS.aLink.length+j;
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+j][1] = leftInput;
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+j][2] = topInput;
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+j][3] = BBCV_BROWSER.FOCUS.inputLink[j].offsetWidth;
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+j][4] = BBCV_BROWSER.FOCUS.inputLink[j].offsetHeight;
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+j][5] = BBCV_BROWSER.FOCUS.inputLink[j].type;
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+j][6] = j;
		}
		for(var m=0;m<BBCV_BROWSER.FOCUS.selectLink.length;m++) {
			var parentSelect = BBCV_BROWSER.FOCUS.selectLink[m];
			
			var leftSelect = BBCV_BROWSER.FOCUS.selectLink[m].offsetLeft;
			var topSelect = BBCV_BROWSER.FOCUS.selectLink[m].offsetTop;
			while(parentSelect.nodeName!="BODY") {
				parentSelect = parentSelect.parentNode;
				if(BBCV_BROWSER.FOCUS.getRealStyle(parentSelect,"position")=="absolute" || BBCV_BROWSER.FOCUS.getRealStyle(parentSelect,"position")=="relative") {
					leftSelect+=parentSelect.offsetLeft;
					topSelect+=parentSelect.offsetTop;
				}
			}
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+BBCV_BROWSER.FOCUS.inputLink.length+m] = new Array();
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+BBCV_BROWSER.FOCUS.inputLink.length+m][0] = BBCV_BROWSER.FOCUS.aLink.length+BBCV_BROWSER.FOCUS.inputLink.length+m;
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+BBCV_BROWSER.FOCUS.inputLink.length+m][1] = leftSelect;
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+BBCV_BROWSER.FOCUS.inputLink.length+m][2] = topSelect;
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+BBCV_BROWSER.FOCUS.inputLink.length+m][3] = BBCV_BROWSER.FOCUS.selectLink[m].offsetWidth;
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+BBCV_BROWSER.FOCUS.inputLink.length+m][4] = BBCV_BROWSER.FOCUS.selectLink[m].offsetHeight;
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+BBCV_BROWSER.FOCUS.inputLink.length+m][5] = "select";
			BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.aLink.length+BBCV_BROWSER.FOCUS.inputLink.length+m][6] = m;
		}
		var minLeft = BBCV_BROWSER.FOCUS.aLinkCss[0][1];
		var minTop = BBCV_BROWSER.FOCUS.aLinkCss[0][2];
		for(var n=0;n<BBCV_BROWSER.FOCUS.aLinkCss.length;n++) {
			if(BBCV_BROWSER.FOCUS.aLinkCss[n][3]==0 || BBCV_BROWSER.FOCUS.aLinkCss[n][4]==0) continue;
			if(BBCV_BROWSER.FOCUS.aLinkCss[n][2]==minTop) {
				if(BBCV_BROWSER.FOCUS.aLinkCss[n][1]<minLeft) {
					minLeft = BBCV_BROWSER.FOCUS.aLinkCss[n][1]
					BBCV_BROWSER.FOCUS.linkFOCUS = n;
					continue;
				}
			}
			if(BBCV_BROWSER.FOCUS.aLinkCss[n][2]<minTop) {
				minTop = BBCV_BROWSER.FOCUS.aLinkCss[n][2]
				BBCV_BROWSER.FOCUS.linkFOCUS = n;
			}
		}
		
	},
	showFOCUS : function() {
		var elment = document.createElement("div");
		elment.id = "browserbbcvisionTvFOCUS";
		document.body.appendChild(elment);
		document.getElementById("browserbbcvisionTvFOCUS").style.border = "2px solid red";
		document.getElementById("browserbbcvisionTvFOCUS").style.position = "absolute";
		document.getElementById("browserbbcvisionTvFOCUS").style.zIndex = 100000;
		
		var KEYBOARD = document.createElement("div");
		KEYBOARD.id = "KEYBOARD";
		document.body.appendChild(KEYBOARD);
		document.getElementById("KEYBOARD").style.zIndex = 100000;
		document.getElementById("KEYBOARD").style.position = "absolute"
		document.getElementById("KEYBOARD").style.display = "none";
		
		var LINK=document.createElement("link");
		LINK.rel = "stylesheet"
		LINK.type = "text/css"
		LINK.href = "FOCUSKEY/KB.css"
		document.getElementsByTagName("HEAD")[0].appendChild(LINK);  
	},
	moveFOCUS : function(index) {
		document.getElementById("browserbbcvisionTvFOCUS").style.left = BBCV_BROWSER.FOCUS.aLinkCss[index][1]-2+"px";
		document.getElementById("browserbbcvisionTvFOCUS").style.top = BBCV_BROWSER.FOCUS.aLinkCss[index][2]-2+"px";
		document.getElementById("browserbbcvisionTvFOCUS").style.width = BBCV_BROWSER.FOCUS.aLinkCss[index][3]+"px";
		document.getElementById("browserbbcvisionTvFOCUS").style.height = BBCV_BROWSER.FOCUS.aLinkCss[index][4]+"px";
	},
	keyMove : function(step) {
		var linkFOCUSLeft = BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][1];
		var linkFOCUSTop = BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][2];
		var minNum;
		var newArray = [];
		var newSame = false;
		var newSameArray = [];
		for(var i=0;i<BBCV_BROWSER.FOCUS.aLinkCss.length;i++) {
			var tempLinkFOCUSLeft = BBCV_BROWSER.FOCUS.aLinkCss[i][1];
			var tempLinkFOCUSTop = BBCV_BROWSER.FOCUS.aLinkCss[i][2];
			
			if(step==-2) {if(tempLinkFOCUSTop<linkFOCUSTop) newArray.push(BBCV_BROWSER.FOCUS.aLinkCss[i]);}
			else if(step==2) {if(tempLinkFOCUSTop>linkFOCUSTop) newArray.push(BBCV_BROWSER.FOCUS.aLinkCss[i])}
			else if(step==-1) {if(tempLinkFOCUSLeft<linkFOCUSLeft) newArray.push(BBCV_BROWSER.FOCUS.aLinkCss[i])}
			else if(step==1) {if(tempLinkFOCUSLeft>linkFOCUSLeft) newArray.push(BBCV_BROWSER.FOCUS.aLinkCss[i])}
		}
		
		for(var j=0;j<newArray.length;j++) {
			
			var c =  (linkFOCUSLeft-newArray[j][1])*(linkFOCUSLeft-newArray[j][1]) + (linkFOCUSTop-newArray[j][2])*(linkFOCUSTop-newArray[j][2]); //求两点间距离
			if(step==-2) {
				if(Math.abs(linkFOCUSLeft - newArray[j][1])<=10) {
					newSameArray.push(newArray[j])
					newSame = true;
				}
				else if(typeof(minNum) =="undefined" || c<=minNum) {
					var index = parseInt(newArray[j][0]);
					if(BBCV_BROWSER.FOCUS.aLinkCss[index][3]==0 || BBCV_BROWSER.FOCUS.aLinkCss[index][4]==0) continue;
					minNum = c
					BBCV_BROWSER.FOCUS.linkFOCUS = index;
				}
			}
			else if(step==2) {
				if(Math.abs(linkFOCUSLeft - newArray[j][1])<=10) {
					newSameArray.push(newArray[j])
					newSame = true;
				}
				else if(typeof(minNum) =="undefined" || c<minNum) {
					var index = parseInt(newArray[j][0]);
					if(BBCV_BROWSER.FOCUS.aLinkCss[index][3]==0 || BBCV_BROWSER.FOCUS.aLinkCss[index][4]==0) continue;
					minNum = c
					BBCV_BROWSER.FOCUS.linkFOCUS = index;
				}
			}
			else if(step==-1) {
				if(Math.abs(linkFOCUSTop - newArray[j][2])<=10) {
					newSameArray.push(newArray[j])
					newSame = true;
				}
				else if(typeof(minNum) =="undefined" || c<=minNum) {
					var index = parseInt(newArray[j][0]);
					if(BBCV_BROWSER.FOCUS.aLinkCss[index][3]==0 || BBCV_BROWSER.FOCUS.aLinkCss[index][4]==0) continue;
					minNum = c;
					BBCV_BROWSER.FOCUS.linkFOCUS = index;
				}
			}
			else if(step==1) {
				if(Math.abs(linkFOCUSTop - newArray[j][2])<=10) {
					newSameArray.push(newArray[j])
					newSame = true;
				}
				else if(typeof(minNum) =="undefined" || c<minNum) {
					var index = parseInt(newArray[j][0]);
					if(BBCV_BROWSER.FOCUS.aLinkCss[index][3]==0 || BBCV_BROWSER.FOCUS.aLinkCss[index][4]==0) continue;
					minNum = c
					BBCV_BROWSER.FOCUS.linkFOCUS = index;
				}
			}
			
			
		}
		if(newSame) {
			for(var m=0;m<newSameArray.length;m++) {
				
				if(step==-2 || step==2) {var mc = Math.abs(newSameArray[m][2] - linkFOCUSTop);}
				else if(step==-1 || step==1) {var mc = Math.abs(newSameArray[m][1] - linkFOCUSLeft);}
			
				
				if(typeof(minNum) =="undefined" || mc<minNum) {
					var index = parseInt(newSameArray[m][0]);
					if(BBCV_BROWSER.FOCUS.aLinkCss[index][3]==0 || BBCV_BROWSER.FOCUS.aLinkCss[index][4]==0) continue;
					minNum = mc
					BBCV_BROWSER.FOCUS.linkFOCUS = index;
				}
			}
		}
		BBCV_BROWSER.FOCUS.moveFOCUS(BBCV_BROWSER.FOCUS.linkFOCUS);
	},
	keyClick : function() {
		var linkType = BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][5];
		
		if(linkType=="a") {
			document.location.href = BBCV_BROWSER.FOCUS.aLink[BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][6]].href;
		}
		else if(linkType=="select") {//alert("下拉")
			var selectFOCUS = BBCV_BROWSER.FOCUS.selectLink[BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][6]];
			var index = selectFOCUS.selectedIndex;
			index++;
			if(index==selectFOCUS.length) index = 0;
			selectFOCUS[index].selected = true;
		}
		else if(linkType=="text" || linkType=="password") {//alert("输入框")
			var inputValue = BBCV_BROWSER.FOCUS.inputLink[BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][6]].value;
			KB.init();
			KB.show(inputValue);
		}
		else if(linkType=="radio") {//alert("单选框")
			BBCV_BROWSER.FOCUS.inputLink[BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][6]].checked = true;
		}
		else if(linkType=="checkbox") {//alert("多选框")
			BBCV_BROWSER.FOCUS.inputLink[BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][6]].checked = BBCV_BROWSER.FOCUS.inputLink[BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][6]].checked?false:true;
			
		}
		else if(linkType=="button" || linkType=="submit") {//alert("按钮")
			BBCV_BROWSER.FOCUS.inputLink[BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][6]].click()
		}
	},
	addLoadEvent : function(func) {
		var oldonload = window.onload;
		if(typeof window.onload != "function") {
			window.onload = func;
		} else {  
			window.onload = function() {
				oldonload();
				func();
			}
		}
	}
}
}
// 软键盘
String.prototype.trim   =   function()
{
	//   用正则表达式将前后空格用空字符串替代。
	return   this.replace(/(^\s*)|(\s*$)/g,   "");
}
var KB = {
	isOpen : false,
	isCap : false, // 是否开启大小写
	init: function() {
		KB.keyBoard = document.getElementById("KEYBOARD");
		KB.x = 1; //横轴
		KB.y = 1; //纵轴
		KB.max_y = 5;
		KB.topPos = 15;
		KB.leftPos = 14;
		KB.offsetX = 42;
		KB.offsetY = 42;
		KB.width = 42;
		
		KB.tbInit();
		//KB.input = document.getElementById("KB_input");
		KB.input = BBCV_BROWSER.FOCUS.inputLink[BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][6]]
		KB.FOCUS1 = document.getElementById("KB_FOCUS");
    },
	// 软键盘方法定义
    show: function(data) {
		KB.keyBoard.style.left = BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][1]+"px";
		KB.keyBoard.style.top = BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][2]+BBCV_BROWSER.FOCUS.aLinkCss[BBCV_BROWSER.FOCUS.linkFOCUS][4]+"px";
		KB.isOpen = true;
		KB.keyBoard.style.display="block";
		if(typeof(data) == "undefined" || data == "") {
			KB.input.value="";
		} else {
			KB.input.value=data.trim();;
		}
		KB.FOCUS1.style.width  = KB.width+"px";
		KB.FOCUSIn();
	},
	max_x : function() {
		return KB.keys[KB.y - 1].length;
	},
	tbInit: function() {
		var tb='<table cellspacing="0" cellpadding="0"><tr><td id="KB_td">';
		tb += '<div id="KB_keyboard_div"><table id="KB_keyboard" border="0" cellspacing="0" cellpadding="0">';
		tb += '<tr class="KB_center"><td height="42" width="42">1</td><td width="42">2</td><td width="42">3</td>';
		tb += '<td width="42">4</td><td width="42">5</td><td width="42">6</td><td width="42">7</td>';
		tb += '<td width="42">8</td><td width="42">9</td><td width="42">0</td></tr><tr class="KB_center">';
		tb += '<td height="42">q</td><td>w</td><td>e</td><td>r</td><td>t</td><td>y</td><td>u</td><td>i</td>';
		tb += '<td>o</td><td>p</td></tr><tr class="KB_center"><td height="42">a</td><td>s</td><td>d</td>';
		tb += '<td>f</td><td>g</td><td>h</td><td>j</td><td>k</td><td>l</td><td><img src=FOCUSKEY/email.png /></td></tr>';
		tb += '<tr class="KB_center"><td height="42"><img src=FOCUSKEY/tab.png /></td><td height="42">z</td><td>x</td><td>c</td><td>v</td><td>b</td><td>n</td>'
		tb += '<td>m</td><td>.</td><td><img src=FOCUSKEY/backspace.png /></td></tr><tr class="KB_center">';
		tb += '<td height="42" colspan="6">空 格</td><td colspan="2">重置</td><td colspan="2">确定</td>'
		tb += '</tr></table></div><div id="KB_FOCUS"></div></td></tr></table>';
		KB.keyBoard.innerHTML = tb;
    }, 
	tbInitLow: function() {
		var tbLow = '<table id="KB_keyboard" border="0" cellspacing="0" cellpadding="0">';
		tbLow += '<tr class="KB_center"><td height="42" width="42">1</td><td width="42">2</td><td width="42">3</td>';
		tbLow += '<td width="42">4</td><td width="42">5</td><td width="42">6</td><td width="42">7</td>';
		tbLow += '<td width="42">8</td><td width="42">9</td><td width="42">0</td></tr><tr class="KB_center">';
		tbLow += '<td height="42">q</td><td>w</td><td>e</td><td>r</td><td>t</td><td>y</td><td>u</td><td>i</td>';
		tbLow += '<td>o</td><td>p</td></tr><tr class="KB_center"><td height="42">a</td><td>s</td><td>d</td>';
		tbLow += '<td>f</td><td>g</td><td>h</td><td>j</td><td>k</td><td>l</td><td><img src=FOCUSKEY/email.png /></td></tr>';
		tbLow += '<tr class="KB_center"><td height="42"><img src=FOCUSKEY/tab.png /></td><td height="42">z</td><td>x</td><td>c</td><td>v</td><td>b</td><td>n</td>'
		tbLow += '<td>m</td><td>.</td><td><img src=FOCUSKEY/backspace.png /></td></tr><tr class="KB_center">';
		tbLow += '<td height="42" colspan="6">空 格</td><td colspan="2">重置</td><td colspan="2">确定</td>'
		tbLow += '</tr></table>';
		document.getElementById("KB_keyboard_div").innerHTML = tbLow;
    }, 
	tbInitCap: function() {
		var tbCap = '<table id="KB_keyboard" border="0" cellspacing="0" cellpadding="0">';
		tbCap += '<tr class="KB_center"><td height="42" width="42">1</td><td width="42">2</td><td width="42">3</td>';
		tbCap += '<td width="42">4</td><td width="42">5</td><td width="42">6</td><td width="42">7</td>';
		tbCap += '<td width="42">8</td><td width="42">9</td><td width="42">0</td></tr><tr class="KB_center">';
		tbCap += '<td height="42">Q</td><td>W</td><td>E</td><td>R</td><td>T</td><td>Y</td><td>U</td><td>I</td>';
		tbCap += '<td>O</td><td>P</td></tr><tr class="KB_center"><td height="42">A</td><td>S</td><td>D</td>';
		tbCap += '<td>F</td><td>G</td><td>H</td><td>J</td><td>K</td><td>L</td><td><img src=FOCUSKEY/email.png /></td></tr>';
		tbCap += '<tr class="KB_center"><td height="42"><img src=FOCUSKEY/tab.png /></td><td height="42">Z</td><td>X</td><td>C</td><td>V</td><td>B</td><td>N</td>'
		tbCap += '<td>M</td><td>.</td><td><img src=FOCUSKEY/backspace.png /></td></tr><tr class="KB_center">';
		tbCap += '<td height="42" colspan="6">空 格</td><td colspan="2">重置</td><td colspan="2">确定</td>'
		tbCap += '</tr></table>';
		document.getElementById("KB_keyboard_div").innerHTML = tbCap;
    }, 
    keys: new Array(new Array("1", "2", "3","4","5","6","7","8","9","0"),new Array("q", "w", "e","r","t","y","u","i","o","p"),new Array("a", "s", "d","f","g","h","j","k","l","@"),new Array("KB.shift()","z", "x", "c","v","b","n","m",".","KB.back()"),new Array(" ","KB.reset()","KB.exit()")),
    keysCap: new Array(new Array("1", "2", "3","4","5","6","7","8","9","0"),new Array("Q", "W", "E","R","T","Y","U","I","O","P"),new Array("A", "S", "D","F","G","H","J","K","L","@"),new Array("KB.shift()","Z", "X", "C","V","B","N","M",".","KB.back()"),new Array(" ","KB.reset()","KB.exit()")),
	up: function() {
		KB.y = KB.y > 1 ? KB.y-1:KB.max_y;
		if(KB.y == 5) {
			if(KB.x<=6) {
				KB.x = 1;	
			} else if(KB.x<=8){
				KB.x = 2;	
			} else {
				KB.x = 3;	
			}
		} else if(KB.y == 4) {
			if(KB.x == 2) {
				KB.x = KB.max_x() - 3;
			} else if(KB.x == 3) {
				KB.x = KB.max_x()-1;
			}
		}
		KB.FOCUSIn();
	},
    down: function() {
		KB.y=KB.y<KB.max_y?KB.y+1:1;
		if(KB.y == 5) {
			if(KB.x<=6) {
				KB.x = 1;	
			} else if(KB.x<=8) {
				KB.x = 2;	
			} else {
				KB.x = 3;	
			}
		} else if(KB.y == 1) {
			if(KB.x == 3) {
				KB.x = KB.max_x() - 1;
			} else if(KB.x == 2) {
				KB.x = KB.max_x() - 3;
			}
		} else {	
			KB.x=KB.x>KB.max_x()?KB.max_x():KB.x;
		}
		KB.FOCUSIn();
	},
    left: function() {
		KB.x=KB.x>1?KB.x-1:KB.max_x();
		KB.FOCUSIn();
	},
    right: function() {
		KB.x=KB.x<KB.max_x()?KB.x+1:1;
		KB.FOCUSIn();
	},
    FOCUSIn: function() {
		KB.FOCUS1.style.top = KB.topPos + KB.offsetY * (KB.y - 1)+"px";
		KB.FOCUS1.style.left = KB.leftPos + KB.offsetX * (KB.x - 1)+"px";
		/*if(KB.x == 9 && KB.y == 4) {
			KB.FOCUS1.style.width  = KB.width * 2;
		} else */if(KB.y == 5 && KB.x == 1) {
			KB.FOCUS1.style.width  = KB.width * 6+"px";
		} else if(KB.y == 5 && KB.x == 2) {
			KB.FOCUS1.style.left = KB.leftPos + KB.offsetX * 6+"px";
			KB.FOCUS1.style.width  = KB.width * 2+"px";
		} else if(KB.y == 5 && KB.x == 3) {
			KB.FOCUS1.style.left = KB.leftPos + KB.offsetX * 8+"px";
			KB.FOCUS1.style.width  = KB.width * 2+"px";
		}
		else {
			KB.FOCUS1.style.width  = KB.width+"px";
		}
	},
    select: function() {
		KB.click();
		var key = KB.keys[KB.y - 1][KB.x - 1];
		var keysCap = KB.keysCap[KB.y - 1][KB.x - 1];
		if(key.indexOf("KB.") == 0) {
			eval(key)
		} else {
			if(KB.input.value.length < 16) {	
				if(KB.isCap) KB.input.value += keysCap;
				else KB.input.value += key;
			}
		}
	},
    back: function() {
		var text = KB.input.value;
    	if(text.length <= 1) {
    		KB.input.value=" ";
    	} else {
			KB.input.value = text.substr(0, text.length - 1);
    	}
	},
    shift: function() {
		if(KB.isCap) KB.tbInitLow()
		else KB.tbInitCap()
		KB.FOCUSIn();
		KB.isCap = KB.isCap ? false : true;
	},
    hide: function() {
		KB.keyBoard.style.display="none";
		KB.isOpen = false;
	},
	exit:  function() {
		KB.hide();
	},
	reset:  function() {
    	KB.input.value="";
	},
	getValue: function() {
		KB.hide();
		return KB.input.value;
	},
	click: function() {
    	KB.FOCUS1.style.backgroundImage="url()";
		var click_timer=setTimeout("KB.clickShow()", 30);
	},
	clickShow : function() {
		KB.FOCUS1.style.backgroundImage="url(FOCUSKEY/KB_FOCUS.png)";
	}
};

document.onkeydown = grabEvent;

function grabEvent(event){
	//alert(event.keyCode);
	switch(event.keyCode) {
		case 38:
		//case 1://up
			if(!KB.isOpen) BBCV_BROWSER.FOCUS.keyMove(-2);
			else KB.up();
			break;
		case 40:
		//case 2://down
			if(!KB.isOpen) BBCV_BROWSER.FOCUS.keyMove(2);
			else KB.down();
			break;
			
		case 37:
		//case 3://left
			if(!KB.isOpen) BBCV_BROWSER.FOCUS.keyMove(-1);
			else KB.left();
			break;
			
		case 39:
		//case 4://right
			if(!KB.isOpen) BBCV_BROWSER.FOCUS.keyMove(1);
			else KB.right();
			break;
			
		case 13://select
			if(!KB.isOpen) BBCV_BROWSER.FOCUS.keyClick();
			else KB.select();
			break;
		case 45: //back
		case 46: //exit
			break;
	}
}
var BBCV_ACCESS = {
	VOD: {
		play : function(spId,URL,mediatype,otherInfo) {
			var aimPath = localStorage.AIMPATH;
			var sessionId = localStorage.SESSIONID;
			if(aimPath!=null&&sessionId!=null&&URL!=null&&mediatype!=null){
				var xhr = new XMLHttpRequest();
				xhr.open("GET", aimPath+"direct?spId="+spId+"&sessionId="+sessionId+"&queryType="+mediatype+"&messageInfo="+otherInfo, true);
				xhr.onreadystatechange = function() {
				  if (xhr.readyState == 4) {
				  	if(xhr.responseText=="OK!")
				    	console.log("request play ok");
				  }
				}
				xhr.send();	
			}	
		}
	},
	GAME: {
		play : function(spId, otherInfo){
			var aimPath = localStorage.AIMPATH;
			var sessionId = localStorage.SESSIONID;
			if(aimPath!=null&&sessionId!=null&&URL!=null&&mediatype!=null){
				var xhr = new XMLHttpRequest();
				xhr.open("GET", aimPath+"direct?spId="+spId+"&sessionId="+sessionId+"&messageInfo="+otherInfo, true);
				xhr.onreadystatechange = function() {
				  if (xhr.readyState == 4) {
				  	if(xhr.responseText=="OK!")
				    	console.log("request play ok");
				  }
				}
				xhr.send();	
			}		
		}
	},
	PLATFORM: {
		logout : function(){
			var aimPath = localStorage.AIMPATH;
			var sessionId = localStorage.SESSIONID;
			if(aimPath!=null&&sessionId!=null&&URL!=null&&mediatype!=null){
				var xhr = new XMLHttpRequest();
				xhr.open("GET", aimPath+"logout?sessionId="+sessionId, true);
				xhr.onreadystatechange = function() {
				  if (xhr.readyState == 4) {
				  	if(xhr.responseText=="OK!")
				    	console.log("request play ok");
				  }
				}
				xhr.send();	
			}		
		}
	}
}
BBCV_BROWSER.FOCUS.init();
}catch(e){
}