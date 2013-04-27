

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import oracle.ord.im.OrdImage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Displays the login dialog and creates the connection
 * to the database.
 */
@SuppressWarnings("serial")
public class JDialogProfilePic extends JDialog implements IMConstants
{
  protected String m_szTitle;	
  protected int m_nWidth;
  protected int m_nHeight;

  private JScrollPane contentPanel;
  private IMFrame m_jFrameOwner;
  private OrdImage m_img;
  private ImageIcon picture;
  
  boolean needSet = false;

  public JDialogProfilePic(IMFrame jFrameOwner, OrdImage image)
  {
    super(jFrameOwner, true);
    m_jFrameOwner = jFrameOwner;
    m_szTitle = "PICTURE_ORIGINAL";
    m_img = image;

    try
    {
    	picture = null;
		try {
			byte[] thumbnail = IMImage.getDataInByteArray(m_img);
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
      
      
      if (m_nWidth>m_jFrameOwner.getScreenWidth()){
    	  m_nWidth = (int) (m_jFrameOwner.getScreenWidth() - 50);
    	  needSet = true;
      } 
      
      if (m_nHeight>m_jFrameOwner.getScreenHeight()){
    	  m_nHeight = (int) (m_jFrameOwner.getScreenHeight() - 50);
    	  m_nWidth += 40;
    	  needSet = true;
       } 
      if (needSet){
    	  this.setSize(new Dimension(m_nWidth, m_nHeight));
    	  this.setPreferredSize(new Dimension(m_nWidth, m_nHeight));
      }
      
      this.getContentPane().add(contentPanel, BorderLayout.CENTER);

    }
    catch (NullPointerException e1)
    {
    	new IMMessage(IMConstants.ERROR, "FILE_NOTFOUND", e1);
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
		JLabel l = new JLabel(picture);
		l.setPreferredSize(new Dimension(m_nWidth, m_nHeight));
		l.setSize(new Dimension(m_nWidth, m_nHeight));
		
	    contentPanel = new JScrollPane(l);
	    contentPanel.addMouseListener(new MouseListener() {
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
  }


  /**
   * Initializes the dialog display.
   */
  private void setupDisplay()
  {
    this.setTitle(IMMessage.getString(m_szTitle));
    this.setSize(new Dimension(m_nWidth+10, m_nHeight+30));
    this.getAccessibleContext().setAccessibleDescription(
        IMMessage.getString("LOGINDIAG_DESC"));
    this.getContentPane().setLayout(new BorderLayout());
    this.setResizable(false);
    

    IMUIUtil.initJDialogHelper(this);
  }
  
}
