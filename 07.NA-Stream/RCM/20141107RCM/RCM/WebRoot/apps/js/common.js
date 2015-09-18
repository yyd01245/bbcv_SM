$(function(){
	$("#detail>tr").hover(
		function() {$(this).find('td').attr('class','tab_over');},
		function() {$(this).find('td').attr('class','table_1');}
	); 
});

//div的显示与隐藏
function show(id){
	eDiv = document.getElementById(id);
	if (eDiv.style.display == 'block'){ eDiv.style.display = "none";}
	else {eDiv.style.display = "block";}
}

function jq_show(id){
	$(id).slideToggle('normal');
}


//返回
function returnBack(){
	window.history.back();
}

function detail_list_hover(obj,clsName){
	var tds = obj.children;
	for(var i=0;i<tds.length;i++){
		tds[i].className = clsName;
	}
}

//判断是否是数字
function f_check_number(obj){          
   if (/^\d+$/.test(obj)){   
		return true;   
	}    
	else{   		      
		return false;   
	}   
} 

//判断残疾证号格式是否正确
function f_check_cardnum(obj){          
   if (/^\d+[X]+\d+$/.test(obj)){  
		return true;   
	}    
	else{   		      
		return false;   
	}   
} 

//判断邮箱格式是否正确
function f_check_email(obj){    
   var CheckMail=/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
   if (CheckMail.test(obj)){   
		return true;   
	}    
	else{   		      
		return false;   
	}   
} 

//验证电话号码(返回 true:false)
function checkPhone(phone)    
{    
	if (phone != ""){     
	  var p1 = /^\d+[-]?\d+$/;    
	  var me = false;    
	  if (p1.test(phone)) {me=true; }   
	  if (!me){       
	    //alert('对不起，您输入的电话号码有错误。区号和电话号码之间请用-分割');      
	   return false;    
	  }    
	}   
    return true;    
}    

//验证手机号码，包含153，159号段  
function checkMobile(mobile)    
{      
	if (mobile != ""){       
		var reg0 = /^13\d{5,9}$/;    
		var reg1 = /^153\d{4,8}$/;    
		var reg2 = /^159\d{4,8}$/;    
		var reg3 = /^0\d{10,11}$/;  
		var reg4 = /^150\d{4,8}$/; 
		var reg5 = /^158\d{4,8}$/; 
		var reg6 = /^15\d{5,9}$/;  
		var my = false;    
		if (reg0.test(mobile))my=true;    
		if (reg1.test(mobile))my=true;    
		if (reg2.test(mobile))my=true;    
		if (reg3.test(mobile))my=true;  
		if (reg4.test(mobile))my=true;    
		if (reg5.test(mobile))my=true;    
		if (reg6.test(mobile))my=true;    
		if (!my){  
		   //alert('对不起，您输入的手机或小灵通号码有错误。');      
		   return false;    
		}    
	   return true;    
	}    
}  


function checkPhoneAndMobile(phone)    
{     
	if (checkMobile(phone)||checkPhone(phone)){  
	   return true;    
	} 
    return false;   
}    

/*

 *判别身份证号码是否合法,入口参数为身份证号码

 *返回 true:false

 *Junsan Jin 20050902

 */
function checkId(varInput){
       if(varInput==null || varInput.trim()==""){
              //alert("身份证号不能为空，请重新输入！");
              return false;
       }

       varInput = varInput.trim();
       if(varInput.length!=18 && varInput.length!=15){
       	      //alert("身份证号位数不对，请重新输入！");
       	      return false;
       }
       var ret = convertID(varInput);
       if(ret == false){
              //alert("身份证号不正确，请重新输入！");
              return false;
       }
       else if(varInput.length==18 && varInput!=ret){
              //alert("身份证号不正确，请重新输入！");
              return false;
       }
       return true;
}

/*
 *15身份证号码升18位,入口参数0为15身份证号码,返回值为18位身份证号码
 *如果证号错误则返回false
 *Junsan Jin 20050902
 */
 function convertID(varInput){
    if(varInput==null || varInput.trim()==""){
            return false;
     }
     var strOldID = new String(varInput.trim());
     var strNewID = "";
     if(strOldID.length==15){
         for(i=0; i<15; i++){
              //15位的身份证号必须全部由数字组成，否则，视为非法
               if(checkZInt(strOldID.substring(i,1))){
                      return false;
               }
          }
          //取得身份证中的年月日
          var year = "19" + strOldID.substr(6,2);
          // alert(year);
          var month = strOldID.substr(8,2);
          //alert(month);
           var day = strOldID.substr(10,2);
          //alert(day);
          
          //校验日期是否正确
            if(checkDate(year,month,day)){             
                   return false;
            }          
            strNewID = strOldID.substring(0,6) + "19" + strOldID.substring(6,15);
       }
       else if(strOldID.length==18){
            for(i=0; i<17; i++){
               //15位的身份证号必须全部由数字组成，否则，视为非法
               if(checkZInt(strOldID.substring(i,1))){
                      return false;
               }
              }
              if(strOldID.substring(17,18).toUpperCase!="X" && checkZInt(strOldID.substring(17,18))){
                     return false;
              }
              //取得身份证中的年月日
              var year = "19" + strOldID.substr(6,4);
              var month = strOldID.substr(10,2);
              var day = strOldID.substr(12,2);
              //校验日期是否正确
              if(checkDate(year,month,day)){
                     return false;
              }
              strNewID = strOldID.substring(0,17);
       }
       else if(strOldID.length==17){
              for(i=0; i<17; i++){
                 //15位的身份证号必须全部由数字组成，否则，视为非法
                 if(checkZInt(strOldID.substring(i,1))){
                        return false;
                 }
              }
              //取得身份证中的年月日
              var year = "19" + strOldID.substr(6,4);
              var month = strOldID.substr(10,2);
              var day = strOldID.substr(12,2);
              //校验日期是否正确
              if(checkDate(year,month,day)){
                     return false;
              }
              strNewID = strOldID;
      }
       return strNewID = strNewID + createCK(strNewID);
}
/*

 *根据17位的身份证号得到最后一位校验码

 *strID：身份证号前17位

 *只返回

 */
 function createCK(strID){
        var s = 0;
        var WI = new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1);
        var AI = "10X98765432";
        for(i=0; i<17; i++){
               j = strID.substr(i,1) * WI[i];
               s = s + j;
        }
        s = s % 11;
        return AI.substr(s,1);
 }

/*

 *主要提供对日期的精确校验，验证日期是否合法

 *非法返回true,合法返回false

 *Junsan Jin 20050902 

 *参数说明：

 *year：年

 *month：月

 *day：日

 */
function checkDate(year,month,day){         
       var flag=false;
       var time=new Date(year,month-1,day);
   //    alert(time);
       var e_year=time.getFullYear();
   //    alert(e_year);
       var e_month=time.getMonth()+1;
   //    alert(e_month);
       var e_day=time.getDate();
   //    alert(e_day);
       if(year!=e_year||month!=e_month||day!=e_day)
       {
              flag=true;
       }
       return flag;
}

/*

 *检查输入的串是否在0到9之间的字符组成

 *不是则返回true，如果是则返回false

 *Junsan Jin 20050902

 */
function checkZInt(str){
      var reg = /^\d+$/;
      if(arr=str.match(reg))
      {
       //全部是数字
           return false;
      }
      else
      {
       //含有其他字符
           return false;
      }
}
/*

 *字符串去掉左右空格的方法

 *Junsan Jin 20050902

 */
String.prototype.trim = function(){
       return this.replace(/(^\s*)|(\s*$)/g, "");
}
/*

 *字符串去掉左空格的方法

 *Junsan Jin 20050902

 */
String.prototype.ltrim = function(){
       return this.replace(/(^\s*)/g, "");
}

/*

 *字符串去掉右空格的方法

 *Junsan Jin 20050902

 */
String.prototype.rtrim = function(){
       return this.replace(/(\s*$)/g, "");

}


//判断是否为空或为空格
function testValue(v){
	if (v.length == 0 || textEmpty(v)){
		return true;
	}
	return false;
} 

//判断是否为空格
function textEmpty(v){
	var emptyValue="";
	for(var i=1;i<=v.length;i++){
		emptyValue=emptyValue+" ";
	}
	if(v == emptyValue){
		return true;
	}
	return false;
}
//清空查询条件
function clearAll(){
$(".clearval").val("");
}