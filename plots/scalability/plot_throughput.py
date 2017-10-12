import numpy as np
import matplotlib.pyplot as plt
import math
import matplotlib

# example data
#with open("throughput_means.txt") as f:
#    y = f.read().splitlines()
#with open("throughput_std.txt") as f:
#    error=f.read().splitlines()

x=[1, 2, 3]
y_6=[96*30.520, 96*44.960, 96*48.612 ]
std_6=[96*0.012500, 96*0.12500, 96*0.30250 ]

y_10=[96*41.877, 96*49.030, 96*48.123 ]
std_10=[96*0.050000, 96*0.17250, 96*0.30250 ]

#len_y= len(y)
#len_error=len(error)

#l=1900 #2000
#l=min(len_y, len_error)

#y=y[0:l]
#error=error[0:l]
#error[l-1]=0

#x=[6, 10]
#x= range(l)

#x = [8, 16, 32, 64]
#y=[0.51, 0.88, 1.71,  3.46]
#error= [0.01, 0.02, 0.03, 0.07]
#yint = range(int(min(y)), math.ceil(max(y))+1)
#plt.yticks(yint)
#print(yint)

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, y_6, yerr=std_6, fmt='-oc', ecolor='c', capthick=2)
ax0.errorbar(x, y_10, yerr=std_10, fmt='-om', ecolor='m', capthick=2)

ax0.set_title('Throughput')
ax0.set_xlabel('Number of middleware instances')
ax0.set_ylabel('Requests per second')
#ax0.set_ylim([999,1001])
#ax0.set_xticklabels([999,1001])
#y_formatter = matplotlib.ticker.ScalarFormatter(useOffset=False)
#ax0.yaxis.set_major_formatter(y_formatter)
#x_formatter = matplotlib.ticker.ScalarFormatter(useOffset=False)
#ax0.xaxis.set_major_formatter(x_formatter)
plt.xlim([0.9, 3.1])
plt.legend(['6 Database connections', '10 Database Connections'], loc=4)

plt.show()



