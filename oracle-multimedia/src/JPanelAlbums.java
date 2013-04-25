import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


import bean.Album;
import bean.Picture;
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
	
	private int m_iOffset = 20;
	private int m_iStartX = 15;
	private int m_iStartY = 15 + m_iFieldHeight + m_iOffset;
	private int m_iX = 0;
	private int m_iY = 0;
	

	
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
				        changeAlbum();
						return null;
				   }
			});
	    
	    
	    contentPanel = new JPanel();
	    contentPanel.setLayout(null);
	    
	    contentPanel.add(m_jComboBoxAlbum, null);
	  }

	  protected void changeAlbum() {
		  if (m_jComboBoxAlbum.getSelectedIndex()!=0){
			  IMQuery q = new IMQuery();
			  display(q.selectPicturesFromAlbum((Album)m_jComboBoxAlbum.getSelectedItem()));
		  } else {
			  display(new ArrayList<Picture>());
		  }
	  }
	  
	  void display(ArrayList<Picture> array){
		  if (array.size()==0){
			  new IMMessage(IMConstants.ERROR, "NO_PICTURE");
			  contentPanel.removeAll();
			  contentPanel.add(m_jComboBoxAlbum, null);
			  contentPanel.revalidate();
			  SwingUtilities.updateComponentTreeUI(this);
		  } else {
			  m_iX = m_iStartX;
			  m_iY = m_iStartY;
			  Iterator<Picture> it = array.iterator();
			  while (it.hasNext()){
				  final Picture p = it.next();
				  if (m_iX > m_nWidth){ m_iX = m_iStartX; m_iY += (THUMB_WIDTH+m_iOffset); }
				  JLabel userPictureLabel = IMImage.showPicture(p.getPictureThumbnail(), new Rectangle(m_iX, m_iY, THUMB_WIDTH, THUMB_HEIGHT), p.getPictureName());
				  userPictureLabel.addMouseListener(new MouseListener() {			
						@Override
						public void mouseReleased(MouseEvent e) {
						}		
						@Override
						public void mousePressed(MouseEvent e) {
						}		
						@Override
						public void mouseExited(MouseEvent e) {
						}		
						@Override
						public void mouseEntered(MouseEvent e) {	
						}
						@Override
						public void mouseClicked(MouseEvent e) {
							new JDialogExtendedPicturePopup(m_jFrameOwner, p.getPicture());
						}
					});
				  m_iX += (THUMB_WIDTH+m_iOffset);
				  contentPanel.add(userPictureLabel, null);
			  }
			  contentPanel.setSize(new Dimension(m_nWidth, m_iFieldHeight*2+(array.size()/(m_nWidth/(THUMB_WIDTH+m_iOffset)))*(THUMB_HEIGHT+m_iOffset)));
			  contentPanel.setPreferredSize(new Dimension(m_nWidth, m_iFieldHeight*2+(array.size()/(m_nWidth/(THUMB_WIDTH+m_iOffset)))*(THUMB_HEIGHT+m_iOffset)));
			  contentPanel.revalidate();
		  }
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
