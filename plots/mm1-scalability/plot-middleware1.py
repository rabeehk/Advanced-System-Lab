import numpy as np
import matplotlib.pyplot as plt



x=[8, 16, 32, 48, 96]
r_measured=[2.50526,    3.79030, 7.10450, 9.83219,  19.4420]
r_calculated=[0.632005, 2.540113, 2.789744, 52.21968, 326.8116]

std1=[0.014038778647370725, 0.12318663053907228,  0.1873341269494677, 0.17477559974817797, 0.29346271803660195]

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, r_measured, std1, fmt='-o', capthick=2, color='b', linewidth=2)
ax0.errorbar(x, r_calculated, fmt='-o', capthick=2, color='g', linewidth=2)

ax0.set_title('M/M/1 model for scalability experiment', weight='heavy')
ax0.set_xlabel('Number of clients', weight='heavy')
ax0.set_ylabel('Response time(ms)', weight='heavy')
plt.legend(['Measured  - 1 Middleware', 'Calculated - 1 Middleware'], loc=2)
#plt.axis([0, 100, 0, 400])
plt.show()


