insert into ENTERTAINMENT_ACTIVITIES values
    (1,	'Description for football',	'Football'),
    (2,	'Description for basket',	'Basket'),
    (3,	'Description for handball',	'Handball'),
    (4,	'Description for tennis',	'Tennis');

insert into ROLES values
    (1,	'The users with this role can manage the app',	'ADMIN'),
    (2,	'The users with this role can manage their entertainment places',	'OWNER'),
    (3,	'The users with this role can rent entertainment places',	'RENTER');

insert into USERS values
    (1,	'$2a$10$BtzkihyDmrYyIGNx1ta3YOeW/zIrW17jZM5HeF1d9M4xQ6xGEIOc.',	'alex',	1),
    (2,	'$2a$10$X9zxgjp2Q6xqTIAO6i4pr.cR3gOHKrNLIwgqPbnTvKyJcX876f.Ia',	'iulia',	2),
    (3,	'$2a$10$Ni/1/tWrL5Cx5imft2WcNulOjTyd1M/AovZn6dCmsRv6ySxwplEvS',	'tudor',	3);

insert into USERS_ROLES values
    (1,	1),
    (1,	2),
    (1,	3),
    (2,	2),
    (2,	3),
    (3,	3);


insert into USER_DETAILS values
    (1,	null,	'Alex',	'Sabou',	null,	1),
    (2,	null,	'Iulia',	'Ioana',	null,	2),
    (3,	null,	'Tudor',	'Movila',	null,	3);

insert into ENTERTAINMENT_PLACES values
    (1,	'Description1',	'Name1',	null,	1,	2),
    (2,	'Description2',	'Name2',	null,	2,	2);

insert into ADDRESSES values
    (1,	'City1',	'County1',	12,	'Street1',	null),
    (2,	null,	null,	null,	null,	null);

insert into ENTERTAINMENT_ACTIVITIES_PLACES values
    (1,	1,	12,	50.0)
    (1,	2,	10,	60.0)
    (4,	1,	4,	40.0)
    (3,	1,	10,	65.0)
    (4,	2,	4,	40.0);

insert into RESERVATIONS values
    (1,	2021-03-21,	12,	1,	1,	3),
    (2,	2021-03-21,	13,	1,	1,	3);
