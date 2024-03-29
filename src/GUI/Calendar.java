package GUI;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
//import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;
import javax.swing.Timer;
import javax.swing.BorderFactory;
//import java.awt.Insets;
//import java.util.Calendar; 
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Calendar extends JFrame implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;
	Container cn;
	JButton bt[][] = new JButton[7][7];
	JComboBox<String> ch;
	JTextField tf;
	Timer timer;
	private static final java.util.Calendar c = java.util.Calendar.getInstance(); // Make c static
	static int YEAR = c.get(java.util.Calendar.YEAR);
	int MONTH = c.get(java.util.Calendar.MONTH);
	int DAY = c.get(java.util.Calendar.DAY_OF_MONTH);
    Event ev = new Event(leng2(DAY + "" + "-" + leng2(MONTH + 1 + "") + "-" + YEAR));
	
	String w[] = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
	String t[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	public Calendar() {
		super("Calendar - ToThanh");
		cn = init();
		timer.start();
	}
	
	String DateTime[][] = new String[7][7];
	
	int preMonth = MONTH;
	BigInteger preYear = toBig(YEAR);
	
	public Container init() {
		Container cn = this.getContentPane();
		JPanel pn = new JPanel();
		pn.setLayout(new GridLayout(7, 7));
		
		for (int i = 0; i < 7; i ++)
			for (int j = 0; j < 7; j++) {
				bt[i][j] = new JButton();
				bt[i][j].addActionListener(this);
				bt[i][j].setActionCommand((i * 7 + j) + "");
				bt[i][j].addKeyListener(this);
				bt[i][j].setFont(new Font("Britannic Bold", 1, 25));
				bt[i][j].setBackground(Color.black);
				bt[i][j].setForeground(Color.white);
				bt[i][j].setBorder(null);
				pn.add(bt[i][j]);
			}
		for (int i = 0; i < 7; i++)
			bt[0][i].setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.cyan));
		cn.add(pn);
		
		JPanel pn2 = new JPanel();
		pn2.setLayout(new GridLayout(1, 2));
		pn2.setBackground(Color.black);
		
		JComboBox<String> ch;
		ch = new JComboBox<>();
		ch.setForeground(Color.white);
		ch.setBackground(Color.black);
		ch.setBackground(null);
		ch.setFont(new Font("Britannic Bold", 1, 20));
		for (int i = 0; i < 12; i++)
			ch.addItem(t[i]);
		ch.setSelectedIndex(preMonth);
		
		tf = new JTextField(String.valueOf(YEAR));
		tf.setBackground(Color.black);
		tf.setForeground(Color.white);
		tf.setBorder(null);
		tf.setFont(new Font("Britannic Bold", 1, 20));
		tf.setHorizontalAlignment(JTextField.CENTER);
		
		pn2.add(ch);
		pn2.add(tf);
		
		cn.add(pn2, "North");
		
		this.setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(500, 398);
		this.setLocationRelativeTo(null);
		Point p = this.getLocation();
		this.setLocation((int)p.getX() - 245, (int)p.getY());
		
		update(preMonth + 1, preYear);
		timer = new Timer(200, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				java.util.Calendar c = java.util.Calendar.getInstance(); // Use fully qualified name
    			YEAR = c.get(java.util.Calendar.YEAR);
    			MONTH = c.get(java.util.Calendar.MONTH);
			    DAY = c.get(java.util.Calendar.DAY_OF_MONTH);
				int m = ch.getSelectedIndex();
				String str = tf.getText();
				while (str.length() > 1 && str.charAt(0) == ' ')
					str = str.substring(1, str.length() - 1);
				while (str.length() > 1 && str.charAt(str.length() - 1) == ' ')
					str = str.substring(0, str.length() - 2);
				if (str.matches("[0-9]+")) {
					if ((m != preMonth || preYear.compareTo(new BigInteger(str)) != 0)) {
						ch.setSelectedIndex(m);
						update(m + 1, new BigInteger(str));
						preMonth = m;
						preYear = new BigInteger(str);
					}
				} else
					tf.setText("");
			} 
		});
		
		return cn;
	}
	
	public void reset() {
		for (int i = 1; i < 7; i ++)
			for (int j = 0; j < 7; j++) {
				bt[i][j].setBackground(Color.black);
				bt[i][j].setForeground(Color.white);
				DateTime[i][j] = "";
				bt[i][j].setBorder(null);
			}
	}
	
	public void update(int month, BigInteger year) {
		reset();
		int thu = getThu(month, year);
		int day = Nday(month, year);
		int pday = 0;
		if (month > 1)
			pday = Nday(month - 1, year);
		else
			pday = Nday(12, year.subtract(toBig(1)));
		int start = thu - 1;
		if (start == 7)
			start = 0;
		
		for (int i = 0; i < 7; i++)
			bt[0][i].setText(w[i]);
		int I = 1, J = start;
//		System.out.println(month + " " + year);
//		System.out.println(MONTH + " " + YEAR);
		for (int i = 1; i <= day; i++) {
			bt[I][J].setText(String.valueOf(i));
			bt[I][J].setForeground(Color.white);
			DateTime[I][J] = leng2(i + "") + "-" + leng2(month + "") + "-" + year;
			if (year.compareTo(toBig(YEAR)) == 0 && month == MONTH + 1 && i == DAY) {
				bt[I][J].setBackground(Color.cyan);
			}
			J++;
			if (J == 7) {
				J = 0;
				I++;
			}
		}
		for (int i = start - 1; i >= 0; i--) {
			bt[1][i].setText(pday-- + "");
			bt[1][i].setForeground(Color.gray);
		}
		int st = 1;
		while (!(I == 7 && J == 0)) {
			bt[I][J].setText(st++ + "");
			bt[I][J].setForeground(Color.gray);
			J++;
			if (J == 7) {
				J = 0;
				I++;
			}
		}
	}
	
	public String leng2(String s) {
		if (s.length() == 1)
			return "0" + s;
		return s;
	}
	
	public BigInteger toBig(int s) {
		return new BigInteger(String.valueOf(s));
	}
	
	public boolean isLeapYear(BigInteger N) {
		if (N.mod(toBig(4)).compareTo(toBig(0)) == 0 && N.mod(toBig(100)).compareTo(toBig(0)) != 0)
			return true;
		if (N.mod(toBig(400)).compareTo(toBig(0)) == 0)
			return true;
		return false;
	}
	
	public int getThu(int month, BigInteger year) {
		year = year.subtract(toBig(1));
		BigInteger d = year;
		d = year.divide(toBig(4));
		d = d.subtract(year.divide(toBig(100)));
		d = d.add(year.divide(toBig(400)));
		d = d.add(year.multiply(toBig(365)));
		for (int i = 1; i < month; i++)
			d = d.add(toBig(Nday(i, year.add(toBig(1)))));
//		System.out.println(d);
		d = d.mod(toBig(7)).add(toBig(2));
		return Integer.parseInt(d.toString());
	}
	
	public int Nday(int month, BigInteger year) {
		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				return 31;
			case 4:
			case 6:
			case 9:
			case 11:
				return 30;
			case 2:
				if (isLeapYear(year))
					return 29;
				return 28;
			}
		return 0;
	}
	
	public static void main(String[] args) {
		new Calendar();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int k = Integer.parseInt(e.getActionCommand());
		int j = k % 7;
		int i = k / 7;
//		System.out.println(i + " " + j + " " + DateTime[i][j]);
		if (DateTime[i][j] != null && DateTime[i][j].length() > 1) {
			for (int I = 0; I < 7; I++)
				for (int J = 0; J < 7; J++)
					bt[I][J].setBorder(null);
					
			bt[i][j].setBorder(BorderFactory.createMatteBorder(5 ,5 ,5 ,5, Color.red));
			ev.updateEvent(DateTime[i][j]);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			System.out.println("Hello");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
