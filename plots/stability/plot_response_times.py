import numpy as np
import matplotlib.pyplot as plt

# example data
with open("response_time_means.txt") as f:
    y = f.read().splitlines()
with open("response_time_std.txt") as f:
    error=f.read().splitlines()

y=[float(i) for i in y]
error=[float(i) for i in error]

#rows=len(y)
x=np.arange(0, 1800, 20)

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, y, yerr=error, fmt='-om', ecolor='m', capthick=2)
ax0.set_title('Response time')
ax0.set_xlabel('Second')
ax0.set_ylabel('response time in milli seconds')

plt.show()



