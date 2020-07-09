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

### join game
Join a game
```
{
	"method": "join game",
	"clientId": 1,
	"gameId": 2
}
```

### start gane
Starts a game
```
{
	"method": "start game"
}
```

### connect game
Connects and reconnets the user to the game.
```
{
	"method": "connect game",
	"clientId": 1
}
```

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

### check win
Checks if the game was won.
```
{
	"method": "check win",
	"clientId": 2,
	"gameId": 3
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

### game starting
Confirms the start of the game
```
{
	"method": "game starting",
	"clientId": 1,
	"gameId": 2,
	"board": [][]
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

### game won
Confirms the player won.
```
{
	"method: "game won",
	"gameId": 1
}
```

### game lost
Confirms the player lost.
```
{
	"method": "game lost",
	"gameId": 2
}
```
