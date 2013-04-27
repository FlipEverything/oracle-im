import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
	protected JButton m_jButtonEdit = new JButton();
	protected JButton m_jButtonDelete = new JButton();
	
	protected String m_szTitle;	
	protected int m_nWidth;
	protected int m_nHeight = 200;
	
	protected int m_iFieldHeight = 30;
	protected int m_iFieldWidth = 120;
	protected int m_iDeltaY = 10;
	
	private int m_iOffset = 20;
	private int m_iStartX = 15;
	private int m_iStartY = 15 + m_iFieldHeight + m_iOffset;
	private int m_iX = 0;
	private int m_iY = 0;
	

	
	public JPanelAlbums(IMFrame frame, User user){
		this.m_jFrameOwner = frame;
		this.m_userDisplayed = user;
		this.m_nWidth = frame.getWidth() - 100;
		this.m_szTitle = IMMessage.getString("MAIN_MENU_ALBUMS")+": "+ user.getUsername() + " "+ (ownAlbum() ? IMMessage.getString("OWN") : "");
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
		  ArrayList<Album> array = new ArrayList<Album>();
		  
		  if (ownAlbum()){
			  array = q.getAlbumsByUser(m_userDisplayed);
		  } else {
			  array = q.getPublicAlbumsByUser(m_userDisplayed);
		  }
		  
		  m_jComboBoxAlbum = IMFrame.createComboBox(m_jComboBoxAlbum, true, 0, new Rectangle(m_iDeltaY, m_iDeltaY, m_nWidth-m_iDeltaY*2-m_iFieldWidth*3, m_iFieldHeight), 
					array, new Callable<Void>() {
				   public Void call() {
				        changeAlbum();
				        setButtonState();
						return null;
				   }
			});
		  
		  
		  
	    
	    
	    contentPanel = new JPanel();
	    contentPanel.setLayout(null);
	    
	    contentPanel.add(m_jComboBoxAlbum, null);
	    
	    if (ownAlbum()){
			  m_jButtonEdit = IMFrame.createButton(m_jButtonEdit, "EDIT", 'S', "edit", "EDIT_DESC", new Rectangle(m_nWidth+m_iDeltaY*2-m_iFieldWidth*3, m_iDeltaY, m_iFieldWidth, m_iFieldHeight), 
					  new Callable<Void>() {
					  public Void call() {
					        edit((Album)m_jComboBoxAlbum.getSelectedItem());
							return null;
					   }
			  });
			  m_jButtonDelete = IMFrame.createButton(m_jButtonDelete, "DELETE", 'T', "delete", "DELETE_DESC", new Rectangle(m_nWidth+m_iDeltaY*2-m_iFieldWidth*2, m_iDeltaY, m_iFieldWidth, m_iFieldHeight), 
					  new Callable<Void>() {
					  public Void call() {
					        delete((Album)m_jComboBoxAlbum.getSelectedItem());
							return null;
					   }
			  });
			  
			  m_jButtonDelete.setEnabled(false);
			  m_jButtonEdit.setEnabled(false);
			  
			  contentPanel.add(m_jButtonEdit, null);
			  contentPanel.add(m_jButtonDelete, null);
		  }
	    
	  }

	 protected void delete(Album selectedItem) {
		IMQuery q = new IMQuery();
		m_userDisplayed.setPictureSum(m_userDisplayed.getPictureSum()-q.deletePicture(selectedItem));
		
			boolean success = q.deleteAlbum(selectedItem);
			if (success){
				new IMMessage(IMMessage.WARNING, "DELETE_SUCCESS");
				m_jComboBoxAlbum.removeItem(selectedItem);
			}
		
	}

	protected void edit(Album selectedItem) {
		JDialogEditAlbum editAlbumDialog = new JDialogEditAlbum(m_jFrameOwner, selectedItem);
		editAlbumDialog.setVisible(true);
		
	}

	protected void setButtonState() {
		if (m_jComboBoxAlbum.getSelectedIndex()==0){
			m_jButtonDelete.setEnabled(false);
			m_jButtonEdit.setEnabled(false);
		} else {
			m_jButtonDelete.setEnabled(true);
			m_jButtonEdit.setEnabled(true);
		}
		
	}
	 

	private boolean ownAlbum() {
		 if (m_userDisplayed.getUserId()==m_jFrameOwner.getUserActive().getUserId()){
			 return true;
		 }
		 
		return false;
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
		  Component[] comps = contentPanel.getComponents();
		  for (int i=0; i<comps.length; i++){
			  if (comps[i] instanceof PictureLabel){
				  contentPanel.remove(comps[i]);
			  } 
		  }
		  
		  if (array.size()==0){
			  //new IMMessage(IMConstants.ERROR, "NO_PICTURE");
		  } else {
			  m_iX = m_iStartX;
			  m_iY = m_iStartY;
			  Iterator<Picture> it = array.iterator();
			  while (it.hasNext()){
				  final Picture p = it.next();
				  if (m_iX > m_nWidth){ m_iX = m_iStartX; m_iY += (THUMB_WIDTH+m_iOffset); }
				  
				  PictureLabel userPictureLabel = new PictureLabel(p, new Rectangle(m_iX, m_iY, THUMB_WIDTH, THUMB_HEIGHT));
				  
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
							if (SwingUtilities.isLeftMouseButton(e)){
								JDialogPicture popup = new JDialogPicture(m_jFrameOwner, p.getPicture());
								popup.setVisible(true);
							} else if (SwingUtilities.isRightMouseButton(e)){
								if (((Album)m_jComboBoxAlbum.getSelectedItem()).getUserId()==m_jFrameOwner.getUserActive().getUserId()){
									doPop(e);	
								}
								
							}
							
						}
						private void doPop(MouseEvent e){
							
							PicturePopup menu = new PicturePopup( ((PictureLabel)e.getSource()).getPicture(), m_userDisplayed, new Callable<Void>() {
								   public Void call() {
									    IMQuery q = new IMQuery();
									    display(q.selectPicturesFromAlbum((Album)m_jComboBoxAlbum.getSelectedItem()));
										return null;
								   }
							});
					        menu.show(e.getComponent(), e.getX(), e.getY());
					    }
					});
				  m_iX += (THUMB_WIDTH+m_iOffset);
				  contentPanel.add(userPictureLabel, null);
			  }
			  contentPanel.setSize(new Dimension(m_nWidth, m_iFieldHeight*2+(array.size()/(m_nWidth/(THUMB_WIDTH+m_iOffset)))*(THUMB_HEIGHT+m_iOffset)));
			  contentPanel.setPreferredSize(new Dimension(m_nWidth, m_iFieldHeight*2+(array.size()/(m_nWidth/(THUMB_WIDTH+m_iOffset)))*(THUMB_HEIGHT+m_iOffset)));

		  }
		  
		  refresh();
	  }
	  
	 public void refresh(){
		 contentPanel.revalidate();
		 SwingUtilities.updateComponentTreeUI(JPanelAlbums.this);
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
