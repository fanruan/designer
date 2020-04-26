package com.fr.design.mainframe.messagecollect.entity;

import com.fr.design.i18n.Toolkit;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/1/8
 */
public enum DesignerErrorMessage {

    DESIGNER_PROCESS_OCCUPIED("11300201", Toolkit.i18nText("Fine-Design_Error_Process_Occupied_Message")),
    PORT_OCCUPIED("11300202", Toolkit.i18nText("Fine-Design_Error_Port_Occupied_Message")),
    FINEDB_PROBLEM("11300203", Toolkit.i18nText("Fine-Design_Error_Finedb_Problem_Message")),
    DESIGNER_OUT_OF_MEMORY("11300204", Toolkit.i18nText("Fine-Design_Error_Out_Of_Memory_Message")),
    REMOTE_DESIGN_NO_RESPONSE("11300205", Toolkit.i18nText("Fine-Design_Error_Remote_No_Response_Message")),
    UNEXCEPTED_START_FAILED("11300200", Toolkit.i18nText("Fine-Design_Error_UnExcepted_Start_Failed")),
    UNEXCEPTED_FALL_BACK("11300400 ", Toolkit.i18nText("Fine-Design_Error_UnExcepted_Fall_Back"));



    private String id;
    private String message;

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    DesignerErrorMessage(String id, String message) {
        this.id = id;
        this.message = message;
    }
}
