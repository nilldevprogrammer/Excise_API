package com.xcs.phase2.controller.master;


import com.xcs.phase2.constant.Message;
import com.xcs.phase2.dao.master.MasProductCategoryGroupPRCDAO;
import com.xcs.phase2.model.master.MasProductCategoryGroupPRC;
import com.xcs.phase2.request.master.MasProductCategoryGroupPRCgetByConReq;
import com.xcs.phase2.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MasProductCategoryGroupPRCController {

    private static final Logger log = LoggerFactory.getLogger(MasProductSizeController.class);

    @Autowired
    private MasProductCategoryGroupPRCDAO masProductCategoryGroupPRCDAO;

    @PostMapping(value = "/MasProductCategoryGroupPRCgetByCon")
    public ResponseEntity MasProductCategoryGroupPRCgetByCon(@RequestBody MasProductCategoryGroupPRCgetByConReq req) {

        log.info("============= Start API MasProductCategoryGroupPRCgetByCon ================");
        MessageResponse msg = new MessageResponse();
        List<MasProductCategoryGroupPRC> res = null;
        Boolean checkType = true;
        try {

            res = masProductCategoryGroupPRCDAO.MasProductCategoryGroupPRCgetByCon(req);

        } catch (Exception e) {
            checkType = false;
            msg.setIsSuccess(Message.FALSE);
            msg.setMsg(e.getMessage());

        }
        log.info("============= End API MasProductCategoryGroupPRCgetByCon =================");
        return new ResponseEntity(checkType ? res : msg, HttpStatus.OK);
    }
}
