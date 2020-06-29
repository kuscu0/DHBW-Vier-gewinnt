# WebSocket message
## Client to server
### connect
Establishes a new connection to the server.
```
{
	"method": "connect"
}
```
The server responds with `connection established`.

### new game
Creates a new game.
```
{
	"method": "new game",
	"clientId": 1
}
```
The server responds with `game created`.

### make turn
User sends his turn to the server.
```
{
	"method": "make turn",
	"clientId": 1,
	"gameId": 3,
	"column": 2
}
```

## Server to client
### connection established
Confirms that a new connection was established.
```
{
	"method": "connection established",
	"clientId": 1
}
```

### game created
Confirms that a new game was created.
```
{
	"method": "game created",
	"gameId": 2
}
```

### turn taken
Confirms turn and response with Board.
```
{
	"method": "turn taken",
	"gameId": 3,
	"board": [1][1]
}
```