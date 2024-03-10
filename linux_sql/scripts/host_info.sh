#!/bin/bash

sql_host=$1
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
lscpu_out=$(lscpu)

# get hardware info
cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | grep -e "^Architecture" | awk '{print $2}')
cpu_model=$(echo "$lscpu_out" | grep -e "^Model name" | awk '{for(i=3;i<=NF;++i)print $i}'| xargs)
cpu_mhz=$(echo "$lscpu_out" | grep -e "^CPU MHz" | awk '{for(i=3;i<=NF;++i)print $i}'| xargs)
l2_cache=$(echo "$lscpu_out" | grep -e "^L2 cache" | awk '{print $3}' | sed 's/[^0-9]*//g')
total_mem=$(echo "$vmstat_mb" | tail -1 | awk '{print $4}')

# current date and time
timestamp=$(date +"%Y-%m-%d %T")

# psql command:insert server usage data into host usage table
insert_stmt="INSERT INTO host_info(hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, "timestamp", total_mem) VALUES('$hostname', '$cpu_number','$cpu_architecture', '$cpu_model', '$cpu_mhz', '$l2_cache', '$timestamp', '$total_mem')";

# setup env var for psql cmd
export PGPASSWORD=$psql_password
# insert date into db
psql -h "$sql_host" -p "$psql_port" -d "$db_name" -U "$psql_user" -c "$insert_stmt"
exit $?
