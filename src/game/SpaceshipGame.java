package game;

import main.Game;
import main.Tools;
import main.cmdLineClass;
import systems.Part;
import ui.components.RichSlider;
import ui.modules.SystemStatusPanel;
import ui.modules.CrewPanel;
import ui.modules.SystemExtinguishPanel;
import weapons.Damage;
import weapons.WeaponSet;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;


public class SpaceshipGame extends JPanel implements ActionListener, Runnable
{
	private static final long serialVersionUID = 1L;
	
	public Game state;
	
	public static Ship ship1, ship2, currentShip;
	public static int turnCount;
	public static boolean turn = false; //true is player 1, false player 2
	
	/***************************************************************************************************************************************/
	
	//Panels and tabbedPane.
	public JPanel mainPanel, titlePanel, statusPanel, weapPanel, shieldPanel, enginePanel, reactorPanel, crewPanel, lifeSupportPanel, medBayPanel, sensorsPanel;
	public JTabbedPane tabbedPane;
	
	public SystemStatusPanel medBayStatus, sensorsStatus, lifeSupportStatus, weapStatus, shieldStatus, engineStatus, reactorStatus;
	public CrewPanel weapCrewPanel, lifeSupportCrewPanel, shieldCrewPanel, engineCrewPanel, reactorCrewPanel, medBayCrewPanel, sensorsCrewPanel;
	public SystemExtinguishPanel sensorsExtinguishPanel, weapExtinguishPanel, medBayExtinguishPanel, reactorExtinguishPanel, lifeSupportExtinguishPanel, engineExtinguishPanel, shieldExtinguishPanel;
	
	/***************************************************************************************************************************************/
	
	//Reactor, Shields, Engine, and Sensors tab buttons.
	private JButton OverloadReactor, EnergyUse, CooldownReactor, OverloadShields, HardResetShields, RamEngine, lockCrew, lockEnergy;
	
	//Buttons for sending injured crew to MedBay and Medbay tab buttons.
	private JButton sendInjuredWeap, sendInjuredShield, sendInjuredEngine, sendInjuredReactor, sendInjuredLifeSupport, sendInjuredSensors, medicList;
	
	//Buttons in Command and Control tab/Title tab.
	private JButton WeapStats, CrewList, Crew, ManShip, HalfRepair, EndRepair, ResetCrew, Repair, StopRepair, End, Forfeit, Pause;
	
	/***************************************************************************************************************************************/
	
	//Drop-downs for Shield targeting, switching between weapons in Weapons tab.
	private JComboBox cShield, cShield2, cShield3, cWeapons, cWeaps;
	
	//Drop-downs for Crew allocation.
	private JComboBox cParts, cPartsWeap;

	/***************************************************************************************************************************************/
	
	//Labels for top panel and crew panel.
	private JLabel lTimer, lShip1, lShip2, lCrew, crewAlive;
	
	//Labels for Shields, Engine, Life Support, Reactor, Weapons, and Medical Bay.
	private JLabel shieldAmount, engineAmount, evade, lifeSupportAmount, AvailableEnergy, energyProd, sWeapon, gunsToFire, availableMedics, treatedCrew;
	
	//Labels for Fire.
	private JLabel weaponFire, shieldFire, engineFire, reactorFire, lifeSupportFire, medBayFire, sensorsFire;
	
	//Labels for extinguishers.
	private JLabel weaponExtinguish, shieldExtinguish, engineExtinguish, reactorExtinguish, lifeSupportExtinguish, medBayExtinguish, sensorsExtinguish;
	
	//Labels for oxygen.
	private JLabel weaponOxygen, shieldOxygen, engineOxygen, reactorOxygen, hullOxygen, lifeSupportOxygen, medBayOxygen, sensorsOxygen, oxygenFillRate;
	
	//Labels for crew in each system.
	private JLabel crewWeap, crewShield, crewEngine, crewReactor, crewLifeSupport, crewSensors;
	
	//Labels for repairs.
	private JLabel repairWeap, repairShield, repairEngine, repairReactor, repairLifeSupport, repairMedBay, repairSensors;
	
	//Labels for sensors.
	private JLabel sensorsAmount, sensPower, sensShield, sensWeaponSys, sensEngine, sensLifeSupport, sensMedBay, sensSensors;
	
	/***************************************************************************************************************************************/

	//Sliders and RichSliders for various.
	private RichSlider sensorsCrewInjured, medBayThreshold, reactorCrewInjured, engineCrewInjured, lifeSupportCrewInjured, shieldCrewInjured, weapCrewInjured, engineSlider, shieldSlider, crewSlider, lifeSupportSlider, weapSlider, sensorsSlider;
	
	/***************************************************************************************************************************************/
	
	//Icons!!
	private ImageIcon overloadIcon = new ImageIcon("img/icons/icon_Overload.png","Overloaded"),
			overloadNIcon = new ImageIcon("img/icons/icon_OverloadN.png","Not Overloaded");
	
	private ImageIcon fireIcon = new ImageIcon("img/icons/icon_Fire.png","On Fire"),
			fireNIcon = new ImageIcon("img/icons/icon_FireN.png","Not On Fire");
	
	private ImageIcon extinguishIcon = new ImageIcon("img/icons/icon_Extinguisher.png","Extinguishers Activated"),
			extinguishNIcon = new ImageIcon("img/icons/icon_ExtinguisherN.png","Extinguishers Deactivated");
	
	private ImageIcon oxygenIcon = new ImageIcon("img/icons/icon_Oxygen.png","Oxygen Icon");
	private ImageIcon repairIcon = new ImageIcon("img/icons/icon_repair.png","Repair");
	
	/***************************************************************************************************************************************/
	
	//String arrays needed for various inputs. 'cNames' is for targeting ComboBox. 'cWeapsCrew' is for weapon repair priority ComboBox. 'cPartNames' is for crew allocation ComboBox. 'yesNo' is for dialog box.
	private String[] cNames = {"Non-Specified", "Reactor", "Shield Generator", "Engines", "Hull", "Life Support", "Weapon System", "Weapons"};
	private String[] cWeapsCrew;
	private String[] cPartNames = {"Reactor", "Shield Generator", "Engines", "Weapon System", "Life Support", "Sensors", "Weapons"};
	private String[] yesNo = {"Yes","No"};
	
	/***************************************************************************************************************************************/
	
	SpaceHandler Handler = new SpaceHandler();
	
	//Timer stuff.
	public Timer timerStep;
	public int roundTime,stepTime=1000;// 1000 = 1 sec
	public int timeStep=roundTime/1000;
	public boolean timerEnabled;
	timerHandler tHandler = new timerHandler();
	
	//Debug Stuff.
	cmdLineClass cmd = new cmdLineClass(ship1,ship2,this);
	Thread cmdLine = new Thread(cmd,"Command Line");
	
	public SpaceshipGame(Ship s1, Ship s2, Game g, boolean b, int t)
	{
		state = g;
		
		ship1=s1;
		ship2=s2;
		turnCount=1;
		
		cmdLine.start();
		
		lShip1 = new JLabel("Player 1's ship - the " + ship1.name + ":");
		lShip2 = new JLabel("Player 2's ship - the " + ship2.name + ":");
		
		timerEnabled=b;
		roundTime=t;
		timeStep=roundTime/1000;
		if (timerEnabled)
		{
			lTimer = new JLabel("Timer: " + timeStep + "     ");
			timerStep = new Timer(1000,this);
		}
		
		initGUI(ship1, ship2);
		
		startGame();
	}

	public void run() 
	{
				
	}

	public void initGUI(Ship s, Ship s2)
	{
		turn=!turn;
		Handler.weapNum=0;
		this.removeAll();
		
		if (timerEnabled && state.gameStart)
		{
			timeStep=roundTime/1000;
			timerStep.restart();
			lTimer.setText("Timer: " + timeStep + "     ");
		}
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		tabbedPane = new JTabbedPane();
		
		createTitlePanel();
		createStatusPanel(s);
		createCrewPanel(s);
		createSensorsPanel(s, s2);
		createWeaponPanel(s);
		if (!s.Shield.isDestroyed)
		{
			createShieldsPanel(s);
		}
		else
		{
			createAltShieldsPanel();
		}
		createEnginePanel(s);
		if (!s.Power.isDestroyed)
		{
			createReactorPanel(s);
		}
		else
		{
			createAltReactorPanel();
		}
		createMedBayPanel(s);
		createLifeSupportPanel(s);
		
		mainPanel.add(titlePanel);
		mainPanel.add(tabbedPane);
		this.add(mainPanel);
		
		tabbedPane.addTab("Command and Control", statusPanel);
		tabbedPane.addTab("Sensors", sensorsPanel);
		tabbedPane.addTab("Medical Bay", medBayPanel);
		tabbedPane.addTab("Life Support", lifeSupportPanel);
		tabbedPane.addTab("Weapons", weapPanel);
		tabbedPane.addTab("Shields", shieldPanel);
		tabbedPane.addTab("Engines", enginePanel);
		tabbedPane.addTab("Reactor", reactorPanel);
		
		this.setBackground(Color.WHITE);
		
		this.validate();
		this.repaint();
	}
	
	private void createTitlePanel()
	{
		titlePanel = new JPanel();
		Border border = BorderFactory.createTitledBorder("Main Controls");
		titlePanel.setBorder(border);
		titlePanel.setLayout(new BoxLayout(titlePanel,BoxLayout.Y_AXIS));
		
		JPanel tPanel1 = new JPanel();
		JPanel tPanel2 = new JPanel();
		
		JLabel t = new JLabel(" Turn " + turnCount);
		
		if (timerEnabled)
		{
			tPanel1.add(lTimer);
		}
		if (turn)
		{
			tPanel1.add(lShip1);
		}
		else
		{
			tPanel1.add(lShip2);
		}
		
		Pause = new JButton ("Pause (not working)");
		End = new JButton ("End Turn");
		Forfeit = new JButton("Forfeit");
		
		tPanel1.add(t);
		tPanel2.add(Pause);
		tPanel2.add(End);
		tPanel2.add(Forfeit);
		
		titlePanel.add(tPanel1);
		titlePanel.add(tPanel2);
		
		End.addActionListener(Handler);
		Forfeit.addActionListener(Handler);
	}
	
	private void createStatusPanel(Ship s)
	{
		statusPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel,BoxLayout.Y_AXIS));
		
		JPanel statusPanel1 = new JPanel();
		JPanel statusPanel2 = new JPanel();
		
		JLabel armor = new JLabel("Hull Remaining:");
		JProgressBar armorStatus = new JProgressBar(0,s.getInitArmor());
		armorStatus.setString(String.valueOf(s.getArmor()));
		armorStatus.setStringPainted(true);
		armorStatus.setValue(s.getArmor());
		hullOxygen = new JLabel(s.hullOxygen*100 + "%",oxygenIcon,JLabel.LEFT);
		
		JLabel scrap = new JLabel();
		scrap.setText("Scrap: " + s.getScrap());
		WeapStats = new JButton("Weapon System Status");
		WeapStats.setToolTipText("See the status of your guns and aiming system.");
		WeapStats.addActionListener(Handler);
		if (s.Weapon.health<=0)
		{
			WeapStats.setEnabled(false);
		}
		
		statusPanel1.add(armor);
		statusPanel1.add(armorStatus);
		statusPanel1.add(hullOxygen);
		statusPanel2.add(scrap);
		statusPanel2.add(WeapStats);
		
		statusPanel.add(statusPanel1);
		statusPanel.add(statusPanel2);
	}
	
	private void createCrewPanel(Ship s)
	{
		crewPanel = new JPanel();
		Border border = BorderFactory.createTitledBorder("Crew Controls");
		crewPanel.setBorder(border);
		crewPanel.setLayout(new BoxLayout(crewPanel,BoxLayout.Y_AXIS));
		
		JPanel crewPanel1 = new JPanel(new FlowLayout());
		JPanel crewPanel2 = new JPanel(new FlowLayout());
		JPanel crewPanel3 = new JPanel(new FlowLayout());
		JPanel crewPanel4 = new JPanel(new FlowLayout());
		JPanel crewPanel5 = new JPanel(new FlowLayout());
		JPanel crewPanel6 = new JPanel(new FlowLayout());
		
		crewAlive = new JLabel("Crew Remaining Alive: " + s.getAliveCrew() + " / " + s.initCrewLength);
		CrewList = new JButton("Full Crew List");
		CrewList.addActionListener(Handler);
		
		JLabel shortcutCrew = new JLabel("Crew allocation shortcuts:");
		ManShip = new JButton("Man the Ship");
		ManShip.setToolTipText("Stock all systems with the optimized number of crew.");
		ManShip.addActionListener(Handler);
		HalfRepair = new JButton("1/2 Repairs");
		HalfRepair.setToolTipText("Suspend half of all repairs, rounding up, per system.");
		HalfRepair.addActionListener(Handler);
		EndRepair = new JButton("Hold All Repairs");
		EndRepair.setToolTipText("Suspend all repairs.");
		EndRepair.addActionListener(Handler);
		
		JLabel chooseCrew = new JLabel("Which part would you like to modify crew for:");
		cParts = new JComboBox(cPartNames);
		cParts.addItemListener(Handler);
		cWeapsCrew = new String[s.Weapon.Weapons.length];
		for (int i=0;i<cWeapsCrew.length;i++)
		{
			cWeapsCrew[i]=s.Weapon.Weapons[i].name;
		}
		cPartsWeap = new JComboBox(cWeapsCrew);
		cPartsWeap.setSelectedIndex(s.Weapon.weapRepairIndex);
		cPartsWeap.addItemListener(Handler);
		cPartsWeap.setEnabled(false);
		
		lCrew = new JLabel("Available Crew: " + s.getAvailableCrew() + "  |  Crew used in selected part: " + s.getPartNum(cParts.getSelectedIndex()).getCrewNum() + "/" + s.getPartNum(cParts.getSelectedIndex()).getEngineersNeeded());
		ResetCrew = new JButton("Free Assigned Crew");
		ResetCrew.setToolTipText("Free all crew that are assigned to the selected part.");
		ResetCrew.addActionListener(Handler);
		StopRepair = new JButton("Stop Repairs");
		StopRepair.setToolTipText("Stop all repairs in selected part.");
		StopRepair.addActionListener(Handler);
		
		JLabel crewAmount = new JLabel("How many crew members to send:");
		if (s.getAvailableCrew()<=50)
			{crewSlider = new RichSlider(this,0,s.getAvailableCrew(),0,5,1,false);}
		else if (s.getAvailableCrew()<=100)	
			{crewSlider = new RichSlider(this,0,s.getAvailableCrew(),0,10,2,false);}
		else
			{crewSlider = new RichSlider(this,0,s.getAvailableCrew(),0,25,5,false);}
		
		JLabel useCrew = new JLabel("What to do with selected crew:");
		Crew = new JButton("Assign Crew");
		Crew.setToolTipText("Assign selected amount of crew to selected part.");
		Crew.addActionListener(Handler);
		Repair = new JButton("Assign to Repair");
		Repair.setToolTipText("Assign selected amount of crew to repair selected part.");
		Repair.addActionListener(Handler);
		
		crewPanel1.add(crewAlive);
		crewPanel1.add(CrewList);
		crewPanel2.add(shortcutCrew);
		crewPanel2.add(ManShip);
		crewPanel2.add(HalfRepair);
		crewPanel2.add(EndRepair);
		crewPanel3.add(chooseCrew);
		crewPanel3.add(cParts);
		crewPanel3.add(cPartsWeap);
		crewPanel4.add(lCrew);
		crewPanel4.add(ResetCrew);
		crewPanel4.add(StopRepair);
		crewPanel5.add(crewAmount);
		crewPanel5.add(crewSlider);
		crewPanel6.add(useCrew);
		crewPanel6.add(Crew);
		crewPanel6.add(Repair);
		//crewPanel5.add(FreeCrew);
		
		crewPanel.add(crewPanel1);
		crewPanel.add(crewPanel2);
		crewPanel.add(crewPanel3);
		crewPanel.add(crewPanel4);
		crewPanel.add(crewPanel5);
		crewPanel.add(crewPanel6);
		
		statusPanel.add(crewPanel);
	}
	
	private void createSensorsPanel(Ship s, Ship s2)
	{
		sensorsPanel = new JPanel();
		sensorsPanel.setLayout(new BoxLayout(sensorsPanel,BoxLayout.Y_AXIS));
		
		sensorsStatus = new SystemStatusPanel(s, 5);
		sensorsExtinguishPanel = new SystemExtinguishPanel(s,5,state);
		JPanel sensorsPanel3 = new JPanel(new FlowLayout());
		JPanel sensorsPanel4 = new JPanel(new FlowLayout());
		JPanel sensorsPanel5 = new JPanel(new FlowLayout());
		
		JPanel sensorsMain = new JPanel(new FlowLayout());
		JPanel sensorsMain1 = new JPanel();
		Border border = BorderFactory.createTitledBorder("Opponent's Systems Reports");
		sensorsMain1.setBorder(border);
		sensorsMain1.setLayout(new BoxLayout(sensorsMain1, BoxLayout.Y_AXIS));
		JPanel sensorsMain2 = new JPanel();
		sensorsMain2.setLayout(new BoxLayout(sensorsMain2, BoxLayout.Y_AXIS));
		
		//Add all system reporting to one of the side-by-side panels and left-align them.
		JPanel[] sensorsMain1Panels = new JPanel[7];
		for (int i=0;i<sensorsMain1Panels.length;i++)
		{
			sensorsMain1Panels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
			sensorsMain1Panels[i].setAlignmentX(LEFT_ALIGNMENT);
			sensorsMain1.add(sensorsMain1Panels[i]);
		}
    
		sensorsAmount = new JLabel("How much energy to allocate for sensors (max of " + s.getMaxSensors() + " energy):");
		sensorsSlider = new RichSlider(this,0,s.getMaxSensors(),0,false);
		
		JLabel lock = new JLabel("Lock in your energy and crew allocation when you are ready. ");
		lockEnergy = new JButton("Lock Sensors Energy");
		lockEnergy.addActionListener(Handler);
		lockCrew = new JButton("Lock Sensors Crew");
		lockCrew.addActionListener(Handler);
		
		//Opponet's armor reporting; you always get this info.
		JLabel sensHealthL = new JLabel("Opponent's Ship Armor status:");
		JLabel sensHealth = s.Sensors.sensHealth(s2.getArmor(), s2.getInitArmor(), false);
		
		//System reporting components.
		JLabel sensPowerL = new JLabel("Reactor status:");
		sensPower = s.Sensors.sensHealth(s2.Power.health, s2.Power.initHealth, true);
		JLabel sensShieldL = new JLabel("Shield Generator status:");
		sensShield = s.Sensors.sensHealth(s2.Shield.health, s2.Shield.initHealth, true);
		JLabel sensEngineL = new JLabel("Engine status:");
		sensEngine = s.Sensors.sensHealth(s2.Engine.health, s2.Engine.initHealth, true);
		JLabel sensWeaponSysL = new JLabel("Weapon System status:");
		sensWeaponSys = s.Sensors.sensHealth(s2.Weapon.health, s2.Weapon.initHealth, true);
		JLabel sensLifeSupportL = new JLabel("Life Support status:");
		sensLifeSupport = s.Sensors.sensHealth(s2.LifeSupport.health, s2.LifeSupport.initHealth, true);
		JLabel sensMedBayL = new JLabel("Medical Bay status:");
		sensMedBay = s.Sensors.sensHealth(s2.MedBay.health, s2.MedBay.initHealth, true);
		JLabel sensSensorsL = new JLabel("Sensors status:");
		sensSensors = s.Sensors.sensHealth(s2.Sensors.health, s2.Sensors.initHealth, true);
		
		//Additional reporting components.
		
		
		//Add all components to their respective container panels.
		sensorsPanel3.add(sensorsAmount);
		sensorsPanel3.add(sensorsSlider);
		sensorsPanel4.add(lock);
		sensorsPanel4.add(lockEnergy);
		sensorsPanel4.add(lockCrew);
		
		sensorsPanel5.add(sensHealthL);
		sensorsPanel5.add(sensHealth);
		
		sensorsMain1Panels[0].add(sensPowerL);
		sensorsMain1Panels[0].add(sensPower);
		sensorsMain1Panels[1].add(sensShieldL);
		sensorsMain1Panels[1].add(sensShield);
		sensorsMain1Panels[2].add(sensEngineL);
		sensorsMain1Panels[2].add(sensEngine);
		sensorsMain1Panels[3].add(sensWeaponSysL);
		sensorsMain1Panels[3].add(sensWeaponSys);
		sensorsMain1Panels[4].add(sensLifeSupportL);
		sensorsMain1Panels[4].add(sensLifeSupport);
		sensorsMain1Panels[5].add(sensMedBayL);
		sensorsMain1Panels[5].add(sensMedBay);
		sensorsMain1Panels[6].add(sensSensorsL);
		sensorsMain1Panels[6].add(sensSensors);
		
		sensorsMain.add(sensorsMain1);
		sensorsMain.add(sensorsMain2);
		
		sensorsPanel.add(sensorsStatus);
		sensorsPanel.add(sensorsExtinguishPanel);
		sensorsPanel.add(sensorsPanel3);
		sensorsPanel.add(sensorsPanel4);
		sensorsPanel.add(sensorsPanel5);
		sensorsPanel.add(sensorsMain);
	}
	
	private void createWeaponPanel(Ship s)
	{
		weapPanel = new JPanel();
		weapPanel.setLayout(new BoxLayout(weapPanel,BoxLayout.Y_AXIS));
		
		weapStatus = new SystemStatusPanel(s, 3);
		weapExtinguishPanel = new SystemExtinguishPanel(s,3,state);
		JPanel weaponsPanel3 = new JPanel(new FlowLayout());
		JPanel weaponsPanel4 = new JPanel(new FlowLayout());
		JPanel weaponsPanel5 = new JPanel(new FlowLayout());
		
		String str;
		if (s.Weapon.Weapons[Handler.weapNum].bInfiniteAmmo)
		{
			str = "Current Weapon System: ";
		}
		else
		{
			str = "Current Weapon System (" + s.Weapon.Weapons[Handler.weapNum].ammo + " ammo left): ";
		}
		sWeapon = new JLabel(str);
		cWeaps = new JComboBox(s.Weapon.getStringArray());
		cWeaps.setToolTipText("Which weapon is selected");
		cWeaps.addItemListener(Handler);
		
		gunsToFire = new JLabel("Weapons to fire this turn (max of " + s.getNumGunsMax(Handler.weapNum) + " guns):");
		weapSlider = new RichSlider(this,0,s.Weapon.getGunCount(Handler.weapNum),0,true);
		
		JLabel weapTargeting = new JLabel("Primary Target for this Weapon System:");
		cWeapons = new JComboBox(cNames);
		cWeapons.addItemListener(Handler);
		cWeapons.setSelectedIndex(s.Weapon.Weapons[Handler.weapNum].aim+1);
		
		weaponsPanel3.add(sWeapon);
		weaponsPanel3.add(cWeaps);
		weaponsPanel4.add(gunsToFire);
		weaponsPanel4.add(weapSlider);
		weaponsPanel5.add(weapTargeting);
		weaponsPanel5.add(cWeapons);
		
		weapPanel.add(weapStatus);
		weapPanel.add(weapExtinguishPanel);
		weapPanel.add(weaponsPanel3);
		weapPanel.add(weaponsPanel4);
		weapPanel.add(weaponsPanel5);
		
		/*
		 * Crew Panel
		 * */
		weapCrewPanel = new CrewPanel(s,3,state);
		weapPanel.add(weapCrewPanel);
	}
	
	private void createShieldsPanel(Ship s)
	{
		shieldPanel = new JPanel();
		shieldPanel.setLayout(new BoxLayout(shieldPanel,BoxLayout.Y_AXIS));
		
		shieldStatus = new SystemStatusPanel(s, 1);
		shieldExtinguishPanel = new SystemExtinguishPanel(s,1,state);
		JPanel shieldsPanel3 = new JPanel(new FlowLayout());
		JPanel shieldsPanel4 = new JPanel(new FlowLayout());
		JPanel shieldsPanel5 = new JPanel(new FlowLayout());

		shieldAmount = new JLabel("How much energy to allocate for shields (max of " + s.getMaxShields() + " energy):");
		shieldSlider = new RichSlider(this,0,s.getMaxShields(),0,true);
		
		JLabel target = new JLabel("Shield Protection Targets (in order of importance):");
		cShield = new JComboBox(cNames);
		cShield.setToolTipText("Which system to protect with shields first.");
		cShield.addItemListener(Handler);
		cShield.setSelectedIndex(s.Shield.getConcentration(0)+1);
		
		cShield2 = new JComboBox(cNames);
		cShield2.setToolTipText("Which system to protect with shields second.");
		cShield2.addItemListener(Handler);
		cShield2.setSelectedIndex(s.Shield.getConcentration(1)+1);
		if (cShield.getSelectedIndex()<1)
		{
			cShield2.setSelectedIndex(0);
			cShield2.setEnabled(false);
		}
		
		cShield3 = new JComboBox(cNames);
		cShield3.setToolTipText("Which system to protect with shields third.");
		cShield3.addItemListener(Handler);
		cShield3.setSelectedIndex(s.Shield.getConcentration(2)+1);
		if (cShield2.getSelectedIndex()<1 || !cShield2.isEnabled())
		{
			cShield3.setSelectedIndex(0);
			cShield3.setEnabled(false);
		}
		
		JLabel shieldSpecial = new JLabel("Special actions:");
		OverloadShields = new JButton("Overload Shield Generator!");
		OverloadShields.setToolTipText("Increase shield capacity but generator takes damage every turn.");
		OverloadShields.addActionListener(Handler);
		if (!s.Shield.canOverload())
		{
			OverloadShields.setEnabled(false);
		}
		else
		{
			OverloadShields.setEnabled(true);
		}
		HardResetShields = new JButton("Hard Reset (Cancel Overload)");
		HardResetShields.setToolTipText("Initiate a hard reset of the shield generator, canceling the overload protocol.");
		HardResetShields.addActionListener(Handler);
		if (!s.Shield.isOverloading)
		{
			HardResetShields.setEnabled(false);
		}
		else
		{
			HardResetShields.setEnabled(true);
		}
		
		shieldsPanel3.add(shieldAmount);
		shieldsPanel3.add(shieldSlider);
		shieldsPanel4.add(target);
		shieldsPanel4.add(cShield);
		shieldsPanel4.add(cShield2);
		shieldsPanel4.add(cShield3);
		shieldsPanel5.add(shieldSpecial);
		shieldsPanel5.add(OverloadShields);
		shieldsPanel5.add(HardResetShields);
		
		shieldPanel.add(shieldStatus);
		shieldPanel.add(shieldExtinguishPanel);
		shieldPanel.add(shieldsPanel3);
		shieldPanel.add(shieldsPanel4);
		shieldPanel.add(shieldsPanel5);
		
		/*
		 * Crew Panel
		 * */
	
		shieldCrewPanel = new CrewPanel(s,1,state);
		
		shieldPanel.add(shieldCrewPanel);
	}
	
	private void createAltShieldsPanel()
	{
		shieldPanel = new JPanel();
		shieldPanel.setLayout(new BoxLayout(shieldPanel,BoxLayout.Y_AXIS));
		
		JPanel shieldsPanel1 = new JPanel(new FlowLayout());
		
		JLabel d = new JLabel("Your Shield Generator has been completely destroyed and can no longer be used.");
		
		shieldsPanel1.add(d);
		
		shieldPanel.add(shieldsPanel1);
	}

	private void createEnginePanel(Ship s)
	{
		enginePanel = new JPanel();
		enginePanel.setLayout(new BoxLayout(enginePanel,BoxLayout.Y_AXIS));
		
		engineStatus = new SystemStatusPanel(s, 2);
		engineExtinguishPanel = new SystemExtinguishPanel(s,2,state);
		JPanel enginePanel3 = new JPanel(new FlowLayout());
		JPanel enginePanel4 = new JPanel(new FlowLayout());
		JPanel enginePanel5 = new JPanel(new FlowLayout());
		
		engineAmount = new JLabel("How much energy to allocate for propulsion (max of " + s.getMaxEngines() + " energy):");
		engineSlider = new RichSlider(this,0,s.getMaxEngines(),0,true);
		engineSlider.setMajorTickSpacing(s.getMaxEngines()/5);
		engineSlider.setMinorTickSpacing(s.getMaxEngines()/25);
		
		evade = new JLabel("Evade %: " + s.getEvade() + "%");
		
		JLabel specialEngines = new JLabel("Special actions:");
		RamEngine = new JButton("Ram Enemy Ship");
		RamEngine.setToolTipText("Initiate a ramming sequence; if successful, the match is a tie, otherwise engines are offline until the reset.");
		RamEngine.addActionListener(Handler);
		if (!s.Engine.canRam())
		{
			RamEngine.setEnabled(false);
		}
		else
		{
			RamEngine.setEnabled(true);
		}
		
		enginePanel3.add(engineAmount);
		enginePanel3.add(engineSlider);
		enginePanel4.add(evade);
		enginePanel5.add(specialEngines);
		enginePanel5.add(RamEngine);
		
		enginePanel.add(engineStatus);
		enginePanel.add(engineExtinguishPanel);
		enginePanel.add(enginePanel3);
		enginePanel.add(enginePanel4);
		enginePanel.add(enginePanel5);
		
		/*
		 * Crew Panel
		 * */
		engineCrewPanel = new CrewPanel(s,2,state);
		enginePanel.add(engineCrewPanel);
	}
	
	private void createReactorPanel(Ship s)
	{
		reactorPanel = new JPanel();
		reactorPanel.setLayout(new BoxLayout(reactorPanel,BoxLayout.Y_AXIS));
		
		reactorStatus = new SystemStatusPanel(s, 0);
		reactorExtinguishPanel = new SystemExtinguishPanel(s,0,state);
		JPanel reactorPanel3 = new JPanel(new FlowLayout());
		JPanel reactorPanel4 = new JPanel(new FlowLayout());
		JPanel reactorPanel5 = new JPanel(new FlowLayout());
		
		AvailableEnergy = new JLabel("Battery Storage: " + s.Power.getEnergyStored() + " / " + s.Power.getEnergyStoredMax() + "  ///  Available Energy (Battery - Usage): " + s.getAvailableEnergy());
		EnergyUse = new JButton("Energy Usage");
		EnergyUse.setToolTipText("A breakdown of energy usage");
		EnergyUse.addActionListener(Handler);
		
		energyProd = new JLabel("Energy produced this turn (assuming no reactor damage is taken): " + s.Power.getEnergyProduced());
		
		JLabel specialReactor = new JLabel("Special actions:");
		OverloadReactor = new JButton("Overload Reactor!");
		OverloadReactor.addActionListener(Handler);
		if (!s.Power.canOverload())
		{
			OverloadReactor.setEnabled(false);
		}
		else
		{
			OverloadReactor.setEnabled(true);
		}
		CooldownReactor = new JButton("Full Cooldown (Cancel Overload)");
		CooldownReactor.setToolTipText("Initiate a full cooldown of the reactor, canceling the overload protocol.");
		CooldownReactor.addActionListener(Handler);
		if (!s.Power.isOverloading)
		{
			CooldownReactor.setEnabled(false);
		}
		else
		{
			CooldownReactor.setEnabled(true);
		}
		
		reactorPanel3.add(AvailableEnergy);
		reactorPanel3.add(EnergyUse);
		reactorPanel4.add(energyProd);
		reactorPanel5.add(specialReactor);
		reactorPanel5.add(OverloadReactor);
		reactorPanel5.add(CooldownReactor);
		
		reactorPanel.add(reactorStatus);
		reactorPanel.add(reactorExtinguishPanel);
		reactorPanel.add(reactorPanel3);
		reactorPanel.add(reactorPanel4);
		reactorPanel.add(reactorPanel5);
		
		/*
		 * Crew Panel
		 */
		reactorCrewPanel = new CrewPanel(s,0,state);
		reactorPanel.add(reactorCrewPanel);
	}
	
	private void createAltReactorPanel()
	{
		reactorPanel = new JPanel();
		reactorPanel.setLayout(new BoxLayout(reactorPanel,BoxLayout.Y_AXIS));
		
		JPanel reactorPanel1 = new JPanel(new FlowLayout());
		
		JLabel d = new JLabel("Your Reactor has been completely destroyed and can no longer be used.");
		
		reactorPanel1.add(d);
		
		reactorPanel.add(reactorPanel1);
	}

	private void createLifeSupportPanel(Ship s)
	{
		lifeSupportPanel = new JPanel();
		lifeSupportPanel.setLayout(new BoxLayout(lifeSupportPanel,BoxLayout.Y_AXIS));
		
		lifeSupportStatus = new SystemStatusPanel(s, 4);
		lifeSupportExtinguishPanel = new SystemExtinguishPanel(s,4,state);
		JPanel lsPanel3 = new JPanel(new FlowLayout());
		JPanel lsPanel4 = new JPanel(new FlowLayout());
		
		lifeSupportAmount = new JLabel("How much energy to allocate for life support (max of " + s.getMaxLifeSupport() + " energy):");
		lifeSupportSlider = new RichSlider(this,0,s.getMaxLifeSupport(),0,s.getMaxLifeSupport()/5,s.getMaxLifeSupport()/25,true);
		lifeSupportSlider.setValue(s.LifeSupport.getEnergy());
		
		oxygenFillRate = new JLabel("Current oxygen fill rate: " + s.LifeSupport.getRefill()*100 + "%.");
		
		lsPanel3.add(lifeSupportAmount);
		lsPanel3.add(lifeSupportSlider);
		lsPanel4.add(oxygenFillRate);
		
		lifeSupportPanel.add(lifeSupportStatus);
		lifeSupportPanel.add(lifeSupportExtinguishPanel);
		lifeSupportPanel.add(lsPanel3);
		lifeSupportPanel.add(lsPanel4);
		
		/*
		 * Crew Panel
		 * */
		lifeSupportCrewPanel = new CrewPanel(s,4,state);
		lifeSupportPanel.add(lifeSupportCrewPanel);
	}
	
	private void createMedBayPanel(Ship s)
	{
		medBayPanel = new JPanel();
		medBayPanel.setLayout(new BoxLayout(medBayPanel,BoxLayout.Y_AXIS));
		
		medBayStatus = new SystemStatusPanel(s, 6);
		medBayExtinguishPanel = new SystemExtinguishPanel(s,6,state);
		JPanel medBayPanel3 = new JPanel(new FlowLayout());
		JPanel medBayPanel4 = new JPanel(new FlowLayout());
		JPanel medBayPanel5 = new JPanel(new FlowLayout());
		JPanel medBayPanel6 = new JPanel(new FlowLayout());
		
		availableMedics = new JLabel("Medics available: " + s.getAvailableMedics());
		medicList = new JButton("Full Medic List");
		
		JLabel bonus = new JLabel("Healing Multiplier (derived from system health): " + s.MedBay.getMultiplier()*100 + "%");
		
		treatedCrew = new JLabel("Number of injured crew in Medical Bay: " + s.MedBay.getInjured().size());
		
		JLabel thresh = new JLabel("Health to return injured to work:");
		medBayThreshold = new RichSlider(this,50,100,s.MedBay.threshold,true);
		
		medBayPanel3.add(availableMedics);
		medBayPanel3.add(medicList);
		medBayPanel4.add(bonus);
		medBayPanel5.add(treatedCrew);
		medBayPanel6.add(thresh);
		medBayPanel6.add(medBayThreshold);
		
		medBayPanel.add(medBayStatus);
		medBayPanel.add(medBayExtinguishPanel);
		medBayPanel.add(medBayPanel3);
		medBayPanel.add(medBayPanel4);
		medBayPanel.add(medBayPanel5);
		medBayPanel.add(medBayPanel6);
	}
	
	public void startGame()
	{
		String[] announcer = {"And the stage is set for another nail-biting, possibly broken game of Spacehip Commander as the " + ship1.fancyName + " faces off against the " + ship2.fancyName + ".",
				"Two ships enter, the " + ship1.fancyName + " and the " + ship2.fancyName +	", one will emerge the victor - THIS is Spaceship Commander!",
				"This is sure to be an exciting match of Spaceship Commander between the " + ship1.fancyName + " and the " +	ship2.fancyName + "!"};
		
		String s = announcer[(int)(Math.random()*announcer.length)];
		JOptionPane.showMessageDialog(null, s);
		if (timerEnabled)
		{
			timerStep.start();
		}
		state.gameStart=true;
	}
	
	public void endTurn()
	{
		if (timerEnabled)
		{
			timerStep.stop();
			timeStep=roundTime/1000;
		}
		
		//calculate and apply weapon damage to both ships, then report it
		damageReport();		
		
		//Overload damage
		overloadStep();
		
		//Check ramming
		ramStep();
		
		//report dead crew
		crewStep();
		
		//report finished repairs
		repairGameStep();
		
		//this is for reseting misc reporting variables and ship variables
		ship1.endStep();
		ship2.endStep();
		
		//End game if one or more players get destroyed
		if (ship1.getArmor()<=0 || ship2.getArmor()<=0)
		{
			if (ship1.getArmor()>ship2.getArmor() && ship1.getArmor()>0)
			{
				end(0);
			}
			else if(ship1.getArmor()<ship2.getArmor() && ship2.getArmor()>0)
			{
				end(1);
			}
			else
			{
				end(2);
			}
		}
		
		if (ship1.getAliveCrew()<=0 || ship2.getAliveCrew()<=0)
		{
			if (ship1.getAliveCrew()>0 && ship2.getAliveCrew()<=0)
			{
				end(0);
			}
			else if(ship2.getAliveCrew()>0 && ship1.getAliveCrew()<=0)
			{
				end(1);
			}
			else
			{
				end(2);
			}
		}
		
		this.removeAll();
		
		turnCount++;
		initGUI(ship1, ship2);
	}
	
	public void damageReport()
	{
		//The ships calculate their weapons' damage to the other ship in the form of an array, then they pass that array to the other ship for it to take damage.
		
		Damage[] d1=ship1.calcDamage(ship2.Weapon.getTotalGunCount(), ship2.getEvade());
		Damage[] d2=ship2.calcDamage(ship1.Weapon.getTotalGunCount(), ship1.getEvade());
		Damage[] fd2 = ship1.takeDamage(d2);
		Damage[] fd1 = ship2.takeDamage(d1);
				
		//Set all weapons to not firing
		ship1.Weapon.lastStep();
		ship2.Weapon.lastStep();
	}
	
	public void overloadStep()
	{
		if (ship1.dPower && !ship1.Power.isDestroyed)
		{
			int i = ship1.overloadRamCrewShort(0);
			JOptionPane.showMessageDialog(null, "Player 1's reactor was destroyed while overloaded, causing " + i + " damage to their ship and killing all crew assigned.");
		}
		if (ship2.dPower && !ship2.Power.isDestroyed)
		{
			int i = ship2.overloadRamCrewShort(0);
			JOptionPane.showMessageDialog(null, "Player 2's reactor was destroyed while overloaded, causing " + i + " damage to their ship and killing all crew assigned.");
		}
		
		if (ship1.dShield && !ship1.Shield.isDestroyed)
		{
			int i = ship1.overloadRamCrewShort(1);			
			JOptionPane.showMessageDialog(null, "Player 1's shield generator was destroyed while overloaded, causing " + i + " damage to their ship and killing all crew assigned.");
		}
		if (ship2.dShield && !ship1.Shield.isDestroyed)
		{
			int i = ship2.overloadRamCrewShort(1);	
			JOptionPane.showMessageDialog(null, "Player 1's shield generator was destroyed while overloaded, causing " + i + " damage to their ship and killing all crew assigned.");
		}
		
		boolean[] s1 = ship1.checkCrewOverload();
		boolean[] s2 = ship2.checkCrewOverload();
		
		if (s1!=null)
		{
			if (s1[0])
			{
				int i = ship1.overloadRamCrewShort(0);
				JOptionPane.showMessageDialog(null, "Player 1's reactor did not have enough crew to sustain the overload process, it exploded causing " + i + " damage!");
			}
			if (s1[1])
			{
				int i = ship1.overloadRamCrewShort(1);
				JOptionPane.showMessageDialog(null, "Player 1's shield generator did not have enough crew to sustain the overload process, it exploded causing " + i + " damage!");
			}
			if (s1[2])
			{
				JOptionPane.showMessageDialog(null, "Player 1's engines did not have enough crew to sustain the ramming process, it has started a system reset!");
				ship1.Engine.initializeReset();
			}
		}
		if (s2!=null)
		{
			if (s2[0])
			{
				int i = ship2.overloadRamCrewShort(0);
				JOptionPane.showMessageDialog(null, "Player 2's reactor did not have enough crew to sustain the overload process, it exploded causing " + i + " damage!");
			}
			if (s2[1])
			{
				int i = ship2.overloadRamCrewShort(1);
				JOptionPane.showMessageDialog(null, "Player 2's shield generator did not have enough crew to sustain the overload process, it exploded causing " + i + " damage!");
			}
			if (s2[2])
			{
				JOptionPane.showMessageDialog(null, "Player 2's engines did not have enough crew to sustain the ramming process, it has started a system reset!");
				ship2.Engine.initializeReset();
			}
		}
	}
	
	public void ramStep()
	{
		int r1 = ship1.ramStep(ship2.getEvade());
		int r2 = ship2.ramStep(ship1.getEvade());
		
		if (r1==1)
		{
			JOptionPane.showMessageDialog(null, "Player 1 has successfully rammed their ship into Player 2's ship!");
			ship1.setArmor(0);
			ship2.setArmor(0);
		}
		
		if (r1==0)
		{
			JOptionPane.showMessageDialog(null, "Player 1's ramming sequence missed Player 2!  Player 1's engines are dead in the water until they reset!");
		}
		
		if (r2==1)
		{
			JOptionPane.showMessageDialog(null, "Player 2 has successfully rammed their ship into Player 1's ship!");
			ship1.setArmor(0);
			ship2.setArmor(0);
		}
		
		if (r1==0)
		{
			JOptionPane.showMessageDialog(null, "Player 2's ramming sequence missed Player 1!  Player 2's engines are dead in the water until they reset!");
		}
	}
	
	public void repairGameStep()
	{
		ship1.repairStep();
		ship2.repairStep();
		
		StringBuffer s1 = new StringBuffer("Player 1's Repair Report:\n");
		StringBuffer s2 = new StringBuffer("Player 2's Repair Report:\n");
		
		if (ship1.rPower)
		{
			s1.append("Reactor repairs complete.\n");
			ship1.rPower=false;
		}
		if (ship2.rPower)
		{
			s2.append("Reactor repairs complete.\n");
			ship2.rPower=false;
		}
		if (ship1.rShield)
		{
			s1.append("Shield Generator repairs complete.\n");
			ship1.rShield=false;
		}
		if (ship2.rShield)
		{
			s2.append("Shield Generator repairs complete.\n");
			ship2.rShield=false;
		}
		if (ship1.rEngine)
		{
			s1.append("Engine repairs complete.\n");
			ship1.rEngine=false;
		}
		if (ship2.rEngine)
		{
			s2.append("Engine repairs complete.\n");
			ship2.rEngine=false;
		}
		if (ship1.rWeapon)
		{
			s1.append("Weapon System repairs complete.\n");
			ship1.rWeapon=false;
		}
		if (ship2.rWeapon)
		{
			s2.append("Weapon System repairs complete.\n");
			ship2.rWeapon=false;
		}
		if (ship1.rLifeSupport)
		{
			s1.append("Life Support repairs complete.\n");
			ship1.rLifeSupport=false;
		}
		if (ship2.rLifeSupport)
		{
			s2.append("Life Support repairs complete.\n");
			ship2.rLifeSupport=false;
		}
		
		if (s1.toString().equals("Player 1's Repair Report:\n"))
		{
			s1.append("No repairs finished.");
		}
		if (s2.toString().equals("Player 2's Repair Report:\n"))
		{
			s2.append("No repairs finished.");
		}
		
		JOptionPane.showMessageDialog(null, s1.toString());
		JOptionPane.showMessageDialog(null, s2.toString());
	}
	
	public void crewStep()
	{
		ArrayList<crew.Crew> c1=ship1.crewCheck();
		ArrayList<crew.Crew> c2=ship2.crewCheck();
		
		if (c1.size()>0)
		{
			StringBuffer s = new StringBuffer();
			for (crew.Crew c : c1)
			{
				s.append(c.toStringDead()+"\n");
			}
			JOptionPane.showMessageDialog(null, "The following " + c1.size() + " crew member(s) have died in battle on Player 1's ship:\n" + s);
		}
		
		if (c2.size()>0)
		{
			StringBuffer s = new StringBuffer();
			for (crew.Crew c : c2)
			{
				s.append(c.toStringDead()+"\n");
			}
			JOptionPane.showMessageDialog(null, "The following " + c2.size() + " crew members have died in battle on Player 2's ship:\n" + s);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void end(int w)
	{
		if (w==0)
		{
			JOptionPane.showMessageDialog(null, "Player 1 wins!");
		}
		else if(w==1)
		{
			JOptionPane.showMessageDialog(null, "Player 2 wins!");
		}
		else if(w==2)
		{
			JOptionPane.showMessageDialog(null, "It is a tie!");
		}
		
		state.gameStart=false;
		
		int n = JOptionPane.showOptionDialog(null, "After " + turnCount + " turns, Player 1 had " + ship1.getArmor() + " armor remaining and " + ship1.crew.size() + " crew remaining, and player 2 had " + ship2.getArmor() + " armor remaining and "
				+ ship2.crew.size() + " crew remaining.\n Would you like to start a new match?", "Rematch", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (n==JOptionPane.YES_OPTION)
		{
			if (timerEnabled)
			{
				timerStep.stop();
			}
			Tools.clearConsole();
			cmdLine.stop();
			state.goToMenu();
		}
		else if (n==JOptionPane.NO_OPTION)
		{
			Tools.clearConsole();
			cmdLine.stop();
			state.close();
		}
	}
	
	public void update()
	{
		if (state.gameStart) {
			Ship s;
			if (turn) {
				s = ship1;
			}
			else {
				s = ship2;
			}
			
			updateCommandAndControlPanel(s);
			
			updateMedbayPanel(s);
			
			updateLifeSupportPanel(s);
			
			updateWeaponsPanel(s);
			
			updateSensorsPanel(s);
			
			updateShieldPanel(s);
			
			updateEnginePanel(s);
			
			updateReactorPanel(s);
			
			validate();
			repaint();
		}
	}
	
	public void updateCommandAndControlPanel(Ship s) {
		if (cParts.getSelectedIndex()!=6) {
			ResetCrew.setEnabled(true);
			Crew.setEnabled(true);
			
			if (s.getPartNum(cParts.getSelectedIndex()).getCrewNum()>0) {
				ResetCrew.setEnabled(true);
			}
			else {
				ResetCrew.setEnabled(false);
			}
			
			if (s.getPartNum(cParts.getSelectedIndex()).getRepairCrewNum()>0) {
				StopRepair.setEnabled(true);
			}
			else {
				StopRepair.setEnabled(false);
			}
			
			if (s.getPartNum(cParts.getSelectedIndex()).canRepair(s.getScrap())) {
				Repair.setEnabled(true);
			}
			else {
				Repair.setEnabled(false);
			}
			
			if (cParts.getSelectedIndex()==5 && s.Sensors.isLockedCrew()) {
				Crew.setEnabled(false);
			}
			else {
				Crew.setEnabled(true);
			}
		}
		else {
			ResetCrew.setEnabled(false);
			Crew.setEnabled(false);
			
			if (s.Weapon.getWeapRepairCrewNum()>0) {
				StopRepair.setEnabled(true);
			}
			else {
				StopRepair.setEnabled(false);
			}
			
			if (s.Weapon.canRepairWeap(s.getScrap())) {
				Repair.setEnabled(true);
			}
			else {
				Repair.setEnabled(false);
			}
		}
		
		crewAlive.setText("Crew Remaining Alive: " + s.getAliveCrew() + "/" + s.initCrewLength);
		
		if (cParts.getSelectedIndex()!=6)
		{
			lCrew.setText("Available Crew: " + s.getAvailableCrew() + "  |  Crew used in selected part: " + s.getPartNum(cParts.getSelectedIndex()).getCrewNum() + "/" + s.getPartNum(cParts.getSelectedIndex()).getEngineersNeeded());
		}
		else
		{
			lCrew.setText("Available Crew: " + s.getAvailableCrew() + "  |  Crew used in Weapon Repairs: " + s.Weapon.getWeapRepairCrewNum());
		}
		crewSlider.setMaximum(s.getAvailableCrew());
//		if (!Handler.updateCrewSelect)
//		{
//			crewSlider.setValue(0);
//		}
		Handler.updateCrewSelect=false;
	}
	
	public void	updateMedbayPanel(Ship s) {
		availableMedics.setText("Medics available: " + s.getAvailableMedics());
		treatedCrew.setText("Number of injured crew in Medical Bay: " + s.MedBay.getInjured().size());
		
		medBayThreshold.update();
		
		medBayStatus.update();
		medBayExtinguishPanel.update();
	}
	
	public void updateLifeSupportPanel(Ship s) {
		lifeSupportAmount.setText("How much energy to allocate for life support (max of " + s.getMaxLifeSupport() + " energy):");
		lifeSupportSlider.setMaximum(s.getMaxLifeSupport());
		lifeSupportSlider.setMajorTickSpacing(s.getMaxLifeSupport()/5);
		lifeSupportSlider.setMinorTickSpacing(s.getMaxLifeSupport()/25);
		if (s.getMaxLifeSupport()>5)
		{
			lifeSupportSlider.setLabelTable(lifeSupportSlider.createStandardLabels(s.getMaxLifeSupport()/5));
		}
		
		lifeSupportSlider.update();
		
		lifeSupportStatus.update();
		oxygenFillRate.setText("Current oxygen fill rate: " + s.LifeSupport.getRefill()*100 + "%.");
		lifeSupportExtinguishPanel.update();
		lifeSupportCrewPanel.update();
	}
	
	public void updateWeaponsPanel(Ship s) {
		String str;
		if (s.Weapon.Weapons[Handler.weapNum].bInfiniteAmmo)
		{
			str = "Current Weapon System: ";
		}
		else
		{
			str = "Current Weapon System (" + s.Weapon.Weapons[Handler.weapNum].ammo + " ammo left): ";
		}
		sWeapon.setText(str);
		gunsToFire.setText("Weapons to fire this turn (max of " + s.getNumGunsMax(Handler.weapNum) + " guns):");
		weapSlider.setMaximum(s.Weapon.getGunCount(Handler.weapNum));
		weapSlider.update();
		if (weapSlider.getValue() != s.Weapon.getNumGunsToFire(Handler.weapNum)) {			
			weapSlider.setValue(s.Weapon.getNumGunsToFire(Handler.weapNum));
		}
		Handler.updateWeaps=true;
		
		weapStatus.update();
		weapCrewPanel.update();
		weapExtinguishPanel.update();	
	}
	
	public void updateShieldPanel(Ship s) {
		shieldAmount.setText("How much energy to allocate for shields (max of " + s.getMaxShields() + " energy):");
		shieldSlider.setMaximum(s.getMaxShields());
		shieldSlider.setMajorTickSpacing(s.getMaxShields()/5);
		shieldSlider.setMinorTickSpacing(s.getMaxShields()/25);
		if (s.getMaxShields()>5)
		{
			shieldSlider.setLabelTable(shieldSlider.createStandardLabels(s.getMaxShields()/5));
		}
		
		shieldSlider.update();
		
		if (!s.Shield.canOverload())
		{
			OverloadShields.setEnabled(false);
		}
		else {
			OverloadShields.setEnabled(true);
		}
		if (!s.Shield.isOverloading) {
			HardResetShields.setEnabled(false);
		}
		else {
			HardResetShields.setEnabled(true);
		}
		shieldStatus.update();	
		shieldExtinguishPanel.update();
		shieldCrewPanel.update();
	}
		
	public void updateEnginePanel(Ship s) {
		engineAmount.setText("How much energy to allocate for propulsion (max of " + s.getMaxEngines() + " energy):");
		engineSlider.setMaximum(s.getMaxEngines());
		engineSlider.setMajorTickSpacing(s.getMaxEngines()/5);
		engineSlider.setMinorTickSpacing(s.getMaxEngines()/25);
		if (s.getMaxEngines()>5)
		{
			engineSlider.setLabelTable(engineSlider.createStandardLabels(s.getMaxEngines()/5));
		}
		
		engineSlider.update();
		
		if (!s.Engine.canRam()) {
			RamEngine.setEnabled(false);
		}
		else {
			RamEngine.setEnabled(true);
		}
		engineStatus.update();
		evade.setText("Evade %: " + s.getEvade() + "%");
		engineExtinguishPanel.update();
		engineCrewPanel.update();
	}
	public void updateSensorsPanel(Ship s) {
		sensorsExtinguishPanel.update();
	}

	public void updateReactorPanel(Ship s) {
		reactorStatus.update();
		
		AvailableEnergy.setText("Battery Storage: " + s.Power.getEnergyStored() + " / " + s.Power.getEnergyStoredMax() + "  ///  Available Energy (Battery - Usage): " + s.getAvailableEnergy());
		energyProd.setText("Energy produced this turn (assuming no reactor damage is taken): " + s.Power.getEnergyProduced());
		if (!s.Power.canOverload()) {
			OverloadReactor.setEnabled(false);
		}
		else {
			OverloadReactor.setEnabled(true);
		}
		if (!s.Power.isOverloading) {
			CooldownReactor.setEnabled(false);
		}
		else {
			CooldownReactor.setEnabled(true);
		}
		reactorExtinguishPanel.update();
		reactorCrewPanel.update();
	}
	
	public void updateSensors(Ship s, Ship s2)
	{
		JLabel sPower = s.Sensors.sensHealth(s2.Power.health, s2.Power.initHealth, true);
		JLabel sShield = s.Sensors.sensHealth(s2.Shield.health, s2.Shield.initHealth, true);
		JLabel sEngine = s.Sensors.sensHealth(s2.Engine.health, s2.Engine.initHealth, true);
		JLabel sWeaponSys = s.Sensors.sensHealth(s2.Weapon.health, s2.Weapon.initHealth, true);
		JLabel sLifeSupport = s.Sensors.sensHealth(s2.LifeSupport.health, s2.LifeSupport.initHealth, true);
		JLabel sMedBay = s.Sensors.sensHealth(s2.MedBay.health, s2.MedBay.initHealth, true);
		JLabel sSensors = s.Sensors.sensHealth(s2.Sensors.health, s2.Sensors.initHealth, true);
		
		//System reporting components.
		sensPower.setText(sPower.getText());
		sensPower.setForeground(sPower.getForeground());
		sensShield.setText(sShield.getText());
		sensShield.setForeground(sShield.getForeground());
		sensEngine.setText(sEngine.getText());
		sensEngine.setForeground(sEngine.getForeground());
		sensWeaponSys.setText(sWeaponSys.getText());
		sensWeaponSys.setForeground(sWeaponSys.getForeground());
		sensLifeSupport.setText(sLifeSupport.getText());
		sensLifeSupport.setForeground(sLifeSupport.getForeground());
		sensMedBay.setText(sMedBay.getText());
		sensMedBay.setForeground(sMedBay.getForeground());
		sensSensors.setText(sSensors.getText());
		sensSensors.setForeground(sSensors.getForeground());
		
		System.out.println("Update Sensors");
		System.out.println(s.Sensors.getEnergy());
		
		validate();
		repaint();
	}
	
	public boolean updateTimer()
	{
		if (timeStep>0)
		{
			timeStep-=1;
			lTimer.setText("Timer: " + timeStep + "     ");
			
			this.validate();
			this.repaint();
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public void richSliderUpdate(RichSlider r)
	{
		Ship s;
		if (turn)
		{
			s = ship1;
		}
		else
		{
			s = ship2;
		}
		
		if (r.equals(lifeSupportSlider))
		{
			Handler.lifeSupportSliderEvent(s);
		}
		
		else if (r.equals(shieldSlider))
		{
			Handler.shieldSliderEvent(s);
		}
		
		else if (r.equals(engineSlider))
		{
			Handler.engineSliderEvent(s);
		}
		
		else if (r.equals(medBayThreshold))
		{
			s.MedBay.threshold=medBayThreshold.getValue();
		}
		
		else if (r.equals(weapSlider) && Handler.updateWeaps)
		{
			Handler.weapSliderEvent(s);
		}
	}
	
	public void actionPerformed(ActionEvent arg0) 
	{
		boolean b = updateTimer();
		if (!b)
		{
			timerStep.restart();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "The round timer is up. You are out of time!");
			timerStep.stop();
			timeStep=roundTime/1000;
			if (turn)
			{
				this.removeAll();
				initGUI(ship2, ship1);
				return;
			}
			else
			{
				endTurn();
				return;
			}
		}
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path, String description) 
	{
	    java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null)
	    {
	        return new ImageIcon(imgURL, description);
	    } 
	    else 
	    {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}
	
	//--------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------
	private class SpaceHandler implements ActionListener, ItemListener
	{
		protected int weapNum=0;
		protected boolean updateWeaps=true,updateCrewSelect=false;
		
		public void actionPerformed(ActionEvent e)
		{
			JButton b = (JButton) e.getSource();
			
			if (b.equals(CrewList))
			{
				ArrayList<crew.Crew> crewList;
				if (turn)
				{
					crewList = new ArrayList<crew.Crew>(ship1.crew);
				}
				else
				{
					crewList = new ArrayList<crew.Crew>(ship2.crew);
				}
				crewList = Tools.sortCrew(crewList);
				
				StringBuffer str = new StringBuffer();
				for (crew.Crew c : crewList)
				{
					str.append(c.toString() + "\n");
				}
				
				JOptionPane.showMessageDialog(null, str.toString());
			}
			
			else if (b.equals(StopRepair))
			{
				if (turn){stopRepair(ship1);}
				else{stopRepair(ship2);}
			}
			
			else if (b.equals(ResetCrew))
			{
				if (turn){resetCrew(ship1);}
				else{resetCrew(ship2);}
			}
			
			else if (b.equals(Crew))
			{
				if (turn){allocateCrew(ship1);}
				else{allocateCrew(ship2);}
			}
			
			else if (b.equals(Repair))
			{
				if (turn){repair(ship1);}
				else{repair(ship2);}
			}
			
			else if (b.equals(ManShip))
			{
				if (turn){ship1.manTheShip();}
				else{ship2.manTheShip();}
			}
			
			else if (b.equals(HalfRepair))
			{
				if (turn){ship1.freeRepairCrewHalf();}
				else{ship1.freeRepairCrewHalf();}
			}
			
			else if (b.equals(EndRepair))
			{
				if (turn){ship1.freeRepairCrew();}
				else{ship2.freeRepairCrew();}
			}
			
			else if (b.equals(WeapStats))
			{
				if (turn){weapStats(ship1);}
				else{weapStats(ship2);}
			}
			
			else if (b.equals(OverloadShields))
			{
				if (turn){overloadShields(ship1);}
				else{overloadShields(ship2);}
			}
			
			else if (b.equals(OverloadReactor))
			{
				if (turn){overloadReactor(ship1);}
				else{overloadReactor(ship2);}
			}
			
			else if (b.equals(HardResetShields))
			{
				if (turn){hardResetShields(ship1);}
				else{hardResetShields(ship2);}
			}
			
			else if (b.equals(CooldownReactor))
			{
				if (turn){cooldownReactor(ship1);}
				else{cooldownReactor(ship2);}
			}
			
			else if (b.equals(RamEngine))
			{
				if (turn){ramEngine(ship1);}
				else{ramEngine(ship2);}
			}
			
			else if (b.equals(lockEnergy))
			{
				if (turn) {lockEnergy(ship1,ship2);}
				else {lockEnergy(ship2,ship1);}
			}
			
			else if (b.equals(lockCrew))
			{
				if (turn) {lockCrew(ship1,ship2);}
				else {lockCrew(ship2,ship1);}
			}
			
			else if (b.equals(sendInjuredLifeSupport))
			{
				if (turn){injuredCrew(ship1,ship1.LifeSupport,lifeSupportCrewInjured);}
				else{injuredCrew(ship2,ship2.LifeSupport,lifeSupportCrewInjured);}
			}
			
			else if (b.equals(sendInjuredWeap))
			{
				if (turn){injuredCrew(ship1,ship1.Weapon,weapCrewInjured);}
				else{injuredCrew(ship2,ship2.Weapon,weapCrewInjured);}
			}
			
			else if (b.equals(sendInjuredShield))
			{
				if (turn){injuredCrew(ship1,ship1.Shield,shieldCrewInjured);}
				else{injuredCrew(ship2,ship2.Shield,shieldCrewInjured);}
			}
			
			else if (b.equals(sendInjuredEngine))
			{
				if (turn){injuredCrew(ship1,ship1.Engine,engineCrewInjured);}
				else{injuredCrew(ship2,ship2.Engine,engineCrewInjured);}
			}
			
			else if (b.equals(sendInjuredReactor))
			{
				if (turn){injuredCrew(ship1,ship1.Power,reactorCrewInjured);}
				else{injuredCrew(ship2,ship2.Power,reactorCrewInjured);}
			}
			
			else if (b.equals(EnergyUse))
			{
				StringBuffer str=new StringBuffer();
				if (turn)
				{
					str.append("Energy Breakdown: " + ship1.Weapon.getEnergyUsed() + " in weaponry, " + ship1.Shield.getEnergy() + " in shields, " + ship1.Engine.getEnergy() + 
							" in engines, " + ship1.LifeSupport.getEnergy() + " in life support, and " + ship1.Sensors.getEnergy() + " in Sensors.");
					
					if (ship1.getExtinguisherEnergy()>0)
					{
						str.append("\nAdditional Usage: " + ship1.getExtinguisherEnergy() + " in extingusihers.");
					}
				}
				else
				{
					str.append("Energy Breakdown: " + ship2.Weapon.getEnergyUsed() + " in weaponry, " + ship2.Shield.getEnergy() + " in shields, " + ship2.Engine.getEnergy() + 
							" in engines, " + ship2.LifeSupport.getEnergy() + " in life support, and " + ship2.Sensors.getEnergy() + " in Sensors.");
					
					if (ship2.getExtinguisherEnergy()>0)
					{
						str.append("\nAdditional Usage: " + ship2.getExtinguisherEnergy() + " in extingusihers.");
					}
				}
				JOptionPane.showMessageDialog(null,str.toString());
			}
			
			else if (b.equals(End))
			{
				int n = JOptionPane.showOptionDialog(null, "Are you sure you want to end your turn?", "End Turn?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, yesNo, yesNo[0]);
				
				if (n==JOptionPane.YES_OPTION)
				{
					if (!turn)
					{
						endTurn();
					}
					else
					{
						initGUI(ship2,ship1);
					}
				}
			}
			
			else if (b.equals(Forfeit))
			{
				if (turn)
				{
					turn=!turn;
					end(1);
				}
				else
				{
					end(0);
				}
			}
		}

		//All combo boxes*************
		public void itemStateChanged(ItemEvent e) 
		{
			JComboBox b = (JComboBox) e.getSource();
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				Ship s;
				if (turn)
				{
					s=ship1;
				}
				else
				{
					s=ship2;
				}
				
				
				if(b.equals(cShield))
				{
					s.Shield.setConcentration(cShield.getSelectedIndex()-1,0);
					
					if (cShield.getSelectedIndex()<1)
					{
						cShield2.setSelectedIndex(0);
						cShield2.setEnabled(false);
					}
					else
					{
						cShield2.setEnabled(true);
					}
				}
				else if(b.equals(cShield2))
				{
					s.Shield.setConcentration(cShield2.getSelectedIndex()-1,1);
					
					if (cShield2.getSelectedIndex()<1 || !cShield2.isEnabled())
					{
						cShield3.setSelectedIndex(0);
						cShield3.setEnabled(false);
					}
					else if (cShield2.getSelectedIndex()>=1)
					{
						cShield3.setEnabled(true);
					}
				}
				else if(b.equals(cShield3))
				{
					s.Shield.setConcentration(cShield3.getSelectedIndex()-1,2);
				}
				else if (b.equals(cWeapons))
				{
					s.Weapon.setAim(cWeapons.getSelectedIndex()-1,weapNum);
				}
				else if (b.equals(cWeaps))
				{
					weapNum=cWeaps.getSelectedIndex();
					cWeapons.setSelectedIndex(s.Weapon.Weapons[weapNum].aim+1);
					updateWeaps=false;
				}
				else if (b.equals(cParts))
				{
					if (cParts.getSelectedIndex()==6)
					{
						cPartsWeap.setEnabled(true);
					}
					else
					{
						cPartsWeap.setEnabled(false);
					}
					
					updateCrewSelect=true;
				}
				else if (b.equals(cPartsWeap))
				{
					s.Weapon.weapRepairIndex=cPartsWeap.getSelectedIndex();
				}
			}
		}
		
		private void weapSliderEvent(Ship s)
		{	
			if (weapSlider.getValue()>s.getNumGunsMax(weapNum))
			{
				weapSlider.setValue(s.getNumGunsMax(weapNum));
			}
			s.Weapon.setGunsToFire(weapSlider.getValue(), weapNum);
		}
		
		private void shieldSliderEvent(Ship s)
		{
			if (shieldSlider.getValue()>s.getAvailableEnergy()+s.Shield.getEnergy())
			{
				shieldSlider.setValue(s.getAvailableEnergy()+s.Shield.getEnergy());
			}
			s.Shield.setEnergy(shieldSlider.getValue());
		}
		
		private void engineSliderEvent(Ship s)
		{
			if (engineSlider.getValue()>s.getAvailableEnergy()+s.Engine.getEnergy())
			{
				engineSlider.setValue(s.getAvailableEnergy()+s.Engine.getEnergy());
			}
			s.Engine.setEnergy(engineSlider.getValue());
		}
		
		private void lifeSupportSliderEvent(Ship s)
		{
			if (lifeSupportSlider.getValue()>s.getAvailableEnergy()+s.LifeSupport.getEnergy())
			{
				lifeSupportSlider.setValue(s.getAvailableEnergy()+s.LifeSupport.getEnergy());
			}
			s.LifeSupport.setEnergy(lifeSupportSlider.getValue());
		}
		
		private void initExtinguish(Ship s, Part p, RichSlider sl)
		{
			p.initializeExtinguishers(sl.getValue());
		}
		
		//Button implementation*******
		private void weapStats(Ship s)
		{
			if (s.Weapon.health>0)
			{
				if (s.Weapon.isActive)
				{	
					StringBuffer str = new StringBuffer();
					
					for (WeaponSet w : s.Weapon.Weapons)
					{
						int br=0, cd=0, d=0, ld=0;
						for (int i=0;i<w.gunArray.length;i++)
						{
							if (w.gunArray[i].health<=0)
							{
								br++;
							}
							else if (w.gunArray[i].health>0 && w.gunArray[i].health<w.gunArray[i].initHealth*0.35)
							{
								cd++;
							}
							else if (w.gunArray[i].health>w.gunArray[i].initHealth*0.35 && w.gunArray[i].health<w.gunArray[i].initHealth*0.8)
							{
								d++;
							}
							else if (w.gunArray[i].health>w.gunArray[i].initHealth*0.8 && w.gunArray[i].health<w.gunArray[i].initHealth)
							{
								ld++;
							}
						}
						
						str.append(w.name + ":\n" + ld + " lightly damaged guns, " + d + " damaged guns, " + cd + " critically damaged guns, and " + br + " broken guns.\n");
					}
					
					JOptionPane.showMessageDialog(null, "Your accuracy modifier is " + s.Weapon.getAccuracyMod()*100 + "% (" +	s.Weapon.getCrewNum() + "/" + s.Weapon.getEngineersNeeded() + " crew assigned/needed).\n" +
					"Your weapons have the following damage:\n" + str.toString());
				}
				
				else
				{
					JOptionPane.showMessageDialog(null, "Your weapon system is being restarted.");
				}
			}
			
			else
			{
				JOptionPane.showMessageDialog(null, "Your weapon system is destroyed. Aiming is offline.");
			}
		}
		
		private void allocateCrew(Ship s) 
		{	
			if (crewSlider.getValue()>0)
			{
				Part p = s.getPartNum(cParts.getSelectedIndex());
				
				if (!p.isDestroyed)
				{
					s.allocateCrew(cParts.getSelectedIndex(), crewSlider.getValue());
					if (p instanceof systems.PowerPlant || p instanceof systems.Shields || p instanceof systems.Engine || p instanceof systems.Sensors)
					{
						JOptionPane.showMessageDialog(null, "You have allocated " + crewSlider.getValue() + " new crew to your " + s.getPartNameCrew(cParts.getSelectedIndex()) + 
								". This gives you an efficiency of " + p.getCrewEfficiency()*100 + "%.");
					}
					else if (p instanceof systems.Weapons)
					{
						JOptionPane.showMessageDialog(null, "You have allocated " + crewSlider.getValue() + " new crew to your " + s.getPartNameCrew(cParts.getSelectedIndex()) + 
								". This gives you an accuracy modifier of " + ((systems.Weapons)p).getAccuracyMod()*100 + "%.");
					}
					else if (p instanceof systems.LifeSupport)
					{
						JOptionPane.showMessageDialog(null, "You have allocated " + crewSlider.getValue() + " new crew to your " + s.getPartNameCrew(cParts.getSelectedIndex()) + 
								". This gives you a max oxygen refill rate of " + ((systems.LifeSupport)p).getMaxRefill()*100 + "%.");
					}
					else if (cParts.getSelectedIndex()==6)
					{
						JOptionPane.showMessageDialog(null, "You have allocated " + crewSlider.getValue() + " new crew to your weapons.");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "This system has been completely destoryed and is no longer operational, thus it cannot have crew allocated.");
				}
			}
		}
		
		private void freeCrew(Ship s)
		{
			if (crewSlider.getValue()<=s.getPartNum(cParts.getSelectedIndex()).getCrewNumNeeded())
			{
				s.freeCrew(cParts.getSelectedIndex(), crewSlider.getValue());
				if (s.getPartNum(cParts.getSelectedIndex()) instanceof systems.PowerPlant || s.getPartNum(cParts.getSelectedIndex()) instanceof systems.Shields || s.getPartNum(cParts.getSelectedIndex()) instanceof systems.Engine)
				{
					JOptionPane.showMessageDialog(null, "You have freed " + crewSlider.getValue() + " crew to your " + s.getPartNameCrew(cParts.getSelectedIndex()) + 
							". This gives you an efficiency of " + s.getPartNum(cParts.getSelectedIndex()).getCrewEfficiency()*100 + "%.");
				}
				else if (s.getPartNum(cParts.getSelectedIndex()) instanceof systems.Weapons)
				{
					JOptionPane.showMessageDialog(null, "You have freed " + crewSlider.getValue() + " crew to your " + s.getPartNameCrew(cParts.getSelectedIndex()) + 
							". This gives you an accuracy modifier of " + ((systems.Weapons)s.getPartNum(cParts.getSelectedIndex())).getAccuracyMod()*100 + "%.");
				}
			}
		}
		
		private void repair(Ship s)
		{
			if (crewSlider.getValue()>0)
			{
				if (cParts.getSelectedIndex()==6)
				{
					s.allocateRepairCrewWeap(crewSlider.getValue());
					JOptionPane.showMessageDialog(null, "You have assigned " + crewSlider.getValue() + " crew to repairs.");
				}
				else
				{
					Part p = s.getPartNum(cParts.getSelectedIndex());
					
					if (!p.isDestroyed)
					{
						s.allocateRepairCrew(cParts.getSelectedIndex(), crewSlider.getValue()); 
					}
					else
					{
						JOptionPane.showMessageDialog(null, "This system has been completely destoryed and is no longer operational, thus it cannot have crew allocated.");
					}
				}
			}
		}
		
		private void stopRepair(Ship s)
		{
			if (cParts.getSelectedIndex()!=6)
			{
				int n = JOptionPane.showOptionDialog(null, "Are you sure you want to stop all repairs for your " + s.getPartNameCrew(cParts.getSelectedIndex()) + "?", "Stop Repairs", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, yesNo, yesNo[0]);
				
				if (n==JOptionPane.YES_OPTION)
				{
					JOptionPane.showMessageDialog(null, "All crew assigned to repairs have been freed up.");
					s.freeRepairCrew(cParts.getSelectedIndex());
				}
			}
			else
			{
				int n = JOptionPane.showOptionDialog(null, "Are you sure you want to stop all weapon repairs?", "Stop Repairs", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, yesNo, yesNo[0]);
				
				if (n==JOptionPane.YES_OPTION)
				{
					JOptionPane.showMessageDialog(null, "All crew assigned to weapon repairs have been freed up.");
					s.Weapon.clearWeapRepairCrew();
				}
			}
		}
		
		private void resetCrew(Ship s)
		{
			if (s.getPartNum(cParts.getSelectedIndex()) instanceof systems.PowerPlant || s.getPartNum(cParts.getSelectedIndex()) instanceof systems.Shields)
			{
				if (s.getPartNum(cParts.getSelectedIndex()).getCrewNum()>0 && !s.getPartNum(cParts.getSelectedIndex()).isOverloading)
				{
					int n = JOptionPane.showOptionDialog(null, "Are you sure you want to remove all crew from your " + s.getPartNameCrew(cParts.getSelectedIndex()) + "?",
							"Reset?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, yesNo, yesNo[0]);
					
					if (n==JOptionPane.YES_OPTION)
					{
						JOptionPane.showMessageDialog(null, "All " + s.getPartNum(cParts.getSelectedIndex()).getCrewNum() + " crew assigned to your " + s.getPartNameCrew(cParts.getSelectedIndex()) + " have been freed up for other assignments.");
						s.getPartNum(cParts.getSelectedIndex()).clearCrew();
					}
				}
				else if (s.getPartNum(cParts.getSelectedIndex()).getCrewNum()>s.getPartNum(cParts.getSelectedIndex()).getEngineersNeeded() && s.getPartNum(cParts.getSelectedIndex()).isOverloading)
				{
					int n = JOptionPane.showOptionDialog(null, "Are you sure you want to remove all " + (s.getPartNum(cParts.getSelectedIndex()).getCrewNum()-
							s.getPartNum(cParts.getSelectedIndex()).getEngineersNeeded()) + " unnecessary crew from your " + 
							s.getPartNameCrew(cParts.getSelectedIndex()) + "?", "Reset?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, yesNo, yesNo[0]);
					
					if (n==JOptionPane.YES_OPTION)
					{
						JOptionPane.showMessageDialog(null, s.getPartNum(cParts.getSelectedIndex()).getCrewNum()-s.getPartNum(cParts.getSelectedIndex()).getEngineersNeeded() + 
								" unnecessary crew assigned to your " + s.getPartNameCrew(cParts.getSelectedIndex()) + " have been freed up for other assignments.");
						s.freeCrew(cParts.getSelectedIndex(), s.getPartNum(cParts.getSelectedIndex()).getCrewNum()-s.getPartNum(cParts.getSelectedIndex()).getEngineersNeeded());
					}
				}
				else if (s.getPartNum(cParts.getSelectedIndex()).getCrewNum()<=s.getPartNum(cParts.getSelectedIndex()).getEngineersNeeded() && s.getPartNum(cParts.getSelectedIndex()).isOverloading)
				{
					JOptionPane.showMessageDialog(null, "You are currently overloading your " + s.getPartNameCrew(cParts.getSelectedIndex()) + " and all crew assigned to it are necessary to continue operation!");
				}
				else if (s.getPartNum(cParts.getSelectedIndex()).getCrewNum()<=0)
				{
					JOptionPane.showMessageDialog(null, "You do not have any crew assigned to your " + s.getPartNameCrew(cParts.getSelectedIndex()) + "!");
				}
			}
			
			else if (s.getPartNum(cParts.getSelectedIndex()) instanceof systems.Engine)
			{
				if (s.getPartNum(cParts.getSelectedIndex()).getCrewNum()>0 && !((systems.Engine)s.getPartNum(cParts.getSelectedIndex())).isRamming)
				{
					int n = JOptionPane.showOptionDialog(null, "Are you sure you want to remove all crew from your " + s.getPartNameCrew(cParts.getSelectedIndex()) + "?",
							"Reset?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, yesNo, yesNo[0]);
					
					if (n==JOptionPane.YES_OPTION)
					{
						JOptionPane.showMessageDialog(null, "All " + s.getPartNum(cParts.getSelectedIndex()).getCrewNum() + " crew assigned to your " + s.getPartNameCrew(cParts.getSelectedIndex()) + " have been freed up for other assignments.");
						s.freeCrew(cParts.getSelectedIndex(), s.getPartNum(cParts.getSelectedIndex()).getCrewNum());
					}
				}
				else if (s.getPartNum(cParts.getSelectedIndex()).getCrewNum()>s.getPartNum(cParts.getSelectedIndex()).getEngineersNeededOverload() && ((systems.Engine)s.getPartNum(cParts.getSelectedIndex())).isRamming)
				{
					int n = JOptionPane.showOptionDialog(null, "Are you sure you want to remove all " + (s.getPartNum(cParts.getSelectedIndex()).getCrewNum()-
							s.getPartNum(cParts.getSelectedIndex()).getEngineersNeededOverload()) + " unnecessary crew from your " + 
							s.getPartNameCrew(cParts.getSelectedIndex()) + "?", "Reset?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, yesNo, yesNo[0]);
					
					if (n==JOptionPane.YES_OPTION)
					{
						JOptionPane.showMessageDialog(null, s.getPartNum(cParts.getSelectedIndex()).getCrewNum()-s.getPartNum(cParts.getSelectedIndex()).getEngineersNeededOverload() + 
								" unnecessary crew assigned to your " + s.getPartNameCrew(cParts.getSelectedIndex()) + " have been freed up for other assignments.");
						s.freeCrew(cParts.getSelectedIndex(), s.getPartNum(cParts.getSelectedIndex()).getCrewNum()-s.getPartNum(cParts.getSelectedIndex()).getEngineersNeededOverload());
					}
				}
				else if (s.getPartNum(cParts.getSelectedIndex()).getCrewNum()<=s.getPartNum(cParts.getSelectedIndex()).getEngineersNeededOverload() && ((systems.Engine)s.getPartNum(cParts.getSelectedIndex())).isRamming)
				{
					JOptionPane.showMessageDialog(null, "You are currently ramming and all crew assigned to your engines are necessary to continue operation!");
				}
				else if (s.getPartNum(cParts.getSelectedIndex()).getCrewNum()<=0)
				{
					JOptionPane.showMessageDialog(null, "You do not have any crew assigned to your " + s.getPartNameCrew(cParts.getSelectedIndex()) + "!");
				}
			}
			
			else if (s.getPartNum(cParts.getSelectedIndex()) instanceof systems.Weapons || s.getPartNum(cParts.getSelectedIndex()) instanceof systems.LifeSupport)
			{
				if (s.getPartNum(cParts.getSelectedIndex()).getCrewNum()>0)
				{
					int n = JOptionPane.showOptionDialog(null, "Are you sure you want to remove all " + s.getPartNum(cParts.getSelectedIndex()).getCrewNum() + " crew from your " + 
							s.getPartNameCrew(cParts.getSelectedIndex()) + "?", "Reset?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, yesNo, yesNo[0]);
					
					if (n==JOptionPane.YES_OPTION)
					{
						JOptionPane.showMessageDialog(null, s.getPartNum(cParts.getSelectedIndex()).getCrewNum() + " unnecessary crew assigned to your " + s.getPartNameCrew(cParts.getSelectedIndex()) + " have been freed up for other assignments.");
						s.freeCrew(cParts.getSelectedIndex(), s.getPartNum(cParts.getSelectedIndex()).getCrewNum());
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "You do not have any crew assigned to your " + s.getPartNameCrew(cParts.getSelectedIndex()) + "!");
				}
			}
		}
		
		private void overloadShields(Ship s)
		{
			String [] overload = {"Overload", "No"};
			
			int o = JOptionPane.showOptionDialog(null, "Initiating shield generator overload command...are you sure? "
					+ "The only way to bring the system back to normal is a hard reset.", "Overload?", 
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, overload, overload[1]);
			
			if (o == JOptionPane.YES_OPTION)
			{
				s.Shield.isOverloading=true;
				
				JOptionPane.showMessageDialog(null, "Shield generator overload sequence complete...increasing output to " + s.Shield.getEnergyMax() + " energy.");
			}
		}
		
		private void overloadReactor(Ship s)
		{
			String[] overload = {"Overload", "No"};
			
			int i = JOptionPane.showOptionDialog(null, "Initiating reactor overload sequence...are you sure? Once overloaded, the only way to stop the process is to do a full cooldown.", "Overload?", 
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, overload, overload[1]);
			
			if (i==JOptionPane.YES_OPTION)
			{
				s.Power.isOverloading=true;
				
				JOptionPane.showMessageDialog(null, "Reactor overload sequence complete...increasing output to " + s.Power.getEnergyProduced() + " energy.");
			}
		}
		
		private void ramEngine(Ship s)
		{
			String [] overload = {"Do it!", "No"};
			
			int o = JOptionPane.showOptionDialog(null, "Initiating ramming sequence...are you sure? There is no coming back from this.", "Overload?", 
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, overload, overload[1]);
			
			if (o == JOptionPane.YES_OPTION)
			{
				s.Engine.initiateRamming();
				
				JOptionPane.showMessageDialog(null, "Engine ramming sequence established...may God bless your soul.");
				
				if (s.getAvailableEnergy()<s.Engine.getEnergyMax() && s.Power.getEnergyStored()>s.Engine.getEnergyMax())
				{
					s.Weapon.resetGuns();
					JOptionPane.showMessageDialog(null, "Weapons reset to allocate energy to engines.");
					
					if (s.getAvailableEnergy()<s.Engine.getEnergyMax())
					{
						s.Shield.setEnergy(0);
						JOptionPane.showMessageDialog(null, "Shields reset to allocate energy to engines.");
					}
					
					s.Engine.setEnergy(s.Engine.getEnergyMax());
				}
				else if (s.getAvailableEnergy()<s.Engine.getEnergyMax() && s.Power.getEnergyStored()<s.Engine.getEnergyMax())
				{
					s.Weapon.resetGuns();
					s.Shield.setEnergy(0);
					s.deactivateExtinguishers();
					s.Engine.setEnergy(s.Power.getEnergyStored());
					JOptionPane.showMessageDialog(null, "All systems reset to free up energy for engines.");
				}
				else
				{
					s.Engine.setEnergy(s.Engine.getEnergyMax());
				}
			}
		}
	
		private void hardResetShields(Ship s)
		{
			String[] reset = {"Reset", "No"};
			
			int r = JOptionPane.showOptionDialog(null, "Are you sure you want to reset the generator to stop the overload process?\n"
					+ "It will take " + s.Shield.RESET_TIME + " turn(s) to do a reset, and you will be unable to use your shields during that time.", "Reset Shields", 
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, reset, reset[1]);
			
			if (r==JOptionPane.YES_OPTION)
			{
				s.Shield.initializeReset();
				
				JOptionPane.showMessageDialog(null, "Shield Hard Reset Sequence Initiated.");
			}
		}
		
		private void cooldownReactor(Ship s)
		{
			String[] reset = {"Cooldown", "No"};
			
			int r = JOptionPane.showOptionDialog(null, "Are you sure you want to run a full cooldown to stop the overload process?\n"
					+ "It will take " + s.Power.RESET_TIME + " turn(s) to do a full cooldown, and you will produce half energy during that time.", "Reset Reactor", 
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, reset, reset[1]);
			
			if (r==JOptionPane.YES_OPTION)
			{
				s.Power.initializeReset();
				
				JOptionPane.showMessageDialog(null, "Reactor Full Cooldown Sequence Initiated.");
			}
		}
		
		private void lockEnergy(Ship s, Ship s2)
		{
			s.Sensors.setEnergy(sensorsSlider.getValue());
			s.Sensors.lockEnergy();
			lockEnergy.setEnabled(false);
			sensorsSlider.setEnabled(false);
			if (s.Sensors.isLockedCrew())
			{
				updateSensors(s, s2);
			}
		}
		
		private void lockCrew(Ship s, Ship s2)
		{
			s.Sensors.lockCrew();
			lockCrew.setEnabled(false);
			if (s.Sensors.isLockedEnergy())
			{
				updateSensors(s, s2);
			}
		}
	
		private void injuredCrew(Ship s, Part p, RichSlider sl)
		{
			int i = sl.getValue();
			
			for (crew.Crew c : p.getInjuredCrew())
			{
				if (i>0)
				{
					s.MedBay.addInjured(c);
					p.getCrew().remove(c);
					i--;
				}
			}
		}
	}
	
	protected class timerHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			JOptionPane.showMessageDialog(null, "The round timer is up. You are out of time!");
			
			if (!turn)
			{
				endTurn();
			}
			
			else
			{
				initGUI(ship2, ship1);
			}
		}
		
	}
}
