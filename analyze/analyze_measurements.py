import sys
import os
import logging
from operator import add
import statistics
import logging


def analyze_logs(typ, path, experiment_time):
    if typ == 'throughput':
        analyze_throughput(path, experiment_time)
    elif typ == 'response-time':
        analyze_responsetime(path)

def analyze_throughput(path, experiment_time):
    basename = os.path.basename(path)
    print(basename)
    if len(basename.split('-')) == 3:
        operation, strategy, bench_id = basename.split('-')
    else:
        operation, _, strategy, bench_id = basename.split('-')
    print('starting to analyze dataset with {} {} {}'.format(operation, strategy, bench_id))

    data=[]	
    num_responses = 0
    for _, _, files in os.walk(path):
        for fname in files:
            #extention=os.path.splitext(fname)[1]
            #if not extention==".txt":
            #     continue
            with open(os.path.join(path, fname)) as f:
                #if(f.find("~") != -1): continue
                for row in f.readlines():
                    if row.find(":") != -1: continue 
                    num_responses = num_responses+1     
                data.append(num_responses)
                num_responses=0

    # divide it by time of the experiment	
    d = [x / float(experiment_time) for x in data]
    mean = statistics.mean(d)
    std = statistics.pstdev(d)
    print('mean:\t{:10.2f}'.format(mean))
    print('std:\t{:10.2f}'.format(std))


def analyze_responsetime(path):
    print ("response-time")
    basename = os.path.basename(path)
    if len(basename.split('-')) == 3:
        operation, strategy, bench_id = basename.split('-')
    else:
        operation, _, strategy, bench_id = basename.split('-')
    
    print('starting to analyze dataset with {} {} {}'
               .format(operation, strategy, bench_id))


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
                        current_interval.append(int(row.strip())/1000.0) 
                    else:
                        num_seconds+=1
                        if num_seconds==20:
                            mean_temp=statistics.mean(current_interval)
                            interval.append(mean_temp)
                            current_interval=[]
                            num_seconds=0
                    total_intervals.append(interval)
   
    
    data=[]
    for i in izip(*total_intervals):
         d=[item for item in i]
         m = statistics.mean(d)
         data.append(m) 
         
                              
    mean=statistics.mean(data)
    std=statistics.pstdev(data)
    print('mean:\t{:10.2f}'.format(mean))
    print('std:\t{:10.2f}'.format(std))


if __name__ == '__main__':
    if not len(sys.argv) == 4:
        logging.error("Usage: analyze_logs.py  type: throughput or response-time folder_with_measurements time_of_experiment")
        sys.exit(1)
    analyze_logs(sys.argv[1], sys.argv[2], sys.argv[3])
