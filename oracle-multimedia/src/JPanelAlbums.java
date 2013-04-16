import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import bean.Album;
import bean.User;


@SuppressWarnings("serial")
public class JPanelAlbums extends JPanel implements IMConstants{
	protected IMFrame m_jFrameOwner = null;
    protected User m_userDisplayed = null;
	
	protected JPanel contentPanel;
	protected JComboBox m_jComboBoxAlbum = new JComboBox();
	
	protected String m_szTitle;	
	protected int m_nWidth;
	protected int m_nHeight = 200;
	
	protected int m_iFieldHeight = 30;
	protected int m_iDeltaY = 10;

	
	public JPanelAlbums(IMFrame frame, User user){
		this.m_jFrameOwner = frame;
		this.m_szTitle = IMMessage.getString("MAIN_MENU_ALBUMS")+": "+user.getUsername();
		this.m_userDisplayed = user;
		this.m_nWidth = frame.getWidth()-300;
	}
	
	public void init(){
		try
	    {
	      setupDisplay();
	      setupButton();
	      setupContentPane();
	      add(contentPanel, BorderLayout.CENTER);

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
	  protected void setupContentPane() 
	  {
		  IMQuery q = new IMQuery();
		  ArrayList<Album> array = q.getAlbumsByUser(m_userDisplayed);
		  m_jComboBoxAlbum = IMFrame.createComboBox(m_jComboBoxAlbum, true, 0, new Rectangle(m_iDeltaY, m_iDeltaY, m_nWidth-m_iDeltaY*2, m_iFieldHeight), 
					array, new Callable<Void>() {
				   public Void call() {
				        selectAlbum();
						return null;
				   }
			});
	    
	    
	    contentPanel = new JPanel();
	    contentPanel.setLayout(null);
	    
	    contentPanel.add(m_jComboBoxAlbum, null);
	    
	  }

	  protected void selectAlbum() {
		  
	  }

	/**
	   * Initializes the dialog display.
	   */
	  private void setupDisplay()
	  {
		m_jFrameOwner.setLabel(m_szTitle);
	    this.setSize(new Dimension(m_nWidth, m_nHeight));
	    this.setPreferredSize(new Dimension(m_nWidth, m_nHeight));
	    this.getAccessibleContext().setAccessibleDescription(
	        IMMessage.getString("LOGINDIAG_DESC"));
	    this.setLayout(new BorderLayout());
	  }

}
