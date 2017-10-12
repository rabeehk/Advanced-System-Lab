import sys
import os
import logging

Z = 0

# each line should have the format of  N R X
def interactive_law(typ, path):
     if typ == 'throughput':
        interactive_law_throughput(path)
     elif typ == 'response-time':
        interactive_law_response_time(path)

def interactive_law_throughput(path):
  count=0
  with open(path, 'r') as f:
     for line in f:
       count = count+1
       if count != 1:
           line=line.strip()
           [N, R, X]=line.split("\t", 3)
           X_calc=(float(N))/( ((float(R))/1000.0)+float(Z) )
           diff=(abs(float(X_calc)-float(X)))/max((float(X)), X_calc)
           print(X_calc, X,  diff) 


def interactive_law_response_time(path):
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
            R_calc=(int(N)/float(X))*1000.0-Z # if throughput values are not normalized 
	    d= (abs(float(R_calc)-float(R)))	
            diff=(abs(float(R_calc)-float(R)))/max(float(R), R_calc)*100
            Rs.append(float(R))
            Ns.append(int(N))
            Re.append(R_calc)
            Xs.append(float(X)) 
            print(float(N), float(X)*float(N), float(R),  float(R_calc), d, diff) 

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
        logging.error("Usage: interactive_law.py  type: throughput or response-time statistics_file")
        sys.exit(1)
    interactive_law(sys.argv[1], sys.argv[2])
