import numpy as np
import matplotlib.pyplot as plt

# example data
x = [32, 64, 96, 128, 160]
y=[7, 12.850, 19.120, 25.095, 31.620]
error= [0.32750, 0.29500, 0.25500, 0.26250, 0.45500]


fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, y, yerr=error, fmt='-om', ecolor='m', capthick=2)
ax0.set_title('Response time')
ax0.set_xlabel('Number of clients')
ax0.set_ylabel('response time in milli seconds')
plt.xlim(20, 165)

plt.show()



