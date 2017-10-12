import numpy as np
import matplotlib.pyplot as plt
import math
import matplotlib


x = [32, 64, 96, 160, 256, 320, 384, 512, 960]

r2_measured = [ 6.9381771986257466, 13.0050508049817, 19.203862229852042, 32.31607987665114, 52.746079006157224, 66.40159484883281, 81.06310111378028, 111.506123279501, 246.41487611595275]

r1_measured =[7.104509904466872, 13.315840893366618, 19.44196335272281, 33.52682422866162, 53.508194978816555, 67.80637216928537, 82.77178468659878, 114.1420591226717, 246.8159594345321]

r1_std=[ 0.1873341269494677, 0.22896710065010667, 
0.29346271803660195, 0.473888026021439,  0.7475689917918574,
0.8780293369332743, 1.0672807278529617, 1.7059220752359843,
4.694102637964856]

r2_calculated = [6.80268734494656, 12.921896557525995, 19.149763786333853, 32.270708214307085, 52.69839091501698, 66.35351493921742, 80.99619697543889, 111.42812994435124, 242.8199264124671]

r1_calculated=[6.956238293746224, 13.22082776153483, 19.372088721475777, 32.68754992512508, 53.44586657462101, 67.72286112157525, 82.68158188814014, 114.01162829537029, 243.27467992063166]

r2_std=[0.20700069778995844, 0.3305686112603974, 0.4099541364775185, 0.6765290573613988, 1.0060613871147779,
1.2477001851495941,1.6058275568345841,  2.054762265598831,
4.732054355875411]


fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, r1_measured, r1_std, fmt='-oc', color='g', capthick=2, linewidth=2)
ax0.errorbar(x, r1_calculated,  fmt='-o', color='b', capthick=2, linewidth=2)
ax0.errorbar(x, r2_measured, r2_std, fmt='-oc', color='r', capthick=2, linewidth=2)
ax0.errorbar(x, r2_calculated,  fmt='-o', color='m', capthick=2, linewidth=2)
ax0.set_title('Scalability experiment for 1 and 2 middlewares', weight='heavy')
ax0.set_xlabel('Number of clients', weight='heavy')
ax0.set_ylabel('Response time(ms)', weight='heavy')
y_formatter = matplotlib.ticker.ScalarFormatter(useOffset=False)
ax0.yaxis.set_major_formatter(y_formatter)
x_formatter = matplotlib.ticker.ScalarFormatter(useOffset=False)
ax0.xaxis.set_major_formatter(x_formatter)
plt.axis([0, 1000, 0, 260])
plt.legend(['Measured - 1 Middleware', 'Calculated - 1 Middleware', 'Measured - 2 Middlewares', 'Calculated - 2 Middlewares'], loc=4)
plt.show()



