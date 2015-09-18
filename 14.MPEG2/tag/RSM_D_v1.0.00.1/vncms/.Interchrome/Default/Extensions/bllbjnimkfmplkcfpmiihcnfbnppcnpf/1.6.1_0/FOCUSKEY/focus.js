var BROWSERFOCUS = {
	getX : function(obj){   
		return obj.offsetLeft + (obj.offsetParent ? BROWSERFOCUS.getX(obj.offsetParent) : obj.x ? obj.x : 0);   
	},         
	getY : function(obj){   
		return (obj.offsetParent ? obj.offsetTop + BROWSERFOCUS.getY(obj.offsetParent) : obj.y ? obj.y : 0);   
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
							left = BROWSERFOCUS.getX(imgs[i]) + pos[0]+"px";  
							top = BROWSERFOCUS.getY(imgs[i]) + pos[1]+"px";  
							width = pos[2]+"px";  
							height = pos[3]+"px";  
						}
						areas[j].parentNode.appendChild(areaA);
				}
			}
		};
	   
	},
	init : function() {
		BROWSERFOCUS.onloadImages()
		BROWSERFOCUS.aLink  = document.getElementsByTagName("a"); //所有A标签对象
		for(var i =0;i<BROWSERFOCUS.aLink.length;i++) {
			var aPosi = BROWSERFOCUS.getRealStyle(BROWSERFOCUS.aLink[i],"position");
			if(aPosi!="absolute") BROWSERFOCUS.aLink[i].style.position = "relative"
			BROWSERFOCUS.aLink[i].style.display = "inline-block"
		}
		BROWSERFOCUS.inputLink  = document.getElementsByTagName("input"); //所有INPUT标签对象
		for(var j =0;j<BROWSERFOCUS.inputLink.length;j++) {
			var inputPosi = BROWSERFOCUS.getRealStyle(BROWSERFOCUS.inputLink[j],"position");
			if(inputPosi!="absolute") BROWSERFOCUS.inputLink[j].style.position = "relative";			
		}
		BROWSERFOCUS.selectLink  = document.getElementsByTagName("select"); //所有SELECT标签对象
		for(var m =0;m<BROWSERFOCUS.selectLink.length;m++) {
			var selectPosi = BROWSERFOCUS.getRealStyle(BROWSERFOCUS.selectLink[m],"position");
			if(selectPosi!="absolute") BROWSERFOCUS.selectLink[m].style.position = "relative";	
		}
		
		BROWSERFOCUS.aLinkCss =[];
		
		BROWSERFOCUS.linkFocus = 0;
		BROWSERFOCUS.cssInit();
		BROWSERFOCUS.showFocus();
		
		BROWSERFOCUS.moveFocus(BROWSERFOCUS.linkFocus);
		//alert(BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][2])
  
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
		for(var i = 0;i<BROWSERFOCUS.aLink.length; i++) {
			var parent = BROWSERFOCUS.aLink[i];
			
			var left = BROWSERFOCUS.aLink[i].offsetLeft;
			var top = BROWSERFOCUS.aLink[i].offsetTop;
			while(parent.nodeName!="BODY") {
				parent = parent.parentNode;
				if(BROWSERFOCUS.getRealStyle(parent,"position")=="absolute" || BROWSERFOCUS.getRealStyle(parent,"position")=="relative") {
					left+=parent.offsetLeft;
					top+=parent.offsetTop;
				}
			}
			BROWSERFOCUS.aLinkCss[i] = new Array();
			BROWSERFOCUS.aLinkCss[i][0] = i;
			BROWSERFOCUS.aLinkCss[i][1] = BROWSERFOCUS.getX(BROWSERFOCUS.aLink[i]);
			BROWSERFOCUS.aLinkCss[i][2] = BROWSERFOCUS.getY(BROWSERFOCUS.aLink[i]);
			BROWSERFOCUS.aLinkCss[i][3] = BROWSERFOCUS.aLink[i].offsetWidth;
			BROWSERFOCUS.aLinkCss[i][4] = BROWSERFOCUS.aLink[i].offsetHeight;
			BROWSERFOCUS.aLinkCss[i][5] = "a";
			BROWSERFOCUS.aLinkCss[i][6] = i;
		}
		for(var j=0;j<BROWSERFOCUS.inputLink.length;j++) {
			var parentInput = BROWSERFOCUS.inputLink[j];
			
			var leftInput = BROWSERFOCUS.inputLink[j].offsetLeft;
			var topInput = BROWSERFOCUS.inputLink[j].offsetTop;
			while(parentInput.nodeName!="BODY") {
				parentInput = parentInput.parentNode;
				if(BROWSERFOCUS.getRealStyle(parentInput,"position")=="absolute" || BROWSERFOCUS.getRealStyle(parentInput,"position")=="relative") {
					leftInput+=parentInput.offsetLeft;
					topInput+=parentInput.offsetTop;
				}
			}
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+j] = new Array();
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+j][0] = BROWSERFOCUS.aLink.length+j;
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+j][1] = leftInput;
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+j][2] = topInput;
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+j][3] = BROWSERFOCUS.inputLink[j].offsetWidth;
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+j][4] = BROWSERFOCUS.inputLink[j].offsetHeight;
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+j][5] = BROWSERFOCUS.inputLink[j].type;
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+j][6] = j;
		}
		for(var m=0;m<BROWSERFOCUS.selectLink.length;m++) {
			var parentSelect = BROWSERFOCUS.selectLink[m];
			
			var leftSelect = BROWSERFOCUS.selectLink[m].offsetLeft;
			var topSelect = BROWSERFOCUS.selectLink[m].offsetTop;
			while(parentSelect.nodeName!="BODY") {
				parentSelect = parentSelect.parentNode;
				if(BROWSERFOCUS.getRealStyle(parentSelect,"position")=="absolute" || BROWSERFOCUS.getRealStyle(parentSelect,"position")=="relative") {
					leftSelect+=parentSelect.offsetLeft;
					topSelect+=parentSelect.offsetTop;
				}
			}
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+BROWSERFOCUS.inputLink.length+m] = new Array();
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+BROWSERFOCUS.inputLink.length+m][0] = BROWSERFOCUS.aLink.length+BROWSERFOCUS.inputLink.length+m;
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+BROWSERFOCUS.inputLink.length+m][1] = leftSelect;
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+BROWSERFOCUS.inputLink.length+m][2] = topSelect;
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+BROWSERFOCUS.inputLink.length+m][3] = BROWSERFOCUS.selectLink[m].offsetWidth;
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+BROWSERFOCUS.inputLink.length+m][4] = BROWSERFOCUS.selectLink[m].offsetHeight;
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+BROWSERFOCUS.inputLink.length+m][5] = "select";
			BROWSERFOCUS.aLinkCss[BROWSERFOCUS.aLink.length+BROWSERFOCUS.inputLink.length+m][6] = m;
		}
			var minLeft = BROWSERFOCUS.aLinkCss[0][1];
			var minTop = BROWSERFOCUS.aLinkCss[0][2];
		for(var n=0;n<BROWSERFOCUS.aLinkCss.length;n++) {
			if(BROWSERFOCUS.aLinkCss[n][3]==0 || BROWSERFOCUS.aLinkCss[n][4]==0) continue;
			if(BROWSERFOCUS.aLinkCss[n][2]==minTop) {
				if(BROWSERFOCUS.aLinkCss[n][1]<minLeft) {
					minLeft = BROWSERFOCUS.aLinkCss[n][1]
					BROWSERFOCUS.linkFocus = n;
					continue;
				}
			}
			if(BROWSERFOCUS.aLinkCss[n][2]<minTop) {
				minTop = BROWSERFOCUS.aLinkCss[n][2]
				BROWSERFOCUS.linkFocus = n;
			}
		}
	},
	showFocus : function() {
		var elment = document.createElement("div");
		elment.id = "browserbbcvisionTvFocus";
		document.body.appendChild(elment);
		document.getElementById("browserbbcvisionTvFocus").style.border = "2px solid red";
		document.getElementById("browserbbcvisionTvFocus").style.position = "absolute";
		document.getElementById("browserbbcvisionTvFocus").style.zIndex = 100000;
		
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
	moveFocus : function(index) {
		document.getElementById("browserbbcvisionTvFocus").style.left = BROWSERFOCUS.aLinkCss[index][1]-2+"px";
		document.getElementById("browserbbcvisionTvFocus").style.top = BROWSERFOCUS.aLinkCss[index][2]-2+"px";
		document.getElementById("browserbbcvisionTvFocus").style.width = BROWSERFOCUS.aLinkCss[index][3]+"px";
		document.getElementById("browserbbcvisionTvFocus").style.height = BROWSERFOCUS.aLinkCss[index][4]+"px";
	},
	keyMove : function(step) {
		var linkFocusLeft = BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][1];
		var linkFocusTop = BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][2];
		var minNum;
		var newArray = [];
		var newSame = false;
		var newSameArray = [];
		for(var i=0;i<BROWSERFOCUS.aLinkCss.length;i++) {
			var tempLinkFocusLeft = BROWSERFOCUS.aLinkCss[i][1];
			var tempLinkFocusTop = BROWSERFOCUS.aLinkCss[i][2];
			
			if(step==-2) {if(tempLinkFocusTop<linkFocusTop) newArray.push(BROWSERFOCUS.aLinkCss[i]);}
			else if(step==2) {if(tempLinkFocusTop>linkFocusTop) newArray.push(BROWSERFOCUS.aLinkCss[i])}
			else if(step==-1) {if(tempLinkFocusLeft<linkFocusLeft) newArray.push(BROWSERFOCUS.aLinkCss[i])}
			else if(step==1) {if(tempLinkFocusLeft>linkFocusLeft) newArray.push(BROWSERFOCUS.aLinkCss[i])}
		}
		
		for(var j=0;j<newArray.length;j++) {
			
			var c =  (linkFocusLeft-newArray[j][1])*(linkFocusLeft-newArray[j][1]) + (linkFocusTop-newArray[j][2])*(linkFocusTop-newArray[j][2]); //求两点间距离
			if(step==-2) {
				if(Math.abs(linkFocusLeft - newArray[j][1])<=10) {
					newSameArray.push(newArray[j])
					newSame = true;
				}
				else if(typeof(minNum) =="undefined" || c<=minNum) {
					var index = parseInt(newArray[j][0]);
					if(BROWSERFOCUS.aLinkCss[index][3]==0 || BROWSERFOCUS.aLinkCss[index][4]==0) continue;
					minNum = c
					BROWSERFOCUS.linkFocus = index;
				}
			}
			else if(step==2) {
				if(Math.abs(linkFocusLeft - newArray[j][1])<=10) {
					newSameArray.push(newArray[j])
					newSame = true;
				}
				else if(typeof(minNum) =="undefined" || c<minNum) {
					var index = parseInt(newArray[j][0]);
					if(BROWSERFOCUS.aLinkCss[index][3]==0 || BROWSERFOCUS.aLinkCss[index][4]==0) continue;
					minNum = c
					BROWSERFOCUS.linkFocus = index;
				}
			}
			else if(step==-1) {
				if(Math.abs(linkFocusTop - newArray[j][2])<=10) {
					newSameArray.push(newArray[j])
					newSame = true;
				}
				else if(typeof(minNum) =="undefined" || c<=minNum) {
					var index = parseInt(newArray[j][0]);
					if(BROWSERFOCUS.aLinkCss[index][3]==0 || BROWSERFOCUS.aLinkCss[index][4]==0) continue;
					minNum = c;
					BROWSERFOCUS.linkFocus = index;
				}
			}
			else if(step==1) {
				if(Math.abs(linkFocusTop - newArray[j][2])<=10) {
					newSameArray.push(newArray[j])
					newSame = true;
				}
				else if(typeof(minNum) =="undefined" || c<minNum) {
					var index = parseInt(newArray[j][0]);
					if(BROWSERFOCUS.aLinkCss[index][3]==0 || BROWSERFOCUS.aLinkCss[index][4]==0) continue;
					minNum = c
					BROWSERFOCUS.linkFocus = index;
				}
			}
			
			
		}
		if(newSame) {
			for(var m=0;m<newSameArray.length;m++) {
				
				if(step==-2 || step==2) {var mc = Math.abs(newSameArray[m][2] - linkFocusTop);}
				else if(step==-1 || step==1) {var mc = Math.abs(newSameArray[m][1] - linkFocusLeft);}
			
				
				if(typeof(minNum) =="undefined" || mc<minNum) {
					var index = parseInt(newSameArray[m][0]);
					if(BROWSERFOCUS.aLinkCss[index][3]==0 || BROWSERFOCUS.aLinkCss[index][4]==0) continue;
					minNum = mc
					BROWSERFOCUS.linkFocus = index;
				}
			}
		}
		BROWSERFOCUS.moveFocus(BROWSERFOCUS.linkFocus);
	},
	keyClick : function() {
		var linkType = BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][5];
		
		if(linkType=="a") {
			document.location.href = BROWSERFOCUS.aLink[BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][6]].href;
		}
		else if(linkType=="select") {//alert("下拉")
			var selectFocus = BROWSERFOCUS.selectLink[BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][6]];
			var index = selectFocus.selectedIndex;
			index++;
			if(index==selectFocus.length) index = 0;
			selectFocus[index].selected = true;
		}
		else if(linkType=="text" || linkType=="password") {//alert("输入框")
			var inputValue = BROWSERFOCUS.inputLink[BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][6]].value;
			KB.init();
			KB.show(inputValue);
		}
		else if(linkType=="radio") {//alert("单选框")
			BROWSERFOCUS.inputLink[BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][6]].checked = true;
		}
		else if(linkType=="checkbox") {//alert("多选框")
			BROWSERFOCUS.inputLink[BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][6]].checked = BROWSERFOCUS.inputLink[BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][6]].checked?false:true;
			
		}
		else if(linkType=="button" || linkType=="submit") {//alert("按钮")
			BROWSERFOCUS.inputLink[BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][6]].click()
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
		KB.input = BROWSERFOCUS.inputLink[BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][6]]
		KB.focus1 = document.getElementById("KB_focus");
    },
	// 软键盘方法定义
    show: function(data) {
		KB.keyBoard.style.left = BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][1]+"px";
		KB.keyBoard.style.top = BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][2]+BROWSERFOCUS.aLinkCss[BROWSERFOCUS.linkFocus][4]+"px";
		KB.isOpen = true;
		KB.keyBoard.style.display="block";
		if(typeof(data) == "undefined" || data == "") {
			KB.input.value="";
		} else {
			KB.input.value=data.trim();;
		}
		KB.focus1.style.width  = KB.width+"px";
		KB.focusIn();
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
		tb += '</tr></table></div><div id="KB_focus"></div></td></tr></table>';
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
		KB.focusIn();
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
		KB.focusIn();
	},
    left: function() {
		KB.x=KB.x>1?KB.x-1:KB.max_x();
		KB.focusIn();
	},
    right: function() {
		KB.x=KB.x<KB.max_x()?KB.x+1:1;
		KB.focusIn();
	},
    focusIn: function() {
		KB.focus1.style.top = KB.topPos + KB.offsetY * (KB.y - 1)+"px";
		KB.focus1.style.left = KB.leftPos + KB.offsetX * (KB.x - 1)+"px";
		/*if(KB.x == 9 && KB.y == 4) {
			KB.focus1.style.width  = KB.width * 2;
		} else */if(KB.y == 5 && KB.x == 1) {
			KB.focus1.style.width  = KB.width * 6+"px";
		} else if(KB.y == 5 && KB.x == 2) {
			KB.focus1.style.left = KB.leftPos + KB.offsetX * 6+"px";
			KB.focus1.style.width  = KB.width * 2+"px";
		} else if(KB.y == 5 && KB.x == 3) {
			KB.focus1.style.left = KB.leftPos + KB.offsetX * 8+"px";
			KB.focus1.style.width  = KB.width * 2+"px";
		}
		else {
			KB.focus1.style.width  = KB.width+"px";
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
		KB.focusIn();
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
    	KB.focus1.style.backgroundImage="url()";
		var click_timer=setTimeout("KB.clickShow()", 30);
	},
	clickShow : function() {
		KB.focus1.style.backgroundImage="url(FOCUSKEY/KB_focus.png)";
	}
};

document.onkeydown = grabEvent;

function grabEvent(event){
	//alert(event.keyCode);
	switch(event.keyCode) {
		case 38:
		//case 1://up
			if(!KB.isOpen) BROWSERFOCUS.keyMove(-2);
			else KB.up();
			break;
		case 40:
		//case 2://down
			if(!KB.isOpen) BROWSERFOCUS.keyMove(2);
			else KB.down();
			break;
			
		case 37:
		//case 3://left
			if(!KB.isOpen) BROWSERFOCUS.keyMove(-1);
			else KB.left();
			break;
			
		case 39:
		//case 4://right
			if(!KB.isOpen) BROWSERFOCUS.keyMove(1);
			else KB.right();
			break;
			
		case 13://select
			if(!KB.isOpen) BROWSERFOCUS.keyClick();
			else KB.select();
			break;
		case 45: //back
		case 46: //exit
			break;
	}
}
BROWSERFOCUS.addLoadEvent(BROWSERFOCUS.init)
