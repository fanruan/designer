package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class DateWrapper implements Encoder, Decoder {

	@Override
	public Object decode(String txt) {
		return txt;
	}

	@Override
	public void validate(String txt) throws ValidationException {

	}

	@Override
	public String encode(Object v) {
		return (String) v;
	}

}