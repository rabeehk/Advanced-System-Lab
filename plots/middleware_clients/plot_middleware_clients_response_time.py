import numpy as np
import matplotlib.pyplot as plt

# example data
x = [8, 16, 32, 64]
y=[0.51, 0.88, 1.71,  3.46]
error= [0.01, 0.02, 0.03, 0.07]

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, y, yerr=error, fmt='-om', ecolor='m', capthick=2)
ax0.set_title('Response time')
ax0.set_xlabel('Number of clients')
ax0.set_ylabel('response time in milli seconds')

plt.show()



