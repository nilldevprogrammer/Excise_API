package com.xcs.phase2.dao.lawsult;

import com.xcs.phase2.constant.Pattern;
import com.xcs.phase2.model.lawsult.LawsuitArrestIndictment;
import com.xcs.phase2.request.lawsult.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Service
@Transactional
public class LawsuitArrestIndictmentDAOImpl extends LawsultExt implements LawsuitArrestIndictmentDAO{

	private static final Logger log = LoggerFactory.getLogger(LawsuitArrestIndictmentDAOImpl.class);

	@Override
	public LawsuitArrestIndictment LawsuiltArrestIndictmentgetByCon(LawsuiltArrestIndictmentgetByConReq req) {
		

		
		StringBuilder sqlBuilder = new StringBuilder()
			    .append("SELECT " +
			    		"OPS_ARREST_INDICTMENT.INDICTMENT_ID," +
			    		"OPS_ARREST_INDICTMENT.ARREST_ID," +
			    		"OPS_ARREST.ARREST_CODE," +
			    		"to_char(OPS_ARREST.OCCURRENCE_DATE,'"+ Pattern.FORMAT_DATETIME+"') as OCCURRENCE_DATE," +
			    		"OPS_ARREST.OFFICE_NAME ARREST_OFFICE_NAME," +
			    		"OPS_ARREST_STAFF.TITLE_NAME_TH ACCUSER_TITLE_NAME_TH," +
			    		"OPS_ARREST_STAFF.TITLE_NAME_EN ACCUSER_TITLE_NAME_EN," +
			    		"OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH ACCUSER_TITLE_SHORT_NAME_TH," +
			    		"OPS_ARREST_STAFF.TITLE_SHORT_NAME_EN ACCUSER_TITLE_SHORT_NAME_EN," +
			    		"OPS_ARREST_STAFF.FIRST_NAME ACCUSER_FIRST_NAME," +
			    		"OPS_ARREST_STAFF.LAST_NAME ACCUSER_LAST_NAME," +
			    		"OPS_ARREST_STAFF.OPERATION_POS_CODE ACCUSER_OPERATION_POS_CODE," +
			    		"OPS_ARREST_STAFF.OPREATION_POS_NAME ACCUSER_OPREATION_POS_NAME," +
			    		"OPS_ARREST_STAFF.OPREATION_POS_LEVEL ACCUSER_OPREATION_POS_LEVEL," +
			    		"OPS_ARREST_STAFF.OPERATION_POS_LEVEL_NAME ACCUSER_OPERATION_POS_LEVEL_NAME," +
			    		"OPS_ARREST_STAFF.OPERATION_DEPT_CODE ACCUSER_OPERATION_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.OPERATION_DEPT_NAME ACCUSER_OPERATION_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.OPERATION_DEPT_LEVEL ACCUSER_OPERATION_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.OPERATION_UNDER_DEPT_CODE ACCUSER_OPERATION_UNDER_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.OPERATION_UNDER_DEPT_NAME ACCUSER_OPERATION_UNDER_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.OPERATION_UNDER_DEPT_LEVEL ACCUSER_OPERATION_UNDER_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.OPERATION_WORK_DEPT_CODE ACCUSER_OPERATION_WORK_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.OPERATION_WORK_DEPT_NAME ACCUSER_OPERATION_WORK_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.OPERATION_WORK_DEPT_LEVEL ACCUSER_OPERATION_WORK_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.OPERATION_OFFICE_CODE ACCUSER_OPERATION_OFFICE_CODE," +
			    		"OPS_ARREST_STAFF.OPERATION_OFFICE_NAME ACCUSER_OPERATION_OFFICE_NAME," +
			    		"OPS_ARREST_STAFF.OPERATION_OFFICE_SHORT_NAME ACCUSER_OPERATION_OFFICE_SHORT_NAME," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_POS_CODE ACCUSER_MANAGEMENT_POS_CODE," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_POS_NAME ACCUSER_MANAGEMENT_POS_NAME," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_POS_LEVEL ACCUSER_MANAGEMENT_POS_LEVEL," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_POS_LEVEL_NAME ACCUSER_MANAGEMENT_POS_LEVEL_NAME," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_DEPT_CODE ACCUSER_MANAGEMENT_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_DEPT_NAME ACCUSER_MANAGEMENT_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_DEPT_LEVEL ACCUSER_MANAGEMENT_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_UNDER_DEPT_CODE ACCUSER_MANAGEMENT_UNDER_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_UNDER_DEPT_NAME ACCUSER_MANAGEMENT_UNDER_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_UNDER_DEPT_LEVEL ACCUSER_MANAGEMENT_UNDER_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_WORK_DEPT_CODE ACCUSER_MANAGEMENT_WORK_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_WORK_DEPT_NAME ACCUSER_MANAGEMENT_WORK_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_WORK_DEPT_LEVEL ACCUSER_MANAGEMENT_WORK_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_OFFICE_CODE ACCUSER_MANAGEMENT_OFFICE_CODE," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_OFFICE_NAME ACCUSER_MANAGEMENT_OFFICE_NAME," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_OFFICE_SHORT_NAME ACCUSER_MANAGEMENT_OFFICE_SHORT_NAME," +
			    		"OPS_ARREST_STAFF.REPRESENT_POS_CODE ACCUSER_REPRESENT_POS_CODE," +
			    		"OPS_ARREST_STAFF.REPRESENT_POS_NAME ACCUSER_REPRESENT_POS_NAME," +
			    		"OPS_ARREST_STAFF.REPRESENT_POS_LEVEL ACCUSER_REPRESENT_POS_LEVEL," +
			    		"OPS_ARREST_STAFF.REPRESENT_POS_LEVEL_NAME ACCUSER_REPRESENT_POS_LEVEL_NAME," +
			    		"OPS_ARREST_STAFF.REPRESENT_DEPT_CODE ACCUSER_REPRESENT_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.REPRESENT_DEPT_NAME ACCUSER_REPRESENT_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.REPRESENT_DEPT_LEVEL ACCUSER_REPRESENT_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.REPRESENT_UNDER_DEPT_CODE ACCUSER_REPRESENT_UNDER_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.REPRESENT_UNDER_DEPT_NAME ACCUSER_REPRESENT_UNDER_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.REPRESENT_UNDER_DEPT_LEVEL ACCUSER_REPRESENT_UNDER_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.REPRESENT_WORK_DEPT_CODE ACCUSER_REPRESENT_WORK_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.REPRESENT_WORK_DEPT_NAME ACCUSER_REPRESENT_WORK_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.REPRESENT_WORK_DEPT_LEVEL ACCUSER_REPRESENT_WORK_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.REPRESENT_OFFICE_CODE ACCUSER_REPRESENT_OFFICE_CODE," +
			    		"OPS_ARREST_STAFF.REPRESENT_OFFICE_NAME ACCUSER_REPRESENT_OFFICE_NAME," +
			    		"OPS_ARREST_STAFF.REPRESENT_OFFICE_SHORT_NAME ACCUSER_REPRESENT_OFFICE_SHORT_NAME," +
			    		"MAS_LAW_GUILTBASE.GUILTBASE_NAME," +
			    		"MAS_LAW_GUILTBASE.FINE," +
			    		"MAS_LAW_GUILTBASE.IS_PROVE," +
			    		"MAS_LAW_GUILTBASE.IS_COMPARE," +
			    		"MAS_LAW_GROUP_SUBSECTION.SUBSECTION_NAME," +
			    		"MAS_LAW_GROUP_SUBSECTION.SUBSECTION_DESC," +
			    		"MAS_LAW_GROUP_SECTION.SECTION_NAME," +
			    		"MAS_LAW_PENALTY.PENALTY_DESC" +
			    		" FROM OPS_ARREST_INDICTMENT" +
			    		" INNER JOIN MAS_LAW_GUILTBASE ON OPS_ARREST_INDICTMENT.GUILTBASE_ID = MAS_LAW_GUILTBASE.GUILTBASE_ID" +
			    		" INNER JOIN MAS_LAW_GROUP_SUBSECTION_RULE ON MAS_LAW_GUILTBASE.SUBSECTION_RULE_ID = MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_RULE_ID" +
			    		" INNER JOIN MAS_LAW_GROUP_SUBSECTION ON MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_ID = MAS_LAW_GROUP_SUBSECTION.SUBSECTION_ID" +
			    		" INNER JOIN MAS_LAW_GROUP_SECTION ON MAS_LAW_GROUP_SUBSECTION_RULE.SECTION_ID = MAS_LAW_GROUP_SECTION.SECTION_ID" +
			    		" INNER JOIN MAS_LAW_PENALTY ON MAS_LAW_GROUP_SECTION.SECTION_ID = MAS_LAW_PENALTY.SECTION_ID" +
			    		" INNER JOIN OPS_ARREST ON OPS_ARREST_INDICTMENT.ARREST_ID = OPS_ARREST.ARREST_ID" +
			    		" INNER JOIN OPS_ARREST_STAFF ON OPS_ARREST.ARREST_ID = OPS_ARREST_STAFF.ARREST_ID" +
			    		" WHERE OPS_ARREST_INDICTMENT.IS_ACTIVE = 1" +
			    		" AND OPS_ARREST.IS_ACTIVE = 1" +
			    		" AND OPS_ARREST_STAFF.IS_ACTIVE = 1" +
			    		" AND OPS_ARREST_STAFF.CONTRIBUTOR_ID = 14" +
			    		" AND OPS_ARREST_INDICTMENT.INDICTMENT_ID = '"+req.getINDICTMENT_ID()+"' ");

				log.info("[SQL] : "+sqlBuilder.toString());
		
		return getJdbcTemplate().query(sqlBuilder.toString(), new ResultSetExtractor<LawsuitArrestIndictment>() {

			public LawsuitArrestIndictment extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					
					LawsuitArrestIndictment item = new LawsuitArrestIndictment();
					item.setINDICTMENT_ID(rs.getInt("INDICTMENT_ID"));
					item.setARREST_ID(rs.getInt("ARREST_ID"));
					item.setARREST_CODE(rs.getString("ARREST_CODE"));
					item.setOCCURRENCE_DATE(rs.getString("OCCURRENCE_DATE"));
					item.setARREST_OFFICE_NAME(rs.getString("ARREST_OFFICE_NAME"));
					item.setACCUSER_TITLE_NAME_TH(rs.getString("ACCUSER_TITLE_NAME_TH"));
					item.setACCUSER_TITLE_NAME_EN(rs.getString("ACCUSER_TITLE_NAME_EN"));
					item.setACCUSER_TITLE_SHORT_NAME_TH(rs.getString("ACCUSER_TITLE_SHORT_NAME_TH"));
					item.setACCUSER_TITLE_SHORT_NAME_EN(rs.getString("ACCUSER_TITLE_SHORT_NAME_EN"));
					item.setACCUSER_FIRST_NAME(rs.getString("ACCUSER_FIRST_NAME"));
					item.setACCUSER_LAST_NAME(rs.getString("ACCUSER_LAST_NAME"));
					item.setACCUSER_OPERATION_POS_CODE(rs.getString("ACCUSER_OPERATION_POS_CODE"));
					item.setACCUSER_OPREATION_POS_NAME(rs.getString("ACCUSER_OPREATION_POS_NAME"));
					item.setACCUSER_OPREATION_POS_LEVEL(rs.getString("ACCUSER_OPREATION_POS_LEVEL"));
					item.setACCUSER_OPERATION_POS_LEVEL_NAME(rs.getString("ACCUSER_OPERATION_POS_LEVEL_NAME"));
					item.setACCUSER_OPERATION_DEPT_CODE(rs.getString("ACCUSER_OPERATION_DEPT_CODE"));
					item.setACCUSER_OPERATION_DEPT_NAME(rs.getString("ACCUSER_OPERATION_DEPT_NAME"));
					item.setACCUSER_OPERATION_DEPT_LEVEL(rs.getInt("ACCUSER_OPERATION_DEPT_LEVEL"));
					item.setACCUSER_OPERATION_UNDER_DEPT_CODE(rs.getString("ACCUSER_OPERATION_UNDER_DEPT_CODE"));
					item.setACCUSER_OPERATION_UNDER_DEPT_NAME(rs.getString("ACCUSER_OPERATION_UNDER_DEPT_NAME"));
					item.setACCUSER_OPERATION_UNDER_DEPT_LEVEL(rs.getInt("ACCUSER_OPERATION_UNDER_DEPT_LEVEL"));
					item.setACCUSER_OPERATION_WORK_DEPT_CODE(rs.getString("ACCUSER_OPERATION_WORK_DEPT_CODE"));
					item.setACCUSER_OPERATION_WORK_DEPT_NAME(rs.getString("ACCUSER_OPERATION_WORK_DEPT_NAME"));
					item.setACCUSER_OPERATION_WORK_DEPT_LEVEL(rs.getInt("ACCUSER_OPERATION_WORK_DEPT_LEVEL"));
					item.setACCUSER_OPERATION_OFFICE_CODE(rs.getString("ACCUSER_OPERATION_OFFICE_CODE"));
					item.setACCUSER_OPERATION_OFFICE_NAME(rs.getString("ACCUSER_OPERATION_OFFICE_NAME"));
					item.setACCUSER_OPERATION_OFFICE_SHORT_NAME(rs.getString("ACCUSER_OPERATION_OFFICE_SHORT_NAME"));
					item.setACCUSER_MANAGEMENT_POS_CODE(rs.getString("ACCUSER_MANAGEMENT_POS_CODE"));
					item.setACCUSER_MANAGEMENT_POS_NAME(rs.getString("ACCUSER_MANAGEMENT_POS_NAME"));
					item.setACCUSER_MANAGEMENT_POS_LEVEL(rs.getString("ACCUSER_MANAGEMENT_POS_LEVEL"));
					item.setACCUSER_MANAGEMENT_POS_LEVEL_NAME(rs.getString("ACCUSER_MANAGEMENT_POS_LEVEL_NAME"));
					item.setACCUSER_MANAGEMENT_DEPT_CODE(rs.getString("ACCUSER_MANAGEMENT_DEPT_CODE"));
					item.setACCUSER_MANAGEMENT_DEPT_NAME(rs.getString("ACCUSER_MANAGEMENT_DEPT_NAME"));
					item.setACCUSER_MANAGEMENT_DEPT_LEVEL(rs.getInt("ACCUSER_MANAGEMENT_DEPT_LEVEL"));
					item.setACCUSER_MANAGEMENT_UNDER_DEPT_CODE(rs.getString("ACCUSER_MANAGEMENT_UNDER_DEPT_CODE"));
					item.setACCUSER_MANAGEMENT_UNDER_DEPT_NAME(rs.getString("ACCUSER_MANAGEMENT_UNDER_DEPT_NAME"));
					item.setACCUSER_MANAGEMENT_UNDER_DEPT_LEVEL(rs.getInt("ACCUSER_MANAGEMENT_UNDER_DEPT_LEVEL"));
					item.setACCUSER_MANAGEMENT_WORK_DEPT_CODE(rs.getString("ACCUSER_MANAGEMENT_WORK_DEPT_CODE"));
					item.setACCUSER_MANAGEMENT_WORK_DEPT_NAME(rs.getString("ACCUSER_MANAGEMENT_WORK_DEPT_NAME"));
					item.setACCUSER_MANAGEMENT_WORK_DEPT_LEVEL(rs.getInt("ACCUSER_MANAGEMENT_WORK_DEPT_LEVEL"));
					item.setACCUSER_MANAGEMENT_OFFICE_CODE(rs.getString("ACCUSER_MANAGEMENT_OFFICE_CODE"));
					item.setACCUSER_MANAGEMENT_OFFICE_NAME(rs.getString("ACCUSER_MANAGEMENT_OFFICE_NAME"));
					item.setACCUSER_MANAGEMENT_OFFICE_SHORT_NAME(rs.getString("ACCUSER_MANAGEMENT_OFFICE_SHORT_NAME"));
					item.setACCUSER_REPRESENT_POS_CODE(rs.getString("ACCUSER_REPRESENT_POS_CODE"));
					item.setACCUSER_REPRESENT_POS_NAME(rs.getString("ACCUSER_REPRESENT_POS_NAME"));
					item.setACCUSER_REPRESENT_POS_LEVEL(rs.getString("ACCUSER_REPRESENT_POS_LEVEL"));
					item.setACCUSER_REPRESENT_POS_LEVEL_NAME(rs.getString("ACCUSER_REPRESENT_POS_LEVEL_NAME"));
					item.setACCUSER_REPRESENT_DEPT_CODE(rs.getString("ACCUSER_REPRESENT_DEPT_CODE"));
					item.setACCUSER_REPRESENT_DEPT_NAME(rs.getString("ACCUSER_REPRESENT_DEPT_NAME"));
					item.setACCUSER_REPRESENT_DEPT_LEVEL(rs.getInt("ACCUSER_REPRESENT_DEPT_LEVEL"));
					item.setACCUSER_REPRESENT_UNDER_DEPT_CODE(rs.getString("ACCUSER_REPRESENT_UNDER_DEPT_CODE"));
					item.setACCUSER_REPRESENT_UNDER_DEPT_NAME(rs.getString("ACCUSER_REPRESENT_UNDER_DEPT_NAME"));
					item.setACCUSER_REPRESENT_UNDER_DEPT_LEVEL(rs.getInt("ACCUSER_REPRESENT_UNDER_DEPT_LEVEL"));
					item.setACCUSER_REPRESENT_WORK_DEPT_CODE(rs.getString("ACCUSER_REPRESENT_WORK_DEPT_CODE"));
					item.setACCUSER_REPRESENT_WORK_DEPT_NAME(rs.getString("ACCUSER_REPRESENT_WORK_DEPT_NAME"));
					item.setACCUSER_REPRESENT_WORK_DEPT_LEVEL(rs.getInt("ACCUSER_REPRESENT_WORK_DEPT_LEVEL"));
					item.setACCUSER_REPRESENT_OFFICE_CODE(rs.getString("ACCUSER_REPRESENT_OFFICE_CODE"));
					item.setACCUSER_REPRESENT_OFFICE_NAME(rs.getString("ACCUSER_REPRESENT_OFFICE_NAME"));
					item.setACCUSER_REPRESENT_OFFICE_SHORT_NAME(rs.getString("ACCUSER_REPRESENT_OFFICE_SHORT_NAME"));
					item.setGUILTBASE_NAME(rs.getString("GUILTBASE_NAME"));
					item.setFINE(rs.getString("FINE"));
					item.setIS_PROVE(rs.getInt("IS_PROVE"));
					item.setIS_COMPARE(rs.getInt("IS_COMPARE"));
					item.setSUBSECTION_NAME(rs.getString("SUBSECTION_NAME"));
					item.setSUBSECTION_DESC(rs.getString("SUBSECTION_DESC"));
					item.setSECTION_NAME(rs.getString("SECTION_NAME"));
					item.setPENALTY_DESC(rs.getString("PENALTY_DESC"));
					
					item.setLawsuitArrestIndictmentProduct(getLawsuitArrestIndictmentProduct(rs.getInt("INDICTMENT_ID")));
					item.setLawsuitArrestIndictmentDetail(getLawsuitArrestIndictmentDetail(rs.getInt("INDICTMENT_ID"),0));
					item.setLawsuitNotice(getLawsuitNotice(rs.getInt("ARREST_ID")));
					item.setLawsuitLocale(getLawsuitLocale(rs.getInt("ARREST_ID")));

                	
					return item;
				}
				
				return null;
			}
		});
	}

	@Override
	public Boolean LawsuiltArrestIndictmentupdIndictmentComplete(LawsuiltArrestIndictmentupdIndictmentCompleteReq req) {
		
		StringBuilder sqlBuilder = new StringBuilder().append("UPDATE OPS_ARREST_INDICTMENT SET IS_LAWSUIT_COMPLETE = '1' WHERE INDICTMENT_ID = '"+req.getINDICTMENT_ID()+"' ");
		
		getJdbcTemplate().update(sqlBuilder.toString(), new Object[] {});
		return true;
	}

	@Override
	public Boolean LawsuiltArrestIndictmentupdDeleteIndictmentComplete(
			LawsuiltArrestIndictmentupdDeleteIndictmentCompleteReq req) {
		
		StringBuilder sqlBuilder = new StringBuilder().append("UPDATE OPS_ARREST_INDICTMENT SET IS_LAWSUIT_COMPLETE = '0' WHERE INDICTMENT_ID = '"+req.getINDICTMENT_ID()+"' ");
		
		getJdbcTemplate().update(sqlBuilder.toString(), new Object[] {});
		return true;
	}

	@Override
	public Boolean LawsuiltArrestIndictmentupdArrestComplete(LawsuiltArrestIndictmentupdArrestCompleteReq req) {
		
		StringBuilder sqlBuilder = new StringBuilder().append("UPDATE OPS_ARREST SET IS_LAWSUIT_COMPLETE = '1' WHERE ARREST_ID = '"+req.getARREST_ID()+"' ");
		
		getJdbcTemplate().update(sqlBuilder.toString(), new Object[] {});
		return true;
	}

	@Override
	public Boolean LawsuiltArrestIndictmentupdDeleteArrestComplete(
			LawsuiltArrestIndictmentupdDeleteArrestCompleteReq req) {
		
		StringBuilder sqlBuilder = new StringBuilder().append("UPDATE OPS_ARREST SET IS_LAWSUIT_COMPLETE = '0' WHERE ARREST_ID = '"+req.getARREST_ID()+"' ");
		
		getJdbcTemplate().update(sqlBuilder.toString(), new Object[] {});
		return true;
	}

	@Override
	public List<LawsuitArrestIndictment> LawsuiltArrestIndictmentCheckComplete(
			LawsuiltArrestIndictmentCheckNotCompleteReq req) {
		

		
		StringBuilder sqlBuilder = new StringBuilder()
			    .append("with temp as (" +
			    		" SELECT " +
			    		"OPS_ARREST_INDICTMENT.INDICTMENT_ID," +
			    		"OPS_ARREST_INDICTMENT.ARREST_ID," +
			    		"OPS_ARREST.ARREST_CODE," +
			    		"OPS_ARREST.OCCURRENCE_DATE," +
			    		"OPS_ARREST.OFFICE_NAME ARREST_OFFICE_NAME," +
			    		"OPS_ARREST_STAFF.TITLE_NAME_TH ACCUSER_TITLE_NAME_TH," +
			    		"OPS_ARREST_STAFF.TITLE_NAME_EN ACCUSER_TITLE_NAME_EN," +
			    		"OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH ACCUSER_TITLE_SHORT_NAME_TH," +
			    		"OPS_ARREST_STAFF.TITLE_SHORT_NAME_EN ACCUSER_TITLE_SHORT_NAME_EN," +
			    		"OPS_ARREST_STAFF.FIRST_NAME ACCUSER_FIRST_NAME," +
			    		"OPS_ARREST_STAFF.LAST_NAME ACCUSER_LAST_NAME," +
			    		"OPS_ARREST_STAFF.OPERATION_POS_CODE ACCUSER_OPERATION_POS_CODE," +
			    		"OPS_ARREST_STAFF.OPREATION_POS_NAME ACCUSER_OPREATION_POS_NAME," +
			    		"OPS_ARREST_STAFF.OPREATION_POS_LEVEL ACCUSER_OPREATION_POS_LEVEL," +
			    		"OPS_ARREST_STAFF.OPERATION_POS_LEVEL_NAME ACCUSER_OPERATION_POS_LEVEL_NAME," +
			    		"OPS_ARREST_STAFF.OPERATION_DEPT_CODE ACCUSER_OPERATION_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.OPERATION_DEPT_NAME ACCUSER_OPERATION_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.OPERATION_DEPT_LEVEL ACCUSER_OPERATION_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.OPERATION_UNDER_DEPT_CODE ACCUSER_OPERATION_UNDER_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.OPERATION_UNDER_DEPT_NAME ACCUSER_OPERATION_UNDER_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.OPERATION_UNDER_DEPT_LEVEL ACCUSER_OPERATION_UNDER_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.OPERATION_WORK_DEPT_CODE ACCUSER_OPERATION_WORK_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.OPERATION_WORK_DEPT_NAME ACCUSER_OPERATION_WORK_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.OPERATION_WORK_DEPT_LEVEL ACCUSER_OPERATION_WORK_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.OPERATION_OFFICE_CODE ACCUSER_OPERATION_OFFICE_CODE," +
			    		"OPS_ARREST_STAFF.OPERATION_OFFICE_NAME ACCUSER_OPERATION_OFFICE_NAME," +
			    		"OPS_ARREST_STAFF.OPERATION_OFFICE_SHORT_NAME ACCUSER_OPERATION_OFFICE_SHORT_NAME," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_POS_CODE ACCUSER_MANAGEMENT_POS_CODE," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_POS_NAME ACCUSER_MANAGEMENT_POS_NAME," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_POS_LEVEL ACCUSER_MANAGEMENT_POS_LEVEL," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_POS_LEVEL_NAME ACCUSER_MANAGEMENT_POS_LEVEL_NAME," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_DEPT_CODE ACCUSER_MANAGEMENT_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_DEPT_NAME ACCUSER_MANAGEMENT_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_DEPT_LEVEL ACCUSER_MANAGEMENT_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_UNDER_DEPT_CODE ACCUSER_MANAGEMENT_UNDER_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_UNDER_DEPT_NAME ACCUSER_MANAGEMENT_UNDER_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_UNDER_DEPT_LEVEL ACCUSER_MANAGEMENT_UNDER_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_WORK_DEPT_CODE ACCUSER_MANAGEMENT_WORK_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_WORK_DEPT_NAME ACCUSER_MANAGEMENT_WORK_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_WORK_DEPT_LEVEL ACCUSER_MANAGEMENT_WORK_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_OFFICE_CODE ACCUSER_MANAGEMENT_OFFICE_CODE," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_OFFICE_NAME ACCUSER_MANAGEMENT_OFFICE_NAME," +
			    		"OPS_ARREST_STAFF.MANAGEMENT_OFFICE_SHORT_NAME ACCUSER_MANAGEMENT_OFFICE_SHORT_NAME," +
			    		"OPS_ARREST_STAFF.REPRESENT_POS_CODE ACCUSER_REPRESENT_POS_CODE," +
			    		"OPS_ARREST_STAFF.REPRESENT_POS_NAME ACCUSER_REPRESENT_POS_NAME," +
			    		"OPS_ARREST_STAFF.REPRESENT_POS_LEVEL ACCUSER_REPRESENT_POS_LEVEL," +
			    		"OPS_ARREST_STAFF.REPRESENT_POS_LEVEL_NAME ACCUSER_REPRESENT_POS_LEVEL_NAME," +
			    		"OPS_ARREST_STAFF.REPRESENT_DEPT_CODE ACCUSER_REPRESENT_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.REPRESENT_DEPT_NAME ACCUSER_REPRESENT_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.REPRESENT_DEPT_LEVEL ACCUSER_REPRESENT_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.REPRESENT_UNDER_DEPT_CODE ACCUSER_REPRESENT_UNDER_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.REPRESENT_UNDER_DEPT_NAME ACCUSER_REPRESENT_UNDER_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.REPRESENT_UNDER_DEPT_LEVEL ACCUSER_REPRESENT_UNDER_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.REPRESENT_WORK_DEPT_CODE ACCUSER_REPRESENT_WORK_DEPT_CODE," +
			    		"OPS_ARREST_STAFF.REPRESENT_WORK_DEPT_NAME ACCUSER_REPRESENT_WORK_DEPT_NAME," +
			    		"OPS_ARREST_STAFF.REPRESENT_WORK_DEPT_LEVEL ACCUSER_REPRESENT_WORK_DEPT_LEVEL," +
			    		"OPS_ARREST_STAFF.REPRESENT_OFFICE_CODE ACCUSER_REPRESENT_OFFICE_CODE," +
			    		"OPS_ARREST_STAFF.REPRESENT_OFFICE_NAME ACCUSER_REPRESENT_OFFICE_NAME," +
			    		"OPS_ARREST_STAFF.REPRESENT_OFFICE_SHORT_NAME ACCUSER_REPRESENT_OFFICE_SHORT_NAME," +
			    		"MAS_LAW_GUILTBASE.GUILTBASE_NAME," +
			    		"MAS_LAW_GUILTBASE.FINE," +
			    		"MAS_LAW_GUILTBASE.IS_PROVE," +
			    		"MAS_LAW_GUILTBASE.IS_COMPARE," +
			    		"MAS_LAW_GROUP_SUBSECTION.SUBSECTION_NAME," +
			    		"MAS_LAW_GROUP_SUBSECTION.SUBSECTION_DESC," +
			    		"MAS_LAW_GROUP_SECTION.SECTION_NAME," +
			    		"MAS_LAW_PENALTY.PENALTY_DESC" +
			    		" FROM OPS_ARREST_INDICTMENT" +
			    		" INNER JOIN MAS_LAW_GUILTBASE ON OPS_ARREST_INDICTMENT.GUILTBASE_ID = MAS_LAW_GUILTBASE.GUILTBASE_ID" +
			    		" INNER JOIN MAS_LAW_GROUP_SUBSECTION_RULE ON MAS_LAW_GUILTBASE.SUBSECTION_RULE_ID = MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_RULE_ID" +
			    		" INNER JOIN MAS_LAW_GROUP_SUBSECTION ON MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_ID = MAS_LAW_GROUP_SUBSECTION.SUBSECTION_ID" +
			    		" INNER JOIN MAS_LAW_GROUP_SECTION ON MAS_LAW_GROUP_SUBSECTION_RULE.SECTION_ID = MAS_LAW_GROUP_SECTION.SECTION_ID" +
			    		" INNER JOIN MAS_LAW_PENALTY ON MAS_LAW_GROUP_SECTION.SECTION_ID = MAS_LAW_PENALTY.SECTION_ID" +
			    		" INNER JOIN OPS_ARREST ON OPS_ARREST_INDICTMENT.ARREST_ID = OPS_ARREST.ARREST_ID" +
			    		" INNER JOIN OPS_ARREST_STAFF ON OPS_ARREST.ARREST_ID = OPS_ARREST_STAFF.ARREST_ID" +
			    		" WHERE OPS_ARREST_INDICTMENT.IS_ACTIVE = 1" +
			    		" AND OPS_ARREST.IS_ACTIVE = 1" +
			    		" AND OPS_ARREST_STAFF.IS_ACTIVE = 1" +
			    		" AND OPS_ARREST_STAFF.CONTRIBUTOR_ID = 14" +
			    		" AND OPS_ARREST_INDICTMENT.IS_LAWSUIT_COMPLETE = 1" +
			    		" AND OPS_ARREST.ARREST_ID = '"+req.getARREST_ID()+"' " +
			    		" ) select DISTINCT * from temp");

				log.info("[SQL] : "+sqlBuilder.toString());
				
				System.out.println("SQL : "+sqlBuilder.toString());

		List<LawsuitArrestIndictment> dataList = getJdbcTemplate().query(sqlBuilder.toString(), new RowMapper() {

			public LawsuitArrestIndictment mapRow(ResultSet rs, int rowNum) throws SQLException {
				LawsuitArrestIndictment item = new LawsuitArrestIndictment();
				item.setINDICTMENT_ID(rs.getInt("INDICTMENT_ID"));
				item.setARREST_ID(rs.getInt("ARREST_ID"));
				item.setARREST_CODE(rs.getString("ARREST_CODE"));
				item.setOCCURRENCE_DATE(rs.getString("OCCURRENCE_DATE"));
				item.setARREST_OFFICE_NAME(rs.getString("ARREST_OFFICE_NAME"));
				item.setACCUSER_TITLE_NAME_TH(rs.getString("ACCUSER_TITLE_NAME_TH"));
				item.setACCUSER_TITLE_NAME_EN(rs.getString("ACCUSER_TITLE_NAME_EN"));
				item.setACCUSER_TITLE_SHORT_NAME_TH(rs.getString("ACCUSER_TITLE_SHORT_NAME_TH"));
				item.setACCUSER_TITLE_SHORT_NAME_EN(rs.getString("ACCUSER_TITLE_SHORT_NAME_EN"));
				item.setACCUSER_FIRST_NAME(rs.getString("ACCUSER_FIRST_NAME"));
				item.setACCUSER_LAST_NAME(rs.getString("ACCUSER_LAST_NAME"));
				item.setACCUSER_OPERATION_POS_CODE(rs.getString("ACCUSER_OPERATION_POS_CODE"));
				item.setACCUSER_OPREATION_POS_NAME(rs.getString("ACCUSER_OPREATION_POS_NAME"));
				item.setACCUSER_OPREATION_POS_LEVEL(rs.getString("ACCUSER_OPREATION_POS_LEVEL"));
				item.setACCUSER_OPERATION_POS_LEVEL_NAME(rs.getString("ACCUSER_OPERATION_POS_LEVEL_NAME"));
				item.setACCUSER_OPERATION_DEPT_CODE(rs.getString("ACCUSER_OPERATION_DEPT_CODE"));
				item.setACCUSER_OPERATION_DEPT_NAME(rs.getString("ACCUSER_OPERATION_DEPT_NAME"));
				item.setACCUSER_OPERATION_DEPT_LEVEL(rs.getInt("ACCUSER_OPERATION_DEPT_LEVEL"));
				item.setACCUSER_OPERATION_UNDER_DEPT_CODE(rs.getString("ACCUSER_OPERATION_UNDER_DEPT_CODE"));
				item.setACCUSER_OPERATION_UNDER_DEPT_NAME(rs.getString("ACCUSER_OPERATION_UNDER_DEPT_NAME"));
				item.setACCUSER_OPERATION_UNDER_DEPT_LEVEL(rs.getInt("ACCUSER_OPERATION_UNDER_DEPT_LEVEL"));
				item.setACCUSER_OPERATION_WORK_DEPT_CODE(rs.getString("ACCUSER_OPERATION_WORK_DEPT_CODE"));
				item.setACCUSER_OPERATION_WORK_DEPT_NAME(rs.getString("ACCUSER_OPERATION_WORK_DEPT_NAME"));
				item.setACCUSER_OPERATION_WORK_DEPT_LEVEL(rs.getInt("ACCUSER_OPERATION_WORK_DEPT_LEVEL"));
				item.setACCUSER_OPERATION_OFFICE_CODE(rs.getString("ACCUSER_OPERATION_OFFICE_CODE"));
				item.setACCUSER_OPERATION_OFFICE_NAME(rs.getString("ACCUSER_OPERATION_OFFICE_NAME"));
				item.setACCUSER_OPERATION_OFFICE_SHORT_NAME(rs.getString("ACCUSER_OPERATION_OFFICE_SHORT_NAME"));
				item.setACCUSER_MANAGEMENT_POS_CODE(rs.getString("ACCUSER_MANAGEMENT_POS_CODE"));
				item.setACCUSER_MANAGEMENT_POS_NAME(rs.getString("ACCUSER_MANAGEMENT_POS_NAME"));
				item.setACCUSER_MANAGEMENT_POS_LEVEL(rs.getString("ACCUSER_MANAGEMENT_POS_LEVEL"));
				item.setACCUSER_MANAGEMENT_POS_LEVEL_NAME(rs.getString("ACCUSER_MANAGEMENT_POS_LEVEL_NAME"));
				item.setACCUSER_MANAGEMENT_DEPT_CODE(rs.getString("ACCUSER_MANAGEMENT_DEPT_CODE"));
				item.setACCUSER_MANAGEMENT_DEPT_NAME(rs.getString("ACCUSER_MANAGEMENT_DEPT_NAME"));
				item.setACCUSER_MANAGEMENT_DEPT_LEVEL(rs.getInt("ACCUSER_MANAGEMENT_DEPT_LEVEL"));
				item.setACCUSER_MANAGEMENT_UNDER_DEPT_CODE(rs.getString("ACCUSER_MANAGEMENT_UNDER_DEPT_CODE"));
				item.setACCUSER_MANAGEMENT_UNDER_DEPT_NAME(rs.getString("ACCUSER_MANAGEMENT_UNDER_DEPT_NAME"));
				item.setACCUSER_MANAGEMENT_UNDER_DEPT_LEVEL(rs.getInt("ACCUSER_MANAGEMENT_UNDER_DEPT_LEVEL"));
				item.setACCUSER_MANAGEMENT_WORK_DEPT_CODE(rs.getString("ACCUSER_MANAGEMENT_WORK_DEPT_CODE"));
				item.setACCUSER_MANAGEMENT_WORK_DEPT_NAME(rs.getString("ACCUSER_MANAGEMENT_WORK_DEPT_NAME"));
				item.setACCUSER_MANAGEMENT_WORK_DEPT_LEVEL(rs.getInt("ACCUSER_MANAGEMENT_WORK_DEPT_LEVEL"));
				item.setACCUSER_MANAGEMENT_OFFICE_CODE(rs.getString("ACCUSER_MANAGEMENT_OFFICE_CODE"));
				item.setACCUSER_MANAGEMENT_OFFICE_NAME(rs.getString("ACCUSER_MANAGEMENT_OFFICE_NAME"));
				item.setACCUSER_MANAGEMENT_OFFICE_SHORT_NAME(rs.getString("ACCUSER_MANAGEMENT_OFFICE_SHORT_NAME"));
				item.setACCUSER_REPRESENT_POS_CODE(rs.getString("ACCUSER_REPRESENT_POS_CODE"));
				item.setACCUSER_REPRESENT_POS_NAME(rs.getString("ACCUSER_REPRESENT_POS_NAME"));
				item.setACCUSER_REPRESENT_POS_LEVEL(rs.getString("ACCUSER_REPRESENT_POS_LEVEL"));
				item.setACCUSER_REPRESENT_POS_LEVEL_NAME(rs.getString("ACCUSER_REPRESENT_POS_LEVEL_NAME"));
				item.setACCUSER_REPRESENT_DEPT_CODE(rs.getString("ACCUSER_REPRESENT_DEPT_CODE"));
				item.setACCUSER_REPRESENT_DEPT_NAME(rs.getString("ACCUSER_REPRESENT_DEPT_NAME"));
				item.setACCUSER_REPRESENT_DEPT_LEVEL(rs.getInt("ACCUSER_REPRESENT_DEPT_LEVEL"));
				item.setACCUSER_REPRESENT_UNDER_DEPT_CODE(rs.getString("ACCUSER_REPRESENT_UNDER_DEPT_CODE"));
				item.setACCUSER_REPRESENT_UNDER_DEPT_NAME(rs.getString("ACCUSER_REPRESENT_UNDER_DEPT_NAME"));
				item.setACCUSER_REPRESENT_UNDER_DEPT_LEVEL(rs.getInt("ACCUSER_REPRESENT_UNDER_DEPT_LEVEL"));
				item.setACCUSER_REPRESENT_WORK_DEPT_CODE(rs.getString("ACCUSER_REPRESENT_WORK_DEPT_CODE"));
				item.setACCUSER_REPRESENT_WORK_DEPT_NAME(rs.getString("ACCUSER_REPRESENT_WORK_DEPT_NAME"));
				item.setACCUSER_REPRESENT_WORK_DEPT_LEVEL(rs.getInt("ACCUSER_REPRESENT_WORK_DEPT_LEVEL"));
				item.setACCUSER_REPRESENT_OFFICE_CODE(rs.getString("ACCUSER_REPRESENT_OFFICE_CODE"));
				item.setACCUSER_REPRESENT_OFFICE_NAME(rs.getString("ACCUSER_REPRESENT_OFFICE_NAME"));
				item.setACCUSER_REPRESENT_OFFICE_SHORT_NAME(rs.getString("ACCUSER_REPRESENT_OFFICE_SHORT_NAME"));
				item.setGUILTBASE_NAME(rs.getString("GUILTBASE_NAME"));
				item.setFINE(rs.getString("FINE"));
				item.setIS_PROVE(rs.getInt("IS_PROVE"));
				item.setIS_COMPARE(rs.getInt("IS_COMPARE"));
				item.setSUBSECTION_NAME(rs.getString("SUBSECTION_NAME"));
				item.setSUBSECTION_DESC(rs.getString("SUBSECTION_DESC"));
				item.setSECTION_NAME(rs.getString("SECTION_NAME"));
				item.setPENALTY_DESC(rs.getString("PENALTY_DESC"));

				item.setLawsuitArrestIndictmentProduct(getLawsuitArrestIndictmentProduct(rs.getInt("INDICTMENT_ID")));
				item.setLawsuitArrestIndictmentDetail(getLawsuitArrestIndictmentDetail(rs.getInt("INDICTMENT_ID"),0));
				item.setLawsuitNotice(getLawsuitNotice(rs.getInt("ARREST_ID")));


				return item;
			}
		});

		return dataList;
	}
}