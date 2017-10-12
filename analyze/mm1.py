import sys
import os
import logging


def mm1(mu, path):
   count=0
   Rs= []
   Ns=[]
   Re=[]
   Xs=[]
   with open(path, 'r') as f:
     for line in f:
        count = count+1
        if count != 1: 
            line=line.strip()
            [N, R, X]=line.split("\t", 3) 
            rho =(float(X)*float(N))/float(mu)
            R_calc=(float(1.0)/(float(mu)*(1-rho)))*1000.0
            Rs.append(float(R))
            Ns.append(int(N))
            Re.append(R_calc)
            Xs.append(float(X)) 
            print "X=", float(X)*int(N),  "rho=", rho, "N=",int(N), "R=",float(R), "R_calc=",float(R_calc)

   print('------N------')
   print(Ns)
   print('-------meaured R ------')
   print(Rs)
   print('-------calculated R -------')
   print(Re)
   print('--------measured throughput ------')
   Xs=[a*b for a,b in zip(Xs,Ns)]
   print(Xs)
 
if __name__ == '__main__':
    if not len(sys.argv) == 3:
        logging.error("Usage: mm1.py  service_rate statistics_file")
        sys.exit(1)
    mm1(sys.argv[1], sys.argv[2])
