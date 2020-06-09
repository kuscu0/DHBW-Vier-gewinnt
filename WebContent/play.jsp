<%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <link rel="shortcut icon" href="favicon.ico"/>
    <title>Vier gewinnt</title>
</head>
<body>

	<jsp:useBean id="control" class="bean.Control"> 
	</jsp:useBean>


	

	<div id="gameCanvas" class="gameCanvas"></div>
	
	<script src="js/display.js"></script>
</body>
</html>