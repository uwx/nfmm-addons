package moddercrib;

import static com.trashers.nfmmaddons.apinext.APIData.criticalB;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;

import com.trashers.nfmmaddons.apinext.APIData;
import com.trashers.nfmmaddons.apinext.Game;
import com.trashers.nfmmaddons.apinext.plugin.HUDComponent;
import com.trashers.nfmmaddons.apinext.plugin.Plugin;
import com.trashers.nfmmaddons.apinext.plugin.RaceListener;
import com.trashers.nfmmaddons.apinext.readonly.RCheckPoints;
import com.trashers.nfmmaddons.apinext.readonly.RContO;
import com.trashers.nfmmaddons.apinext.readonly.RContOCopy;
import com.trashers.nfmmaddons.apinext.readonly.RControl;
import com.trashers.nfmmaddons.apinext.readonly.RMedium;
import com.trashers.nfmmaddons.apinext.readonly.RxtGraphics;
import com.trashers.nfmmaddons.game.ContO;
import com.trashers.nfmmaddons.game.Medium;
import com.trashers.nfmmaddons.game.SavedData;
import com.trashers.nfmmaddons.game.SavedData.Setting;
import com.trashers.nfmmaddons.game.Settings;

/**
 * @author ApexNova
 * @author API port by rafa
 */
@SuppressWarnings("unused")
public class ApexRadar extends Plugin implements HUDComponent, KeyListener, RaceListener {

    // a sub-Graphics object of API.rd which is the same as xtGraphics.rd.
    // you can modify this Graphics object freely without affecting the original one
    private final Graphics2D g = (Graphics2D) APIData.rd.create();

    private static BufferedImage map;

    private static GeneralPath poly;

    // the positions of the walls. i have not made getters for this yet but it being mutable is not dangerous so ehhh
    //private static int criticalB[] = criticalB;

    private static double pointX[] = new double[100];
    private static double pointZ[] = new double[100];
    private static double rate = 1;
    private static double ratio = 1;
    private static double zoom;

    private boolean zoomin;
    private boolean zoomout;

    private static JCheckBoxMenuItem radarCheckbox;

    private static boolean isEnabled = false;

    public ApexRadar() {
        // add the KeyListener so we can handle key presses
        Game.addKeyListener(this);
        // add the RaceListener so the minimap loads when a stage is loaded
        Game.addRaceListener(this);

        Game.registerHudComponent(this);

        // register our tickbox
        Settings.invokeLater(new Settings.Callback() {
            @Override
            public void callback() {
                radarCheckbox = Settings.addCheckboxMenuItem("ApexNova Radar", new ItemListener() {
                    @Override
                    public void itemStateChanged(final ItemEvent e) {
                        isEnabled = e.getStateChange() == ItemEvent.SELECTED;
                    }
                });
                if (radarCheckbox.isSelected() != isEnabled) { // refer to paranoid safety check below
                    radarCheckbox.setSelected(isEnabled);
                }
            }
        });

        // register the entry for saving the tickbox status
        SavedData.registerEntry("ApexNova Radar Enabled", this, new SavedData.Callback() {

            @Override
            public Setting saveSetting() {
                return new Setting(isEnabled);
            }

            @Override
            public void loadSetting(final Setting setting) {
                //System.err.println("got loaded setting");
                final boolean b = setting.getAsBoolean();
                if (isEnabled != b) {
                    isEnabled = b;
                    if (radarCheckbox != null) { // for paranoid safety
                        radarCheckbox.setSelected(b);
                    }
                }
            }
        });

        // set some options for our Graphics2D object
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setClip(new Rectangle2D.Double(10, 55, 172, 172));
    }

    @Override
    public void paint(final Graphics2D ignored) {
        RControl.set_radar(false);

        // handle zooming
        if (zoomin) {
            zoom += 0.02;
        }
        if (zoomout) {
            zoom -= 0.02;
        }

        if (zoom > 1.0) {
            zoom = 1;
        }
        if (zoom < 0.05) {
            zoom = 0.05;
        }

        // paint the background
        //127 is the alpha here. it should be used instead of the Composite methods since they introduce some overhead.
        g.setColor(new Color(RMedium.csky(0), RMedium.csky(1), RMedium.csky(2), 127));

        g.fillRect(10, 55, 172, 172);

        g.setColor(RMedium.csky());

        // rotate the radar according to the player's angle
        g.rotate(RContO.xz() * 0.017453292519943295D, 96, 141);
        rate /= zoom;
        // draw the map
        g.drawImage(map, (int) ((RContO.x() - criticalB[1]) / -rate) + 96, (int) ((RContO.z() - criticalB[2]) / rate) + 141, (int) (map.getWidth() * zoom), (int) (map.getHeight() * zoom), null);
        // draw the player blips
        for (byte pN = 0; pN < RxtGraphics.nplayers(); pN++) {
            if (pN == 0) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.BLACK);
            }
            if (RCheckPoints.magperc(pN) < 1f) {
                pointX[0] = (RContO.x(pN) - RContO.x()) / rate + 96 + 4 * Medium.sin(-RContO.xz(pN));
                pointX[1] = (RContO.x(pN) - RContO.x()) / rate + 96 + 2.828427124746190097 * Medium.sin(135 - RContO.xz(pN));
                pointX[2] = (RContO.x(pN) - RContO.x()) / rate + 96 + 2.828427124746190097 * Medium.sin(-RContO.xz(pN) - 135);
                pointZ[0] = (RContO.z() - RContO.z(pN)) / rate + 141 - 4 * Medium.cos(-RContO.xz(pN));
                pointZ[1] = (RContO.z() - RContO.z(pN)) / rate + 141 - 2.828427124746190097 * Medium.cos(135 - RContO.xz(pN));
                pointZ[2] = (RContO.z() - RContO.z(pN)) / rate + 141 - 2.828427124746190097 * Medium.cos(-RContO.xz(pN) - 135);
                poly = new GeneralPath(0, 3);
                poly.moveTo(pointX[0], pointZ[0]);
                for (int B = 1; B < 3; B++) {
                    poly.lineTo(pointX[B], pointZ[B]);
                }
                poly.closePath();
                g.fill(poly);
                if (pN == 0) {
                    g.setColor(Color.BLACK);
                    g.draw(poly);
                }
            }
        }
        g.drawRect((int) ((RContO.x() - criticalB[1]) / -rate) + 96, (int) ((RContO.z() - criticalB[2]) / rate) + 141, (int) (map.getWidth() * zoom), (int) (map.getHeight() * zoom));
        rate *= zoom;

        // rotate the radar back so we can use our Graphics2D again later
        g.rotate(-RContO.xz() * 0.017453292519943295D, 96, 141);
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    private static void loadMiniMap(final ContO[] co, final int numContos) {
        // this method loads the minimap. can't give you any info about this one, it's all apex's work and i understand none of it.

        final List<Integer> list = new ArrayList<>();
        int[][] tpolys;
        int a, b, c, d, yMod, polys;
        polys = 0;
        for (b = RxtGraphics.nplayers(); b < numContos; b++) {
            polys += co[b].npl;
        }

        tpolys = new int[polys][4];
        ratio = (criticalB[2] - criticalB[3]) / (double) (criticalB[0] - criticalB[1]);
        d = 0;
        for (a = RxtGraphics.nplayers(); a < numContos; a++) {
            if (co[a].elec) {
                yMod = co[a].y;
            } else {
                yMod = 0;
            }
            for (b = 0; b < co[a].npl; b++) {
                tpolys[d][0] = a;
                tpolys[d][1] = b;
                for (c = 0; c < co[a].p[b].n; c++) {
                    if (c == 0) {
                        tpolys[d][2] = co[a].p[b].oy[0] + yMod;
                        tpolys[d][3] = co[a].p[b].oy[0] + yMod;
                    }
                    if (tpolys[d][2] > co[a].p[b].oy[c] + yMod) {
                        tpolys[d][2] = co[a].p[b].oy[c] + yMod;
                    }
                    if (tpolys[d][3] < co[a].p[b].oy[c] + yMod) {
                        tpolys[d][3] = co[a].p[b].oy[c] + yMod;
                    }
                }
                d++;
            }
        }

        Arrays.sort(tpolys, new Comparator<int[]>() {
            @Override
            public int compare(final int[] entry1, final int[] entry2) {
                return Integer.compare(entry2[2], entry1[2]);
            }
        });

        b = 0;
        for (a = 0; a < polys; a++) {
            if (a == 0) {
                list.add(0);
                b = tpolys[0][2];
            }
            if (tpolys[a][2] != b) {
                list.add(a);
                b = tpolys[a][2];
            }
        }
        list.add(polys);

        for (a = 0; a < list.size() - 1; a++) {
            Arrays.sort(tpolys, list.get(a), list.get(a + 1), new Comparator<int[]>() {
                @Override
                public int compare(final int[] entry1, final int[] entry2) {
                    return Integer.compare(entry2[3], entry1[3]);
                }
            });
        }

        if (ratio > 1) {
            map = new BufferedImage((int) (4000 / ratio), 4000, 2);
            rate = (criticalB[2] - criticalB[3]) / 4000D;
        } else {
            map = new BufferedImage(4000, (int) (4000 * ratio), 2);
            rate = (criticalB[0] - criticalB[1]) / 4000D;
        }

        final Graphics2D g = map.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        for (a = 0; a < polys; a++) {
            for (b = 0; b < co[tpolys[a][0]].p[tpolys[a][1]].n; b++) {
                pointX[b] = (co[tpolys[a][0]].p[tpolys[a][1]].ox[b] + co[tpolys[a][0]].x - criticalB[1]) / rate;
                pointZ[b] = (co[tpolys[a][0]].p[tpolys[a][1]].oz[b] + co[tpolys[a][0]].z - criticalB[2]) / -rate;
            }

            g.setColor(new Color(co[tpolys[a][0]].p[tpolys[a][1]].c[0], co[tpolys[a][0]].p[tpolys[a][1]].c[1], co[tpolys[a][0]].p[tpolys[a][1]].c[2]));
            poly = new GeneralPath(0, co[tpolys[a][0]].p[tpolys[a][1]].n);
            poly.moveTo(pointX[0], pointZ[0]);
            for (c = 1; c < co[tpolys[a][0]].p[tpolys[a][1]].n; c++) {
                poly.lineTo(pointX[c], pointZ[c]);
            }

            poly.closePath();
            g.fill(poly);
        }

        g.dispose();

        zoom = 0.2;
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        // those who have modded nfm before might be somewhat familiar with this method,
        // however, there is one difference: you don't have to deal with any of those ol' silly ASCII numbers!
        // KeyEvent provides a number of virtual key codes. These are slightly different than the ASCII key codes.
        // The differences are detailed in the javadocs for KeyEvent.

        final int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_J) {
            zoomout = true;
        }
        if (keyCode == KeyEvent.VK_K) {
            zoomin = true;
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        final int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_J) {
            zoomout = false;
        }
        if (keyCode == KeyEvent.VK_K) {
            zoomin = false;
        }
    }

    @Override
    public void stageLoaded(final int stage, final String stageName) {
        loadMiniMap(RContOCopy.getStageContos(), RContOCopy.getStageContoAmount());
    }

    @Override
    public void raceStarted(final int stage, final String stageName) {
    }

    @Override
    public void raceEnded(final int stage, final String stageName, final boolean winner) {
    }

}
