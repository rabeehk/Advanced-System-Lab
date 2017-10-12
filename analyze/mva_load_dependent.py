import pdb 

def update_R(n, P, m, S):
    r = 0
    for j in range(1, n+1):
        mu = min(j, m)/float(S)
        r += (P[j-1]*j/mu)
    return r             

def update_P(X, n, P, m, S):
   for j in range(n-1, 0, -1):
      mu = min(j, m)/float(S)
      P[j]=(X/mu)*P[j-1]
       
   P[0]=1-sum(P[i] for i in range(1, n))


def mva():
    n = 160      # number of users 
    points = [32, 64, 96, 128, 160]
    m  = 4       # number of devices 
    z = 0.000000001 # 0.000000000000001
    n = 160
    
    P_db = [0]*n # P for database
    P_db[0]=1
    S_nt = 0.0766491  # S for network    
    S_fr = 0.00081869078 # S for front-end 
    S_bk = 0.018945692078318192    # S for back-end
    S_db = 0.3981097160887932
    m_db = 2.0
    m_bk = 32
    m_nt= 60
    m_fr = 60
    Q_bk = [0]*32
    Q_fr = [0]*60
    Q_nt = [0]*60
    Ss_bk = [float(S_bk)] * 32
    Ss_nt = [float(S_nt)] * 60
    Ss_fr = [float(S_fr)] * 60    


    V = [1]*m
    Rs = [0] * m
    R_calc=[]
    X_calc=[]
    print('point', 'R', 'X')
    for j in range(1, n+1):
        # update Rs
        Rs[0] = sum([Ss_nt[i]*(1+Q_nt[i]) for i in range(0, m_nt)])
        Rs[1] = sum([Ss_fr[i]*(1+Q_fr[i]) for i in range(0, m_fr)])
        Rs[2] = sum([Ss_bk[i]*(1+Q_bk[i]) for i in range(0, m_bk)])
        Rs[3] = update_R(n, P_db, m_db, S_db)
        R = sum(V[i]*Rs[i] for i in range(0,m))
        X = j / float(z+R)
        update_P(X, n, P_db, m_db, S_db)
        # print the results for this point 
        if (len(points) > 0 and j in points) or len(points) == 0:
            print(j, R, X*1000)
            R_calc.append(R)
            X_calc.append(X*1000)
    print(R_calc)
    print(X_calc) 
            
if __name__ == '__main__':
    mva()

