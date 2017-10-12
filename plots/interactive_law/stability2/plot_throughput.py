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


x=np.arange(0, 1800, 20)
y_calc=np.empty(90)
value=40/(9.69627754996/1000.0 ) #+ 0.0003)
y_calc.fill(value)

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, y, yerr=error, fmt='-oc', ecolor='b', capthick=2, linewidth=2)
ax0.errorbar(x, y_calc,  fmt='-oc', color='r', capthick=2, linewidth=2)
ax0.set_title('Throughput over 30 min run', weight='heavy')
ax0.set_xlabel('Second', weight='heavy')
ax0.set_ylabel('Requests per second', weight='heavy')
y_formatter = matplotlib.ticker.ScalarFormatter(useOffset=False)
ax0.yaxis.set_major_formatter(y_formatter)
x_formatter = matplotlib.ticker.ScalarFormatter(useOffset=False)
ax0.xaxis.set_major_formatter(x_formatter)
plt.axis([0, 1800, 0, 4700])
plt.legend(['Measured', 'Calculated'], loc=4)
plt.show()



