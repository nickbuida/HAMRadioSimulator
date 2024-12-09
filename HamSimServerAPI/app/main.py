from fastapi import FastAPI, WebSocket, WebSocketDisconnect, Query, HTTPException
from typing import List, Dict

app = FastAPI()

# Dictionary to keep track of multiple servers
servers: Dict[str, Dict] = {}

# Endpoint to create a new server with noise level only
@app.post("/server/{server_id}")
async def create_server(
    server_id: str,
    noise_level: float = Query(0.0, description="Noise level for simulation")
):
    if server_id in servers:
        raise HTTPException(status_code=400, detail="Server ID already exists")

    servers[server_id] = {
        "conditions": {
            "noise_level": noise_level
        },
        "clients": [],  # WebSocket clients connected to this server
    }
    return {"message": "Server created", "server_id": server_id, "noise_level": servers[server_id]["conditions"]["noise_level"]}

# Endpoint to list all available servers
@app.get("/servers")
async def list_servers():
    if not servers:
        return {"message": "No servers available. You can create one.", "active_servers": []}
    return {"active_servers": list(servers.keys())}

# Endpoint to retrieve clients connected to a specific server
@app.get("/server/{server_id}/clients")
async def get_server_clients(server_id: str):
    if server_id not in servers:
        raise HTTPException(status_code=404, detail="Server not found")

    # Return a list of client IDs
    client_ids = [client["id"] for client in servers[server_id]["clients"]]
    return {"clients": client_ids}

# Endpoint to retrieve noise level conditions of a specific server
@app.get("/server/{server_id}/conditions")
async def get_server_conditions(server_id: str):
    if server_id not in servers:
        raise HTTPException(status_code=404, detail="Server not found")

    return {"noise_level": servers[server_id]["conditions"]["noise_level"]}

# WebSocket endpoint for clients to connect to a specific server
@app.websocket("/ws/{server_id}")
async def websocket_endpoint(websocket: WebSocket, server_id: str, user_id: str = Query(..., description="User ID of the client")):
    if server_id not in servers:
        await websocket.close(code=1008)
        return

    await websocket.accept()
    
    # Add client using provided user_id
    servers[server_id]["clients"].append({"id": user_id, "websocket": websocket})
    print(f"Client {user_id} connected to server {server_id}")

    try:
        while True:
            data = await websocket.receive_text()
            print(f"Received message: {data}")  # Log received messages
            # Broadcast the message to all connected clients
            for client in servers[server_id]["clients"]:
                await client["websocket"].send_text(data)

    except WebSocketDisconnect:
        servers[server_id]["clients"] = [
            client for client in servers[server_id]["clients"] if client["websocket"] != websocket
        ]
        print(f"Client {user_id} disconnected from server {server_id}")
