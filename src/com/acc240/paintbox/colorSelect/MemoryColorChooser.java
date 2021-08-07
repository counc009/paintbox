package com.acc240.paintbox.colorSelect;

import com.acc240.paintbox.Operations;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class MemoryColorChooser extends JFrame {

    public static final int BORDER = 0;
    public static final int FILL = 1;
    public static final int BACKGROUND = 2;

    public static final DecimalFormat fmt = new DecimalFormat("0.##");

    private final MemoryColorChooserListener listener = new MemoryColorChooserListener(this);

    private PreviewPanel preview;
    private JButton ok, cancel;
    private final ArrayList<JButton> colorButtons = new ArrayList<>();

    private static Color[] baseColors;
    private static Color[] colors;

    //RGB Components
    private JSlider red, green, blue, alpha;
    private JTextField counterR, counterG, counterB, counterA;
    private JTextField html;

    private Color startColor;
    private Color color;

    private boolean changing = false;
    private boolean canceled = false;
    private boolean defaults = false;
    private boolean panelOpen;

    private int mode;
    private JComponent caller;

    public MemoryColorChooser(String title, Color initialColor, JFrame location, int mode, JComponent caller, boolean defaults) {
        this(title, initialColor, location, mode, caller);
        this.defaults = defaults;
    }

    public MemoryColorChooser(String title, Color initialColor, JFrame location, int mode, JComponent caller) {
        super(title);

        panelOpen = Operations.isPanelOpen();

        if (initialColor == null) {
            if (mode == FILL) {
                initialColor = Operations.properties.getFill();
            } else if (mode == BORDER) {
                initialColor = Operations.properties.getBorder();
            } else {
                initialColor = Operations.properties.getBackground();
            }
        }

        if (location != null) {
            setLocation(location.getLocationOnScreen());
        }

        Operations.panelOpened();
        color = initialColor;
        if (mode != BACKGROUND) {
            startColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        } else {
            startColor = new Color(color.getRed(), color.getGreen(), color.getBlue());
        }

        this.mode = mode;
        this.caller = caller;

        preview = new PreviewPanel();
        preview.setBackground(color);
        preview.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        preview.setPreferredSize(new Dimension(10, 10));

        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.X_AXIS));
        previewPanel.add(Box.createHorizontalGlue());
        previewPanel.add(preview);
        previewPanel.add(Box.createHorizontalGlue());

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        ok = new JButton("ok");
        listener.setOk(ok);
        ok.addActionListener(listener);
        buttons.add(ok);
        buttons.add(Box.createHorizontalStrut(20));

        cancel = new JButton("cancel");
        listener.setCancel(cancel);
        cancel.addActionListener(listener);
        buttons.add(cancel);

        JPanel slidersPanel = new JPanel();
        slidersPanel.setLayout(new BoxLayout(slidersPanel, BoxLayout.X_AXIS));

        JPanel namesPanel = new JPanel();
        namesPanel.setLayout(new BoxLayout(namesPanel, BoxLayout.Y_AXIS));
        JPanel sliders = new JPanel();
        sliders.setLayout(new BoxLayout(sliders, BoxLayout.Y_AXIS));
        JPanel numbers = new JPanel();
        numbers.setLayout(new BoxLayout(numbers, BoxLayout.Y_AXIS));

        if (Operations.properties.useRGB255()) {
            counterR = new JTextField("" + color.getRed());
            counterG = new JTextField("" + color.getGreen());
            counterB = new JTextField("" + color.getBlue());
            if (mode != BACKGROUND) {
                counterA = new JTextField("" + color.getAlpha());
            }
        } else {
            counterR = new JTextField("" + color.getRed() / 255.0);
            counterG = new JTextField("" + color.getGreen() / 255.0);
            counterB = new JTextField("" + color.getBlue() / 255.0);
            if (mode != BACKGROUND) {
                counterA = new JTextField("" + color.getAlpha() / 255.0);
            }
        }
        counterR.setPreferredSize(new Dimension(50, 10));
        counterG.setPreferredSize(new Dimension(50, 10));
        counterB.setPreferredSize(new Dimension(50, 10));
        if (mode != BACKGROUND) {
            counterA.setPreferredSize(new Dimension(50, 10));
        }

        counterR.addActionListener(listener);
        counterG.addActionListener(listener);
        counterB.addActionListener(listener);
        if (mode != BACKGROUND) {
            counterA.addActionListener(listener);
        }

        numbers.add(counterR);
        numbers.add(counterG);
        numbers.add(counterB);
        if (mode != BACKGROUND) {
            numbers.add(counterA);
        }

        listener.setR(counterR);
        listener.setG(counterG);
        listener.setB(counterB);
        if (mode != BACKGROUND) {
            listener.setA(counterA);
        }

        namesPanel.add(new JLabel("Red: "));
        red = new JSlider(0, 255, color.getRed());
        red.addChangeListener(listener);
        red.setMinorTickSpacing(5);
        sliders.add(red);

        namesPanel.add(new JLabel("Green: "));
        green = new JSlider(0, 255, color.getGreen());
        green.addChangeListener(listener);
        green.setMinorTickSpacing(5);
        sliders.add(green);

        namesPanel.add(new JLabel("Blue: "));
        blue = new JSlider(0, 255, color.getBlue());
        blue.addChangeListener(listener);
        blue.setMinorTickSpacing(5);
        sliders.add(blue);

        if (mode != BACKGROUND) {
            namesPanel.add(new JLabel("Alpha: "));
            alpha = new JSlider(0, 255, color.getAlpha());
            alpha.addChangeListener(listener);
            alpha.setMinorTickSpacing(5);
            sliders.add(alpha);
        }

        slidersPanel.add(namesPanel);
        slidersPanel.add(sliders);
        slidersPanel.add(numbers);

        JPanel htmlCode = new JPanel();
        htmlCode.setLayout(new BoxLayout(htmlCode, BoxLayout.X_AXIS));
        htmlCode.add(new JLabel("HTML Color: "));

        html = new JTextField(getHTML(color));
        listener.setHTML(html);
        html.addActionListener(listener);
        htmlCode.add(html);

        JPanel changePanel = new JPanel();
        changePanel.setLayout(new BoxLayout(changePanel, BoxLayout.Y_AXIS));
        changePanel.add(slidersPanel);
        changePanel.add(Box.createVerticalStrut(25));
        changePanel.add(htmlCode);
        changePanel.add(Box.createVerticalStrut(25));

        JTabbedPane presets = new JTabbedPane();
        for (int i = 0; i < Operations.properties.getColorPallet().size(); i++) {
            presets.add(getPresetPanel(i, this), getPalletName(Operations.properties.getColorPallet().get(i)));
        }

        JPanel recents = new JPanel();
        recents.setLayout(new GridLayout(2, 5, 5, 5));
        for (int i = 0; i < 10; i++) {
            JButton temp = new JButton();
            if (Operations.getRecentColors().size() > i) {
                temp.setBackground(Operations.getRecentColors().get(i));
                temp.addActionListener(listener);
                colorButtons.add(temp);
            } else {
                temp.setBackground(getBackground());
                temp.setEnabled(false);
            }
            recents.add(temp);
        }

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(presets);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(changePanel);
        topPanel.setPreferredSize(new Dimension(400, 100));

        JPanel midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        midPanel.add(new JLabel("Recently Use Colors"));
        midPanel.add(recents);

        JPanel finalPanel = new JPanel();
        finalPanel.setLayout(new BoxLayout(finalPanel, BoxLayout.Y_AXIS));
        finalPanel.add(topPanel);
        finalPanel.add(Box.createVerticalGlue());
        finalPanel.add(midPanel);
        finalPanel.add(Box.createVerticalGlue());
        finalPanel.add(previewPanel);
        finalPanel.add(Box.createVerticalGlue());
        finalPanel.add(buttons);
        finalPanel.setPreferredSize(new Dimension(430, 200));

        getContentPane().add(finalPanel);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(listener);
        pack();
        setVisible(true);
    }

    public Color getColor() {
        return color;
    }

    public void setDone() {
        if (!canceled) {
            Operations.addRecentColor(color);
        }
        if (!panelOpen) {
            Operations.panelClosed();
        }
        dispose();
    }

    public void reset() {
        color = startColor;
        update();
    }

    public void cancel() {
        reset();
        canceled = true;
        setColor(startColor);
        setDone();
    }

    public boolean changing() {
        return changing;
    }

    public void setColor(Color col) {
        color = col;
        preview.setBackground(color);

        changing = true;

        red.setValue(color.getRed());
        green.setValue(color.getGreen());
        blue.setValue(color.getBlue());
        if (mode != BACKGROUND) {
            alpha.setValue(color.getAlpha());
        }

        changing = false;

        if (Operations.properties.useRGB255()) {
            counterR.setText("" + color.getRed());
            counterG.setText("" + color.getGreen());
            counterB.setText("" + color.getBlue());
            if (mode != BACKGROUND) {
                counterA.setText("" + color.getAlpha());
            }
        } else {
            counterR.setText(fmt.format(color.getRed() / 255.0));
            counterG.setText(fmt.format(color.getGreen() / 255.0));
            counterB.setText(fmt.format(color.getBlue() / 255.0));
            if (mode != BACKGROUND) {
                counterA.setText(fmt.format(color.getAlpha() / 255.0));
            }
        }

        html.setText(getHTML(color));

        if (mode == BORDER) {
            if (!defaults) {
                Operations.setBorder(color);
                Operations.update();
                Operations.setChanged();
                if (!(caller instanceof JMenuItem)) {
                    caller.setBackground(col);
                }
            } else {
                Operations.properties.setBorder(color);
                if (!(caller instanceof JMenuItem)) {
                    caller.setBackground(col);
                }
            }
        } else if (mode == FILL) {
            if (!defaults) {
                Operations.setFill(color);
                Operations.update();
                Operations.setChanged();
                if (!(caller instanceof JMenuItem)) {
                    caller.setBackground(col);
                }
            } else {
                Operations.properties.setFill(color);
                if (!(caller instanceof JMenuItem)) {
                    caller.setBackground(col);
                }
            }
        } else {
            if (!defaults) {
                Operations.getCanvas().setBackground(color);
                Operations.setChanged();
            } else {
                Operations.properties.setBackground(color);
                if (!(caller instanceof JMenuItem)) {
                    caller.setBackground(col);
                }
            }
        }
    }

    public String getHTML(Color color) {
        String result = "";

        int fr = color.getRed() / 16;
        int sr = color.getRed() % 16;
        int fg = color.getGreen() / 16;
        int sg = color.getGreen() % 16;
        int fb = color.getBlue() / 16;
        int sb = color.getBlue() % 16;

        if (fr >= 10) {
            if (fr == 10) {
                result += "A";
            } else if (fr == 11) {
                result += "B";
            } else if (fr == 12) {
                result += "C";
            } else if (fr == 13) {
                result += "D";
            } else if (fr == 14) {
                result += "E";
            } else {
                result += "F";
            }
        } else {
            result += fr;
        }

        if (sr >= 10) {
            if (sr == 10) {
                result += "A";
            } else if (sr == 11) {
                result += "B";
            } else if (sr == 12) {
                result += "C";
            } else if (sr == 13) {
                result += "D";
            } else if (sr == 14) {
                result += "E";
            } else {
                result += "F";
            }
        } else {
            result += sr;
        }

        if (fg >= 10) {
            if (fg == 10) {
                result += "A";
            } else if (fg == 11) {
                result += "B";
            } else if (fg == 12) {
                result += "C";
            } else if (fg == 13) {
                result += "D";
            } else if (fg == 14) {
                result += "E";
            } else {
                result += "F";
            }
        } else {
            result += fg;
        }

        if (sg >= 10) {
            if (sg == 10) {
                result += "A";
            } else if (sg == 11) {
                result += "B";
            } else if (sg == 12) {
                result += "C";
            } else if (sg == 13) {
                result += "D";
            } else if (sg == 14) {
                result += "E";
            } else {
                result += "F";
            }
        } else {
            result += sg;
        }

        if (fb >= 10) {
            if (fb == 10) {
                result += "A";
            } else if (fb == 11) {
                result += "B";
            } else if (fb == 12) {
                result += "C";
            } else if (fb == 13) {
                result += "D";
            } else if (fb == 14) {
                result += "E";
            } else {
                result += "F";
            }
        } else {
            result += fb;
        }

        if (sb >= 10) {
            if (sb == 10) {
                result += "A";
            } else if (sb == 11) {
                result += "B";
            } else if (sb == 12) {
                result += "C";
            } else if (sb == 13) {
                result += "D";
            } else if (sb == 14) {
                result += "E";
            } else {
                result += "F";
            }
        } else {
            result += sb;
        }

        return result;
    }

    public void updateHTML() {
        String code = html.getText();
        if (code.length() == 6) {
            String rValue = code.substring(0, 2);
            String gValue = code.substring(2, 4);
            String bValue = code.substring(4);

            int r, g, b;
            try {
                r = Integer.parseInt(rValue, 16);
            } catch (NumberFormatException e) {
                r = color.getRed();
            }
            try {
                g = Integer.parseInt(gValue, 16);
            } catch (NumberFormatException e) {
                g = color.getGreen();
            }
            try {
                b = Integer.parseInt(bValue, 16);
            } catch (NumberFormatException e) {
                b = color.getBlue();
            }

            if (r < 0) {
                r = color.getRed();
            }
            if (g < 0) {
                g = color.getGreen();
            }
            if (b < 0) {
                b = color.getBlue();
            }

            setColor(new Color(r, g, b));
        }
    }

    public void updateCounters() {
        if (Operations.properties.useRGB255()) {
            int r, g, b, a = 255;
            try {
                r = Integer.parseInt(counterR.getText());
            } catch (NumberFormatException e) {
                r = color.getRed();
            }
            try {
                g = Integer.parseInt(counterG.getText());
            } catch (NumberFormatException e) {
                g = color.getGreen();
            }
            try {
                b = Integer.parseInt(counterB.getText());
            } catch (NumberFormatException e) {
                b = color.getBlue();
            }

            if (mode != BACKGROUND) {
                try {
                    a = Integer.parseInt(counterA.getText());
                } catch (NumberFormatException e) {
                    a = color.getAlpha();
                }
            }

            if (r == (int) ':') {
                r--;
            }
            if (g == (int) ':') {
                g--;
            }
            if (b == (int) ':') {
                b--;
            }
            if (a == (int) ':') {
                a--;
            }

            if (r > 255) {
                r = color.getRed();
            }
            if (g > 255) {
                g = color.getGreen();
            }
            if (b > 255) {
                b = color.getBlue();
            }
            if (a > 255) {
                a = color.getAlpha();
            }
            if (r < 0) {
                r = color.getRed();
            }
            if (g < 0) {
                g = color.getGreen();
            }
            if (b < 0) {
                b = color.getBlue();
            }
            if (a < 255) {
                a = color.getAlpha();
            }

            setColor(new Color(r, g, b, a));
        } else {
            double r, g, b, a = 1.0;
            try {
                r = Double.parseDouble(counterR.getText());
            } catch (NumberFormatException e) {
                r = color.getRed() / 255.0;
            }
            try {
                g = Double.parseDouble(counterG.getText());
            } catch (NumberFormatException e) {
                g = color.getGreen() / 255.0;
            }
            try {
                b = Double.parseDouble(counterB.getText());
            } catch (NumberFormatException e) {
                b = color.getBlue() / 255.0;
            }
            if (mode != BACKGROUND) {
                try {
                    a = Double.parseDouble(counterA.getText());
                } catch (NumberFormatException e) {
                    a = color.getAlpha() / 255.0;
                }
            }

            if ((int) r * 255 == (int) ':') {
                r -= 1 / 255.0;
            }
            if ((int) g * 255 == (int) ':') {
                g -= 1 / 255.0;
            }
            if ((int) b * 255 == (int) ':') {
                b -= 1 / 255.0;
            }
            if ((int) a * 255 == (int) ':') {
                a -= 1 / 255.0;
            }

            if (r > 1) {
                r = color.getRed() / 255.0;
            }
            if (g > 1) {
                g = color.getGreen() / 255.0;
            }
            if (b > 1) {
                b = color.getBlue() / 255.0;
            }
            if (a > 1) {
                a = color.getAlpha() / 255.0;
            }
            if (r < 0) {
                r = color.getRed() / 255.0;
            }
            if (g < 0) {
                g = color.getGreen() / 255.0;
            }
            if (b < 0) {
                b = color.getBlue() / 255.0;
            }
            if (a < 0) {
                a = color.getAlpha() / 255.0;
            }

            if (mode != BACKGROUND) {
                setColor(new Color((int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (a * 255)));
            } else {
                setColor(new Color((int) (r * 255), (int) (g * 255), (int) (b * 255)));
            }
        }
    }

    public void update() {
        if (red.getValue() == ':') {
            red.setValue(':' - 1);
        }
        if (green.getValue() == ':') {
            green.setValue(':' - 1);
        }
        if (blue.getValue() == ':') {
            blue.setValue(':' - 1);
        }

        if (mode != BACKGROUND) {
            if (alpha.getValue() == ':') {
                alpha.setValue(':' - 1);
            }
            setColor(new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()));
        } else {
            setColor(new Color(red.getValue(), green.getValue(), blue.getValue()));
        }
    }

    private static Color[] getBaseColors(int num) {
        Color[] result = new Color[7];

        try {
            File pallet = new File(Operations.properties.getColorPallet().get(num));

            Scanner scan = new Scanner(pallet);
            scan.useDelimiter(";");

            for (int i = 0; i < 7; i++) {
                String line = scan.next();
                Scanner scan2 = new Scanner(line);
                scan2.useDelimiter(",");
                Color color = new Color(Integer.parseInt(scan2.next()), Integer.parseInt(scan2.next()), Integer.parseInt(scan2.next()));
                result[i] = color;
            }
        } catch (FileNotFoundException e) {
            result = new Color[]{Color.black, new Color(51, 0, 0), new Color(51, 40, 0),
                new Color(51, 51, 0), new Color(0, 51, 0), new Color(0, 0, 51), new Color(51, 0, 51)};
        }

        return result;
    }

    private static Color[] getAdditionalColors(int num) {
        Color[] result = new Color[5];

        try {
            File pallet = new File(Operations.properties.getColorPallet().get(num));

            Scanner scan = new Scanner(pallet);
            scan.useDelimiter(";");
            scan.nextLine();

            for (int i = 0; i < 5; i++) {
                String line = scan.next();
                Scanner scan2 = new Scanner(line);
                scan2.useDelimiter(",");
                Color color = new Color(Integer.parseInt(scan2.next()), Integer.parseInt(scan2.next()), Integer.parseInt(scan2.next()));
                result[i] = color;
            }
        } catch (FileNotFoundException e) {
            result = new Color[]{new Color(156, 102, 31), new Color(113, 198, 113), Color.pink,
                new Color(252, 230, 201), Color.cyan};
        }

        return result;
    }

    private static JPanel getPresetPanel(int num, MemoryColorChooser caller) {
        baseColors = getBaseColors(num);
        colors = getAdditionalColors(num);

        JPanel presets = new JPanel();
        presets.setLayout(new GridLayout(8, 5, 5, 5));

        for (Color c : baseColors) {
            if (c.getRed() != 0 || c.getGreen() != 0 || c.getBlue() != 0) {
                for (int i = 1; i < 6; i++) {
                    JButton temp = new JButton();
                    temp.addActionListener(caller.listener);
                    temp.setBackground(new Color(c.getRed() * i, c.getGreen() * i, c.getBlue() * i));
                    caller.colorButtons.add(temp);
                    presets.add(temp);
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    JButton temp = new JButton();
                    temp.addActionListener(caller.listener);
                    temp.setBackground(new Color((int) ((255.0 / 4.0) * i), (int) ((255.0 / 4.0) * i), (int) ((255.0 / 4.0) * i)));
                    caller.colorButtons.add(temp);
                    presets.add(temp);
                }
            }
        }

        for (Color c : colors) {
            JButton temp = new JButton();
            temp.addActionListener(caller.listener);
            temp.setBackground(c);
            caller.colorButtons.add(temp);
            presets.add(temp);
        }

        return presets;
    }

    private String getPalletName(String fileName) {
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        if (name.contains("Pallet")) {
            name = name.substring(0, name.indexOf("Pallet"));
        }
        name = name.substring(name.lastIndexOf("\\") + 1);
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }
}
