import java.awt.Rectangle;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import bean.Picture;


@SuppressWarnings("serial")
public class PictureLabel extends JLabel{
	private Picture m_picture;
	
	public PictureLabel(Picture p, Rectangle r){
		  m_picture = p;
		  Icon profilePicture = null;
			
			if (m_picture.getPictureThumbnail()==null){
				profilePicture = new ImageIcon(IMFrame.class.getResource("icons/no_profile_picture.png"));
			} else {
				try {
					byte[] thumbnail = IMImage.getDataInByteArray(m_picture.getPictureThumbnail());
					profilePicture = new ImageIcon(thumbnail);
				} catch (SQLException e) {
					new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
				} catch (IOException e) {
					new IMMessage(IMConstants.ERROR, "APP_ERR", e);
				}
			}
			
			setIcon(profilePicture);
			setHorizontalAlignment(JLabel.CENTER);
			setBounds(r);

			
	  }

	public Picture getPicture() {
		return m_picture;
	}

	public void setPicture(Picture m_picture) {
		this.m_picture = m_picture;
	}
	
	
	
}
