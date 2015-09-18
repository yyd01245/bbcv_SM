
function kysxplay(rtsp)
{
    window.location = "/getCustomInfo/"+rtsp;
}


function kysxplay(rtsp,vodname,posterurl)
{
    //window.location = "getCustomInfo"+rtsp+vodname+posterurl;
    
    alert("before window.location set");
    window.location = "/getCustomInfo/"+rtsp+vodname+posterurl;
    alert("after window.location set");
}

