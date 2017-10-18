import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn import linear_model
from sklearn.metrics import mean_squared_error, r2_score
from tpot import TPOTClassifier
from sklearn.datasets import load_digits
from sklearn.model_selection import train_test_split
import json


districts = pd.read_csv("regions.csv")

teco = pd.read_csv("teconer_output_201611.csv")
teco['timestamp'] = pd.to_datetime(teco['timestamp'])

teco = teco.sort_values(['timestamp'], ascending=[ True])

fmi = pd.read_csv("fmi-2.csv")
fmi['timestamp'] = pd.to_datetime(fmi['timestamp'])

fmi = fmi.sort_values(['timestamp'], ascending=[True])
fmi = fmi.fillna(method='ffill')


#minDepth = (fmi['snowDepth'].min())
#maxDepth = (fmi['snowDepth'].max())

#print('min ' + str(minDepth))
#print('max ' + str(maxDepth))

#teco[teco['snowDepth'] == 0]['snowDepth'] = 0
#teco[teco['snowDepth'] > 0 & fmi['snowDepth'] <= 10]['snowDepth'] = 1
#teco[teco['snowDepth'] > 10 & fmi['snowDepth'] <= 20]['snowDepth'] = 2
#teco[teco['snowDepth'] > 20]['snowDepth'] = 3





#print(teco.head(100))

#train street temp



join = teco.set_index('timestamp').join(fmi.set_index('timestamp'))
join = join.reset_index()

del join['timestamp']
del join['airTemperature_fmi']
del join['snowDepth']

join['friction_class'] = np.nan

join.loc[join['friction'] <= 0.4, 'friction_class'] = 1
join.loc[(join['friction'] > 0.4) & (join['friction'] <= 0.6), 'friction_class'] = 2
join.loc[join['friction'] > 0.6, 'friction_class'] = 3


del join['friction']
del join['surfaceTemperature']
del join['waterThickness']

from sklearn.metrics import accuracy_score
from sklearn.ensemble import RandomForestClassifier
from sklearn.utils import shuffle

print(join.head(0))

print('District,Data point count,Optimal number of trees, Accuracy')


district_classfiers = {}

for index, row in districts.iterrows():
    #break
    areaId = row['ID']
    district_set = join[join['SubAreaId'] == areaId] 

    district_set = shuffle(district_set)
    
    
    if(len(district_set) == areaId) : 
        print('No data for ' , areaId)
        continue

    #print('Train for ' , areaId)
    #print('Data set length ' , len(district_set))

    del district_set['SubAreaId']
    train_set = district_set[0 : int(0.8 * len(district_set))]
    train_label = district_set[0 : int(0.8 * len(district_set))]['friction_class'].values

    validate_set = district_set[int(0.8 * len(district_set)) : int(0.9 * len(district_set))]
    validate_label = district_set[int(0.8 * len(district_set)) : int(0.9 * len(district_set))]['friction_class'].values

    test_set = district_set[int(0.9 * len(district_set)) : int(1.0 * len(district_set))]
    test_label = district_set[int(0.9 * len(district_set)) : int(1.0 * len(district_set))]['friction_class'].values

    del train_set['friction_class']
    del validate_set['friction_class']
    del test_set['friction_class']
    
    train_set = train_set.as_matrix()
    validate_set = validate_set.as_matrix()
    test_set = test_set.as_matrix()

    
    sizes = np.arange(100, 500, 100)
    val_accs = np.zeros(len(sizes))
    classifiers = []
 
    for idx, size in enumerate(sizes):
        rf = RandomForestClassifier(n_estimators=size)
        rf.fit(train_set, train_label)
        acc = accuracy_score(validate_label, rf.predict(validate_set))
        val_accs[idx] = acc
        classifiers.append(rf)

    

    #print("best val acc", sizes[np.argmax(val_accs)])

    best = classifiers[np.argmax(val_accs)]
    print('area id --> ' , int(areaId))
    district_classfiers[int(areaId)] = best
    print(row['NAME'], len(district_set),  sizes[np.argmax(val_accs)]  , accuracy_score(test_label, best.predict(test_set)))
    

from flask import Flask, jsonify, Response
from flask import request
from flask_cors import CORS
from flask import abort

app = Flask(__name__)
CORS(app)


@app.route('/predict2/<string:arr>', methods=['GET'])
def get_task(arr):
    return 200

@app.route('/')
def index():
    return "Hello, World!"

@app.route('/predict', methods=['POST'])
def predict():
    print("-------------------")
    print(request.get_json(force=True))
    out = []
    best = None
    for jsonz in request.get_json(force=True):
        if not json or not 'zoneId' in jsonz:
            print('Error no zoneId')
            abort(400)
        
        zoneId = int(jsonz['zoneId'])
        if zoneId in district_classfiers :
            best = district_classfiers[zoneId]
        else :
            print('Not found classifier')
            abort(400)

        print("Zone id", zoneId)

        input = []
        input.append(jsonz['airTemperature'])
        input.append(jsonz['windSpeed'])
        input.append(jsonz['gustSpeed'])
        
        input.append(jsonz['relativeHumidity'])
        input.append(jsonz['dewPointTemperature'])
        input.append(jsonz['precipation'])
        input.append(jsonz['precipationDensity'])
        input.append(jsonz['airPressure'])
        input.append(jsonz['visibility'])
        input.append(jsonz['cloudAmount'])
        inz = np.array([input])
        pred = best.predict(inz)
        out.append({'condition' : pred[0], 'zoneId' : zoneId})
    print('returned')
    return jsonify(out), 200
    
if __name__ == '__main__':
    app.run(host= '0.0.0.0')





#trainTeco = teco[0 : int(0.9 * len(teco))]
#testTeco = teco[int(0.9 * len(teco)) : int(1.0 * len(teco))]



#trainTeco = teco[0 : int(0.9 * len(teco))]
#testTeco = teco[int(0.9 * len(teco)) : int(1.0 * len(teco))]



#snowLabel = fmi['snowDepth'].values
#del fmi['timestamp']
#del fmi['snowDepth']
#trainSnowLabel = snowLabel[0 : int(0.9 * len(fmi))]
#testSnowLabel = snowLabel[int(0.9 * len(fmi)) : int(1.0 * len(fmi))]
#trainFmi = fmi[0 : int(0.9 * len(fmi))]
#testFmi = fmi[int(0.9 * len(fmi)) : int(1.0 * len(fmi))]













