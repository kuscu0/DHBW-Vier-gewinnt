function onSign(x, onNegative, onPositive) {
	if (x < 0) {
		onNegative();
		
	} else {
		onPositive();
	}
}

function printSign(x) {
	onNegative = () => {
		console.log("negative");
	};
	
	onPositive = () => {
		console.log("positive");
	};
	
	onSign(x, onNegative, onPositive);
};

function removeAllChildNodes(parentNode) {
	while (parentNode.firstChild) {
		parentNode.removeChild(parentNode.firstChild);
	}
};

function createTable(tableData, parentNode) {
	abstractCreateTable(tableData, parentNode, "play", null);
};

function abstractCreateTable(tablaData, parentNode, action, onclick) {
	var table = document.createElement("table");

	var tRow = document.createElement("tr");
	for (i = 0; i < 7; i++) {
		var tField = document.createElement("th");
		var form = document.createElement("form");
		form.action = action;
		form.method = "post";
		var btn = document.createElement("button");
		const btnid = i;
		btn.onclick = () => onclick(btnid);
		btn.className = ("arrow-down");
		btn.id = ("btn_spalte_" + i)
		btn.name = ("insertBtn");
		btn.value = i;
		form.appendChild(btn);
		if (tablaData[0][i] != 0) {
			disableButton(btn);
		};
		tField.appendChild(form);
		tRow.appendChild(tField);
	};
	table.appendChild(tRow);
	var img;
	for (var i = 0; i < 6; i++) {
		tRow = document.createElement("tr");

		for (var j = 0; j < 7; j++) {
			tField = document.createElement("td");
			tField.setAttribute("id", String(i) + "," + String(j));
			img = document.createElement("img");
			img.setAttribute("src", getSource(tablaData[i][j]));
			img.setAttribute("style", "margin: 10px;");
			tField.appendChild(img);
			tRow.appendChild(tField);
		};
		table.appendChild(tRow);

	};


	parentNode.appendChild(table);
	parentNode.setAttribute("style", "position: fixed;" +
		"margin-top: 10%; " +
		"left: 50%; " +
		"margin-left: -15%; " +
		"display: block;");

	table.setAttribute("style", "margin: 0 auto;" +
		"background-color: #0000FF;" +
		"border-radius: 20px;");

	var cord = getChanged(tablaData);
	if (typeof cord !== 'undefined') {
		var cordStart = {
			xCord: cord.xCord,
			yCord: 0,
			value: cord.value,
		};
		var cordTmp = {
			xCord: cord.xCord,
			yCord: cord.yCord,
			value: 0,
		}
		setChip(cordTmp);
		animateFall(cord, cordStart)
	}
};

function setChip(cord) {
	var element = document.getElementById(String(cord.yCord) + "," + String(cord.xCord));
	if (element !== null) {
		removeAllChildNodes(element);
		var img = document.createElement("img");
		img.setAttribute("src", getSource(cord.value));
		img.setAttribute("style", "margin: 10px;");
		element.appendChild(img);
	}
};

function animateFall(cordEnd, cordCurrent) {
	var cord = {
		xCord: cordCurrent.xCord,
		yCord: cordCurrent.yCord - 1,
		value: 0
	};
	setChip(cord);
	setChip(cordCurrent);

	if (cordEnd.yCord != cordCurrent.yCord) {
		cordCurrent.yCord = (cordCurrent.yCord + 1);
		setTimeout(function() { animateFall(cordEnd, cordCurrent); }, 300);
	}
};

function getChanged(tableData) {
	for (var i = 0; i < 6; i++) {
		for (var j = 0; j < 7; j++) {
			if (tableData[i][j] > 2) {
				var cord = {
					xCord: j,
					yCord: i,
					value: (tableData[i][j] - 2)
				};
				return cord;
			}
		}
	}
};

function removeButton(id) {
	var btn = document.getElementById(id);
	btn.remove();
};

function disableButton(element) {
	element.setAttribute("style", "border-top: 20px solid gray;");
	element.disabled = true;
};

function removeButtons() {
	for (i = 0; i < 7; i++) {
		disableButton(document.getElementById("btn_spalte_" + i));
	}
};

function getSource(index) {
	if (index == 1) {
		return "res/red.png";
	} else if (index == 2) {
		return "res/yellow.png";
	} else return "res/gray.png"
};



removeAllChildNodes(document.getElementById("gameCanvas"));
var emptyTable = [
	[0, 0, 0, 0, 0, 0, 0],
	[0, 0, 0, 0, 0, 0, 0],
	[0, 0, 0, 0, 0, 0, 0],
	[0, 0, 0, 0, 0, 0, 0],
	[0, 0, 0, 0, 0, 0, 0],
	[0, 0, 0, 0, 0, 0, 0]
];