package com.fr.design.mainframe.widget.wrappers.primitive;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class ByteWrapper implements Encoder, Decoder {

    @Override
    public String encode(Object v) {
        if(v==null)
            return "0";
        return v.toString();
    }

    @Override
    public Object decode(String txt) {
        if(txt==null)
            return Byte.valueOf((byte)0);
        return Byte.parseByte(txt);        
    }

    @Override
    public void validate(String txt) throws ValidationException {
        try{
             Byte.parseByte(txt); 
        }catch(NumberFormatException nfe){
            throw new ValidationException(nfe.getMessage());
        }
    }
}