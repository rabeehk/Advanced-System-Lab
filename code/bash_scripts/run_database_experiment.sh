#!/bin/bash

key=rabeeh.pem
ssh_db_host=ubuntu@52.28.105.140
DATABASE_URL="jdbc:postgresql://52.28.105.140:5432/asl_db?user=postgres&password=`cat PW.txt`"
db_ad="/home/ubuntu/postgres/bin/"

function reset_db {
    ssh -i $key $ssh_db_host  "killall java"
    ssh -i $key $ssh_db_host "PGPASSWORD=`cat PW.txt` $db_ad/dropdb -U postgres asl_db -p 5432 -h /home/ubuntu "
    ssh -i $key $ssh_db_host "PGPASSWORD=`cat PW.txt` $db_ad/createdb -O postgres asl_db -p 5432 -h /home/ubuntu"
    ssh -i $key $ssh_db_host "cd ~/asl &&  PGPASSWORD=`cat PW.txt` $db_ad/psql -U postgres -d asl_db -f create_tables.sql -p 5432 -h /home/ubuntu"
    ssh -i $key $ssh_db_host "cd ~/asl &&  PGPASSWORD=`cat PW.txt` $db_ad/psql -U postgres -d asl_db -f create_stored_procedures.sql -p 5432 -h /home/ubuntu"
    ssh -i $key $ssh_db_host "cd ~/asl &&  PGPASSWORD=`cat PW.txt` $db_ad/psql -U postgres -d asl_db -f create_indices.sql -p 5432 -h /home/ubuntu"
}

function run_db_bench {
   ssh -i $key $ssh_db_host  "killall java;"
   java -jar db-bench.jar $1 $2 $3 $4 	
}

function run_comb_bench {
   ssh -i $key $ssh_db_host  "killall java;"
   java -jar db-mload-bench.jar $1 $2 $3 $4	
}

for count in 4 8 16 24 36 
do
    reset_db   
    run_db_bench $count $count "peek" $DATABASE_URL
done


for count in 4 8 16 24 36 
do
    reset_db   
    run_db_bench $count $count "send" $DATABASE_URL
done

for count in 1 2 4 6 9 
do
    reset_db   
    run_comb_bench `expr 4 \* $count` $count $DATABASE_URL
done


