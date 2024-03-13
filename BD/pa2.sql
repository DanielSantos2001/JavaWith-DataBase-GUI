/*==============================================================*/
/* DBMS name:      Sybase SQL Anywhere 12                       */
/* Created on:     16/05/2022 16:48:44                          */
/*==============================================================*/


drop index if exists JOGADOR.JOGADOR_PK;

drop table if exists JOGADOR;

drop index if exists SIMULACAO.SIMULACAO_PK;

drop table if exists SIMULACAO;

/*==============================================================*/
/* Table: JOGADOR                                               */
/*==============================================================*/
create table JOGADOR 
(
   IDJOGADOR            integer                        not null,
   NOME                 varchar(30)                    null,
   NJOGOS               integer                        null,
   NVITORIAS            integer                        null,
   TEMPOTOTAL           integer                        null,
   constraint PK_JOGADOR primary key (IDJOGADOR)
);

/*==============================================================*/
/* Index: JOGADOR_PK                                            */
/*==============================================================*/
create unique index JOGADOR_PK on JOGADOR (
IDJOGADOR ASC
);

/*==============================================================*/
/* Table: SIMULACAO                                             */
/*==============================================================*/
create table SIMULACAO 
(
   IDJOGO               integer                        not null,
   POSICAO              varchar(500)                   null,
   constraint PK_SIMULACAO primary key (IDJOGO)
);

/*==============================================================*/
/* Index: SIMULACAO_PK                                          */
/*==============================================================*/
create unique index SIMULACAO_PK on SIMULACAO (
IDJOGO ASC
);

