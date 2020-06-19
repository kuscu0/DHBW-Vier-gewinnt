function removeAllChildNodes(parentNode) {
    while (parentNode.firstchild) {
        parentNode.removeChild(parentNode.firstChild);
    }
}

function createTable(tablaData, parentNode) {
    var table = document.createElement("table");
    
    var tRow = document.createElement("tr");
    for (i = 0; i < 7; i++) {
        var tField = document.createElement("th")
        var form = document.createElement("form");
        form.action = "play";
        form.method = "post";
        var btn = document.createElement("button")
        btn.className = ("arrow-down");
        btn.id = ("btn_spalte_" + i)
        btn.name = ("insertBtn")
        btn.value = i
        form.appendChild(btn);
        tField.appendChild(form);
        tRow.appendChild(tField)
    }
    table.appendChild(tRow);
    var img;
    for (i = 0; i < 6; i++) {
        tRow = document.createElement("tr");

        for (j = 0; j < 7; j++) {
            tField = document.createElement("td")
            img = document.createElement("img")
            img.setAttribute("src", getSource(tablaData[i][j]))
            img.setAttribute("style", "margin: 10px;");
            tField.appendChild(img);
            tRow.appendChild(tField);
        }
        table.appendChild(tRow);
    }


    parentNode.appendChild(table);
    parentNode.setAttribute("style", "position: fixed;" +
    								 "margin-top: 10%; " +
    								 "left: 50%; " +
    								 "margin-left: -15%; " +
    								 "display: block;");
    
    table.setAttribute("style", "margin: 0 auto;" +
    							"background-color: #0000FF;" +
								"border-radius: 20px;");
}

function removeButton(id) {
    var btn = document.getElementById(id);
    btn.remove();
}

function removeButtons() {
    for (i = 0; i < 7; i++) {
        var btn = document.getElementById("btn_spalte_" + i);
        btn.setAttribute("style", "border-top: 20px solid gray; cursor: not-allowed;");
        btn.disabled = true;
    }
}

function getSource(index) {
    if (index == 1) {
        return "res/red.png";
    } else if (index == 2) {
        return "res/yellow.png";
    } else return "res/gray.png"
}



removeAllChildNodes(document.getElementById("gameCanvas"));
var emptyTable = [
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0]
];