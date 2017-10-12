import numpy as np
import matplotlib.pyplot as plt
import statistics

x=[4, 8, 16, 24, 36]

# write statistics
y_write=[2.31, 2.74, 3.79, 5.62, 8.21]
e_write=[0.12, 0.04, 0.29, 1.28, 1.61]

# read statistics
y_read=[1.30, 1.46, 2.49, 4.03, 6.25]
e_read=[0.01, 0.01, 0.01, 0.94, 1.30]

#combination
y_comb=[2.0967,  2.4767, 3.4800,   5.1033, 7.6233 ]
e_comb=[0.030000, 0.04,  0.083333, 0.18 ,  0.21667]


fig, ax0 = plt.subplots(nrows=1, sharex=True)

ax0.errorbar(x, y_write, yerr=e_write, fmt='-o', ecolor='b', capthick=2)
ax0.errorbar(x, y_comb, yerr=e_comb, fmt='-o', ecolor='g', capthick=2)
ax0.errorbar(x, y_read, yerr=e_read, fmt='-o', ecolor='r', capthick=2)

ax0.set_title('response time')
ax0.set_xlabel('Number of database connections')
ax0.set_ylabel('milli seconds')

plt.legend(['Write', 'Combination', 'Read'], loc='upper left')
plt.show()



