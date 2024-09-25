import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * Creates GUI paint application that can clear, fill, erase, has Hex and RGB buttons as well
 *
 * <p>Purdue University -- CS18000 -- Fall 2023</p>
 *
 * @author Netra Joshi Purdue CS
 * @version November 16, 2023
 */

public class Paint extends JComponent implements Runnable {
    Image image;
    Graphics2D graphics2D;
    int curX;
    int curY;
    int oldX;
    int oldY;

    JButton clearButton;
    JButton fillButton;
    JButton eraseButton;
    JButton randomButton;
    JButton hexButton;
    JButton rgbButton;

    JTextField hexText;
    JTextField rText;
    JTextField gText;
    JTextField bText;

    Color currentColor = Color.BLACK;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Paint());
    }

    public Paint() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                oldX = e.getX();
                oldY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                curX = e.getX();
                curY = e.getY();

                if (graphics2D != null) {
                    graphics2D.drawLine(oldX, oldY, curX, curY);

                    repaint();
                    oldX = curX;
                    oldY = curY;
                }
            }
        });
    }

    public void run() {
        JFrame frame = new JFrame();
        frame.setTitle("Simple Paint Walkthrough");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        clearButton = new JButton("Clear");
        fillButton = new JButton("Fill");
        eraseButton = new JButton("Erase");
        randomButton = new JButton("Random");
        hexButton = new JButton("Hex");
        rgbButton = new JButton("RGB");

        hexText = new JTextField("#", 10);
        rText = new JTextField("", 5);
        gText = new JTextField("", 5);
        bText = new JTextField("", 5);

        JPanel panel = new JPanel();
        //anel.add(strTextField);
        panel.add(clearButton);
        panel.add(fillButton);
        panel.add(eraseButton);
        panel.add(randomButton);
        panel.add(hexButton);
        panel.add(rgbButton);

        content.add(panel, BorderLayout.NORTH);

        content.add(this, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(hexText);
        bottomPanel.add(rText);
        bottomPanel.add(gText);
        bottomPanel.add(bText);

        content.add(bottomPanel, BorderLayout.SOUTH);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        fillButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (graphics2D != null) {
                    graphics2D.setPaint(currentColor);
                    graphics2D.fillRect(0, 0, getWidth(), getHeight());
                    repaint();
                    hexText.setText("#");
                    rText.setText("");
                    gText.setText("");
                    bText.setText("");

                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (graphics2D != null) {
                    graphics2D.clearRect(0, 0, getWidth(), getHeight());
                    graphics2D.setColor(Color.WHITE);
                    graphics2D.fillRect(0, 0, getWidth(), getHeight());
                    graphics2D.setColor(currentColor);
                    repaint();
                    hexText.setText("#");
                    rText.setText("");
                    gText.setText("");
                    bText.setText("");
                }
            }
        });
        eraseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int size = 5;
                draw(size);
            }
        });
        eraseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int size = 5;
                draw(size);
            }
        });
        randomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Random random = new Random();
                Color randomColor = new Color(random.nextInt(256), random.nextInt(256),
                        random.nextInt(256));
                currentColor = randomColor;
                repaint();
            }
        });
        hexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String hexValue = hexText.getText();
                    Color decodedColor = Color.decode(hexValue);
                    graphics2D.setColor(decodedColor);
                    rText.setText(String.valueOf(decodedColor.getRed()));
                    gText.setText(String.valueOf(decodedColor.getGreen()));
                    bText.setText(String.valueOf(decodedColor.getBlue()));
                    repaint();
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(Paint.this, "This is not a valid Hex value",
                            "Error!", JOptionPane.ERROR_MESSAGE);

                }
            }
        });
        rgbButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String rTextValue = rText.getText();
                    int r = 0;
                    if (!rTextValue.isEmpty()) {
                        r = Integer.parseInt(rTextValue);
                    }
                    String gTextValue = rText.getText();
                    int g = 0;
                    if (!rTextValue.isEmpty()) {
                        g = Integer.parseInt(gTextValue);
                    }
                    String bTextValue = rText.getText();
                    int b = 0;
                    if (!rTextValue.isEmpty()) {
                        b = Integer.parseInt(bTextValue);
                    }
                    Color newColor = new Color(r, g, b);
                    graphics2D.setColor(newColor);
                    hexText.setText(String.format("#%02x%02x%02x", r, g, b));
                    repaint();
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(Paint.this, "This is not a valid RGB value",
                            "Error", JOptionPane.ERROR_MESSAGE);

                }
            }
        });
    }

    protected void paintComponent(Graphics graphicsObject) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);

            graphics2D = (Graphics2D) image.getGraphics();

            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            graphics2D.setPaint(Color.WHITE);
            graphics2D.fillRect(0, 0, getSize().width, getSize().height);
            graphics2D.setPaint(currentColor);
            graphics2D.setStroke(new BasicStroke(5));
            repaint();
        }
        graphicsObject.drawImage(image, 0, 0, null);
    }

    public void draw(int size) {
        if (graphics2D != null) {
            graphics2D.setStroke(new BasicStroke(size));
        }
    }
}
