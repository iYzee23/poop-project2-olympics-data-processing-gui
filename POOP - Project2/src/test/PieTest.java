package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PieTest extends JPanel {
	
	private static final long serialVersionUID = 1L;
	int value[];
    int start = 0;

    Color[] colors = {Color.red, Color.green, Color.blue, Color.orange};
    public PieTest(String[] args)
    {
        value = new int[args.length];

        for(int i = 0; i<args.length; i++) //Error at this line
        {
            value[i] = Integer.parseInt(args[i]);
        }

    }

    @Override
	public void paintComponent(Graphics g)  {
         super.paintComponent(g);
         if (value == null) {
             return;
         }
        for(int i = 0; i<value.length; i++) {
            g.setColor(colors[i%colors.length]);
            g.fillArc(150, 150, 200, 200, start, value[i]);
            start = start + value[i];
        }

    }

    public static void main(String args[])
    {
        JFrame f = new JFrame("Piechart");
        PieTest p = new PieTest(new String[]{"90","90","90", "90"});

        f.add(p);
        p.setPreferredSize(new Dimension(400, 400));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.repaint();
    }
}



/*
//CARD LAYOUT
CardLayout cl = new CardLayout();
Panel cardPanel = new Panel(cl);	
Panel card;		
for(int i = 0; i< cardsCount; i++) {
	card = new Panel();
	card.setBackground(new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));
	cardPanel.add(card);
}
add(cardPanel, BorderLayout.CENTER);
Button next = new Button("-> Next"), previous = new Button("<- Previous");		
next.addActionListener((ia)->{
	cl.next(cardPanel);
});	
previous.addActionListener((ia)->{
	cl.previous(cardPanel);
});
Panel southPanel = new Panel();		
southPanel.add(previous);
southPanel.add(next);
add(southPanel, BorderLayout.SOUTH);

//QUIT DIALOG
private class QuitDialog extends Dialog{
                    private Button ok = new Button("OK");
	private Button cancel = new Button("Cancel");		
	public void paint(Graphics g) {//moze umesto ovoga i add(new Label("Neki tekst"));
		g.drawString("Are you sure you want to quit?", 20, 70);
		super.paint(g);
	}
	public QuitDialog(Frame owner) {
		setModalityType(ModalityType.APPLICATION_MODAL);	
		Panel buttons = new Panel();
		buttons.add(ok);
		buttons.add(cancel);
		ok.addActionListener((ae)->{
			LoginForm.this.dispose();
		});
		cancel.addActionListener((ae)->{
			QuitDialog.this.dispose();
		});	
		add(buttons, BorderLayout.SOUTH);
		setVisible(true);
	}		
}
addWindowListener(new WindowAdapter() {
	public void windowClosing(WindowEvent e) {
		new QuitDialog(LoginForm.this);
	}
});

//CIRCLE DRAW
g.setColor(Color.YELLOW);
g.fillOval(x - width/2, y - width/2, width, width);

//PLAYER DRAW
g.drawLine(x, y - width/2, x, y + width/2 - 1);
g.drawLine(x - width/2, y, x + width/2 - 1, y);

//PAUZIRANJE
public synchronized void pause() {
	work = false;
}

//KEYLISTENER
addKeyListener(new KeyAdapter() {
	public void keyTyped(KeyEvent e) {
		char key = Character.toUpperCase(e.getKeyChar());
		switch(key) {
		case KeyEvent.VK_W:{
			int y = player.getY() - squareWidth;
			player.setY(y<0?player.getY():y);
			break;
		}
		}
		repaint();
	}
});

//COMPONENTS
TextArea log = new TextArea();
log.setEditable(false);
log.append("Neki tekst");
log.setColumns(20);
log.setRows(1);
log.setEditable(false);

Label passStrength = new Label("");
TextField coins = new TextField("10"); //sta pise unutra
Integer.parseInt(coins.getText())
TextField password = new TextField(10); //neka prosecna duzina
password.setEchoChar('*');
password.addTextListener((te)->{
	int passLen = password.getText().length();
	if(passLen == 0) {
		passStrength.setText("");
	}
	else if(passLen<5) {
		passStrength.setText("Weak");
		passStrength.setForeground(Color.RED);
	}
	passStrength.revalidate();
});

//MENU
Menu bgColorMenu = new Menu("Bg color");
MenuItem bgWhite = new MenuItem("white");
bgColorMenu.add(bgWhite);
bgWhite.addActionListener((ae)->{
	scene.setBgColor(Color.WHITE);
});	
Menu factorMenu = new Menu("Size");
MenuItem small = new MenuItem("3");
factorMenu.add(small);
small.addActionListener((ae)->{
	scene.setFactor(Integer.parseInt(((MenuItem)ae.getSource()).getLabel()));
});	
MenuItem quitMenu = new MenuItem("Quit", new MenuShortcut(KeyEvent.VK_Q));	
quitMenu.addActionListener((ae)->{
	dispose();
});
Menu file = new Menu("File");
file.add(bgColorMenu);
file.add(factorMenu);
file.addSeparator();
MenuBar menuBar = new MenuBar();
menuBar.add(file);
this.setMenuBar(menuBar);

//CHOICE
Choice chooseShape = new Choice();
chooseShape.add("Heart");
chooseShape.addItemListener((ie)->{
	String name = chooseShape.getSelectedItem();
	if(name.equals("Heart")) {
		scene.setShape(new Heart());
	}
});
//LIST
List chooseColor = new List(2);//koliko ce ih biti prikazano
chooseColor.add("Black");
chooseColor.select(0);
chooseColor.addItemListener((ie)->{
	String item = chooseColor.getSelectedItem();
	if(item.equals("Black")) {
		scene.setColor(Color.BLACK);
	}
});

//RADIOBUTTON
CheckboxGroup genderGroup = new CheckboxGroup();
Checkbox maleCb = new Checkbox("Male", true, genderGroup);
Checkbox femaleCb = new Checkbox("Female", false, genderGroup);
genderPanel.add(maleCb);
genderPanel.add(femaleCb);

//CHECKBOX
Checkbox termsCb = new Checkbox("I agree with terms and conditions.");
termsPanel.add(termsCb);
termsCb.addItemListener((ie)->{
	if(ie.getStateChange() == ItemEvent.SELECTED) {
		submit.setEnabled(true);
	}
	else {
		submit.setEnabled(false);
	}
});

//DRAWING LINES
public void paint(Graphics g) {
	for(Line it: lines) {
		g.drawLine(it.begX, it.begY, it.endX, it.endY);
	}
	super.paint(g);
}
private class MouseEventHandler extends MouseAdapter{
	public void mouseDragged(MouseEvent e) {
		line.endX = e.getX();
		line.endY = e.getY();
		repaint();
	}
	public void mousePressed(MouseEvent e) {
		line = new Line();
		lines.add(line);
		line.begX = e.getX();
		line.begY = e.getY(); 
	}
	public void mouseReleased(MouseEvent e) {
		repaint();
	}
}
*/