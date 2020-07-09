<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="de">

<head>
	<meta charset="ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<link rel="shortcut icon" href="favicon.ico" />
	<title>Vier gewinnt</title>
</head>
<div id="gameCanvas"></div>
<div id="gameResult"></div>
<button class="backBtn" onclick="location.href = 'index.jsp';">Zum Hauptmenü</button>

<script src="js/display.js"></script>
<body>

</body>
<script type="text/javascript">
	const board = sessionStorage.getItem("board");
	const clientId = sessionStorage.getItem("clientId");
	const gameId = sessionStorage.getItem("gameId");

	var connection = new WebSocket("ws://localhost:8080/DHBW-Vier-gewinnt/socket");

	function methodConnect() {
		return {
			method: "connect game",
			clientId: sessionStorage.getItem("clientId"),
		}
	};

	function methodMakeTurn(column) {
		return {
			method: "make turn",
			clientId: sessionStorage.getItem("clientId"),
			gameId: sessionStorage.getItem("gameId"),
			column: column
		}
	};

	function methodCheckWin() {
		return {
			method: "check win",
			clientId: sessionStorage.getItem("clientId"),
			gameId: sessionStorage.getItem("gameId")
		}
	};

	function handleClientId(clientId) {
		console.log("Client id " + clientId);
		sessionStorage.setItem("clientId", clientId);
	};

	function handleWin() {
		sessionStorage.clear();
		document.getElementById("gameResult").innerHTML = "<a style=\"color: white;\" > Game Won </a><br></br>";
		connection.close();
	};

	function handleLost() {
		sessionStorage.clear();
		document.getElementById("gameResult").innerHTML = "<a style=\"color: white;\" > Game Lost </a><br></br>";
		connection.close();
	};

	connection.onopen = function () {
		console.log("reconnected");
		console.log(board);
		connect();
		checkWin();

		abstractCreateTable(eval(board), document.getElementById("gameCanvas"), "", (column) => {
			event.preventDefault();
			console.log("ACTIONNNN " + column);
			makeTurn(column);
		});
	};

	connection.onmessage = function (message) {
		const data = message.data;
		console.log("Server: " + data);
		const json = JSON.parse(data);
		console.log(json);
		const method = json.method;

		if (method === "connection established") {
			console.log("connection established");
			const clientId = json.clientId;
			handleClientId(clientId);
			return;
		};

		if (method == "turn taken") {
			console.log("turn taken")
			const board = JSON.stringify(json.board);
			sessionStorage.setItem("board", board);
			location.reload(true);
		};

		if (method === "game won") {
			console.log("game won");
			handleWin();
		};

		if (method === "game lost") {
			console.log("game lost");
			handleLost();
		};

		if (method === "error") {
			const errorMessage = json.errorMessage;
			console.log("error: " + errorMessage);
			return;
		};
	};

	connection.onerror = function (error) {
		console.log("Websocket Error: " + error);
	};

	connection.onclose = function (event) {
		console.log("Connection closed" + event);
	};

	function connect() {
		console.log("connect");
		const json = methodConnect();
		const request = JSON.stringify(json);
		connection.send(request);
	};

	function makeTurn(column) {
		console.log("make turn");
		const json = methodMakeTurn(column);
		const request = JSON.stringify(json);
		connection.send(request);
	};

	function checkWin() {
		console.log("check win");
		const json = methodCheckWin();
		const request = JSON.stringify(json);
		connection.send(request);
	};
</script>

</html>