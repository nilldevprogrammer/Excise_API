package com.xcs.phase2.request.master;

import lombok.Data;

@Data
public class MasProductModelgetByConReq extends MasterRequest {

    private String TEXT_SEARCH;
    private String PRODUCT_MODEL_ID;
    private String PRODUCT_GROUP_CODE;
    private String PRODUCT_CATEGORY_CODE;



}