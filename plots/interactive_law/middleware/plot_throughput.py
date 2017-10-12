import numpy as np
import matplotlib.pyplot as plt
import math
import matplotlib

x=[8, 16, 32, 64]
y=[0.5170035256883078, 0.8838437125175677,  1.7125690328213985,  3.488241890145819]
y_calc=[0.521582522839013, 0.8964095994262978, 1.7352022606431952, 3.486426655547629]

y_std=[0.002593896295600442, 0.006526208147366405, 0.0353897198217834, 
 0.2261475436393703]

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, y, y_std,  fmt='-oc', ecolor='b', capthick=2, linewidth=2)
ax0.errorbar(x, y_calc,  fmt='-oc', color='r', capthick=2, linewidth=2)
ax0.set_title('Middleware experiment', weight='heavy')
ax0.set_xlabel('Number of clients', weight='heavy')
ax0.set_ylabel('Response time(ms)', weight='heavy')
y_formatter = matplotlib.ticker.ScalarFormatter(useOffset=False)
ax0.yaxis.set_major_formatter(y_formatter)
x_formatter = matplotlib.ticker.ScalarFormatter(useOffset=False)
ax0.xaxis.set_major_formatter(x_formatter)
plt.axis([0, 66, 0, 4])
plt.legend(['Measured', 'Calculated'], loc=4)
plt.show()



