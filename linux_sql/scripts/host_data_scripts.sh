# save hostname as a variable
hostname=$(hostname -f)

# save the number of CPUs to a variable
lscpu_out=`lscpu`
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
# tip: `xargs` is a trick to remove leading and trailing white spaces
# tip: the $2 is instructing awk to find the second field

cpu_architecture=$(lscpu | grep -e "^Architecture" | awk '{print $2}')
cpu_model=$(lscpu | grep -e "^Model name" | awk '{for(i=3;i<=NF;++i)print $i}'| xargs)
cpu_mhz=$(lscpu | grep -e "^CPU MHz" | awk '{for(i=3;i<=NF;++i)print $i}'| xargs)
l2_cache=$(lscpu | grep -e "^L2 cache" | awk '{print $3}' | sed 's/[^0-9]*//g')
total_mem=$(vmstat --unit M | tail -1 | awk '{print $4}')
#total_mem=unit M | tail -1 | awk '{print $4}')
timestamp=$(date +"%Y-%m-%d %T")
# timestamp= # current timestamp in `2019-11-26 14:40:19` format; use `date` cmd

# usage info
memory_free=$(vmstat --unit M | tail -1 | awk -v col="4" '{print $col}')
cpu_idle=$(vmstat | awk '{print $15}' | tr "id" " "| xargs)
cpu_kernel=$(vmstat | awk '{print $14}' | tr "sy" " " | xargs)
disk_io=$(vmstat --unit M -d | tail -1 | awk -v col="10" '{print $col}')
disk_available=$(df -BM / | tail -1 | awk -v col="4" '{print $col}'|sed 's/[^0-9]*//g')

