package com.fr.design.gui.ispinner;

import java.text.ParseException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.DocumentFilter;

import com.fr.stable.StableUtils;

//_denny: 实在是没有办法处理让Spinner,小写转大写。这个UpperCaseSpinner在其他地方最好不要用
public class UpperCaseSpinner extends UIBasicSpinner {
	public UpperCaseSpinner(SpinnerModel model) {
		super(model);
	}

	protected JComponent createEditor(SpinnerModel model) {
		if (model instanceof SpinnerDateModel) {
			return new DateEditor(this);
		} else if (model instanceof SpinnerListModel) {
			return new UpperCaseEditor(this);
		} else if (model instanceof SpinnerNumberModel) {
			return new NumberEditor(this);
		} else {
			return new DefaultEditor(this);
		}
	}

	private class UpperCaseEditor extends DefaultEditor {
		public UpperCaseEditor(JSpinner spinner) {
			super(spinner);
			if (!(spinner.getModel() instanceof SpinnerListModel)) {
				throw new IllegalArgumentException(
						"model not a SpinnerListModel");
			}
			getTextField().setEditable(true);
			getTextField().setFormatterFactory(
					new DefaultFormatterFactory(new ListFormatter()));
		}

		public SpinnerListModel getModel() {
			return (SpinnerListModel) (getSpinner().getModel());
		}

		private class ListFormatter extends
				JFormattedTextField.AbstractFormatter {
			private DocumentFilter filter;

			public String valueToString(Object value) throws ParseException {
				if (value == null) {
					return "";
				}
				return value.toString();
			}

			public Object stringToValue(String string)
					throws ParseException {
				//add将行数转换成ABC
				try {
					int num = Integer.parseInt(string);
					if (num > 0) {
						string = StableUtils.convertIntToABC(num);
					}
				} catch (Exception e) {
					//do nothing string is string again
				}
				return string;
			}

			protected DocumentFilter getDocumentFilter() {
				if (filter == null) {
					filter = new Filter();
				}
				return filter;
			}

			private class Filter extends DocumentFilter {
				public void replace(FilterBypass fb, int offset,
									int length, String string, AttributeSet attrs)
						throws BadLocationException {
					string = string.toUpperCase();
					if (string != null
							&& (offset + length) == fb.getDocument()
							.getLength()) {
						List list = getModel().getList();
						Object next = null;
						for (int counter = 0; counter < list.size(); counter++) {
							Object value = list.get(counter);
							String str = value.toString();

							if (str != null
									&& str.startsWith(fb.getDocument()
									.getText(0, offset)
									+ string)) {
								next = value;
								break;
							}
						}

						String value = (next != null) ? next.toString()
								: null;

						if (value != null) {
							fb.remove(0, offset + length);
							fb.insertString(0, value, null);
							getFormattedTextField().select(
									offset + string.length(),
									value.length());
							return;
						}
					}
					super.replace(fb, offset, length, string, attrs);
				}

				public void insertString(FilterBypass fb, int offset,
										 String string, AttributeSet attr)
						throws BadLocationException {
					replace(fb, offset, 0, string, attr);
				}
			}
		}
	}
}