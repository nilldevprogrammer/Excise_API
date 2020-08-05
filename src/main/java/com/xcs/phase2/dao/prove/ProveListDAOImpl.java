package com.xcs.phase2.dao.prove;


import com.xcs.phase2.constant.Pattern;
import com.xcs.phase2.model.arrest.ArrestLawbreaker;
import com.xcs.phase2.model.prove.ProveList;
import com.xcs.phase2.request.prove.ProveListgetByConAdvReq;
import com.xcs.phase2.request.prove.ProveListgetByKeywordReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProveListDAOImpl extends ProveExt implements ProveListDAO{

    private static final Logger log = LoggerFactory.getLogger(ProveListDAOImpl.class);

    @Override
    public List<ProveList> ProveListgetByKeyword(ProveListgetByKeywordReq req) {
        // TODO Auto-generated method stub

        String str = "";

        if(req.getACCOUNT_OFFICE_CODE() != null && !"".equals(req.getACCOUNT_OFFICE_CODE()) && !"00".equals(req.getACCOUNT_OFFICE_CODE())) {

            if(req.getACCOUNT_OFFICE_CODE().length() == 6) {

                if("00".equals(req.getACCOUNT_OFFICE_CODE().substring(0, 2))) {
                    str = " ";
                }else if("0000".equals(req.getACCOUNT_OFFICE_CODE().substring(2, 6))) {
                    str = " AND" +
                            "( " +
                            "  SUBSTR(MAS_SUB_DISTRICT.OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
                            "  OR SUBSTR(OPS_PROVE_STAFF.OPERATION_OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
                            "  OR SUBSTR(OPS_PROVE_STAFF.MANAGEMENT_OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
                            "  OR SUBSTR(OPS_PROVE_STAFF.REPRESENT_OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
                            ") " ;
                }else if("00".equals(req.getACCOUNT_OFFICE_CODE().substring(4, 6))) {
                    str = " AND " +
                            "( " +
                            "  SUBSTR(MAS_SUB_DISTRICT.OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
                            "  OR SUBSTR(OPS_PROVE_STAFF.OPERATION_OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
                            "  OR SUBSTR(OPS_PROVE_STAFF.MANAGEMENT_OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
                            "  OR SUBSTR(OPS_PROVE_STAFF.REPRESENT_OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
                            " )";
                }else {
                    str = " AND" +
                            " (" +
                            "  MAS_SUB_DISTRICT.OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
                            "  OR OPS_PROVE_STAFF.OPERATION_OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
                            "  OR OPS_PROVE_STAFF.MANAGEMENT_OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
                            "  OR OPS_PROVE_STAFF.REPRESENT_OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
                            " )";
                }
            }
        }


        StringBuilder sqlBuilder = new StringBuilder()
                .append("with temp as (" +
                        "    Select distinct" +
                        "    OPS_ARREST.ARREST_ID," +
                        "    OPS_ARREST.ARREST_CODE," +
                        "    OPS_ARREST.ARREST_DATE," +
                        "    OPS_ARREST.OCCURRENCE_DATE," +
                        "    OPS_ARREST_INDICTMENT.INDICTMENT_ID," +
                        "    case when OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH is null or OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH = 'null' THEN OPS_ARREST_STAFF.TITLE_NAME_TH ||''|| OPS_ARREST_STAFF.FIRST_NAME ||' '|| OPS_ARREST_STAFF.LAST_NAME " +
                        "    else OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH ||''|| OPS_ARREST_STAFF.FIRST_NAME ||' '|| OPS_ARREST_STAFF.LAST_NAME end as ARREST_STAFF_NAME, " +
//                        "    OPS_LAWSUIT_DETAIL.LAWSUIT_TYPE," +
//                        "    OPS_LAWSUIT_DETAIL.LAWSUIT_END," +
                        "    MAS_LAW_GROUP_SUBSECTION.SUBSECTION_NAME, " +
                        "    MAS_LAW_GUILTBASE.GUILTBASE_NAME," +
                        "    MAS_SUB_DISTRICT.SUB_DISTRICT_NAME_TH," +
                        "    MAS_SUB_DISTRICT.SUB_DISTRICT_NAME_EN," +
                        "    MAS_DISTRICT.DISTRICT_NAME_TH," +
                        "    MAS_DISTRICT.DISTRICT_NAME_EN," +
                        "    MAS_PROVINCE.PROVINCE_NAME_TH," +
                        "    MAS_PROVINCE.PROVINCE_NAME_EN," +
                        "    OPS_LAWSUIT.LAWSUIT_ID," +
                        "    CASE WHEN OPS_LAWSUIT.IS_OUTSIDE = '1' THEN 'น. ' END || OPS_LAWSUIT.LAWSUIT_NO || CASE WHEN OPS_LAWSUIT.LAWSUIT_NO IS NOT NULL THEN '/' END || TO_CHAR(OPS_LAWSUIT.LAWSUIT_NO_YEAR, 'YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS Lawsuilt_no," +
                        "    OPS_LAWSUIT.LAWSUIT_DATE," +
                        "    OPS_LAWSUIT.IS_LAWSUIT," +
                        "    case when OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH is null or OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH = 'null' THEN OPS_LAWSUIT_STAFF.TITLE_NAME_TH ||''|| OPS_LAWSUIT_STAFF.FIRST_NAME ||' '|| OPS_LAWSUIT_STAFF.LAST_NAME " +
                        "    else OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH ||''|| OPS_LAWSUIT_STAFF.FIRST_NAME ||' '|| OPS_LAWSUIT_STAFF.LAST_NAME end as LAWSUIT_STAFF_NAME, " +
                        "    OPS_LAWSUIT.OFFICE_NAME," +
                        "    OPS_PROVE.PROVE_ID," +
                        "    CASE WHEN OPS_PROVE.IS_OUTSIDE = '1' THEN 'น. ' END || OPS_PROVE.PROVE_NO || CASE WHEN OPS_PROVE.PROVE_NO IS NOT NULL THEN '/' END || TO_CHAR(OPS_PROVE.PROVE_NO_YEAR, 'YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS PROVE_NO," +
                        "    OPS_PROVE.IS_OUTSIDE PROVE_IS_OUTSIDE," +
                        "    OPS_PROVE.PROVE_DATE," +
                        "    OPS_PROVE_STAFF.STAFF_ID," +
                        "    case when OPS_PROVE_STAFF.TITLE_SHORT_NAME_TH is null or OPS_PROVE_STAFF.TITLE_SHORT_NAME_TH = 'null' THEN OPS_PROVE_STAFF.TITLE_NAME_TH ||''|| OPS_PROVE_STAFF.FIRST_NAME ||' '|| OPS_PROVE_STAFF.LAST_NAME " +
                        "    else OPS_PROVE_STAFF.TITLE_SHORT_NAME_TH ||''|| OPS_PROVE_STAFF.FIRST_NAME ||' '|| OPS_PROVE_STAFF.LAST_NAME end as PROVE_STAFF_NAME, " +
                        "    OPS_PROVE_STAFF.OPERATION_POS_CODE PROVE_OPERATION_POS_CODE," +
                        "    OPS_PROVE_STAFF.OPREATION_POS_NAME PROVE_OPREATION_POS_NAME," +
                        "    OPS_PROVE_STAFF.OPREATION_POS_LEVEL PROVE_OPREATION_POS_LEVEL," +
                        "    OPS_PROVE_STAFF.OPERATION_POS_LEVEL_NAME PROVE_OPERATION_POS_LEVEL_NAME," +
                        "    OPS_PROVE_STAFF.OPERATION_DEPT_CODE PROVE_OPERATION_DEPT_CODE," +
                        "    OPS_PROVE_STAFF.OPERATION_DEPT_NAME PROVE_OPERATION_DEPT_NAME," +
                        "    OPS_PROVE_STAFF.OPERATION_DEPT_LEVEL PROVE_OPERATION_DEPT_LEVEL," +
                        "    OPS_PROVE_STAFF.OPERATION_UNDER_DEPT_CODE PROVE_OPERATION_UNDER_DEPT_CODE," +
                        "    OPS_PROVE_STAFF.OPERATION_UNDER_DEPT_NAME PROVE_OPERATION_UNDER_DEPT_NAME," +
                        "    OPS_PROVE_STAFF.OPERATION_UNDER_DEPT_LEVEL PROVE_OPERATION_UNDER_DEPT_LEVEL," +
                        "    OPS_PROVE_STAFF.OPERATION_WORK_DEPT_CODE PROVE_OPERATION_WORK_DEPT_CODE," +
                        "    OPS_PROVE_STAFF.OPERATION_WORK_DEPT_NAME PROVE_OPERATION_WORK_DEPT_NAME," +
                        "    OPS_PROVE_STAFF.OPERATION_WORK_DEPT_LEVEL PROVE_OPERATION_WORK_DEPT_LEVEL," +
                        "    OPS_PROVE_STAFF.OPERATION_OFFICE_CODE PROVE_OPERATION_OFFICE_CODE," +
                        "    OPS_PROVE_STAFF.OPERATION_OFFICE_NAME PROVE_OPERATION_OFFICE_NAME," +
                        "    OPS_PROVE_STAFF.OPERATION_OFFICE_SHORT_NAME PROVE_OPERATION_OFFICE_SHORT_NAME," +
                        "    OPS_PROVE.RECEIVE_OFFICE_NAME," +
                        "    CASE WHEN OPS_PROVE.PROVE_NO IS NOT NULL THEN 'พิสูจน์แล้ว' else 'ยังไม่พิสูจน์' END prove_status," +
                        "    OPS_ARREST_LAWBREAKER.LAWBREAKER_ID" +
                        "    from OPS_ARREST_INDICTMENT" +
                        "    INNER JOIN OPS_ARREST ON OPS_ARREST_INDICTMENT.ARREST_ID = OPS_ARREST.ARREST_ID" +
                        "    AND OPS_ARREST.IS_ACTIVE = 1" +
                        "    INNER JOIN OPS_ARREST_STAFF ON OPS_ARREST.ARREST_ID = OPS_ARREST_STAFF.ARREST_ID" +
                        "    AND OPS_ARREST_STAFF.CONTRIBUTOR_ID = 14" +
                        "    AND OPS_ARREST_STAFF.IS_ACTIVE = 1" +
                        "    LEFT JOIN OPS_ARREST_INDICTMENT_DETAIL ON OPS_ARREST_INDICTMENT.INDICTMENT_ID = OPS_ARREST_INDICTMENT_DETAIL.INDICTMENT_ID" +
                        "    AND OPS_ARREST_INDICTMENT_DETAIL.IS_ACTIVE = 1" +
                        "    LEFT JOIN OPS_ARREST_LAWBREAKER ON OPS_ARREST_INDICTMENT_DETAIL.LAWBREAKER_ID = OPS_ARREST_LAWBREAKER.LAWBREAKER_ID" +
                        "    AND OPS_ARREST_LAWBREAKER.IS_ACTIVE = 1" +
                        "    INNER JOIN MAS_LAW_GUILTBASE ON OPS_ARREST_INDICTMENT.GUILTBASE_ID = MAS_LAW_GUILTBASE.GUILTBASE_ID" +
                        "    AND MAS_LAW_GUILTBASE.IS_PROVE=1" +
                        "    INNER JOIN MAS_LAW_GROUP_SUBSECTION_RULE ON MAS_LAW_GUILTBASE.SUBSECTION_RULE_ID = MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_RULE_ID" +
                        "    INNER JOIN MAS_LAW_GROUP_SUBSECTION ON MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_ID = MAS_LAW_GROUP_SUBSECTION.SUBSECTION_ID" +
                        "    INNER JOIN MAS_LAW_GROUP_SECTION ON MAS_LAW_GROUP_SUBSECTION_RULE.SECTION_ID = MAS_LAW_GROUP_SECTION.SECTION_ID" +
                        "    INNER JOIN MAS_LAW_PENALTY ON MAS_LAW_GROUP_SECTION.SECTION_ID = MAS_LAW_PENALTY.SECTION_ID" +
                        "    LEFT JOIN OPS_LAWSUIT ON OPS_ARREST_INDICTMENT.INDICTMENT_ID = OPS_LAWSUIT.INDICTMENT_ID" +
                        "    LEFT JOIN OPS_LAWSUIT_STAFF ON OPS_LAWSUIT.LAWSUIT_ID = OPS_LAWSUIT_STAFF.LAWSUIT_ID" +
                        "    AND OPS_LAWSUIT_STAFF.IS_ACTIVE = 1" +
                        "    AND OPS_LAWSUIT_STAFF.CONTRIBUTOR_ID = 16" +
                        "    INNER JOIN OPS_ARREST_LOCALE ON OPS_ARREST.ARREST_ID = OPS_ARREST_LOCALE.ARREST_ID" +
                        "    INNER JOIN MAS_SUB_DISTRICT ON OPS_ARREST_LOCALE.SUB_DISTRICT_ID = MAS_SUB_DISTRICT.SUB_DISTRICT_ID" +
                        "    INNER JOIN MAS_DISTRICT ON MAS_SUB_DISTRICT.DISTRICT_ID = MAS_DISTRICT.DISTRICT_ID" +
                        "    INNER JOIN MAS_PROVINCE ON MAS_DISTRICT.PROVINCE_ID = MAS_PROVINCE.PROVINCE_ID" +
                        "    LEFT JOIN OPS_LAWSUIT_DETAIL ON OPS_ARREST_INDICTMENT_DETAIL.INDICTMENT_DETAIL_ID = OPS_LAWSUIT_DETAIL.INDICTMENT_DETAIL_ID" +
                        "    AND OPS_LAWSUIT_DETAIL.IS_ACTIVE = 1" +
                        "    LEFT JOIN OPS_PROVE on OPS_LAWSUIT.LAWSUIT_ID = OPS_PROVE.LAWSUIT_ID" +
                        "    AND OPS_PROVE.IS_ACTIVE = 1" +
                        "    AND OPS_PROVE.PROVE_TYPE = 0 " +
                        "    left join OPS_PROVE_STAFF on OPS_PROVE_STAFF.PROVE_ID= OPS_PROVE.PROVE_ID " +
                        "    and OPS_PROVE_STAFF.CONTRIBUTOR_ID = 25" +
                        "    and OPS_PROVE_STAFF.IS_ACTIVE = 1" +
                        "    where (OPS_PROVE.IS_ACTIVE=1 or OPS_PROVE.IS_ACTIVE IS NULL)" +
                        "    AND OPS_LAWSUIT.IS_ACTIVE = 1" +
                        "    AND OPS_LAWSUIT.IS_LAWSUIT = 1" +
                        "    and" +
                        "    (" +
                        "        lower(OPS_ARREST.ARREST_CODE)like lower(replace ('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_ARREST_STAFF.TITLE_NAME_TH||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_ARREST_STAFF.TITLE_NAME_EN||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_ARREST_STAFF.TITLE_SHORT_NAME_EN||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_ARREST_LAWBREAKER.TITLE_NAME_TH||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_ARREST_LAWBREAKER.TITLE_NAME_EN||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_ARREST_LAWBREAKER.TITLE_SHORT_NAME_TH||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_ARREST_LAWBREAKER.TITLE_SHORT_NAME_EN||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(MAS_LAW_GROUP_SUBSECTION.SUBSECTION_NAME) LIKE LOWER('%"+req.getTEXT_SEARCH()+"%')" +
                        "        OR LOWER(MAS_LAW_GUILTBASE.GUILTBASE_NAME) LIKE LOWER('%"+req.getTEXT_SEARCH()+"%')" +
                        "        OR LOWER(MAS_SUB_DISTRICT.SUB_DISTRICT_NAME_TH||MAS_DISTRICT.DISTRICT_NAME_TH||MAS_PROVINCE.PROVINCE_NAME_TH) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_LAWSUIT_STAFF.TITLE_NAME_TH||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_LAWSUIT_STAFF.TITLE_NAME_EN||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_EN||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_PROVE_STAFF.TITLE_NAME_TH||OPS_PROVE_STAFF.FIRST_NAME||OPS_PROVE_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_PROVE_STAFF.TITLE_NAME_EN||OPS_PROVE_STAFF.FIRST_NAME||OPS_PROVE_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_PROVE_STAFF.TITLE_SHORT_NAME_TH||OPS_PROVE_STAFF.FIRST_NAME||OPS_PROVE_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_PROVE_STAFF.TITLE_SHORT_NAME_EN||OPS_PROVE_STAFF.FIRST_NAME||OPS_PROVE_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
                        "        OR LOWER(OPS_PROVE.RECEIVE_OFFICE_NAME) LIKE LOWER('%"+req.getTEXT_SEARCH()+"%')" +
                        "    )" + str +
                        "     ORDER BY OPS_ARREST.OCCURRENCE_DATE DESC" +
                        "   ) " +
                        "   select " +
                        "    ARREST_ID," +
                        "    ARREST_CODE," +
                        "    ARREST_DATE," +
                        "    OCCURRENCE_DATE," +
                        "    INDICTMENT_ID," +
                        "    ARREST_STAFF_NAME," +
//                        "    LAWSUIT_TYPE," +
//                        "    LAWSUIT_END," +
                        "    SUBSECTION_NAME," +
                        "    GUILTBASE_NAME," +
                        "    SUB_DISTRICT_NAME_TH," +
                        "    SUB_DISTRICT_NAME_EN," +
                        "    DISTRICT_NAME_TH," +
                        "    DISTRICT_NAME_EN," +
                        "    PROVINCE_NAME_TH," +
                        "    PROVINCE_NAME_EN," +
                        "    LAWSUIT_ID," +
                        "    LAWSUILT_NO," +
                        "    LAWSUIT_DATE," +
                        "    IS_LAWSUIT," +
                        "    LAWSUIT_STAFF_NAME," +
                        "    OFFICE_NAME," +
                        "    PROVE_ID," +
                        "    PROVE_NO," +
                        "    PROVE_IS_OUTSIDE," +
                        "    PROVE_DATE," +
                        "    STAFF_ID," +
                        "    PROVE_STAFF_NAME," +
                        "    PROVE_OPERATION_POS_CODE," +
                        "    PROVE_OPREATION_POS_NAME," +
                        "    PROVE_OPREATION_POS_LEVEL," +
                        "    PROVE_OPERATION_POS_LEVEL_NAME," +
                        "    PROVE_OPERATION_DEPT_CODE," +
                        "    PROVE_OPERATION_DEPT_NAME," +
                        "    PROVE_OPERATION_DEPT_LEVEL," +
                        "    PROVE_OPERATION_UNDER_DEPT_CODE," +
                        "    PROVE_OPERATION_UNDER_DEPT_NAME," +
                        "    PROVE_OPERATION_UNDER_DEPT_LEVEL," +
                        "    PROVE_OPERATION_WORK_DEPT_CODE," +
                        "    PROVE_OPERATION_WORK_DEPT_NAME," +
                        "    PROVE_OPERATION_WORK_DEPT_LEVEL," +
                        "    PROVE_OPERATION_OFFICE_CODE," +
                        "    PROVE_OPERATION_OFFICE_NAME," +
                        "    PROVE_OPERATION_OFFICE_SHORT_NAME," +
                        "    RECEIVE_OFFICE_NAME," +
                        "    PROVE_STATUS," +
                        "    LISTAGG(LAWBREAKER_ID, ',') WITHIN GROUP (" +
                        "    ORDER BY " +
                        "        ARREST_ID," +
                        "        ARREST_CODE," +
                        "        ARREST_DATE," +
                        "        OCCURRENCE_DATE," +
                        "        INDICTMENT_ID," +
                        "        ARREST_STAFF_NAME," +
//                        "        LAWSUIT_TYPE," +
//                        "        LAWSUIT_END," +
                        "        SUBSECTION_NAME," +
                        "        GUILTBASE_NAME," +
                        "        SUB_DISTRICT_NAME_TH," +
                        "        SUB_DISTRICT_NAME_EN," +
                        "        DISTRICT_NAME_TH," +
                        "        DISTRICT_NAME_EN," +
                        "        PROVINCE_NAME_TH," +
                        "        PROVINCE_NAME_EN," +
                        "        LAWSUIT_ID," +
                        "        LAWSUILT_NO," +
                        "        LAWSUIT_DATE," +
                        "        IS_LAWSUIT," +
                        "        LAWSUIT_STAFF_NAME," +
                        "        OFFICE_NAME," +
                        "        PROVE_ID," +
                        "        PROVE_NO," +
                        "        PROVE_IS_OUTSIDE," +
                        "        PROVE_DATE," +
                        "        STAFF_ID," +
                        "        PROVE_STAFF_NAME," +
                        "        PROVE_OPERATION_POS_CODE," +
                        "        PROVE_OPREATION_POS_NAME," +
                        "        PROVE_OPREATION_POS_LEVEL," +
                        "        PROVE_OPERATION_POS_LEVEL_NAME," +
                        "        PROVE_OPERATION_DEPT_CODE," +
                        "        PROVE_OPERATION_DEPT_NAME," +
                        "        PROVE_OPERATION_DEPT_LEVEL," +
                        "        PROVE_OPERATION_UNDER_DEPT_CODE," +
                        "        PROVE_OPERATION_UNDER_DEPT_NAME," +
                        "        PROVE_OPERATION_UNDER_DEPT_LEVEL," +
                        "        PROVE_OPERATION_WORK_DEPT_CODE," +
                        "        PROVE_OPERATION_WORK_DEPT_NAME," +
                        "        PROVE_OPERATION_WORK_DEPT_LEVEL," +
                        "        PROVE_OPERATION_OFFICE_CODE," +
                        "        PROVE_OPERATION_OFFICE_NAME," +
                        "        PROVE_OPERATION_OFFICE_SHORT_NAME," +
                        "        RECEIVE_OFFICE_NAME," +
                        "        PROVE_STATUS" +
                        "    ) AS LAWBREAKER_ID" +
                        "   from temp" +
                        "   GROUP BY" +
                        "        ARREST_ID," +
                        "        ARREST_CODE," +
                        "        ARREST_DATE," +
                        "        OCCURRENCE_DATE," +
                        "        INDICTMENT_ID," +
                        "        ARREST_STAFF_NAME," +
//                        "        LAWSUIT_TYPE," +
//                        "        LAWSUIT_END," +
                        "        SUBSECTION_NAME," +
                        "        GUILTBASE_NAME," +
                        "        SUB_DISTRICT_NAME_TH," +
                        "        SUB_DISTRICT_NAME_EN," +
                        "        DISTRICT_NAME_TH," +
                        "        DISTRICT_NAME_EN," +
                        "        PROVINCE_NAME_TH," +
                        "        PROVINCE_NAME_EN," +
                        "        LAWSUIT_ID," +
                        "        LAWSUILT_NO," +
                        "        LAWSUIT_DATE," +
                        "        IS_LAWSUIT," +
                        "        LAWSUIT_STAFF_NAME," +
                        "        OFFICE_NAME," +
                        "        PROVE_ID," +
                        "        PROVE_NO," +
                        "        PROVE_IS_OUTSIDE," +
                        "        PROVE_DATE," +
                        "        STAFF_ID," +
                        "        PROVE_STAFF_NAME," +
                        "        PROVE_OPERATION_POS_CODE," +
                        "        PROVE_OPREATION_POS_NAME," +
                        "        PROVE_OPREATION_POS_LEVEL," +
                        "        PROVE_OPERATION_POS_LEVEL_NAME," +
                        "        PROVE_OPERATION_DEPT_CODE," +
                        "        PROVE_OPERATION_DEPT_NAME," +
                        "        PROVE_OPERATION_DEPT_LEVEL," +
                        "        PROVE_OPERATION_UNDER_DEPT_CODE," +
                        "        PROVE_OPERATION_UNDER_DEPT_NAME," +
                        "        PROVE_OPERATION_UNDER_DEPT_LEVEL," +
                        "        PROVE_OPERATION_WORK_DEPT_CODE," +
                        "        PROVE_OPERATION_WORK_DEPT_NAME," +
                        "        PROVE_OPERATION_WORK_DEPT_LEVEL," +
                        "        PROVE_OPERATION_OFFICE_CODE," +
                        "        PROVE_OPERATION_OFFICE_NAME," +
                        "        PROVE_OPERATION_OFFICE_SHORT_NAME," +
                        "        RECEIVE_OFFICE_NAME," +
                        "        PROVE_STATUS");

        log.info("[SQL] : " + sqlBuilder.toString());


        @SuppressWarnings("unchecked")
        List<ProveList> dataList = getJdbcTemplate().query(sqlBuilder.toString(), new RowMapper() {

            public ProveList mapRow(ResultSet rs, int rowNum) throws SQLException {
                ProveList item = new ProveList();
                item.setARREST_ID(rs.getInt("ARREST_ID"));
                item.setARREST_CODE(rs.getString("ARREST_CODE"));
                item.setARREST_DATE(rs.getString("ARREST_DATE"));
                item.setOCCURRENCE_DATE(rs.getString("OCCURRENCE_DATE"));
                item.setINDICTMENT_ID(rs.getInt("INDICTMENT_ID"));
                item.setARREST_STAFF_NAME(rs.getString("ARREST_STAFF_NAME"));
//                item.setLAWSUIT_TYPE(rs.getInt("LAWSUIT_TYPE"));
//                item.setLAWSUIT_END(rs.getString("LAWSUIT_END"));
                item.setSUBSECTION_NAME(rs.getString("SUBSECTION_NAME"));
                item.setGUILTBASE_NAME(rs.getString("GUILTBASE_NAME"));
                item.setSUB_DISTRICT_NAME_TH(rs.getString("SUB_DISTRICT_NAME_TH"));
                item.setSUB_DISTRICT_NAME_EN(rs.getString("SUB_DISTRICT_NAME_EN"));
                item.setDISTRICT_NAME_TH(rs.getString("DISTRICT_NAME_TH"));
                item.setDISTRICT_NAME_EN(rs.getString("DISTRICT_NAME_EN"));
                item.setPROVINCE_NAME_TH(rs.getString("PROVINCE_NAME_TH"));
                item.setPROVINCE_NAME_EN(rs.getString("PROVINCE_NAME_EN"));
                item.setLAWSUIT_ID(rs.getInt("LAWSUIT_ID"));
                item.setLAWSUILT_NO(rs.getString("LAWSUILT_NO"));
                item.setLAWSUIT_DATE(rs.getString("LAWSUIT_DATE"));
                item.setIS_LAWSUIT(rs.getInt("IS_LAWSUIT"));
                item.setLAWSUIT_STAFF_NAME(rs.getString("LAWSUIT_STAFF_NAME"));
                item.setOFFICE_NAME(rs.getString("OFFICE_NAME"));
                item.setPROVE_ID(rs.getInt("PROVE_ID"));
                item.setPROVE_NO(rs.getString("PROVE_NO"));
                item.setPROVE_IS_OUTSIDE(rs.getString("PROVE_IS_OUTSIDE"));
                item.setPROVE_DATE(rs.getString("PROVE_DATE"));
                item.setSTAFF_ID(rs.getInt("STAFF_ID"));
                item.setPROVE_STAFF_NAME(rs.getString("PROVE_STAFF_NAME"));
                item.setPROVE_OPERATION_POS_CODE(rs.getString("PROVE_OPERATION_POS_CODE"));
                item.setPROVE_OPREATION_POS_NAME(rs.getString("PROVE_OPREATION_POS_NAME"));
                item.setPROVE_OPREATION_POS_LEVEL(rs.getString("PROVE_OPREATION_POS_LEVEL"));
                item.setPROVE_OPERATION_POS_LEVEL_NAME(rs.getString("PROVE_OPERATION_POS_LEVEL_NAME"));
                item.setPROVE_OPERATION_DEPT_CODE(rs.getString("PROVE_OPERATION_DEPT_CODE"));
                item.setPROVE_OPERATION_DEPT_NAME(rs.getString("PROVE_OPERATION_DEPT_NAME"));
                item.setPROVE_OPERATION_DEPT_LEVEL(rs.getString("PROVE_OPERATION_DEPT_LEVEL"));
                item.setPROVE_OPERATION_UNDER_DEPT_CODE(rs.getString("PROVE_OPERATION_UNDER_DEPT_CODE"));
                item.setPROVE_OPERATION_UNDER_DEPT_NAME(rs.getString("PROVE_OPERATION_UNDER_DEPT_NAME"));
                item.setPROVE_OPERATION_UNDER_DEPT_LEVEL(rs.getString("PROVE_OPERATION_UNDER_DEPT_LEVEL"));
                item.setPROVE_OPERATION_WORK_DEPT_CODE(rs.getString("PROVE_OPERATION_WORK_DEPT_CODE"));
                item.setPROVE_OPERATION_WORK_DEPT_NAME(rs.getString("PROVE_OPERATION_WORK_DEPT_NAME"));
                item.setPROVE_OPERATION_WORK_DEPT_LEVEL(rs.getString("PROVE_OPERATION_WORK_DEPT_LEVEL"));
                item.setPROVE_OPERATION_OFFICE_CODE(rs.getString("PROVE_OPERATION_OFFICE_CODE"));
                item.setPROVE_OPERATION_OFFICE_NAME(rs.getString("PROVE_OPERATION_OFFICE_NAME"));
                item.setPROVE_OPERATION_OFFICE_SHORT_NAME(rs.getString("PROVE_OPERATION_OFFICE_SHORT_NAME"));
                item.setRECEIVE_OFFICE_NAME(rs.getString("RECEIVE_OFFICE_NAME"));
                item.setPROVE_STATUS(rs.getString("PROVE_STATUS"));
                item.setArrestLawbreaker(getArrestLawbreakerByLAWBREAKER_ID(rs.getInt("INDICTMENT_ID")));
                // item.setArrestLawbreaker(getArrestLawbreakerByLAWBREAKER_ID(rs.getString("LAWBREAKER_ID")));

                return item;
            }
        });

        return dataList;
    }

    @Override
    public List<ProveList> ProveListgetByConAdv(final ProveListgetByConAdvReq req) {
        // TODO Auto-generated method stub

        String str = "";

        if(req.getACCOUNT_OFFICE_CODE() != null && !"".equals(req.getACCOUNT_OFFICE_CODE()) && !"00".equals(req.getACCOUNT_OFFICE_CODE())) {

            if(req.getACCOUNT_OFFICE_CODE().length() == 6) {

                if("00".equals(req.getACCOUNT_OFFICE_CODE().substring(0, 2))) {
                    str = " ";
                }else if("0000".equals(req.getACCOUNT_OFFICE_CODE().substring(2, 6))) {
                    str = " AND" +
                            "( " +
                            "  SUBSTR(MAS_SUB_DISTRICT.OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
                            "  OR SUBSTR(OPS_PROVE_STAFF.OPERATION_OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
                            "  OR SUBSTR(OPS_PROVE_STAFF.MANAGEMENT_OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
                            "  OR SUBSTR(OPS_PROVE_STAFF.REPRESENT_OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
                            ") " ;
                }else if("00".equals(req.getACCOUNT_OFFICE_CODE().substring(4, 6))) {
                    str = " AND " +
                            "( " +
                            "  SUBSTR(MAS_SUB_DISTRICT.OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
                            "  OR SUBSTR(OPS_PROVE_STAFF.OPERATION_OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
                            "  OR SUBSTR(OPS_PROVE_STAFF.MANAGEMENT_OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
                            "  OR SUBSTR(OPS_PROVE_STAFF.REPRESENT_OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
                            " )";
                }else {
                    str = " AND" +
                            " (" +
                            "  MAS_SUB_DISTRICT.OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
                            "  OR OPS_PROVE_STAFF.OPERATION_OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
                            "  OR OPS_PROVE_STAFF.MANAGEMENT_OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
                            "  OR OPS_PROVE_STAFF.REPRESENT_OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
                            " )";
                }
            }
        }

        StringBuilder sqlBuilder = new StringBuilder();

        String tempOCCURRENCE_DATE_FROM = "";
        String tempOCCURRENCE_DATE_TO = "";
        String tempPROVE_DATE_FROM = "";
        String tempPROVE_DATE_TO = "";
        String tempLAWSUIT_DATE_FROM = "";
        String tempLAWSUIT_DATE_TO = "";

        if(!"".equals(req.getOCCURRENCE_DATE_FROM())) {
            tempOCCURRENCE_DATE_FROM = String.format("%s %s", req.getOCCURRENCE_DATE_FROM(), Pattern.TIME_FROM);
        }

        if(!"".equals(req.getOCCURRENCE_DATE_TO())) {
            tempOCCURRENCE_DATE_TO = String.format("%s %s", req.getOCCURRENCE_DATE_TO(),Pattern.TIME_TO);
        }

        if(!"".equals(req.getPROVE_DATE_FROM())) {
            tempPROVE_DATE_FROM = String.format("%s %s", req.getPROVE_DATE_FROM(),Pattern.TIME_FROM);
        }

        if(!"".equals(req.getPROVE_DATE_TO())) {
            tempPROVE_DATE_TO = String.format("%s %s", req.getPROVE_DATE_TO(),Pattern.TIME_TO);
        }

        if(!"".equals(req.getLAWSUIT_DATE_FROM())) {
            tempLAWSUIT_DATE_FROM = String.format("%s %s", req.getLAWSUIT_DATE_FROM(),Pattern.TIME_FROM);
        }

        if(!"".equals(req.getLAWSUIT_DATE_TO())) {
            tempLAWSUIT_DATE_TO = String.format("%s %s", req.getLAWSUIT_DATE_TO(),Pattern.TIME_TO);
        }


        if(req.getARREST_CODE() != null && !"".equals(req.getARREST_CODE())) {
            sqlBuilder.append(" AND lower(OPS_ARREST.ARREST_CODE)like lower(replace ('%"+req.getARREST_CODE()+"%',' ','')) ");
        }

        if(req.getOCCURRENCE_DATE_FROM() != null && !"".equals(req.getOCCURRENCE_DATE_FROM()) && req.getOCCURRENCE_DATE_TO() != null && !"".equals(req.getOCCURRENCE_DATE_TO())) {
            sqlBuilder.append(" AND OPS_ARREST.OCCURRENCE_DATE BETWEEN  to_date(nvl('"+tempOCCURRENCE_DATE_FROM+"','0001-01-01 00:00'),'YYYY-MM-DD HH24:MI') and to_date(nvl('"+tempOCCURRENCE_DATE_TO+"','9999-12-31 23:59'),'YYYY-MM-DD HH24:MI')");
        }

        if(req.getARREST_STAFF_NAME() != null && !"".equals(req.getARREST_STAFF_NAME())) {
            sqlBuilder.append(" AND " +
                    " (" +
                    "  LOWER(OPS_ARREST_STAFF.TITLE_NAME_TH||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getARREST_STAFF_NAME()+"%',' ',''))" +
                    "  OR LOWER(OPS_ARREST_STAFF.TITLE_NAME_EN||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getARREST_STAFF_NAME()+"%',' ',''))" +
                    "  OR LOWER(OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getARREST_STAFF_NAME()+"%',' ',''))" +
                    "  OR LOWER(OPS_ARREST_STAFF.TITLE_SHORT_NAME_EN||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getARREST_STAFF_NAME()+"%',' ',''))" +
                    " )");
        }

        if(req.getLAWBREAKER_STAFF_NAME() != null && !"".equals(req.getLAWBREAKER_STAFF_NAME())) {
            sqlBuilder.append(" AND " +
                    " (" +
                    "  LOWER(OPS_ARREST_LAWBREAKER.TITLE_NAME_TH||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER(REPLACE('%"+req.getLAWBREAKER_STAFF_NAME()+"%',' ',''))" +
                    "  OR LOWER(OPS_ARREST_LAWBREAKER.TITLE_NAME_EN||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER(REPLACE('%"+req.getLAWBREAKER_STAFF_NAME()+"%',' ',''))" +
                    "  OR LOWER(OPS_ARREST_LAWBREAKER.TITLE_SHORT_NAME_TH||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER(REPLACE('%"+req.getLAWBREAKER_STAFF_NAME()+"%',' ',''))" +
                    "  OR LOWER(OPS_ARREST_LAWBREAKER.TITLE_SHORT_NAME_EN||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER(REPLACE('%"+req.getLAWBREAKER_STAFF_NAME()+"%',' ',''))" +
                    " )");
        }

        if(req.getSECTION_NAME() != null && !"".equals(req.getSECTION_NAME())) {
            sqlBuilder.append(" AND LOWER(MAS_LAW_GROUP_SUBSECTION.SUBSECTION_NAME) LIKE LOWER('%"+req.getSECTION_NAME()+"%') ");
        }

        if(req.getGUILTBASE_NAME() != null && !"".equals(req.getGUILTBASE_NAME())) {
            sqlBuilder.append(" AND LOWER ( MAS_LAW_GUILTBASE.GUILTBASE_NAME ) LIKE LOWER('%"+req.getGUILTBASE_NAME()+"%') ");
        }

        if(req.getARREST_OFFICE_NAME() != null && !"".equals(req.getARREST_OFFICE_NAME())) {
            sqlBuilder.append(" AND LOWER(MAS_SUB_DISTRICT.SUB_DISTRICT_NAME_TH||MAS_DISTRICT.DISTRICT_NAME_TH||MAS_PROVINCE.PROVINCE_NAME_TH) LIKE LOWER(REPLACE('%"+req.getARREST_OFFICE_NAME()+"%',' ','')) ");
        }

        if(req.getLAWSUIT_NO() != null && !"".equals(req.getLAWSUIT_NO())) {
            sqlBuilder.append(" AND OPS_LAWSUIT.LAWSUIT_NO = '"+req.getLAWSUIT_NO()+"' ");
        }

        if(req.getLAWSUIT_NO_YEAR() != null && !"".equals(req.getLAWSUIT_NO_YEAR())) {
            sqlBuilder.append(" AND (to_number(TO_CHAR(OPS_LAWSUIT.LAWSUIT_NO_YEAR,'YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI')) = '"+req.getLAWSUIT_NO_YEAR()+"' ) ");
        }

        if(req.getLAWSUIT_IS_OUTSIDE()!= null && !"".equals(req.getLAWSUIT_IS_OUTSIDE())) {
            sqlBuilder.append(" AND OPS_LAWSUIT.IS_OUTSIDE = '"+req.getLAWSUIT_IS_OUTSIDE()+"' ");
        }

        if(req.getLAWSUIT_DATE_FROM() != null && !"".equals(req.getLAWSUIT_DATE_FROM()) && req.getLAWSUIT_DATE_TO() != null && !"".equals(req.getLAWSUIT_DATE_TO())) {
            sqlBuilder.append(" AND OPS_LAWSUIT.LAWSUIT_DATE BETWEEN  to_date(nvl('"+tempLAWSUIT_DATE_FROM+"','0001-01-01 00:00'),'YYYY-MM-DD HH24:MI') and to_date(nvl('"+tempLAWSUIT_DATE_TO+"','9999-12-31 23:59'),'YYYY-MM-DD HH24:MI')");
        }

        if(req.getLAWSUIT_STAFF_NAME() != null && !"".equals(req.getLAWSUIT_STAFF_NAME())) {
            sqlBuilder.append(" AND " +
                    " (" +
                    "  LOWER(OPS_LAWSUIT_STAFF.TITLE_NAME_TH||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getLAWSUIT_STAFF_NAME()+"%',' ',''))" +
                    "  OR LOWER(OPS_LAWSUIT_STAFF.TITLE_NAME_EN||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getLAWSUIT_STAFF_NAME()+"%',' ',''))" +
                    "  OR LOWER(OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getLAWSUIT_STAFF_NAME()+"%',' ',''))" +
                    "  OR LOWER(OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_EN||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getLAWSUIT_STAFF_NAME()+"%',' ',''))" +
                    " )");
        }

        if(req.getPROVE_STATUS() != null && !"".equals(req.getPROVE_STATUS())) {
            if("0".equals(req.getPROVE_STATUS())){
                sqlBuilder.append(" and OPS_PROVE.PROVE_NO is null ");
            }else{
                sqlBuilder.append(" and OPS_PROVE.PROVE_NO is not null ");
            }

        }

        if(req.getPROVE_NO() != null && !"".equals(req.getPROVE_NO())) {
            sqlBuilder.append(" AND OPS_PROVE.PROVE_NO = '"+req.getPROVE_NO()+"' ");
        }

        if(req.getPROVE_NO_YEAR()!= null && !"".equals(req.getPROVE_NO_YEAR())) {
            sqlBuilder.append(" AND (to_number(TO_CHAR(OPS_PROVE.PROVE_NO_YEAR,'YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI'))= '"+req.getPROVE_NO_YEAR()+"') ");
        }

        if(req.getPROVE_IS_OUTSIDE()!= null && !"".equals(req.getPROVE_IS_OUTSIDE())) {
            sqlBuilder.append(" AND OPS_PROVE.IS_OUTSIDE = '"+req.getPROVE_IS_OUTSIDE()+"' ");
        }

        if(req.getPROVE_DATE_FROM() != null && !"".equals(req.getPROVE_DATE_FROM()) && req.getPROVE_DATE_TO() != null && !"".equals(req.getPROVE_DATE_TO())) {
            sqlBuilder.append(" AND OPS_PROVE.PROVE_DATE BETWEEN  to_date(nvl('"+tempPROVE_DATE_FROM+"','0001-01-01 00:00'),'YYYY-MM-DD HH24:MI') and to_date(nvl('"+tempPROVE_DATE_TO+"','9999-12-31 23:59'),'YYYY-MM-DD HH24:MI')");
        }

        if(req.getPROVE_STAFF_NAME() != null && !"".equals(req.getPROVE_STAFF_NAME())) {
            sqlBuilder.append(" AND" +
                    " (" +
                    "  LOWER(OPS_PROVE_STAFF.TITLE_NAME_TH||OPS_PROVE_STAFF.FIRST_NAME||OPS_PROVE_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getPROVE_STAFF_NAME()+"%',' ',''))" +
                    "  OR LOWER(OPS_PROVE_STAFF.TITLE_NAME_EN||OPS_PROVE_STAFF.FIRST_NAME||OPS_PROVE_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getPROVE_STAFF_NAME()+"%',' ',''))" +
                    "  OR LOWER(OPS_PROVE_STAFF.TITLE_SHORT_NAME_TH||OPS_PROVE_STAFF.FIRST_NAME||OPS_PROVE_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getPROVE_STAFF_NAME()+"%',' ',''))" +
                    "  OR LOWER(OPS_PROVE_STAFF.TITLE_SHORT_NAME_EN||OPS_PROVE_STAFF.FIRST_NAME||OPS_PROVE_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getPROVE_STAFF_NAME()+"%',' ',''))" +
                    " )");
        }

        if(req.getRECEIVE_OFFICE_NAME()!= null && !"".equals(req.getRECEIVE_OFFICE_NAME())) {
            sqlBuilder.append(" AND LOWER(OPS_PROVE.RECEIVE_OFFICE_NAME) LIKE LOWER('%"+req.getRECEIVE_OFFICE_NAME()+"%') ");
        }

        sqlBuilder.append(str);


        StringBuilder sqlBuilder1 = new StringBuilder()
                .append("with temp as (" +
                        "    Select distinct" +
                        "    OPS_ARREST.ARREST_ID," +
                        "    OPS_ARREST.ARREST_CODE," +
                        "    OPS_ARREST.ARREST_DATE," +
                        "    OPS_ARREST.OCCURRENCE_DATE," +
                        "    OPS_ARREST_INDICTMENT.INDICTMENT_ID," +
                        "    case when OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH is null or OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH = 'null' THEN OPS_ARREST_STAFF.TITLE_NAME_TH ||''|| OPS_ARREST_STAFF.FIRST_NAME ||' '|| OPS_ARREST_STAFF.LAST_NAME " +
                        "    else OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH ||''|| OPS_ARREST_STAFF.FIRST_NAME ||' '|| OPS_ARREST_STAFF.LAST_NAME end as ARREST_STAFF_NAME, " +
//                        "    OPS_LAWSUIT_DETAIL.LAWSUIT_TYPE," +
//                        "    OPS_LAWSUIT_DETAIL.LAWSUIT_END," +
                        "    MAS_LAW_GROUP_SUBSECTION.SUBSECTION_NAME, " +
                        "    MAS_LAW_GUILTBASE.GUILTBASE_NAME," +
                        "    MAS_SUB_DISTRICT.SUB_DISTRICT_NAME_TH," +
                        "    MAS_SUB_DISTRICT.SUB_DISTRICT_NAME_EN," +
                        "    MAS_DISTRICT.DISTRICT_NAME_TH," +
                        "    MAS_DISTRICT.DISTRICT_NAME_EN," +
                        "    MAS_PROVINCE.PROVINCE_NAME_TH," +
                        "    MAS_PROVINCE.PROVINCE_NAME_EN," +
                        "    OPS_LAWSUIT.LAWSUIT_ID," +
                        "    CASE WHEN OPS_LAWSUIT.IS_OUTSIDE = '1' THEN 'น. ' END || OPS_LAWSUIT.LAWSUIT_NO || CASE WHEN OPS_LAWSUIT.LAWSUIT_NO IS NOT NULL THEN '/' END || TO_CHAR(OPS_LAWSUIT.LAWSUIT_NO_YEAR, 'YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS Lawsuilt_no," +
                        "    OPS_LAWSUIT.LAWSUIT_DATE," +
                        "    OPS_LAWSUIT.IS_LAWSUIT," +
                        "    case when OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH is null or OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH = 'null' THEN OPS_LAWSUIT_STAFF.TITLE_NAME_TH ||''|| OPS_LAWSUIT_STAFF.FIRST_NAME ||' '|| OPS_LAWSUIT_STAFF.LAST_NAME " +
                        "    else OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH ||''|| OPS_LAWSUIT_STAFF.FIRST_NAME ||' '|| OPS_LAWSUIT_STAFF.LAST_NAME end as LAWSUIT_STAFF_NAME, " +
                        "    OPS_LAWSUIT.OFFICE_NAME," +
                        "    OPS_PROVE.PROVE_ID," +
                        "    CASE WHEN OPS_PROVE.IS_OUTSIDE = '1' THEN 'น. ' END || OPS_PROVE.PROVE_NO || CASE WHEN OPS_PROVE.PROVE_NO IS NOT NULL THEN '/' END || TO_CHAR(OPS_PROVE.PROVE_NO_YEAR, 'YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS PROVE_NO," +
                        "    OPS_PROVE.IS_OUTSIDE PROVE_IS_OUTSIDE," +
                        "    OPS_PROVE.PROVE_DATE," +
                        "    OPS_PROVE_STAFF.STAFF_ID," +
                        "    case when OPS_PROVE_STAFF.TITLE_SHORT_NAME_TH is null or OPS_PROVE_STAFF.TITLE_SHORT_NAME_TH = 'null' THEN OPS_PROVE_STAFF.TITLE_NAME_TH ||''|| OPS_PROVE_STAFF.FIRST_NAME ||' '|| OPS_PROVE_STAFF.LAST_NAME " +
                        "    else OPS_PROVE_STAFF.TITLE_SHORT_NAME_TH ||''|| OPS_PROVE_STAFF.FIRST_NAME ||' '|| OPS_PROVE_STAFF.LAST_NAME end as PROVE_STAFF_NAME, " +
                        "    OPS_PROVE_STAFF.OPERATION_POS_CODE PROVE_OPERATION_POS_CODE," +
                        "    OPS_PROVE_STAFF.OPREATION_POS_NAME PROVE_OPREATION_POS_NAME," +
                        "    OPS_PROVE_STAFF.OPREATION_POS_LEVEL PROVE_OPREATION_POS_LEVEL," +
                        "    OPS_PROVE_STAFF.OPERATION_POS_LEVEL_NAME PROVE_OPERATION_POS_LEVEL_NAME," +
                        "    OPS_PROVE_STAFF.OPERATION_DEPT_CODE PROVE_OPERATION_DEPT_CODE," +
                        "    OPS_PROVE_STAFF.OPERATION_DEPT_NAME PROVE_OPERATION_DEPT_NAME," +
                        "    OPS_PROVE_STAFF.OPERATION_DEPT_LEVEL PROVE_OPERATION_DEPT_LEVEL," +
                        "    OPS_PROVE_STAFF.OPERATION_UNDER_DEPT_CODE PROVE_OPERATION_UNDER_DEPT_CODE," +
                        "    OPS_PROVE_STAFF.OPERATION_UNDER_DEPT_NAME PROVE_OPERATION_UNDER_DEPT_NAME," +
                        "    OPS_PROVE_STAFF.OPERATION_UNDER_DEPT_LEVEL PROVE_OPERATION_UNDER_DEPT_LEVEL," +
                        "    OPS_PROVE_STAFF.OPERATION_WORK_DEPT_CODE PROVE_OPERATION_WORK_DEPT_CODE," +
                        "    OPS_PROVE_STAFF.OPERATION_WORK_DEPT_NAME PROVE_OPERATION_WORK_DEPT_NAME," +
                        "    OPS_PROVE_STAFF.OPERATION_WORK_DEPT_LEVEL PROVE_OPERATION_WORK_DEPT_LEVEL," +
                        "    OPS_PROVE_STAFF.OPERATION_OFFICE_CODE PROVE_OPERATION_OFFICE_CODE," +
                        "    OPS_PROVE_STAFF.OPERATION_OFFICE_NAME PROVE_OPERATION_OFFICE_NAME," +
                        "    OPS_PROVE_STAFF.OPERATION_OFFICE_SHORT_NAME PROVE_OPERATION_OFFICE_SHORT_NAME," +
                        "    OPS_PROVE.RECEIVE_OFFICE_NAME," +
                        "    CASE WHEN OPS_PROVE.PROVE_NO IS NOT NULL THEN 'พิสูจน์แล้ว' else 'ยังไม่พิสูจน์' END prove_status," +
                        "    OPS_ARREST_LAWBREAKER.LAWBREAKER_ID" +
                        "    from OPS_ARREST_INDICTMENT" +
                        "    INNER JOIN OPS_ARREST ON OPS_ARREST_INDICTMENT.ARREST_ID = OPS_ARREST.ARREST_ID" +
                        "    AND OPS_ARREST.IS_ACTIVE = 1" +
                        "    INNER JOIN OPS_ARREST_STAFF ON OPS_ARREST.ARREST_ID = OPS_ARREST_STAFF.ARREST_ID" +
                        "    AND OPS_ARREST_STAFF.CONTRIBUTOR_ID = 14" +
                        "    AND OPS_ARREST_STAFF.IS_ACTIVE = 1" +
                        "    LEFT JOIN OPS_ARREST_INDICTMENT_DETAIL ON OPS_ARREST_INDICTMENT.INDICTMENT_ID = OPS_ARREST_INDICTMENT_DETAIL.INDICTMENT_ID" +
                        "    AND OPS_ARREST_INDICTMENT_DETAIL.IS_ACTIVE = 1" +
                        "    LEFT JOIN OPS_ARREST_LAWBREAKER ON OPS_ARREST_INDICTMENT_DETAIL.LAWBREAKER_ID = OPS_ARREST_LAWBREAKER.LAWBREAKER_ID" +
                        "    AND OPS_ARREST_LAWBREAKER.IS_ACTIVE = 1" +
                        "    INNER JOIN MAS_LAW_GUILTBASE ON OPS_ARREST_INDICTMENT.GUILTBASE_ID = MAS_LAW_GUILTBASE.GUILTBASE_ID" +
                        "    AND MAS_LAW_GUILTBASE.IS_PROVE=1" +
                        "    INNER JOIN MAS_LAW_GROUP_SUBSECTION_RULE ON MAS_LAW_GUILTBASE.SUBSECTION_RULE_ID = MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_RULE_ID" +
                        "    INNER JOIN MAS_LAW_GROUP_SUBSECTION ON MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_ID = MAS_LAW_GROUP_SUBSECTION.SUBSECTION_ID" +
                        "    INNER JOIN MAS_LAW_GROUP_SECTION ON MAS_LAW_GROUP_SUBSECTION_RULE.SECTION_ID = MAS_LAW_GROUP_SECTION.SECTION_ID" +
                        "    INNER JOIN MAS_LAW_PENALTY ON MAS_LAW_GROUP_SECTION.SECTION_ID = MAS_LAW_PENALTY.SECTION_ID" +
                        "    LEFT JOIN OPS_LAWSUIT ON OPS_ARREST_INDICTMENT.INDICTMENT_ID = OPS_LAWSUIT.INDICTMENT_ID" +
                        "    LEFT JOIN OPS_LAWSUIT_STAFF ON OPS_LAWSUIT.LAWSUIT_ID = OPS_LAWSUIT_STAFF.LAWSUIT_ID" +
                        "    AND OPS_LAWSUIT_STAFF.IS_ACTIVE = 1" +
                        "    AND OPS_LAWSUIT_STAFF.CONTRIBUTOR_ID = 16" +
                        "    INNER JOIN OPS_ARREST_LOCALE ON OPS_ARREST.ARREST_ID = OPS_ARREST_LOCALE.ARREST_ID" +
                        "    INNER JOIN MAS_SUB_DISTRICT ON OPS_ARREST_LOCALE.SUB_DISTRICT_ID = MAS_SUB_DISTRICT.SUB_DISTRICT_ID" +
                        "    INNER JOIN MAS_DISTRICT ON MAS_SUB_DISTRICT.DISTRICT_ID = MAS_DISTRICT.DISTRICT_ID" +
                        "    INNER JOIN MAS_PROVINCE ON MAS_DISTRICT.PROVINCE_ID = MAS_PROVINCE.PROVINCE_ID" +
                        "    LEFT JOIN OPS_LAWSUIT_DETAIL ON OPS_ARREST_INDICTMENT_DETAIL.INDICTMENT_DETAIL_ID = OPS_LAWSUIT_DETAIL.INDICTMENT_DETAIL_ID" +
                        "    AND OPS_LAWSUIT_DETAIL.IS_ACTIVE = 1" +
                        "    LEFT JOIN OPS_PROVE on OPS_LAWSUIT.LAWSUIT_ID = OPS_PROVE.LAWSUIT_ID" +
                        "    AND OPS_PROVE.IS_ACTIVE = 1" +
                        "    AND OPS_PROVE.PROVE_TYPE = 0 " +
                        "    left join OPS_PROVE_STAFF on OPS_PROVE_STAFF.PROVE_ID= OPS_PROVE.PROVE_ID " +
                        "    and OPS_PROVE_STAFF.CONTRIBUTOR_ID = 25" +
                        "    and OPS_PROVE_STAFF.IS_ACTIVE = 1" +
                        "    where (OPS_PROVE.IS_ACTIVE=1 or OPS_PROVE.IS_ACTIVE IS NULL)" +
                        "    AND OPS_LAWSUIT.IS_ACTIVE = 1" +
                        "    AND OPS_LAWSUIT.IS_LAWSUIT = 1 " + sqlBuilder.toString()+
                        "     ORDER BY OPS_ARREST.OCCURRENCE_DATE DESC" +
                        "   ) " +
                        "   select " +
                        "    ARREST_ID," +
                        "    ARREST_CODE," +
                        "    ARREST_DATE," +
                        "    OCCURRENCE_DATE," +
                        "    INDICTMENT_ID," +
                        "    ARREST_STAFF_NAME," +
//                        "    LAWSUIT_TYPE," +
//                        "    LAWSUIT_END," +
                        "    SUBSECTION_NAME," +
                        "    GUILTBASE_NAME," +
                        "    SUB_DISTRICT_NAME_TH," +
                        "    SUB_DISTRICT_NAME_EN," +
                        "    DISTRICT_NAME_TH," +
                        "    DISTRICT_NAME_EN," +
                        "    PROVINCE_NAME_TH," +
                        "    PROVINCE_NAME_EN," +
                        "    LAWSUIT_ID," +
                        "    LAWSUILT_NO," +
                        "    LAWSUIT_DATE," +
                        "    IS_LAWSUIT," +
                        "    LAWSUIT_STAFF_NAME," +
                        "    OFFICE_NAME," +
                        "    PROVE_ID," +
                        "    PROVE_NO," +
                        "    PROVE_IS_OUTSIDE," +
                        "    PROVE_DATE," +
                        "    STAFF_ID," +
                        "    PROVE_STAFF_NAME," +
                        "    PROVE_OPERATION_POS_CODE," +
                        "    PROVE_OPREATION_POS_NAME," +
                        "    PROVE_OPREATION_POS_LEVEL," +
                        "    PROVE_OPERATION_POS_LEVEL_NAME," +
                        "    PROVE_OPERATION_DEPT_CODE," +
                        "    PROVE_OPERATION_DEPT_NAME," +
                        "    PROVE_OPERATION_DEPT_LEVEL," +
                        "    PROVE_OPERATION_UNDER_DEPT_CODE," +
                        "    PROVE_OPERATION_UNDER_DEPT_NAME," +
                        "    PROVE_OPERATION_UNDER_DEPT_LEVEL," +
                        "    PROVE_OPERATION_WORK_DEPT_CODE," +
                        "    PROVE_OPERATION_WORK_DEPT_NAME," +
                        "    PROVE_OPERATION_WORK_DEPT_LEVEL," +
                        "    PROVE_OPERATION_OFFICE_CODE," +
                        "    PROVE_OPERATION_OFFICE_NAME," +
                        "    PROVE_OPERATION_OFFICE_SHORT_NAME," +
                        "    RECEIVE_OFFICE_NAME," +
                        "    PROVE_STATUS," +
                        "    LISTAGG(LAWBREAKER_ID, ',') WITHIN GROUP (" +
                        "    ORDER BY " +
                        "        ARREST_ID," +
                        "        ARREST_CODE," +
                        "        ARREST_DATE," +
                        "        OCCURRENCE_DATE," +
                        "        INDICTMENT_ID," +
                        "        ARREST_STAFF_NAME," +
//                        "        LAWSUIT_TYPE," +
//                        "        LAWSUIT_END," +
                        "        SUBSECTION_NAME," +
                        "        GUILTBASE_NAME," +
                        "        SUB_DISTRICT_NAME_TH," +
                        "        SUB_DISTRICT_NAME_EN," +
                        "        DISTRICT_NAME_TH," +
                        "        DISTRICT_NAME_EN," +
                        "        PROVINCE_NAME_TH," +
                        "        PROVINCE_NAME_EN," +
                        "        LAWSUIT_ID," +
                        "        LAWSUILT_NO," +
                        "        LAWSUIT_DATE," +
                        "        IS_LAWSUIT," +
                        "        LAWSUIT_STAFF_NAME," +
                        "        OFFICE_NAME," +
                        "        PROVE_ID," +
                        "        PROVE_NO," +
                        "        PROVE_IS_OUTSIDE," +
                        "        PROVE_DATE," +
                        "        STAFF_ID," +
                        "        PROVE_STAFF_NAME," +
                        "        PROVE_OPERATION_POS_CODE," +
                        "        PROVE_OPREATION_POS_NAME," +
                        "        PROVE_OPREATION_POS_LEVEL," +
                        "        PROVE_OPERATION_POS_LEVEL_NAME," +
                        "        PROVE_OPERATION_DEPT_CODE," +
                        "        PROVE_OPERATION_DEPT_NAME," +
                        "        PROVE_OPERATION_DEPT_LEVEL," +
                        "        PROVE_OPERATION_UNDER_DEPT_CODE," +
                        "        PROVE_OPERATION_UNDER_DEPT_NAME," +
                        "        PROVE_OPERATION_UNDER_DEPT_LEVEL," +
                        "        PROVE_OPERATION_WORK_DEPT_CODE," +
                        "        PROVE_OPERATION_WORK_DEPT_NAME," +
                        "        PROVE_OPERATION_WORK_DEPT_LEVEL," +
                        "        PROVE_OPERATION_OFFICE_CODE," +
                        "        PROVE_OPERATION_OFFICE_NAME," +
                        "        PROVE_OPERATION_OFFICE_SHORT_NAME," +
                        "        RECEIVE_OFFICE_NAME," +
                        "        PROVE_STATUS" +
                        "    ) AS LAWBREAKER_ID" +
                        "   from temp" +
                        "   GROUP BY" +
                        "        ARREST_ID," +
                        "        ARREST_CODE," +
                        "        ARREST_DATE," +
                        "        OCCURRENCE_DATE," +
                        "        INDICTMENT_ID," +
                        "        ARREST_STAFF_NAME," +
//                        "        LAWSUIT_TYPE," +
//                        "        LAWSUIT_END," +
                        "        SUBSECTION_NAME," +
                        "        GUILTBASE_NAME," +
                        "        SUB_DISTRICT_NAME_TH," +
                        "        SUB_DISTRICT_NAME_EN," +
                        "        DISTRICT_NAME_TH," +
                        "        DISTRICT_NAME_EN," +
                        "        PROVINCE_NAME_TH," +
                        "        PROVINCE_NAME_EN," +
                        "        LAWSUIT_ID," +
                        "        LAWSUILT_NO," +
                        "        LAWSUIT_DATE," +
                        "        IS_LAWSUIT," +
                        "        LAWSUIT_STAFF_NAME," +
                        "        OFFICE_NAME," +
                        "        PROVE_ID," +
                        "        PROVE_NO," +
                        "        PROVE_IS_OUTSIDE," +
                        "        PROVE_DATE," +
                        "        STAFF_ID," +
                        "        PROVE_STAFF_NAME," +
                        "        PROVE_OPERATION_POS_CODE," +
                        "        PROVE_OPREATION_POS_NAME," +
                        "        PROVE_OPREATION_POS_LEVEL," +
                        "        PROVE_OPERATION_POS_LEVEL_NAME," +
                        "        PROVE_OPERATION_DEPT_CODE," +
                        "        PROVE_OPERATION_DEPT_NAME," +
                        "        PROVE_OPERATION_DEPT_LEVEL," +
                        "        PROVE_OPERATION_UNDER_DEPT_CODE," +
                        "        PROVE_OPERATION_UNDER_DEPT_NAME," +
                        "        PROVE_OPERATION_UNDER_DEPT_LEVEL," +
                        "        PROVE_OPERATION_WORK_DEPT_CODE," +
                        "        PROVE_OPERATION_WORK_DEPT_NAME," +
                        "        PROVE_OPERATION_WORK_DEPT_LEVEL," +
                        "        PROVE_OPERATION_OFFICE_CODE," +
                        "        PROVE_OPERATION_OFFICE_NAME," +
                        "        PROVE_OPERATION_OFFICE_SHORT_NAME," +
                        "        RECEIVE_OFFICE_NAME," +
                        "        PROVE_STATUS");

        log.info("[SQL] : " + sqlBuilder1.toString());


        @SuppressWarnings("unchecked")
        List<ProveList> dataList = getJdbcTemplate().query(sqlBuilder1.toString(), new RowMapper() {

            public ProveList mapRow(ResultSet rs, int rowNum) throws SQLException {
                ProveList item = new ProveList();
                item.setARREST_ID(rs.getInt("ARREST_ID"));
                item.setARREST_CODE(rs.getString("ARREST_CODE"));
                item.setARREST_DATE(rs.getString("ARREST_DATE"));
                item.setOCCURRENCE_DATE(rs.getString("OCCURRENCE_DATE"));
                item.setINDICTMENT_ID(rs.getInt("INDICTMENT_ID"));
                item.setARREST_STAFF_NAME(rs.getString("ARREST_STAFF_NAME"));
//                item.setLAWSUIT_TYPE(rs.getInt("LAWSUIT_TYPE"));
//                item.setLAWSUIT_END(rs.getString("LAWSUIT_END"));
                item.setSUBSECTION_NAME(rs.getString("SUBSECTION_NAME"));
                item.setGUILTBASE_NAME(rs.getString("GUILTBASE_NAME"));
                item.setSUB_DISTRICT_NAME_TH(rs.getString("SUB_DISTRICT_NAME_TH"));
                item.setSUB_DISTRICT_NAME_EN(rs.getString("SUB_DISTRICT_NAME_EN"));
                item.setDISTRICT_NAME_TH(rs.getString("DISTRICT_NAME_TH"));
                item.setDISTRICT_NAME_EN(rs.getString("DISTRICT_NAME_EN"));
                item.setPROVINCE_NAME_TH(rs.getString("PROVINCE_NAME_TH"));
                item.setPROVINCE_NAME_EN(rs.getString("PROVINCE_NAME_EN"));
                item.setLAWSUIT_ID(rs.getInt("LAWSUIT_ID"));
                item.setLAWSUILT_NO(rs.getString("LAWSUILT_NO"));
                item.setLAWSUIT_DATE(rs.getString("LAWSUIT_DATE"));
                item.setIS_LAWSUIT(rs.getInt("IS_LAWSUIT"));
                item.setLAWSUIT_STAFF_NAME(rs.getString("LAWSUIT_STAFF_NAME"));
                item.setOFFICE_NAME(rs.getString("OFFICE_NAME"));
                item.setPROVE_ID(rs.getInt("PROVE_ID"));
                item.setPROVE_NO(rs.getString("PROVE_NO"));
                item.setPROVE_IS_OUTSIDE(rs.getString("PROVE_IS_OUTSIDE"));
                item.setPROVE_DATE(rs.getString("PROVE_DATE"));
                item.setSTAFF_ID(rs.getInt("STAFF_ID"));
                item.setPROVE_STAFF_NAME(rs.getString("PROVE_STAFF_NAME"));
                item.setPROVE_OPERATION_POS_CODE(rs.getString("PROVE_OPERATION_POS_CODE"));
                item.setPROVE_OPREATION_POS_NAME(rs.getString("PROVE_OPREATION_POS_NAME"));
                item.setPROVE_OPREATION_POS_LEVEL(rs.getString("PROVE_OPREATION_POS_LEVEL"));
                item.setPROVE_OPERATION_POS_LEVEL_NAME(rs.getString("PROVE_OPERATION_POS_LEVEL_NAME"));
                item.setPROVE_OPERATION_DEPT_CODE(rs.getString("PROVE_OPERATION_DEPT_CODE"));
                item.setPROVE_OPERATION_DEPT_NAME(rs.getString("PROVE_OPERATION_DEPT_NAME"));
                item.setPROVE_OPERATION_DEPT_LEVEL(rs.getString("PROVE_OPERATION_DEPT_LEVEL"));
                item.setPROVE_OPERATION_UNDER_DEPT_CODE(rs.getString("PROVE_OPERATION_UNDER_DEPT_CODE"));
                item.setPROVE_OPERATION_UNDER_DEPT_NAME(rs.getString("PROVE_OPERATION_UNDER_DEPT_NAME"));
                item.setPROVE_OPERATION_UNDER_DEPT_LEVEL(rs.getString("PROVE_OPERATION_UNDER_DEPT_LEVEL"));
                item.setPROVE_OPERATION_WORK_DEPT_CODE(rs.getString("PROVE_OPERATION_WORK_DEPT_CODE"));
                item.setPROVE_OPERATION_WORK_DEPT_NAME(rs.getString("PROVE_OPERATION_WORK_DEPT_NAME"));
                item.setPROVE_OPERATION_WORK_DEPT_LEVEL(rs.getString("PROVE_OPERATION_WORK_DEPT_LEVEL"));
                item.setPROVE_OPERATION_OFFICE_CODE(rs.getString("PROVE_OPERATION_OFFICE_CODE"));
                item.setPROVE_OPERATION_OFFICE_NAME(rs.getString("PROVE_OPERATION_OFFICE_NAME"));
                item.setPROVE_OPERATION_OFFICE_SHORT_NAME(rs.getString("PROVE_OPERATION_OFFICE_SHORT_NAME"));
                item.setRECEIVE_OFFICE_NAME(rs.getString("RECEIVE_OFFICE_NAME"));
                item.setPROVE_STATUS(rs.getString("PROVE_STATUS"));

                if(req.getLAWSUIT_TYPE() != null && !"".equals(req.getLAWSUIT_TYPE())){
                    Integer cnt = getCount(rs.getInt("INDICTMENT_ID"),req.getLAWSUIT_TYPE());
                    if(cnt > 0){
                        item.setArrestLawbreaker(getArrestLawbreakerByLAWBREAKER_ID(rs.getInt("INDICTMENT_ID")));
                    }else{
                        List<ArrestLawbreaker> list = new ArrayList<>();
                        item.setArrestLawbreaker(list);
                        return null;
                    }
                }else{
                    item.setArrestLawbreaker(getArrestLawbreakerByLAWBREAKER_ID(rs.getInt("INDICTMENT_ID")));

                }
               // item.setArrestLawbreaker(getArrestLawbreakerByLAWBREAKER_ID(rs.getString("LAWBREAKER_ID")));
                return item;

            }
        });
        while (dataList.remove(null)) {}
        return dataList;
    }
}
