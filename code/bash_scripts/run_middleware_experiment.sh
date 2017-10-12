#!/bin/bash

key=rabeeh.pem 
server_ip="52.29.12.249"
server_host=ubuntu@$server_ip
server_inet="172.31.27.69"
middleware_host=ubuntu@52.29.26.22
DATABASE_URL="jdbc:postgresql://52.29.31.11:5432/asl_db?user=postgres&password=`cat PW.txt`"
db_ad="/home/ubuntu/postgres/bin/"
port=7777

# it runs the server 
function reset_server {
    ssh -i $key $server_host "killall java"
    ssh -i $key $server_host 'screen -dm java -jar /home/ubuntu/asl/server_main.jar 7777 '"$DATABASE_URL"
    sleep 2
}

# it runs the benchmark
function do_benchmark_run {
   killall java
   java -jar middleware_bench.jar $1 $2 $3 $4 $5 
}


for count in 8 16 32 64 # number of clients
do
    reset_server
    do_benchmark_run $count $count "mecho" $server_inet $port
done

