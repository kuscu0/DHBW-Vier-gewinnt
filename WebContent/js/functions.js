var button = document.getElementsByClassName("button");
var body = document.body;
button.remove();

button.onmouseover = function() {
	body.className = 'blur';
	button.remove();
}

button.onmouseout = function() {
	body.className = '';
}