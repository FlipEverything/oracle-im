/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Container;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The IMExampleFrame class displays the main frame.
 */
public class IMExampleFrame extends JFrame implements IMConstants
{
  JMenuBar m_menuBar = new JMenuBar();
  JMenu m_menuConnection = new JMenu();

  JMenuItem m_menuConnectionOpen = new JMenuItem();
  JMenuItem m_menuConnectionClose = new JMenuItem();
  JMenuItem m_menuConnectionExit = new JMenuItem();

  JScrollPane m_jQueryResultPanel = new JScrollPane();
  JTable m_jResultSetTable = null;
  JLabel m_labelTable = new JLabel();

  Container m_jContentPane = null;

  public IMExampleFrame()
  {
    try
    {
      m_jContentPane = getContentPane();

      setupDisplay();
      setupMenu();
      setupContentPane();

      setVisible(true);

      clickConnect();
    }
    catch(Exception e)
    {
      new IMMessage(IMConstants.ERROR, "APP_ERR", e);
    }
  }

  /**
   * Lays out the content pane.
   */
  private void setupContentPane()
  {
    // Lays out the panel.
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BorderLayout(10,10));
    contentPanel.setBackground(UIManager.getColor("Panel.background"));
    contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

    // Sets the label for the table.
    m_labelTable.setText(IMMessage.getString("MAIN_TBL_TITLE"));
    m_labelTable.setEnabled(false);
    m_labelTable.setLabelFor(m_jQueryResultPanel);

    // Lays out the scroll pane for the table.
    m_jQueryResultPanel.setPreferredSize(new Dimension(846, 340));
    m_jQueryResultPanel.setBorder(BorderFactory.createEtchedBorder());
    m_jQueryResultPanel.setAutoscrolls(true);
    m_jQueryResultPanel.setEnabled(false);

    // Puts the label in the middle horizontally.
    Box hbox = Box.createHorizontalBox();
    hbox.add(m_labelTable);
    hbox.add(Box.createHorizontalGlue());

    // Leaves some space around the label.
    Box vbox = Box.createVerticalBox();
    vbox.add(Box.createVerticalStrut(5));
    vbox.add(hbox);
    vbox.add(Box.createVerticalStrut(5));

    // Adds the label and the scroll pane.
    contentPanel.add(vbox, BorderLayout.NORTH);
    contentPanel.add(m_jQueryResultPanel, BorderLayout.CENTER);

    this.getContentPane().add(contentPanel, BorderLayout.CENTER);
  }

  /**
   * Simulates the clicking of the connection open menu item.
   */
  void clickConnect()
  {
    m_menuConnectionOpen.doClick();
  }

  /**
   * Shows the login dialog.
   */
  void connectActionPerformed() 
  {
    new IMLoginDialog(this);
  }

  /**
   * Enables the table and some menu items after the connection established.
   */
  void enableDisplay()
  {
    m_labelTable.setEnabled(true);
    m_menuConnectionClose.setEnabled(true);
    m_jQueryResultPanel.setEnabled(true);
    m_jQueryResultPanel.setVisible(true);

    m_menuConnectionOpen.setEnabled(false);
    m_menuConnectionExit.setEnabled(false);
  }

  /**
   * Drops the database connection and sets the proper display.
   */
  void disconnectActionPerformed(ActionEvent ae)
  {
    try
    {
      m_menuConnectionOpen.setEnabled(true);
      m_menuConnectionExit.setEnabled(true);

      m_menuConnectionClose.setEnabled(false);
      m_jQueryResultPanel.setViewportView(null);
      m_jQueryResultPanel.setEnabled(false);
      m_labelTable.setEnabled(false);

      IMExample.closeDBConnection();
    }
    catch (Exception e)
    {
      new IMMessage(IMConstants.ERROR, "CONNECT_CLOSE_FAIL", e);
    }
  }

  /**
   * Displays the retrieved table from the database.
   */
  void showDefaultTable()
  {
    IMExampleQuery imQuery = new IMExampleQuery();

    m_jResultSetTable = imQuery.execQuery();

    if (m_jResultSetTable != null)
    {
      m_jQueryResultPanel.setViewportView(m_jResultSetTable);
      m_jResultSetTable.requestFocus();
      // select the first row
      m_jResultSetTable.changeSelection(0, 0, false, false);
    }
    else
    {
      new IMMessage(IMConstants.ERROR, "NO_TABLE");
    }
  }

  /**
   * Initiates the frame.
   */
  private void setupDisplay()
  {
    m_jContentPane.setLayout(new BorderLayout());
    setSize(new Dimension(946, 601));
    setTitle(IMMessage.getString("MAIN_TITLE"));
    getAccessibleContext().setAccessibleDescription
      (IMMessage.getString("MAIN_DESC"));

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = getSize();
    if (frameSize.height > screenSize.height)
    {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width)
    {
      frameSize.width = screenSize.width;
    }
    setLocation((screenSize.width - frameSize.width) / 2, 
        (screenSize.height - frameSize.height) / 2);

    addWindowListener(new WindowAdapter()
        {
          public void windowClosing(WindowEvent e)
          {
          System.exit(0);
          }
        });
  }

  /**
   * Adds menu items.
   */
  private void setupMenu()
  {
    m_menuConnectionOpen.setText(IMMessage.getString("MAIN_MENU_CONNECT"));
    m_menuConnectionOpen.setMnemonic('O');
    m_menuConnectionOpen.setToolTipText(IMMessage.getString("MAIN_MENU_CONNECT_DESC"));
    m_menuConnectionOpen.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          connectActionPerformed();
        }
      });

    m_menuConnectionClose.setText(IMMessage.getString("MAIN_MENU_DISCONNECT"));
    m_menuConnectionClose.setEnabled(false);
    m_menuConnectionClose.setMnemonic('E');
    m_menuConnectionClose.setToolTipText(IMMessage.getString("MAIN_MENU_DISCONNECT_DESC"));
    m_menuConnectionClose.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          disconnectActionPerformed(e);
        }
      });

    m_menuConnectionExit.setText(IMMessage.getString("MAIN_MENU_EXIT"));
    m_menuConnectionExit.setMnemonic('x');
    m_menuConnectionExit.setToolTipText(IMMessage.getString("MAIN_MENU_EXIT_DESC"));
    m_menuConnectionExit.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent ae)
        {
          exitActionPerformed(ae);
        }
      });

    m_menuConnection.setText(IMMessage.getString("MAIN_MENU_FILE"));
    m_menuConnection.setMnemonic('C');
    m_menuConnection.add(m_menuConnectionOpen);
    m_menuConnection.add(m_menuConnectionClose);
    m_menuConnection.add(m_menuConnectionExit);

    m_menuBar.add(m_menuConnection);

    this.setJMenuBar(m_menuBar);
  }

  /**
   * Exits the demo.
   */
  void exitActionPerformed(ActionEvent e)
  {
    disconnectActionPerformed(e);
    System.exit(0);
  }
}
