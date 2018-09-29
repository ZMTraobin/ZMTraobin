package com.cmig.future.csportal.module.properties.base.customer;

import com.hand.hap.core.exception.BaseException;

public class CustomerInfoException extends BaseException {

    public CustomerInfoException(String code, String descriptionKey, Object[] parameters) {
        super(code, descriptionKey, parameters);
        // TODO Auto-generated constructor stub
    }


    private static final long serialVersionUID = 9046687211507280533L;
    
    public static final String MSG_ERROR_ID_CARD_ERROR = "msg.error.user.id.card.error";
    
    public static final String MSG_ERROR_MOBILE_ERROR = "msg.error.user.mobile.error";
    
    public static final String MSG_ERROR_MOBILE_REPEAT = "msg.error.user.mobile.repeat";
    
    public static final String MSG_ERROR_IDNO_REPEAT = "msg.error.user.idno.repeat";
    
    public static final String MSG_ERROR_HOUSEMAP_ERROR = "msg.error.user.owner.house.isnull";

    public static final String MSG_ERROR_COMMUNITY_BIND = "msg.error.community.has.bind";
    
    public static final String MSG_ERROR_COMMUNITY_DEFAULT = "msg.error.community.core.default";

}
