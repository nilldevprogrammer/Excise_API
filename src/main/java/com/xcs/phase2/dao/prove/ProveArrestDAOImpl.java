package com.xcs.phase2.dao.prove;


import com.xcs.phase2.model.prove.NewProveArrest;
import com.xcs.phase2.model.prove.ProveArrest;
import com.xcs.phase2.request.prove.ProveArrestgetByConReq;
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
public class ProveArrestDAOImpl extends ProveExt implements ProveArrestDAO {

    private static final Logger log = LoggerFactory.getLogger(ProveArrestDAOImpl.class);

    @Override
    public List<ProveArrest> ProveArrestgetByCon(ProveArrestgetByConReq req) {
        // TODO Auto-generated method stub

        StringBuilder sqlBuilder = new StringBuilder()
                .append("    select" +
                        "    OPS_ARREST.ARREST_ID," +
                        "    OPS_ARREST.ARREST_CODE," +
                        "    OPS_ARREST.ARREST_DATE," +
                        "    OPS_ARREST.OFFICE_NAME," +
                        "    OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH ||' '|| OPS_ARREST_STAFF.FIRST_NAME ||' '|| OPS_ARREST_STAFF.LAST_NAME as ARREST_STAFF_NAME," +
                        "    OPS_ARREST_STAFF.OPREATION_POS_NAME as ARREST_OPREATION_POS_NAME, " +
                        "    OPS_ARREST_STAFF.OPERATION_OFFICE_NAME as ARREST_OPERATION_OFFICE_NAME," +
                        "    OPS_ARREST_STAFF.OPERATION_OFFICE_SHORT_NAME as ARREST_OPERATION_OFFICE_SHORT_NAME," +
                        "    OPS_ARREST_STAFF.MANAGEMENT_POS_NAME as ARREST_MANAGEMENT_POS_NAME," +
                        "    OPS_ARREST_STAFF.MANAGEMENT_OFFICE_NAME as ARREST_MANAGEMENT_OFFICE_NAME," +
                        "    OPS_ARREST_STAFF.MANAGEMENT_OFFICE_SHORT_NAME as ARREST_MANAGEMENT_OFFICE_SHORT_NAME," +
                        "    OPS_ARREST_STAFF.REPRESENT_POS_NAME as ARREST_REPRESENT_POS_NAME," +
                        "    OPS_ARREST_STAFF.REPRESENT_OFFICE_NAME as ARREST_REPRESENT_OFFICE_NAME," +
                        "    OPS_ARREST_STAFF.REPRESENT_OFFICE_SHORT_NAME as ARREST_REPRESENT_OFFICE_SHORT_NAME," +
                        "    MAS_LAW_GROUP_SECTION.SECTION_NAME," +
                        "    MAS_LAW_GUILTBASE.GUILTBASE_NAME," +
                        "    MAS_LAW_PENALTY.SECTION_ID," +
                        "    MAS_LAW_PENALTY.PENALTY_DESC," +
                        "    OPS_LAWSUIT.LAWSUIT_ID," +
                        "    OPS_LAWSUIT.LAWSUIT_NO," +
                        "    OPS_LAWSUIT.LAWSUIT_NO_YEAR," +
                        "    OPS_LAWSUIT.LAWSUIT_DATE," +
                        "    OPS_LAWSUIT.DELIVERY_DOC_NO_1," +
                        "    OPS_LAWSUIT.DELIVERY_DOC_NO_2" +

//                        "    OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH || ' ' || OPS_LAWSUIT_STAFF.FIRST_NAME || ' ' || OPS_LAWSUIT_STAFF.LAST_NAME as LAWSUIT_STAFF_NAME," +
//                        "    OPS_LAWSUIT_STAFF.OPREATION_POS_NAME as LAWSUIT_OPREATION_POS_NAME, " +
//                        "    OPS_LAWSUIT_STAFF.OPERATION_OFFICE_NAME as LAWSUIT_OPERATION_OFFICE_NAME," +
//                        "    OPS_LAWSUIT_STAFF.OPERATION_OFFICE_SHORT_NAME as LAWSUIT_OPERATION_OFFICE_SHORT_NAME," +
//                        "    OPS_LAWSUIT_STAFF.MANAGEMENT_POS_NAME as LAWSUIT_MANAGEMENT_POS_NAME," +
//                        "    OPS_LAWSUIT_STAFF.MANAGEMENT_OFFICE_NAME as LAWSUIT_MANAGEMENT_OFFICE_NAME," +
//                        "    OPS_LAWSUIT_STAFF.MANAGEMENT_OFFICE_SHORT_NAME as LAWSUIT_MANAGEMENT_OFFICE_SHORT_NAME," +
//                        "    OPS_LAWSUIT_STAFF.REPRESENT_POS_NAME as LAWSUIT_REPRESENT_POS_NAME," +
//                        "    OPS_LAWSUIT_STAFF.REPRESENT_OFFICE_NAME as LAWSUIT_REPRESENT_OFFICE_NAME," +
//                        "    OPS_LAWSUIT_STAFF.REPRESENT_OFFICE_SHORT_NAME as LAWSUIT_REPRESENT_OFFICE_SHORT_NAME," +
//                        "    OPS_LAWSUIT_STAFF.CONTRIBUTOR_ID" +
                        "    from OPS_ARREST" +
                        "    left join OPS_ARREST_STAFF on OPS_ARREST_STAFF.ARREST_ID=OPS_ARREST.ARREST_ID and OPS_ARREST_STAFF.CONTRIBUTOR_ID=14" +
                        "    left join OPS_ARREST_INDICTMENT on OPS_ARREST_INDICTMENT.ARREST_ID=OPS_ARREST.ARREST_ID" +
                        "    left join MAS_LAW_GUILTBASE on MAS_LAW_GUILTBASE.GUILTBASE_ID=OPS_ARREST_INDICTMENT.GUILTBASE_ID and MAS_LAW_GUILTBASE.IS_PROVE=1" +
                        "    left join MAS_LAW_GROUP_SUBSECTION_RULE on MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_RULE_ID=MAS_LAW_GUILTBASE.SUBSECTION_RULE_ID" +
                        "    left join MAS_LAW_GROUP_SECTION on MAS_LAW_GROUP_SECTION.SECTION_ID = MAS_LAW_GROUP_SUBSECTION_RULE.SECTION_ID" +
                        "    left join MAS_LAW_PENALTY on MAS_LAW_PENALTY.SECTION_ID=MAS_LAW_GROUP_SECTION.SECTION_ID" +
                        "    left join OPS_LAWSUIT on OPS_LAWSUIT.INDICTMENT_ID=OPS_ARREST_INDICTMENT.INDICTMENT_ID" +
                        //"    left join OPS_LAWSUIT_STAFF on OPS_LAWSUIT_STAFF.LAWSUIT_ID=OPS_LAWSUIT.LAWSUIT_ID and OPS_LAWSUIT_STAFF.CONTRIBUTOR_ID=16 or OPS_LAWSUIT_STAFF.CONTRIBUTOR_ID=21" +
                        "    Where OPS_LAWSUIT.LAWSUIT_ID= "+req.getLAWSUIT_ID());

        log.info("[SQL] : " + sqlBuilder.toString());


        @SuppressWarnings("unchecked")
        List<ProveArrest> dataList = getJdbcTemplate().query(sqlBuilder.toString(), new RowMapper() {

            public ProveArrest mapRow(ResultSet rs, int rowNum) throws SQLException {
                ProveArrest item = new ProveArrest();
                item.setARREST_ID(rs.getInt("ARREST_ID"));
                item.setARREST_CODE(rs.getString("ARREST_CODE"));
                item.setARREST_DATE(rs.getString("ARREST_DATE"));
                item.setOFFICE_NAME(rs.getString("OFFICE_NAME"));
                item.setARREST_STAFF_NAME(rs.getString("ARREST_STAFF_NAME"));
                item.setARREST_OPREATION_POS_NAME(rs.getString("ARREST_OPREATION_POS_NAME"));
                item.setARREST_OPERATION_OFFICE_NAME(rs.getString("ARREST_OPERATION_OFFICE_NAME"));
                item.setARREST_OPERATION_OFFICE_SHORT_NAME(rs.getString("ARREST_OPERATION_OFFICE_SHORT_NAME"));
                item.setARREST_MANAGEMENT_POS_NAME(rs.getString("ARREST_MANAGEMENT_POS_NAME"));
                item.setARREST_MANAGEMENT_OFFICE_NAME(rs.getString("ARREST_MANAGEMENT_OFFICE_NAME"));
                item.setARREST_MANAGEMENT_OFFICE_SHORT_NAME(rs.getString("ARREST_MANAGEMENT_OFFICE_SHORT_NAME"));
                item.setARREST_REPRESENT_POS_NAME(rs.getString("ARREST_REPRESENT_POS_NAME"));
                item.setARREST_REPRESENT_OFFICE_NAME(rs.getString("ARREST_REPRESENT_OFFICE_NAME"));
                item.setARREST_REPRESENT_OFFICE_SHORT_NAME(rs.getString("ARREST_REPRESENT_OFFICE_SHORT_NAME"));
                item.setSECTION_NAME(rs.getString("SECTION_NAME"));
                item.setGUILTBASE_NAME(rs.getString("GUILTBASE_NAME"));
                item.setSECTION_ID(rs.getInt("SECTION_ID"));
                item.setPENALTY_DESC(rs.getString("PENALTY_DESC"));
                item.setLAWSUIT_ID(rs.getInt("LAWSUIT_ID"));
                item.setLAWSUIT_NO(rs.getString("LAWSUIT_NO"));
                item.setLAWSUIT_NO_YEAR(rs.getString("LAWSUIT_NO_YEAR"));
                item.setLAWSUIT_DATE(rs.getString("LAWSUIT_DATE"));
                item.setDELIVERY_DOC_NO_1(rs.getString("DELIVERY_DOC_NO_1"));
                item.setDELIVERY_DOC_NO_2(rs.getString("DELIVERY_DOC_NO_2"));

                item.setProveLawsuitStaff(getProveLawsuitStaff(rs.getInt("LAWSUIT_ID")));
                item.setProveLawsuitType(getProveLawsuitType(rs.getInt("LAWSUIT_ID")));

                return item;
            }
        });

        return dataList;
    }

    @Override
    public NewProveArrest NewProveArrestgetByCon(ProveArrestgetByConReq req) {
        StringBuilder sqlBuilder = new StringBuilder()
                .append("    select" +
                        "    OPS_ARREST.ARREST_ID," +
                        "    OPS_ARREST.ARREST_CODE," +
                        "    OPS_ARREST.ARREST_DATE," +
                        "    OPS_ARREST.OFFICE_NAME," +
                        "    MAS_SUB_DISTRICT.SUB_DISTRICT_NAME_TH, " +
                        "    MAS_SUB_DISTRICT.SUB_DISTRICT_NAME_EN," +
                        "    MAS_DISTRICT.DISTRICT_NAME_TH, " +
                        "    MAS_DISTRICT.DISTRICT_NAME_EN," +
                        "    MAS_PROVINCE.PROVINCE_NAME_TH, " +
                        "    MAS_PROVINCE.PROVINCE_NAME_EN," +
                        "    case when OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH is null or OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH = 'null' then OPS_ARREST_STAFF.TITLE_NAME_TH||''|| OPS_ARREST_STAFF.FIRST_NAME ||' '|| OPS_ARREST_STAFF.LAST_NAME else" +
                        "    OPS_ARREST_STAFF.TITLE_SHORT_NAME_TH||''|| OPS_ARREST_STAFF.FIRST_NAME ||' '|| OPS_ARREST_STAFF.LAST_NAME end as ARREST_STAFF_NAME ," +
                        "    OPS_ARREST_STAFF.OPREATION_POS_NAME as ARREST_OPREATION_POS_NAME, " +
                        "    OPS_ARREST_STAFF.OPERATION_OFFICE_NAME as ARREST_OPERATION_OFFICE_NAME," +
                        "    OPS_ARREST_STAFF.OPERATION_OFFICE_SHORT_NAME as ARREST_OPERATION_OFFICE_SHORT_NAME," +
                        "    OPS_ARREST_STAFF.MANAGEMENT_POS_NAME as ARREST_MANAGEMENT_POS_NAME," +
                        "    OPS_ARREST_STAFF.MANAGEMENT_OFFICE_NAME as ARREST_MANAGEMENT_OFFICE_NAME," +
                        "    OPS_ARREST_STAFF.MANAGEMENT_OFFICE_SHORT_NAME as ARREST_MANAGEMENT_OFFICE_SHORT_NAME," +
                        "    OPS_ARREST_STAFF.REPRESENT_POS_NAME as ARREST_REPRESENT_POS_NAME," +
                        "    OPS_ARREST_STAFF.REPRESENT_OFFICE_NAME as ARREST_REPRESENT_OFFICE_NAME," +
                        "    OPS_ARREST_STAFF.REPRESENT_OFFICE_SHORT_NAME as ARREST_REPRESENT_OFFICE_SHORT_NAME," +
                        "    MAS_LAW_GROUP_SECTION.SECTION_NAME," +
                        "    MAS_LAW_GROUP_SUBSECTION.SUBSECTION_NAME," +
                        "    MAS_LAW_GUILTBASE.GUILTBASE_NAME," +
                        "    MAS_LAW_PENALTY.SECTION_ID," +
                        "    MAS_LAW_PENALTY.PENALTY_DESC," +
                        "    OPS_LAWSUIT.LAWSUIT_ID," +
                        "    OPS_LAWSUIT.LAWSUIT_NO," +
                        "    OPS_LAWSUIT.LAWSUIT_NO_YEAR," +
                        "    OPS_LAWSUIT.LAWSUIT_DATE," +
                        "    OPS_LAWSUIT.DELIVERY_DOC_NO_1," +
                        "    OPS_LAWSUIT.DELIVERY_DOC_NO_2," +
                        "    OPS_LAWSUIT.IS_OUTSIDE," +
                        "    case when OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH is null or OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH = 'null' then OPS_LAWSUIT_STAFF.TITLE_NAME_TH|| '' || OPS_LAWSUIT_STAFF.FIRST_NAME || ' ' || OPS_LAWSUIT_STAFF.LAST_NAME  else" +
                        "    OPS_LAWSUIT_STAFF.TITLE_SHORT_NAME_TH|| '' || OPS_LAWSUIT_STAFF.FIRST_NAME || ' ' || OPS_LAWSUIT_STAFF.LAST_NAME end  as LAWSUIT_STAFF_NAME ," +
                        "    OPS_LAWSUIT_STAFF.OPREATION_POS_NAME as LAWSUIT_OPREATION_POS_NAME, " +
                        "    OPS_LAWSUIT_STAFF.OPERATION_OFFICE_NAME as LAWSUIT_OPERATION_OFFICE_NAME," +
                        "    OPS_LAWSUIT_STAFF.OPERATION_OFFICE_SHORT_NAME as LAWSUIT_OPERATION_OFFICE_SHORT_NAME," +
                        "    OPS_LAWSUIT_STAFF.MANAGEMENT_POS_NAME as LAWSUIT_MANAGEMENT_POS_NAME," +
                        "    OPS_LAWSUIT_STAFF.MANAGEMENT_OFFICE_NAME as LAWSUIT_MANAGEMENT_OFFICE_NAME," +
                        "    OPS_LAWSUIT_STAFF.MANAGEMENT_OFFICE_SHORT_NAME as LAWSUIT_MANAGEMENT_OFFICE_SHORT_NAME," +
                        "    OPS_LAWSUIT_STAFF.REPRESENT_POS_NAME as LAWSUIT_REPRESENT_POS_NAME," +
                        "    OPS_LAWSUIT_STAFF.REPRESENT_OFFICE_NAME as LAWSUIT_REPRESENT_OFFICE_NAME," +
                        "    OPS_LAWSUIT_STAFF.REPRESENT_OFFICE_SHORT_NAME as LAWSUIT_REPRESENT_OFFICE_SHORT_NAME," +
                        "    OPS_LAWSUIT_STAFF.CONTRIBUTOR_ID" +
                        "    from OPS_ARREST" +
                        "    left join OPS_ARREST_STAFF on OPS_ARREST_STAFF.ARREST_ID=OPS_ARREST.ARREST_ID and OPS_ARREST_STAFF.CONTRIBUTOR_ID=14" +
                        "    left join OPS_ARREST_INDICTMENT on OPS_ARREST_INDICTMENT.ARREST_ID=OPS_ARREST.ARREST_ID" +
                        "    inner join OPS_ARREST_LOCALE on OPS_ARREST_LOCALE.ARREST_ID = OPS_ARREST.ARREST_ID " +
                        "    and OPS_ARREST_LOCALE.IS_ACTIVE=1" +
                        "    inner join MAS_SUB_DISTRICT on MAS_SUB_DISTRICT.SUB_DISTRICT_ID=OPS_ARREST_LOCALE.SUB_DISTRICT_ID" +
                        "    inner join MAS_DISTRICT on MAS_DISTRICT.DISTRICT_ID = MAS_SUB_DISTRICT.DISTRICT_ID" +
                        "    inner join MAS_PROVINCE on MAS_PROVINCE.PROVINCE_ID = MAS_DISTRICT.PROVINCE_ID" +
                        "    left join MAS_LAW_GUILTBASE on MAS_LAW_GUILTBASE.GUILTBASE_ID=OPS_ARREST_INDICTMENT.GUILTBASE_ID and MAS_LAW_GUILTBASE.IS_PROVE=1" +
                        "    left join MAS_LAW_GROUP_SUBSECTION_RULE on MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_RULE_ID=MAS_LAW_GUILTBASE.SUBSECTION_RULE_ID" +
                        "    left join MAS_LAW_GROUP_SECTION on MAS_LAW_GROUP_SECTION.SECTION_ID = MAS_LAW_GROUP_SUBSECTION_RULE.SECTION_ID" +
                        "    INNER JOIN MAS_LAW_GROUP_SUBSECTION ON MAS_LAW_GROUP_SUBSECTION_RULE.SUBSECTION_ID = MAS_LAW_GROUP_SUBSECTION.SUBSECTION_ID" +
                        "    left join MAS_LAW_PENALTY on MAS_LAW_PENALTY.SECTION_ID=MAS_LAW_GROUP_SECTION.SECTION_ID" +
                        "    left join OPS_LAWSUIT on OPS_LAWSUIT.INDICTMENT_ID=OPS_ARREST_INDICTMENT.INDICTMENT_ID" +
                        "    left join OPS_LAWSUIT_STAFF on OPS_LAWSUIT_STAFF.LAWSUIT_ID=OPS_LAWSUIT.LAWSUIT_ID and OPS_LAWSUIT_STAFF.CONTRIBUTOR_ID=16 or OPS_LAWSUIT_STAFF.CONTRIBUTOR_ID=21" +
                        "    Where OPS_LAWSUIT.LAWSUIT_ID= "+req.getLAWSUIT_ID());

        log.info("[SQL] : "+sqlBuilder.toString());

        return getJdbcTemplate().query(sqlBuilder.toString(), new ResultSetExtractor<NewProveArrest>() {

            public NewProveArrest extractData(ResultSet rs) throws SQLException,
                    DataAccessException {
                if (rs.next()) {

                    NewProveArrest item = new NewProveArrest();
                    item.setARREST_ID(rs.getInt("ARREST_ID"));
                    item.setARREST_CODE(rs.getString("ARREST_CODE"));
                    item.setARREST_DATE(rs.getString("ARREST_DATE"));
                    item.setOFFICE_NAME(rs.getString("OFFICE_NAME"));
                    item.setSUB_DISTRICT_NAME_TH(rs.getString("SUB_DISTRICT_NAME_TH"));
                    item.setSUB_DISTRICT_NAME_EN(rs.getString("SUB_DISTRICT_NAME_EN"));
                    item.setDISTRICT_NAME_TH(rs.getString("DISTRICT_NAME_TH"));
                    item.setDISTRICT_NAME_EN(rs.getString("DISTRICT_NAME_EN"));
                    item.setPROVINCE_NAME_TH(rs.getString("PROVINCE_NAME_TH"));
                    item.setPROVINCE_NAME_EN(rs.getString("PROVINCE_NAME_EN"));
                    item.setARREST_STAFF_NAME(rs.getString("ARREST_STAFF_NAME"));
                    item.setARREST_OPREATION_POS_NAME(rs.getString("ARREST_OPREATION_POS_NAME"));
                    item.setARREST_OPERATION_OFFICE_NAME(rs.getString("ARREST_OPERATION_OFFICE_NAME"));
                    item.setARREST_OPERATION_OFFICE_SHORT_NAME(rs.getString("ARREST_OPERATION_OFFICE_SHORT_NAME"));
                    item.setARREST_MANAGEMENT_POS_NAME(rs.getString("ARREST_MANAGEMENT_POS_NAME"));
                    item.setARREST_MANAGEMENT_OFFICE_NAME(rs.getString("ARREST_MANAGEMENT_OFFICE_NAME"));
                    item.setARREST_MANAGEMENT_OFFICE_SHORT_NAME(rs.getString("ARREST_MANAGEMENT_OFFICE_SHORT_NAME"));
                    item.setARREST_REPRESENT_POS_NAME(rs.getString("ARREST_REPRESENT_POS_NAME"));
                    item.setARREST_REPRESENT_OFFICE_NAME(rs.getString("ARREST_REPRESENT_OFFICE_NAME"));
                    item.setARREST_REPRESENT_OFFICE_SHORT_NAME(rs.getString("ARREST_REPRESENT_OFFICE_SHORT_NAME"));
                    item.setSECTION_NAME(rs.getString("SECTION_NAME"));
                    item.setSUBSECTION_NAME(rs.getString("SUBSECTION_NAME"));
                    item.setGUILTBASE_NAME(rs.getString("GUILTBASE_NAME"));
                    item.setSECTION_ID(rs.getInt("SECTION_ID"));
                    item.setPENALTY_DESC(rs.getString("PENALTY_DESC"));
                    item.setLAWSUIT_ID(rs.getInt("LAWSUIT_ID"));
                    item.setLAWSUIT_NO(rs.getString("LAWSUIT_NO"));
                    item.setLAWSUIT_NO_YEAR(rs.getString("LAWSUIT_NO_YEAR"));
                    item.setLAWSUIT_DATE(rs.getString("LAWSUIT_DATE"));
                    item.setDELIVERY_DOC_NO_1(rs.getString("DELIVERY_DOC_NO_1"));
                    item.setDELIVERY_DOC_NO_2(rs.getString("DELIVERY_DOC_NO_2"));
                    item.setIS_OUTSIDE(rs.getInt("IS_OUTSIDE"));
                    item.setLAWSUIT_STAFF_NAME(rs.getString("LAWSUIT_STAFF_NAME"));
                    item.setLAWSUIT_OPREATION_POS_NAME(rs.getString("LAWSUIT_OPREATION_POS_NAME"));
                    item.setLAWSUIT_OPERATION_OFFICE_NAME(rs.getString("LAWSUIT_OPERATION_OFFICE_NAME"));
                    item.setLAWSUIT_OPERATION_OFFICE_SHORT_NAME(rs.getString("LAWSUIT_OPERATION_OFFICE_SHORT_NAME"));
                    item.setLAWSUIT_MANAGEMENT_POS_NAME(rs.getString("LAWSUIT_MANAGEMENT_POS_NAME"));
                    item.setLAWSUIT_MANAGEMENT_OFFICE_NAME(rs.getString("LAWSUIT_MANAGEMENT_OFFICE_NAME"));
                    item.setLAWSUIT_MANAGEMENT_OFFICE_SHORT_NAME(rs.getString("LAWSUIT_MANAGEMENT_OFFICE_SHORT_NAME"));
                    item.setLAWSUIT_REPRESENT_POS_NAME(rs.getString("LAWSUIT_REPRESENT_POS_NAME"));
                    item.setLAWSUIT_REPRESENT_OFFICE_NAME(rs.getString("LAWSUIT_REPRESENT_OFFICE_NAME"));
                    item.setLAWSUIT_REPRESENT_OFFICE_SHORT_NAME(rs.getString("LAWSUIT_REPRESENT_OFFICE_SHORT_NAME"));
                    item.setCONTRIBUTOR_ID(rs.getInt("CONTRIBUTOR_ID"));


                    return item;
                }

                return null;
            }
        });
    }
}
