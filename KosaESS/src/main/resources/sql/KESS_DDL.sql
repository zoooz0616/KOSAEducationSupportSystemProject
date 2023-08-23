create table cmcd(
 cmcd_id varchar2(7),
 tpcd_id varchar2(7),
 cmcd_nm varchar2(20),
 rgst_dt TIMESTAMP default SYSTIMESTAMP,
 rgster_id varchar2(10) not null,
 updt_dt TIMESTAMP,
 updter_id  varchar2(10),
 CONSTRAINT cmcd_PK PRIMARY KEY(cmcd_id, tpcd_id)
);

create table post(
 post_id varchar2(10) primary key,
 master_id varchar2(10),
 post_title varchar2(100) not null,
 post_content varchar2(100) not null,
 post_hit number not null,
 post_cd varchar2(10) not null,
 rgst_dt TIMESTAMP default SYSTIMESTAMP,
 rgster_id varchar2(10) not null,
 updt_dt TIMESTAMP,
 updter_id  varchar2(10) 
);

create table mngr(
 mngr_id varchar2(10) primary key,
 mngr_pwd varchar2(20) not null,
 mngr_nm varchar2(20) not null,
 mngr_tel varchar2(15) not null,
 mngr_email varchar2(20) not null,
 role_cd varchar2(10) not null,
 clss1_id varchar2(10),
 clss2_id varchar2(10),
 clss3_id varchar2(10),
 rgst_dt TIMESTAMP default SYSTIMESTAMP,
 rgster_id varchar2(10) not null,
 updt_dt TIMESTAMP,
 updter_id  varchar2(10)
);

create table cmpy(
 cmpy_id varchar2(10) primary key,
 cmpy_nm varchar2(20) not null,
 cmpy_tel varchar2(15) not null,
 cmpy_adr varchar2(100) not null,
 rgst_dt TIMESTAMP default SYSTIMESTAMP,
 rgster_id varchar2(10) not null,
 updt_dt TIMESTAMP,
 updter_id  varchar2(10)
);

create table clss(
 clss_id varchar2(10) primary key,
 cmpy_id varchar2(10) not null,
 mngr_id varchar2(10),
 clss_nm varchar2(100) not null,
 clss_content varchar2(100),
 limit_cnt number default(30),
 aply_start_dt TIMESTAMP ,
 aply_end_dt TIMESTAMP,
 clss_start_dd date,
 clss_end_dd date,
 set_in_tm TIMESTAMP,
 set_out_time TIMESTAMP,
 clss_cd varchar2(10) not null,
 clss_adr varchar2(100),
 total_tm number,
 clss_etc varchar2(4000),
 rgst_dt TIMESTAMP default SYSTIMESTAMP,
 rgster_id varchar2(10) not null,
 updt_dt TIMESTAMP,
 updter_id  varchar2(10)
);

create table flup (
  file_id varchar2(10) primary key,
  post_id varchar2(10) not null,
  file_nm varchar2(20) not null,   
  file_content BLOB not null,
  file_size number not null,
  file_type varchar2(10) not null
);

create table stdt(
 stdt_id varchar2(10) primary key,
 stdt_email varchar2(20) not null,
 stdt_pwd varchar2(20) not null,
 stdt_nm varchar2(20) not null,
 gender_cd varchar2(10) not null,
 birth_dd date not null,
 stdt_tel varchar2(15) not null,
 job_cd varchar2(10) not null,
 role_cd varchar2(10) not null,
 stdt_cd varchar2(10) not null,
 rgst_dt TIMESTAMP default SYSTIMESTAMP,
 rgster_id varchar2(10) not null,
 updt_dt TIMESTAMP,
 updter_id  varchar2(10)
);

create table aply (
 aply_id varchar2(10) primary key,
 stdt_id varchar2(10) not null,
 clss_id varchar2(10) not null,
 aply_cd varchar2(10) not null,
 rgst_dt TIMESTAMP default SYSTIMESTAMP,
 rgster_id varchar2(10) not null,
 updt_dt TIMESTAMP,
 updter_id  varchar2(10)
);

create table rgst(
 rgst_id varchar2(10) primary key,
 stdt_id varchar2(10) not null,
 clss_id varchar2(10) not null,
 rgst_cd varchar2(10) not null,
 cmpt_cd varchar2(10) not null,
 rgst_dt TIMESTAMP default SYSTIMESTAMP,
 rgster_id varchar2(10) not null,
 updt_dt TIMESTAMP,
 updter_id  varchar2(10)
);

create table sbjt(
 sbjt_id varchar2(10) primary key,
 sbjt_nm varchar2(20) not null,
 rgst_dt TIMESTAMP default SYSTIMESTAMP,
 rgster_id varchar2(10) not null,
 updt_dt TIMESTAMP,
 updter_id  varchar2(10)
);

create table prof(
 prof_id varchar2(10) primary key,
 prof_nm varchar2(10) not null,
 prof_tel varchar2(15) not null,
 prof_email varchar2(20) not null,
 rgst_dt TIMESTAMP default SYSTIMESTAMP,
 rgster_id varchar2(10) not null,
 updt_dt TIMESTAMP,
 updter_id  varchar2(10)
);

create table lctr(
 lctr_id varchar2(10) primary key,
 clss_id varchar2(10) not null,
 sbjt_id varchar2(10) not null,
 prof_id varchar2(10) not null,
 lctr_nm varchar2(20) not null,
 lctr_tm number not null,
 lctr_etc varchar2(4000),
 lctr_step number not null,
 rgst_dt TIMESTAMP default SYSTIMESTAMP,
 rgster_id varchar2(10) not null,
 updt_dt TIMESTAMP,
 updter_id  varchar2(10)
);


alter table post add FOREIGN KEY(master_id) REFERENCES post(post_id);
alter table clss add FOREIGN KEY(mngr_id) REFERENCES mngr(mngr_id);
alter table clss add FOREIGN KEY(cmpy_id) REFERENCES cmpy(cmpy_id);
alter table flup add FOREIGN KEY(post_id) REFERENCES post(post_id);
alter table flup add FOREIGN KEY(post_id) REFERENCES clss(clss_id);
alter table flup add FOREIGN KEY(post_id) REFERENCES cmpy(cmpy_id);
alter table aply add FOREIGN KEY(stdt_id) REFERENCES stdt(stdt_id);
alter table aply add FOREIGN KEY(clss_id) REFERENCES clss(clss_id);
alter table rgst add FOREIGN KEY(stdt_id) REFERENCES stdt(stdt_id);
alter table rgst add FOREIGN KEY(clss_id) REFERENCES clss(clss_id);
alter table lctr add FOREIGN KEY(sbjt_id) REFERENCES sbjt(sbjt_id);
alter table lctr add FOREIGN KEY(clss_id) REFERENCES clss(clss_id);
alter table lctr add FOREIGN KEY(prof_id) REFERENCES prof(prof_id);
