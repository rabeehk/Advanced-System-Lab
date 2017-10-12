import numpy as np
import matplotlib.pyplot as plt
import math
import matplotlib

x=[32, 64, 96, 128, 160]
y=[7.0191949739430495, 12.875675196531727, 19.149654486573635, 25.13257333611814, 31.662288354029588]
y_calc=[6.737943818182202 , 12.724737419741368,  19.049672682967802, 25.028368743478122,  31.56918584385092]
y_std=[0.2683149398382193, 0.37403104995376996, 0.49049529993516905, 
0.6432249565099903, 0.7323213835003738] 


fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, y, y_std, fmt='-oc', ecolor='b', capthick=2, linewidth=2)
ax0.errorbar(x, y_calc,  fmt='-oc', color='r', capthick=2, linewidth=2)
ax0.set_title('Maximum throughput experiment', weight='bold')
ax0.set_xlabel('Number of clients', weight='bold')
ax0.set_ylabel('Response time(ms)', weight='bold')
y_formatter = matplotlib.ticker.ScalarFormatter(useOffset=False)
ax0.yaxis.set_major_formatter(y_formatter)
x_formatter = matplotlib.ticker.ScalarFormatter(useOffset=False)
ax0.xaxis.set_major_formatter(x_formatter)
plt.axis([0, 165, 0, 40])
plt.legend(['Measured', 'Calculated'], loc=4)
plt.show()



