create database internship;

create schema schema_internship;

create table schema_internship."user"
(
    id int not null
);

create unique index user_id_uindex
    on schema_internship."user" (id);

create table company
(
    id               uuid not null,
    "CompanyName"    text not null,
    "CompanyAddress" text not null,
    "CompanyInn"     int  not null,
    "CompanyKpp"     int,
    "CompanyOgrn"    int,
    "CompanyOwner"   text
);

create unique index company_id_uindex
    on company (id);


create table "user"
(
    id             uuid not null,
    "userName"     text not null,
    "userPassword" int  not null,
    "userRole"     text not null
);

create unique index user_id_uindex
    on "user" (id);

