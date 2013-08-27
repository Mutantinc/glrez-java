/*
/**
* Copyright 2005-2012 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a> and <a href="mailto:rez@chiptune.com">Christophe Résigné</a>
* 
* Java port of the GLRez intro released at the Breakpoint 2005
* @see http://www.pouet.net/prod.php?which=16327
* @see http://www.chiptune.com
* 
* Original C source code on GitHub
* @see https://github.com/chiptune/glrez
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package com.chiptune.glrez;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.mars.toolkit.display.DisplayModeChoiceListener;
import org.mars.toolkit.display.DisplayModeComboBox;
import org.mars.toolkit.display.DisplayModeInfo;
import org.mars.toolkit.realtime.graph.FrameInfo;

/**
 * Basic display mode chooser.
 */
public class OptionsDialog extends JDialog {

  private static final long serialVersionUID = 1L;
  
  private final JPanel contentPanel = new JPanel();
  
  private DisplayModeInfo displayModeInfo = new DisplayModeInfo();
  private JCheckBox fullScreenCheckBox;
  private DisplayModeComboBox displayModesComboBox;
  private JRadioButton fmodButton;
  private JRadioButton openAlButton;


  public OptionsDialog(DisplayModeChoiceListener displayModeChoiceListener) {
    this(true, true, true, displayModeChoiceListener);
  }

  public OptionsDialog(boolean allowModes, boolean allowFullScreen, boolean allowSound, DisplayModeChoiceListener displayModeChoiceListener) {
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Please choose a display mode");
    
    setBounds(100, 100, 350, 135);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    GridBagLayout gbl_contentPanel = new GridBagLayout();
    gbl_contentPanel.columnWidths = new int[]{50, 80, 122, 0};
    gbl_contentPanel.rowHeights = new int[]{25, 25, 0};
    gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
    gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
    
    contentPanel.setLayout(gbl_contentPanel);
    {
      JLabel graphicsLabel = new JLabel("Graphics");
      GridBagConstraints gbc_graphicsLabel = new GridBagConstraints();
      gbc_graphicsLabel.anchor = GridBagConstraints.WEST;
      gbc_graphicsLabel.fill = GridBagConstraints.VERTICAL;
      gbc_graphicsLabel.insets = new Insets(0, 0, 5, 5);
      gbc_graphicsLabel.gridx = 0;
      gbc_graphicsLabel.gridy = 0;
      contentPanel.add(graphicsLabel, gbc_graphicsLabel);
    }
    {
      fullScreenCheckBox = new JCheckBox("Full screen");
      fullScreenCheckBox.addActionListener(new FullScreenActionListener(displayModeInfo));
      GridBagConstraints gbc_fullScreenCheckBox = new GridBagConstraints();
      gbc_fullScreenCheckBox.fill = GridBagConstraints.BOTH;
      gbc_fullScreenCheckBox.insets = new Insets(0, 0, 5, 5);
      gbc_fullScreenCheckBox.gridx = 1;
      gbc_fullScreenCheckBox.gridy = 0;
      contentPanel.add(fullScreenCheckBox, gbc_fullScreenCheckBox);
    }
    {
      displayModesComboBox = new DisplayModeComboBox(displayModeInfo);
      GridBagConstraints gbc_ResolutionComboBox = new GridBagConstraints();
      gbc_ResolutionComboBox.insets = new Insets(0, 0, 5, 0);
      gbc_ResolutionComboBox.fill = GridBagConstraints.HORIZONTAL;
      gbc_ResolutionComboBox.gridx = 2;
      gbc_ResolutionComboBox.gridy = 0;
      contentPanel.add(displayModesComboBox, gbc_ResolutionComboBox);
    }
    {
      JLabel soundLabel = new JLabel("Sound");
      GridBagConstraints gbc_soundLabel = new GridBagConstraints();
      gbc_soundLabel.fill = GridBagConstraints.BOTH;
      gbc_soundLabel.insets = new Insets(0, 0, 0, 5);
      gbc_soundLabel.gridx = 0;
      gbc_soundLabel.gridy = 1;
      contentPanel.add(soundLabel, gbc_soundLabel);
    }
    ButtonGroup soundGroup = new ButtonGroup();
    {
      fmodButton = new JRadioButton("FModEx");
      soundGroup.add(fmodButton);
      fmodButton.setSelected(true);
      GridBagConstraints gbc_fmodButton = new GridBagConstraints();
      gbc_fmodButton.fill = GridBagConstraints.BOTH;
      gbc_fmodButton.insets = new Insets(0, 0, 0, 5);
      gbc_fmodButton.gridx = 1;
      gbc_fmodButton.gridy = 1;
      contentPanel.add(fmodButton, gbc_fmodButton);
    }
    {
      openAlButton = new JRadioButton("OpenAL");
      soundGroup.add(openAlButton);
      GridBagConstraints gbc_openAlButton = new GridBagConstraints();
      gbc_openAlButton.fill = GridBagConstraints.BOTH;
      gbc_openAlButton.gridx = 2;
      gbc_openAlButton.gridy = 1;
      contentPanel.add(openAlButton, gbc_openAlButton);
    }
    {
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);
      {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new OkActionListener(this));
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
      }
      {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelActionListener(this));
        buttonPane.add(cancelButton);
      }
    }
    
    displayModeInfo.addChoiceListener(displayModeChoiceListener);
    displayModeInfo.addSelectionListener(displayModesComboBox);
    boolean fullScreen = allowFullScreen && displayModeInfo.isFullScreenSupported();
    
    if(allowModes) {
      displayModesComboBox.selectDefaultModes(fullScreen); //this triggers the listeners above, so keep it under.
    }
    else {
      displayModesComboBox.setEnabled(false);
    }
    fullScreenCheckBox.setSelected(fullScreen);
    fullScreenCheckBox.setEnabled(fullScreen);
    
    if(allowSound) {
      if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
        fmodButton.setSelected(true);
      }
      else {
        openAlButton.setSelected(true);
      }
    }
    fmodButton.setEnabled(allowSound);
    openAlButton.setEnabled(allowSound);
    
    setResizable(false);
  }

  protected DisplayModeInfo getDisplayModeInfo() {
    return displayModeInfo;
  }
  
  public FrameInfo getDisplayModeSelected() {
    return displayModeInfo.getDisplayModeSelected();
  }
  
  public boolean isFullScreenSelected() {
    return fullScreenCheckBox.isSelected();
  }

  public boolean isFmodSelected() {
    return fmodButton.isSelected();
  }

  public boolean isOpenALSelected() {
    return openAlButton.isSelected();
  }
}

class FullScreenActionListener implements ActionListener {
  private DisplayModeInfo dmi;
  public FullScreenActionListener(DisplayModeInfo dmi) {
    this.dmi = dmi;
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    AbstractButton source = (AbstractButton)e.getSource();
    dmi.setFullScreenSelected(source.isSelected());
  }
}

class OkActionListener implements ActionListener {
  private OptionsDialog parent;
  public OkActionListener(OptionsDialog parent) {
    this.parent = parent;
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    parent.dispose();
    parent.getDisplayModeInfo().fireDisplayModeChosen();
  }
}

class CancelActionListener implements ActionListener {
  private OptionsDialog parent;
  public CancelActionListener(OptionsDialog parent) {
    this.parent = parent;
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    parent.dispose();
  }
}
