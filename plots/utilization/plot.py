import numpy as np
import matplotlib.pyplot as plt



x=[32, 64, 96, 128, 160]
#Netowrk utilization
nt=[0.006220362283946856, 0.006318273240043511, 0.0063512570931949204, 0.006367814836142582, 0.006377770644092802]
#front-end utilization
fr=[6.643983099771598e-05, 6.74856201461511e-05, 6.783792143167084e-05, 6.801477506059618e-05, 6.812111327169451e-05]
#back-end utilization
bk=[0.0028828388505354567, 0.0029282158712368, 0.0029435023013456426, 0.002951176019124089, 0.002955790057444885]
#database utilization
db=[0.9692429511233087, 0.9844992175114158, 0.9896386878040696, 0.9922186783104022, 0.9937699700580537]

fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, nt,   fmt='-s', capthick=4, color='r', linewidth=2.0)
ax0.errorbar(x, fr, fmt='-o',    capthick=4, color='m', linewidth=2.0)
ax0.errorbar(x, bk,   fmt='-^',  capthick=4, color='g', linewidth=2.0)
ax0.errorbar(x, db, fmt='-o',   capthick=4, color='b', linewidth=2.0)
ax0.set_title('Utilizations of different parts of system', weight='heavy')
ax0.set_xlabel('Number of clients', weight='heavy')
ax0.set_ylabel('Utilization', weight='heavy')
plt.legend(['Network I/O', 'Front-end', 'Back-end', 'Database'], loc=4)
plt.axis([0, 165, 0, 1.2])
plt.show()


