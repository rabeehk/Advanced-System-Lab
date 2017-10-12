import numpy as np
import matplotlib.pyplot as plt


x = [8, 16, 32, 64, 160]
r_measured = [2.543683384593723, 3.8720424557686375, 6.9381771986257466,  13.0050508049817, 32.31607987665114]
r_calculated = [0.6901563496758305, 1.7332212855843032, 3.9141439313181485, 149.8417670939331, 689.4776517308053]

std2=[0.04338646542437698, 0.1850460531393707, 0.20700069778995844,  0.3305686112603974, 0.6765290573613988]


fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, r_measured, std2, fmt='-o', capthick=2, color='b', linewidth=2)
ax0.errorbar(x, r_calculated, fmt='-o', capthick=2, color= 'g', linewidth=2)

ax0.set_title('M/M/1 model for scalability experiment', weight='heavy')
ax0.set_xlabel('Number of clients', weight='heavy')
ax0.set_ylabel('Response time(ms)', weight='heavy')
plt.legend(['Measured  - 2 Middlewares',
            'Calculated - 2 Middlewares'], loc=2)
plt.axis([0, 170, 0, 750])
plt.show()


