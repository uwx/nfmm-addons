package moddercrib;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.trashers.nfmmaddons.apinext.APIData;
import com.trashers.nfmmaddons.apinext.Game;
import com.trashers.nfmmaddons.apinext.ModIO.ModResource;
import com.trashers.nfmmaddons.apinext.ModUtility;
import com.trashers.nfmmaddons.apinext.builtin.RadicalHUD;
import com.trashers.nfmmaddons.apinext.plugin.HUD;
import com.trashers.nfmmaddons.apinext.plugin.Plugin;
import com.trashers.nfmmaddons.apinext.readonly.RMad;
import com.trashers.nfmmaddons.apinext.readonly.RxtGraphics;
import com.trashers.nfmmaddons.game.SavedData;
import com.trashers.nfmmaddons.game.SavedData.Setting;
import com.trashers.nfmmaddons.game.Settings;
import com.trashers.nfmmaddons.game.music.RadicalMod;

public class GadgetsInit extends Plugin implements HUD, KeyListener {

    static Graphics2D rd = (Graphics2D) APIData.rd.create();
    //    private static JCheckBoxMenuItem gadgetsCheckbox;
    //    private static boolean isEnabled = false;

    public static Font fontAdventure;
    public static Font fontDigital;
    public static Font fontYukari;

    public static int speedon = 0;
    public static boolean kph = true;
    public static int modon = 0;
    public static boolean hidemod = true;
    public static int poson = 0;
    public static int staton = 0;
    public static int arrowon = 0;
    // subclassing ftm is ill-advised; instead, overwrite it in xtgraphics
    //public static FontMetrics ftm;

    public GadgetsInit() {
        // Load fonts from resources
        try {
            fontAdventure = Font.createFont(Font.TRUETYPE_FONT, ModResource.getResource(GadgetsInit.class, "adventure.ttf"));
            fontDigital = Font.createFont(Font.TRUETYPE_FONT, ModResource.getResource(GadgetsInit.class, "digital-7.ttf"));
            fontYukari = Font.createFont(Font.TRUETYPE_FONT, ModResource.getResource(GadgetsInit.class, "yukari.ttf"));
        } catch (FontFormatException | IOException | URISyntaxException e1) {
            e1.printStackTrace();
        }

        // add the KeyListener so we can handle key presses
        Game.addKeyListener(this);

        Game.registerHud(this);

        // register our tickbox
        Settings.invokeLater(new Settings.Callback() {
            @Override
            public void callback() {
                //                gadgetsCheckbox = Settings.addCheckboxMenuItem("DS-Addons Gadgets", new ItemListener() {
                //                    @Override
                //                    public void itemStateChanged(ItemEvent e) {
                //                        isEnabled = e.getStateChange() == ItemEvent.SELECTED;
                //                    }
                //                });
                //                if (gadgetsCheckbox.isSelected() != isEnabled) { // refer to paranoid safety check below
                //                    gadgetsCheckbox.setSelected(isEnabled);
                //                }
                initMenu();
            }
        });

        SavedData.registerEntry("DS-Addons--speedon", this, new SavedData.Callback() {
            @Override
            public Setting saveSetting() {
                return new Setting(speedon);
            }

            @Override
            public void loadSetting(final Setting setting) {
                speedon = setting.getAsInt();
            }
        });
        SavedData.registerEntry("DS-Addons--kph", this, new SavedData.Callback() {
            @Override
            public Setting saveSetting() {
                return new Setting(kph);
            }

            @Override
            public void loadSetting(final Setting setting) {
                kph = setting.getAsBoolean();
            }
        });
        SavedData.registerEntry("DS-Addons--modon", this, new SavedData.Callback() {
            @Override
            public Setting saveSetting() {
                return new Setting(modon);
            }

            @Override
            public void loadSetting(final Setting setting) {
                modon = setting.getAsInt();
            }
        });
        SavedData.registerEntry("DS-Addons--hidemod", this, new SavedData.Callback() {
            @Override
            public Setting saveSetting() {
                return new Setting(hidemod);
            }

            @Override
            public void loadSetting(final Setting setting) {
                hidemod = setting.getAsBoolean();
            }
        });
        SavedData.registerEntry("DS-Addons--poson", this, new SavedData.Callback() {
            @Override
            public Setting saveSetting() {
                return new Setting(poson);
            }

            @Override
            public void loadSetting(final Setting setting) {
                poson = setting.getAsInt();
            }
        });
        SavedData.registerEntry("DS-Addons--staton", this, new SavedData.Callback() {
            @Override
            public Setting saveSetting() {
                return new Setting(staton);
            }

            @Override
            public void loadSetting(final Setting setting) {
                staton = setting.getAsInt();
            }
        });
        SavedData.registerEntry("DS-Addons--arrowon", this, new SavedData.Callback() {
            @Override
            public Setting saveSetting() {
                return new Setting(arrowon);
            }

            @Override
            public void loadSetting(final Setting setting) {
                arrowon = setting.getAsInt();
            }
        });

        // set some options for our Graphics2D object
        rd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void initMenu() {
        final JMenu men = new JMenu("Gadgets settings");
        final JMenu menabout = new JMenu("NfmM DS-addons pack 2.2 ~ DragShot Software / RadicalPlay.com");
        JMenu mens = new JMenu("Set Skin");
        JMenuItem mit = new JMenuItem("Default (add-ons off)");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                speedon = 0;
                modon = 0;
                //chronon = 0;
                poson = 0;
                arrowon = 0;
                staton = 0;
                postMessage("Now using Default skin");
            }
        });
        mens.add(mit);
        mit = new JMenuItem("DSmod2 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                speedon = 1;
                modon = 1;
                //chronon = 1;
                poson = 1;
                arrowon = 1;
                staton = 1;
                postMessage("Now using DSmod 2 skin");
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Black Fusion skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                speedon = 2;
                modon = 2;
                //chronon = 2;
                poson = 2;
                arrowon = 2;
                staton = 2;
                postMessage("Now using Black Fusion skin");
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Digtal 7 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                speedon = 3;
                modon = 3;
                //chronon = 3;
                poson = 3;
                arrowon = 3;
                staton = 3;
                postMessage("Now using Digital 7 skin");
            }
        });
        mens.add(mit);
        mit = new JMenuItem("S7 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                speedon = 4;
                modon = 4;
                //chronon = 4;
                poson = 4;
                arrowon = 4;
                staton = 4;
                postMessage("Now using S7 skin");
            }
        });
        mens.add(mit);
        men.add(mens);
        mens = new JMenu("Speedometer");
        mit = new JMenuItem("Do not show speedometer");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                speedon = 0;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("DSmod2 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                speedon = 1;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Black Fusion skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                speedon = 2;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Digtal 7 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                speedon = 3;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("S7 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                speedon = 4;
            }
        });
        mens.add(mit);
        final JMenuItem mitkph = new JMenuItem(kph ? "Switch to MPH" : "Switch to KPH");
        mitkph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                kph = !kph;
                if (kph) {
                    mitkph.setText("Switch to MPH");
                    if (speedon != 0) {
                        postMessage("Showing KPH");
                    }
                } else {
                    mitkph.setText("Switch to KPH");
                    if (speedon != 0) {
                        postMessage("Showing MPH");
                    }
                }

            }
        });
        mens.add(mitkph);
        men.add(mens);
        mens = new JMenu("Track visor");
        mit = new JMenuItem("Do not show Track visor");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                modon = 0;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("DSmod2 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                modon = 1;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Black Fusion skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                modon = 2;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Digtal 7 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                modon = 3;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("S7 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                modon = 4;
            }
        });
        mens.add(mit);
        final JMenuItem mitmod = new JMenuItem(hidemod ? "Set to always visible" : "Set to hide automatically");
        mitmod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                hidemod = !hidemod;
                if (hidemod) {
                    mitmod.setText("Set to always visible");
                } else {
                    mitmod.setText("Set to hide automatically");
                    GadgetPainter.modc = 150;
                }

            }
        });
        mens.add(mitmod);
        men.add(mens);

        mens = new JMenu("Opponent status");
        mit = new JMenuItem("Classic skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                arrowon = 0;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("DSmod2 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                arrowon = 1;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Black Fusion skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                arrowon = 2;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Digtal 7 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                arrowon = 3;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("S7 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                arrowon = 4;
            }
        });
        mens.add(mit);
        men.add(mens);
        mens = new JMenu("Damage/Power");
        mit = new JMenuItem("Classic skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                staton = 0;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("DSmod2 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                staton = 1;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Black Fusion skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                staton = 2;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Digtal 7 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                staton = 3;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("S7 skin");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                staton = 4;
            }
        });
        mens.add(mit);
        men.add(mens);
        mens = new JMenu("Skin color");
        mit = new JMenuItem("Turquoise (Default)");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                GadgetPainter.ci = 0;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Light Blue");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                GadgetPainter.ci = 1;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Green");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                GadgetPainter.ci = 2;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Yellow");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                GadgetPainter.ci = 3;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Orange");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                GadgetPainter.ci = 4;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Red");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                GadgetPainter.ci = 5;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Purple");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                GadgetPainter.ci = 6;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("Pink");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                GadgetPainter.ci = 7;
            }
        });
        mens.add(mit);
        mit = new JMenuItem("White");
        mit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                GadgetPainter.ci = 8;
            }
        });
        mens.add(mit);
        men.add(mens);

        Settings.appendMenu(men);
        Settings.appendMenu(mens);
        Settings.appendMenu(menabout);
    }

    protected void postMessage(final String string) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(final KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(final KeyEvent e) {
        final int i = e.getKeyCode();
        //        if(i == 102 || i == 70) {
        //           this.u[0].chron = true;
        //        }

        if (i == 115 || i == 83) {
            ++speedon;
            if (speedon > 4) {
                speedon = 0;
            }

        }

        if (i == 100 || i == 68) {
            ++modon;
            GadgetPainter.modc = 150;
            if (modon > 4) {
                modon = 0;
            }
        }

        if (i == 119 || i == 87) {
            ++poson;
            if (poson > 4) {
                poson = 0;
            }

        }

        if (i == 101 || i == 69) {
            ++arrowon;
            if (arrowon > 4) {
                arrowon = 0;
            }
        }

        if (i == 114 || i == 82) {
            ++staton;
            if (staton > 4) {
                staton = 0;
            }
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void nfmmHud(final Graphics2D rd) {
        RadicalHUD.DefaultHUD.switchArrow();

        GadgetPainter.position();
        GadgetPainter.status();
        if (!RxtGraphics.holdit() && speedon > 0) {
            GadgetPainter.speedometer();
        }

        if (!RxtGraphics.holdit() && RadicalMod.nonempty && !RxtGraphics.mutem() && modon > 0) {
            GadgetPainter.modTrackVisor(ModUtility.xm(), ModUtility.ym());
        }

        GadgetPainter.arrow(RMad.point(), RMad.missedcp(), RadicalHUD.arrace);
    }

}
