package ui.modules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import game.SpaceshipGame;

public class RichSlider extends JPanel implements ActionListener,ChangeListener
{

	private JSlider slider;
	private JButton plus,minus;
	private JTextField text;
	private int value,min,max;
	private SpaceshipGame parent;
	
	public RichSlider(SpaceshipGame s, int min, int max, int val)
	{
		init(min,max,val);
		if (max%10==0)
		{
			slider.setMajorTickSpacing(max/5);
			slider.setMinorTickSpacing(max/10);
		}
		else
		{
			slider.setMajorTickSpacing(max/5);
			slider.setMinorTickSpacing(max/25);
		}
		parent=s;
		value=val;
	}
	
	public RichSlider(SpaceshipGame s, int min, int max, int val, int majTick, int minTick)
	{
		init(min,max,val);
		slider.setMajorTickSpacing(majTick);
		slider.setMinorTickSpacing(minTick);
		parent=s;
		value=max;
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
		
		this.max= max;
		this.min=min;
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
		update(value);
	}
	
	public int getMaximum()
	{
		return max;
	}
	
	public int getMinimum()
	{
		return min;
	}
	
	public void update(int i)
	{
		slider.setValue(i);
		text.setText(Integer.toString(i));
	}
	
	public void setMaximum(int i)
	{
		max=i;
		slider.setMaximum(i);
		//update(min);
	}
	
	public void setMinimum(int i)
	{
		min=i;
		slider.setMinimum(i);
		update(min);
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

	public void stateChanged(ChangeEvent arg0) 
	{
		setValue(slider.getValue());
		if (parent.gameStart)
		{
			parent.richSliderUpdate(this);
		}
	}
	
	public void setEnabled(Boolean b)
	{
		slider.setEnabled(b);
		plus.setEnabled(b);
		minus.setEnabled(b);
		text.setEnabled(b);
		parent.validate();
		parent.repaint();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof JTextField)
		{
			JTextField f = (JTextField)e.getSource();
			if (f.equals(text))
			{
				setValue(Integer.valueOf(text.getText()));
				parent.richSliderUpdate(this);
			}
		}
		
		else if (e.getSource() instanceof JButton)
		{
			JButton b = (JButton)e.getSource();
			if (b.equals(plus))
			{
				setValue(value+1);
				parent.richSliderUpdate(this);
			}
			else if (b.equals(minus))
			{
				setValue(value-1);
				parent.richSliderUpdate(this);
			}
		}
	}
}
