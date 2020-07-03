<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

<head>
	<meta charset="ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<link rel="shortcut icon" href="favicon.ico" />
	<title>Vier gewinnt</title>
</head>

<body>
	<div>
		<button name="new game" onclick="newGame()">Neues Spiel</button><br><br>
		<label style="color: white;">Spiele Id eingeben:</label>
		<input type="text" placeholder="Spiel-Id" id="join" name="join id text">
		<button name="join game" onclick="joinGame()">Spiel beitreten</button><br><br>
		<button name="turn" onclick="makeTurn(1)">test make turn</button>
	</div>
</body>
<script type="text/javascript">
	const gameId = sessionStorage.getItem("gameId");
	const clientId = sessionStorage.getItem("clientId");

	function methodConnect() {
		return {
			method: "connect",
		}
	};

	function methodNewGame() {
		return {
			method: "new game",
			clientId: sessionStorage.getItem("clientId")
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

	function methodJoinGame(gameId) {
		return {
			method: "join game",
			clientId: sessionStorage.getItem("clienId"),
			gameId: sessionStorage.getItem("gameId")
		}
	};

	//TODO see if client can reconnect to websocket.
	function handleClientId(clientId) {
		console.log("Client id " + clientId);
		sessionStorage.setItem("clientId", clientId);
	};

	function handleGameId(gameId) {
		console.log("Game Id: " + gameId);
		sessionStorage.setItem("gameId", gameId);
	};

	var connection = new WebSocket("ws://localhost:8080/DHBW-Vier-gewinnt/socket");

	// ... = (error) => foo(error);

	connection.onopen = function () {
		console.log("Websocket open");
		const json = methodConnect();
		const request = JSON.stringify(json);
		connection.send(request);
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

		if (method === "game created") {
			console.log("game created");
			const gameId = json.gameId;
			handleGameId(gameId);
			return;
		};

		if (method === "game starting") {
			console.log("game starting");
			//TODO draw board.
		};

		console.log("Error: unknown method " + method);
	};

	connection.onerror = function (error) {
		console.log("Websocket Error" + error);
	};

	connection.onclose = function (event) {
		console.log("Connection closed");
		sessionStorage.clear();
	};


	function newGame() {
		console.log("create new game");
		const json = methodNewGame();
		const request = JSON.stringify(json);
		connection.send(request);
	};

	function joinGame() {
		console.log("join game");
		const gameId = document.getElementById("join").value;
		const json = methodJoinGame(gameId);
		const request = JSON.stringify(json);
		connection.send(request);
	};

	function makeTurn(column) {
		console.log("make turn");
		const json = methodMakeTurn(column);
		const request = JSON.stringify(json);
		connection.send(request);
	};

</script>

</html>