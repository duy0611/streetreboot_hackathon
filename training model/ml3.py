import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn import linear_model
from sklearn.metrics import mean_squared_error, r2_score
from tpot import TPOTClassifier
from sklearn.datasets import load_digits
from sklearn.model_selection import train_test_split
import json
import matplotlib.pyplot as plt
from sklearn.utils import shuffle


from sklearn.metrics import accuracy_score
from sklearn.ensemble import RandomForestClassifier
from sklearn.utils import shuffle
from sklearn.ensemble import RandomForestRegressor


teco = pd.read_csv("teconer_output_201611.csv")
teco['timestamp'] = pd.to_datetime(teco['timestamp'])

teco = teco.sort_values(['timestamp'], ascending=[ True])

fmi = pd.read_csv("fmi-2.csv")
fmi['timestamp'] = pd.to_datetime(fmi['timestamp'])

fmi = fmi.sort_values(['timestamp'], ascending=[True])
fmi = fmi.fillna(method='ffill')


join = teco.set_index('timestamp').join(fmi.set_index('timestamp'))
join = join.reset_index()

del join['timestamp']
del join['airTemperature_fmi']
del join['snowDepth'] 
del join['friction']
del join['waterThickness']

join = join[(abs(join['surfaceTemperature'] - join['airTemperature']) < 10 )]

join = shuffle(join)



train_set = join[0 : int(0.8 * len(join))]
train_label = join[0 : int(0.8 * len(join))]['surfaceTemperature'].values

validate_set = join[int(0.8 * len(join)) : int(0.9 * len(join))]
validate_label = join[int(0.8 * len(join)) : int(0.9 * len(join))]['surfaceTemperature'].values

test_set = join[int(0.9 * len(join)) : int(1.0 * len(join))]
test_label = join[int(0.9 * len(join)) : int(1.0 * len(join))]['surfaceTemperature'].values

del train_set['surfaceTemperature']
del validate_set['surfaceTemperature']
del test_set['surfaceTemperature']


sizes = np.arange(300, 1000, 100)
val_accs = np.zeros(len(sizes))
classifiers = []


best = RandomForestRegressor(n_estimators=300)
best.fit(train_set, train_label)

pred =  best.predict(test_set)

print("test error ", mean_squared_error(pred, test_label))



fig, ax = plt.subplots()
x = range(0, len(test_label))
p1 = ax.plot(x, pred, 'bs', linewidth=2, color='r', label = 'predict surface temp.')
p2 = ax.plot(x, test_label, '+', linewidth=2, color='b', label = 'actual surface temp.')


# Now add the legend with some customizations.
legend = ax.legend(loc='upper center', shadow=True)

# The frame is matplotlib.patches.Rectangle instance surrounding the legend.
frame = legend.get_frame()
frame.set_facecolor('0.90')

# Set the fontsize
for label in legend.get_texts():
    label.set_fontsize('large')

for label in legend.get_lines():
    label.set_linewidth(1.5)  # the legend line width

plt.show()
