import numpy as np
import matplotlib.pyplot as plt

x = [32, 64, 96, 128, 160]
y = [4749.3, 5029.6, 5039.5,5114.6, 5068.8]
error = [19.600, 18.560, 32.640, 46.080, 46.400]

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, y, yerr=error, fmt='-oc', ecolor='c', capthick=2)
ax0.set_title('Throughput')
ax0.set_xlabel('Number of clients')
ax0.set_ylabel('Number of requests per second')
plt.xlim(20, 165)

plt.show()


