import re

# read 553 page 
# z:client think time
# n:number of users
# vs: service_time, count
def mva(z, n, vs, print_points):   
    z = float(z)
    n = int(n)
 
    if print_points:
        pp = [int(x) for x in print_points.split(',')]
    else:
        pp = []
    Vs = []
    Ss = []
    m = 0
    groups = []
    for x in vs:
        count = int(x[0])
        Ss += [float(x[1])] * count
        Vs += [float(1.0/count)] * count
        m += count
        groups.append(count)
    Qs = [0] * m
    Rs = [0] * m
    R = 0
    
    U_nt=[]
    U_fr = []
    U_bk=[]
    U_db=[]
    print ('Point', 'R', 'X', 'Utilizations(network, front-end, back-end, database)')
    for j in range(1, n+1):
        Rs = [Ss[i]*(1+Qs[i]) for i in range(0, m)]
        R = sum(Rs[i]*Vs[i] for i in range(0, m))
        X = j / (z + R)

        Qs = [X * Vs[i] * Rs[i] for i in range(0, m)]

        if (len(pp) > 0 and j in pp) or len(pp) == 0:
            utilizations = ''
            ind = 0
            us=[]
            for g in groups:
                Ug = X * Ss[ind] * Vs[ind]
                ind += g
                utilizations += '\t{:.6f}'.format(Ug)
                us.append(Ug)
            U_nt.append(us[0])
            U_fr.append(us[1])
            U_bk.append(us[2])
            U_db.append(us[3])     
            print('{}\t{:.3f}\t{:.6f}{}'.format(j, R, X*1000, utilizations))
    print('Netowrk utilization')
    print(U_nt)
    print('front-end utilization')
    print(U_fr)
    print('back-end utilization')
    print(U_bk)
    print('database utilization')
    print(U_db)    
if __name__ == '__main__':
     mva(0.000000001, 160, [(60, 0.0766491), (60, 0.00081869078), (32, 0.018945692078318192), (2, 
0.3981097160887932)], "32, 64, 96, 128, 160") 
     
