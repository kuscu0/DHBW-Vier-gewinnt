var testTableData = [
	[0,0,0,0,0,0,0],
	[0,0,0,2,0,0,0],
	[0,0,0,0,0,0,0],
	[0,0,0,0,0,0,0],
	[0,0,0,0,0,0,0],
	[1,1,1,1,2,2,2]
];

document.getElementById('compBtn').onclick = createTable(testTableData, document.getElementById("gameContainer"));

document.getElementById('2playersBtn').onclick = function(){
    location.href='play/2players.html';
}

document.getElementById('onlineBtn').onclick = function(){
    location.href='play/online.html';
}

document.getElementById('settingsBtn').onclick = function(){
    location.href='play/settings.html';
}
document.getElementById('aboutBtn').onclick = function(){
    location.href='play/about.html';
}
