import numpy as np
import matplotlib.pyplot as plt

# example data
x = [8, 16, 32, 64]
y = [1916.88*8, 1115.57*16, 576.31*32, 286.83*64]
# example error bar values that vary with x-position
error = [7.82*8, 2.60*16, 1.39*32, 1.46*64]

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, y, yerr=error, fmt='-oy', ecolor='y', capthick=2)
ax0.set_title('Throughput')
ax0.set_xlabel('Number of clients')
ax0.set_ylabel('Number of requests per second')

plt.show()


