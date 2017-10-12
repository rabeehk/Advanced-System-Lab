import numpy as np
import matplotlib.pyplot as plt



#[8, 16, 64, 4]
#-------meaured R ------
#[0.5170035256883078, 0.8838437125175677, 3.488241890145819, 0.2950806018192568]
#-------calculated R -------
#[1.894721379937924, 3.2598042753865575, 13.371937078206443, 1.7759786388431384]
#--------measured throughput ------
#[15337.937238493723, 17848.983333333334, 18356.904166666667, 13526.48]

x=[4, 8, 16, 64]
r_measured=[0.2950806018192568,  0.5170035256883078, 0.8838437125175677, 3.488241890145819]
r_calculated=[1.7759786388431384, 1.894721379937924, 3.2598042753865575, 13.371937078206443]
r_std=[ 0.0031554871514115285, 0.002593896295600442,  0.006526208147366405,  0.2261475436393703]


fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, r_measured, r_std, fmt='-o', capthick=2, color='b', linewidth=2)
ax0.errorbar(x, r_calculated, fmt='-o', capthick=2, color='g', linewidth=2)
ax0.set_title('M/M/30 model for middleware experiment', weight='heavy')
ax0.set_xlabel('Number of clients', weight='heavy')
ax0.set_ylabel('Response time(ms)', weight='heavy')
plt.legend(['Measured', 'Calculated'], loc=2)
#plt.axis([0, 45, 0, 40])
plt.show()


