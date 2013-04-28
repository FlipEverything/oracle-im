import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

import bean.Picture;


@SuppressWarnings("serial")
public class JPanelGallery extends JPanel implements IMConstants{
	  
	protected IMFrame m_jFrameOwner = null;
	protected JPanel contentPanel;
	
	protected String m_szTitle;	
	protected int m_nWidth;
	protected int m_nHeight = 450;
	protected int m_nOffset = 30;
	private int m_nLineHeight = 30;
	private int m_nX = 0;
	private int m_nY = 0;
	
	  	
	public JPanelGallery(IMFrame frame){
		this.m_jFrameOwner = frame;
		this.m_szTitle = IMMessage.getString("TOP_PICTURES");
		this.m_nWidth = frame.getWidth() - 70;

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
	  protected void setupButton()
	  {

	  }

	  /**
	   * Draws the content pane.
	   */
	  private void setupContentPane() 
	  {
		  contentPanel = new JPanel();
		  contentPanel.setLayout(null);
		  
		  IMQuery q = new IMQuery();
		  ArrayList<Picture> array = null;
		  
		  array = q.selectPicturesTopValue();
		  m_jFrameOwner.createPictureResultTable(array, "TOP_VALUE", m_nWidth, IMQuery.TOP*m_nLineHeight, m_nX, m_nY, contentPanel);
		  m_nY+=IMQuery.TOP*m_nLineHeight;
		  
		  array = q.selectPicturesTopCommentCount();
		  m_jFrameOwner.createPictureResultTable(array, "TOP_COMMENT", m_nWidth, IMQuery.TOP*m_nLineHeight, m_nX, m_nY, contentPanel);
		  m_nY+=IMQuery.TOP*m_nLineHeight;
		  
		  array = q.selectPicturesNew();
		  m_jFrameOwner.createPictureResultTable(array, "TOP_NEW", m_nWidth, IMQuery.TOP*m_nLineHeight, m_nX, m_nY, contentPanel);


		  
		  /*array = q.selectPicturesTopRateCount();
		  createTable(array, "TOP_RATE_COUNT");*/
	  }
	  

	  


	  /**
	   * Initializes the dialog display.
	   */
	  private void setupDisplay()
	  {
		m_jFrameOwner.setLabel(m_szTitle);
	    this.setSize(new Dimension(m_nWidth, m_nHeight));
	    this.getAccessibleContext().setAccessibleDescription(
	        IMMessage.getString("LOGINDIAG_DESC"));
	    this.setLayout(new BorderLayout());
	  }
	  
	  
	
	
	
}
