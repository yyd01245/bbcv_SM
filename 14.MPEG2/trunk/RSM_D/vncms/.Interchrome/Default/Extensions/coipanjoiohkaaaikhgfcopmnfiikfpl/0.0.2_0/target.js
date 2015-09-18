function changeTarget(){
	var elementsA = document.getElementsByTagName('a');
	var nowHost = location.hostname;
	//alert(fairing(nowHost));
	for(i=0 ; i < elementsA.length ; i++){
		elementsA.item(i).setAttribute("target","_self");
	}
}
changeTarget();