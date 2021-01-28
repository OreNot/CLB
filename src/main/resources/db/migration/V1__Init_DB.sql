create sequence affiliation_sequence start 1 increment 1;
create sequence arm_sequence start 1 increment 1;
create sequence armkc_sequence start 1 increment 1;
create sequence armskzi_sequence start 1 increment 1;
create sequence armsysname_sequence start 1 increment 1;
create sequence armszi_sequence start 1 increment 1;
create sequence armtype_sequence start 1 increment 1;
create sequence av_sequence start 1 increment 1;
create sequence contact_sequence start 1 increment 1;
create sequence doctype_sequence start 1 increment 1;
create sequence document_sequence start 1 increment 1;
create sequence kc_sequence start 1 increment 1;
create sequence kctype_sequence start 1 increment 1;
create sequence org_sequence start 1 increment 1;
create sequence orgtype_sequence start 1 increment 1;
create sequence secclass_sequence start 1 increment 1;
create sequence skzi_sequence start 1 increment 1;
create sequence status_sequence start 1 increment 1;
create sequence sys_sequence start 1 increment 1;
create sequence systemname_sequence start 1 increment 1;
create sequence systemstatus_sequence start 1 increment 1;
create sequence systemtype_sequence start 1 increment 1;
create sequence szi_sequence start 1 increment 1;
create sequence trustlevel_sequence start 1 increment 1;
create sequence uc_sequence start 1 increment 1;
create sequence user_sequence start 2 increment 1;



create table affiliation (
    id int4 not null,
    name varchar(255),
    primary key (id)
                         );

create table antivirus (
    id int4 not null,
    av_name varchar(255) not null,
    av_version varchar(255) not null,
    primary key (id)
                       );


create table arm (
    id int4 not null,
    arm_name varchar(255),
    arm_number varchar(255),
    av_id int4,
    arm_type_id int4 not null,
    organization_id int4,
    uc_id int4,
     primary key (id)
                 );

create table armskzi (
    id int4 not null,
    arm_id int4,
    skzi_id int4,
    primary key (id)
                     );

create table armsystem_name (
    id int4 not null,
    arm_id int4,
    systemname_id int4,
    primary key (id)
                            );

create table armszi (
    id int4 not null,
    arm_id int4,
    szi_id int4,
    primary key (id)
                    );

create table armkc (
    id int4 not null,
    arm_id int4,
    kc_id int4,
    primary key (id)
                   );

create table armtype (
    id int4 not null,
    name varchar(255) not null,
    primary key (id)
                     );

create table contact (
    id int4 not null,
    email varchar(255),
    fio varchar(100) not null,
    phone_number varchar(255),
    position varchar(100),
    organization_id int4,
    primary key (id)
                     );

create table document (
    id int4 not null,
    contact_log varchar(1000),
    document_date varchar(255) not null,
    document_file_name varchar(255),
    document_name varchar(255),
    document_number varchar(255) not null,
    last_document_status_update_date varchar(255),
    last_document_status_update_status varchar(255),
    link varchar(255),
    osname varchar(255),
    parameters varchar(255),
    valid_until_date varchar(255) not null,
    av_id int4,
    arm_id int4,
    bank_id int4,
    document_type_id int4,
    kc_id int4,
    systemname_id int4,
    organization_id int4,
    uc_id int4,
    skzi_id int4,
    status_id int4,
    system_id int4,
    system_status_id int4,
    szi_id int4,
    trust_level_id int4,
    primary key (id)
                      );

create table document_type (
    id int4 not null,
    name varchar(255),
    affiliation_id int4,
    primary key (id)
                           );

create table es_type (
    id int4 not null,
    name varchar(255) not null,
    primary key (id)
                     );

create table key_carriers (
    id int4 not null,
    key_carriers_name varchar(255) not null,
    kctype_id int4 not null,
    primary key (id)
                          );

create table key_carriers_type (
    id int4 not null,
    name varchar(255) not null,
    primary key (id)
                               );

create table organization (
    id int4 not null,
    gid varchar(255),
    inn varchar(255),
    name varchar(255),
    org_type_id int4,
    primary key (id)
                          );

create table organization_type (
    id int4 not null,
    name varchar(255),
    primary key (id)
                               );

create table pakuc (
    id int4 not null,
    pakucname varchar(255) not null,
    primary key (id)
                   );

create table security_class (
    id int4 not null,
    name varchar(255),
    primary key (id)
                            );

create table skzi (
    id int4 not null,
    ks varchar(255) not null,
    name varchar(255) not null,
    realization_variant varchar(255) not null,
    version varchar(255) not null,
     primary key (id)
                  );

create table status (
    id int4 not null,
    name varchar(255),
    primary key (id)
                    );

create table system (
    id int4 not null,
    name varchar(255) not null,
    key_expiration_months varchar(255) not null,
    crypto_type varchar(255) not null,
    bank_software boolean not null,
    bank_id int4 not null,
    es_type_id int4 not null,
    organization_id int4 not null,
    system_name_id int4 not null,
    system_type_id int4 not null,
    primary key (id)
                    );

create table system_name (
    id int4 not null,
    description varchar(1000),
    name varchar(255),
    primary key (id)
                         );

create table system_status (
    id int4 not null,
    name varchar(255),
    primary key (id)
                           );

create table system_type (
    id int4 not null,
    name varchar(255),
    primary key (id)
                         );

create table szi (
    id int4 not null,
    ks2 varchar(255) not null,
    szi_name varchar(255) not null,
    primary key (id)
                 );

create table trust_level (
    id int4 not null,
    name varchar(255),
    primary key (id)
                         );

create table user_role (
    user_id int4 not null,
    roles varchar(255)
                       );

create table usr (
    id int4 not null,
    active boolean not null,
    email varchar(255),
    fio varchar(255),
    password varchar(255),
    username varchar(255),
    primary key (id)
                 );


alter table if exists arm add constraint arm_antivirus_fk foreign key (av_id) references antivirus;
alter table if exists arm add constraint arm_armtype_fk foreign key (arm_type_id) references armtype;
alter table if exists arm add constraint arm_organization_fk foreign key (organization_id) references organization;
alter table if exists arm add constraint arm_pakuc_fk foreign key (uc_id) references pakuc;
alter table if exists armkc add constraint armkc_arm_fk foreign key (arm_id) references arm;
alter table if exists armkc add constraint armkc_kc_fk foreign key (kc_id) references key_carriers;
alter table if exists armskzi add constraint armskzi_arm_fk foreign key (arm_id) references arm;
alter table if exists armskzi add constraint armskzi_skzi_fk foreign key (skzi_id) references skzi;
alter table if exists armsystem_name add constraint armsystem_name_arm_fk foreign key (arm_id) references arm;
alter table if exists armsystem_name add constraint armsystem_name_sname_fk foreign key (systemname_id) references system_name;
alter table if exists armszi add constraint armszi_arm_fk foreign key (arm_id) references arm;
alter table if exists armszi add constraint armszi_szi_fk foreign key (szi_id) references szi;
alter table if exists contact add constraint contact_organization_fk foreign key (organization_id) references organization;
alter table if exists document add constraint document_arm_fk foreign key (arm_id) references arm;
alter table if exists document add constraint document_antivirus_fk foreign key (av_id) references antivirus;
alter table if exists document add constraint document_key_carriers_fk foreign key (kc_id) references key_carriers;
alter table if exists document add constraint document_pakuc_fk foreign key (uc_id) references pakuc;
alter table if exists document add constraint document_szi_fk foreign key (szi_id) references szi;
alter table if exists document add constraint document_doctype_fk foreign key (document_type_id) references document_type;
alter table if exists document add constraint document_systemname_fk foreign key (systemname_id) references system_name;
alter table if exists document add constraint document_organization_fk foreign key (organization_id) references organization;
alter table if exists document add constraint document_bank_fk foreign key (organization_id) references organization;
alter table if exists document add constraint document_skzi_fk foreign key (skzi_id) references skzi;
alter table if exists document add constraint document_status_fk foreign key (status_id) references status;
alter table if exists document add constraint document_system_fk foreign key (system_id) references system;
alter table if exists document add constraint document_sysstatus_fk foreign key (system_status_id) references system_status;
alter table if exists document add constraint document_trustlevel_fk foreign key (trust_level_id) references trust_level;
alter table if exists key_carriers add constraint key_carriers_type_fk foreign key (kctype_id) references key_carriers_type;
alter table if exists document_type add constraint documenttype_affiliation_fk foreign key (affiliation_id) references affiliation;
alter table if exists organization add constraint organization_orgtype_fk foreign key (org_type_id) references organization_type;
alter table if exists system add constraint system_systype_fk foreign key (system_type_id) references system_type;
alter table if exists system add constraint system_sysname_fk foreign key (system_name_id) references system_name;
alter table if exists system add constraint system_bank_fk foreign key (bank_id) references organization;
alter table if exists system add constraint system_organization_fk foreign key (organization_id) references organization;
alter table if exists user_role add constraint role_usr_fk foreign key (user_id) references usr;

insert into status (name, id) values ('Новое', 1);
insert into status (name, id) values ('В архиве', 2);
insert into status (name, id) values ('Запрос направлен в банк, ожидание ответа', 3);
insert into status (name, id) values ('Запрос направлен клиенту, ожидание ответа', 4);
insert into status (name, id) values ('Запрос получен банком, ожидание ответа', 5);
insert into status (name, id) values ('Запрос получен клиентом, ожидание ответа', 6);
insert into status (name, id) values ('Информация получена от банка, обработка информации', 7);
insert into status (name, id) values ('Информация получена от клиента, обработка информации', 8);
insert into status (name, id) values ('Заключение выдано', 9);
insert into status (name, id) values ('Подготовка заключения', 10);
insert into status (name, id) values ('Звонить в банк', 11);
insert into status (name, id) values ('Звонить клиенту', 12);
insert into status (name, id) values ('Подготовка официального запроса в банк', 13);
insert into status (name, id) values ('Организация встречи', 14);

insert into system_status (name, id) values ('Не задан', 1);
insert into system_status (name, id) values ('В эксплуатации', 2);
insert into system_status (name, id) values ('Выведена из эксплуатации', 3);

insert into trust_level (name, id) values ('Высокий', 1);
insert into trust_level (name, id) values ('Средний', 2);
insert into trust_level (name, id) values ('Низкий', 3);

insert into es_type (name, id) values ('УКЭП', 1);
insert into es_type (name, id) values ('УУКЭП', 2);
insert into es_type (name, id) values ('УУКЭП', 3);
insert into es_type (name, id) values ('УУНЭП', 4);

insert into key_carriers_type (name, id) values ('Токен', 1);
insert into key_carriers_type (name, id) values ('Смарт-карта', 2);
insert into key_carriers_type (name, id) values ('Флэшка / Диск', 3);

insert into affiliation (name, id) values ('Банк', 1);
insert into affiliation (name, id) values ('Клиент', 2);
insert into affiliation (name, id) values ('Система', 3);
insert into affiliation (name, id) values ('СКЗИ', 4);
insert into affiliation (name, id) values ('Общий', 5);
insert into affiliation (name, id) values ('Антивирус', 6);
insert into affiliation (name, id) values ('ПАК УЦ', 7);
insert into affiliation (name, id) values ('СЗИ', 8);
insert into affiliation (name, id) values ('Ключевые носители', 9);
insert into affiliation (name, id) values ('АРМ', 10);



insert into document_type (name, id, affiliation_id) values ('Аттестат соответствия', 18, 1);
insert into document_type (name, id, affiliation_id) values ('Документ о выполнении стандарта банка России', 11, 1);
insert into document_type (name, id, affiliation_id) values ('Документ на использование СКЗИ, использующегося в составе средства, реализующего инфраструктуру ключевой системы', 4, 1);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающий право передачи СКЗИ Клиенту, использующегося в работе Системы', 52, 1);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающий допуск пользователей к самостоятельной работе с СКЗИ', 23, 1);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающий порядок хранение ключей в Банке', 69, 1);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающий порядок уничтожение ключей в Банке', 70, 1);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающий порядок уничтожение СКЗИ в Банке', 71, 1);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающий прохождение обучения пользователями правилам работы с СКЗИ', 24, 1);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающий проведение периодического контроля администраторами условий использования СКЗИ', 26, 1);
insert into document_type (name, id, affiliation_id) values ('Документ, регламентирующий жизненный цикл ключесвой системы', 8, 1);
insert into document_type (name, id, affiliation_id) values ('Лицензия ФСБ России на соответствующие виды деятельности', 46, 1);
insert into document_type (name, id, affiliation_id) values ('Локальные нормативные акты, определяющие права и роли работников в Системе (подписантов, администраторов безопасности)', 25, 1);
insert into document_type (name, id, affiliation_id) values ('Свидетельство об аккредитации', 9, 1);
insert into document_type (name, id, affiliation_id) values ('Приказ об администраторах безопасности и лицах их замещающих', 66, 1);



insert into document_type (name, id, affiliation_id) values ('Аттестат соответствия', 33, 2);
insert into document_type (name, id, affiliation_id) values ('Договор между АО "Гринатом" и Клиентом', 50, 2);
insert into document_type (name, id, affiliation_id) values ('Договор между Банком и Клиентом', 51, 2);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающий допуск пользователей к самостоятельной работе с СКЗИ', 36, 2);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающий прохождение обучения пользователями правилам работы с СКЗИ', 37, 2);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающий проведение периодического контроля администраторами условий использования СКЗИ', 39, 2);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающий право использования на средство реализующее инфраструктуру ключевой системы', 1, 1);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающтй получение СКЗИ по доверенному каналу', 15, 2);
insert into document_type (name, id, affiliation_id) values ('Журнал поэкземплярного учета', 67, 2);
insert into document_type (name, id, affiliation_id) values ('Заявление на услугу', 61, 2);
insert into document_type (name, id, affiliation_id) values ('Заявление о присоединении', 64, 2);
insert into document_type (name, id, affiliation_id) values ('Лицензия на СКЗИ переданных Клиенту для работы в Системе', 14, 2);
insert into document_type (name, id, affiliation_id) values ('Локальные нормативные акты, определяющие права и роли работников в Системе (подписантов, администраторов безопасности)', 38, 2);
insert into document_type (name, id, affiliation_id) values ('Приказ об администраторах безопасности и лицах их замещающих', 65, 2);





insert into document_type (name, id, affiliation_id) values ('Документация на систему', 44, 3);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающий использование усиленной квалифицированной электронной подписи, в том числе для подписания выписки', 40, 3);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающий использование усиленной неквалифицированной электронной подписи, в том числе для подписания выписки', 41, 3);
insert into document_type (name, id, affiliation_id) values ('Документ, подтверждающий использование формата усовершенствованной подписи (дополнительных служб удостоверяющего центра (службы онлайновой проверки статусов сертификатов и службы штампов времени))', 42, 3);
insert into document_type (name, id, affiliation_id) values ('Заключение о корректности встраивания СКЗИ в Систему', 17, 3);
insert into document_type (name, id, affiliation_id) values ('Лицензия на программное обеспечение Системы', 43, 3);



insert into document_type (name, id, affiliation_id) values ('Сертификат соответствия ФСБ России', 28, 4);



insert into document_type (name, id, affiliation_id) values ('Заключение', 45, 5);



insert into document_type (name, id, affiliation_id) values ('Сертификат соответствия', 34, 6);



insert into document_type (name, id, affiliation_id) values ('Сертификат соответствия ФСБ России', 5, 7);



insert into document_type (name, id, affiliation_id) values ('Сертификат соответствия ФСТЭК России', 22, 8);



insert into document_type (name, id, affiliation_id) values ('Сертификат соответствия', 7, 9);




insert into document_type (name, id, affiliation_id) values ('Заключение органа криптографической защиты о возможности эксплуатации СКЗИ', 30, 10);
insert into document_type (name, id, affiliation_id) values ('Акт готовности СКЗИ к эксплуатации', 68, 10);



insert into organization_type (name, id) values ('Банк', 1);
insert into organization_type (name, id) values ('Организация (Клиент)', 2);



insert into system_type (name, id) values ('Классический «Банк-Клиент» (толстый клиент)', 1);
insert into system_type (name, id) values ('Интернет банкинг (тонкий клиент)', 2);



insert into security_class (name, id) values ('КС1', 1);
insert into security_class (name, id) values ('КС2', 2);
insert into security_class (name, id) values ('КС3', 3);
insert into security_class (name, id) values ('КВ1', 4);
insert into security_class (name, id) values ('КВ2', 5);
insert into security_class (name, id) values ('КА1', 6);



insert into armtype (name, id) values ('Сервер', 1);
insert into armtype (name, id) values ('Пользователь', 2);



insert into system_name (description, name, id) values ('АО «АйСиБиСи Банк»', 'Автоматизированная система «Клиент-Банк» «АйСиБиСи Банк» (АО)', 1);
insert into system_name (description, name, id) values ('АО «АЛЬФА-БАНК»', 'Информационная система интернет-банк «Альфа-Бизнес Онлайн» АО «АЛЬФА-БАНК»', 2);
insert into system_name (description, name, id) values ('АО «Россельхозбанк»', 'Система ДБО «Банк-Клиент» «Россельхозбанк» (АО)', 3);
insert into system_name (description, name, id) values ('АО АКБ «НОВИКОМБАНК»', 'АС «ДБО BS-Client» АО АКБ «НОВИКОМБАНК»', 4);
insert into system_name (description, name, id) values ('АО АКБ «Банк Китая (Элос)»', 'Система ДБО АКБ "БЭНК ОФ ЧАЙНА», АО (АКБ «Банк Китая (Элос)»)', 5);
insert into system_name (description, name, id) values ('АО АКБ «Банк Китая (Элос)»', 'Система ДБО "Тонкий клиент» АКБ «БЭНК ОФ ЧАЙНА», АО (АКБ «Банк Китая (Элос)»)', 6);
insert into system_name (description, name, id) values ('Банк ВТБ (ПАО)','Банк ВТБ (ПАО)", "АС «ДБО BS-Client» ПАО "Банк ВТБ»', 7);
insert into system_name (description, name, id) values ('Банк ВТБ (ПАО)', 'стема ДБО Сервис «Интернет-Клиент» Банка ВТБ (ПАО)', 8);
insert into system_name (description, name, id) values ('Банк ВТБ (ПАО)', 'АС «ДБО BS-Client» ПАО "Банк ВТБ»', 9);
insert into system_name (description, name, id) values ('Банк ВТБ (ПАО)', 'Система электронного документооборота в рамках зарплатного проекта Банка ВТБ (ПАО)', 10);
insert into system_name (description, name, id) values ('Банк ГПБ (АО)', 'Система «Клиент-Банк WEB» Банка "Газпромбанк» (АО)', 11);
insert into system_name (description, name, id) values ('Банк ГПБ (АО)', 'Информационная система «Клиент-Банк» «Газпромбанк» (АО)', 12);
insert into system_name (description, name, id) values ('Банк ГПБ (АО)', 'Система электронного документооборота в рамках зарплатного проекта «Газпромбанк» (АО)', 13);
insert into system_name (description, name, id) values ('ООО «Чайна Констракшн Банк»', 'АС «Клиент-Банк» «Чайна Констракшн Банк» (ООО)', 14);
insert into system_name (description, name, id) values ('ПАО «Росбанк»', 'Корпоративная ИС Интернет Клиент-Банк «Росбанк» (ПАО)', 15);
insert into system_name (description, name, id) values ('ПАО «Сбербанк»', 'АС «Сбербанк Бизнес Онлайн» «Сбербанк» (ПАО)', 16);
insert into system_name (description, name, id) values ('ПАО «МОСКОВСКИЙ КРЕДИТНЫЙ БАНК»', 'Электронная система «Ваш Банк Онлайн»', 17);
insert into system_name (description, name, id) values ('ПАО «Нордеа Банк»', 'АС «Нордеа Клиент-Банк (Internet)» «Нордеа Банк» (АО)', 18);
insert into system_name (description, name, id) values ('ПАО «Промсвязьбанк»', 'Система ДБО «PSB Corporate» «Промсвязьбанк» (ПАО)', 19);
insert into system_name (description, name, id) values ('ПАО АБ «Россия»', 'АС «ДБО BS-Client» «АБ «Россия» (АО)', 20);