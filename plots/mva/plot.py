import numpy as np
import matplotlib.pyplot as plt



x=[32, 64, 96, 128, 160]
r_measured=[6.571887315836746, 12.940092472657716,19.309336434365147, 25.678837120943392 ,32.04844002707101]
x_measured=[4869.225301233952, 4945.86882823948, 4971.688194534511 ,  4984.649397951899 , 4992.442685505351]
r_calculated=[6.572, 13.032688168543036, 19.447996998899207, 25.86356520415514, 32.27923668267793]
x_calculated = [4834.253901540043, 4910.352248142991, 4935.98718769063, 4948.855663710782, 4956.593149326518]

x_error = [19.600, 18.560, 32.640, 46.080, 46.400]
r_error= [0.32750, 0.29500, 0.25500, 0.26250, 0.45500]



fig, ax0 = plt.subplots(nrows=1, sharex=True)
ax0.errorbar(x, r_measured, r_error,  fmt='-o', capthick=2, color='b', linewidth=2)
ax0.errorbar(x, r_calculated, fmt='-o', capthick=2, color='g', capthick=2, linewidth=2)
ax0.set_title('Measured and Calculated Response-times With MVA Model', weight='heavy')
ax0.set_xlabel('Number of clients', weight='heavy')
ax0.set_ylabel('Response time(ms)', weight='heavy')
plt.legend(['Measured', 'Calculated'], loc=4)
plt.axis([0, 165, 0, 35])

fig1, ax1 = plt.subplots(nrows=1, sharex=True)
ax1.errorbar(x, x_measured, x_error, fmt='-o', capthick=2, color='b', linewidth=2)
ax1.errorbar(x, x_calculated, fmt='-o', capthick=2, color='g', linewidth=2)
ax1.set_title('Measured and Calculated Throughputs With MVA Model', weight='heavy')
ax1.set_xlabel('Number of clients', weight='heavy')
ax1.set_ylabel('throughput(requests/second)', weight='heavy')
plt.legend(['Measured', 'Calculated'], loc=4)
plt.axis([0, 165, 0, 5400])

plt.show()


