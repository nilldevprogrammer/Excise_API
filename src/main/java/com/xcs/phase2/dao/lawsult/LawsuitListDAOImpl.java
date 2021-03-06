package com.xcs.phase2.dao.lawsult;

import com.xcs.phase2.constant.Pattern;
import com.xcs.phase2.model.lawsult.LawsuitList;
import com.xcs.phase2.request.lawsult.LawsuiltListgetByConAdvReq;
import com.xcs.phase2.request.lawsult.LawsuiltListgetByKeywordReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Service
@Transactional
public class LawsuitListDAOImpl extends LawsultExt implements LawsuitListDAO{

	private static final Logger log = LoggerFactory.getLogger(LawsuitListDAOImpl.class);

	@Override
	public List<LawsuitList> LawsuiltListgetByKeyword(LawsuiltListgetByKeywordReq req) {
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
							"  OR SUBSTR(OPS_LAWSUIT_STAFF.OPERATION_OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
							"  OR SUBSTR(OPS_LAWSUIT_STAFF.MANAGEMENT_OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
							"  OR SUBSTR(OPS_LAWSUIT_STAFF.REPRESENT_OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
							") " ;
				}else if("00".equals(req.getACCOUNT_OFFICE_CODE().substring(4, 6))) {
					str = " AND " +
							"( " +
							"  SUBSTR(MAS_SUB_DISTRICT.OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
							"  OR SUBSTR(OPS_LAWSUIT_STAFF.OPERATION_OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
							"  OR SUBSTR(OPS_LAWSUIT_STAFF.MANAGEMENT_OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
							"  OR SUBSTR(OPS_LAWSUIT_STAFF.REPRESENT_OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
							" )";
				}else {
					str = " AND" +
							" (" +
							"  MAS_SUB_DISTRICT.OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
							"  OR OPS_LAWSUIT_STAFF.OPERATION_OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
							"  OR OPS_LAWSUIT_STAFF.MANAGEMENT_OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
							"  OR OPS_LAWSUIT_STAFF.REPRESENT_OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
							" )";
				}
				
			}
			
		}
		
		StringBuilder sqlBuilderDetail = new StringBuilder()
				.append("with temp as (" +
						"    SELECT distinct" +
						"    OPS_LAWSUIT.LAWSUIT_ID," +
						"    OPS_ARREST_INDICTMENT.INDICTMENT_ID," +
						"    OPS_ARREST.ARREST_CODE," +
						"    OPS_ARREST.OCCURRENCE_DATE," +
						"    OPS_ARREST_STAFF.TITLE_NAME_TH ACCUSER_TITLE_NAME_TH," +
						"    OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH ACCUSER_TITLE_SHORT_NAME_TH," +
						"    OPS_ARREST_STAFF.FIRST_NAME ACCUSER_FIRST_NAME," +
						"    OPS_ARREST_STAFF.LAST_NAME ACCUSER_LAST_NAME," +
						"    OPS_ARREST.OFFICE_NAME ARREST_OFFICE_NAME," +
						"    MAS_LAW_GUILTBASE.GUILTBASE_NAME," +
						"    MAS_LAW_GROUP_SUBSECTION.SUBSECTION_NAME," +
						"    MAS_SUB_DISTRICT.SUB_DISTRICT_NAME_TH," +
						"    MAS_DISTRICT.DISTRICT_NAME_TH," +
						"    MAS_PROVINCE.PROVINCE_NAME_TH," +
						"    OPS_LAWSUIT.LAWSUIT_NO," +
						"    OPS_LAWSUIT.LAWSUIT_NO_YEAR," +
						"    OPS_LAWSUIT.IS_OUTSIDE LAWSUIT_IS_OUTSIDE," +
						"    OPS_LAWSUIT.LAWSUIT_DATE," +
						"    OPS_LAWSUIT.IS_LAWSUIT," +
						"    OPS_LAWSUIT_STAFF.OPERATION_OFFICE_SHORT_NAME LAWSUIT_OPERATION_OFFICE_SHORT_NAME," +
						"    OPS_LAWSUIT.OFFICE_NAME LAWSUIT_OFFICE_NAME," +
						"    OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH LAWSUIT_TITLE_SHORT_NAME_TH," +
						"    OPS_LAWSUIT_STAFF.TITLE_NAME_TH LAWSUIT_TITLE_NAME_TH," +
						"    OPS_LAWSUIT_STAFF.FIRST_NAME LAWSUIT_FIRST_NAME," +
						"    OPS_LAWSUIT_STAFF.LAST_NAME LAWSUIT_LAST_NAME," +
						"    OPS_ARREST_INDICTMENT.IS_LAWSUIT_COMPLETE INDICTMENT_IS_LAWSUIT_COMPLETE," +
						"    OPS_LAWSUIT_DETAIL.LAWSUIT_TYPE," +
						"    OPS_LAWSUIT_DETAIL.LAWSUIT_END," +
						"    OPS_ARREST_LAWBREAKER.LAWBREAKER_ID " +
						"    FROM OPS_ARREST_INDICTMENT" +
						"    INNER JOIN OPS_ARREST ON OPS_ARREST_INDICTMENT.ARREST_ID = OPS_ARREST.ARREST_ID" +
						"    INNER JOIN OPS_ARREST_STAFF ON OPS_ARREST.ARREST_ID = OPS_ARREST_STAFF.ARREST_ID" +
						"    LEFT JOIN OPS_ARREST_INDICTMENT_DETAIL ON OPS_ARREST_INDICTMENT.INDICTMENT_ID = OPS_ARREST_INDICTMENT_DETAIL.INDICTMENT_ID" +
						"    AND OPS_ARREST_INDICTMENT_DETAIL.IS_ACTIVE = 1" +
						"    LEFT JOIN OPS_ARREST_LAWBREAKER ON OPS_ARREST_INDICTMENT_DETAIL.LAWBREAKER_ID = OPS_ARREST_LAWBREAKER.LAWBREAKER_ID" +
						"    AND OPS_ARREST_LAWBREAKER.IS_ACTIVE = 1" +
						"    INNER JOIN MAS_LAW_GUILTBASE ON OPS_ARREST_INDICTMENT.GUILTBASE_ID = MAS_LAW_GUILTBASE.GUILTBASE_ID" +
						"    INNER JOIN MAS_LAW_GROUP_SUBSECTION_RULE ON MAS_LAW_GUILTBASE.SUBSECTION_RULE_ID = MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_RULE_ID" +
						"    INNER JOIN MAS_LAW_GROUP_SUBSECTION ON MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_ID = MAS_LAW_GROUP_SUBSECTION.SUBSECTION_ID" +
						"    INNER JOIN MAS_LAW_GROUP_SECTION ON MAS_LAW_GROUP_SUBSECTION_RULE.SECTION_ID = MAS_LAW_GROUP_SECTION.SECTION_ID" +
						"    INNER JOIN MAS_LAW_PENALTY ON MAS_LAW_GROUP_SECTION.SECTION_ID = MAS_LAW_PENALTY.SECTION_ID" +
						"    LEFT JOIN OPS_LAWSUIT ON OPS_ARREST_INDICTMENT.INDICTMENT_ID = OPS_LAWSUIT.INDICTMENT_ID" +
						"    AND OPS_LAWSUIT.IS_ACTIVE = 1" +
						"    LEFT JOIN OPS_LAWSUIT_STAFF ON OPS_LAWSUIT.LAWSUIT_ID = OPS_LAWSUIT_STAFF.LAWSUIT_ID" +
						"    AND OPS_LAWSUIT_STAFF.IS_ACTIVE = 1" +
						"    AND OPS_LAWSUIT_STAFF.CONTRIBUTOR_ID = 16" +
						"    INNER JOIN OPS_ARREST_LOCALE ON OPS_ARREST.ARREST_ID = OPS_ARREST_LOCALE.ARREST_ID" +
						"    INNER JOIN MAS_SUB_DISTRICT ON OPS_ARREST_LOCALE.SUB_DISTRICT_ID = MAS_SUB_DISTRICT.SUB_DISTRICT_ID" +
						"    INNER JOIN MAS_DISTRICT ON MAS_SUB_DISTRICT.DISTRICT_ID = MAS_DISTRICT.DISTRICT_ID" +
						"    INNER JOIN MAS_PROVINCE ON MAS_DISTRICT.PROVINCE_ID = MAS_PROVINCE.PROVINCE_ID" +
						"    LEFT JOIN OPS_LAWSUIT_DETAIL ON OPS_ARREST_INDICTMENT_DETAIL.INDICTMENT_DETAIL_ID = OPS_LAWSUIT_DETAIL.INDICTMENT_DETAIL_ID" +
						"    AND OPS_LAWSUIT_DETAIL.IS_ACTIVE = 1" +
						"    WHERE OPS_ARREST_INDICTMENT.IS_ACTIVE = 1" +
						"    AND OPS_ARREST.IS_ACTIVE = 1" +
						"    AND OPS_ARREST_STAFF.IS_ACTIVE = 1" +
						"    AND OPS_ARREST_STAFF.CONTRIBUTOR_ID = 14" +
						"    AND OPS_ARREST_LOCALE.IS_ACTIVE = 1" +
						"    AND" +
						"    (" +
						"        LOWER(OPS_ARREST.ARREST_CODE) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
						"        OR LOWER(OPS_ARREST_STAFF.TITLE_NAME_TH||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
						"        OR LOWER(OPS_ARREST_STAFF.TITLE_NAME_EN||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
						"        OR LOWER(OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
						"        OR LOWER(OPS_ARREST_STAFF.TITLE_SHORT_NAME_EN||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
						"        OR LOWER(OPS_ARREST_LAWBREAKER.TITLE_NAME_TH||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER('%"+req.getTEXT_SEARCH()+"%')" +
						"        OR LOWER(OPS_ARREST_LAWBREAKER.TITLE_NAME_EN||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER('%"+req.getTEXT_SEARCH()+"%')" +
						"        OR LOWER(OPS_ARREST_LAWBREAKER.TITLE_SHORT_NAME_TH||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER('%"+req.getTEXT_SEARCH()+"%')" +
						"        OR LOWER(OPS_ARREST_LAWBREAKER.TITLE_SHORT_NAME_EN||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER('%"+req.getTEXT_SEARCH()+"%')" +
						"        OR LOWER(MAS_SUB_DISTRICT.SUB_DISTRICT_NAME_TH||MAS_DISTRICT.DISTRICT_NAME_TH||MAS_PROVINCE.PROVINCE_NAME_TH) LIKE LOWER('%"+req.getTEXT_SEARCH()+"%')" +
						"        OR LOWER(MAS_LAW_GROUP_SUBSECTION.SUBSECTION_NAME) LIKE LOWER('"+req.getTEXT_SEARCH()+"%')" +
						"        OR LOWER(MAS_LAW_GUILTBASE.GUILTBASE_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
						"        OR LOWER(OPS_LAWSUIT_STAFF.TITLE_NAME_TH||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
						"        OR LOWER(OPS_LAWSUIT_STAFF.TITLE_NAME_EN||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
						"        OR LOWER(OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
						"        OR LOWER(OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_EN||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getTEXT_SEARCH()+"%',' ',''))" +
						"        OR LOWER(OPS_LAWSUIT.OFFICE_NAME) LIKE LOWER('%"+req.getTEXT_SEARCH()+"%')" +
						"    )" +str+
						"    ORDER BY OPS_LAWSUIT.LAWSUIT_DATE DESC" +
						") " +
						"select " +
						"    LAWSUIT_ID," +
						"    INDICTMENT_ID," +
						"    ARREST_CODE," +
						"    OCCURRENCE_DATE," +
						"    ACCUSER_TITLE_NAME_TH," +
						"	 ACCUSER_TITLE_SHORT_NAME_TH," +
						"    ACCUSER_FIRST_NAME," +
						"    ACCUSER_LAST_NAME," +
						"    ARREST_OFFICE_NAME," +
						"    GUILTBASE_NAME," +
						"    SUBSECTION_NAME," +
						"    SUB_DISTRICT_NAME_TH," +
						"    DISTRICT_NAME_TH," +
						"    PROVINCE_NAME_TH," +
						"    LAWSUIT_NO," +
						"    LAWSUIT_NO_YEAR," +
						"    LAWSUIT_IS_OUTSIDE," +
						"    LAWSUIT_DATE," +
						"    IS_LAWSUIT," +
						"    LAWSUIT_OPERATION_OFFICE_SHORT_NAME," +
						"    LAWSUIT_OFFICE_NAME," +
						"    LAWSUIT_TITLE_SHORT_NAME_TH," +
						"    LAWSUIT_TITLE_NAME_TH," +
						"    LAWSUIT_FIRST_NAME," +
						"    LAWSUIT_LAST_NAME," +
						"    INDICTMENT_IS_LAWSUIT_COMPLETE," +
						"    LAWSUIT_TYPE," +
						"    LAWSUIT_END," +
						"    LISTAGG(LAWBREAKER_ID, ',') WITHIN GROUP (" +
						"    ORDER BY " +
						"        LAWSUIT_ID," +
						"        INDICTMENT_ID," +
						"        ARREST_CODE," +
						"        OCCURRENCE_DATE," +
						"        ACCUSER_TITLE_NAME_TH," +
						"		 ACCUSER_TITLE_SHORT_NAME_TH," +
						"        ACCUSER_FIRST_NAME," +
						"        ACCUSER_LAST_NAME," +
						"        ARREST_OFFICE_NAME," +
						"        GUILTBASE_NAME," +
						"        SUBSECTION_NAME," +
						"        SUB_DISTRICT_NAME_TH," +
						"        DISTRICT_NAME_TH," +
						"        PROVINCE_NAME_TH," +
						"        LAWSUIT_NO," +
						"        LAWSUIT_NO_YEAR," +
						"        LAWSUIT_IS_OUTSIDE," +
						"        LAWSUIT_DATE," +
						"        IS_LAWSUIT," +
						"        LAWSUIT_OPERATION_OFFICE_SHORT_NAME," +
						"        LAWSUIT_OFFICE_NAME," +
						"        LAWSUIT_TITLE_SHORT_NAME_TH," +
						"        LAWSUIT_TITLE_NAME_TH," +
						"        LAWSUIT_FIRST_NAME," +
						"        LAWSUIT_LAST_NAME," +
						"        INDICTMENT_IS_LAWSUIT_COMPLETE," +
						"    	LAWSUIT_TYPE," +
						"    	LAWSUIT_END" +
						"    ) AS LAWBREAKER_ID" +
						" from temp" +
						" GROUP BY" +
						"        LAWSUIT_ID," +
						"        INDICTMENT_ID," +
						"        ARREST_CODE," +
						"        OCCURRENCE_DATE," +
						"        ACCUSER_TITLE_NAME_TH," +
						"		 ACCUSER_TITLE_SHORT_NAME_TH," +
						"        ACCUSER_FIRST_NAME," +
						"        ACCUSER_LAST_NAME," +
						"        ARREST_OFFICE_NAME," +
						"        GUILTBASE_NAME," +
						"        SUBSECTION_NAME," +
						"        SUB_DISTRICT_NAME_TH," +
						"        DISTRICT_NAME_TH," +
						"        PROVINCE_NAME_TH," +
						"        LAWSUIT_NO," +
						"        LAWSUIT_NO_YEAR," +
						"        LAWSUIT_IS_OUTSIDE," +
						"        LAWSUIT_DATE," +
						"        IS_LAWSUIT," +
						"        LAWSUIT_OPERATION_OFFICE_SHORT_NAME," +
						"        LAWSUIT_OFFICE_NAME," +
						"        LAWSUIT_TITLE_SHORT_NAME_TH," +
						"        LAWSUIT_TITLE_NAME_TH," +
						"        LAWSUIT_FIRST_NAME," +
						"        LAWSUIT_LAST_NAME," +
						"        INDICTMENT_IS_LAWSUIT_COMPLETE," +
						"    	 LAWSUIT_TYPE," +
						"    	 LAWSUIT_END" );
		


		log.info("[SQL]  : " + sqlBuilderDetail.toString());
		System.out.println("[SQL] [AdjustCompareListgetByKeyword]  : " + sqlBuilderDetail.toString());

		@SuppressWarnings("unchecked")
		List<LawsuitList> dataList = getJdbcTemplate().query(sqlBuilderDetail.toString(), new RowMapper() {

			public LawsuitList mapRow(ResultSet rs, int rowNum) throws SQLException {
				LawsuitList item = new LawsuitList();
				item.setLAWSUIT_ID(rs.getInt("LAWSUIT_ID"));
				item.setINDICTMENT_ID(rs.getInt("INDICTMENT_ID"));
				item.setARREST_CODE(rs.getString("ARREST_CODE"));
				item.setOCCURRENCE_DATE(rs.getString("OCCURRENCE_DATE"));
				item.setACCUSER_TITLE_NAME_TH(rs.getString("ACCUSER_TITLE_NAME_TH"));
				item.setACCUSER_TITLE_SHORT_NAME_TH(rs.getString("ACCUSER_TITLE_SHORT_NAME_TH"));
				item.setACCUSER_FIRST_NAME(rs.getString("ACCUSER_FIRST_NAME"));
				item.setACCUSER_LAST_NAME(rs.getString("ACCUSER_LAST_NAME"));
				item.setARREST_OFFICE_NAME(rs.getString("ARREST_OFFICE_NAME"));
				item.setGUILTBASE_NAME(rs.getString("GUILTBASE_NAME"));
				item.setSUBSECTION_NAME(rs.getString("SUBSECTION_NAME"));
				item.setSUB_DISTRICT_NAME_TH(rs.getString("SUB_DISTRICT_NAME_TH"));
				item.setDISTRICT_NAME_TH(rs.getString("DISTRICT_NAME_TH"));
				item.setPROVINCE_NAME_TH(rs.getString("PROVINCE_NAME_TH"));
				item.setLAWSUIT_NO(rs.getString("LAWSUIT_NO"));
				item.setLAWSUIT_NO_YEAR(rs.getString("LAWSUIT_NO_YEAR"));
				item.setLAWSUIT_IS_OUTSIDE(rs.getInt("LAWSUIT_IS_OUTSIDE"));
				item.setLAWSUIT_DATE(rs.getString("LAWSUIT_DATE"));
				item.setIS_LAWSUIT(rs.getInt("IS_LAWSUIT"));
				item.setLAWSUIT_OPERATION_OFFICE_SHORT_NAME(rs.getString("LAWSUIT_OPERATION_OFFICE_SHORT_NAME"));
				item.setLAWSUIT_OFFICE_NAME(rs.getString("LAWSUIT_OFFICE_NAME"));
				item.setLAWSUIT_TITLE_SHORT_NAME_TH(rs.getString("LAWSUIT_TITLE_SHORT_NAME_TH"));
				item.setLAWSUIT_TITLE_NAME_TH(rs.getString("LAWSUIT_TITLE_NAME_TH"));
				item.setLAWSUIT_FIRST_NAME(rs.getString("LAWSUIT_FIRST_NAME"));
				item.setLAWSUIT_LAST_NAME(rs.getString("LAWSUIT_LAST_NAME"));
				item.setINDICTMENT_IS_LAWSUIT_COMPLETE(rs.getInt("INDICTMENT_IS_LAWSUIT_COMPLETE"));
				item.setLAWSUIT_TYPE(rs.getInt("LAWSUIT_TYPE"));
				item.setLAWSUIT_END(rs.getInt("LAWSUIT_END"));

				System.out.println("================================"+rs.getString("LAWBREAKER_ID"));

				item.setLawsuitLawbreaker(getLawsuitLawbreaker(rs.getInt("INDICTMENT_ID")));
				//item.setLawsuitArrestIndictmentDetail(getLawsuitArrestIndictmentDetail(rs.getInt("INDICTMENT_ID"),0));


				return item;
			}
		});

		return dataList;
	}

	@Override
	public List<LawsuitList> LawsuiltListgetByConAdv(LawsuiltListgetByConAdvReq req) {
		// TODO Auto-generated method stub
		
		String tempOCCURRENCE_DATE_FROM   = "";
		String tempOCCURRENCE_DATE_TO = "";
		String tempLAWSUILT_DATE_START_FROM   = "";
		String tempLAWSUILT_DATE_START_TO = "";
		 
		 
		 if(!"".equals(req.getOCCURRENCE_DATE_FROM())) {
			 tempOCCURRENCE_DATE_FROM = String.format("%s %s", req.getOCCURRENCE_DATE_FROM(), Pattern.TIME_FROM);
		 }
		 
		 if(!"".equals(req.getOCCURRENCE_DATE_TO())) {
			 tempOCCURRENCE_DATE_TO = String.format("%s %s", req.getOCCURRENCE_DATE_TO(),Pattern.TIME_TO);
		 }
		 
		 if(!"".equals(req.getLAWSUILT_DATE_START_FROM())) {
			 tempLAWSUILT_DATE_START_FROM = String.format("%s %s", req.getLAWSUILT_DATE_START_FROM(),Pattern.TIME_FROM);
		 }
		 
		 if(!"".equals(req.getLAWSUILT_DATE_START_TO())) {
			 tempLAWSUILT_DATE_START_TO = String.format("%s %s", req.getLAWSUILT_DATE_START_TO(),Pattern.TIME_TO);
		 }
		
		String str = "";
		
		if(req.getACCOUNT_OFFICE_CODE() != null && !"".equals(req.getACCOUNT_OFFICE_CODE()) && !"00".equals(req.getACCOUNT_OFFICE_CODE())) {
			
			if(req.getACCOUNT_OFFICE_CODE().length() == 6) {

				if("00".equals(req.getACCOUNT_OFFICE_CODE().substring(0, 2))) {
					str = " ";
				}else if("0000".equals(req.getACCOUNT_OFFICE_CODE().substring(2, 6))) {
					str = " AND" +
							"( " +
							"  SUBSTR(MAS_SUB_DISTRICT.OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
							"  OR SUBSTR(OPS_LAWSUIT_STAFF.OPERATION_OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
							"  OR SUBSTR(OPS_LAWSUIT_STAFF.MANAGEMENT_OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
							"  OR SUBSTR(OPS_LAWSUIT_STAFF.REPRESENT_OFFICE_CODE,1,2) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,2) " +
							") " ;
				}else if("00".equals(req.getACCOUNT_OFFICE_CODE().substring(4, 6))) {
					str = " AND " +
							"( " +
							"  SUBSTR(MAS_SUB_DISTRICT.OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
							"  OR SUBSTR(OPS_LAWSUIT_STAFF.OPERATION_OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
							"  OR SUBSTR(OPS_LAWSUIT_STAFF.MANAGEMENT_OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
							"  OR SUBSTR(OPS_LAWSUIT_STAFF.REPRESENT_OFFICE_CODE,1,4) = SUBSTR('"+req.getACCOUNT_OFFICE_CODE()+"',1,4) " +
							" )";
				}else {
					str = " AND" +
							" (" +
							"  MAS_SUB_DISTRICT.OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
							"  OR OPS_LAWSUIT_STAFF.OPERATION_OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
							"  OR OPS_LAWSUIT_STAFF.MANAGEMENT_OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
							"  OR OPS_LAWSUIT_STAFF.REPRESENT_OFFICE_CODE = '"+req.getACCOUNT_OFFICE_CODE()+"'" +
							" )";
				}
				
			}
			
		}

		StringBuilder sqlBuilder = new StringBuilder();

		if(req.getARREST_CODE() != null && !"".equals(req.getARREST_CODE())) {
			sqlBuilder.append(" AND LOWER(OPS_ARREST.ARREST_CODE) LIKE LOWER(REPLACE('%"+req.getARREST_CODE()+"%',' ','')) ");
		}

		if(req.getOCCURRENCE_DATE_FROM() != null && !"".equals(req.getOCCURRENCE_DATE_FROM()) && req.getOCCURRENCE_DATE_TO() != null && !"".equals(req.getOCCURRENCE_DATE_TO())) {
			sqlBuilder.append(" AND OPS_ARREST.OCCURRENCE_DATE BETWEEN  to_date(nvl('"+tempOCCURRENCE_DATE_FROM+"','0001-01-01 00:00'),'YYYY-MM-DD HH24:MI') and to_date(nvl('"+tempOCCURRENCE_DATE_TO+"','9999-12-31 23:59'),'YYYY-MM-DD HH24:MI')");
		}

		if(req.getARREST_STAFF() != null && !"".equals(req.getARREST_STAFF())) {
			sqlBuilder.append( "AND" +
					" ( " +
					"  LOWER(OPS_ARREST_STAFF.TITLE_NAME_TH||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getARREST_STAFF()+"%',' ',''))" +
					"  OR LOWER(OPS_ARREST_STAFF.TITLE_NAME_EN||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getARREST_STAFF()+"%',' ',''))" +
					"  OR LOWER(OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getARREST_STAFF()+"%',' ',''))" +
					"  OR LOWER(OPS_ARREST_STAFF.TITLE_SHORT_NAME_EN||OPS_ARREST_STAFF.FIRST_NAME||OPS_ARREST_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getARREST_STAFF()+"%',' ',''))" +
					" )");
		}

		if(req.getLAWBREAKER_STAFF() != null && !"".equals(req.getLAWBREAKER_STAFF())) {
			sqlBuilder.append( "AND" +
					" ( " +
					"  LOWER(OPS_ARREST_LAWBREAKER.TITLE_NAME_TH||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER(REPLACE('%"+req.getLAWBREAKER_STAFF()+"%',' ','')) " +
					"  OR LOWER(OPS_ARREST_LAWBREAKER.TITLE_NAME_EN||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER(REPLACE('%"+req.getLAWBREAKER_STAFF()+"%',' ','')) " +
					"  OR LOWER(OPS_ARREST_LAWBREAKER.TITLE_SHORT_NAME_TH||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER(REPLACE('%"+req.getLAWBREAKER_STAFF()+"%',' ','')) " +
					"  OR LOWER(OPS_ARREST_LAWBREAKER.TITLE_SHORT_NAME_EN||OPS_ARREST_LAWBREAKER.FIRST_NAME||OPS_ARREST_LAWBREAKER.LAST_NAME||OPS_ARREST_LAWBREAKER.COMPANY_NAME) LIKE LOWER(REPLACE('%"+req.getLAWBREAKER_STAFF()+"%',' ','')) " +
					" )");
		}

		if(req.getARREST_LOCAL() != null && !"".equals(req.getARREST_LOCAL())) {
			sqlBuilder.append(" AND LOWER(MAS_SUB_DISTRICT.SUB_DISTRICT_NAME_TH||MAS_DISTRICT.DISTRICT_NAME_TH||MAS_PROVINCE.PROVINCE_NAME_TH) LIKE LOWER('%"+req.getARREST_LOCAL()+"%') ");
		}

		if(req.getSUBSECTION_NAME() != null && !"".equals(req.getSUBSECTION_NAME())) {
			sqlBuilder.append(" AND LOWER(MAS_LAW_GROUP_SUBSECTION.SUBSECTION_NAME) LIKE LOWER('"+req.getSUBSECTION_NAME()+"%') ");
		}

		if(req.getGUILTBASE_NAME() != null && !"".equals(req.getGUILTBASE_NAME())) {
			sqlBuilder.append(" AND LOWER(MAS_LAW_GUILTBASE.GUILTBASE_NAME) LIKE LOWER(REPLACE('%"+req.getGUILTBASE_NAME()+"%',' ','')) ");
		}

		if(req.getLAWSUIT_NO() != null && !"".equals(req.getLAWSUIT_NO())) {
			sqlBuilder.append(" AND OPS_LAWSUIT.LAWSUIT_NO = '"+req.getLAWSUIT_NO()+"' ");
		}

		if(req.getLAWSUIT_NO_YEAR() != null && !"".equals(req.getLAWSUIT_NO_YEAR())) {
			sqlBuilder.append(" AND (to_number(TO_CHAR(OPS_LAWSUIT.LAWSUIT_NO_YEAR,'YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI')) >= '"+req.getLAWSUIT_NO_YEAR()+"' AND to_number(TO_CHAR(OPS_LAWSUIT.LAWSUIT_NO_YEAR,'YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI')) <='"+req.getLAWSUIT_NO_YEAR()+"') ");
		}

		if(req.getIS_OUTSIDE() != null && !"".equals(req.getIS_OUTSIDE())) {
			sqlBuilder.append(" AND OPS_LAWSUIT.IS_OUTSIDE = '"+req.getIS_OUTSIDE()+"' ");
		}

		if(req.getLAWSUILT_DATE_START_FROM() != null && !"".equals(req.getLAWSUILT_DATE_START_FROM()) && req.getLAWSUILT_DATE_START_TO() != null && !"".equals(req.getLAWSUILT_DATE_START_TO())) {
			sqlBuilder.append(" AND OPS_LAWSUIT.LAWSUIT_DATE BETWEEN  to_date(nvl('"+tempLAWSUILT_DATE_START_FROM+"','0001-01-01 00:00'),'YYYY-MM-DD HH24:MI') and to_date(nvl('"+tempLAWSUILT_DATE_START_TO+"','9999-12-31 23:59'),'YYYY-MM-DD HH24:MI')");
		}

		if(req.getLAWSUILT_OFFICE_NAME() != null && !"".equals(req.getLAWSUILT_OFFICE_NAME())) {
			sqlBuilder.append(" AND LOWER(OPS_LAWSUIT.OFFICE_NAME) LIKE LOWER(REPLACE('%"+req.getLAWSUILT_OFFICE_NAME()+"%',' ','')) ");
		}

		if(req.getLAWSUILT_STAFF() != null && !"".equals(req.getLAWSUILT_STAFF())) {
			sqlBuilder.append( "AND" +
					" ( " +
					"  LOWER(OPS_LAWSUIT_STAFF.TITLE_NAME_TH||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getLAWSUILT_STAFF()+"%',' ',''))" +
					"  OR LOWER(OPS_LAWSUIT_STAFF.TITLE_NAME_EN||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getLAWSUILT_STAFF()+"%',' ',''))" +
					"  OR LOWER(OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getLAWSUILT_STAFF()+"%',' ',''))" +
					"  OR LOWER(OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_EN||OPS_LAWSUIT_STAFF.FIRST_NAME||OPS_LAWSUIT_STAFF.LAST_NAME) LIKE LOWER(REPLACE('%"+req.getLAWSUILT_STAFF()+"%',' ',''))" +
					" )");
		}

		if(req.getIS_LAWSUIT_COMPLETE() != null && !"".equals(req.getIS_LAWSUIT_COMPLETE())) {
			sqlBuilder.append(" AND OPS_ARREST_INDICTMENT.IS_LAWSUIT_COMPLETE = '"+req.getIS_LAWSUIT_COMPLETE()+"' ");
		}

		if(req.getLAWSUIT_TYPE() != null && !"".equals(req.getLAWSUIT_TYPE())) {
			sqlBuilder.append(" AND OPS_LAWSUIT_DETAIL.LAWSUIT_TYPE = "+req.getLAWSUIT_TYPE()+" ");
		}

		sqlBuilder.append(str);

		StringBuilder sqlBuilderDetail = new StringBuilder()
				.append("with temp as (" +
						"    SELECT distinct" +
						"    OPS_LAWSUIT.LAWSUIT_ID," +
						"    OPS_ARREST_INDICTMENT.INDICTMENT_ID," +
						"    OPS_ARREST.ARREST_CODE," +
						"    OPS_ARREST.OCCURRENCE_DATE," +
						"    OPS_ARREST_STAFF.TITLE_NAME_TH ACCUSER_TITLE_NAME_TH," +
						"    OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH ACCUSER_TITLE_SHORT_NAME_TH," +
						"    OPS_ARREST_STAFF.FIRST_NAME ACCUSER_FIRST_NAME," +
						"    OPS_ARREST_STAFF.LAST_NAME ACCUSER_LAST_NAME," +
						"    OPS_ARREST.OFFICE_NAME ARREST_OFFICE_NAME," +
						"    MAS_LAW_GUILTBASE.GUILTBASE_NAME," +
						"    MAS_LAW_GROUP_SUBSECTION.SUBSECTION_NAME," +
						"    MAS_SUB_DISTRICT.SUB_DISTRICT_NAME_TH," +
						"    MAS_DISTRICT.DISTRICT_NAME_TH," +
						"    MAS_PROVINCE.PROVINCE_NAME_TH," +
						"    OPS_LAWSUIT.LAWSUIT_NO," +
						"    OPS_LAWSUIT.LAWSUIT_NO_YEAR," +
						"    OPS_LAWSUIT.IS_OUTSIDE LAWSUIT_IS_OUTSIDE," +
						"    OPS_LAWSUIT.LAWSUIT_DATE," +
						"    OPS_LAWSUIT.IS_LAWSUIT," +
						"    OPS_LAWSUIT_STAFF.OPERATION_OFFICE_SHORT_NAME LAWSUIT_OPERATION_OFFICE_SHORT_NAME," +
						"    OPS_LAWSUIT.OFFICE_NAME LAWSUIT_OFFICE_NAME," +
						"    OPS_LAWSUIT_STAFF.TITLE_NAME_TH LAWSUIT_TITLE_NAME_TH," +
						"    OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH LAWSUIT_TITLE_SHORT_NAME_TH," +
						"    OPS_LAWSUIT_STAFF.FIRST_NAME LAWSUIT_FIRST_NAME," +
						"    OPS_LAWSUIT_STAFF.LAST_NAME LAWSUIT_LAST_NAME," +
						"    OPS_ARREST_INDICTMENT.IS_LAWSUIT_COMPLETE INDICTMENT_IS_LAWSUIT_COMPLETE," +
						"    OPS_LAWSUIT_DETAIL.LAWSUIT_TYPE," +
						"    OPS_LAWSUIT_DETAIL.LAWSUIT_END," +
						"    OPS_ARREST_LAWBREAKER.LAWBREAKER_ID " +
						"    FROM OPS_ARREST_INDICTMENT" +
						"    INNER JOIN OPS_ARREST ON OPS_ARREST_INDICTMENT.ARREST_ID = OPS_ARREST.ARREST_ID" +
						"    INNER JOIN OPS_ARREST_STAFF ON OPS_ARREST.ARREST_ID = OPS_ARREST_STAFF.ARREST_ID" +
						"    LEFT JOIN OPS_ARREST_INDICTMENT_DETAIL ON OPS_ARREST_INDICTMENT.INDICTMENT_ID = OPS_ARREST_INDICTMENT_DETAIL.INDICTMENT_ID" +
						"    AND OPS_ARREST_INDICTMENT_DETAIL.IS_ACTIVE = 1" +
						"    LEFT JOIN OPS_ARREST_LAWBREAKER ON OPS_ARREST_INDICTMENT_DETAIL.LAWBREAKER_ID = OPS_ARREST_LAWBREAKER.LAWBREAKER_ID" +
						"    AND OPS_ARREST_LAWBREAKER.IS_ACTIVE = 1" +
						"    INNER JOIN MAS_LAW_GUILTBASE ON OPS_ARREST_INDICTMENT.GUILTBASE_ID = MAS_LAW_GUILTBASE.GUILTBASE_ID" +
						"    INNER JOIN MAS_LAW_GROUP_SUBSECTION_RULE ON MAS_LAW_GUILTBASE.SUBSECTION_RULE_ID = MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_RULE_ID" +
						"    INNER JOIN MAS_LAW_GROUP_SUBSECTION ON MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_ID = MAS_LAW_GROUP_SUBSECTION.SUBSECTION_ID" +
						"    INNER JOIN MAS_LAW_GROUP_SECTION ON MAS_LAW_GROUP_SUBSECTION_RULE.SECTION_ID = MAS_LAW_GROUP_SECTION.SECTION_ID" +
						"    INNER JOIN MAS_LAW_PENALTY ON MAS_LAW_GROUP_SECTION.SECTION_ID = MAS_LAW_PENALTY.SECTION_ID" +
						"    LEFT JOIN OPS_LAWSUIT ON OPS_ARREST_INDICTMENT.INDICTMENT_ID = OPS_LAWSUIT.INDICTMENT_ID" +
						"    AND OPS_LAWSUIT.IS_ACTIVE = 1" +
						"    LEFT JOIN OPS_LAWSUIT_STAFF ON OPS_LAWSUIT.LAWSUIT_ID = OPS_LAWSUIT_STAFF.LAWSUIT_ID" +
						"    AND OPS_LAWSUIT_STAFF.IS_ACTIVE = 1" +
						"    AND OPS_LAWSUIT_STAFF.CONTRIBUTOR_ID = 16" +
						"    INNER JOIN OPS_ARREST_LOCALE ON OPS_ARREST.ARREST_ID = OPS_ARREST_LOCALE.ARREST_ID" +
						"    INNER JOIN MAS_SUB_DISTRICT ON OPS_ARREST_LOCALE.SUB_DISTRICT_ID = MAS_SUB_DISTRICT.SUB_DISTRICT_ID" +
						"    INNER JOIN MAS_DISTRICT ON MAS_SUB_DISTRICT.DISTRICT_ID = MAS_DISTRICT.DISTRICT_ID" +
						"    INNER JOIN MAS_PROVINCE ON MAS_DISTRICT.PROVINCE_ID = MAS_PROVINCE.PROVINCE_ID" +
						"    LEFT JOIN OPS_LAWSUIT_DETAIL ON OPS_ARREST_INDICTMENT_DETAIL.INDICTMENT_DETAIL_ID = OPS_LAWSUIT_DETAIL.INDICTMENT_DETAIL_ID" +
						"    AND OPS_LAWSUIT_DETAIL.IS_ACTIVE = 1" +
						"    WHERE OPS_ARREST_INDICTMENT.IS_ACTIVE = 1" +
						"    AND OPS_ARREST.IS_ACTIVE = 1" +
						"    AND OPS_ARREST_STAFF.IS_ACTIVE = 1" +
						"    AND OPS_ARREST_STAFF.CONTRIBUTOR_ID = 14" +
						"    AND OPS_ARREST_LOCALE.IS_ACTIVE = 1" + sqlBuilder.toString()+
						"    ORDER BY OPS_LAWSUIT.LAWSUIT_DATE DESC" +
						") " +
						"select " +
						"    LAWSUIT_ID," +
						"    INDICTMENT_ID," +
						"    ARREST_CODE," +
						"    OCCURRENCE_DATE," +
						"    ACCUSER_TITLE_NAME_TH," +
						"	 ACCUSER_TITLE_SHORT_NAME_TH," +
						"    ACCUSER_FIRST_NAME," +
						"    ACCUSER_LAST_NAME," +
						"    ARREST_OFFICE_NAME," +
						"    GUILTBASE_NAME," +
						"    SUBSECTION_NAME," +
						"    SUB_DISTRICT_NAME_TH," +
						"    DISTRICT_NAME_TH," +
						"    PROVINCE_NAME_TH," +
						"    LAWSUIT_NO," +
						"    LAWSUIT_NO_YEAR," +
						"    LAWSUIT_IS_OUTSIDE," +
						"    LAWSUIT_DATE," +
						"    IS_LAWSUIT," +
						"    LAWSUIT_OPERATION_OFFICE_SHORT_NAME," +
						"    LAWSUIT_OFFICE_NAME," +
						"    LAWSUIT_TITLE_NAME_TH," +
						"    LAWSUIT_TITLE_SHORT_NAME_TH," +
						"    LAWSUIT_FIRST_NAME," +
						"    LAWSUIT_LAST_NAME," +
						"    INDICTMENT_IS_LAWSUIT_COMPLETE," +
						"    LAWSUIT_TYPE," +
						"    LAWSUIT_END," +
						"    LISTAGG(LAWBREAKER_ID, ',') WITHIN GROUP (" +
						"    ORDER BY " +
						"        LAWSUIT_ID," +
						"        INDICTMENT_ID," +
						"        ARREST_CODE," +
						"        OCCURRENCE_DATE," +
						"        ACCUSER_TITLE_NAME_TH," +
						"		 ACCUSER_TITLE_SHORT_NAME_TH," +
						"        ACCUSER_FIRST_NAME," +
						"        ACCUSER_LAST_NAME," +
						"        ARREST_OFFICE_NAME," +
						"        GUILTBASE_NAME," +
						"        SUBSECTION_NAME," +
						"        SUB_DISTRICT_NAME_TH," +
						"        DISTRICT_NAME_TH," +
						"        PROVINCE_NAME_TH," +
						"        LAWSUIT_NO," +
						"        LAWSUIT_NO_YEAR," +
						"        LAWSUIT_IS_OUTSIDE," +
						"        LAWSUIT_DATE," +
						"        IS_LAWSUIT," +
						"        LAWSUIT_OPERATION_OFFICE_SHORT_NAME," +
						"        LAWSUIT_OFFICE_NAME," +
						"        LAWSUIT_TITLE_NAME_TH," +
						"        LAWSUIT_TITLE_SHORT_NAME_TH," +
						"        LAWSUIT_FIRST_NAME," +
						"        LAWSUIT_LAST_NAME," +
						"        INDICTMENT_IS_LAWSUIT_COMPLETE," +
						"    	 LAWSUIT_TYPE," +
						"    	 LAWSUIT_END" +
						"    ) AS LAWBREAKER_ID" +
						" from temp" +
						" GROUP BY" +
						"        LAWSUIT_ID," +
						"        INDICTMENT_ID," +
						"        ARREST_CODE," +
						"        OCCURRENCE_DATE," +
						"        ACCUSER_TITLE_NAME_TH," +
						"		 ACCUSER_TITLE_SHORT_NAME_TH," +
						"        ACCUSER_FIRST_NAME," +
						"        ACCUSER_LAST_NAME," +
						"        ARREST_OFFICE_NAME," +
						"        GUILTBASE_NAME," +
						"        SUBSECTION_NAME," +
						"        SUB_DISTRICT_NAME_TH," +
						"        DISTRICT_NAME_TH," +
						"        PROVINCE_NAME_TH," +
						"        LAWSUIT_NO," +
						"        LAWSUIT_NO_YEAR," +
						"        LAWSUIT_IS_OUTSIDE," +
						"        LAWSUIT_DATE," +
						"        IS_LAWSUIT," +
						"        LAWSUIT_OPERATION_OFFICE_SHORT_NAME," +
						"        LAWSUIT_OFFICE_NAME," +
						"        LAWSUIT_TITLE_NAME_TH," +
						"        LAWSUIT_TITLE_SHORT_NAME_TH," +
						"        LAWSUIT_FIRST_NAME," +
						"        LAWSUIT_LAST_NAME," +
						"        INDICTMENT_IS_LAWSUIT_COMPLETE," +
						"    	 LAWSUIT_TYPE," +
						"    	 LAWSUIT_END" );


		log.info("[SQL]  : " + sqlBuilderDetail.toString());
		System.out.println("[SQL] [LawsuiltListgetByConAdv]  : " + sqlBuilderDetail.toString());

		@SuppressWarnings("unchecked")
		List<LawsuitList> dataList = getJdbcTemplate().query(sqlBuilderDetail.toString(), new RowMapper() {

			public LawsuitList mapRow(ResultSet rs, int rowNum) throws SQLException {
				LawsuitList item = new LawsuitList();
				item.setLAWSUIT_ID(rs.getInt("LAWSUIT_ID"));
				item.setINDICTMENT_ID(rs.getInt("INDICTMENT_ID"));
				item.setARREST_CODE(rs.getString("ARREST_CODE"));
				item.setOCCURRENCE_DATE(rs.getString("OCCURRENCE_DATE"));
				item.setACCUSER_TITLE_NAME_TH(rs.getString("ACCUSER_TITLE_NAME_TH"));
				item.setACCUSER_TITLE_SHORT_NAME_TH(rs.getString("ACCUSER_TITLE_SHORT_NAME_TH"));
				item.setACCUSER_FIRST_NAME(rs.getString("ACCUSER_FIRST_NAME"));
				item.setACCUSER_LAST_NAME(rs.getString("ACCUSER_LAST_NAME"));
				item.setARREST_OFFICE_NAME(rs.getString("ARREST_OFFICE_NAME"));
				item.setGUILTBASE_NAME(rs.getString("GUILTBASE_NAME"));
				item.setSUBSECTION_NAME(rs.getString("SUBSECTION_NAME"));
				item.setSUB_DISTRICT_NAME_TH(rs.getString("SUB_DISTRICT_NAME_TH"));
				item.setDISTRICT_NAME_TH(rs.getString("DISTRICT_NAME_TH"));
				item.setPROVINCE_NAME_TH(rs.getString("PROVINCE_NAME_TH"));
				item.setLAWSUIT_NO(rs.getString("LAWSUIT_NO"));
				item.setLAWSUIT_NO_YEAR(rs.getString("LAWSUIT_NO_YEAR"));
				item.setLAWSUIT_IS_OUTSIDE(rs.getInt("LAWSUIT_IS_OUTSIDE"));
				item.setLAWSUIT_DATE(rs.getString("LAWSUIT_DATE"));
				item.setIS_LAWSUIT(rs.getInt("IS_LAWSUIT"));
				item.setLAWSUIT_OPERATION_OFFICE_SHORT_NAME(rs.getString("LAWSUIT_OPERATION_OFFICE_SHORT_NAME"));
				item.setLAWSUIT_OFFICE_NAME(rs.getString("LAWSUIT_OFFICE_NAME"));
				item.setLAWSUIT_TITLE_NAME_TH(rs.getString("LAWSUIT_TITLE_NAME_TH"));
				item.setLAWSUIT_TITLE_SHORT_NAME_TH(rs.getString("LAWSUIT_TITLE_SHORT_NAME_TH"));
				item.setLAWSUIT_FIRST_NAME(rs.getString("LAWSUIT_FIRST_NAME"));
				item.setLAWSUIT_LAST_NAME(rs.getString("LAWSUIT_LAST_NAME"));
				item.setINDICTMENT_IS_LAWSUIT_COMPLETE(rs.getInt("INDICTMENT_IS_LAWSUIT_COMPLETE"));
				item.setLAWSUIT_TYPE(rs.getInt("LAWSUIT_TYPE"));
				item.setLAWSUIT_END(rs.getInt("LAWSUIT_END"));

				item.setLawsuitLawbreaker(getLawsuitLawbreaker(rs.getInt("INDICTMENT_ID")));
				//item.setLawsuitArrestIndictmentDetail(getLawsuitArrestIndictmentDetail(rs.getInt("INDICTMENT_ID"),0));

				return item;
			}
		});

		return dataList;
	}

}
