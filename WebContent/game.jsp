<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="de">

<head>
	<meta charset="ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<link rel="shortcut icon" href="favicon.ico" />
	<title>Vier gewinnt</title>
</head>
<div style="display: flex; justify-content: space-evenly;" id="gameCanvas"></div>
<button class="backBtn" onclick="location.href = 'index.jsp';">Zum Hauptmenü</button>

<script src="js/display.js"></script>
<div style="color: white;" id="test"></div>


<body>

</body>
<script type="text/javascript">
	const board = sessionStorage.getItem("board");
	console.log(board);


	const clientId = sessionStorage.getItem("clientId");
	const gameId = sessionStorage.getItem("gameId");

	var connection = new WebSocket("ws://localhost:8080/DHBW-Vier-gewinnt/socket");

	connection.onopen = function () {
		console.log("reconnected");
		event.preventDefault();

		createTable(board, document.getElementById("gameCanvas"));

		// abstractCreateTable(board, document.getElementById("gameCanvas"), null, (column) => {
		// 	makeTurn(column);
		// 	console.log("aCCCCCtion" + column);
		// });
	}

	connection.onmessage = function (message) {

	}

	connection.onerror = function (error) {

	}

	connection.onclose = function (event) {

	}

	document.getElementById("test").innerHTML = "<a>" + clientId + "</a><br></br>" +
		"<a>" + gameId + "</a><br></br>" + board;


	/*
	abstractCreateTable(board, document.getElementById("gameCanvas"), null, (column) => {
		//event.preventDefault();
		makeTurn(column);
		console.log("AAAACCTCTOIIONNN" + column);
	}); */
</script>

</html>