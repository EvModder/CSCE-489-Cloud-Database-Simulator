from __future__ import print_function
import pymongo
import json
import pprint

HOST_IP = "34.233.78.56"
DB_NAME = "fitness_424003778"
HOST_URL = "mongodb://" + HOST_IP + "/" + DB_NAME
MONGO_CLIENT = pymongo.MongoClient(HOST_URL)
MONGO_DB = MONGO_CLIENT[DB_NAME]

def getCollection(name):
    collection = MONGO_DB[name]
    return collection



##################################################
def main():
    print("Welcome!")
    print("Available Commands: select <board>, read, write <msg>, listen, stop, exit")
    print("Available Message Boards: ", end="")
    for name in MONGO_DB.collection_names():
        print(name, end="  ")
    print()
    
    cmd = None
    board = None
    while cmd != "exit":
        cmd = raw_input("> ").lower()
        
        if cmd == "exit":
            break
        
        elif cmd.startswith("select "):
            board_name = cmd[7:]
            if board_name in MONGO_DB.collection_names():
                board = getCollection(board_name)
                print("Selected:", board_name)
            else:
                print("Unknown Message Board!")
            
        elif cmd == "read":
            pass
        
        elif cmd.startswith("write "):
            msg = cmd[6:]

        elif cmd == "listen":
            pass

        elif cmd == "stop" or cmd == "stoplisten" or cmd == "return":
            pass

        else:
            print("Unknown command")

main()
