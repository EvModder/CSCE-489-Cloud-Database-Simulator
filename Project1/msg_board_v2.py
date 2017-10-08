from __future__ import print_function
import pymongo
import time
import getpass
import threading

USERNAME = getpass.getuser()
HOST_IP = "34.233.78.56"
DB_NAME = "fitness_424003778"
HOST_URL = "mongodb://" + HOST_IP + "/" + DB_NAME
MONGO_CLIENT = pymongo.MongoClient(HOST_URL)
MONGO_DB = MONGO_CLIENT[DB_NAME]

def getCollection(name):
    collection = MONGO_DB[name]
    return collection


#health_quotes, fit_chat, stories, memes
user_input = [None]

def get_user_input(ref):
    ref[0] = raw_input("listening...\n").lower()

def get_user_input_thread():
    global user_input
    user_input = [None]
    mythread = threading.Thread(target=get_user_input, args=(user_input,))
    mythread.daemon = True
    mythread.start()

def listen(board):
    last_ts = time.time()
    get_user_input_thread()
    
    while user_input[0] != "stop":
        if user_input[0] != None and user_input[0] != "stop":
            get_user_input_thread()
            
        time.sleep(1)
        new_msgs = board.find({"timestamp": {"$gt": last_ts}})
        for msg in new_msgs:
            print(msg['user'], ": ", msg['message'], sep="")
            if msg['timestamp'] > last_ts:
                last_ts = msg['timestamp']


def read(board, raw):
    print("All messages from '", board.name, "':", sep="")

    messages = board.find()
    if raw:
        for msg in messages:
            print(msg)
    else:
        for msg in messages:
            print(msg['user'], ": ", msg['message'], sep="")


def main():
    global USERNAME
    print("Welcome, ", USERNAME, "!", sep="")
    print("Available Commands: select, read, write, listen, stop, exit")
    print("Available Message Boards: ", end="")
    for name in MONGO_DB.collection_names():
        if name != "fitness_424003778":
            print(name, end=" ")
    print()
    
    cmd = None
    board = None
    while cmd != "exit":
        cmd = raw_input("> ").lower()
        
        if cmd == "exit":
            break
        
        elif cmd.startswith("select"):
            board_name = cmd[7:]
            if board_name in MONGO_DB.collection_names():
                board = getCollection(board_name)
                print("Selected:", board_name)
            else:
                print("Unknown Message Board!")
            
        elif cmd.startswith("read"):
            if board == None:
                print("Error: no board selected")
            else:
                read(board, cmd.endswith("raw"))
        
        elif cmd.startswith("write"):
            msg = cmd[6:]
            if len(msg) == 0:
                print("Error: empty message")
            elif board == None:
                print("Error: no board selected")
            else:
                ts = time.time()
                board.insert_one({"message":msg,"user":USERNAME,"timestamp":ts})
                print("Message sent!")
            
        elif cmd == "listen":
            if board == None:
                print("Error: no board selected")
            else:
                listen(board)
                
        elif cmd.startswith("username "):
            USERNAME = cmd[9:]
            
        elif cmd == "stop":
            print("You are not currently listening to a message board")

        elif cmd != "":
            print("Unknown command")

main()
