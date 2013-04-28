

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

  protected JScrollPane contentPanel;
  protected IMFrame m_jFrameOwner;
  protected OrdImage m_img;
  protected ImageIcon picture;

  protected int m_nOffset = 150;
  protected int m_nMouseScrollSpeed = 16;


  public JDialogProfilePic(IMFrame jFrameOwner, OrdImage image)
  {
    super(jFrameOwner, true);
    m_jFrameOwner = jFrameOwner;
    m_szTitle = "PICTURE_ORIGINAL";
    m_img = image;

    try
    {	
      setupContentPane();
      setupDisplay();
      setupButton();
      

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
  protected void setupButton()
  {

  }

  /**
   * Draws the content pane.
   */
  protected void setupContentPane() 
  {
		try {
			byte[] thumbnail = IMImage.getDataInByteArray(m_img);
			picture = new ImageIcon(thumbnail);
			m_nWidth = (int) (m_jFrameOwner.getScreenWidth() - m_nOffset);
			m_nHeight = (int) (m_jFrameOwner.getScreenHeight() - m_nOffset);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_ERR", e);
		} catch (IOException e) {
			new IMMessage(IMConstants.ERROR, "APP_ERR", e);
		}
		
		
		
		JLabel l = new JLabel(picture);
		
		
	    contentPanel = new JScrollPane(l);
	    contentPanel.setPreferredSize(new Dimension(m_nWidth, m_nHeight));
	    contentPanel.setSize(new Dimension(m_nWidth, m_nHeight));
	    contentPanel.getVerticalScrollBar().setUnitIncrement(m_nMouseScrollSpeed );
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
