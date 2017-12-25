package game;

//import java.awt.Container;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import systems.*;
import main.Game;
import systems.Engine;
import systems.LifeSupport;
import systems.Medbay;
import systems.PowerPlant;
import systems.Sensors;
import systems.Shields;
import systems.Weapons;
import weapons.WeaponSet;
import weapons.artillery.BasicArtillery;
import weapons.cannon.AccurateCannons;
import weapons.cannon.BasicCannons;
import weapons.cannon.HeavyCannons;
import weapons.hero.MantaRaySet;
import weapons.laser.BasicLasers;
import weapons.laser.HeavyLasers;
import weapons.launcher.ArtemisLaunchers;
import weapons.launcher.InfernoLaunchers;

public class Menu extends JPanel
{
	private static final long serialVersionUID = 1L;

	public Ship pShip1,pShip2;
	
	//ship sets (will be updated in the future)************
	private Ship[] fighters = new Ship[1], cruisers = new Ship[8], battleships = new Ship[2],
			fighters2 = new Ship[1], cruisers2 = new Ship[8], battleships2 = new Ship[2];
	
	//************************************************
	
	private String[] fighterNames, cruiserNames, battleshipNames;
	
	private JButton stats, stats2, start;
	private JComboBox ship1, ship2, weightClass;
	private JCheckBox timerBox;
	private JSlider timerSlider,scrapSlider;
	private JPanel mainPanel, settingsPanel;
	
	GUIHandler GUI = new GUIHandler();
	SpaceshipGame game;
	public Game state;
	
	public Menu(Game g)
	{		
		setLayout(new FlowLayout());
		initShips();
		initNames();
		initGUI();
		pShip1=cruisers[0];
		pShip2=cruisers2[0];
		
		state = g;
	}
	
	public void initShips()
	{
		//PLAYER 1.
		fighters[0] = new Ship("Fighter", "Nerf Fighter", 700, 1, new PowerPlant(250,700,700,500,0,5,200,250,true), 
				new Weapons(100,0,new WeaponSet[] {new AccurateCannons(5)},200), new Shields(100,100,1,3,100,200,true), new Engine(150,100,0.9,1,2,100,200),
				new LifeSupport(100,50,20,1,200),new Medbay(50,1),new Sensors(50,1,200,50,false,50),5,7,1,20);
		
		/************************************************************************************************************************************************************************************/
		
		cruisers[0] = new Ship("Hardened Cruiser", "Hardened 'Bernardo' Cruiser", 3500, 25, new PowerPlant(700,3000,3700,2600,1,35,1500,2000,true), 
				new Weapons(600,0,new WeaponSet[] {new BasicCannons(25), new ArtemisLaunchers(1,3)},1000), new Shields(500,800,2,30,1500,2000,false), new Engine(500,300,0.8,2,10,1000,1500),
				new LifeSupport(400,250,100,5,800),new Medbay(800,12),new Sensors(200,1,300,100,false,200),40,50,5,120); 
						
		cruisers[1] = new Ship("Fleet Cruiser", "Terrifying Fleet Cruiser", 3000, 10, new PowerPlant(500,3000,3500,2500,2,12,1000,1750,true), 
				new Weapons(500,0,new WeaponSet[] {new AccurateCannons(25)},1000), new Shields(400,600,2,20,500,1000,true), new Engine(400,400,0.8,2,10,500,1000),
				new LifeSupport(500,300,100,7,600),new Medbay(500,8),new Sensors(200,1,300,100,false,200),30,38,3,80);
						
		cruisers[2] = new Ship("Millennium Cruiser", "Millenium 'All-In' Cruiser", 2000, 15, new PowerPlant(400,4000,5000,3500,2,15,1250,2000,false), 
				new Weapons(600,0,new WeaponSet[] {new HeavyCannons(20), new HeavyLasers(15)},1500), new Shields(650,500,2,20,1000,1500,true), new Engine(400,300,0.7,2,15,750,1000),
				new LifeSupport(500,200,150,8,500),new Medbay(400,6),new Sensors(200,1,300,100,false,200),35,45,3,80);
				
		cruisers[3] = new Ship("Frontline Cruiser", "Firey Frontline Cruiser", 3000, 12, new PowerPlant(500,3500,4000,3000,2,15,1000,1750,true), 
				new Weapons(550,0,new WeaponSet[] {new BasicCannons(5), new BasicLasers(40)},1250), new Shields(600,500,2,15,1000,1500,true), new Engine(450,400,0.75,2,20,750,1000),
				new LifeSupport(400,300,150,6,700),new Medbay(600,10),new Sensors(200,1,300,100,false,200),40,45,5,100);
				
		cruisers[4] = new Ship("Thunder Cruiser", "Rolling Thunder Cruiser", 3500, 30, new PowerPlant(500,3500,4000,3000,1,25,1000,1500,true),
				new Weapons(650,0,new WeaponSet[] {new BasicArtillery(5)},1500), new Shields(600,600,2,25,750,1250,true), new Engine(500,300,0.75,2,15,750,1000),
				new LifeSupport(400,200,100,5,600),new Medbay(500,8),new Sensors(200,1,300,100,false,200),30,40,3,80);
				
		cruisers[5] = new Ship("Manta Cruiser", "Manta Ray Cruiser", 3500, 30, new PowerPlant(550,3750,4500,3250,2,15,1250,1750,true),
				new Weapons(500,0,new WeaponSet[] {new BasicCannons(15), new MantaRaySet(1)},1000), new Shields(400,750,1,15,750,1250,true), new Engine(400,350,0.75,3,15,1000,1500),
				new LifeSupport(500,300,150,7,500),new Medbay(400,4),new Sensors(200,1,300,100,false,200),30,40,4,100);
				
		cruisers[6] = new Ship("Alamo Cruiser", "Alamo Battle Cruiser", 2000, 10, new PowerPlant(500,3200,4000,3000,2,10,800,1200,true),
				new Weapons(350,0,new WeaponSet[] {new BasicCannons(10), new BasicArtillery(2), new BasicLasers(15), new ArtemisLaunchers(1,3)},1000), new Shields(400,250,1,5,500,750,true),
				new Engine(400,250,0.95,1,10,1300,1600), new LifeSupport(300,200,100,2,400),new Medbay(300,2),new Sensors(200,1,300,100,false,200),30,40,2,80);
		
		cruisers[7] = new Ship("Inferno Cruiser", "Inferno 'Crew-Killer' Cruiser", 2500, 10, new PowerPlant(450,2500,3500,2000,2,10,700,1000,true),
				new Weapons(350,0,new WeaponSet[] {new InfernoLaunchers(3,15), new HeavyLasers(15)},1200), new Shields(300,350,1,5,600,850,true),
				new Engine(400,300,0.9,1,8,1200,1600), new LifeSupport(350,200,100,3,300),new Medbay(300,2),new Sensors(200,1,300,100,false,200),35,45,2,100);
		
		/************************************************************************************************************************************************************************************/
		
		battleships[0] = new Ship("Lancer Class Battleship", "The Shining Lancer", 7000, 150, new PowerPlant(1000,6500,7500,6000,3,50,6000,8500,true),
				new Weapons(1000,0,new WeaponSet[] {new HeavyCannons(40), new BasicArtillery(5)},7000), new Shields(1500,1200,3,40,4500,5500,true), new Engine(1000,1500,0.5,3,50,2000,3500),
				new LifeSupport(1200,800,500,20,1700),new Medbay(1500,25),new Sensors(200,1,300,100,false,200),150,200,15,400);

		battleships[1] = new Ship("Gaurdian Class Battleship", "The Fire Gaurdian", 10000, 150, new PowerPlant(1200,7500,9000,7500,3,60,5000,7000,true),
				new Weapons(800,0,new WeaponSet[] {new BasicLasers(70), new HeavyLasers(20)},6500), new Shields(2000,1500,2,45,5000,6000,true), new Engine(1200,2000,0.6,2,40,2500,4000),
				new LifeSupport(1500,900,500,25,1500), new Medbay(1400,20),new Sensors(200,1,300,100,false,200),200,250,20,400);
		
		
		//PLAYER 2.
		fighters2[0] = new Ship("Fighter", "Nerf Fighter", 700, 1, new PowerPlant(250,700,700,500,0,5,200,250,true), 
				new Weapons(100,0,new WeaponSet[] {new AccurateCannons(5)},200), new Shields(100,100,1,3,100,200,true), new Engine(150,100,0.9,1,2,100,200),
				new LifeSupport(100,50,20,1,200),new Medbay(50,1),new Sensors(200,1,300,100,false,200),5,7,1,20);
		
		/************************************************************************************************************************************************************************************/
		
		cruisers2[0] = new Ship("Hardened Cruiser", "Hardened 'Bernardo' Cruiser", 3500, 25, new PowerPlant(700,3000,3700,2600,1,35,1500,2000,true), 
				new Weapons(600,0,new WeaponSet[] {new BasicCannons(25), new ArtemisLaunchers(1,3)},1000), new Shields(500,800,2,30,1500,2000,false), new Engine(500,300,0.8,2,10,1000,1500),
				new LifeSupport(400,250,100,5,800),new Medbay(800,12),new Sensors(200,1,300,100,false,200),40,50,5,120); 
						
		cruisers2[1] = new Ship("Fleet Cruiser", "Terrifying Fleet Cruiser", 3000, 10, new PowerPlant(500,3000,3500,2500,2,12,1000,1750,true), 
				new Weapons(500,0,new WeaponSet[] {new AccurateCannons(25)},1000), new Shields(400,600,2,20,500,1000,true), new Engine(400,400,0.8,2,10,500,1000),
				new LifeSupport(500,300,100,7,600),new Medbay(500,8),new Sensors(200,1,300,100,false,200),30,38,3,80);
						
		cruisers2[2] = new Ship("Millennium Cruiser", "Millenium 'All-In' Cruiser", 2000, 15, new PowerPlant(400,4000,5000,3500,2,15,1250,2000,false), 
				new Weapons(600,0,new WeaponSet[] {new HeavyCannons(20), new HeavyLasers(15)},1500), new Shields(650,500,2,20,1000,1500,true), new Engine(400,300,0.7,2,15,750,1000),
				new LifeSupport(500,200,150,8,500),new Medbay(400,6),new Sensors(200,1,300,100,false,200),35,45,3,80);
				
		cruisers2[3] = new Ship("Frontline Cruiser", "Firey Frontline Cruiser", 3000, 12, new PowerPlant(500,3500,4000,3000,2,15,1000,1750,true), 
				new Weapons(550,0,new WeaponSet[] {new BasicCannons(5), new BasicLasers(40)},1250), new Shields(600,500,2,15,1000,1500,true), new Engine(450,400,0.75,2,20,750,1000),
				new LifeSupport(400,300,150,6,700),new Medbay(600,10),new Sensors(200,1,300,100,false,200),40,45,5,100);
				
		cruisers2[4] = new Ship("Thunder Cruiser", "Rolling Thunder Cruiser", 3500, 30, new PowerPlant(500,3500,4000,3000,1,25,1000,1500,true),
				new Weapons(650,0,new WeaponSet[] {new BasicArtillery(5)},1500), new Shields(600,600,2,25,750,1250,true), new Engine(500,300,0.75,2,15,750,1000),
				new LifeSupport(400,200,100,5,600),new Medbay(500,8),new Sensors(200,1,300,100,false,200),30,40,3,80);
				
		cruisers2[5] = new Ship("Manta Cruiser", "Manta Ray Cruiser", 3500, 30, new PowerPlant(550,3750,4500,3250,2,15,1250,1750,true),
				new Weapons(500,0,new WeaponSet[] {new BasicCannons(15), new MantaRaySet(1)},1000), new Shields(400,750,1,15,750,1250,true), new Engine(400,350,0.75,3,15,1000,1500),
				new LifeSupport(500,300,150,7,500),new Medbay(400,4),new Sensors(200,1,300,100,false,200),30,40,4,100);
				
		cruisers2[6] = new Ship("Alamo Cruiser", "Alamo Battle Cruiser", 2000, 10, new PowerPlant(500,3200,4000,3000,2,10,800,1200,true),
				new Weapons(350,0,new WeaponSet[] {new BasicCannons(10), new BasicArtillery(2), new BasicLasers(15), new ArtemisLaunchers(1,3)},1000), new Shields(400,250,1,5,500,750,true),
				new Engine(400,250,0.95,1,10,1300,1600), new LifeSupport(300,200,100,2,400),new Medbay(300,2),new Sensors(200,1,300,100,false,200),30,40,2,80);
		
		cruisers2[7] = new Ship("Inferno Cruiser", "Inferno 'Crew-Killer' Cruiser", 2500, 10, new PowerPlant(450,2500,3500,2000,2,10,700,1000,true),
				new Weapons(350,0,new WeaponSet[] {new InfernoLaunchers(3,15), new HeavyLasers(15)},1200), new Shields(300,350,1,5,600,850,true),
				new Engine(400,300,0.9,1,8,1200,1600), new LifeSupport(350,200,100,3,300),new Medbay(300,2),new Sensors(200,1,300,100,false,200),35,45,2,100);			
		
		/************************************************************************************************************************************************************************************/
		
		battleships2[0] = new Ship("Lancer Class Battleship", "The Shining Lancer", 7000, 150, new PowerPlant(1000,6500,7500,6000,3,50,6000,8500,true),
				new Weapons(1000,0,new WeaponSet[] {new HeavyCannons(40), new BasicArtillery(5)},7000), new Shields(1500,1200,3,40,4500,5500,true), new Engine(1000,1500,0.5,3,50,2000,3500),
				new LifeSupport(1200,800,500,20,1700),new Medbay(1500,25),new Sensors(200,1,300,100,false,200),150,200,15,400);

		battleships2[1] = new Ship("Gaurdian Class Battleship", "The Fire Gaurdian", 10000, 150, new PowerPlant(1200,7500,9000,7500,3,60,5000,7000,true),
				new Weapons(800,0,new WeaponSet[] {new BasicLasers(70), new HeavyLasers(20)},6500), new Shields(2000,1500,2,45,5000,6000,true), new Engine(1200,2000,0.6,2,40,2500,4000),
				new LifeSupport(1500,900,500,25,1500), new Medbay(1400,20),new Sensors(200,1,300,100,false,200),200,250,20,400);
	}
	
	private void initNames()
	{
		fighterNames = new String[fighters.length];
		cruiserNames = new String[cruisers.length];
		battleshipNames = new String[battleships.length];
		
		for (int i=0;i<fighterNames.length;i++)
		{
			fighterNames[i]=fighters[i].name;
		}
		
		for (int i=0;i<cruiserNames.length;i++)
		{
			cruiserNames[i]=cruisers[i].name;
		}
		
		for (int i=0;i<battleshipNames.length;i++)
		{
			battleshipNames[i]=battleships[i].name;
		}
	}
	
	private void initGUI()
	{
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel mPanel1 = new JPanel();
		JPanel mPanel2 = new JPanel();
		JPanel mPanel3 = new JPanel();
		JPanel mPanel4 = new JPanel();
		JPanel mPanel5 = new JPanel();
		JPanel mPanel6 = new JPanel();
		
		//Buttons**********
		stats=new JButton("Stats");
		stats.setToolTipText("Stats for Player 1's ship");
		stats.addActionListener(GUI);
		stats2=new JButton("Stats");
		stats2.setToolTipText("Stats for Player 2's ship");
		stats2.addActionListener(GUI);
		start=new JButton("Start Game");
		start.addActionListener(GUI);
		
		//Combo Boxes*********************
		ship1 = new JComboBox(cruiserNames);
		ship1.addItemListener(GUI);
		ship2 = new JComboBox(cruiserNames);
		ship2.addItemListener(GUI);
		weightClass = new JComboBox(new String[]{"Fighter", "Cruiser", "Battleship"});
		weightClass.addItemListener(GUI);
		weightClass.setSelectedIndex(1);
		
		//Timer/Scrap*********************
		JLabel scrapLabel = new JLabel("Scrap:");
		scrapSlider = new JSlider(0,4000,2500);
		scrapSlider.setMajorTickSpacing(1000);
		scrapSlider.setMinorTickSpacing(500);
		scrapSlider.setPaintLabels(true);
		scrapSlider.setPaintTicks(true);
		scrapSlider.setSnapToTicks(true);
		timerBox = new JCheckBox("Timer?");
		timerSlider = new JSlider(60,90,90);
		timerSlider.setMajorTickSpacing(10);
		timerSlider.setMinorTickSpacing(5);
		timerSlider.setPaintLabels(true);
		timerSlider.setPaintTicks(true);
		timerSlider.setSnapToTicks(true);
		
		//Labels********************
		mPanel1.add(new JLabel("Main Menu/Ship Select:"));
		mPanel2.add(new JLabel("Weight Class"));
		mPanel2.add(weightClass);
		mPanel2.add(start);
		mPanel3.add(new JLabel("Player 1's Ship:"));
		mPanel3.add(ship1);
		mPanel3.add(stats);
		mPanel4.add(new JLabel("Player 2's Ship:"));
		mPanel4.add(ship2);
		mPanel4.add(stats2);
		mPanel5.add(timerBox);
		mPanel5.add(timerSlider);
		mPanel6.add(scrapLabel);
		mPanel6.add(scrapSlider);
		
		mainPanel.add(mPanel1);
		mainPanel.add(mPanel2);
		mainPanel.add(mPanel3);
		mainPanel.add(mPanel4);
		mainPanel.add(mPanel5);
		mainPanel.add(mPanel6);
		
		this.add(mainPanel);
	}

	private void createSettingsPanel()
	{
		settingsPanel = new JPanel();
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
		
		JPanel sPanel1 = new JPanel();
		JPanel sPanel2 = new JPanel();
		JPanel sPanel3 = new JPanel();
		
		
		
		settingsPanel.add(sPanel1);
		settingsPanel.add(sPanel2);
		settingsPanel.add(sPanel3);
	}
	
	private Ship[] getShipArray(boolean b)
	{
		if (weightClass.getSelectedIndex()==0)
		{
			if (b)
			{return fighters;}
			else
			{return fighters2;}
		}
		else if (weightClass.getSelectedIndex()==1)
		{
			if (b)
			{return cruisers;}
			else
			{return cruisers2;}
		}
		else if (weightClass.getSelectedIndex()==2)
		{
			if (b)
			{return battleships;}
			else
			{return battleships2;}
		}
		return null;
	}
	
	private String[] getShipNames()
	{
		if (weightClass.getSelectedIndex()==0)
		{
			return fighterNames;
		}
		else if (weightClass.getSelectedIndex()==1)
		{
			return cruiserNames;
		}
		else if (weightClass.getSelectedIndex()==2)
		{
			return battleshipNames;
		}
		return null;
	}
	
	private class GUIHandler implements ActionListener, ItemListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			JButton b = (JButton) e.getSource();
			
			if (b.equals(stats))
			{
				JOptionPane.showMessageDialog(null, "Ship Properties:\nArmor/Weight: " + pShip1.getArmor() + " armor at a total of " + pShip1.getWeight() + " tons." + "\nPower Reactor: " + pShip1.Power.getEnergyStored() + 
						" starting energy with " + pShip1.Power.getEnergyProducedBase() + " energy produced.\nWeapons System: " + pShip1.Weapon.toString() + ".\nShields: " + pShip1.Shield.getEnergyMaxBase() +
						" max energy.\nEngine: " + pShip1.Engine.getEnergyMaxBase() + " max energy input, capable of " + pShip1.Engine.getMaxThrust() + " thrust, and a max evade of " + pShip1.getMaxEvade() + "%.");
			}
			else if (b.equals(stats2))
			{
				JOptionPane.showMessageDialog(null, "Ship Properties:\nArmor/Weight: " + pShip2.getArmor() + " armor at a total of " + pShip2.getWeight() + " tons." + "\nPower Reactor: " + pShip2.Power.getEnergyStored() + 
						" starting energy with " + pShip2.Power.getEnergyProducedBase() + " energy produced.\nWeapons System: " + pShip2.Weapon.toString() + ".\nShields: " + pShip2.Shield.getEnergyMaxBase() +
						" max energy.\nEngine: " + pShip2.Engine.getEnergyMaxBase() + " max energy input, capable of " + pShip2.Engine.getMaxThrust() + " thrust, and a max evade of " + pShip2.getMaxEvade() + "%.");
			}
			else if (b.equals(start))
			{
				pShip1=getShipArray(true)[ship1.getSelectedIndex()];
				pShip2=getShipArray(false)[ship2.getSelectedIndex()];

				pShip1.setScrap(scrapSlider.getValue());
				pShip2.setScrap(scrapSlider.getValue());
				
				boolean useTimer = timerBox.isSelected();
				int timerLength = timerSlider.getValue()*1000;
				
				state.beginStrategy(useTimer, timerLength);
			}
		}

		public void itemStateChanged(ItemEvent e) 
		{
			JComboBox b = (JComboBox) e.getSource();
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				if(b.equals(ship1))
				{
					pShip1=getShipArray(true)[ship1.getSelectedIndex()];
				}
				else if (b.equals(ship2))
				{
					pShip2=getShipArray(false)[ship2.getSelectedIndex()];
				}
				else if (b.equals(weightClass))
				{
					ship1.removeAllItems();
					ship2.removeAllItems();
					
					String[] n = getShipNames();
					for (int i=0; i<n.length;i++)
					{
						ship1.addItem(n[i]);
						ship2.addItem(n[i]);
					}
					
					ship1.setSelectedIndex(0);
					ship2.setSelectedIndex(0);
					
					pShip1=getShipArray(true)[0];
					pShip2=getShipArray(false)[0];
				}
			}
		}
	}
}