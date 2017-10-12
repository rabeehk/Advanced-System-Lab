import numpy as np
import matplotlib.pyplot as plt
import statistics

# write statistics
x=[4, 8, 16, 24, 36]
y_write=[4*433.62, 8*364.72, 16*264.67, 190.18*24, 125.79*36]
e_write= [16.62*4, 9.5*8, 2.71*16, 6.11*24, 2.90*36]

# read statistics
y_read = [765.68*4, 685.75*8, 402.78*16, 256.52*24, 165.25*36]
e_read =[68.41*4, 29.46*8, 17.19*16, 5.44*24, 2.25*36]

# combination
y_comb=[3*783.58, 3*1284.1, 3*1594.0, 3*1621.8,  3*1619.5]
e_comb=[43.067, 36.273,  35.520, 17.220, 28.830]

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, y_read, yerr=e_read, fmt='-o', ecolor='b', capthick=2)
ax0.errorbar(x, y_write, yerr=e_write, fmt='-o', ecolor='g', capthick=2)
ax0.errorbar(x, y_comb, yerr=e_comb, fmt='-o', ecolor='r', capthick=2)


ax0.set_title('throughput')
ax0.set_xlabel('Number of database connections')
ax0.set_ylabel('Request per second')

plt.legend(['Read', 'Write', 'Combination'], loc=4)
plt.show()



