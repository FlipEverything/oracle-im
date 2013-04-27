import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;

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
    
    public PicturePopup(Picture p,  User user, Callable<Void> func){
    	this.m_picture = p;
    	this.m_callableFunc = func;
    	this.m_userDisplayed = user;
    	
        m_itemEdit = new JMenuItem(IMMessage.getString("EDIT"));
        m_itemDelete = new JMenuItem(IMMessage.getString("DELETE"));
        
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
		// TODO Auto-generated method stub
		
	}
}