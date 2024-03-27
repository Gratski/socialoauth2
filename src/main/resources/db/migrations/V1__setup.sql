create table "users" (
 id serial not null primary key,
 "name" varchar(150) not null,
 email varchar(150) not null,
 image_url varchar(150) not null,
 email_verified boolean not null default false,
 "password" varchar(100),
 provider varchar(50) not null,
 provider_id varchar(100)
);