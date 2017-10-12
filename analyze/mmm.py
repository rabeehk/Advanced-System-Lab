import sys
import os
import logging


def mmm(mu, m, path):
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
            rho =float(X)/(float(mu)*float(m))
            R_calc=(float(1000.0)/float(mu))*(1+(float(rho))/(float(m)*(1-float(rho))))
            d=(abs(float(R_calc)-float(R)))	
            diff=(abs(float(R_calc)-float(R)))/max(float(R), R_calc)*100
            Rs.append(float(R))
            Ns.append(int(N))
            Re.append(R_calc)
            Xs.append(float(X)) 
            print "N=",int(N), "X=", float(X),  "rho=", rho, "R=",float(R), "R_calc=",float(R_calc), "diff=", diff

   print('------N------')
   print(Ns)
   print('-------meaured R ------')
   print(Rs)
   print('-------calculated R -------')
   print(Re)
   print('--------measured throughput ------')
   print(Xs)
 
if __name__ == '__main__':
    if not len(sys.argv) == 4:
        logging.error("Usage: mmm.py  mu m statistics_file")
        sys.exit(1)
    mmm(sys.argv[1], sys.argv[2], sys.argv[3])
