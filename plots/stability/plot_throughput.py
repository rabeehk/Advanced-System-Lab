import numpy as np
import matplotlib.pyplot as plt
import math
import matplotlib

# example data
with open("throughput_means.txt") as f:
    y = f.read().splitlines()
with open("throughput_std.txt") as f:
    error=f.read().splitlines()

y=[float(i) for i in y]
error=[float(i) for i in error]

#len_y= len(y)
#len_error=len(error)

#l=1900 #2000
#l=min(len_y, len_error)

#y=y[0:l]
#error=error[0:l]
#error[l-1]=0

x=np.arange(0, 1800, 20)
#x= range(l)

#x = [8, 16, 32, 64]
#y=[0.51, 0.88, 1.71,  3.46]
#error= [0.01, 0.02, 0.03, 0.07]
#yint = range(int(min(y)), math.ceil(max(y))+1)
#plt.yticks(yint)
#print(yint)

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, y, yerr=error, fmt='-om', ecolor='m', capthick=2)
ax0.set_title('Throughput')
ax0.set_xlabel('Second')
ax0.set_ylabel('Requests per second')
ax0.set_ylim([999,1001])
ax0.set_xticklabels([999,1001])
y_formatter = matplotlib.ticker.ScalarFormatter(useOffset=False)
ax0.yaxis.set_major_formatter(y_formatter)
x_formatter = matplotlib.ticker.ScalarFormatter(useOffset=False)
ax0.xaxis.set_major_formatter(x_formatter)
plt.show()



