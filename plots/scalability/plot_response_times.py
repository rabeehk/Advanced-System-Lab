import numpy as np
import matplotlib.pyplot as plt

# example data
#with open("response_time_means.txt") as f:
#    y = f.read().splitlines()
#with open("response_time_std.txt") as f:
#    error=f.read().splitlines()

#y=[float(i) for i in y]
#error=[float(i) for i in error]

#rows=len(y)
#x=np.arange(0, 1800, 20)

x=[1,2,3]
y_6=[32.797,  22.282, 20.635]
std_6=[0.96750,0.68500, 0.37250]

y_10=[23.900, 20.490,  21.070]
std_10=[0.35500,0.42000, 0.56500]

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, y_6, yerr=std_6, fmt='-oc', ecolor='c', capthick=2)
ax0.errorbar(x, y_10, yerr=std_10, fmt='-om', ecolor='m', capthick=2)
ax0.set_title('Response time')
ax0.set_xlabel('Number of middlewares')
ax0.set_ylabel('response time in milli seconds')
plt.legend(['6 Database connections', '10 Database Connections'], loc=1)
plt.xlim([0.9,3.1])
plt.show()



