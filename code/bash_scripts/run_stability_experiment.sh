#!/bin/bash

key=rabeeh.pem 
ssh_db_host=ubuntu@52.29.25.245

server_host1="ubuntu@52.29.40.194"
server_inet1="172.31.27.69"

server_host2="ubuntu@52.29.45.206"
server_inet2="172.31.21.205"

DATABASE_URL="jdbc:postgresql://52.29.25.245:5432/asl_db?user=postgres&password=`cat PW.txt`"    
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

reset_db	
reset_server1
reset_server2
java -jar stability_bench.jar 40 10 $server_inet1 4444 $server_inet2 7777  $DATABASE_URL 





