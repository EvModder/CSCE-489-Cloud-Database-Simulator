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

BOARD_MAP = {}
idx = 0
for name in MONGO_DB.collection_names():
    idx = idx + 1
    BOARD_MAP[str(idx)] = name


##################################################
print("Welcome!")

def main():
    print("Available Message Boards:")
    for i in BOARD_MAP:
        print("(", i, ") ", BOARD_MAP[i], sep="")

    select_idx = raw_input("Select #: ")

    if BOARD_MAP[select_idx] != None:
        board_name = BOARD_MAP[select_idx]
        board = getCollection(board_name)
        message_board_loop(board)
    else:
        print("Unknown choice, exiting")


def message_board_loop(board):
    select = None
    while select != "0":
        print("\n(0) Exit, (1) Read, (2) Write, (3) Listen, (4) Switch MsgBoard")
        select = raw_input("Select #: ")
        if select == "0":
            return
        elif select == "1":
            pass
            #pull msgs
        elif select == "2":
            pass
            #enter msg, upload msg
        elif select == "3":
            pass
            #enter waiting loop & display new msgs
        elif select == "4":
            main()
            select = 0
        else:
            print("Invalid selection, try again")
        
    
# RQ1:
#print "Total:", collection.find().count()

# RQ2:
#active_employees = collection.find({"tags": "active"})
#print "Active:", active_employees.count()

# RQ3:
#gt_60_regex = "([7-9]\d)|(6[1-9])|\d{3,}"
#goal_dur_gt_60 = collection.find({"goal.activityGoal": {"$regex": gt_60_regex}})
#print "Goal Duration>60:", goal_dur_gt_60.count()

# RQ4:
#result = collection.aggregate([
#    { '$unwind': '$activityDuration' }, 
#    { '$group': { 
#        '_id': '$uid', 
#        'total_dur': { '$sum': '$activityDuration' }
#    }}
#])
#for data in result:
#	pprint.pprint(data)
main()
