#!/bin/bash

key=rabeeh.pem 
ssh_db_host=ubuntu@52.29.11.85
server_host1="ubuntu@52.29.125.151"
server_inet1="172.31.24.96"
server_host2="ubuntu@52.29.95.4"
server_inet2="172.31.21.205"
DATABASE_URL="jdbc:postgresql://52.29.11.85:5432/asl_db?user=postgres&password=`cat PW.txt`"
db_ad="/home/ubuntu/postgres/bin/"

function reset_db {
    ssh -i $key $ssh_db_host  "killall java"
    ssh -i $key $ssh_db_host "PGPASSWORD=`cat PW.txt` $db_ad/dropdb -U postgres asl_db -p 5432 -h /home/ubuntu "
    ssh -i $key $ssh_db_host "PGPASSWORD=`cat PW.txt` $db_ad/createdb -O postgres asl_db -p 5432 -h /home/ubuntu"
    ssh -i $key $ssh_db_host "cd ~/asl &&  PGPASSWORD=`cat PW.txt` $db_ad/psql -U postgres -d asl_db -f create_tables.sql -p 5432 -h /home/ubuntu"
    ssh -i $key $ssh_db_host "cd ~/asl &&  PGPASSWORD=`cat PW.txt` $db_ad/psql -U postgres -d asl_db -f create_stored_procedures.sql -p 5432 -h /home/ubuntu"
    ssh -i $key $ssh_db_host "cd ~/asl &&  PGPASSWORD=`cat PW.txt` $db_ad/psql -U postgres -d asl_db -f create_indices.sql -p 5432 -h /home/ubuntu"
}


# it runs the server 
function reset_server1 {
    ssh -i $key $server_host1 "killall java"
    ssh -i $key $server_host1 'screen -dm java -jar /home/ubuntu/asl/server_main.jar 4444 '"$DATABASE_URL"
    sleep 2
}

function reset_server2 {
    ssh -i $key $server_host2 "killall java"
    ssh -i $key $server_host2 'screen -dm java -jar /home/ubuntu/asl/server_main.jar 7777 '"$DATABASE_URL"
    sleep 2
}


# it runs the benchmark
function do_benchmark_run {
   killall java
   java -jar system_bench.jar $1 $2 $3 $4 $5 $6 $7 $8 
}


for count in 4 8 12 16 20 # number of clients
do
    reset_db	
    reset_server1
    reset_server2
    do_benchmark_run `expr 8 \* $count` $count $server_inet1 4444 $server_inet2 7777 200 $DATABASE_URL 
done

