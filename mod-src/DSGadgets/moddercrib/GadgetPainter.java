package moddercrib;

import static moddercrib.GadgetsInit.rd;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;

import com.trashers.nfmmaddons.apinext.ModUtility;
import com.trashers.nfmmaddons.apinext.builtin.RadicalHUD;
import com.trashers.nfmmaddons.apinext.readonly.RCarDefine;
import com.trashers.nfmmaddons.apinext.readonly.RCheckPoints;
import com.trashers.nfmmaddons.apinext.readonly.RMad;
import com.trashers.nfmmaddons.apinext.readonly.RMedium;
import com.trashers.nfmmaddons.apinext.readonly.RxtGraphics;
import com.trashers.nfmmaddons.apinext.readonly.TData;
import com.trashers.nfmmaddons.game.Medium;
import com.trashers.nfmmaddons.game.xtGraphics;
import com.trashers.nfmmaddons.game.music.RadicalMod;

public class GadgetPainter {
    // checkpoints\.(\w+)( < | > | >= | <= | \&\& | \|\| | == | \!= |\)|\?|, | \+ |;)
    // RCheckPoints\.\1\(\)\2

    static int modc = 150;
    static float modp = 1.0F;
    static int speedocnt = 0;
    static int degreecnt = -80;
    static int[][] co = new int[][] {
            {
                    50, 204, 255
            }, {
                    151, 218, 241
            }, {
                    138, 181, 71
            }, {
                    255, 224, 50
            }, {
                    234, 155, 31
            }, {
                    255, 10, 0
            }, {
                    220, 10, 250
            }, {
                    250, 180, 180
            }, {
                    250, 250, 250
            }
    };
    static int ci = 0;
    static DecimalFormat dc = new DecimalFormat("##0.00");
    static DecimalFormat dp = new DecimalFormat("00");
    static DecimalFormat dt = new DecimalFormat("000");
    static int groove = 0;
    static int sgroove = 0;
    static int sshake = 0;
    static int sprevdam = 0;
    private static Area BF_pow;

    private static int dmcnt = 0;
    private static boolean dmflk = false;

    // TODO call methods

    public static void speedometer() {
        if (RMedium.resdown() == 0) {
            rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        final boolean clanchat = RxtGraphics.clanchat();
        final int swit = RCarDefine.swits(RxtGraphics.sc(TData.im), 2);
        final boolean goingfast = Math.abs(RMad.speed()) - swit > 50.0F;
        if (clanchat) {
            rd.translate(0, -25);
        }

        Polygon pol;
        short py;
        int maxs;
        int scale;
        int sensitivity;
        int degrees;
        int pox;
        if (GadgetsInit.speedon == 1) {
            final boolean px = RMedium.darksky() || RMedium.lightson();
            if (px || goingfast) {
                if (goingfast) {
                    rd.setColor(new Color(180, 0, 0));
                } else {
                    rd.setColor(new Color(0, 0, 0, 100));
                }

                rd.fillArc(671, 320, 150, 150, 61, 115);
                pol = new Polygon();
                pol.addPoint(782, 330);
                pol.addPoint(782, 410);
                pol.addPoint(618, 410);
                pol.addPoint(623, 390);
                pol.addPoint(670, 390);
                pol.addPoint(746, 395);
                rd.fillPolygon(pol);
            }

            rd.setColor(px ? new Color(co[ci][0], co[ci][1], co[ci][2]) : ModUtility.colorSnap(30, 70, 150));
            rd.drawArc(671, 320, 150, 150, 61, 115);
            rd.drawLine(782, 330, 782, 410);
            rd.drawLine(782, 410, 618, 410);
            rd.drawLine(618, 410, 623, 390);
            rd.drawLine(623, 390, 670, 390);
            py = 750;
            final short mins = 400;
            degrees = 90 - (int) (Math.abs(RMad.speed()) / swit * 110.0D);
            if (degrees < -20) {
                degrees = -20;
            }

            int byte0;
            for (pox = 0; pox <= 90; pox += 30) {
                maxs = py - (int) (Medium.sin(pox) * 60.0F);
                scale = mins - (int) (Medium.cos(pox) * 60.0F);
                sensitivity = py - (int) (Medium.sin(pox) * 80.0F);
                byte0 = mins - (int) (Medium.cos(pox) * 80.0F);
                rd.drawLine(maxs, scale, sensitivity, byte0);
            }

            for (pox = -15; pox <= 90; pox += 30) {
                maxs = py - (int) (Medium.sin(pox) * 80.0F);
                scale = mins - (int) (Medium.cos(pox) * 80.0F);
                sensitivity = py - (int) (Medium.sin(pox) * 70.0F);
                byte0 = mins - (int) (Medium.cos(pox) * 70.0F);
                rd.drawLine(maxs, scale, sensitivity, byte0);
            }

            rd.drawString("Speed:", 628, 405);
            rd.setColor(px ? new Color(250, 0, 0) : new Color(100, 0, 0));
            pol = new Polygon();
            maxs = py - (int) (Medium.sin(degrees + 180) * 5.0F);
            scale = mins - (int) (Medium.cos(degrees + 180) * 5.0F);
            pol.addPoint(maxs, scale);
            maxs = py - (int) (Medium.sin(degrees + 90) * 4.0F);
            scale = mins - (int) (Medium.cos(degrees + 90) * 4.0F);
            pol.addPoint(maxs, scale);
            maxs = py - (int) (Medium.sin(degrees) * 70.0F);
            scale = mins - (int) (Medium.cos(degrees) * 70.0F);
            pol.addPoint(maxs, scale);
            maxs = py - (int) (Medium.sin(degrees - 90) * 4.0F);
            scale = mins - (int) (Medium.cos(degrees - 90) * 4.0F);
            pol.addPoint(maxs, scale);
            rd.fillPolygon(pol);
            rd.setFont(new Font("Arial", 1, 14));
            if (goingfast) {
                rd.setColor(new Color(250, 0, 0));
            } else {
                rd.setColor(px ? new Color(250, 250, 250) : new Color(0, 0, 0));
            }

            String var39;
            if (GadgetsInit.kph) {
                var39 = (dc.format(Math.abs(RMad.speed())) + " Km/h").replace(",", ".");
            } else {
                var39 = (dc.format(Math.abs(RMad.speed() / 1.609344D)) + " Ml/h").replace(",", ".");
            }

            rd.drawString(var39, 778 - rd.getFontMetrics().stringWidth(var39), 406);
            rd.setFont(new Font("Arial", 1, 11));
            if (goingfast) {
                rd.setColor(new Color(255, 140, 10));
                rd.drawString("[Boost]", 625, 383);
            } else if (RMad.speed() < 0.0F) {
                rd.setColor(new Color(100, 140, 10));
                rd.drawString("[Rev]", 632, 383);
            }
        } else {
            short var29;
            int var32;
            if (GadgetsInit.speedon == 2) {
                var29 = 730;
                py = 379;
                scale = 110 - (int) (Math.abs(RMad.speed()) / swit * 220.0D);
                if (scale < -115) {
                    scale = -115;
                }

                rd.setColor(ModUtility.colorSnap(50, 50, 50, 220));
                rd.fillArc(667, 316, 125, 125, -30, 240);
                pol = new Polygon();
                var32 = var29 + 1 - (int) (Medium.sin(-120) * 62.0F);
                maxs = py + 2 - (int) (Medium.cos(-120) * 62.0F);
                pol.addPoint(var32, maxs);
                var32 = var29 - (int) (Medium.sin(-120) * 62.0F);
                maxs = py - 1 - (int) (Medium.cos(-120) * 62.0F);
                pol.addPoint(var32, maxs);
                pol.addPoint(var29 + 1, py);
                pol.addPoint(var29 - 2, py);
                var32 = var29 - 1 - (int) (Medium.sin(120) * 62.0F);
                maxs = py - 1 - (int) (Medium.cos(120) * 62.0F);
                pol.addPoint(var32, maxs);
                --var32;
                maxs += 3;
                pol.addPoint(var32, maxs);
                rd.fillPolygon(pol);
                rd.setStroke(new BasicStroke(6.0F));
                rd.setColor(ModUtility.colorSnap(40, 40, 40));
                rd.drawArc(667, 316, 125, 125, -30, 240);
                rd.setStroke(new BasicStroke(2.0F));
                if (!goingfast) {
                    if (RMedium.darksky() && !RMedium.lightson()) {
                        rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                    } else {
                        rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                    }
                } else if (RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(250, 20, 20, 220));
                } else {
                    rd.setColor(new Color(250, 20, 20, 220));
                }

                rd.drawArc(667, 316, 125, 125, -30, 240);
                rd.setStroke(new BasicStroke(5.0F));

                for (sensitivity = 11; sensitivity >= 0; --sensitivity) {
                    if (!goingfast) {
                        if (sensitivity == 3) {
                            if (RMedium.darksky() && !RMedium.lightson()) {
                                rd.setColor(ModUtility.colorSnap(50, 204, 120, 220));
                            } else {
                                rd.setColor(new Color(50, 204, 120, 220));
                            }
                        }

                        if (sensitivity == 2) {
                            if (RMedium.darksky() && !RMedium.lightson()) {
                                rd.setColor(ModUtility.colorSnap(250, 250, 20, 220));
                            } else {
                                rd.setColor(new Color(250, 250, 20, 220));
                            }
                        }

                        if (sensitivity == 1) {
                            if (RMedium.darksky() && !RMedium.lightson()) {
                                rd.setColor(ModUtility.colorSnap(250, 200, 20, 220));
                            } else {
                                rd.setColor(new Color(250, 150, 20, 220));
                            }
                        }

                        if (sensitivity == 0) {
                            if (RMedium.darksky() && !RMedium.lightson()) {
                                rd.setColor(ModUtility.colorSnap(250, 20, 20, 220));
                            } else {
                                rd.setColor(new Color(250, 20, 20, 220));
                            }
                        }
                    }

                    rd.drawArc(675, 324, 109, 109, -30 + (int) (20.8D * sensitivity), 10);
                }

                rd.setStroke(new BasicStroke(1.0F));
                rd.setColor(ModUtility.colorSnap(40, 40, 40));
                rd.fillOval(var29 - 8, py - 8, 16, 16);
                pol = new Polygon();
                var32 = var29 - (int) (Medium.sin(scale + 68) * 8.0F);
                maxs = py - (int) (Medium.cos(scale + 68) * 8.0F);
                pol.addPoint(var32, maxs);
                var32 = var29 - (int) (Medium.sin(scale) * 25.0F);
                maxs = py - (int) (Medium.cos(scale) * 25.0F);
                pol.addPoint(var32, maxs);
                var32 = var29 - (int) (Medium.sin(scale - 68) * 8.0F);
                maxs = py - (int) (Medium.cos(scale - 68) * 8.0F);
                pol.addPoint(var32, maxs);
                rd.fillPolygon(pol);
                if (goingfast) {
                    rd.setColor(new Color(255, 140, 10));
                    rd.drawString("[Boost]", 711, 348);
                } else if (RMad.speed() < 0.0F) {
                    rd.setColor(new Color(100, 140, 10));
                    rd.drawString("[Rev]", 717, 348);
                }

                rd.setColor(new Color(250, 0, 0));
                pol = new Polygon();
                var32 = var29 - (int) (Medium.sin(scale + 180) * 5.0F);
                maxs = py - (int) (Medium.cos(scale + 180) * 5.0F);
                pol.addPoint(var32, maxs);
                var32 = var29 - (int) (Medium.sin(scale + 90) * 3.2D);
                maxs = py - (int) (Medium.cos(scale + 90) * 3.2D);
                pol.addPoint(var32, maxs);
                var32 = var29 - (int) (Medium.sin(scale) * 60.0F);
                maxs = py - (int) (Medium.cos(scale) * 60.0F);
                pol.addPoint(var32, maxs);
                var32 = var29 - (int) (Medium.sin(scale - 90) * 3.2D);
                maxs = py - (int) (Medium.cos(scale - 90) * 3.2D);
                pol.addPoint(var32, maxs);
                rd.fillPolygon(pol);
                rd.setFont(new Font("Arial", 1, 14));
                if (goingfast) {
                    rd.setColor(new Color(250, 0, 0));
                } else {
                    rd.setColor(new Color(250, 250, 250));
                }

                String var33;
                if (GadgetsInit.kph) {
                    var33 = (dc.format(Math.abs(RMad.speed())) + " Km/h").replace(",", ".");
                } else {
                    var33 = (dc.format(Math.abs(RMad.speed() / 1.609344D)) + " Ml/h").replace(",", ".");
                }

                rd.drawString(var33, 730 - rd.getFontMetrics().stringWidth(var33) / 2, 406);
                rd.setFont(new Font("Arial", 1, 11));
            } else {
                FontMetrics ftm;
                if (GadgetsInit.speedon == 3) {
                    if (RMedium.resdown() < 2) {
                        rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    }

                    final int var30 = (int) (Math.abs(RMad.speed()) / swit * 94.0F);
                    rd.setColor(ModUtility.colorSnap(0, 0, 0, 100));
                    rd.fillRoundRect(677, 330, 121, 80, 10, 10);
                    if (RMedium.darksky() && !RMedium.lightson()) {
                        rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                    } else {
                        rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                    }

                    rd.drawString("SPEED", 685, 382);

                    for (var32 = 0; var32 < var30 && var32 <= 94; var32 += 4) {
                        int var31;
                        if (var32 > 38 && var32 <= 72) {
                            var31 = 17 + (int) ((var32 - 36) / 32.0F * 12.0F);
                        } else {
                            var31 = var32 > 70 ? 30 : 17;
                        }

                        rd.fillRect(685 + var32, 402 - var31, 2, var31);
                    }

                    rd.fillRect(685, 403, 94, 2);
                    if (goingfast) {
                        if (RMedium.darksky() && !RMedium.lightson()) {
                            rd.setColor(ModUtility.colorSnap(250, 20, 20, 220));
                        } else {
                            rd.setColor(new Color(250, 20, 20, 220));
                        }

                        for (var32 = 0; var32 < 2; ++var32) {
                            for (maxs = 0; maxs < 5; ++maxs) {
                                rd.drawLine(784 + maxs, 376 + var32 * 6 - maxs, 784 + maxs, 380 + var32 * 6 - maxs);
                            }

                            for (maxs = 4; maxs > 0; --maxs) {
                                rd.drawLine(793 - maxs, 377 + var32 * 6 - maxs, 793 - maxs, 381 + var32 * 6 - maxs);
                            }
                        }
                    }

                    if (RMad.speed() >= 0.0F) {
                        for (var32 = 0; var32 < 5; ++var32) {
                            rd.drawLine(784 + 4 - var32, 390 + var32, 793 - (5 - var32), 390 + var32);
                        }
                    } else {
                        for (var32 = 0; var32 < 5; ++var32) {
                            rd.drawLine(784 + 4 - var32, 400 - var32, 793 - (5 - var32), 400 - var32);
                        }
                    }

                    String var37;
                    if (GadgetsInit.kph) {
                        rd.drawString("KPH", 771, 344);
                        var37 = dt.format(Math.abs((int) RMad.speed()));
                        rd.setFont(GadgetsInit.fontDigital.deriveFont(2, Math.abs((int) RMad.speed()) < 2000 ? 47.0F : 40.0F));
                        ftm = rd.getFontMetrics();
                        rd.drawString(var37, 762 - ftm.stringWidth(var37), 368);
                        rd.setFont(GadgetsInit.fontDigital.deriveFont(2, 25.0F));
                        rd.drawString(dp.format((int) Math.abs(RMad.speed() * 100.0F) % 100), 767, 368);
                    } else {
                        rd.drawString("MPH", 771, 344);
                        var37 = dt.format(Math.abs((int) RMad.speed() / 1.609344D));
                        rd.setFont(GadgetsInit.fontDigital.deriveFont(2, Math.abs((int) RMad.speed() / 1.609344D) < 2000.0D ? 47.0F : 40.0F));
                        ftm = rd.getFontMetrics();
                        rd.drawString(var37, 762 - ftm.stringWidth(var37), 368);
                        rd.setFont(GadgetsInit.fontDigital.deriveFont(2, 25.0F));
                        rd.drawString(dp.format((int) Math.abs(RMad.speed() / 1.609344D * 100.0D) % 100), 767, 368);
                    }

                    rd.fillRect(763, 365, 3, 3);
                    rd.setFont(new Font("Arial", 1, 11));
                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                } else if (GadgetsInit.speedon == 4) {
                    var29 = 730;
                    py = 365;
                    final byte var38 = -120;
                    final byte var40 = 120;
                    final float var35 = 0.8F;
                    final float var36 = 3.55F;
                    rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    rd.setColor(ModUtility.colorSnap(50, 50, 50, 220));
                    rd.fillArc(var29 - (int) (80.0F * var35), py - (int) (80.0F * var35), (int) (160.0F * var35), (int) (160.0F * var35), -30, 240);
                    pol = new Polygon();
                    pol.addPoint(var29, py);
                    pol.addPoint(var29 - (int) (Medium.sin(-120) * 80.0F * var35) - 1, py - (int) (Medium.cos(-120) * 80.0F * var35) - 1);
                    pol.addPoint(var29 - (int) (Medium.sin(120) * 80.0F * var35) + 1, py - (int) (Medium.cos(120) * 80.0F * var35) - 1);
                    rd.fillPolygon(pol);
                    pol = new Polygon();
                    final byte var34 = 3;
                    pol.addPoint(var29 - (int) (Medium.sin(-120) * 80.0F * var35) + var34, py - (int) (Medium.cos(-120) * 80.0F * var35));
                    pol.addPoint(var29 - (int) (Medium.sin(-120) * 80.0F * var35) + var34, py - (int) (Medium.cos(-120) * 80.0F * var35) + 14);
                    pol.addPoint(var29 - (int) (Medium.sin(-120) * 80.0F * var35) + var34 - 3, py - (int) (Medium.cos(-120) * 80.0F * var35) + 17);
                    pol.addPoint(var29 - (int) (Medium.sin(120) * 80.0F * var35) - var34 + 3, py - (int) (Medium.cos(120) * 80.0F * var35) + 17);
                    pol.addPoint(var29 - (int) (Medium.sin(120) * 80.0F * var35) - var34, py - (int) (Medium.cos(120) * 80.0F * var35) + 14);
                    pol.addPoint(var29 - (int) (Medium.sin(120) * 80.0F * var35) - var34, py - (int) (Medium.cos(120) * 80.0F * var35));
                    rd.fillPolygon(pol);
                    if (RMedium.resdown() < 2) {
                        rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    }

                    if (RMedium.darksky() && !RMedium.lightson()) {
                        rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                    } else {
                        rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                    }

                    rd.drawPolygon(pol);
                    rd.drawArc(var29 - (int) (81.0F * var35), py - (int) (81.0F * var35), (int) (161.0F * var35), (int) (161.0F * var35), -15, 210);
                    degrees = 120 - (int) (Math.abs(RMad.speed()) * 2.0F / 3.0F);
                    if (RMad.speed() < 0.0F) {
                        rd.setColor(new Color(100, 140, 10));
                        rd.drawString("[Rev]", 717, 386);
                    }

                    if (speedocnt != 0) {
                        --speedocnt;
                    }

                    if (RMad.speed() >= swit) {
                        speedocnt = 10;
                    } else if (RMad.speed() < swit - 10) {
                        speedocnt = 0;
                    }

                    if (speedocnt != 0) {
                        rd.setColor(new Color(255, 0, 0));
                        rd.drawString("[Max]", 717, 386);
                    }

                    if (RMedium.darksky() && !RMedium.lightson()) {
                        rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                    } else {
                        rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                    }

                    if (degrees > var40) {
                        degrees = var40;
                    }

                    if (degrees < var38) {
                        degrees = var38;
                    }

                    degreecnt = (int) (degreecnt + (degrees - degreecnt) / var36);
                    if (Math.abs(degrees - degreecnt) < 1.5F) {
                        degreecnt = degrees;
                    }

                    int poy;
                    int str;
                    int tmp;
                    int color;
                    for (str = var38; str <= var40; str += 30) {
                        pox = var29 - (int) (Medium.sin(str) * 70.0F * var35);
                        poy = py - (int) (Medium.cos(str) * 70.0F * var35);
                        tmp = var29 - (int) (Medium.sin(str) * 80.0F * var35);
                        color = py - (int) (Medium.cos(str) * 80.0F * var35);
                        rd.drawLine(pox, poy, tmp, color);
                    }

                    for (str = var38 + 10; str <= var40; str += 30) {
                        pox = var29 - (int) (Medium.sin(str) * 80.0F * var35);
                        poy = py - (int) (Medium.cos(str) * 80.0F * var35);
                        tmp = var29 - (int) (Medium.sin(str) * 75.0F * var35);
                        color = py - (int) (Medium.cos(str) * 75.0F * var35);
                        rd.drawLine(pox, poy, tmp, color);
                    }

                    for (str = var38 + 20; str <= var40; str += 30) {
                        pox = var29 - (int) (Medium.sin(str) * 80.0F * var35);
                        poy = py - (int) (Medium.cos(str) * 80.0F * var35);
                        tmp = var29 - (int) (Medium.sin(str) * 75.0F * var35);
                        color = py - (int) (Medium.cos(str) * 75.0F * var35);
                        rd.drawLine(pox, poy, tmp, color);
                    }

                    final String[] var41 = new String[] {
                            "0", "45", "90", "135", "180", "225", "270", "315", "360"
                    };
                    Font var42 = rd.getFont();
                    Color var43 = rd.getColor();
                    short r1 = 0;
                    short g1 = 0;
                    byte b1 = 0;

                    for (int coorx = var38; coorx <= var40; coorx += 30) {
                        rd.setFont(new Font("Arial", 1, 11));
                        ftm = rd.getFontMetrics();
                        pox = var29 - (int) (Medium.sin(coorx) * 55.0F * var35);
                        poy = py - (int) (Medium.cos(coorx) * 55.0F * var35);
                        if (coorx == var38) {
                            if (degreecnt >= coorx + 5) {
                                r1 = 70;
                                g1 = 0;
                                b1 = 0;
                            } else {
                                r1 = 170;
                                g1 = 30;
                                b1 = 30;
                            }
                        }

                        if (coorx == var38 + 30) {
                            if (degreecnt >= coorx) {
                                r1 = 150;
                                g1 = 0;
                                b1 = 0;
                            } else {
                                r1 = 230;
                                g1 = 30;
                                b1 = 30;
                            }
                        }

                        if (coorx == var38 + 60) {
                            if (degreecnt >= coorx) {
                                r1 = 150;
                                g1 = 30;
                                b1 = 0;
                            } else {
                                r1 = 230;
                                g1 = 60;
                                b1 = 30;
                            }
                        }

                        if (coorx == var38 + 90) {
                            if (degreecnt >= coorx) {
                                r1 = 150;
                                g1 = 70;
                                b1 = 0;
                            } else {
                                r1 = 230;
                                g1 = 100;
                                b1 = 30;
                            }
                        }

                        if (coorx == var38 + 120) {
                            if (degreecnt >= coorx) {
                                r1 = 150;
                                g1 = 150;
                                b1 = 30;
                            } else {
                                r1 = 230;
                                g1 = 230;
                                b1 = 60;
                            }
                        }

                        if (coorx == var38 + 150) {
                            if (degreecnt >= coorx) {
                                r1 = 130;
                                g1 = 150;
                                b1 = 45;
                            } else {
                                r1 = 190;
                                g1 = 230;
                                b1 = 80;
                            }
                        }

                        if (coorx == var38 + 180) {
                            if (degreecnt >= coorx) {
                                r1 = 100;
                                g1 = 150;
                                b1 = 45;
                            } else {
                                r1 = 150;
                                g1 = 230;
                                b1 = 80;
                            }
                        }

                        if (coorx == var38 + 210) {
                            if (degreecnt >= coorx) {
                                r1 = 50;
                                g1 = 150;
                                b1 = 20;
                            } else {
                                r1 = 80;
                                g1 = 230;
                                b1 = 30;
                            }
                        }

                        if (coorx == var38 + 240) {
                            if (degreecnt >= coorx) {
                                r1 = 0;
                                g1 = 80;
                                b1 = 0;
                            } else {
                                r1 = 30;
                                g1 = 150;
                                b1 = 30;
                            }
                        }

                        rd.setColor(new Color(r1, g1, b1));
                        rd.drawString(var41[8 - (coorx / 30 + 4)], pox - ftm.stringWidth(var41[8 - (coorx / 30 + 4)]) / 2, poy + 5);
                    }

                    rd.setFont(var42);
                    rd.setColor(var43);
                    ftm = rd.getFontMetrics();
                    final int[] var44 = new int[720];
                    final int[] coory = new int[720];
                    var43 = rd.getColor();

                    int s9;
                    for (s9 = 0; s9 < 482; ++s9) {
                        if (s9 < 241) {
                            var44[s9] = var29 - (int) (Medium.sin(s9 - 120) * 80.0F * var35);
                            coory[s9] = py - (int) (Medium.cos(s9 - 120) * 80.0F * var35);
                        } else {
                            var44[s9] = var29 - (int) (Medium.sin(481 - s9 - 120) * 82.0F * var35);
                            coory[s9] = py - (int) (Medium.cos(481 - s9 - 120) * 82.0F * var35);
                        }
                    }

                    rd.fillPolygon(var44, coory, 482);
                    rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 150));

                    for (s9 = 0; s9 < 482; ++s9) {
                        if (s9 < 241) {
                            var44[s9] = var29 - (int) (Medium.sin(s9 - 120) * 82.0F * var35);
                            coory[s9] = py - (int) (Medium.cos(s9 - 120) * 82.0F * var35);
                        } else {
                            var44[s9] = var29 - (int) (Medium.sin(481 - s9 - 120) * 83.0F * var35);
                            coory[s9] = py - (int) (Medium.cos(481 - s9 - 120) * 83.0F * var35);
                        }
                    }

                    rd.fillPolygon(var44, coory, 482);
                    rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 230));

                    for (s9 = 0; s9 < 482; ++s9) {
                        if (s9 < 241) {
                            var44[s9] = var29 - (int) (Medium.sin(s9 - 120) * 83.0F * var35);
                            coory[s9] = py - (int) (Medium.cos(s9 - 120) * 83.0F * var35);
                        } else {
                            var44[s9] = var29 - (int) (Medium.sin(481 - s9 - 120) * 83.0F * var35);
                            coory[s9] = py - (int) (Medium.cos(481 - s9 - 120) * 83.0F * var35);
                        }
                    }

                    rd.setColor(var43);
                    rd.fillPolygon(var44, coory, 482);
                    pol = new Polygon();
                    pol.addPoint(var29 - (int) (Medium.sin(-120) * 80.0F * var35), py - (int) (Medium.cos(-120) * 80.0F * var35) - 1);
                    pol.addPoint(var29 - (int) (Medium.sin(-120) * 80.0F * var35), py - (int) (Medium.cos(-120) * 80.0F * var35) + 1);
                    pol.addPoint(var29 - (int) (Medium.sin(120) * 80.0F * var35), py - (int) (Medium.cos(120) * 80.0F * var35) + 1);
                    pol.addPoint(var29 - (int) (Medium.sin(120) * 80.0F * var35), py - (int) (Medium.cos(120) * 80.0F * var35) - 1);
                    rd.fillPolygon(pol);
                    pol.addPoint(var29 - (int) (Medium.sin(-120) * 80.0F * var35) + 3, py - (int) (Medium.cos(-120) * 80.0F * var35) + 14);
                    pol.addPoint(var29 - (int) (Medium.sin(-120) * 80.0F * var35) + 3 - 3, py - (int) (Medium.cos(-120) * 80.0F * var35) + 17);
                    pol.addPoint(var29 - (int) (Medium.sin(120) * 80.0F * var35) - 3 + 3, py - (int) (Medium.cos(120) * 80.0F * var35) + 17);
                    pol.addPoint(var29 - (int) (Medium.sin(120) * 80.0F * var35) - 3, py - (int) (Medium.cos(120) * 80.0F * var35) + 14);
                    String var45 = "Speed: ";
                    if (GadgetsInit.kph) {
                        var45 = var45 + (dc.format(Math.abs(RMad.speed())) + " Km/h").replace(",", ".");
                    } else {
                        var45 = var45 + (dc.format(Math.abs(RMad.speed() / 1.609344D)) + " Ml/h").replace(",", ".");
                    }

                    var42 = rd.getFont();
                    rd.setFont(new Font("Arial", 1, 10));
                    ftm = rd.getFontMetrics();
                    rd.drawString(var45, var29 - ftm.stringWidth(var45) / 2, py - (int) (Medium.cos(-120) * 80.0F * var35) + 13);
                    rd.setFont(var42);
                    rd.setColor(new Color(250, 0, 0));
                    pol = new Polygon();
                    pox = var29 - (int) (Medium.sin(degreecnt + 180) * 5.0F);
                    poy = py - (int) (Medium.cos(degreecnt + 180) * 5.0F);
                    pol.addPoint(pox, poy);
                    pox = var29 - (int) (Medium.sin(degreecnt + 90) * 4.0F);
                    poy = py - (int) (Medium.cos(degreecnt + 90) * 4.0F);
                    pol.addPoint(pox, poy);
                    pox = var29 - (int) (Medium.sin(degreecnt) * 70.0F * var35);
                    poy = py - (int) (Medium.cos(degreecnt) * 70.0F * var35);
                    pol.addPoint(pox, poy);
                    pox = var29 - (int) (Medium.sin(degreecnt - 90) * 4.0F);
                    poy = py - (int) (Medium.cos(degreecnt - 90) * 4.0F);
                    pol.addPoint(pox, poy);
                    rd.fillPolygon(pol);
                }
            }
        }

        rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        if (clanchat) {
            rd.translate(0, 25);
        }

    }

    public static void modTrackVisor(final int xm, final int ym) {
        if (GadgetsInit.modon == 3) {
            rd.setFont(GadgetsInit.fontDigital.deriveFont(1, 15.0F));
        } else {
            rd.setFont(new Font("Arial", 1, 14));
        }

        FontMetrics ftm = rd.getFontMetrics();
        int dist = ftm.stringWidth(RadicalMod.name);
        rd.setFont(new Font("Arial", 1, 11));
        ftm = rd.getFontMetrics();
        if (GadgetsInit.hidemod) {
            if (xm < 35 + dist && ym < 397 && ym > 355) {
                modc = 150;
            }

            if (modc == 0 && modp > 0.0F) {
                modp -= 0.1F;
            } else if (modc > 0) {
                --modc;
            }
        } else {
            modc = 150;
        }

        if (modc > 0 && modp < 1.0F) {
            modp += 0.1F;
        }

        if (ftm.stringWidth(RadicalMod.filename) > dist) {
            dist = ftm.stringWidth(RadicalMod.filename);
        }

        if (GadgetsInit.modon == 1) {
            if (RMedium.darksky() || RMedium.lightson()) {
                rd.setColor(new Color(0, 0, 0, 100));
                rd.fillRoundRect(-10, 355, 35 + (int) (dist * modp), 40, 12, 12);
            }

            rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(30, 70, 150) : new Color(co[ci][0], co[ci][1], co[ci][2]));
            rd.fillRoundRect(-10, 340, 20 + (int) (80.0F * modp), 18, 12, 12);
            rd.drawRoundRect(-10, 355, 35 + (int) (dist * modp), 40, 12, 12);
            if (modp == 1.0F) {
                rd.setColor(RMedium.darksky() && !RMedium.lightson() ? new Color(20, 20, 20) : new Color(250, 250, 250));
                rd.setFont(new Font("Arial", 1, 14));
                rd.drawString(RadicalMod.name, 11, 374);
                rd.setFont(new Font("Arial", 1, 11));
                rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(220, 220, 220) : new Color(20, 20, 20));
                rd.drawString("Now listening: ", 5, 353);
                rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(30, 70, 150) : new Color(co[ci][0], co[ci][1], co[ci][2]));
                rd.drawString(RadicalMod.filename, 12, 387);
            }
        } else {
            Polygon pol;
            if (GadgetsInit.modon == 2) {
                rd.setColor(ModUtility.colorSnap(50, 50, 50, 220));
                rd.fillRoundRect(-10, 355, 35 + (int) (dist * modp), 40, 12, 12);
                if (RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                } else {
                    rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                }

                pol = new Polygon();
                pol.addPoint(0, 340);
                pol.addPoint((int) (85.0F * modp), 340);
                pol.addPoint(10 + (int) (85.0F * modp), 349);
                pol.addPoint((int) (85.0F * modp), 358);
                pol.addPoint(0, 358);
                rd.fillPolygon(pol);
                pol = new Polygon();
                pol.addPoint(5 + (int) (85.0F * modp), 340);
                pol.addPoint(15 + (int) (85.0F * modp), 340);
                pol.addPoint(25 + (int) (85.0F * modp), 349);
                pol.addPoint(15 + (int) (85.0F * modp), 358);
                pol.addPoint(5 + (int) (85.0F * modp), 358);
                pol.addPoint(15 + (int) (85.0F * modp), 349);
                rd.fillPolygon(pol);
                rd.drawRoundRect(-10, 355, 35 + (int) (dist * modp), 40, 12, 12);
                if (modp == 1.0F) {
                    rd.setFont(new Font("Arial", 1, 14));
                    rd.drawString(RadicalMod.name, 11, 374);
                    rd.setFont(new Font("Arial", 1, 11));
                    rd.setColor(ModUtility.colorSnap(50, 50, 50));
                    rd.drawString("Now listening: ", 5, 353);
                    if (!RMedium.lightson() && !RMedium.darksky()) {
                        rd.setColor(ModUtility.colorSnap(250, 250, 250));
                    } else {
                        rd.setColor(new Color(250, 250, 250, 220));
                    }

                    rd.drawString(RadicalMod.filename, 12, 387);
                }
            } else if (GadgetsInit.modon == 3) {
                rd.setColor(ModUtility.colorSnap(0, 0, 0, 100));
                rd.fillRect(-10, 355, 35 + (int) (dist * modp), 40);
                if (RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                } else {
                    rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                }

                rd.fillRect(25 + (int) (dist * modp), 355, 10, 41);
                rd.drawRect(-10, 355, 35 + (int) (dist * modp), 40);
                if (modp == 1.0F) {
                    if (RMedium.resdown() < 2) {
                        rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    }

                    rd.setFont(GadgetsInit.fontDigital.deriveFont(1, 15.0F));
                    rd.drawString(RadicalMod.name, 11, 374);
                    rd.setFont(new Font("Arial", 1, 11));
                    if (!RMedium.lightson() && !RMedium.darksky()) {
                        rd.setColor(ModUtility.colorSnap(250, 250, 250));
                    } else {
                        rd.setColor(new Color(250, 250, 250, 220));
                    }

                    rd.drawString(RadicalMod.filename, 12, 387);
                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                }
            } else if (GadgetsInit.modon == 4) {
                rd.setColor(ModUtility.colorSnap(50, 50, 50, 220));
                rd.fillRect(-10, 355, 35 + (int) (dist * modp), 40);
                if (RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2]));
                } else {
                    rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2]));
                }

                rd.drawRect(-10, 355, 35 + (int) (dist * modp), 40);
                pol = new Polygon();
                pol.addPoint(15 + (int) (dist * modp), 355);
                pol.addPoint(25 + (int) (dist * modp), 355);
                pol.addPoint(35 + (int) (dist * modp), 375);
                pol.addPoint(25 + (int) (dist * modp), 396);
                pol.addPoint(15 + (int) (dist * modp), 396);
                pol.addPoint(25 + (int) (dist * modp), 375);
                rd.fillPolygon(pol);
                rd.translate(12, 0);
                rd.fillPolygon(pol);
                rd.translate(-12, 0);
                if (modp == 1.0F) {
                    if (RMedium.resdown() < 2) {
                        rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    }

                    rd.setFont(new Font("Arial", 1, 14));
                    rd.drawString(RadicalMod.name, 11, 374);
                    rd.setFont(new Font("Arial", 1, 11));
                    if (!RMedium.lightson() && !RMedium.darksky()) {
                        rd.setColor(ModUtility.colorSnap(250, 250, 250));
                    } else {
                        rd.setColor(new Color(250, 250, 250, 220));
                    }

                    rd.drawString(RadicalMod.filename, 12, 387);
                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                }
            }
        }

    }

    public static void reset() {
        modc = 150;
        modp = 1.0F;
        speedocnt = 0;
        degreecnt = 117;
        sshake = 0;
        sprevdam = 0;
    }

    public static void position() {
        if (GadgetsInit.poson == 0) {
            if (RMedium.darksky()) {
                final float[] pol = new float[3];
                Color.RGBtoHSB(RMedium.csky(0), RMedium.csky(1), RMedium.csky(2), pol);
                pol[2] = 0.6F;
                final Color tmp = Color.getHSBColor(pol[0], pol[1], pol[2]);
                rd.setColor(tmp);
                rd.fillRect(18, 6, 155, 14);
                rd.drawLine(17, 7, 17, 18);
                rd.drawLine(16, 9, 16, 16);
                rd.drawLine(173, 7, 173, 18);
                rd.drawLine(174, 9, 174, 16);
                rd.fillRect(40, 26, 107, 21);
                rd.drawLine(39, 27, 39, 45);
                rd.drawLine(38, 29, 38, 43);
                rd.drawLine(147, 27, 147, 45);
                rd.drawLine(148, 29, 148, 43);
            }

            rd.drawImage(xtGraphics.lap, 19, 7, null);
            rd.setColor(new Color(0, 0, 100));
            rd.drawString("" + (RMad.nlaps() + 1) + " / " + RCheckPoints.nlaps() + "", 51, 18);
            rd.drawImage(xtGraphics.was, 92, 7, null);
            rd.setColor(new Color(0, 0, 100));
            rd.drawString("" + RCheckPoints.wasted() + " / " + (TData.nplayers - 1) + "", 150, 18);
            rd.drawImage(xtGraphics.pos, 42, 27, null);
            rd.drawImage(xtGraphics.rank[RCheckPoints.pos(TData.im)], 110, 28, null);
        } else {
            String pos;
            int pol1;
            int tmp1;
            if (GadgetsInit.poson == 1) {
                if (RMedium.darksky() || RMedium.lightson()) {
                    rd.setColor(new Color(0, 0, 0, 100));
                    if (RMedium.resdown() == 0) {
                        rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    }

                    rd.fillRoundRect(13, 6, 173, 18, 5, 5);
                    rd.fillRoundRect(40, 29, 73, 18, 5, 5);
                    if (RMedium.resdown() == 0) {
                        rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    }
                }

                if (RMedium.resdown() == 0) {
                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                }

                rd.setFont(GadgetsInit.fontYukari.deriveFont(1, 15.0F));
                rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(0, 0, 0) : new Color(250, 250, 250));
                rd.drawString("lap", 19, 20);
                rd.drawString("wasted", 90, 20);
                rd.drawString("position", 46, 42);
                rd.setFont(GadgetsInit.fontYukari.deriveFont(0, 13.0F));
                rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(30, 70, 150) : new Color(co[ci][0], co[ci][1], co[ci][2]));
                rd.drawString(RMad.nlaps() + 1 + " / " + RCheckPoints.nlaps(), 51, 20);
                rd.drawString(RCheckPoints.wasted() + " / " + (TData.nplayers - 1), 154, 20);
                rd.setFont(GadgetsInit.fontYukari.deriveFont(1, 72.0F));
                pol1 = RCheckPoints.pos(TData.im) + 1;
                tmp1 = rd.getFontMetrics().stringWidth("" + pol1);
                rd.setColor(ModUtility.colorSnap(0, 0, 0, 110));
                rd.drawString("" + pol1, 112, 82);
                rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(30, 70, 150) : new Color(co[ci][0], co[ci][1], co[ci][2]));
                rd.drawString("" + pol1, 110, 80);
                rd.setFont(GadgetsInit.fontYukari.deriveFont(1, 32.0F));
                if (pol1 == 1) {
                    pos = "st";
                } else if (pol1 == 2) {
                    pos = "nd";
                } else if (pol1 == 3) {
                    pos = "rd";
                } else {
                    pos = "th";
                }

                rd.setColor(ModUtility.colorSnap(0, 0, 0, 110));
                rd.drawString(pos, 113 + tmp1, 77);
                rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(0, 0, 0) : new Color(250, 250, 250));
                rd.drawString(pos, 111 + tmp1, 75);
                rd.setFont(new Font("Arial", 1, 11));
                if (RMedium.resdown() == 0) {
                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                }
            } else if (GadgetsInit.poson == 2) {
                rd.setColor(ModUtility.colorSnap(50, 50, 50, 220));
                if (RMedium.resdown() == 0) {
                    rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                }

                rd.fillRoundRect(13, 6, 165, 18, 5, 5);
                rd.fillRoundRect(36, 29, 92, 26, 5, 5);
                if (RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                } else {
                    rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                }

                rd.drawRoundRect(13, 6, 165, 18, 5, 5);
                rd.drawRoundRect(36, 29, 92, 26, 5, 5);
                rd.setFont(new Font("Arial", 1, 12));
                rd.drawString("Lap:", 19, 20);
                rd.drawString("Wasted:", 92, 20);
                rd.drawString("Position:", 45, 45);
                if (!RMedium.lightson() && !RMedium.darksky()) {
                    rd.setColor(ModUtility.colorSnap(250, 250, 250));
                } else {
                    rd.setColor(new Color(250, 250, 250, 220));
                }

                rd.drawString("" + (RMad.nlaps() + 1) + " / " + RCheckPoints.nlaps() + "", 51, 20);
                rd.drawString("" + RCheckPoints.wasted() + " / " + (TData.nplayers - 1) + "", 150, 20);
                if (RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                } else {
                    rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                }

                rd.setFont(new Font("Arial", 1, 23));
                pol1 = RCheckPoints.pos(TData.im) + 1;
                tmp1 = rd.getFontMetrics().stringWidth("" + pol1);
                rd.drawString("" + pol1, 98, 51);
                rd.setFont(new Font("Arial", 1, 11));
                if (pol1 == 1) {
                    pos = "st";
                } else if (pol1 == 2) {
                    pos = "nd";
                } else if (pol1 == 3) {
                    pos = "rd";
                } else {
                    pos = "th";
                }

                if (!RMedium.lightson() && !RMedium.darksky()) {
                    rd.setColor(ModUtility.colorSnap(250, 250, 250));
                } else {
                    rd.setColor(new Color(250, 250, 250, 220));
                }

                rd.drawString(pos, 99 + tmp1, 50);
                if (RMedium.resdown() == 0) {
                    rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                }
            } else if (GadgetsInit.poson == 3) {
                if (RMedium.resdown() == 0) {
                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                }

                rd.setColor(ModUtility.colorSnap(0, 0, 0, 100));
                rd.fillRoundRect(6, 6, 120, 80, 10, 10);
                if (RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                } else {
                    rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                }

                rd.drawString("Lap:", 12, 20);
                rd.drawString("Wasted:", 12, 35);
                rd.drawString("Position:", 12, 50);
                rd.setFont(GadgetsInit.fontDigital.deriveFont(2, 52.0F));
                pol1 = RCheckPoints.pos(TData.im) + 1;
                rd.drawString("" + pol1, 104 - rd.getFontMetrics().stringWidth("" + pol1), 80);
                rd.setFont(GadgetsInit.fontDigital.deriveFont(1, 13.0F));
                String tmp2;
                if (pol1 == 1) {
                    tmp2 = "st";
                } else if (pol1 == 2) {
                    tmp2 = "nd";
                } else if (pol1 == 3) {
                    tmp2 = "rd";
                } else {
                    tmp2 = "th";
                }

                if (!RMedium.lightson() && !RMedium.darksky()) {
                    rd.setColor(ModUtility.colorSnap(250, 250, 250));
                } else {
                    rd.setColor(new Color(250, 250, 250, 220));
                }

                rd.drawString(tmp2, 108, 79);
                pos = RMad.nlaps() + 1 + " / " + RCheckPoints.nlaps();
                rd.drawString(pos, 120 - rd.getFontMetrics().stringWidth(pos), 21);
                pos = RCheckPoints.wasted() + " / " + (TData.nplayers - 1);
                rd.drawString(pos, 120 - rd.getFontMetrics().stringWidth(pos), 36);
                rd.setFont(new Font("Arial", 1, 11));
                if (RMedium.resdown() == 0) {
                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                }
            } else if (GadgetsInit.poson == 4) {
                if (RMedium.resdown() == 0) {
                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                }

                rd.setColor(ModUtility.colorSnap(50, 50, 50, 220));
                final Polygon pol2 = new Polygon(new int[] {
                        -1, 220, 200, -1
                }, new int[] {
                        6, 6, 56, 56
                }, 4);
                rd.fillPolygon(pol2);
                if (RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                } else {
                    rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                }

                rd.drawPolygon(pol2);
                final Font tmp3 = rd.getFont();
                rd.setFont(GadgetsInit.fontAdventure.deriveFont(0, 14.0F));
                rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(250, 250, 250) : Color.WHITE);
                rd.drawString("Lap:", 15, 22);
                rd.drawString("Wasted:", 102, 22);
                rd.drawString("Position:", 32, 42);
                rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2]) : new Color(co[ci][0], co[ci][1], co[ci][2]));
                rd.drawString(RMad.nlaps() + 1 + " / " + RCheckPoints.nlaps(), 46, 22);
                rd.drawString(RCheckPoints.wasted() + " / " + (TData.nplayers - 1), 160, 22);
                final int pos1 = RCheckPoints.pos(TData.im) + 1;
                String sufix;
                if (pos1 == 1) {
                    sufix = "st";
                } else if (pos1 == 2) {
                    sufix = "nd";
                } else if (pos1 == 3) {
                    sufix = "rd";
                } else {
                    sufix = "th";
                }

                sufix = sufix + " / " + (TData.nplayers - RCheckPoints.wasted());
                rd.drawString(sufix, 125, 42);
                rd.setFont(GadgetsInit.fontAdventure.deriveFont(0, 32.0F));
                rd.drawString(String.valueOf(pos1), 121 - rd.getFontMetrics().stringWidth(String.valueOf(pos1)), 52);
                rd.setFont(tmp3);
                if (RMedium.resdown() == 0) {
                    rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                }
            }
        }

    }

    public static void status() {
        if (GadgetsInit.staton == 0) {
            if (RMedium.darksky()) {
                final float[] pol = new float[3];
                Color.RGBtoHSB(RMedium.csky(0), RMedium.csky(1), RMedium.csky(2), pol);
                pol[2] = 0.6F;
                final Color pow = Color.getHSBColor(pol[0], pol[1], pol[2]);
                rd.setColor(pow);
                rd.fillRect(602, 9, 54, 14);
                rd.drawLine(601, 10, 601, 21);
                rd.drawLine(600, 12, 600, 19);
                rd.fillRect(607, 29, 49, 14);
                rd.drawLine(606, 30, 606, 41);
                rd.drawLine(605, 32, 605, 39);
            }

            rd.drawImage(xtGraphics.dmg, 600, 7, null);
            rd.drawImage(xtGraphics.pwr, 600, 27, null);
            RadicalHUD.DefaultHUD.drawstat(RCarDefine.maxmag(TData.sc), RMad.hitmag(), RMad.power());
        } else if (GadgetsInit.staton == 1) {
            rd.translate(28, 0);
            if (RMedium.resdown() == 0) {
                rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }

            if (RMedium.darksky() || RMedium.lightson()) {
                rd.setColor(new Color(0, 0, 0, 100));
                rd.fillRoundRect(590, 6, 174, 18, 5, 5);
                rd.fillRoundRect(597, 26, 167, 18, 5, 5);
            }

            rd.setFont(GadgetsInit.fontYukari.deriveFont(1, 15.0F));
            rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(0, 0, 0) : new Color(250, 250, 250));
            rd.drawString("damage", 596, 19);
            rd.drawString("power", 605, 39);
            if (RMedium.resdown() == 0) {
                rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }

            RadicalHUD.DefaultHUD.drawstat(RCarDefine.maxmag(TData.sc), RMad.hitmag(), RMad.power());
            rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(0, 40, 120) : new Color(co[ci][0], co[ci][1], co[ci][2]));
            final Stroke var15 = rd.getStroke();
            rd.setStroke(new BasicStroke(2.0F));
            final int[] var18 = new int[4];
            final int[] dam = new int[4];
            var18[0] = 661;
            dam[0] = 10;
            var18[1] = 661;
            dam[1] = 20;
            var18[2] = 760;
            dam[2] = 20;
            var18[3] = 760;
            dam[3] = 10;
            rd.drawPolygon(var18, dam, 4);
            dam[0] = 30;
            dam[1] = 40;
            dam[2] = 40;
            dam[3] = 30;
            rd.drawPolygon(var18, dam, 4);
            rd.setStroke(var15);
            rd.translate(-28, 0);
            rd.setFont(new Font("Arial", 1, 11));
        } else {
            int shakex;
            int shakey;
            int stroke;
            int var20;
            int var21;
            if (GadgetsInit.staton == 2) {
                if (RMedium.resdown() == 0) {
                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                }

                rd.setColor(ModUtility.colorSnap(50, 50, 50, 220));
                rd.fill(getBFpow());
                if (RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                } else {
                    rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                }

                rd.draw(getBFpow());
                rd.drawString("Damage", 669, 41);
                rd.drawString("Power", 669, 52);
                if (RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(250, 250, 250));
                } else {
                    rd.setColor(new Color(250, 250, 250));
                }

                String var16 = (int) (100.0F * ((float) RMad.hitmag() / (float) RCarDefine.maxmag(TData.sc))) + "%";
                rd.drawString(var16, 778 - rd.getFontMetrics().stringWidth(var16), 41);
                var16 = (int) (RMad.power() / 98.0F * 100.0D) + "%";
                rd.drawString(var16, 778 - rd.getFontMetrics().stringWidth(var16), 52);
                var20 = 244;
                var21 = (int) (98.0F * ((float) RMad.hitmag() / (float) RCarDefine.maxmag(TData.sc)));
                if (var21 > 33) {
                    var20 = (int) (244.0F - 233.0F * ((var21 - 33) / 65.0F));
                }

                if (var21 > 70) {
                    if (dmcnt < 10) {
                        if (dmflk) {
                            var20 = 170;
                            dmflk = false;
                        } else {
                            dmflk = true;
                        }
                    }

                    dmcnt += 1;
                    if (dmcnt > 167.0D - var21 * 1.5D) {
                        dmcnt = 0;
                    }
                }

                if (var20 > 255) {
                    var20 = 255;
                }

                if (var20 < 0) {
                    var20 = 0;
                }

                if (RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(244, var20, 11));
                } else {
                    rd.setColor(new Color(244, var20, 11, 200));
                }

                shakex = (int) ((float) RMad.hitmag() / (float) RCarDefine.maxmag(TData.sc) * 110.0F);

                for (shakey = 0; shakey < 110; shakey += 8) {
                    if (shakey >= shakex) {
                        rd.setColor(ModUtility.colorSnap(50, 50, 50, 220));
                    }

                    rd.fillRect(669 + shakey, shakey == 0 ? 18 : shakey == 8 ? 15 : 12, 6, shakey == 0 ? 11 : shakey == 8 ? 14 : 17);
                }

                final short var24 = 632;
                final byte tmp = 40;
                final int degrees = 115 - (int) (RMad.power() / 98.0F * 230.0D);
                rd.setColor(new Color(250, 0, 0));
                final Polygon pol1 = new Polygon();
                int str = var24 - (int) (Medium.sin(degrees + 180) * 5.0F);
                stroke = tmp - (int) (Medium.cos(degrees + 180) * 5.0F);
                pol1.addPoint(str, stroke);
                str = var24 - (int) (Medium.sin(degrees + 90) * 3.5D);
                stroke = tmp - (int) (Medium.cos(degrees + 90) * 3.5D);
                pol1.addPoint(str, stroke);
                str = var24 - (int) (Medium.sin(degrees) * 32.0F);
                stroke = tmp - (int) (Medium.cos(degrees) * 32.0F);
                pol1.addPoint(str, stroke);
                str = var24 - (int) (Medium.sin(degrees - 90) * 3.5D);
                stroke = tmp - (int) (Medium.cos(degrees - 90) * 3.5D);
                pol1.addPoint(str, stroke);
                rd.fillPolygon(pol1);
                rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            } else {
                String var28;
                if (GadgetsInit.staton == 3) {
                    if (RMedium.resdown() == 0) {
                        rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    }

                    final int var17 = (int) (RMad.power() / 98.0F * 240.0F);
                    var20 = (int) ((float) RMad.hitmag() / (float) RCarDefine.maxmag(TData.sc) * 124.0F);
                    rd.setColor(ModUtility.colorSnap(0, 0, 0, 100));
                    rd.fillRoundRect(604, 6, 191, 42, 10, 10);
                    if (RMedium.darksky() && !RMedium.lightson()) {
                        rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                    } else {
                        rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                    }

                    rd.setFont(new Font("Arial", 1, 11));
                    rd.drawString("DAMAGE", 661, 43);
                    rd.drawString("POW", 610, 21);
                    if (RMedium.resdown() != 0) {
                        rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    }

                    final Shape var22 = rd.getClip();
                    final Area var23 = new Area(new Rectangle2D.Float(0.0F, 0.0F, 800.0F, 450.0F));
                    final Area var25 = new Area(new Ellipse2D.Float(626.0F, 19.0F, 16.0F, 16.0F));
                    var23.subtract(var25);
                    rd.setClip(var23);

                    int var26;
                    for (var26 = 0; var26 < 12; ++var26) {
                        if (var26 * 20 < var17) {
                            rd.fillArc(var17 >= 240 && var26 == groove / 3 - 1 ? 617 : 619, var17 >= 240 && var26 == groove / 3 - 1 ? 10 : 12, var17 >= 240 && var26 == groove / 3 - 1 ? 34 : 30, var17 >= 240 && var26 == groove / 3 - 1 ? 34 : 30, -185 + var26 * 21, 14);
                        }
                    }

                    rd.setClip(var22);
                    if (RMedium.resdown() != 0) {
                        rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    }

                    rd.drawRoundRect(604, 6, 191, 42, 10, 10);
                    var26 = (int) (var20 / 124.0F * 100.0F);
                    if (var26 >= 45 && var26 < 80) {
                        if (RMedium.darksky() && !RMedium.lightson()) {
                            rd.setColor(ModUtility.colorSnap(250, 250, 0, 220));
                        } else {
                            rd.setColor(new Color(250, 250, 0, 220));
                        }
                    } else if (var26 >= 80) {
                        if (RMedium.darksky() && !RMedium.lightson()) {
                            rd.setColor(ModUtility.colorSnap(250, 50, 0, 220));
                        } else {
                            rd.setColor(new Color(250, 30, 0, 220));
                        }
                    }

                    var28 = var26 + "%";
                    rd.drawString(var28, 790 - rd.getFontMetrics().stringWidth(var28), 43);

                    for (stroke = 0; stroke < var20 && stroke < 125; stroke += 4) {
                        rd.fillRect(661 + stroke, var26 >= 50 && stroke == groove ? 11 : 13, 2, var26 >= 50 && stroke == groove ? 21 : 17);
                    }

                    groove += 4;
                    if (groove > 124) {
                        groove = 0;
                    }

                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                    rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                } else if (GadgetsInit.staton == 4) {
                    if (RMedium.resdown() == 0) {
                        rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    }

                    rd.setColor(ModUtility.colorSnap(50, 50, 50, 220));
                    Polygon var19 = new Polygon(new int[] {
                            580, 801, 801, 600
                    }, new int[] {
                            6, 6, 56, 56
                    }, 4);
                    rd.fillPolygon(var19);
                    if (RMedium.darksky() && !RMedium.lightson()) {
                        rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                    } else {
                        rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                    }

                    rd.drawPolygon(var19);
                    var20 = (int) (RMad.power() / 98.0F * 116.0F);
                    var21 = (int) ((float) RMad.hitmag() / (float) RCarDefine.maxmag(TData.sc) * 116.0F);
                    shakex = 0;
                    shakey = 0;
                    if (var21 != sprevdam) {
                        if (var21 > sprevdam) {
                            sshake = 15;
                        }

                        sprevdam = var21;
                    }

                    if (sshake > 0) {
                        shakex = 3 - (int) (Math.random() * 6.0D);
                        shakey = 3 - (int) (Math.random() * 6.0D);
                        rd.translate(shakex, shakey);
                    }

                    final Font var27 = rd.getFont();
                    rd.setFont(GadgetsInit.fontAdventure.deriveFont(0, 14.0F));
                    rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(250, 250, 250) : Color.WHITE);
                    rd.drawString("Damage", 605, 27);
                    rd.drawString("Power", 614, 47);
                    if (var21 > 0) {
                        rd.setClip(new Rectangle(669, 14, 116, 14));
                        rd.setColor(var21 > 90 ? Color.RED : var21 > 50 ? new Color(250, 140, 0) : Color.YELLOW);
                        var19 = new Polygon(new int[] {
                                650, 674 + var21, 669 + var21, 650
                        }, new int[] {
                                14, 14, 28, 28
                        }, 4);
                        rd.fillPolygon(var19);
                        rd.setClip((Shape) null);
                    }

                    var28 = (int) Math.floor(100.0F * ((float) RMad.hitmag() / (float) RCarDefine.maxmag(TData.sc))) + "%";
                    rd.setFont(new Font("Arial", 1, 11));
                    var21 = var21 > 116 ? 116 : var21;
                    if (var21 > 30) {
                        rd.setColor(Color.BLACK);
                        rd.drawString(var28, 671 + (var21 - rd.getFontMetrics().stringWidth(var28)) / 2, 25);
                    } else {
                        rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(250, 250, 250) : Color.WHITE);
                        rd.drawString(var28, 675 + var21, 25);
                    }

                    if (RMedium.darksky() && !RMedium.lightson()) {
                        rd.setColor(ModUtility.colorSnap(50, 204, 255, 220));
                    } else {
                        rd.setColor(new Color(50, 204, 255, 220));
                    }

                    for (stroke = 0; stroke < var20 && stroke < 114; stroke += 6) {
                        rd.fillRect(671 + stroke, var20 >= 110 && stroke == sgroove ? 35 : 36, 4, var20 >= 110 && stroke == sgroove ? 12 : 10);
                    }

                    sgroove += 6;
                    if (sgroove > 114) {
                        sgroove = 0;
                    }

                    var28 = (int) Math.floor(100.0F * (RMad.power() / 98.0F)) + "%";
                    rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(250, 250, 250) : Color.WHITE);
                    rd.drawString(var28, 782 - rd.getFontMetrics().stringWidth(var28), 45);
                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                    rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    rd.setColor(ModUtility.colorSnap(0, 0, 0));
                    final Stroke var29 = rd.getStroke();
                    rd.setStroke(new BasicStroke(2.0F));
                    rd.drawRect(668, 13, 118, 16);
                    rd.drawRect(668, 33, 118, 16);
                    rd.setStroke(var29);
                    rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(250, 0, 0) : Color.RED);
                    rd.drawRect(667, 12, 119, 17);
                    rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(50, 204, 255) : new Color(50, 204, 255));
                    rd.drawRect(667, 32, 119, 17);
                    rd.setFont(var27);
                    if (sshake > 0) {
                        rd.translate(-shakex, -shakey);
                        --sshake;
                    }
                }
            }
        }

    }

    public static void opponentStatus(final int i, final int i_48_, final boolean bool) {
        final int i_66_ = TData.nplayers;
        int i_63_;
        boolean bool_64_;
        int i_65_;
        int bool_74_;
        int j;
        int var17;
        int var18;
        if (GadgetsInit.arrowon == 0) {
            for (i_63_ = 0; i_63_ < i_66_; ++i_63_) {
                bool_64_ = false;

                for (i_65_ = 0; i_65_ < TData.nplayers; ++i_65_) {
                    if (RCheckPoints.pos(i_65_) == i_63_ && RCheckPoints.dested(i_65_) == 0 && !bool_64_) {
                        rd.setColor(new Color(0, 0, 100));
                        if (i_63_ == 0) {
                            rd.drawString("1st", 673, 76 + 30 * i_63_);
                        }

                        if (i_63_ == 1) {
                            rd.drawString("2nd", 671, 76 + 30 * i_63_);
                        }

                        if (i_63_ == 2) {
                            rd.drawString("3rd", 671, 76 + 30 * i_63_);
                        }

                        if (i_63_ >= 3) {
                            rd.drawString("" + (i_63_ + 1) + "th", 671, 76 + 30 * i_63_);
                        }

                        rd.setColor(new Color(0, 0, 0));
                        rd.drawString(RxtGraphics.plnames(i_65_), 730 - xtGraphics.ftm.stringWidth(RxtGraphics.plnames(i_65_)) / 2, 70 + 30 * i_63_);
                        bool_74_ = (int) (60.0F * RCheckPoints.magperc(i_65_));
                        final short idam = 244;
                        j = 244;
                        final byte j1 = 11;
                        if (bool_74_ > 20) {
                            j = (int) (244.0F - 233.0F * ((bool_74_ - 20) / 40.0F));
                        }

                        var17 = (int) (idam + idam * (RMedium.snap(0) / 100.0F));
                        if (var17 > 255) {
                            var17 = 255;
                        }

                        if (var17 < 0) {
                            var17 = 0;
                        }

                        j = (int) (j + j * (RMedium.snap(1) / 100.0F));
                        if (j > 255) {
                            j = 255;
                        }

                        if (j < 0) {
                            j = 0;
                        }

                        var18 = (int) (j1 + j1 * (RMedium.snap(2) / 100.0F));
                        if (var18 > 255) {
                            var18 = 255;
                        }

                        if (var18 < 0) {
                            var18 = 0;
                        }

                        rd.setColor(new Color(var17, j, var18));
                        rd.fillRect(700, 74 + 30 * i_63_, bool_74_, 5);
                        rd.setColor(new Color(0, 0, 0));
                        rd.drawRect(700, 74 + 30 * i_63_, 60, 5);
                        boolean bool_74_1 = false;
                        if ((RxtGraphics.im() != i_65_ || RxtGraphics.multion() >= 2) && i > 661 && i < 775 && i_48_ > 58 + 30 * i_63_ && i_48_ < 83 + 30 * i_63_) {
                            bool_74_1 = true;
                            if (bool) {
                                if (!RxtGraphics.onlock()) {
                                    if (RxtGraphics.alocked() == i_65_ && RxtGraphics.multion() < 2) {
                                        xtGraphics.alocked = -1;
                                    } else {
                                        xtGraphics.alocked = i_65_;
                                        if (RxtGraphics.multion() >= 2) {
                                            // TODO
                                            //xt.im = i_65_;
                                        }
                                    }
                                }

                                RxtGraphics.set_onlock(true);
                            } else if (RxtGraphics.onlock()) {
                                RxtGraphics.set_onlock(false);
                            }
                        }

                        if (RxtGraphics.alocked() == i_65_) {
                            var17 = (int) (159.0F + 159.0F * (RMedium.snap(0) / 100.0F));
                            if (var17 > 255) {
                                var17 = 255;
                            }

                            if (var17 < 0) {
                                var17 = 0;
                            }

                            j = (int) (207.0F + 207.0F * (RMedium.snap(1) / 100.0F));
                            if (j > 255) {
                                j = 255;
                            }

                            if (j < 0) {
                                j = 0;
                            }

                            var18 = (int) (255.0F + 255.0F * (RMedium.snap(2) / 100.0F));
                            if (var18 > 255) {
                                var18 = 255;
                            }

                            if (var18 < 0) {
                                var18 = 0;
                            }

                            rd.setColor(new Color(var17, j, var18));
                            rd.drawRect(661, 58 + 30 * i_63_, 114, 25);
                            rd.drawRect(662, 59 + 30 * i_63_, 112, 23);
                        }

                        if (bool_74_1 && !RxtGraphics.onlock()) {
                            if (RxtGraphics.alocked() == i_65_) {
                                var17 = (int) (120.0F + 120.0F * (RMedium.snap(0) / 100.0F));
                                if (var17 > 255) {
                                    var17 = 255;
                                }

                                if (var17 < 0) {
                                    var17 = 0;
                                }

                                j = (int) (114.0F + 114.0F * (RMedium.snap(1) / 100.0F));
                                if (j > 255) {
                                    j = 255;
                                }

                                if (j < 0) {
                                    j = 0;
                                }

                                var18 = (int) (255.0F + 255.0F * (RMedium.snap(2) / 100.0F));
                                if (var18 > 255) {
                                    var18 = 255;
                                }

                                if (var18 < 0) {
                                    var18 = 0;
                                }
                            } else {
                                var17 = (int) (140.0F + 140.0F * (RMedium.snap(0) / 100.0F));
                                if (var17 > 255) {
                                    var17 = 255;
                                }

                                if (var17 < 0) {
                                    var17 = 0;
                                }

                                j = (int) (160.0F + 160.0F * (RMedium.snap(1) / 100.0F));
                                if (j > 255) {
                                    j = 255;
                                }

                                if (j < 0) {
                                    j = 0;
                                }

                                var18 = (int) (255.0F + 255.0F * (RMedium.snap(2) / 100.0F));
                                if (var18 > 255) {
                                    var18 = 255;
                                }

                                if (var18 < 0) {
                                    var18 = 0;
                                }
                            }

                            rd.setColor(new Color(var17, j, var18));
                            rd.drawRect(660, 57 + 30 * i_63_, 116, 27);
                        }

                        bool_64_ = true;
                    }
                }
            }
        } else if (GadgetsInit.arrowon == 1) {
            for (i_63_ = 0; i_63_ < i_66_; ++i_63_) {
                bool_64_ = false;

                for (i_65_ = 0; i_65_ < i_66_; ++i_65_) {
                    if (RCheckPoints.pos(i_65_) == i_63_ && RCheckPoints.dested(i_65_) == 0 && !bool_64_) {
                        if (RMedium.darksky() || RMedium.lightson()) {
                            rd.setColor(new Color(0, 0, 0, 100));
                            rd.fillRect(654, 58 + 30 * i_63_, 121, 25);
                        }

                        if (RxtGraphics.im() == i_65_ || RxtGraphics.alocked() == i_65_ && RxtGraphics.multion() < 2) {
                            rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(0, 40, 120) : new Color(co[ci][0], co[ci][1], co[ci][2]));
                            rd.drawRect(654, 58 + 30 * i_63_, 121, 25);
                            rd.fillRect(655, 59 + 30 * i_63_, RxtGraphics.alocked() == i_65_ && RxtGraphics.multion() < 2 ? 4 : 29, 24);
                        }

                        rd.setColor(RxtGraphics.im() == i_65_ ? RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(250, 250, 250) : new Color(0, 0, 0) : RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(30, 70, 150) : new Color(co[ci][0], co[ci][1], co[ci][2]));
                        if (i_63_ == 0) {
                            rd.drawString("1st", (RxtGraphics.im() == i_65_ ? -1 : 2) + (RxtGraphics.alocked() == i_65_ && RxtGraphics.multion() < 2 ? 2 : 0) + 662, 76 + 30 * i_63_);
                        }

                        if (i_63_ == 1) {
                            rd.drawString("2nd", (RxtGraphics.im() == i_65_ ? 0 : 2) + (RxtGraphics.alocked() == i_65_ && RxtGraphics.multion() < 2 ? 2 : 0) + 660, 76 + 30 * i_63_);
                        }

                        if (i_63_ == 2) {
                            rd.drawString("3rd", (RxtGraphics.im() == i_65_ ? 1 : 2) + (RxtGraphics.alocked() == i_65_ && RxtGraphics.multion() < 2 ? 2 : 0) + 660, 76 + 30 * i_63_);
                        }

                        if (i_63_ >= 3) {
                            rd.drawString(i_63_ + 1 + "th", (RxtGraphics.im() == i_65_ ? 1 : 2) + (RxtGraphics.alocked() == i_65_ && RxtGraphics.multion() < 2 ? 2 : 0) + 660, 76 + 30 * i_63_);
                        }

                        rd.setFont(new Font("Arial", 1, 11));
                        xtGraphics.ftm = rd.getFontMetrics();
                        rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(0, 0, 0) : new Color(250, 250, 250));
                        rd.drawString(RxtGraphics.plnames(i_65_), 730 - xtGraphics.ftm.stringWidth(RxtGraphics.plnames(i_65_)) / 2, 70 + 30 * i_63_);
                        rd.setFont(new Font("Arial", 1, 11));
                        xtGraphics.ftm = rd.getFontMetrics();
                        bool_74_ = (int) (60.0F * RCheckPoints.magperc(i_65_));
                        var17 = 244;
                        if (bool_74_ > 20) {
                            var17 = (int) (244.0F - 233.0F * ((bool_74_ - 20) / 40.0F));
                        }

                        rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(244, var17, 11) : new Color(244, var17, 11));
                        rd.fillRect(700, 74 + 30 * i_63_, bool_74_, 5);
                        rd.setColor(RMedium.darksky() && !RMedium.lightson() ? new Color(0, 0, 0) : new Color(250, 250, 250));
                        rd.drawRect(700, 74 + 30 * i_63_, 60, 5);
                        boolean var20 = false;
                        if ((RxtGraphics.im() != i_65_ || RxtGraphics.multion() >= 2) && i > 653 && i < 775 && i_48_ > 58 + 30 * i_63_ && i_48_ < 83 + 30 * i_63_) {
                            var20 = true;
                            if (bool) {
                                if (!RxtGraphics.onlock()) {
                                    if (RxtGraphics.alocked() == i_65_ && RxtGraphics.multion() < 2) {
                                        xtGraphics.alocked = -1;
                                    } else {
                                        xtGraphics.alocked = i_65_;
                                        if (RxtGraphics.multion() >= 2) {
                                            // TODO
                                            //xt.im = i_65_;
                                        }
                                    }
                                }

                                RxtGraphics.set_onlock(true);
                            } else if (RxtGraphics.onlock()) {
                                RxtGraphics.set_onlock(false);
                            }
                        }

                        if (var20 && !RxtGraphics.onlock()) {
                            if (RxtGraphics.alocked() == i_65_) {
                                rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(120, 114, 255) : new Color(120, 140, 250));
                            } else {
                                rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(140, 160, 255) : new Color(140, 160, 250));
                            }

                            rd.drawRect(653, 57 + 30 * i_63_, 123, 27);
                            rd.drawRect(654, 58 + 30 * i_63_, 121, 25);
                        }

                        bool_64_ = true;
                    }
                }
            }
        } else {
            boolean var16;
            if (GadgetsInit.arrowon != 2 && GadgetsInit.arrowon != 4) {
                if (GadgetsInit.arrowon == 3) {
                    if (RMedium.resdown() == 0) {
                        rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    }

                    for (i_63_ = 0; i_63_ < i_66_; ++i_63_) {
                        bool_64_ = false;

                        for (i_65_ = 0; i_65_ < i_66_; ++i_65_) {
                            if (RCheckPoints.pos(i_65_) == i_63_ && RCheckPoints.dested(i_65_) == 0 && !bool_64_) {
                                var16 = false;
                                if ((RxtGraphics.im() != i_65_ || RxtGraphics.multion() >= 2) && i > 684 && i < 795 && i_48_ > 58 + 30 * i_63_ && i_48_ < 83 + 30 * i_63_) {
                                    var16 = true;
                                    if (bool) {
                                        if (!RxtGraphics.onlock()) {
                                            if (RxtGraphics.alocked() == i_65_ && RxtGraphics.multion() < 2) {
                                                xtGraphics.alocked = -1;
                                            } else {
                                                xtGraphics.alocked = i_65_;
                                                if (RxtGraphics.multion() >= 2) {
                                                    // TODO
                                                    //xt.im = i_65_;
                                                }
                                            }
                                        }

                                        RxtGraphics.set_onlock(true);
                                    } else if (RxtGraphics.onlock()) {
                                        RxtGraphics.set_onlock(false);
                                    }
                                }

                                if ((!var16 || RxtGraphics.onlock()) && (xtGraphics.alocked != i_65_ || RxtGraphics.multion() >= 2)) {
                                    rd.setColor(ModUtility.colorSnap(0, 0, 0, 100));
                                } else {
                                    rd.setColor(ModUtility.colorSnap(0, 0, 0, 150));
                                }

                                rd.fillRoundRect(684, 58 + 30 * i_63_, 111, 25, 8, 8);
                                if (RMedium.darksky() && !RMedium.lightson()) {
                                    rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                                } else {
                                    rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                                }

                                if (RxtGraphics.im() == i_65_) {
                                    rd.drawRoundRect(684, 58 + 30 * i_63_, 111, 25, 8, 8);
                                }

                                rd.setFont(GadgetsInit.fontDigital.deriveFont(0, 28.0F));
                                rd.drawString(String.valueOf(i_63_ + 1), 690, 80 + 30 * i_63_);
                                var17 = (int) (100.0F * RCheckPoints.magperc(i_65_));
                                if (var17 < 45) {
                                    if (RMedium.darksky() && !RMedium.lightson()) {
                                        rd.setColor(ModUtility.colorSnap(50, 204, 255, 220));
                                    } else {
                                        rd.setColor(new Color(50, 204, 255, 220));
                                    }
                                } else if (var17 >= 45 && var17 < 80) {
                                    if (RMedium.darksky() && !RMedium.lightson()) {
                                        rd.setColor(ModUtility.colorSnap(250, 250, 0, 220));
                                    } else {
                                        rd.setColor(new Color(250, 250, 0, 220));
                                    }
                                } else if (var17 >= 80) {
                                    if (RMedium.darksky() && !RMedium.lightson()) {
                                        rd.setColor(ModUtility.colorSnap(250, 50, 0, 220));
                                    } else {
                                        rd.setColor(new Color(250, 30, 0, 220));
                                    }
                                }

                                var17 = (int) (RCheckPoints.magperc(i_65_) * 60.0F);

                                for (j = 0; j < var17 && j <= 60; j += 3) {
                                    rd.fillRect(720 + j, 74 + 30 * i_63_, 2, 6);
                                }

                                rd.setFont(new Font("Arial", 1, 11));
                                xtGraphics.ftm = rd.getFontMetrics();
                                rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(250, 250, 250) : new Color(250, 250, 250));
                                rd.drawString(RxtGraphics.plnames(i_65_), 751 - xtGraphics.ftm.stringWidth(RxtGraphics.plnames(i_65_)) / 2, 70 + 30 * i_63_);
                                bool_64_ = true;
                            }
                        }
                    }

                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                    rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                }
            } else {
                if (RMedium.resdown() == 0) {
                    rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                }

                for (i_63_ = 0; i_63_ < i_66_; ++i_63_) {
                    bool_64_ = false;

                    for (i_65_ = 0; i_65_ < i_66_; ++i_65_) {
                        if (RCheckPoints.pos(i_65_) == i_63_ && RCheckPoints.dested(i_65_) == 0 && !bool_64_) {
                            var16 = false;
                            if ((RxtGraphics.im() != i_65_ || RxtGraphics.multion() >= 2) && i > 684 && i < 795 && i_48_ > 60 + 30 * i_63_ && i_48_ < 85 + 30 * i_63_) {
                                var16 = true;
                                if (bool) {
                                    if (!RxtGraphics.onlock()) {
                                        if (RxtGraphics.alocked() == i_65_ && RxtGraphics.multion() < 2) {
                                            xtGraphics.alocked = -1;
                                        } else {
                                            xtGraphics.alocked = i_65_;
                                            if (RxtGraphics.multion() >= 2) {
                                                // TODO
                                                //xt.im = i_65_;
                                            }
                                        }
                                    }

                                    RxtGraphics.set_onlock(true);
                                } else if (RxtGraphics.onlock()) {
                                    RxtGraphics.set_onlock(false);
                                }
                            }

                            if ((!var16 || RxtGraphics.onlock()) && (xtGraphics.alocked != i_65_ || RxtGraphics.multion() >= 2)) {
                                rd.setColor(ModUtility.colorSnap(50, 50, 50, 220));
                            } else {
                                rd.setColor(ModUtility.colorSnap(0, 0, 0, 120));
                            }

                            final Polygon var19 = new Polygon();
                            var19.addPoint(682, 60 + 30 * i_63_);
                            var19.addPoint(793, 60 + 30 * i_63_);
                            var19.addPoint(787, 85 + 30 * i_63_);
                            var19.addPoint(676, 85 + 30 * i_63_);
                            rd.fill(var19);
                            if (RMedium.darksky() && !RMedium.lightson()) {
                                rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                            } else {
                                rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                            }

                            if (RxtGraphics.im() == i_65_) {
                                rd.draw(var19);
                            }

                            rd.setFont(GadgetsInit.arrowon == 2 ? new Font("Arial", 1, 23) : GadgetsInit.fontAdventure.deriveFont(1, 23.0F));
                            rd.drawString(String.valueOf(i_63_ + 1), GadgetsInit.arrowon == 2 ? 769 : 768, (GadgetsInit.arrowon == 2 ? 81 : 82) + 30 * i_63_);
                            j = (int) (100.0F * RCheckPoints.magperc(i_65_));
                            if (j < 25) {
                                if (RMedium.darksky() && !RMedium.lightson()) {
                                    rd.setColor(ModUtility.colorSnap(50, 250, 0, 220));
                                } else {
                                    rd.setColor(new Color(50, 250, 0, 220));
                                }
                            } else if (j >= 25 && j < 60) {
                                if (RMedium.darksky() && !RMedium.lightson()) {
                                    rd.setColor(ModUtility.colorSnap(250, 250, 0, 220));
                                } else {
                                    rd.setColor(new Color(250, 250, 0, 220));
                                }
                            } else if (j >= 60 && j < 80) {
                                if (RMedium.darksky() && !RMedium.lightson()) {
                                    rd.setColor(ModUtility.colorSnap(250, 140, 0, 220));
                                } else {
                                    rd.setColor(new Color(250, 140, 0, 220));
                                }
                            } else if (j >= 80) {
                                if (RMedium.darksky() && !RMedium.lightson()) {
                                    rd.setColor(ModUtility.colorSnap(250, 50, 0, 220));
                                } else {
                                    rd.setColor(new Color(250, 30, 0, 220));
                                }
                            }

                            j = (int) (RCheckPoints.magperc(i_65_) * 60.0F);
                            if (GadgetsInit.arrowon == 2) {
                                for (var18 = 0; var18 < j && var18 <= 60; var18 += 3) {
                                    rd.fillRect(692 + var18, 76 + 30 * i_63_, 2, 6);
                                }
                            } else {
                                rd.fillPolygon(new int[] {
                                        694, 694 + j, 692 + j, 692
                                }, new int[] {
                                        76 + 30 * i_63_, 76 + 30 * i_63_, 81 + 30 * i_63_, 81 + 30 * i_63_
                                }, 4);
                                rd.setColor(new Color(250, 250, 250, 120));
                                rd.drawPolygon(new int[] {
                                        693, 753, 751, 691
                                }, new int[] {
                                        75 + 30 * i_63_, 75 + 30 * i_63_, 81 + 30 * i_63_, 81 + 30 * i_63_
                                }, 4);
                            }

                            rd.setFont(new Font("Arial", 1, 11));
                            xtGraphics.ftm = rd.getFontMetrics();
                            rd.setColor(RMedium.darksky() && !RMedium.lightson() ? ModUtility.colorSnap(250, 250, 250) : new Color(250, 250, 250));
                            rd.drawString(RxtGraphics.plnames(i_65_), 723 - xtGraphics.ftm.stringWidth(RxtGraphics.plnames(i_65_)) / 2, 72 + 30 * i_63_);
                            bool_64_ = true;
                        }
                    }
                }

                rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }
        }

    }

    public static void arrow(int point, final int missedcp, final boolean arrace) {
        short dist;
        int pox;
        int tmp;
        int var33;
        byte var42;
        if (GadgetsInit.arrowon != 0 && GadgetsInit.arrowon != 1) {
            int var22 = RxtGraphics.im();
            int var23 = 0;

            int var24;
            for (var24 = 0; var24 < RCheckPoints.nlaps(); ++var24) {
                for (int var25 = 0; var25 < RCheckPoints.n(); ++var25) {
                    if (RCheckPoints.typ(var25) == 1 || RCheckPoints.typ(var25) == 2) {
                        ++var23;
                        if (var23 == RCheckPoints.clear(RxtGraphics.im()) + 1) {
                            point = var25;
                            break;
                        }
                    }
                }
            }

            int var21;
            int var27;
            if (!arrace) {
                dist = 0;
                if (RCheckPoints.x(point) - RCheckPoints.opx(RxtGraphics.im()) >= 0) {
                    dist = 180;
                }

                var21 = (int) (90 + dist + Math.atan((double) (RCheckPoints.z(point) - RCheckPoints.opz(RxtGraphics.im())) / (double) (RCheckPoints.x(point) - RCheckPoints.opx(RxtGraphics.im()))) / 0.017453292519943295D);
            } else {
                if (RxtGraphics.multion() != 0 && xtGraphics.alocked != -1) {
                    var22 = RxtGraphics.alocked();
                } else {
                    var24 = -1;
                    boolean var26 = false;

                    for (var27 = 0; var27 < TData.nplayers; ++var27) {
                        if (var27 != RxtGraphics.im() && (ModUtility.py(RCheckPoints.opx(RxtGraphics.im()) / 100, RCheckPoints.opx(var27) / 100, RCheckPoints.opz(RxtGraphics.im()) / 100, RCheckPoints.opz(var27) / 100) < var24 || var24 == -1) && (!var26 || RCheckPoints.onscreen(var27) != 0) && RCheckPoints.dested(var27) == 0) {
                            var22 = var27;
                            var24 = ModUtility.py(RCheckPoints.opx(RxtGraphics.im()) / 100, RCheckPoints.opx(var27) / 100, RCheckPoints.opz(RxtGraphics.im()) / 100, RCheckPoints.opz(var27) / 100);
                            if (RCheckPoints.onscreen(var27) != 0) {
                                var26 = true;
                            }
                        }
                    }
                }

                dist = 0;
                if (RCheckPoints.opx(var22) - RCheckPoints.opx(RxtGraphics.im()) >= 0) {
                    dist = 180;
                }

                var21 = (int) (90 + dist + Math.atan((double) (RCheckPoints.opz(var22) - RCheckPoints.opz(RxtGraphics.im())) / (double) (RCheckPoints.opx(var22) - RCheckPoints.opx(RxtGraphics.im()))) / 0.017453292519943295D);
            }

            for (var21 += RMedium.xz(); var21 < 0; var21 += 360) {

            }

            while (var21 > 180) {
                var21 -= 360;
            }

            if (Math.abs(RadicalHUD.ana - var21) < 180) {
                if (Math.abs(RadicalHUD.ana - var21) < 10) {
                    RadicalHUD.ana = var21;
                } else if (RadicalHUD.ana < var21) {
                    RadicalHUD.ana += 10;
                } else {
                    RadicalHUD.ana -= 10;
                }
            } else {
                if (var21 < 0) {
                    RadicalHUD.ana += 15;
                    if (RadicalHUD.ana > 180) {
                        RadicalHUD.ana -= 360;
                    }
                }

                if (var21 > 0) {
                    RadicalHUD.ana -= 15;
                    if (RadicalHUD.ana < -180) {
                        RadicalHUD.ana += 360;
                    }
                }
            }

            var21 = -RadicalHUD.ana;
            float var29;
            if (arrace) {
                var29 = (float) Math.sqrt(Math.pow(RCheckPoints.opz(var22) - RCheckPoints.opz(RxtGraphics.im()), 2.0D) + Math.pow(RCheckPoints.opx(var22) - RCheckPoints.opx(RxtGraphics.im()), 2.0D)) / 2.0F;
            } else {
                var29 = (float) Math.sqrt(Math.pow(RCheckPoints.z(point) - RCheckPoints.opz(RxtGraphics.im()), 2.0D) + Math.pow(RCheckPoints.x(point) - RCheckPoints.opx(RxtGraphics.im()), 2.0D)) / 2.0F;
            }

            if (RMedium.resdown() == 0) {
                rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }

            int t3;
            String var28;
            Area var43;
            if (GadgetsInit.arrowon == 2) {
                rd.setColor(ModUtility.colorSnap(50, 50, 50, 220));
                rd.fillRoundRect(328, 13, 149, 49, 48, 48);
                if (!RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(250, 250, 250));
                } else {
                    rd.setColor(new Color(250, 250, 250));
                }

                var28 = RxtGraphics.plnames(var22);

                for (var27 = 15; var27 > 0; --var27) {
                    rd.setFont(new Font("Arial", 1, var27));
                    if (rd.getFontMetrics().stringWidth(var28) < 83) {
                        break;
                    }
                }

                rd.drawString(var28, 384, 37);
                var28 = String.valueOf((int) (var29 / 100.0F));
                rd.setFont(new Font("Arial", 1, 13));
                xtGraphics.ftm = rd.getFontMetrics();
                rd.drawString(var28, 349 - xtGraphics.ftm.stringWidth(var28) / 2, 38);

                for (var27 = 11; var27 > 0; --var27) {
                    rd.setFont(new Font("Arial", 0, var27));
                    if (rd.getFontMetrics().stringWidth(var28) < 83) {
                        break;
                    }
                }

                if (!RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(220, 220, 220));
                } else {
                    rd.setColor(new Color(220, 220, 220));
                }

                rd.drawString(RCarDefine.names(RxtGraphics.sc(var22)), 385, 49);
                rd.setFont(new Font("Arial", 0, 11));
                xtGraphics.ftm = rd.getFontMetrics();
                rd.drawString("mts", 341, 48);
                final Area var35 = new Area(new Ellipse2D.Float(324.0F, 12.0F, 50.0F, 50.0F));
                var43 = new Area(new Ellipse2D.Float(331.0F, 19.0F, 36.0F, 36.0F));
                var35.subtract(var43);
                final Polygon var41 = new Polygon();
                final short var37 = 349;
                final byte var38 = 37;
                t3 = var37 - (int) (Medium.sin(var21 - 180) * 36.0F);
                int poy1 = var38 - (int) (Medium.cos(var21 - 180) * 36.0F);
                var41.addPoint(t3, poy1);
                t3 = var37 - (int) (Medium.sin(var21 + 30 - 180) * 24.0F);
                poy1 = var38 - (int) (Medium.cos(var21 + 30 - 180) * 24.0F);
                var41.addPoint(t3, poy1);
                t3 = var37 - (int) (Medium.sin(var21 - 30 - 180) * 24.0F);
                poy1 = var38 - (int) (Medium.cos(var21 - 30 - 180) * 24.0F);
                var41.addPoint(t3, poy1);
                final Area trig = new Area(var41);
                var35.add(trig);
                if (arrace) {
                    if (!RMedium.darksky() && !RMedium.lightson()) {
                        rd.setColor(ModUtility.colorSnap(50, 204, 255, 220));
                    } else {
                        rd.setColor(new Color(50, 204, 255, 220));
                    }
                } else if (!RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(250, 250, 20));
                } else {
                    rd.setColor(new Color(250, 250, 20));
                }

                rd.fill(var35);
                if (arrace) {
                    if (!RMedium.darksky() && !RMedium.lightson()) {
                        rd.setColor(ModUtility.colorSnap(0, 154, 205, 220));
                    } else {
                        rd.setColor(new Color(0, 154, 205, 220));
                    }
                } else if (!RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(200, 160, 0));
                } else {
                    rd.setColor(new Color(200, 160, 0));
                }

                final Stroke tmp1 = rd.getStroke();
                rd.setStroke(new BasicStroke(2.0F));
                rd.draw(var35);
                rd.setStroke(tmp1);
                rd.setFont(new Font("Arial", 1, 11));
                xtGraphics.ftm = rd.getFontMetrics();
            } else if (GadgetsInit.arrowon == 3) {
                rd.setColor(ModUtility.colorSnap(0, 0, 0, 100));
                rd.fillRoundRect(320, 6, 160, 42, 10, 10);
                if (!RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(co[ci][0], co[ci][1], co[ci][2], 220));
                } else {
                    rd.setColor(new Color(co[ci][0], co[ci][1], co[ci][2], 220));
                }

                var28 = RxtGraphics.plnames(var22);

                for (var27 = 14; var27 > 0; --var27) {
                    rd.setFont(new Font("Arial", 1, var27));
                    if (rd.getFontMetrics().stringWidth(var28) < 72) {
                        break;
                    }
                }

                rd.drawString(var28, 326, 24);
                rd.setFont(new Font("Arial", 0, 11));
                if (!RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(250, 250, 250, 220));
                } else {
                    rd.setColor(new Color(250, 250, 250, 220));
                }

                rd.drawString(RCarDefine.names(RxtGraphics.sc(var22)), 326, 40);
                rd.setFont(new Font("Arial", 1, 11));
                final Shape var34 = rd.getClip();
                var43 = new Area(new Rectangle2D.Float(0.0F, 0.0F, 800.0F, 450.0F));
                final Area var40 = new Area(new Ellipse2D.Float(421.0F, 15.0F, 36.0F, 13.0F));
                var43.subtract(var40);
                rd.setClip(var43);
                if (arrace) {
                    if (!RMedium.darksky() && !RMedium.lightson()) {
                        rd.setColor(ModUtility.colorSnap(50, 204, 255, 150));
                    } else {
                        rd.setColor(new Color(50, 204, 255, 150));
                    }
                } else if (!RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(220, 220, 0, 150));
                } else {
                    rd.setColor(new Color(220, 220, 0, 150));
                }

                for (var33 = 0; var33 < 17; ++var33) {
                    rd.fillArc(404, 12, 70, 25, -185 + var33 * 21, 14);
                }

                if (arrace) {
                    if (!RMedium.darksky() && !RMedium.lightson()) {
                        rd.setColor(ModUtility.colorSnap(50, 204, 255));
                    } else {
                        rd.setColor(new Color(50, 204, 255));
                    }
                } else if (!RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(220, 220, 0));
                } else {
                    rd.setColor(new Color(220, 220, 0));
                }

                var33 = (int) (var21 / 360.0F * 16.0F);
                tmp = var33 == 0 ? 16 : var33 - 1;
                t3 = var33 == 16 ? 0 : var33 + 1;
                rd.fillArc(404, 12, 70, 25, -100 + var33 * 21, 14);
                rd.fillArc(404, 12, 70, 25, -100 + tmp * 21, 14);
                rd.fillArc(404, 12, 70, 25, -100 + t3 * 21, 14);
                rd.setClip(var34);
            }

            if (GadgetsInit.arrowon == 4) {
                rd.setColor(ModUtility.colorSnap(50, 50, 50, 220));
                rd.fillPolygon(new int[] {
                        320, 340, 460, 479
                }, new int[] {
                        -1, 30, 30, -1
                }, 4);
                if (!RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(250, 250, 250));
                } else {
                    rd.setColor(new Color(250, 250, 250));
                }

                var28 = RxtGraphics.plnames(var22);
                rd.setFont(new Font("Arial", 1, 12));
                rd.drawString(var28, 400 - rd.getFontMetrics().stringWidth(var28) / 2, 13);
                rd.setFont(new Font("Arial", 0, 10));
                if (!RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(220, 220, 220));
                } else {
                    rd.setColor(new Color(220, 220, 220));
                }

                rd.drawString(RCarDefine.names(RxtGraphics.sc(var22)), 400 - rd.getFontMetrics().stringWidth(RCarDefine.names(RxtGraphics.sc(var22))) / 2, 24);
                final Polygon var30 = new Polygon();
                final short var36 = 400;
                var42 = 51;
                pox = var36 - (int) (Medium.sin(var21 - 180) * 19.0F);
                var33 = var42 - (int) (Medium.cos(var21 - 180) * 19.0F);
                var30.addPoint(pox, var33);
                pox = var36 - (int) (Medium.sin(var21 + 90) * 16.0F);
                var33 = var42 - (int) (Medium.cos(var21 + 90) * 16.0F);
                var30.addPoint(pox, var33);
                pox = var36 - (int) (Medium.sin(var21 + 90) * 5.0F);
                var33 = var42 - (int) (Medium.cos(var21 + 90) * 5.0F);
                var30.addPoint(pox, var33);
                pox = var36 - (int) (Medium.sin(var21 + 24) * 20.0F);
                var33 = var42 - (int) (Medium.cos(var21 + 24) * 20.0F);
                var30.addPoint(pox, var33);
                pox = var36 - (int) (Medium.sin(var21 - 24) * 20.0F);
                var33 = var42 - (int) (Medium.cos(var21 - 24) * 20.0F);
                var30.addPoint(pox, var33);
                pox = var36 - (int) (Medium.sin(var21 - 90) * 5.0F);
                var33 = var42 - (int) (Medium.cos(var21 - 90) * 5.0F);
                var30.addPoint(pox, var33);
                pox = var36 - (int) (Medium.sin(var21 - 90) * 16.0F);
                var33 = var42 - (int) (Medium.cos(var21 - 90) * 16.0F);
                var30.addPoint(pox, var33);
                rd.setColor(ModUtility.colorSnap(50, 50, 50, 220));
                rd.translate(0, 5);
                rd.fillPolygon(var30);
                rd.translate(0, -5);
                if (arrace) {
                    if (!RMedium.darksky() && !RMedium.lightson()) {
                        rd.setColor(ModUtility.colorSnap(50, 204, 255));
                    } else {
                        rd.setColor(new Color(50, 204, 255));
                    }
                } else if (!RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(80, 230, 30));
                } else {
                    rd.setColor(new Color(80, 230, 30));
                }

                rd.fillPolygon(var30);
                if (arrace) {
                    if (!RMedium.darksky() && !RMedium.lightson()) {
                        rd.setColor(ModUtility.colorSnap(0, 154, 205, 220));
                    } else {
                        rd.setColor(new Color(0, 154, 205, 220));
                    }
                } else if (!RMedium.darksky() && !RMedium.lightson()) {
                    rd.setColor(ModUtility.colorSnap(50, 150, 20));
                } else {
                    rd.setColor(new Color(50, 150, 20));
                }

                final Stroke var39 = rd.getStroke();
                rd.setStroke(new BasicStroke(2.0F));
                rd.drawPolygon(var30);
                rd.setStroke(var39);
                rd.setFont(new Font("Arial", 1, 11));
                xtGraphics.ftm = rd.getFontMetrics();
            }

            rd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        } else {
            final int[] i_202_ = new int[7];
            final int[] i_204_ = new int[7];
            final int[] base = new int[7];
            dist = 400;
            final byte str = -90;
            final short pol = 700;

            int px;
            for (px = 0; px < 7; ++px) {
                i_204_[px] = str;
            }

            i_202_[0] = dist;
            base[0] = pol + 110;
            i_202_[1] = dist - 35;
            base[1] = pol + 50;
            i_202_[2] = dist - 15;
            base[2] = pol + 50;
            i_202_[3] = dist - 15;
            base[3] = pol - 50;
            i_202_[4] = dist + 15;
            base[4] = pol - 50;
            i_202_[5] = dist + 15;
            base[5] = pol + 50;
            i_202_[6] = dist + 35;
            base[6] = pol + 50;
            int py;
            if (!arrace) {
                short var31 = 0;
                if (RCheckPoints.x(point) - RCheckPoints.opx(RxtGraphics.im()) >= 0) {
                    var31 = 180;
                }

                px = (int) (90 + var31 + Math.atan((double) (RCheckPoints.z(point) - RCheckPoints.opz(RxtGraphics.im())) / (double) (RCheckPoints.x(point) - RCheckPoints.opx(RxtGraphics.im()))) / 0.017453292519943295D);
            } else {
                py = 0;
                if (RxtGraphics.multion() != 0 && xtGraphics.alocked != -1) {
                    py = RxtGraphics.alocked();
                } else {
                    pox = -1;
                    boolean poy = false;

                    for (tmp = 0; tmp < TData.nplayers; ++tmp) {
                        if (tmp != RxtGraphics.im() && (ModUtility.py(RCheckPoints.opx(RxtGraphics.im()) / 100, RCheckPoints.opx(tmp) / 100, RCheckPoints.opz(RxtGraphics.im()) / 100, RCheckPoints.opz(tmp) / 100) < pox || pox == -1) && (!poy || RCheckPoints.onscreen(tmp) != 0) && RCheckPoints.dested(tmp) == 0) {
                            py = tmp;
                            pox = ModUtility.py(RCheckPoints.opx(RxtGraphics.im()) / 100, RCheckPoints.opx(tmp) / 100, RCheckPoints.opz(RxtGraphics.im()) / 100, RCheckPoints.opz(tmp) / 100);
                            if (RCheckPoints.onscreen(tmp) != 0) {
                                poy = true;
                            }
                        }
                    }
                }

                short var32 = 0;
                if (RCheckPoints.opx(py) - RCheckPoints.opx(RxtGraphics.im()) >= 0) {
                    var32 = 180;
                }

                px = (int) (90 + var32 + Math.atan((double) (RCheckPoints.opz(py) - RCheckPoints.opz(RxtGraphics.im())) / (double) (RCheckPoints.opx(py) - RCheckPoints.opx(RxtGraphics.im()))) / 0.017453292519943295D);
                if (RxtGraphics.multion() == 0) {
                    if (GadgetsInit.arrowon == 0) {
                        ModUtility.drawCenteredString(13, "[                                ]", 76, 67, 240, 0);
                    } else {
                        ModUtility.drawCenteredString(13, "[                                ]", 30, 70, 150, 0);
                    }

                    ModUtility.drawCenteredString(13, RCarDefine.names(RxtGraphics.sc(py)), 0, 0, 0, 0);
                } else {
                    rd.setFont(new Font("Arial", 1, 12));
                    xtGraphics.ftm = rd.getFontMetrics();
                    if (GadgetsInit.arrowon == 0) {
                        ModUtility.drawCenteredString(17, "[                                ]", 76, 67, 240, 0);
                    } else {
                        ModUtility.drawCenteredString(17, "[                                ]", 30, 70, 150, 0);
                    }

                    ModUtility.drawCenteredString(12, RxtGraphics.plnames(py), 0, 0, 0, 0);
                    rd.setFont(new Font("Arial", 0, 10));
                    xtGraphics.ftm = rd.getFontMetrics();
                    ModUtility.drawCenteredString(24, RCarDefine.names(RxtGraphics.sc(py)), 0, 0, 0, 0);
                    rd.setFont(new Font("Arial", 1, 11));
                    xtGraphics.ftm = rd.getFontMetrics();
                }
            }

            for (px += RMedium.xz(); px < 0; px += 360) {

            }

            while (px > 180) {
                px -= 360;
            }

            if (!arrace) {
                if (px > 130) {
                    px = 130;
                }

                if (px < -130) {
                    px = -130;
                }
            } else {
                if (px > 100) {
                    px = 100;
                }

                if (px < -100) {
                    px = -100;
                }
            }

            if (Math.abs(RadicalHUD.ana - px) < 180) {
                if (Math.abs(RadicalHUD.ana - px) < 10) {
                    RadicalHUD.ana = px;
                } else if (RadicalHUD.ana < px) {
                    RadicalHUD.ana += 10;
                } else {
                    RadicalHUD.ana -= 10;
                }
            } else {
                if (px < 0) {
                    RadicalHUD.ana += 15;
                    if (RadicalHUD.ana > 180) {
                        RadicalHUD.ana -= 360;
                    }
                }

                if (px > 0) {
                    RadicalHUD.ana -= 15;
                    if (RadicalHUD.ana < -180) {
                        RadicalHUD.ana += 360;
                    }
                }
            }

            ModUtility.rot(i_202_, base, dist, pol, RadicalHUD.ana, 7);
            px = Math.abs(RadicalHUD.ana);
            if (!arrace) {
                if (px > 7 || missedcp > 0 || missedcp == -2 || xtGraphics.cntan != 0 || GadgetsInit.arrowon == 1) {
                    for (py = 0; py < 7; ++py) {
                        i_202_[py] = ModUtility.xs(i_202_[py], base[py]);
                        i_204_[py] = ModUtility.ys(i_204_[py], base[py]);
                    }

                    if (GadgetsInit.arrowon == 0) {
                        py = (int) (190.0F + 190.0F * (RMedium.snap(0) / 100.0F));
                        if (py > 255) {
                            py = 255;
                        }

                        if (py < 0) {
                            py = 0;
                        }

                        pox = (int) (255.0F + 255.0F * (RMedium.snap(1) / 100.0F));
                        if (pox > 255) {
                            pox = 255;
                        }

                        if (pox < 0) {
                            pox = 0;
                        }

                        var33 = 0;
                        if (missedcp <= 0) {
                            if (px <= 45 && missedcp != -2 && RxtGraphics.cntan() == 0) {
                                py = (py * px + RMedium.csky(0) * (45 - px)) / 45;
                                pox = (pox * px + RMedium.csky(1) * (45 - px)) / 45;
                                var33 = (var33 * px + RMedium.csky(2) * (45 - px)) / 45;
                            }

                            if (px >= 90) {
                                tmp = (int) (255.0F + 255.0F * (RMedium.snap(0) / 100.0F));
                                if (tmp > 255) {
                                    tmp = 255;
                                }

                                if (tmp < 0) {
                                    tmp = 0;
                                }

                                py = (py * (140 - px) + tmp * (px - 90)) / 50;
                                if (py > 255) {
                                    py = 255;
                                }
                            }
                        } else if (xtGraphics.flk) {
                            py = (int) (255.0F + 255.0F * (RMedium.snap(0) / 100.0F));
                            if (py > 255) {
                                py = 255;
                            }

                            if (py < 0) {
                                py = 0;
                            }

                            xtGraphics.flk = false;
                        } else {
                            py = (int) (255.0F + 255.0F * (RMedium.snap(0) / 100.0F));
                            if (py > 255) {
                                py = 255;
                            }

                            if (py < 0) {
                                py = 0;
                            }

                            pox = (int) (220.0F + 220.0F * (RMedium.snap(1) / 100.0F));
                            if (pox > 255) {
                                pox = 255;
                            }

                            if (pox < 0) {
                                pox = 0;
                            }

                            xtGraphics.flk = true;
                        }

                        rd.setColor(new Color(py, pox, var33));
                    } else if (missedcp <= 0) {
                        if (px <= 45 && missedcp != -2 && RxtGraphics.cntan() == 0) {
                            rd.setColor(ModUtility.colorSnap(190, 255, 0, (int) (255.0F * (45.0F - px) / 45.0F)));
                        } else if (px >= 90) {
                            py = (int) (255.0F + 255.0F * (RMedium.snap(0) / 100.0F));
                            if (py > 255) {
                                py = 255;
                            }

                            if (py < 0) {
                                py = 0;
                            }

                            rd.setColor(ModUtility.colorSnap((190 * (140 - px) + py * (px - 90)) / 50, 255, 0, 200));
                        } else {
                            rd.setColor(ModUtility.colorSnap(190, 255, 0, 200));
                        }
                    } else if (xtGraphics.flk) {
                        rd.setColor(ModUtility.colorSnap(255, 255, 0));
                        xtGraphics.flk = false;
                    } else {
                        rd.setColor(ModUtility.colorSnap(255, 220, 0));
                        xtGraphics.flk = true;
                    }

                    rd.fillPolygon(i_202_, i_204_, 7);
                    if (GadgetsInit.arrowon == 0) {
                        py = (int) (115.0F + 115.0F * (RMedium.snap(0) / 100.0F));
                        if (py > 255) {
                            py = 255;
                        }

                        if (py < 0) {
                            py = 0;
                        }

                        pox = (int) (170.0F + 170.0F * (RMedium.snap(1) / 100.0F));
                        if (pox > 255) {
                            pox = 255;
                        }

                        if (pox < 0) {
                            pox = 0;
                        }

                        var33 = 0;
                        if (missedcp <= 0) {
                            if (px <= 45 && missedcp != -2 && RxtGraphics.cntan() == 0) {
                                py = (py * px + RMedium.csky(0) * (45 - px)) / 45;
                                pox = (pox * px + RMedium.csky(1) * (45 - px)) / 45;
                                var33 = (var33 * px + RMedium.csky(2) * (45 - px)) / 45;
                            }
                        } else if (xtGraphics.flk) {
                            py = (int) (255.0F + 255.0F * (RMedium.snap(0) / 100.0F));
                            if (py > 255) {
                                py = 255;
                            }

                            if (py < 0) {
                                py = 0;
                            }

                            pox = 0;
                        }

                        rd.setColor(new Color(py, pox, var33));
                    } else if (missedcp <= 0) {
                        if (px <= 45 && missedcp != -2 && RxtGraphics.cntan() == 0) {
                            rd.setColor(ModUtility.colorSnap(115, 170, 0, (int) (255.0F * (45.0F - px) / 45.0F)));
                        } else {
                            rd.setColor(ModUtility.colorSnap(115, 170, 0));
                        }
                    } else if (xtGraphics.flk) {
                        rd.setColor(ModUtility.colorSnap(255, 0, 0));
                    } else {
                        rd.setColor(ModUtility.colorSnap(115, 170, 0));
                    }

                    rd.drawPolygon(i_202_, i_204_, 7);
                }
            } else {
                var42 = 0;
                if (RxtGraphics.multion() != 0) {
                    var42 = 8;
                }

                for (pox = 0; pox < 7; ++pox) {
                    i_202_[pox] = ModUtility.xs(i_202_[pox], base[pox]);
                    i_204_[pox] = ModUtility.ys(i_204_[pox], base[pox]) + var42;
                }

                if (GadgetsInit.arrowon == 0) {
                    rd.setColor(ModUtility.colorSnap(159, 207, 255));
                } else {
                    rd.setColor(ModUtility.colorSnap(130, 170, 250, 180));
                }

                rd.fillPolygon(i_202_, i_204_, 7);
                if (GadgetsInit.arrowon == 0) {
                    rd.setColor(ModUtility.colorSnap(120, 114, 255));
                } else {
                    rd.setColor(ModUtility.colorSnap(30, 70, 150));
                }

                rd.drawPolygon(i_202_, i_204_, 7);
            }
        }

    }

    private static Area getBFpow() {
        if (BF_pow == null) {
            final Area elipse = new Area(new Ellipse2D.Float(597.0F, 5.0F, 70.0F, 70.0F));
            elipse.add(new Area(new Rectangle2D.Float(597.0F, 40.0F, 70.0F, 17.0F)));
            elipse.subtract(new Area(new Rectangle2D.Float(597.0F, 56.0F, 70.0F, 20.0F)));
            elipse.add(new Area(new RoundRectangle2D.Float(690.0F, 6.0F, 94.0F, 50.0F, 15.0F, 15.0F)));
            elipse.add(new Area(new Arc2D.Float(643.0F, 6.0F, 100.0F, 100.0F, 0.0F, 180.0F, 1)));
            elipse.subtract(new Area(new Ellipse2D.Float(604.0F, 12.0F, 56.0F, 56.0F)));
            elipse.add(new Area(new Ellipse2D.Float(619.0F, 27.0F, 26.0F, 26.0F)));
            elipse.add(new Area(new Polygon(new int[] {
                    597, 631, 632, 666
            }, new int[] {
                    56, 39, 39, 56
            }, 4)));
            elipse.subtract(new Area(new Rectangle2D.Float(597.0F, 56.0F, 70.0F, 20.0F)));
            BF_pow = elipse;
        }

        return BF_pow;
    }
}
