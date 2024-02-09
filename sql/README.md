# Introduction
The purpose of the RDBMS and SQL project is to build a database for a club which consist of information like members, booking and available facilities. Docker, PostgreSQL, and Git was utilized to implement this project.  Docker was used to create a psql instance, SQL was used to create, modify and query the database. Git was used for version control and GitHub to manage the source code.

# Quick Start
This is a single user project as its purpose is to improve SQL and RDBMS knowledge and skills. The following steps were taken to create and manage the database
1. psql_docker.sh: create/start/stop psql docker container
2. clubdata.sql: create schema/database/tables 
3. queries.sql: insert values into/query tables

# SQL Queries 

### Table Setup (DDL)
#### Create Tables
```sql
-- Create table members if it doesn't already exist in cd schema
create table if not exists cd.members (
    memid integer not null,
    surname varchar(200) not null,
    firstname varchar(200) not null,
    address varchar(300) not null,
    zipcode integer not null,
    telephone varchar(20) not null,
    recommendedby integer,
    joindate timestamp not null,
    constraint memid_pk PRIMARY KEY (memid),
    constraint fk_members_recommendedby FOREIGN KEY (recommendedby) references cd.members(memid) 
    ON delete set null
);


-- Create table facilities if it doesn't already exist in cd schema
create table if not exists cd.facilities (
    facid integer not null,
    name varchar(100) not null,
    membercost numeric not null,
    guestcost numeric not null,
    initialoutlay numeric not null,
    montlymaintenance numeric not null,
    constraint facid_pk PRIMARY KEY (facid)
);


-- Create table bookings if it doesn't already exist in cd schema
create table if not exists cd.bookings (
    bookid integer not null,
    facid integer not null,
    memid integer not null,
    starttime timestamp not null,
    slots integer not null,
    constraint bookid_pk PRIMARY KEY (bookid),
    constraint fk_facid FOREIGN KEY (facid) references cd.facilities(facid),
    constraint fk_memid FOREIGN KEY (memid) references cd.members (memid)
);
```


### Table Modification

#### Insertion 
###### Questions 1: Add a new facility into the facilities table. Use the following values: facid: 9, Name: 'Spa', membercost: 20, guestcost: 30, initialoutlay: 100000, monthlymaintenance: 800
```sql
-- insert new row
insert into cd.facilities(
   facid, name, membercost, guestcost, initialoutlay, monthlymaintenance
)
values (9, 'Spa', 20, 30, 100000, 800);
```
###### Questions 2: Add the spa to the facilities table again, but this time, automatically generate the value for the next facid, rather than specifying it as a constant. Use the following values for everything else: Name: 'Spa', membercost: 20, guestcost: 30, initialoutlay: 100000, monthlymaintenance: 800.

```sql
-- insert by setting id as 1 more than the largest id value
insert into cd.facilities(
   facid, name, membercost, guestcost, initialoutlay, monthlymaintenance
)
values ((select max(facid) from cd.facilities)+1, 'Spa', 20, 30, 100000, 800);
```

#### Update 
###### Questions 3: Alter the initial outlay value for the second tennis court from 10000 to 8000
```sql
-- update value at a particular instance
update 
  cd.facilities 
set 
  initialoutlay = 10000 
where 
  facid = 1;
```
###### Questions 4: Alter the price of the second tennis court so that it costs 10% more than the first one. Try to do this without using constant values for the prices, so that we can reuse the statement if we want to.
```sql
-- update values with selection
update 
  cd.facilities 
set 
  membercost = (
    select 
      membercost 
    from 
      cd.facilities 
    where 
      facid = 0
  )* 1.1, 
  guestcost = (
    select 
      guestcost 
    from 
      cd.facilities 
    where 
      facid = 0
  )* 1.1 
where 
  facid = 1;
```

#### Deletion 
###### Questions 5: Delete all bookings from the cd.bookings table
```sql
-- delete all rows from bookings
delete from cd.bookings;
```
###### Questions 6: Remove member 37, who has never made a booking, from our database
```sql
-- delete values based on some condition
delete from cd.members
where memid = 37;
```


### Querying Data

#### SQL Basics
###### Questions 7: How can you produce a list of facilities that charge a fee to members, and that fee is less than 1/50th of the monthly maintenance cost? Return the facid, facility name, member cost, and monthly maintenance of the facilities in question.
```sql
-- where 
select 
  facid, 
  name, 
  membercost, 
  monthlymaintenance 
from 
  cd.facilities 
where 
  membercost < monthlymaintenance / 50 
  and membercost > 0;
```
###### Questions 8: How can you produce a list of all facilities with the word 'Tennis' in their name?
```sql
select 
  * 
from 
  cd.facilities 
where 
  name like '%Tennis%';```
###### Questions 9: How can you retrieve the details of facilities with ID 1 and 5? Try to do it without using the OR operator.
````sql
select 
  * 
from 
  cd.facilities 
where 
  facid in (1, 5);
```
###### Questions 10: How can you produce a list of members who joined after the start of September 2012? Return the memid, surname, firstname, and joindate of the members in question.
```sql
-- date
select 
  memid, 
  surname, 
  firstname, 
  joindate 
from 
  cd.members 
where 
  joindate >= '2012-09-01';
```
###### Questions 11: You, for some reason, want a combined list of all surnames and all facility names.
```sql
-- union
select 
  DISTINCT m.surname 
from 
  cd.members as m 
UNION ALL 
select 
  DISTINCT f.name 
from 
  cd.facilities as f;
```

#### Joins
###### Questions 12: How can you produce a list of the start times for bookings by members named 'David Farrell'?
```sql
-- nested select
select 
  starttime 
from 
  cd.bookings 
where 
  memid = (
    select 
      memid 
    from 
      cd.members 
    where 
      firstname = 'David' 
      and surname = 'Farrell'
  );
```
###### Questions 13: How can you produce a list of the start times for bookings for tennis courts, for the date '2012-09-21'? Return a list of start time and facility name pairings, ordered by the time.
```sql
-- inner join with condition
select 
  b.starttime as start, 
  f.name as name 
from 
  cd.facilities f 
  inner join cd.bookings b on f.facid = b.facid 
where 
  f.name like 'Tennis%' 
  and DATE(b.starttime) = '2012-09-21' 
order by 
  b.starttime;
```
###### Questions 14: How can you output a list of all members, including the individual who recommended them (if any)? Ensure that results are ordered by (surname, firstname).
```sql
-- left join
select 
  mems.firstname as memfname, 
  mems.surname as memsname, 
  recs.firstname as recfname, 
  recs.surname as recsname 
from 
  cd.members mems 
  left outer join cd.members recs on recs.memid = mems.recommendedby 
order by 
  memsname, 
  memfname;
```
###### Questions 15: How can you output a list of all members who have recommended another member? Ensure that there are no duplicates in the list, and that results are ordered by (surname, firstname).
```sql
-- inner join with distinct
select 
  distinct c2.firstname, 
  c2.surname 
FROM 
  cd.members c1 
  INNER JOIN cd.members c2 ON c1.recommendedby = c2.memid 
order by 
  c2.surname, 
  c2.firstname;
```
###### Questions 16: How can you output a list of all members, including the individual who recommended them (if any), without using any joins? Ensure that there are no duplicates in the list, and that each firstname + surname pairing is formatted as a column and ordered.
```sql
-- nested select, with concat/distinct 
select 
  distinct m.firstname || ' ' || m.surname as member, 
  (
    select 
      r.firstname || ' ' || r.surname as recommender 
    from 
      cd.members r 
    where 
      r.memid = m.recommendedby
  ) 
from 
  cd.members m 
order by 
  member;
```

#### Aggregation
###### Questions 17: Produce a count of the number of recommendations each member has made. Order by member ID.
```sql
-- count
select 
  recommendedby, 
  count(*) 
from 
  cd.members 
where 
  recommendedby is not null 
group by 
  recommendedby 
order by 
  recommendedby;
```
###### Questions 18: Produce a list of the total number of slots booked per facility. For now, just produce an output table consisting of facility id and slots, sorted by facility id.
```sql
-- sum
select 
  f.facid, 
  sum(b.slots) as "Total Slots" 
from 
  cd.facilities f 
  left join cd.bookings b on f.facid = b.facid 
group by 
  f.facid 
order by 
  f.facid;
```
###### Questions 19: Produce a list of the total number of slots booked per facility in the month of September 2012. Produce an output table consisting of facility id and slots, sorted by the number of slots.
```sql
-- sum with condition
select 
  facid, 
  sum(slots) as "Total Slots" 
from 
  cd.bookings 
where 
  date(starttime) >= '2012-09-01' 
  and date(starttime) <= '2012-09-30' 
group by 
  facid 
order by 
  sum(slots);
```
###### Questions 20: Produce a list of the total number of slots booked per facility per month in the year of 2012. Produce an output table consisting of facility id and slots, sorted by the id and month.
```sql
-- extract
select 
  facid, 
  extract(
    month 
    from 
      starttime
  ) as month, 
  sum(slots) 
from 
  cd.bookings 
where 
  extract(
    year 
    from 
      starttime
  ) = 2012 
group by 
  facid, 
  month 
order by 
  facid, 
  month;
```
###### Questions 21: Find the total number of members (including guests) who have made at least one booking.
```sql
-- count distinct
select count(distinct memid)
from cd.bookings;
```
###### Questions 22: Produce a list of each member name, id, and their first booking after September 1st 2012. Order by member ID.
```sql
-- min
select 
  m.surname, 
  m.firstname, 
  m.memid, 
  min(b.starttime) as starttime 
from 
  cd.members m 
  inner join cd.bookings b on m.memid = b.memid 
where 
  date(b.starttime) >= '2012-09-01' 
group by 
  m.memid 
order by 
  m.memid;
```
###### Questions 23: Produce a list of member names, with each row containing the total member count. Order by join date, and include guest members.
```sql
-- Window Functions: over all
select 
  count(*) over(), 
  firstname, 
  surname 
from 
  cd.members 
group by 
  memid 
order by 
  joindate;
```
###### Questions 24: Produce a monotonically increasing numbered list of members (including guests), ordered by their date of joining. Remember that member IDs are not guaranteed to be sequential.
```sql
-- Window Functions: row_number
select 
  row_number() over(
    order by 
      joindate
  ) as row_number, 
  firstname, 
  surname 
from 
  cd.members 
order by 
  joindate;
```
###### Questions 25: Output the facility id that has the highest number of slots booked. Ensure that in the event of a tie, all tieing results get output.
```sql
-- limit
select 
  facid, 
  sum(slots) as total 
from 
  cd.bookings 
group by 
  facid 
order by 
  total desc 
limit 1;
```

#### String
###### Questions 26: Output the names of all members, formatted as 'Surname, Firstname'
```sql
--concat
select 
  concat(m.surname, ', ', m.firstname) as name 
from 
  cd.members m;

-- concat, another form
select 
  surname || ', ' || firstname as name 
from 
  cd.members
```
###### Questions 27: You've noticed that the club's member table has telephone numbers with very inconsistent formatting. You'd like to find all the telephone numbers that contain parentheses, returning the member ID and telephone number sorted by member ID.
```sql
--like
select 
  memid, 
  telephone 
from 
  cd.members 
where 
  telephone like '(%' 
order by 
  memid;

-- like, another form
select 
  memid, 
  telephone 
from 
  cd.members 
where 
  telephone ~ '[()]';
```
###### Questions 28: You'd like to produce a count of how many members you have whose surname starts with each letter of the alphabet. Sort by the letter, and don't worry about printing out a letter if the count is 0.
```sql
-- substring
select 
  substring(surname, 1, 1) as letter, 
  count(surname) 
from 
  cd.members 
group by 
  letter 
order by 
  letter;
```
# Testing
Modifications were tested using select statements to ensure the expected changes were made. View tables visually displayed whether queries extracted the correct information.

# Deployment
This project uses Docker for the psql instance, pgadmin to connect to the database and complete queries, and GitHub for source code management. 

