function gameStart(payload) {
    connection.send(payload);
}

function turn(payload){
	connection.send(payload);
};