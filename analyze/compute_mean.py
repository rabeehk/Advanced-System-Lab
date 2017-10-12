import sys
import os
import logging

def mean(path):
  total=0
  count=0
  with open(path, 'r') as f:
     for line in f:
       line=line.strip()
       total+=(float(line))
       count+=1
  print(total/count) 


if __name__ == '__main__':
    if not len(sys.argv) == 2:
        logging.error("compute_mean.py  stability_mean_file")
        sys.exit(1)
    mean(sys.argv[1])
