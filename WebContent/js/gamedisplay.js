function removeAllChildNodes(parentNode){
	while(parentNode.firstchild){
		parentNode.removeChild(parentNode.firstChild);
	}
}
function createTable(tablaData , parentNode ){
	var table = document.createElement("table");
	table.setAttribute("style", "background-color: #0000FF;")
	var tRow = document.createElement("tr");
	for (i=0; i<7;i++){
		var tField = document.createElement("th")
		var btn = document.createElement("button")
		btn.innerHTML = "Spalte #" + (i+1);
		btn.id = ("btn_spalte_" + i)
		tField.appendChild(btn);
		tRow.appendChild(tField)
	}
	table.appendChild(tRow);
	var img;
	for(i=0;i<6;i++){
		tRow = document.createElement("tr");
		
		for(j=0;j<7;j++){
			tField = document.createElement("td")
			img = document.createElement("img")
			img.setAttribute("src",getSource(tablaData[i][j]))
			tField.appendChild(img);
			tRow.appendChild(tField);
		}
		table.appendChild(tRow);
	}
	
	
	parentNode.appendChild(table);
}



function getSource(index){
	if (index == 1){
		return "../res/yellow.png";
	}else if(index == 2){
		return "../res/red.png";
	}
	else return "../res/gray.png"
}



removeAllChildNodes(document.getElementById("gameContainer"));
