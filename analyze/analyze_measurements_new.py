import sys
import os
from operator import add
import statistics
import logging


def analyze_logs(typ, path):
    count=0
    total_mean=0
    total_std=0
    subdirs = [p for p in os.listdir(path) if os.path.isdir(os.path.join(path, p))]
     
    for dname in subdirs:
        print(dname)
        if typ=='response-time':
            (mean, std) = analyze_response_time(os.path.abspath(os.path.join(path, dname)))
        else:
            (mean, std) = analyze_throughput(os.path.abspath(os.path.join(path, dname)))
        total_mean+=mean
        total_std+=std
        count = count+1
    mean = total_mean/float(count)
    std = total_std/float(count)
    print('total mean=', mean, ' total std=', std)


def analyze_throughput(path):
    file_intervals = []
    num_secs = 0
    # go through all the files inside the folder 
    for _, _, files in os.walk(path):
        for fname in files:
            with open(os.path.join(path, fname)) as f:
                if(fname.find("~") != -1): continue
                intervals = []
                stdev_interval = []
                current_count = 0
                for row in f.readlines():
                    if row.find(":") == -1: # one block line, the line with date
                        current_count=current_count+1 # one operation
                    else:
                        num_secs += 1 # now we are one block more 
                        if num_secs == 1:
                            intervals.append(current_count)
                            current_count = 0
                            num_secs = 0
                file_intervals.append(intervals) 

    means = []
    stds = []
    for i in zip(*file_intervals): # joins all the block intervals together
        mean = statistics.mean(i)
        pstdev = statistics.pstdev(i)
        means.append(mean)
        stds.append(pstdev)
    
    mean=statistics.mean(means)
    std=statistics.mean(stds)
    print('mean=',mean, ' std=',std)
    return (mean, std)



# this function analyze one subfolder: for instance MPeek-response-time-1: inside are files
def analyze_response_time(path):
    file_intervals = []
    num_secs = 0
    intervals = []
    current_interval = []
    for _, _, files in os.walk(path):
        for fname in files:
            intervals = []
            current_interval = []
            num_secs=0
            with open(os.path.join(path, fname)) as f:
                if(fname.find("~") != -1): continue
                intervals = []
                current_interval = []
                num_secs=0
                for row in f.readlines():
                    if row.find(":") == -1: # one block line, the line with date
                        current_interval.append(float(row.strip())/1000.0)
                    else:
                        num_secs = num_secs+1 # now we are one block more 
                        if num_secs == 1:
                            mean = statistics.mean(current_interval)			  	
                            intervals.append(mean)
                            current_interval = []
                            num_secs = 0
                file_intervals.append(intervals)
                    
    means = []
    stds = []
    for i in zip(*file_intervals):
        mean = statistics.mean(i)
        pstdev = statistics.pstdev(i, mean)
        means.append(mean)
        stds.append(pstdev)
     	
    mean = statistics.mean(means)
    std=statistics.mean(stds)
    print('mean=',mean, ' std=',std)
    return (mean, std)

if __name__ == '__main__':
    if not len(sys.argv) == 2:
        logging.error("Usage: analyze_measurements_new.py  folder_with_measurements")
        sys.exit(1)
    print('throughput')
    analyze_logs('throughput', sys.argv[1])
    print('response-time')
    analyze_logs('response-time', sys.argv[1])
