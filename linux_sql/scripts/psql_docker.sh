#!/bin/bash

# Capture CLI arguments
cmd=$1
db_username=$2
db_password=$3

# Todo - Start docker
# Make sure you understand the double pipe operator
sudo systemctl status docker | head -n 5 || sudo systemctl start docker 

# Check container status (try the following cmds on terminal)
docker container inspect jrvs-psql | head -n 26
container_status=$?

# User switch case to handle create|stop|start opetions
case $cmd in 
  create)
  
  # Check if the container is already created
  if [ $container_status -eq 0 ]; then
	echo 'Container already exists'
	exit 1	
  fi

  # Check # of CLI arguments
  if [ $# -ne 3 ]; then
    echo 'Create requires username and password'
    exit 1
  fi
  
  #Todo- Create container
  docker volume create pgdata
  #Todo-  Start the container
  docker run --name jrvs-psql -e POSTGRES_USERNAME="$db_username" -e POSTGRES_PASSWORD="$db_password" -d -v pgdata:/var/lib/postgresql/data -p  5432:5432 postgres:9.6-alpine

  # Make sure you understand what's `$?`
  exit $?
  ;;

  start|stop) 
  #Todo- Check instance status; exit 1 if container has not been created
  if [ $container_status -ne 0 ]; then
    echo 'Failed to create container'
    exit 1 
  fi

  # Start or stop the container
  docker container $cmd jrvs-psql
  echo 'Request Completed'
  exit $?
  ;;	
  
  *)
  echo 'Illegal command'
  echo 'Commands: start|stop|create'
  exit 1
  ;;
esac 

