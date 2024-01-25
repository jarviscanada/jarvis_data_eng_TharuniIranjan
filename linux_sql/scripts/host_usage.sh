#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ "$#" -ne 5 ]; then
  echo "Illegal number of parameters"
  exit 1
fi 

# save machine stats and hostname in mb
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

# get hardware specs
memory_free=$(echo "$vmstat_mb" | awk '{print $4}' | tail -n1 | xargs)
cpu_idle=$(echo "$vmstat_mb" | awk '{print $15}' | tr "id" " "| xargs)
cpu_kernel=$(echo "$vmstat_mb" | awk '{print $14}' | tr "sy" " " | xargs)
disk_io=$(vmstat -d | awk '{print $10}' | tail -n1 | xargs)
disk_available=$(df -BM / | tail -1 | awk -v col="4" '{print $col}'|sed 's/[^0-9]*//g')

# current date and time
timestamp=$(date +"%Y-%m-%d %T")

# subquery to find matching id in host_info table
# host_id="(SELECT id FROM host_info WHERE hostname=`$hostname`)";
# setup env var for psql cmd
export PGPASSWORD=$psql_password
get_id="(SELECT id FROM host_info WHERE hostname='$hostname')";
host_id=$(psql -h "$psql_host" -p "$psql_port" -d "$db_name" -U "$psql_user" -t -c "$get_id")


# psql command:insert server usage data into host usage table
insert_stmt="INSERT INTO host_usage("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES ('$timestamp', '$host_id', '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available')";

# setup env var for psql cmd
export PGPASSWORD=$psql_password
# insert date into db
psql -h "$psql_host" -p "$psql_port" -d "$db_name" -U "$psql_user" -c "$insert_stmt"
exit $?
