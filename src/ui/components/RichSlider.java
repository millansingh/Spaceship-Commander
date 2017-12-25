package ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import game.SpaceshipGame;
import main.Game;
import ui.modules.Module;

public class RichSlider extends JPanel implements ActionListener,ChangeListener
{

	private JSlider slider;
	private JButton plus,minus;
	private JTextField text;
	private int value,min,max;
	private SpaceshipGame parent;
	private Module module;
	private boolean realTimeUpdate = false;
	
	public RichSlider(SpaceshipGame s, int min, int max, int val, boolean updates)
	{
		init(min,max,val);
		if (max <= 50) {
			if (max % 10 == 0) {
				slider.setMajorTickSpacing(10);
				slider.setMinorTickSpacing(2);
			}
			else if (max % 5 == 0) {
				slider.setMajorTickSpacing(5);
				slider.setMinorTickSpacing(1);
			}
			else {
				slider.setMajorTickSpacing(max/5);
				slider.setMinorTickSpacing(max/25);
			}
		}
		else {			
			if (max%10==0) {
				slider.setMajorTickSpacing(max/5);
				slider.setMinorTickSpacing(max/10);
			}
			else {
				slider.setMajorTickSpacing(max/5);
				slider.setMinorTickSpacing(max/25);
			}
		}
		parent = s;
		value = val;
		realTimeUpdate = updates;
	}
	
	public RichSlider(SpaceshipGame s, int min, int max, int val, int majTick, int minTick, boolean updates)
	{
		init(min,max,val);
		slider.setMajorTickSpacing(majTick);
		slider.setMinorTickSpacing(minTick);
		parent = s;
		value = max;
		realTimeUpdate = updates;
	}
	
	public RichSlider(Module m, int min, int max, int val, boolean updates)
	{
		init(min,max,val);
		if (max <= 50) {
			if (max % 10 == 0) {
				slider.setMajorTickSpacing(10);
				slider.setMinorTickSpacing(2);
			}
			else if (max % 5 == 0) {
				slider.setMajorTickSpacing(5);
				slider.setMinorTickSpacing(1);
			}
			else {
				slider.setMajorTickSpacing(max/5);
				slider.setMinorTickSpacing(max/25);
			}
		}
		else {			
			if (max%10==0) {
				slider.setMajorTickSpacing(max/5);
				slider.setMinorTickSpacing(max/10);
			}
			else {
				slider.setMajorTickSpacing(max/5);
				slider.setMinorTickSpacing(max/25);
			}
		}
		module = m;
		value = val;
		realTimeUpdate = updates;
	}
	
	public RichSlider(Module m, int min, int max, int val, int majTick, int minTick, boolean updates)
	{
		init(min,max,val);
		slider.setMajorTickSpacing(majTick);
		slider.setMinorTickSpacing(minTick);
		module = m;
		value = max;
		realTimeUpdate = updates;
	}
	
	private void init(int min, int max, int val)
	{
		plus = new JButton("+");
		plus.addActionListener(this);
		minus = new JButton("-");
		minus.addActionListener(this);
		slider = new JSlider(min,max,val);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		//slider.setSnapToTicks(true);
		slider.addChangeListener(this);
		text = new JTextField(3);
		text.addActionListener(this);
		
		add(minus);
		add(slider);
		add(plus);
		add(text);
		
		this.max = max;
		this.min = min;
		slider.setValue(val);
		text.setText(Integer.toString(val));
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void setValue(int i)
	{
		if (i>max)
		{
			i=max;
		}
		else if (i<min)
		{
			i=min;
		}
		value=i;
	}
	
	public int getMaximum()
	{
		return max;
	}
	
	public int getMinimum()
	{
		return min;
	}
	
	public void setMaximum(int i)
	{
		max=i;
		slider.setMaximum(i);
	}
	
	public void setMinimum(int i)
	{
		min=i;
		slider.setMinimum(i);
	}
	
	public void setMajorTickSpacing(int i)
	{
		slider.setMajorTickSpacing(i);
	}
	
	public void setMinorTickSpacing(int i)
	{
		slider.setMinorTickSpacing(i);
	}
	
	public void setLabelTable(Dictionary d)
	{
		slider.setLabelTable(d);
	}
	
	public Hashtable createStandardLabels(int i)
	{
		return slider.createStandardLabels(i);
	}
	
	public void update() {
		slider.setValue(value);
		text.setText(Integer.toString(value));
		
		this.revalidate();
		this.repaint();
	}
	
	public void update(int i) {
		setValue(i);
		slider.setValue(i);
		text.setText(Integer.toString(i));
		
		if (realTimeUpdate) {
			if (parent != null) {
				parent.richSliderUpdate(this);
			}
			else if (module != null) {
				module.update();
			}
		}
	}

	public void stateChanged(ChangeEvent arg0) 
	{
//		System.out.println(slider.getValue());
		setValue(slider.getValue());
	}
	
	public void rsSetEnabled(Boolean b)
	{
		slider.setEnabled(b);
		plus.setEnabled(b);
		minus.setEnabled(b);
		text.setEnabled(b);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof JTextField)
		{
			JTextField f = (JTextField)e.getSource();
			if (f.equals(text))
			{
				try {
					int val = Integer.valueOf(text.getText());
					if (val <= max && val >= min) {
						setValue(val);
					}
					else {
						JOptionPane.showMessageDialog(null, "Number entered was out of bounds.");
					}
				} catch (NumberFormatException numEx) {
					JOptionPane.showMessageDialog(null, "Invalid input -- that's not a number.");
				}
			}
		}
		
		else if (e.getSource() instanceof JButton)
		{
			JButton b = (JButton)e.getSource();
			if (b.equals(plus))
			{
				setValue(value+1);
			}
			else if (b.equals(minus))
			{
				setValue(value-1);
			}
		}
	}
}
