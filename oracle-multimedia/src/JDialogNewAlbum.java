

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

import bean.Album;


import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Callable;

/**
 * Displays the login dialog and creates the connection
 * to the database.
 */
@SuppressWarnings("serial")
public class JDialogNewAlbum extends JDialog implements IMConstants
{
  
  IMFrame m_jFrameOwner = null;

  JButton m_jButtonLogin = new JButton();
  JButton m_jButtonCancel = new JButton();

  JLabel m_jLabelAlbumName = new JLabel();
  JTextField m_jAlbumNameField = new JTextField();

  JLabel m_jLabelPasswd = new JLabel();
  protected JCheckBox m_jCheckBoxPublic = new JCheckBox();

  protected String m_szButtonConfirmTitle = "LOGINDIAG_CREATE_BUTTON";
	
  protected String m_szTitle;	
  protected int m_nWidth = 400;
  protected int m_nHeight = 180;
	
  protected int m_iFieldHeight = 30;
  protected int m_iDeltaY = 10;
  protected int m_iLabelStartY = 20;
  protected int m_iFieldStartY = m_iLabelStartY + 5;
  protected int m_iLabelStartX = 25;
  protected int m_iFieldStartX = 145;	
  protected int m_nButtonWidth = 120;
  protected int m_nButtonHeight = 35;
  protected int m_nButtonStart = 70;
  protected int m_nLastButtonStart = m_nButtonStart;
  protected int m_iLabelWidth = 100;
  protected int m_iFieldWidth = 185;

private JPanel contentPanel;



  public JDialogNewAlbum(IMFrame jFrameOwner)
  {
    super(jFrameOwner, true);
    m_jFrameOwner = jFrameOwner;
    m_szTitle = "MAIN_MENU_ALBUM_NEW";

    try
    {
      setupDisplay();
      setupButton();
      setupContentPane();
      this.getContentPane().add(contentPanel, BorderLayout.CENTER);
      addListener();
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
	  IMFrame.createButton(m_jButtonLogin, m_szButtonConfirmTitle, 'L', "confirm", "LOGINDIAG_LOGIN_BUTTON_DESC", 
	    		new Rectangle(m_nLastButtonStart, m_nHeight-m_nButtonHeight-40, m_nButtonWidth  , m_nButtonHeight), new Callable<Void>() {
			   public Void call() {
			        confirm();
					return null;
			   }
			});
	    
	    IMFrame.createButton(m_jButtonCancel, "LOGINDIAG_CANCEL_BUTTON", 'N', "cancel", "LOGINDIAG_CANCEL_BUTTON_DESC", 
	    		new Rectangle(m_nLastButtonStart+=m_nButtonWidth, m_nHeight-m_nButtonHeight-40, m_nButtonWidth, m_nButtonHeight), new Callable<Void>() {
			   public Void call() {
			        cancel();
					return null;
			   }
			});
  }

  /**
   * Draws the content pane.
   */
  protected void setupContentPane() 
  {
	  m_jLabelAlbumName = IMFrame.createJLabel(m_jLabelAlbumName, new Rectangle(m_iLabelStartX, m_iLabelStartY, m_iLabelWidth , m_iFieldHeight), 
				"NEWALBUMDIAG_NAME", 'U', m_jAlbumNameField);
		
		m_jLabelPasswd = IMFrame.createJLabel(m_jLabelPasswd, new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"NEWALBUMDIAG_IS_PUBLIC", 'P', m_jCheckBoxPublic);

	    m_jAlbumNameField.setBounds(
		        new Rectangle(m_iFieldStartX, m_iFieldStartY, m_iFieldWidth , m_iFieldHeight));
	    m_jAlbumNameField.setToolTipText(IMMessage.getString("LOGINDIAG_USERNAME_FIELD_DESC"));


	    m_jCheckBoxPublic.setBounds(
		        new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth, m_iFieldHeight));
	    m_jCheckBoxPublic.setToolTipText(IMMessage.getString("LOGINDIAG_PASSWD_FIELD_DESC"));

	    
	    
	    contentPanel = new JPanel();
	    contentPanel.setLayout(null);

	    contentPanel.add(m_jAlbumNameField, null);
	    contentPanel.add(m_jCheckBoxPublic, null);
	    contentPanel.add(m_jLabelPasswd, null);
	    contentPanel.add(m_jLabelAlbumName, null);

	    contentPanel.add(m_jButtonCancel, null);
	    contentPanel.add(m_jButtonLogin, null);

  }

  /**
   * 
   */
  void confirm()
  {
	  if (
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
      }
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
              m_jAlbumNameField.requestFocus();
              gotFocus =true;
            }
          }
        });
  }
}
