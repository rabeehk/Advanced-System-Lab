import numpy as np
import matplotlib.pyplot as plt
import math
import matplotlib

x=[4, 8, 16, 24, 36]
y=[1.8683, 2.1156 , 3.4153, 5.2162, 7.5606]
y_calc=[1.9041, 2.1587,  3.4027, 5.0569, 7.1659]
y_std = [0.005652719962348712, 0.0515353037091526, 0.18004146480036418, 0.532725164843786, 0.8295484725952146] 

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, y, y_std,  fmt='-oc', ecolor='b', capthick=2, linewidth=2)
ax0.errorbar(x, y_calc,  fmt='-oc', color='r', capthick=2, linewidth=2)
ax0.set_title('Database combination load experiment', weight='heavy')
ax0.set_xlabel('Number of database connections', weight='heavy')
ax0.set_ylabel('Response time(ms)', weight='heavy')
y_formatter = matplotlib.ticker.ScalarFormatter(useOffset=False)
ax0.yaxis.set_major_formatter(y_formatter)
x_formatter = matplotlib.ticker.ScalarFormatter(useOffset=False)
ax0.xaxis.set_major_formatter(x_formatter)
plt.axis([0, 38, 0, 10])
plt.legend(['Measured', 'Calculated'], loc=4)
plt.show()



