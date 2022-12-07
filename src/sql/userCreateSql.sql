drop table if exists users;
drop table if exists authorities;
drop table if exists ix_auth_username;

create table if not exists users(
	username varchar(50) not null primary key,
    password varchar(255) not null,
    enabled char(1) default '1');
    
create table if not exists authorities(
	username varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authroities_users
     foreign key(username) references users(username));
     
create unique index ix_auth_usernameusersusersusers
	on authorities (username, authority);
	
insert into users (username, password) values ('user1', 'password1');
insert into users (username, password) values ('user2', 'password2');

insert into authorities (username, authority) values ('user1', 'ROLE_USER');
insert into authorities (username, authority) values ('user2', 'ROLE_USER');   