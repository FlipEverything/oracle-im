

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import bean.Album;


import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Displays the login dialog and creates the connection
 * to the database.
 */
@SuppressWarnings("serial")
public class JDialogTable extends JDialog implements IMConstants
{
  
  IMFrame m_jFrameOwner = null;
  JTable m_jTableData = new JTable();
  private String m_szTitle;
  private int m_nWidth = 300;
  private int m_nHeight = 600;
  private TableModel m_model;

  public JDialogTable(IMFrame jFrameOwner, TableModel model)
  {
    super(jFrameOwner, true);
    m_jFrameOwner = jFrameOwner;
    m_szTitle = "MAIN_MENU_ALBUM_NEW";
    m_model = model;

    try
    {
      setupDisplay();
      setupButton();
     
      setupContentPane();
      addListener();

      setVisible(true);
    }
    catch (Exception e)
    {
      new IMMessage(IMConstants.ERROR, "APP_ERR", e);
    }
  }

  /**
   * Draws buttons.
   */
  private void setupButton()
  {
	
  }

  /**
   * Draws the content pane.
   */
  private void setupContentPane() 
  {
	  
	  m_jTableData = new JTable(m_model);

  }

  /**
   * 
   */
  void confirm()
  {
	/*  if (
		m_jAlbumNameField.getText().equals("") 
		 ){
		  new IMMessage(IMConstants.ERROR, "EMPTY_FIELD", new Exception());
	  } else {
		  IMQuery q = new IMQuery();
		  Album a = new Album(0, m_jFrameOwner.getUserActive().getUserId(), m_jAlbumNameField.getText(), m_jCheckBoxPublic.isSelected());
		  a = q.createAlbum(a);
		  
		  if (a!=null){
			  new IMMessage(IMConstants.WARNING, "CREATE_SUCCESS");
			  this.setVisible(false);
			  this.dispose();
		  }
      }*/
  }

  /**
   * 
   */
  void cancel()
  {
    this.setVisible(false);
    this.dispose();
  }

  /**
   * Initializes the dialog display.
   */
  private void setupDisplay()
  {
    this.setTitle(IMMessage.getString(m_szTitle));
    this.setSize(new Dimension(m_nWidth, m_nHeight));
    this.getAccessibleContext().setAccessibleDescription(
        IMMessage.getString("LOGINDIAG_DESC"));
    this.getContentPane().setLayout(new BorderLayout());
    this.setResizable(false);

    IMUIUtil.initJDialogHelper(this);
  }
  
  /**
   * Adds the WindowEvent listener.
   */
  private void addListener()
  {
    this.addWindowListener(new WindowAdapter()
        {
          boolean gotFocus = false;
          public void windowOpened(WindowEvent e)
          {
            if (!gotFocus)
            {
              
              gotFocus =true;
            }
          }
        });
  }
}
