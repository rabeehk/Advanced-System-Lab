#!/bin/bash

key=rabeeh.pem 
ssh_db_host=ubuntu@52.28.197.168

server_host1="ubuntu@52.29.105.92"
server_inet1="172.31.27.69"
server_host2="ubuntu@52.29.45.206"
server_inet2="172.31.21.205"
server_host3="ubuntu@52.29.45.206"
server_inet3="172.31.21.205"
DATABASE_URL="jdbc:postgresql://52.28.197.168:5432/asl_db?user=postgres&password=`cat PW.txt`"
db_ad="/home/ubuntu/postgres/bin/"

function reset_db {
    ssh -i $key $ssh_db_host  "killall java"
    ssh -i $key $ssh_db_host "PGPASSWORD=`cat PW.txt` $db_ad/dropdb -U postgres asl_db -p 5432 -h /home/ubuntu "
    ssh -i $key $ssh_db_host "PGPASSWORD=`cat PW.txt` $db_ad/createdb -O postgres asl_db -p 5432 -h /home/ubuntu"
    ssh -i $key $ssh_db_host "cd ~/asl &&  PGPASSWORD=`cat PW.txt` $db_ad/psql -U postgres -d asl_db -f create_tables.sql -p 5432 -h /home/ubuntu"
    ssh -i $key $ssh_db_host "cd ~/asl &&  PGPASSWORD=`cat PW.txt` $db_ad/psql -U postgres -d asl_db -f create_stored_procedures.sql -p 5432 -h /home/ubuntu"
    ssh -i $key $ssh_db_host "cd ~/asl &&  PGPASSWORD=`cat PW.txt` $db_ad/psql -U postgres -d asl_db -f create_indices.sql -p 5432 -h /home/ubuntu"
}

# 96 clients
# run 96 clients for 1 middleware, 6 database connection: id = 16
echo "running 96 clients for 1 middleware and 6 database connections"
reset_db	
ssh -i $key $server_host1 "killall java"
ssh -i $key $server_host1 'screen -dm java -jar /home/ubuntu/asl/server_main_6.jar 1111 '"$DATABASE_URL"
sleep 2
java -jar system_bench_1.jar 16 24 $server_inet1 1111 $DATABASE_URL 


# run 96 clients for 1 middleware, 10 database connection: id = 110
echo "running 96 clients for 1 middleware and 10 database connections"
reset_db	
ssh -i $key $server_host1 "killall java"
ssh -i $key $server_host1 'screen -dm java -jar /home/ubuntu/asl/server_main_10.jar 1111 '"$DATABASE_URL"
sleep 2
java -jar system_bench_1.jar 110 24 $server_inet1 1111 $DATABASE_URL 

exit 0

# run 96 clients for 2 middleware and 6 database connections: id=26 
echo "running 96 clients for 2 middleware and 6 database connections"
reset_db	
ssh -i $key $server_host1 "killall java"
ssh -i $key $server_host1 'screen -dm java -jar /home/ubuntu/asl/server_main_6.jar 1111 '"$DATABASE_URL"
sleep 2
ssh -i $key $server_host2 "killall java"
ssh -i $key $server_host2 'screen -dm java -jar /home/ubuntu/asl/server_main_6.jar 2222 '"$DATABASE_URL"
sleep 2
java -jar system_bench_2.jar 26 12 $server_inet1 1111 $server_inet2 2222 $DATABASE_URL 


# run 96 clients for 2 middleware and 10 database connections: id=210 
echo "running 96 clients for 2 middleware and 24 database connections"
reset_db	
ssh -i $key $server_host1 "killall java"
ssh -i $key $server_host1 'screen -dm java -jar /home/ubuntu/asl/server_main_10.jar 1111 '"$DATABASE_URL"
sleep 2
ssh -i $key $server_host2 "killall java"
ssh -i $key $server_host2 'screen -dm java -jar /home/ubuntu/asl/server_main_10.jar 2222 '"$DATABASE_URL"
sleep 2
java -jar system_bench_2.jar 210 12 $server_inet1 1111 $server_inet2 2222 $DATABASE_URL 


# run 96 clients for 3 middleware and 6 database connections: id=36 
echo "running 96 clients for 3 middleware and 12 database connections"
reset_db	
ssh -i $key $server_host1 "killall java"
ssh -i $key $server_host1 'screen -dm java -jar /home/ubuntu/asl/server_main_6.jar 1111 '"$DATABASE_URL"
sleep 2
ssh -i $key $server_host2 "killall java"
ssh -i $key $server_host2 'screen -dm java -jar /home/ubuntu/asl/server_main_6.jar 2222 '"$DATABASE_URL"
sleep 2
ssh -i $key $server_host3 "killall java"
ssh -i $key $server_host3 'screen -dm java -jar /home/ubuntu/asl/server_main_6.jar 3333 '"$DATABASE_URL"
sleep 2
java -jar system_bench_3.jar 36 8 $server_inet1 1111 $server_inet2 2222 $server_inet3 3333 $DATABASE_URL 


# run 96 clients for 3 middleware and 10 database connections: id=310 
echo "running 96 clients for 3 middleware and 10 database connections"
reset_db	
ssh -i $key $server_host1 "killall java"
ssh -i $key $server_host1 'screen -dm java -jar /home/ubuntu/asl/server_main_10.jar 1111 '"$DATABASE_URL"
sleep 2
ssh -i $key $server_host2 "killall java"
ssh -i $key $server_host2 'screen -dm java -jar /home/ubuntu/asl/server_main_10.jar 2222 '"$DATABASE_URL"
sleep 2
ssh -i $key $server_host3 "killall java"
ssh -i $key $server_host3 'screen -dm java -jar /home/ubuntu/asl/server_main_10.jar 2222 '"$DATABASE_URL"
sleep 2
java -jar system_bench_3.jar 310 8 $server_inet1 1111 $server_inet2 2222 $server_inet3 3333 $DATABASE_URL 












