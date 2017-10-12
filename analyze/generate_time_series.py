import sys
import os
from operator import add
import statistics
import logging


# this script is for the stability and averaging the values for every Block 
# this script generate the response-time and throughput mean and std for every block
# how to call this function:python3 generate_time_series.py  "throughput" /home/rabeeh/log/mload-1/
def analyze_logs(typ, path):
    if not os.path.isdir(path):
        print("The given path was not a directory")
        sys.exit(1)

    if typ == 'throughput':
        analyze_logs_throughput(path)
    elif typ == 'response-time':
        analyze_logs_response_time(path)

def analyze_logs_throughput(path):
    total_mean = [0] * 90
    total_std =  [0]*  90
    count=0
    # get all folders name inside the path folder:load-1 for instance 	 	
    subdirs = [p for p in os.listdir(path) if os.path.isdir(os.path.join(path, p))]
    for dname in subdirs:
        # analyze inside of each folder which has files inside 
        (mean, std) = analyze_throughput(os.path.abspath(os.path.join(path, dname)))
        total_mean=map(sum, zip(mean[5:95],total_mean))
        total_std=map(sum, zip(std[5:95], total_std))
        count = count+1
        
    meanfile=open("throughput_means.txt",'w')      
    stdfile=open("throughput_std.txt",  'w')
    for item in total_mean:
        item=(item/count)*40 # 40 clients
        meanfile.write("%s\n" % item)      
    for item in total_std:
        item=(item/count)*40 
        stdfile.write("%s\n" % item)      


def analyze_logs_response_time(path):
    count=0
    total_mean = [0] * 90
    total_std =  [0]*  90
    subdirs = [p for p in os.listdir(path) if os.path.isdir(os.path.join(path, p))]
    for dname in subdirs:
        (mean, std) = analyze_response_time(os.path.abspath(os.path.join(path, dname)))
        total_mean=map(sum, zip(mean[0:90],total_mean))
        total_std=map(sum, zip(std[0:90], total_std))
        count = count+1
    # get the average to generate the final time series
    meanfile=open("response_time_means.txt",'w')      
    stdfile=open("response_time_std.txt",  'w')
    for item in total_mean:
        item=item/count
        meanfile.write("%s\n" % item)      
    for item in total_std:
        item=item/count
        stdfile.write("%s\n" % item)      
    

def analyze_throughput(path):
    # get the name of the folder 
    basename = os.path.basename(path)
    if len(basename.split('-')) == 3:
        operation, strategy, bench_id = basename.split('-')
    else:
        operation, _, strategy, bench_id = basename.split('-')
    print('starting to analyze dataset with {} {} {}'
               .format(operation, strategy, bench_id))

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
                        if num_secs == 20:
                            intervals.append(current_count)
                            current_count = 0
                            num_secs = 0
                print(fname)
                print(len(intervals))
                if(len(intervals) > 20):
                   file_intervals.append(intervals) # list of the list of the list
    means = []
    pstdevs = []
    for i in zip(*file_intervals): # joins all the block intervals together
        a =  [x / 20.0 for x in i]  
        mean = statistics.mean(a)
        pstdev = statistics.pstdev(a)
        means.append(mean)
        pstdevs.append(pstdev)
    return (means, pstdevs)



# this function analyze one subfolder: for instance MPeek-response-time-1: inside are files
def analyze_response_time(path):
    basename = os.path.basename(path)
    if len(basename.split('-')) == 3:
        operation, strategy, bench_id = basename.split('-')
    else:
        operation, _, strategy, bench_id = basename.split('-')
    print('starting to analyze dataset with {} {} {}'
               .format(operation, strategy, bench_id))

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
                        current_interval.append(int(row.strip())/1000.0)
                    else:
                        num_secs = num_secs+1 # now we are one block more 
                        if num_secs == 20:
                            intervals.append(current_interval)
                            current_interval = []
                            num_secs = 0
                if(len(intervals)>20):
                    file_intervals.append(intervals)
    means = []
    pstdevs = []
    for i in zip(*file_intervals):
        d = [item for sublist in i for item in sublist]
        if(len(d) > 0):   
           mean = statistics.mean(d)
           pstdev = statistics.pstdev(d, mean)
           means.append(mean)
           pstdevs.append(pstdev) 	
    return (means, pstdevs)


if __name__ == '__main__':
    if not len(sys.argv) == 3:
        logging.error("Usage: generate_time_series.py  type: throughput or response-time folder_with_measurements")
        sys.exit(1)
    analyze_logs(sys.argv[1], sys.argv[2])
