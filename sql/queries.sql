-- Creating Tables --
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


--Modifying Data--
-- insert values
insert into cd.facilities(
   facid, name, membercost, guestcost, initialoutlay, monthlymaintenance
)
values (9, 'Spa', 20, 30, 100000, 800);

-- insert without a specific id
insert into cd.facilities(
   facid, name, membercost, guestcost, initialoutlay, monthlymaintenance
)
values ((select max(facid) from cd.facilities)+1, 'Spa', 20, 30, 100000, 800);

-- update value
update 
  cd.facilities 
set 
  initialoutlay = 10000 
where 
  facid = 1;

-- update values with another select
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


-- delete all values
delete from cd.bookings;

-- delete values based on some condition
delete from cd.members
where memid = 37;


-- Basics --
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

-- like
select 
  * 
from 
  cd.facilities 
where 
  name like '%Tennis%';

-- in
select 
  * 
from 
  cd.facilities 
where 
  facid in (1, 5);

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


-- union, distinct
select 
  DISTINCT m.surname 
from 
  cd.members as m 
UNION ALL 
select 
  DISTINCT f.name 
from 
  cd.facilities as f;


-- Join --
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


-- inner join
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

-- inner join distinct
select 
  distinct c2.firstname, 
  c2.surname 
FROM 
  cd.members c1 
  INNER JOIN cd.members c2 ON c1.recommendedby = c2.memid 
order by 
  c2.surname, 
  c2.firstname;

-- select within select 
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

-- Aggregation --
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

-- count
select count(distinct memid)
from cd.bookings;

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

-- window functions
-- over all
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

-- row_number
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

-- String --
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


