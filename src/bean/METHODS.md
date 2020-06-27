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