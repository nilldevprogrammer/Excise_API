package com.xcs.phase2.model.revenue;

import lombok.Data;

import java.util.List;

@Data
public class RevenueIncPayment extends RevenueModel {


    private int INC_PAYMENT_ID;
    private int SEQ_NO;
    private String GROUPID;
    private int TAX_AMT;
    private int BRIBE_AMT;
    private int REWARD_AMT;
    private int COUNT_NUM;
    private int REVENUE_ID;
    private int IS_ACTIVE;
    private List<RevenueIncPaymentType> RevenueIncPaymentType;

}
