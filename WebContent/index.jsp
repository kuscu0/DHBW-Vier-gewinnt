<%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<html lang="de">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <link rel="stylesheet" type="text/css" href="css/style.css">
  <link rel="shortcut icon" href="favicon.ico"/>
  <title>Vier gewinnt</title>
</head>
<body>
<h1>Vier gewinnt</h1>

<div class="menu">
	<form action="play" method="post" style="display: flex; justify-content: space-evenly;">
		<button class="button compBtn" name="playBtn">vs.</button>
		<button class="button twoPlayersBtn" name="2playersBtn">vs.</button>
		<button class="button onlineBtn" name="onlineBtn">vs.</button>
		<button class="button helpBtn" name="helpBtn">?</button>
	</form>
</div>

<script src="js/functions.js"></script>
</body>
</html>
<!--
All Icons available on https://fontawesome.com/icons?d=gallery&m=free
-->
