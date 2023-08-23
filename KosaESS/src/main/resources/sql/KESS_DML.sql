
INSERT INTO post  (post_id, post_title,post_content, post_hit, post_cd,  rgster_id)
VALUES('NTC0000001','공지사항입니다','공지공지','20','GRP0001001','ADM');

INSERT INTO post  (post_id, post_title,post_content, post_hit, post_cd,  rgster_id)
VALUES('INQ0000001','문의사항입니다','질문질문','5','GRP0001003','STU0000001');


INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('GRP0001', '0', '게시글 상태', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('001', 'GRP0001', '공개', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('002', 'GRP0001', '비공개', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('003', 'GRP0001', '답변대기', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('004', 'GRP0001', '답변완료', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('GRP0002', '0', '교육과정 상태', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('001', 'GRP0002', '접수예정', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('002', 'GRP0002', '접수중', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('003', 'GRP0002', '접수마감', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('004', 'GRP0002', '교육중', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('005', 'GRP0002', '교육완료', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('006', 'GRP0002', '취소', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('007', 'GRP0002', '폐강', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('GRP0003', '0', '교육 지원 상태', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('001', 'GRP0003', '지원완료', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('002', 'GRP0003', '합격', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('003', 'GRP0003', '불합격', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('GRP0004', '0', '역할', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('001', 'GRP0004', '교육생', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('002', 'GRP0004', '관리자', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('003', 'GRP0004', '업무담당자', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('GRP0005', '0', '수강 상태', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('001', 'GRP0005', '교육중', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('002', 'GRP0005', '교육완료', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('003', 'GRP0005', '중도포기', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('GRP0006', '0', '성별', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('001', 'GRP0006', '남', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('002', 'GRP0006', '여', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('GRP0007', '0', '직업', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('001', 'GRP0007', '무직', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('002', 'GRP0007', '학생', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('003', 'GRP0007', '직장인', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('GRP0008', '0', '계정상태', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('001', 'GRP0008', '활성화', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('002', 'GRP0008', '휴먼', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('003', 'GRP0008', '탈퇴', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('GRP0009', '0', '수강상태', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('001', 'GRP0009', '미이수', 'ADM');

INSERT INTO cmcd (cmcd_id, tpcd_id, cmcd_nm, rgster_id)
VALUES ('002', 'GRP0009', '이수완료', 'ADM');


INSERT INTO sbjt (sbjt_id, sbjt_nm, rgster_id)
VALUES ('SUB0000001', 'JAVA', 'ADM');

INSERT INTO sbjt (sbjt_id, sbjt_nm, rgster_id)
VALUES ('SUB0000002', 'DB', 'ADM');


INSERT INTO prof( prof_id,  prof_nm, prof_tel, prof_email, rgster_id)
VALUES('PRO0000001', '김교수', '01011112459', 'pro@naver.com', 'ADM');

INSERT INTO prof( prof_id,  prof_nm, prof_tel, prof_email, rgster_id)
VALUES('PRO0000002', '이박사', '01022502459', 'fes@naver.com', 'ADM');


INSERT INTO cmpy( cmpy_id, cmpy_nm, cmpy_tel, cmpy_adr, rgster_id)
VALUES('COM0000001', 'KCC정보통신', '0260907800', '서울시 용산구 청파로 61길 5 (청파동 1가 178-1) 6F~8F', 'ADM');

 INSERT INTO cmpy( cmpy_id, cmpy_nm, cmpy_tel, cmpy_adr, rgster_id)
VALUES('COM0000002', 'KCC오토', ' 0263550000', '서울특별시 강서구 공항대로 665', 'ADM');


INSERT INTO mngr(mngr_id, mngr_pwd, mngr_nm, mngr_tel, mngr_email, role_cd, rgster_id) 
VALUES('MNG0000001', 'abc123!', '김직원', '01012123434', 'man@naver.com', 'GRP0004003',  'ADM');

INSERT INTO mngr(mngr_id, mngr_pwd, mngr_nm, mngr_tel, mngr_email, role_cd, rgster_id) 
VALUES('MNG0000002', 'abab1234!', '나직월', '01012223444', 'ger@naver.com', 'GRP0004003',  'ADM');



INSERT INTO stdt(stdt_id, stdt_email, stdt_pwd, stdt_nm, gender_cd,  birth_dd, stdt_tel,  job_cd, role_cd, stdt_cd, rgster_id)
VALUES('STU0000001', 'stu@naver.com','1212aaa!','김학생','GRP0006001', '20000130', '01033334455', 'GRP0007002','GRP0004001', 'GRP0008001', 'ADM');

INSERT INTO stdt(stdt_id, stdt_email, stdt_pwd, stdt_nm, gender_cd,  birth_dd, stdt_tel,  job_cd, role_cd, stdt_cd, rgster_id)
VALUES('STU0000002', 'ent@naver.com','177add!','김무직','GRP0006002', '19981002', '01034445556', 'GRP0007001','GRP0004001', 'GRP0008001', 'ADM');


INSERT INTO clss(clss_id, cmpy_id, mngr_id, clss_nm, clss_content, limit_cnt, clss_cd, rgster_id) 
VALUES('CLS0000001', 'COM0000001', 'MNG0000001', '야호 JAVA', 'JAVAVAVA', '30', 'GRP0002001',  'ADM');

INSERT INTO clss(clss_id, cmpy_id, mngr_id, clss_nm, clss_content, limit_cnt, clss_cd, rgster_id) 
VALUES('CLS0000002', 'COM0000002', 'MNG0000002', '야호 DB', 'DBDB', '25', 'GRP0002001',  'ADM');


INSERT INTO lctr(lctr_id, clss_id, sbjt_id, prof_id, lctr_nm, lctr_tm,lctr_step,  rgster_id)
VALUES('LEC0000001', 'CLS0000001', 'SUB0000001', 'PRO0000001', 'JAVA기초', '100', '1',  'ADM');

INSERT INTO lctr(lctr_id, clss_id, sbjt_id, prof_id, lctr_nm, lctr_tm,lctr_step,  rgster_id)
VALUES('LEC0000002', 'CLS0000002', 'SUB0000002', 'PRO0000002', 'DB심화', '50', '3',  'ADM');


INSERT INTO rgst(rgst_id,  stdt_id , clss_id,rgst_cd,cmpt_cd, rgster_id)
VALUES('RES0000001', 'STU0000001', 'CLS0000001','GRP0005001', 'GRP0009001', 'ADM');

INSERT INTO rgst(rgst_id,  stdt_id , clss_id,rgst_cd,cmpt_cd, rgster_id)
VALUES('RES0000002', 'STU0000002', 'CLS0000002','GRP0005001', 'GRP0009001', 'ADM');

INSERT INTO aply (aply_id,stdt_id ,clss_id ,aply_cd,rgster_id)
VALUES('APL0000001', 'STU0000001', 'CLS0000001','GRP0003003', 'ADM');

INSERT INTO aply (aply_id,stdt_id ,clss_id ,aply_cd,rgster_id)
VALUES('APL0000002', 'STU0000001', 'CLS0000002','GRP0003004', 'ADM');





