import numpy as np
import matplotlib.pyplot as plt


x = [32, 64, 96, 160, 256, 320, 384, 512, 960]
r2= [6.9381771986257466, 13.0050508049817, 19.203862229852042, 32.31607987665114, 52.746079006157224, 66.40159484883281, 81.06310111378028,
 111.506123279501, 246.41487611595275]
std2=[0.20700069778995844, 0.3510286419734839,
0.4099541364775185, 0.6765290573613988, 
1.0060613871147779, 1.2477001851495941,
1.6058275568345841, 2.054762265598831, 4.732054355875411]



r1=[7.104509904466872, 13.315840893366618,  19.44196335272281, 32.76677229178585, 53.508194978816555, 67.80637216928537, 82.77178468659878, 114.1420591226717, 246.8159594345321]
std1=[0.1873341269494677, 0.22896710065010667, 
0.28514228128505253, 0.5794196569894646, 
0.7475689917918574, 0.8780293369332743,
1.0672807278529617, 1.7059220752359843, 
4.694102637964856]


fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, r1, yerr=std1, fmt='-o', capthick=2, linewidth=2)
ax0.errorbar(x, r2, yerr=std2, fmt='-o', capthick=2, linewidth=2)
ax0.set_title('Measured Response times in scalability experiment', weight='heavy')
ax0.set_xlabel('Number of clients', weight='heavy')
ax0.set_ylabel('response time (ms)', weight='heavy')
plt.legend(['1 middleware', '2 middlewares'], loc=4)
plt.show()



