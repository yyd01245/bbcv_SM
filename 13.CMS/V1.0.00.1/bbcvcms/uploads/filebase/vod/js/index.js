
function kysxplay(codeType,codeId,otherInfo)
{
    var infoJson = "codeType:"+codeType+","+"codeId:"+codeId+","+"otherInfo:"+otherInfo;
    
    window.location = "/getInfo/"+infoJson;
}


function showInfo(info)
{
    $("p#").html(info);
}
