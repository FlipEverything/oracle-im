

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import oracle.ord.im.OrdImage;

import bean.Album;


import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Callable;

/**
 * Displays the login dialog and creates the connection
 * to the database.
 */
@SuppressWarnings("serial")
public class JDialogPicturePopup extends JDialog implements IMConstants
{
  protected String m_szTitle;	
  protected int m_nWidth;
  protected int m_nHeight;

  private JPanel contentPanel;
  private IMFrame m_jFrameOwner;
  private OrdImage m_img;
  private ImageIcon picture;

  public JDialogPicturePopup(IMFrame jFrameOwner, OrdImage image)
  {
    super(jFrameOwner, true);
    m_jFrameOwner = jFrameOwner;
    m_szTitle = "PICTURE_ORIGINAL";
    m_img = image;

    try
    {
    	picture = null;
		try {
			byte[] thumbnail = IMImagePanel.getDataInByteArray(m_img);
			picture = new ImageIcon(thumbnail);
			m_nWidth = m_img.getWidth();
			m_nHeight = m_img.getHeight();
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_ERR", e);
		} catch (IOException e) {
			new IMMessage(IMConstants.ERROR, "APP_ERR", e);
		}
		
      setupDisplay();
      setupButton();
      setupContentPane();
      this.getContentPane().add(contentPanel, BorderLayout.CENTER);

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
		JLabel l = new JLabel(picture);
		l.setPreferredSize(new Dimension(m_nWidth, m_nHeight));
		l.setSize(new Dimension(m_nWidth, m_nHeight));
		
	    contentPanel = new JPanel();
	    contentPanel.setLayout(null);
	    contentPanel.add(l, null);
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
    this.addMouseListener(new MouseListener() {
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
			dispose();
			setVisible(false);
		}
	});

    IMUIUtil.initJDialogHelper(this);
  }
  
}
