package com.fr.design.utils;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-13
 * Time: 下午4:24
 */
public class ColorRoutines {
    //一些常量
    private static final int GRB_MAX = 256;
    private static final int GRB_VALUE = 255;
    private static final int ANGLE = 360;
    private static final int BRIGHTNESS = 100;
    private static final int HALF_ANGLE = 60;
    private static final int ANGLE120 = 120;
    private static final int ANGLE240 = 240;
    private static final int GREY_MAX = 128;
    private static final int GREY_VALUE = 127;
    private static final int RGB = 1;
    private static final int RBG = 2;
    private static final int GBR = 3;
    private static final int GRB = 4;
    private static final int BRG = 5;
    private static final int BGR = 6;

    private static final double VALUE1 = 1.1;
    private boolean preserveGrey;
    private int chue, csat, cbri;
    private int fr, fg, fb;
    int hi, lo, md;
    boolean hiIsR, hiIsG, hiIsB;
    boolean mdIsR, mdIsG, mdIsB;
    boolean loIsR, loIsG, loIsB;

    public ColorRoutines(Color c) {
        setHSB(c.getRed(), c.getGreen(), c.getBlue());
    }

    public ColorRoutines(int hue, int sat, int bri, boolean preserveGrey) {
        chue = hue;
        csat = sat;
        cbri = bri;
        this.preserveGrey = preserveGrey;

        Color c = Color.getHSBColor((float) ((double) chue / ANGLE), 1.0f, 1.0f);
        fr = c.getRed();
        fg = c.getGreen();
        fb = c.getBlue();

        // sort colors - 6 options
        if (fr >= fg && fg >= fb) {
            hi = fr;
            md = fg;
            lo = fb;
            hiIsR = true;
            mdIsG = true;
            loIsB = true;
        } else if (fr >= fb && fb >= fg) {
            hi = fr;
            md = fb;
            lo = fg;
            hiIsR = true;
            mdIsB = true;
            loIsG = true;
        } else if (fg >= fr && fr >= fb) {
            hi = fg;
            md = fr;
            lo = fb;
            hiIsG = true;
            mdIsR = true;
            loIsB = true;
        } else if (fg >= fb && fb >= fr) {
            hi = fg;
            md = fb;
            lo = fr;
            hiIsG = true;
            mdIsB = true;
            loIsR = true;
        } else if (fb >= fg && fg >= fr) {
            hi = fb;
            md = fg;
            lo = fr;
            hiIsB = true;
            mdIsG = true;
            loIsR = true;
        } else if (fb >= fr && fr >= fg) {
            hi = fb;
            md = fr;
            lo = fg;
            hiIsB = true;
            mdIsR = true;
            loIsG = true;
        }
    }

    private void setHSB(int r, int g, int b) {
        chue = getHue(r, g, b);
        csat = getSaturation(r, g, b);
        cbri = getBrightness(r, g, b);
    }

    public static Color getAverage(Color c1, Color c2) {
        int r = (int) Math.round((c1.getRed() + c2.getRed()) / 2.0);
        int g = (int) Math.round((c1.getGreen() + c2.getGreen()) / 2.0);
        int b = (int) Math.round((c1.getBlue() + c2.getBlue()) / 2.0);

        return new Color(r, g, b);
    }

    // i >= 0 <= d
    // c1 ist Einblendfarbe
    // c2 ist Hintergrundfarbe
    public static Color getGradient(Color c1, Color c2, int d, int i) {
        if (i == 0) {
            return c1;
        }
        if (i == d) {
            return c2;
        }
        double d2 = i * VALUE1 / d;
        double d1 = 1.0 - d2;

        int r = (int) Math.round(c1.getRed() * d1 + c2.getRed() * d2);
        int g = (int) Math.round(c1.getGreen() * d1 + c2.getGreen() * d2);
        int b = (int) Math.round(c1.getBlue() * d1 + c2.getBlue() * d2);

        return new Color(r, g, b);
    }

    public static Color getMaxSaturation(Color c, int memH) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        if (r == g && r == b) {
            return c;
        }
        int ta = 0, tb = 0, tc = 0;
        int mapping = RGB;

        calculateRC(r, g, b, ta, tb, tc, mapping);
        if (tb == 0) {
            return c;
        }

        int nc = Math.min(GRB_VALUE, tc + tb);
        int nb = Math.max(0, tc + tb - GRB_VALUE);
        int na = ta;
        int h = 0, mh = 0;
        int ba = 0, delta = ANGLE;
        Color rc = null;
        Color switchValue = null;

        switch (mapping) {
            case RGB:
                switchValue = switchRGB(nc, nb, na, h, ba, delta, mh, ta, memH, rc);
                break;
            case RBG:
                switchValue = switchRBG(nc, nb, na, h, ba, delta, mh, ta, memH, rc);
                break;
            case GBR:
                switchValue = switchGBR(nc, nb, na, h, ba, delta, mh, ta, memH, rc);
                break;
            case GRB:
                switchValue = switchGRB(nc, nb, na, h, ba, delta, mh, ta, memH, rc);
                break;
            case BRG:
                switchValue = switchBRG(nc, nb, na, h, ba, delta, mh, ta, memH, rc);
                break;
            case BGR:
                switchValue = switchBGR(nc, nb, na, h, ba, delta, mh, ta, memH, rc);
                break;
        }
        if (switchValue != null) {
            return switchValue;
        }
        return rc;
    }

    private static void calculateRC(int r, int g, int b, int ta, int tb, int tc, int mapping) {
        if (r >= g && r >= b) {
            tc = r;
            if (g == b) {
                ta = g;
                tb = b;
                mapping = RGB;
            } else if (g > b) {
                ta = g;
                tb = b;
                mapping = RGB;
            } else {
                tb = g;
                ta = b;
                mapping = RBG;
            }
        } else if (g >= r && g >= b) {
            tc = g;
            if (r == b) {
                ta = r;
                tb = b;
                mapping = GRB;
            } else if (r > b) {
                ta = r;
                tb = b;
                mapping = GRB;
            } else {
                tb = r;
                ta = b;
                mapping = GBR;
            }
        } else if (b >= r && b >= g) {
            tc = b;

            if (r == g) {
                ta = r;
                tb = g;
                mapping = BRG;
            } else if (r > g) {
                ta = r;
                tb = g;
                mapping = BRG;
            } else {
                tb = r;
                ta = g;
                mapping = BGR;
            }
        }
    }

    private static Color switchRGB(int nc, int nb, int na, int h, int ba, int delta, int mh, int ta, int memH, Color rc) {
        h = getHue(nc, na, nb);
        mh = h;
        boolean isTrue1 = mh < memH && h > memH;
        boolean isTrue2 = mh > memH && h < memH;
        while (h != memH && na < GRB_MAX) {
            h = getHue(nc, ++na, nb);
            if (na == GRB_MAX) {
                break;
            }
            if (h == memH) {
                return new Color(nc, na, nb);
            } else if (isTrue1 || isTrue2) {
                return new Color(nc, na, nb);
            } else if (Math.abs(h - memH) < delta) {
                delta = Math.abs(h - memH);
                ba = na;
            }
            mh = h;
        }

        if (h != memH) {
            h = getHue(nc, na, nb);
            mh = h;
            na = ta;
            while (h != memH && na >= 0) {
                h = getHue(nc, --na, nb);
                if (na == -1) {
                    break;
                }
                if (h == memH) {
                    return new Color(nc, na, nb);
                } else if (isTrue1 || isTrue2) {
                    return new Color(nc, na, nb);
                } else if (Math.abs(h - memH) < delta) {
                    delta = Math.abs(h - memH);
                    ba = na;
                }
                mh = h;
            }
        }
        if (na == GRB_MAX | na == -1) {
            na = ba;
        }
        rc = new Color(nc, na, nb);
        return null;
    }

    private static Color switchRBG(int nc, int nb, int na, int h, int ba, int delta, int mh, int ta, int memH, Color rc) {
        h = getHue(nc, nb, na);
        mh = h;
        boolean isTrue1 = mh < memH && h > memH;
        boolean isTrue2 = mh > memH && h < memH;
        while (h != memH && na < GRB_MAX) {
            h = getHue(nc, nb, ++na);
            if (na == GRB_MAX) {
                break;
            }
            if (h == memH) {
                return new Color(nc, nb, na);
            } else if (isTrue1 || isTrue2) {
                return new Color(nc, nb, na);
            } else if (Math.abs(h - memH) < delta) {
                delta = Math.abs(h - memH);
                ba = na;
            }
            mh = h;
        }

        if (h != memH) {
            h = getHue(nc, na, nb);
            mh = h;
            na = ta;
            while (h != memH && na >= 0) {
                h = getHue(nc, nb, --na);
                if (na == -1) {
                    break;
                }
                if (h == memH) {
                    return new Color(nc, nb, na);
                } else if (isTrue1 || isTrue2) {
                    return new Color(nc, nb, na);
                } else if (Math.abs(h - memH) < delta) {
                    delta = Math.abs(h - memH);
                    ba = na;
                }
                mh = h;
            }
        }
        if (na == GRB_MAX | na == -1) {
            na = ba;
        }
        rc = new Color(nc, nb, na);
        return null;
    }

    private static Color switchGBR(int nc, int nb, int na, int h, int ba, int delta, int mh, int ta, int memH, Color rc) {
        h = getHue(nb, nc, na);
        mh = h;
        boolean isTrue1 = mh < memH && h > memH;
        boolean isTrue2 = mh > memH && h < memH;
        while (h != memH && na < GRB_MAX) {
            h = getHue(nb, nc, ++na);
            if (na == GRB_MAX) {
                break;
            }
            if (h == memH) {
                return new Color(nb, nc, na);
            } else if (isTrue1 || isTrue2) {
                return new Color(nb, nc, na);
            } else if (Math.abs(h - memH) < delta) {
                delta = Math.abs(h - memH);
                ba = na;
            }
            mh = h;
        }

        if (h != memH) {
            h = getHue(nc, na, nb);
            mh = h;
            na = ta;
            while (h != memH && na >= 0) {
                h = getHue(nb, nc, --na);
                if (na == -1) {
                    break;
                }

                if (h == memH) {
                    return new Color(nb, nc, na);
                } else if (isTrue1 || isTrue2) {
                    return new Color(nb, nc, na);
                } else if (Math.abs(h - memH) < delta) {
                    delta = Math.abs(h - memH);
                    ba = na;
                }
                mh = h;
            }
        }

        if (na == GRB_MAX | na == -1) {
            na = ba;
        }

        rc = new Color(nb, nc, na);
        return null;
    }

    private static Color switchGRB(int nc, int nb, int na, int h, int ba, int delta, int mh, int ta, int memH, Color rc) {
        h = getHue(na, nc, nb);
        mh = h;
        boolean isTrue1 = mh < memH && h > memH;
        boolean isTrue2 = mh > memH && h < memH;
        while (h != memH && na < GRB_MAX) {
            h = getHue(++na, nc, nb);
            if (na == GRB_MAX) {
                break;
            }
            if (h == memH) {
                return new Color(na, nc, nb);
            } else if (isTrue1 || isTrue2) {
                return new Color(na, nc, nb);
            } else if (Math.abs(h - memH) < delta) {
                delta = Math.abs(h - memH);
                ba = na;
            }
            mh = h;
        }

        if (h != memH) {
            h = getHue(nc, na, nb);
            mh = h;
            na = ta;
            while (h != memH && na >= 0) {
                h = getHue(--na, nc, nb);
                if (na == -1) {
                    break;
                }

                if (h == memH) {
                    return new Color(na, nc, nb);
                } else if (isTrue1 || isTrue2) {
                    return new Color(na, nc, nb);
                } else if (Math.abs(h - memH) < delta) {
                    delta = Math.abs(h - memH);
                    ba = na;
                }
                mh = h;
            }
        }
        if (na == GRB_MAX | na == -1) {
            na = ba;
        }
        rc = new Color(na, nc, nb);
        return null;
    }

    private static Color switchBRG(int nc, int nb, int na, int h, int ba, int delta, int mh, int ta, int memH, Color rc) {
        h = getHue(na, nb, nc);
        mh = h;
        boolean isTrue1 = mh < memH && h > memH;
        boolean isTrue2 = mh > memH && h < memH;
        while (h != memH && na < GRB_MAX) {
            h = getHue(++na, nb, nc);
            if (na == GRB_MAX) {
                break;
            }
            if (h == memH) {
                return new Color(na, nb, nc);
            } else if (isTrue1 || isTrue2) {
                return new Color(na, nb, nc);
            } else if (Math.abs(h - memH) < delta) {
                delta = Math.abs(h - memH);
                ba = na;
            }
            mh = h;
        }

        if (h != memH) {
            h = getHue(nc, na, nb);
            mh = h;
            na = ta;
            while (h != memH && na >= 0) {
                h = getHue(--na, nb, nc);
                if (na == -1) {
                    break;
                }

                if (h == memH) {
                    return new Color(na, nb, nc);
                } else if (isTrue1 || isTrue2) {
                    return new Color(na, nb, nc);
                } else if (Math.abs(h - memH) < delta) {
                    delta = Math.abs(h - memH);
                    ba = na;
                }
                mh = h;
            }
        }
        if (na == GRB_MAX | na == -1) {
            na = ba;
        }
        rc = new Color(na, nb, nc);
        return null;
    }

    private static Color switchBGR(int nc, int nb, int na, int h, int ba, int delta, int mh, int ta, int memH, Color rc) {
        h = getHue(nb, na, nc);
        mh = h;
        boolean isTrue1 = mh < memH && h > memH;
        boolean isTrue2 = mh > memH && h < memH;
        while (h != memH && na < GRB_MAX) {
            h = getHue(nb, ++na, nc);
            if (na == GRB_MAX) {
                break;
            }
            if (h == memH) {
                return new Color(nb, na, nc);
            } else if (isTrue1 || isTrue2) {
                return new Color(nb, na, nc);
            } else if (Math.abs(h - memH) < delta) {
                delta = Math.abs(h - memH);
                ba = na;
            }
            mh = h;
        }

        if (h != memH) {
            h = getHue(nc, na, nb);
            mh = h;
            na = ta;
            while (h != memH && na >= 0) {
                h = getHue(nb, --na, nc);
                if (na == -1) {
                    break;
                }
                if (h == memH) {
                    return new Color(nb, na, nc);
                } else if (isTrue1 || isTrue2) {
                    return new Color(nb, na, nc);
                } else if (Math.abs(h - memH) < delta) {
                    delta = Math.abs(h - memH);
                    ba = na;
                }
                mh = h;
            }
        }
        if (na == GRB_MAX | na == -1) {
            na = ba;
        }
        rc = new Color(nb, na, nc);
        return null;
    }


    public static float getGreyValue(Color c) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        int ta = 0, tb = 0, tc = 0;

        if (r >= g && r >= b) {
            if (r == 0) {
                return 0; // black
            }

            tc = r;

            if (g >= b) {
                ta = g;
                tb = b;
            } else {
                tb = g;
                ta = b;
            }
        } else if (g >= r && g >= b) {
            tc = g;

            if (r >= b) {
                ta = r;
                tb = b;
            } else {
                tb = r;
                ta = b;
            }
        } else if (b >= r && b >= g) {
            tc = b;

            if (r >= g) {
                ta = r;
                tb = g;
            } else {
                tb = r;
                ta = g;
            }
        }

        return (float) ((tc + tb) / 2.0);
    }

    public static int getBrightness(Color c) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        return getBrightness(r, g, b);
    }

    public static int getBrightness(int r, int g, int b) {
        if (r >= g && r >= b) {
            return (int) Math.round(BRIGHTNESS * r / GRB_VALUE);
        } else if (g >= r && g >= b) {
            return (int) Math.round(BRIGHTNESS * g / GRB_VALUE);
        } else if (b >= r && b >= g) {
            return (int) Math.round(BRIGHTNESS * b / GRB_VALUE);
        }

        return -1;
    }

    public static int getSaturation(Color c) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        return getSaturation(r, g, b);
    }

    public static int getSaturation(int r, int g, int b) {
        int ta = 0, tb = 0, tc = 0;

        if (r >= g && r >= b) {
            if (r == 0) {
                return 0; // black
            }
            tc = r;

            if (g >= b) {
                ta = g;
                tb = b;
            } else {
                tb = g;
                ta = b;
            }
        } else if (g >= r && g >= b) {
            tc = g;

            if (r >= b) {
                ta = r;
                tb = b;
            } else {
                tb = r;
                ta = b;
            }
        } else if (b >= r && b >= g) {
            tc = b;

            if (r >= g) {
                ta = r;
                tb = g;
            } else {
                tb = r;
                ta = g;
            }
        }

        return BRIGHTNESS - (int) Math.round(BRIGHTNESS * tb / tc);
    }

    public static int getHue(Color c) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        return getHue(r, g, b);
    }

    /**
     * 转换颜色
     *
     * @param c RGB颜色值
     * @return 返回值
     */
    public static int calculateHue(Color c) {
        float f[] = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        return (int) Math.round(ANGLE * f[0]);
    }

    public static int getHue(int r, int g, int b) {
        int ta = 0, tb = 0, tc = 0;
        int mapping = RGB;
        if (r >= g && r >= b) {
            tc = r;
            if (g == b) {
                return 0;
            } else if (g > b) {
                ta = g;
                tb = b;
                mapping = RGB;
            } else {
                tb = g;
                ta = b;
                mapping = RBG;
            }
        } else if (g >= r && g >= b) {
            tc = g;

            if (r == b) {
                return ANGLE120;
            } else if (r > b) {
                ta = r;
                tb = b;
                mapping = GRB;
            } else {
                tb = r;
                ta = b;
                mapping = GBR;
            }
        } else if (b >= r && b >= g) {
            tc = b;

            if (r == g) {
                return ANGLE240;
            } else if (r > g) {
                ta = r;
                tb = g;
                mapping = BRG;
            } else {
                tb = r;
                ta = g;
                mapping = BGR;
            }
        }

        return getValue(ta, tb, tc, mapping);
    }

    private static int getValue(int ta, int tb, int tc, int mapping) {
        // normalize
        double na = (ta * GRB_VALUE / tc);
        double nb = (tb * GRB_VALUE / tc);

        double val = ((na - nb) * GRB_VALUE / (GRB_VALUE - nb));

        int w = (int) Math.round(HALF_ANGLE * val / GRB_VALUE);

        switch (mapping) {
            case RGB:
                return w; // 0 - HALF_ANGLE
            case RBG:
                return ANGLE - w; // 300 - ANGLE
            case GBR:
                return ANGLE120 + w; // 120 - 180
            case GRB:
                return ANGLE120 - w; // 60 - ANGLE120
            case BRG:
                return ANGLE240 + w; // ANGLE240 - 300
            case BGR:
                return ANGLE240 - w; // 180 - 240
            default:
                return -1;
        }
    }

    public static Color getHSB(int h, int s, int b) {
        double cr = 0, cg = 0, cb = 0;
        int mapping = RGB;
        if (h == ANGLE) {
            h = 0;
        }
        // compute hue
        int winkel = h / HALF_ANGLE;
        int amount = h % HALF_ANGLE;
        calculateHSB(winkel, amount, cr, cg, cb, mapping);
        // compute BRIGHTNESS
        cr = (cr * b / BRIGHTNESS);
        cg = (cg * b / BRIGHTNESS);
        cb = (cb * b / BRIGHTNESS);
        // compute saturation
        int d = BRIGHTNESS - s;
        switch (mapping) {
            case RGB:
                cg += ((cr - cg) * d / BRIGHTNESS);
                cb += ((cr - cb) * d / BRIGHTNESS);
                break;
            case GBR:
                cr += ((cg - cr) * d / BRIGHTNESS);
                cb += ((cg - cb) * d / BRIGHTNESS);
                break;
            case BRG:
                cr += ((cb - cr) * d / BRIGHTNESS);
                cg += ((cb - cg) * d / BRIGHTNESS);
                break;
        }
        return new Color((int) Math.round(cr), (int) Math.round(cg), (int) Math.round(cb));
    }

    private static void calculateHSB(int winkel, int amount, double cr, double cg, double cb, int mapping) {
        switch (winkel) {
            case 0: // 0 - HALF_ANGLE
                cr = GRB_VALUE;
                cg = (GRB_VALUE * amount / HALF_ANGLE);
                mapping = RGB;
                break;
            case 1: // HALF_ANGLE - ANGLE120
                cg = GRB_VALUE;
                cr = GRB_VALUE - (GRB_VALUE * amount / HALF_ANGLE);
                mapping = GBR;
                break;
            case 2: // ANGLE120 - 180
                cg = GRB_VALUE;
                cb = (GRB_VALUE * amount / HALF_ANGLE);
                mapping = GBR;
                break;
            case 3: // 180 - 240
                cb = GRB_VALUE;
                cg = GRB_VALUE - (GRB_VALUE * amount / HALF_ANGLE);
                mapping = BRG;
                break;
            case GRB: // 240 - 300
                cb = GRB_VALUE;
                cr = (GRB_VALUE * amount / HALF_ANGLE);
                mapping = BRG;
                break;
            case BRG: // 300 - 360
                cr = GRB_VALUE;
                cb = GRB_VALUE - (GRB_VALUE * amount / HALF_ANGLE);
                mapping = RGB;
                break;
        }
    }

    /**
     * 将颜色值转换成int值
     *
     * @param r 红色值
     * @param g 绿色值
     * @param b 蓝色值
     * @param a 透明度
     * @return 转换的值
     */
    public int colorize(int r, int g, int b, int a) {
        if (cbri == BRIGHTNESS) {
            return colorToInt(GRB_VALUE, GRB_VALUE, GRB_VALUE, a);
        } else if (cbri == -BRIGHTNESS) {
            return colorToInt(0, 0, 0, a);
        }

        int hi1 = r;
        if (g >= r && g >= b) {
            hi1 = g;
        } else if (b >= r && b >= g) {
            hi1 = b;
        }
        int lo1 = r;
        if (g <= r && g <= b) {
            lo1 = g;
        } else if (b <= r && b <= g) {
            lo1 = b;
        }
        int grey = (hi1 + lo1) / 2; // floor
        if (cbri < 0) {
            grey += grey * cbri / BRIGHTNESS;
        } else if (cbri > 0) {
            grey += (GRB_VALUE - grey) * cbri / BRIGHTNESS;
        }

        if (preserveGrey) {
            if (r == g && r == b) {
                return colorToInt(grey, grey, grey, a);
            }
        }

        int hr = 0;
        int hg = 0;
        int hb = 0;

        calculateColorToInt(hr, hg, hb, grey);
        return colorToInt(hr, hg, hb, a);
    }


    private void calculateColorToInt(int hr, int hg, int hb, int grey) {
        int diff = 0;
        if (grey >= GREY_VALUE) {
            diff = GRB_VALUE - grey;
        } else {
            diff = grey;
        }

        if (hiIsR) {
            hr = grey + diff * csat / BRIGHTNESS;
        } else if (hiIsG) {
            hg = grey + diff * csat / BRIGHTNESS;
        } else if (hiIsB) {
            hb = grey + diff * csat / BRIGHTNESS;
        }
        if (mdIsR) {
            if (grey >= GREY_VALUE) {
                diff = fr + (GRB_VALUE - fr) * (grey - GREY_VALUE) / GREY_MAX - grey;
            } else {
                diff = fr * grey / GREY_VALUE - grey;
            }
            hr = grey + diff * csat / BRIGHTNESS;
        } else if (mdIsG) {
            if (grey >= GREY_VALUE) {
                diff = fg + (GRB_VALUE - fg) * (grey - GREY_VALUE) / GREY_MAX - grey;
            } else {
                diff = fg * grey / GREY_VALUE - grey;
            }
            hg = grey + diff * csat / BRIGHTNESS;
        } else if (mdIsB) {
            if (grey >= GREY_VALUE) {
                diff = fb + (GRB_VALUE - fb) * (grey - GREY_VALUE) / GREY_MAX - grey;
            } else {
                diff = fb * grey / GREY_VALUE - grey;
            }
            hb = grey + diff * csat / BRIGHTNESS;
        }
        diff = grey - (GRB_VALUE - grey);
        if (diff < 0){
            diff = 0;
        }
        diff = grey - diff;
        if (loIsR) {
            hr = grey - diff * csat / BRIGHTNESS;
        } else if (loIsG) {
            hg = grey - diff * csat / BRIGHTNESS;
        } else if (loIsB) {
            hb = grey - diff * csat / BRIGHTNESS;
        }
    }

    public static Color getInverseColor(Color c) {
        int r = GRB_VALUE - c.getRed();
        int g = GRB_VALUE - c.getGreen();
        int b = GRB_VALUE - c.getBlue();

        return new Color(r, g, b);
    }

    public static Color getRandomColor() {
        int r = (int) (Math.random() * GRB_VALUE);
        int g = (int) (Math.random() * GRB_VALUE);
        int b = (int) (Math.random() * GRB_VALUE);

        return new Color(r, g, b);
    }

    public static Color getAlphaColor(Color c, int a) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        return new Color(r, g, b, a);
    }

    protected static int colorToInt(Color c, int a) {
        return c.getBlue() + c.getGreen() * GRB_MAX + c.getRed() * (GRB_MAX * GRB_MAX) + a * (GRB_MAX * GRB_MAX * GRB_MAX);
    }

    protected static int colorToInt(int r, int g, int b, int a) {
        return b + g * GRB_MAX + r * (GRB_MAX * GRB_MAX) + a * (GRB_MAX * GRB_MAX * GRB_MAX);
    }

    /**
     * 计算亮度，减轻
     *
     * @param c      颜色值
     * @param amount 亮度值
     * @return 计算过亮度的颜色值
     */
    public static Color lighten(Color c, int amount) {
        if (amount < 0) {
            return c;
        }
        if (amount > BRIGHTNESS) {
            amount = BRIGHTNESS;
        }

        int dr = (int) Math.round((GRB_VALUE - c.getRed()) * amount / BRIGHTNESS);
        int dg = (int) Math.round((GRB_VALUE - c.getGreen()) * amount / BRIGHTNESS);
        int db = (int) Math.round((GRB_VALUE - c.getBlue()) * amount / BRIGHTNESS);

        return new Color(c.getRed() + dr, c.getGreen() + dg, c.getBlue() + db, c.getAlpha());
    }

    /**
     * 计算亮度，家中
     *
     * @param c      颜色值
     * @param amount 加重值
     * @return 加重了亮度的颜色值
     */
    public static Color darken(Color c, int amount) {
        if (amount < 0 || amount > BRIGHTNESS) {
            return c;
        }

        int r = (int) Math.round(c.getRed() * (BRIGHTNESS - amount) / BRIGHTNESS);
        int g = (int) Math.round(c.getGreen() * (BRIGHTNESS - amount) / BRIGHTNESS);
        int b = (int) Math.round(c.getBlue() * (BRIGHTNESS - amount) / BRIGHTNESS);

        return new Color(r, g, b, c.getAlpha());
    }

    /**
     * 颜色
     *
     * @param grey   灰度
     * @param amount 量
     * @return 颜色值
     */
    public static Color lighten(int grey, int amount) {
        if (amount < 0 || amount > BRIGHTNESS) {
            return new Color(grey, grey, grey);
        }

        int val = (GRB_VALUE - grey) * amount / BRIGHTNESS + grey;

        return new Color(val, val, val);
    }

    /**
     * 颜色
     *
     * @param grey   灰度
     * @param amount 量
     * @return 颜色值
     */
    public static Color darken(int grey, int amount) {
        if (amount < 0 || amount > BRIGHTNESS) {
            return new Color(grey, grey, grey);
        }

        int val = grey * (BRIGHTNESS - amount) / BRIGHTNESS;

        return new Color(val, val, val);
    }
}