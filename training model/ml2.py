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


fmi = pd.read_csv("fmi-snow_diff_3.csv")
fmi = fmi.fillna(method='ffill')

del fmi['snowDiff']
del fmi['snowDepth']
del fmi['timestamp']
fmi = shuffle(fmi)

train_set = fmi[0 : int(0.8 * len(fmi))]
train_label = fmi[0 : int(0.8 * len(fmi))]['snow_class'].values

validate_set = fmi[int(0.8 * len(fmi)) : int(0.9 * len(fmi))]
validate_label = fmi[int(0.8 * len(fmi)) : int(0.9 * len(fmi))]['snow_class'].values

test_set = fmi[int(0.9 * len(fmi)) : int(1.0 * len(fmi))]
test_label = fmi[int(0.9 * len(fmi)) : int(1.0 * len(fmi))]['snow_class'].values

sizes = np.arange(100, 1000, 100)
val_accs = np.zeros(len(sizes))
classifiers = []

del train_set['snow_class']
del validate_set['snow_class']
del test_set['snow_class']

for idx, size in enumerate(sizes):
    rf = RandomForestClassifier(n_estimators=size)
    rf.fit(train_set, train_label)
    acc = accuracy_score(validate_label, rf.predict(validate_set))
    print("step score ", acc)
    val_accs[idx] = acc
    classifiers.append(rf)

    

    #print("best val acc", sizes[np.argmax(val_accs)])

best = classifiers[np.argmax(val_accs)]
pred = best.predict(test_set)
print("Score : "  , accuracy_score(test_label, pred))  

fig, ax = plt.subplots()
x = range(0, len(test_label))
p1 = ax.plot(x, pred, 'bs', linewidth=2, color='r', label = 'snow predict')
p2 = ax.plot(x, test_label, '+', linewidth=2, color='b', label = 'snow actual')


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