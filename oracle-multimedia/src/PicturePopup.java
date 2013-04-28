import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import bean.Picture;
import bean.User;


@SuppressWarnings("serial")
class PicturePopup extends JPopupMenu {
    JMenuItem m_itemEdit;
    JMenuItem m_itemDelete;
    
    final Callable<Void> m_callableFunc;
    
    Picture m_picture;
    User m_userDisplayed;
	private IMFrame m_jFrameOwner;
    
    public PicturePopup(Picture p,  User user, IMFrame m_jFrameOwner, Callable<Void> func){
    	this.m_picture = p;
    	this.m_callableFunc = func;
    	this.m_userDisplayed = user;
    	this.m_jFrameOwner = m_jFrameOwner;
    	
        m_itemEdit = new JMenuItem(IMMessage.getString("EDIT"), new ImageIcon((IMFrame.class.getResource("icons/edit.png"))));
        m_itemDelete = new JMenuItem(IMMessage.getString("DELETE"), new ImageIcon((IMFrame.class.getResource("icons/delete.png"))));
        
        m_itemEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				edit();
			}
		});
        
        m_itemDelete.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();				
			}
		});
        
        add(m_itemEdit);
        add(m_itemDelete);
    }
    
    
	protected void delete() {
		IMQuery q = new IMQuery();
		boolean success = q.deletePicture(m_picture);
		
		if (success){
			new IMMessage(IMMessage.WARNING, "DELETE_SUCCESS");
			m_userDisplayed.setPictureSum(m_userDisplayed.getPictureSum()-1);
			try {
				m_callableFunc.call();
			} catch (Exception e1) {
				new IMMessage(IMConstants.ERROR, "APP_ERR", e1);
			}
		}
		
		
	}

	protected void edit() {
		m_jFrameOwner.showEditPicturePanel(m_userDisplayed, m_picture);
	}
}