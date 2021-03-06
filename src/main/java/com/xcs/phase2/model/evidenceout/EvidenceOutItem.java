package com.xcs.phase2.model.evidenceout;

import lombok.Data;

import java.util.List;

@Data
public class EvidenceOutItem extends EvidenceOutModel{

        private int EVIDENCE_OUT_ITEM_ID;
        private int EVIDENCE_OUT_ID;
        private int STOCK_ID;
        private float QTY;
        private String QTY_UNIT;
        private float PRODUCT_SIZE;
        private String PRODUCT_SIZE_UNIT;
        private float NET_VOLUMN;
        private String NET_VOLUMN_UNIT;
        private int IS_RETURN;
        private int IS_ACTIVE;
        private float COST;
        private String RECEIPT_NO;
        private String BOOK_NO;
        private List<EvidenceOutStockBalance> EvidenceOutStockBalance;

}
