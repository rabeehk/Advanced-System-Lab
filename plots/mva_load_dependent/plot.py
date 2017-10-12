import numpy as np
import matplotlib.pyplot as plt



x=[32, 64, 96, 128, 160]
r_measured=[7.0191949739430495,12.875675196531727, 19.149654486573635, 25.13257333611814, 31.662288354029588]
x_measured=[4749.223333333333, 5029.573333333334, 5039.456666666667, 5114.196666666667, 5068.233333333333]

r_calculated=[6.717946585133995, 12.73951091946941, 19.109266371262066, 25.479021828682768, 31.848777286103456]
x_calculated=[4763.360290188787, 5023.7407385213655, 5023.740740740743, 5023.74074074074, 5023.740740740741]


x_error = [19.600, 18.560, 32.640, 46.080, 46.400]
r_error= [0.32750, 0.29500, 0.25500, 0.26250, 0.45500]
 

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, r_measured, r_error, fmt='-o', capthick=2, color='b', linewidth=2)
ax0.errorbar(x, r_calculated, fmt='-o', capthick=2, color='g', linewidth=2)
ax0.set_title('Measured and Calculated Response-times With MVA model', weight='heavy')
ax0.set_xlabel('Number of clients', weight='heavy')
ax0.set_ylabel('Response time(ms)', weight='heavy')
plt.legend(['Measured', 'Calculated'], loc=4)
plt.axis([0, 165, 0, 35])

fig1, ax1 = plt.subplots(nrows=1, sharex=True)
ax1.errorbar(x, x_measured, x_error, fmt='-o', capthick=2, color='b', linewidth=2)
ax1.errorbar(x, x_calculated, fmt='-o', capthick=2, color='g', linewidth=2)
ax1.set_title('Measured and Calculated Throughputs With MVA model', weight='heavy')
ax1.set_xlabel('Number of clients', weight='heavy')
ax1.set_ylabel('throughput(requests/second)', weight='heavy')
plt.legend(['Measured', 'Calculated'], loc=4)
plt.axis([0, 165, 0, 5400])

plt.show()


