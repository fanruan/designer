/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.grid;

import java.awt.event.KeyEvent;
/**
 * 
 * @editor zhou
 * @since 2012-3-23上午10:50:08
 */
public class KeyEventWork {
	/**
	 * 屏蔽一些没有用的键
	 * @param evt
	 * @return
	 */
	public static KeyEvent processKeyEvent(KeyEvent evt) {
		int keyCode = evt.getKeyCode();
		char ch = evt.getKeyChar();

		switch (evt.getID()) {
		case KeyEvent.KEY_PRESSED:
			// get rid of keys we never need to handle
			switch (keyCode) {
			case KeyEvent.VK_ALT:
			case KeyEvent.VK_ALT_GRAPH:
			case KeyEvent.VK_CONTROL:
			case KeyEvent.VK_SHIFT:
			case KeyEvent.VK_META:
			case KeyEvent.VK_DEAD_GRAVE:
			case KeyEvent.VK_DEAD_ACUTE:
			case KeyEvent.VK_DEAD_CIRCUMFLEX:
			case KeyEvent.VK_DEAD_TILDE:
			case KeyEvent.VK_DEAD_MACRON:
			case KeyEvent.VK_DEAD_BREVE:
			case KeyEvent.VK_DEAD_ABOVEDOT:
			case KeyEvent.VK_DEAD_DIAERESIS:
			case KeyEvent.VK_DEAD_ABOVERING:
			case KeyEvent.VK_DEAD_DOUBLEACUTE:
			case KeyEvent.VK_DEAD_CARON:
			case KeyEvent.VK_DEAD_CEDILLA:
			case KeyEvent.VK_DEAD_OGONEK:
			case KeyEvent.VK_DEAD_IOTA:
			case KeyEvent.VK_DEAD_VOICED_SOUND:
			case KeyEvent.VK_DEAD_SEMIVOICED_SOUND:
			case '\0':
				return null;
			default:
				switch (keyCode) {
				case KeyEvent.VK_NUMPAD0:
				case KeyEvent.VK_NUMPAD1:
				case KeyEvent.VK_NUMPAD2:
				case KeyEvent.VK_NUMPAD3:
				case KeyEvent.VK_NUMPAD4:
				case KeyEvent.VK_NUMPAD5:
				case KeyEvent.VK_NUMPAD6:
				case KeyEvent.VK_NUMPAD7:
				case KeyEvent.VK_NUMPAD8:
				case KeyEvent.VK_NUMPAD9:
				case KeyEvent.VK_MULTIPLY:
				case KeyEvent.VK_ADD:
				case KeyEvent.VK_SUBTRACT:
				case KeyEvent.VK_DECIMAL:
				case KeyEvent.VK_DIVIDE:
					last = LAST_NUMKEYPAD;
					lastKeyTime = System.currentTimeMillis();
					return evt;
				}

				handleBrokenKeys(evt, keyCode);

				break;
			}

			return evt;

			// key typed.
		case KeyEvent.KEY_TYPED:
			// donot let \b passed. leave '\t'.
			if ((ch < 0x20 || ch == 0x7f || ch == 0xff || ch == '\b') && (ch != '\t'))
				return null;

			if ((evt.isControlDown() ^ evt.isAltDown()) || evt.isMetaDown()) {
				return null;
			}

			if (last == LAST_MOD) {
				switch (ch) {
				case 'B':
				case 'M':
				case 'X':
				case 'c':
				case '!':
				case ',':
				case '?':
					last = LAST_NOTHING;
					return null;
				}
			}

			// if the last key was a numeric keypad key
			// and NumLock is off, filter it out
			if (last == LAST_NUMKEYPAD 
					&& System.currentTimeMillis() - lastKeyTime < 750) {
				last = LAST_NOTHING;
				if ((ch >= '0' && ch <= '9') 
						|| ch == '.' 
						|| ch == '/' 
						|| ch == '*' 
						|| ch == '-' 
						|| ch == '+') {
					return null;
				}
			}
			// if the last key was a broken key, filter
			// out all except 'a'-'z' that occur 750 ms after.
			else if (last == LAST_BROKEN 
					&& System.currentTimeMillis() - lastKeyTime < 750
					&& !Character.isLetter(ch)) {
				last = LAST_NOTHING;
				return null;
			}
			// otherwise, if it was ALT, filter out everything.
			else if (last == LAST_ALT 
					&& System.currentTimeMillis() - lastKeyTime < 750) {
				last = LAST_NOTHING;
				return null;
			}

			return evt;

		case KeyEvent.KEY_RELEASED:
			return evt;
		default:
			return evt;
		}
	}

	/**
	 * A workaround for non-working NumLock status in some Java versions.
	 * 
	 * @since jEdit 4.0pre8
	 */
	public static void numericKeypadKey() {
		last = LAST_NOTHING;
	}

	// Private members

	// Static variables
	private static long lastKeyTime;

	private static int last;
	private static final int LAST_NOTHING = 0;
	private static final int LAST_ALT = 1;
	private static final int LAST_BROKEN = 2;
	private static final int LAST_NUMKEYPAD = 3;
	private static final int LAST_MOD = 4;

	// handleBrokenKeys() method
	private static void handleBrokenKeys(KeyEvent evt, int keyCode) {
		if (evt.isAltDown() 
				&& evt.isControlDown()
				&& !evt.isMetaDown()) {
			last = LAST_NOTHING;
			return;
		} else if (!(evt.isAltDown() 
				|| evt.isControlDown() 
				|| evt.isMetaDown())) {
			last = LAST_NOTHING;
			return;
		}

		if (evt.isAltDown()) {
			last = LAST_ALT;
		}

		switch (keyCode) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_DELETE:
		case KeyEvent.VK_BACK_SPACE:
		case KeyEvent.VK_TAB:
		case KeyEvent.VK_ENTER:
			last = LAST_NOTHING;
			break;
		default:
			if (keyCode < KeyEvent.VK_A || keyCode > KeyEvent.VK_Z) {
				last = LAST_BROKEN;
			} else {
				last = LAST_NOTHING;
			}

			break;
		}

		lastKeyTime = System.currentTimeMillis();
	}
}