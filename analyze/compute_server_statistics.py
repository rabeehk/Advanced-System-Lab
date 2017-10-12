from collections import namedtuple
import sys
import os
from operator import add
import statistics
import logging
import operator

def analyze(path):
    basename = os.path.basename(path)
    
    # add all the values in the files and find the mean and std
    num_seconds=0
    total_intervals=[]
    for _, _, files in os.walk(path):
        for fname in files:
            with open(os.path.join(path, fname)) as f:
                current_interval=[]
                interval=[]
                if(fname.find("~") != -1): continue
                for row in f.readlines():
                    if row.find(":") == -1:
                        row=row.strip()
                        temp=row.split('\t')
                        if(len(temp)==2):
                          second=temp[1]
                        else:
                          second=row
                        current_interval.append(float(second)/1000.0) 
                    else:
                        num_seconds+=1
                        if num_seconds==1:
                            mean_temp=statistics.mean(current_interval)
                            interval.append(mean_temp)
                            current_interval=[]
                            num_seconds=0
                total_intervals.append(interval)
    
                
    length=len(total_intervals)
    data= [0] * len(total_intervals[0])
    for interval in total_intervals:
         data = list(map(lambda a, b: a + b, interval, data))
    data = [x / float(length) for x in data]     
    mean=statistics.mean(data)
    std=statistics.pstdev(data)
    print('mean', mean)
    print('std', std)
    

if __name__ == '__main__':
    if not len(sys.argv) == 2:
        logging.error("Usage: compute_server_statistics.py  folder_with_measurements")
        sys.exit(1)
    print("backend_waiting_time:")
    analyze(os.path.join(sys.argv[1], "backend_waiting_time"))
    print("backend_time:")
    analyze(os.path.join(sys.argv[1], "backend_time"))
    print("frontend_waiting_time:")
    analyze(os.path.join(sys.argv[1], "frontend_waiting_time"))
    print("frontend_time:")
    analyze(os.path.join(sys.argv[1], "frontend_time"))
    print("response_waiting_time:")
    analyze(os.path.join(sys.argv[1], "response_waiting_time"))
    print("response_time:")
    analyze(os.path.join(sys.argv[1], "response_time"))
    print("total_time:")
    analyze(os.path.join(sys.argv[1], "total_time"))

