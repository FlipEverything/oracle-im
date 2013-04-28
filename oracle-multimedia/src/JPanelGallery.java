import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
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
		  createTable(array, "TOP_VALUE");
		  
		  array = q.selectPicturesTopCommentCount();
		  createTable(array, "TOP_COMMENT");
		  
		  array = q.selectPicturesNew();
		  createTable(array, "TOP_NEW");
		  
		  /*array = q.selectPicturesTopRateCount();
		  createTable(array, "TOP_RATE_COUNT");*/
	  }
	  
	  private void createTable(ArrayList<Picture> array, String text){
		  IMTableModelPicture m_model = new IMTableModelPicture(array, m_jFrameOwner); 
		  IMTable m_jTableData = new IMTable(m_model);		
		  m_jTableData.setRowSelectionAllowed(false);
		  
		  JScrollPane scroll = new JScrollPane(m_jTableData);
		  scroll.setSize(new Dimension(m_nWidth, IMQuery.TOP*m_nLineHeight));
		  scroll.setPreferredSize(new Dimension(m_nWidth, IMQuery.TOP*m_nLineHeight));
		  scroll.setBounds(new Rectangle(m_nX, m_nY, m_nWidth, IMQuery.TOP*m_nLineHeight));
		  
		  m_nY +=IMQuery.TOP*m_nLineHeight;
		  
		  TitledBorder title;
		  title = BorderFactory.createTitledBorder(IMMessage.getString(text));
		  scroll.setBorder(title);
		  
		  contentPanel.add(scroll, null);
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
