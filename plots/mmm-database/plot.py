import numpy as np
import matplotlib.pyplot as plt

x = [4, 8, 16, 24, 40]
r_measured=[1.868381285227245, 2.115646482579043, 3.415342854957656, 5.216265060300032, 8.450317833555255]
r_calculated=[0.5411617550461464, 0.9578889516170715, 3.3073620969465933, 3.7983829834609923, 29.525842162803634]
r_std= [0.005652719962348712, 0.0515353037091526, 0.18004146480036418, 0.532725164843786, 1.0047973676840345] 

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, r_measured, r_std, fmt='-o', capthick=2, color='b', linewidth=2)
ax0.errorbar(x, r_calculated, fmt='-o', capthick=2, color='g', linewidth=2)

ax0.set_title('M/M/2 model for database experiment', weight='heavy')
ax0.set_xlabel('Number of database connections', weight='heavy')
ax0.set_ylabel('Response time(ms)', weight='heavy')
plt.legend(['Measured', 'Calculated'], loc=2)
plt.axis([0, 45, 0, 40])
plt.show()


