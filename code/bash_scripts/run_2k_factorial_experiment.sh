#!/bin/bash

key=rabeeh.pem 
ssh_db_host="ubuntu@52.29.57.219"
middleware1_ip="52.29.36.113"
middleware1_inet="172.31.19.204"
port1=1111

middleware2_ip=52.29.88.104
middleware2_inet=52.29.88.104
port2=7777

DATABASE_URL="jdbc:postgresql://52.29.57.219:5432/asl_db?user=postgres&password=`cat PW.txt`"
db_ad="/home/ubuntu/postgres/bin/"

# reset database
function reset_db {
    ssh -i $key $ssh_db_host  "killall java"
    ssh -i $key $ssh_db_host "PGPASSWORD=`cat PW.txt` $db_ad/dropdb -U postgres asl_db -p 5432 -h /home/ubuntu "
    ssh -i $key $ssh_db_host "PGPASSWORD=`cat PW.txt` $db_ad/createdb -O postgres asl_db -p 5432 -h /home/ubuntu"
    ssh -i $key $ssh_db_host "cd ~/asl &&  PGPASSWORD=`cat PW.txt` $db_ad/psql -U postgres -d asl_db -f create_tables.sql -p 5432 -h /home/ubuntu"
    ssh -i $key $ssh_db_host "cd ~/asl &&  PGPASSWORD=`cat PW.txt` $db_ad/psql -U postgres -d asl_db -f create_stored_procedures.sql -p 5432 -h /home/ubuntu"
    ssh -i $key $ssh_db_host "cd ~/asl &&  PGPASSWORD=`cat PW.txt` $db_ad/psql -U postgres -d asl_db -f create_indices.sql -p 5432 -h /home/ubuntu"
}

# reset middleware 1
function reset_middleware1 {
    ssh -i $key ubuntu@$middleware1_ip "killall java"
    ssh -i $key ubuntu@$middleware1_ip 'screen -dm java -jar /home/ubuntu/asl/server_main.jar'"$port1""$DATABASE_URL" 
    sleep 2
}


# reset middleware 2
function reset_middleware2 {
    ssh -i $key ubuntu@$middleware2_ip "killall java"
    ssh -i $key ubuntu@$middleware2_ip 'screen -dm java -jar /home/ubuntu/asl/server_main.jar'"$port2""$DATABASE_URL" 
    sleep 2
}


# it runs the experiment
function run_experiment {
   killall java
   java -jar system_bench.jar $1 $2 $3 $4 $5  $DATABASE_URL
}

# expr 1: length=1200 middleware=1 client=32 
echo "running expr-1"
for count in 1 2 3
do
reset_db
reset_middleware1
run_experiment "expr-1$count" 8  $middleware1_inet $port1 1200
done

# expr 2: length=200 middleware=1 client=32
echo "running expr-2"
for count in 1 2 3 
do
reset_db
reset_middleware1
run_experiment "expr-2$count" 8  $middleware1_inet $port1 200
done


# expr 5: length=1200 middleware=1 client=64 
echo "running expr-5"
for count in 1 2 3 
do
reset_db
reset_middleware1
run_experiment "expr-5$count" 16  $middleware1_inet $port1 1200
done

# expr 6: length=200 middleware=1 client=64
echo "running expr-6"
for count in 1 2 3 
do
reset_db
reset_middleware1
run_experiment "expr-6$count" 16  $middleware1_inet $port1 200
done


# expr 3: length=1200 middleware=2 client=32 

# expr 4: length=200 middleware=2 client=32 

# expr 7: length=1200 middleware=2 client=64

# expr 8: length=200 middleware=2 client=64 






























































