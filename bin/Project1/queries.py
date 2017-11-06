from mongo_connect import connectMongo
import constants
import pymongo
import json
import pprint


collection = connectMongo()

# RQ1:
print "Total:", collection.find().count()

# RQ2:
active_employees = collection.find({"tags": "active"})
print "Active:", active_employees.count()

# RQ3:
gt_60_regex = "([7-9]\d)|(6[1-9])|\d{3,}"
goal_dur_gt_60 = collection.find({"goal.activityGoal": {"$regex": gt_60_regex}})
print "Goal Duration>60:", goal_dur_gt_60.count()

# RQ4:
result = collection.aggregate([
    { '$unwind': '$activityDuration' }, 
    { '$group': { 
        '_id': '$uid', 
        'total_dur': { '$sum': '$activityDuration' }
    }}
])
for data in result:
	pprint.pprint(data)
