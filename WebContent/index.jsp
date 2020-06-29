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

<body>

	<h1>Vier gewinnt</h1>

	<div class="menu">
		<form action="play" method="post" style="display: flex; justify-content: space-evenly;">
			<button class="button compBtn" name="playBtn">vs.</button>
			<button class="button twoPlayersBtn" name="2playersBtn">vs.</button>
			<a href="onlinegame.jsp" class="button onlineBtn" name="onlineBtn">vs.</a>
			<button class="button helpBtn" id="helpBtn" name="helpBtn">?</button>
			<span class="hiddenText1">Spieler gegen Computer</span> <span class="hiddenText2">2 Spieler</span> <span
				class="hiddenText3">Online-Match</span>
			<span class="hiddenText4">Hilfe</span>
		</form>
	</div>

	<a href="impressum.jsp" class="imprint">Impressum</a>
</body>

<script type="text/javascript">
	// const clients = {};

	// function create_UUID() {
	// 	var dt = new Date().getTime();
	// 	var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
	// 		var r = (dt + Math.random() * 16) % 16 | 0;
	// 		dt = Math.floor(dt / 16);
	// 		return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
	// 	});
	// 	return uuid;
	// }


</script>

</html>
<!--
All Icons available on https://fontawesome.com/icons?d=gallery&m=free
-->