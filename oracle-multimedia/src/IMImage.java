import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import oracle.ord.im.OrdImage;
import oracle.sql.BFILE;

import bean.Category;
import bean.City;
import bean.Comment;
import bean.Country;
import bean.Keyword;
import bean.Picture;
import bean.Rating;
import bean.Region;


import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;


/**
 * A kép adatlap ablakot hozza létre
 * A bal oldali panelban szerepel a nagy kép, a jobb oldali panelban pedig a képadatok és a módosításhoz szükséges elemek
 * 
 */
@SuppressWarnings("serial")
public class IMImage extends JDialogProfilePic implements IMConstants
{

  public static final int MAX_RATING_VALUE = 5;
	
  JPanel sidePanel;
  int m_nSidePanelWidth;
  int m_nButtonWidth;
  
  JButton m_jButtonSave;
  JButton m_jButtonManipulate;
  JButton m_jButtonZoomOut;
  JButton m_jButtonRate;
  JButton m_jButtonComment;
  
  JComboBox m_jComboRating;
  
  JLabel m_jLabelData;
  
  protected int m_iFieldHeight;	
  protected int m_nButtonHeight;
  protected int m_nButtonStart;
  protected int m_nLastButtonStart;
  private int m_nButtonY;
  private int m_nButtonX1;
  private int m_nButtonX2;
  
  private boolean m_bAlreadyRated = false;
  private Rating m_ratingActual = null;
  
  String[] m_attrColNames = {"Attribútum", "Érték"};
  private Picture m_pictureDisplayed;

  private int m_nPanelHeight;

  public IMImage(IMFrame jFrameOwner, Picture p)
  {
    super(jFrameOwner, p.getPicture());
    this.m_pictureDisplayed = p;
    
    createRatingCombo();
    
    try {
		insertProperty();
	} catch (SQLException e) {
		new IMMessage(IMMessage.ERROR, "SQL_FAIL", e);
	}
    
    refreshLabel(m_jLabelData);
    m_jLabelData.setBounds(new Rectangle(m_nButtonX1, m_nButtonY+=(m_iFieldHeight+m_nPanelHeight), m_nSidePanelWidth, contentPanel.getHeight()/3));
    

    
    sidePanel.add(m_jLabelData, null);
    
    JScrollPane pane = new JScrollPane(sidePanel);
    pane.setSize(new Dimension(m_nSidePanelWidth, contentPanel.getHeight()));
    pane.setPreferredSize(new Dimension(m_nSidePanelWidth, contentPanel.getHeight()));
    this.getContentPane().add(pane, BorderLayout.EAST);
  }
  
  private void createRatingCombo() {
	  Iterator<Rating> ratingIt = m_pictureDisplayed.getRating().iterator();

	    while (ratingIt.hasNext()){
	    	Rating r = ratingIt.next();
	    	if (r.getUserId()==m_jFrameOwner.getUserActive().getUserId()){
	    		m_bAlreadyRated = true;
	    		m_ratingActual = r;
	    		break;
	    	}
	    }
	    
	    
	    ArrayList<Integer> arrayNumbers = new ArrayList<Integer>();
	    for (int count=1; count<=MAX_RATING_VALUE; count++){
	    	arrayNumbers.add(count);
	    }
	    

	    int i = 0;
	    IMFrame.createComboBox(m_jComboRating, true, 0, 
	    		new Rectangle(i%2==0?m_nButtonX1:m_nButtonX2, i++%2==0?m_nButtonY+=(m_nButtonHeight):m_nButtonY, m_nButtonWidth  , m_nButtonHeight),
	    		arrayNumbers, new Callable<Void>() {
			   		public Void call() {
			            setComboStatus();
			   			return null;
			   		}
	    });
	    
	    if (m_bAlreadyRated){
    			m_jComboRating.setSelectedIndex(m_ratingActual.getValue());
    			m_jComboRating.setEnabled(false);
    	}
	    m_jButtonRate.setEnabled(false);
	    
	    
	    IMFrame.createButton(m_jButtonRate, "RATE", 'N', "rate", "LOGINDIAG_CANCEL_BUTTON_DESC", 
	    		new Rectangle(i%2==0?m_nButtonX1:m_nButtonX2, i++%2==0?m_nButtonY+=(m_nButtonHeight):m_nButtonY, m_nButtonWidth  , m_nButtonHeight), 
	    		new Callable<Void>() {
			   		public Void call() {
			   			rate();
			   			return null;
			   		}
		});
	
  }

  /**
   * Újrarajzolja a felületet, így frissütjük a GUI-t
   * @param label
   */
  public void refreshLabel(JLabel label){
	    label.setText("");
	  
	    displayName(m_jLabelData, m_pictureDisplayed);
	    displayPlace(m_jLabelData, m_pictureDisplayed);
	    displayCategories(m_jLabelData, m_pictureDisplayed);
	    displayKeywords(m_jLabelData, m_pictureDisplayed);
	    displayRating(m_jLabelData, m_pictureDisplayed);
	    displayComments(m_jLabelData, m_pictureDisplayed);
	    
	    label.setText("<html>"+m_jLabelData.getText()+"</html>");
	    
	    
	    
  }

  /**
   * Draws buttons.
   */
   @Override
  protected void setupButton()
  {
	   m_jButtonSave = new JButton();
	   m_jButtonManipulate = new JButton();
	   m_jButtonZoomOut = new JButton();
	   m_jButtonRate = new JButton();
	   m_jButtonComment = new JButton();
	   
	   m_jComboRating = new JComboBox();

	   int i = 0;
	   
	   IMFrame.createButton(m_jButtonSave, "SAVE", 'L', "save", "LOGINDIAG_LOGIN_BUTTON_DESC", 
	    		new Rectangle(i%2==0?m_nButtonX1:m_nButtonX2, i++%2==0?m_nButtonY:m_nButtonY, m_nButtonWidth  , m_nButtonHeight), 
	    		new Callable<Void>() {
			   		public Void call() {
			   			saveToFile();
			   			return null;
			   		}
		});
	    
	    IMFrame.createButton(m_jButtonManipulate, "MANIPULATE", 'N', "manipulate", "LOGINDIAG_CANCEL_BUTTON_DESC", 
	    		new Rectangle(i%2==0?m_nButtonX1:m_nButtonX2, i++%2==0?m_nButtonY+=(m_nButtonHeight):m_nButtonY, m_nButtonWidth  , m_nButtonHeight), 
	    		new Callable<Void>() {
			   		public Void call() {
			        
			   			return null;
			   		}
		});
	    
	    
	    
	    IMFrame.createButton(m_jButtonZoomOut, "ZOOMOUT", 'N', "zoom_out", "LOGINDIAG_CANCEL_BUTTON_DESC", 
	    		new Rectangle(i%2==0?m_nButtonX1:m_nButtonX2, i++%2==0?m_nButtonY+=(m_nButtonHeight):m_nButtonY, m_nButtonWidth  , m_nButtonHeight), 
	    		new Callable<Void>() {
			   		public Void call() {
			        
			   			return null;
			   		}
		});
	    
    
	    IMFrame.createButton(m_jButtonComment, "COMMENT", 'N', "comment", "LOGINDIAG_CANCEL_BUTTON_DESC", 
	    		new Rectangle(i%2==0?m_nButtonX1:m_nButtonX2, i++%2==0?m_nButtonY+=(m_nButtonHeight):m_nButtonY, m_nButtonWidth  , m_nButtonHeight), 
	    		new Callable<Void>() {
			   		public Void call() {
			   			comment();
			   			return null;
			   		}
		});
	    

	    
	    sidePanel.add(m_jComboRating, null);
	    sidePanel.add(m_jButtonSave, null);
	    sidePanel.add(m_jButtonManipulate, null);
	    sidePanel.add(m_jButtonZoomOut, null);
	    sidePanel.add(m_jButtonRate, null);
	    sidePanel.add(m_jButtonComment, null);
	    
  }

  protected void saveToFile() {
	try {
		new IMSaveFile(this, IMMessage.getString("SAVE"), IMImage.getDataInByteArray(m_pictureDisplayed.getPicture()));
	} catch (SQLException e) {
		new IMMessage(IMMessage.ERROR, "APP_ERR");
	} catch (IOException e) {
		new IMMessage(IMMessage.ERROR, "APP_ERR");
	}
  }

protected void rate() {
	IMQuery q = new IMQuery();
	Rating r = new Rating(m_pictureDisplayed.getPictureId(), m_jFrameOwner.getUserActive().getUserId(), m_jComboRating.getSelectedIndex());
	boolean success = q.insertRating(r);
	if (success){
		m_pictureDisplayed.getRating().add(r);
		m_jButtonRate.setEnabled(false);
		m_jComboRating.setEnabled(false);
		refreshLabel(m_jLabelData);
	}
  }

	protected void setComboStatus() {
		if (m_jComboRating.getSelectedIndex()==0){
			m_jButtonRate.setEnabled(false);
		} else {
			m_jButtonRate.setEnabled(true);
		}
	  }

	protected void comment() {
		JDialogNewComment newComment = new JDialogNewComment(m_jFrameOwner, m_pictureDisplayed);
		newComment.setVisible(true);
		refreshLabel(m_jLabelData);
	  }

/**
   * Draws the content pane.
   */
   @Override
  protected void setupContentPane() 
  {
		super.setupContentPane();
		
		m_nSidePanelWidth = 300;
		m_nButtonWidth = 120;
		m_iFieldHeight = 30;
		m_nButtonHeight = 35;
		m_nButtonStart = 70;
		m_nButtonY = 10;
		m_nLastButtonStart = m_nButtonStart;
		m_nButtonX1 = 20;
		m_nButtonX2 = m_nButtonX1+m_nButtonWidth;
		m_nPanelHeight = 150;
		
		contentPanel.setSize(new Dimension( contentPanel.getWidth()-m_nSidePanelWidth, contentPanel.getHeight()));
	    contentPanel.setPreferredSize(new Dimension( contentPanel.getWidth()-m_nSidePanelWidth, contentPanel.getHeight()));
	    
	    sidePanel = new JPanel();
	    sidePanel.setLayout(null);
	   
	    m_jLabelData = new JLabel();
	    
  }
   
   protected JLabel displayKeywords(JLabel label, Picture p){
	   
	   append(label, "<b>"+IMMessage.getString("KEYWORDS")+"</b><br/>");
	   
	   Iterator<Keyword> it = p.getKeywords().iterator();
	   while (it.hasNext()){
		   append(label, ((Keyword)it.next())+", ");
	   }
	   removeLastTwoChar(label);
	   append(label, "<br/><br/>");
	   
	   return label;
   }
   
  protected JLabel displayName(JLabel label, Picture p){
	   
	   append(label, "<h1>"+p.getPictureName()+"</h1>");
	   
	   return label;
   }
   
  protected JLabel displayCategories(JLabel label, Picture p){
	   
	   append(label, "<b>"+IMMessage.getString("CATEGORIES")+"</b><br/>");
	   
	   Iterator<Category> it = p.getCategories().iterator();
	   while (it.hasNext()){
		   append(label, ((Category)it.next())+", ");
	   }
	   removeLastTwoChar(label);
	   append(label, "<br/><br/>");
	   
	   return label;
   }
  
  protected JLabel displayComments(JLabel label, Picture p){
	  append(label, "<hr/><br/><b>"+IMMessage.getString("COMMENTS")+"</b><br/>");
	   
	  if (p.getComments().size()==0){
		  append(label, IMMessage.getString("NO_COMMENT"));
	  } else {
		  Iterator<Comment> it = p.getComments().iterator();
		   while (it.hasNext()){
			   append(label, ((Comment)it.next())+"<br/>");
		   }
	  }
	  append(label, "<br/><br/>");
	   
	   
	   return label;
  }
  
  protected JLabel displayRating(JLabel label, Picture p){	   
	  append(label, "<h2>"+IMMessage.getString("RATING")+" ");  
	  if (p.getRating().size()==0){
		  append(label, IMMessage.getString("NO_RATING_VALUE"));
	  } else {
		  append(label, ""+p.getRatingValue());  
	  }
	  append(label, "</h2>");  

	   return label;
  }
  
  
  
  protected JLabel displayPlace(JLabel label, Picture p){	
	  City c = m_jFrameOwner.selectCurrentCity(p.getCityId());
	  Region r = m_jFrameOwner.selectCurrentRegion(c.getRegionId());
	  Country cou = m_jFrameOwner.selectCurrentCountry(r.getCountryId());
	  
	   
	  append(label, "<h4>"+cou+", "+r+", <br/> "+c+"</h4>");  
	    
	   return label;
  }
  
  /**
   * Shows the attribute table using Oracle Multimedia accessor methods 
   * to get image properties.
   */
  boolean insertProperty() throws SQLException
  {
    boolean isFormatSupported = false;
    String sMimeType = m_pictureDisplayed.getPicture().getMimeType();

    if (sMimeType == null)
      isFormatSupported = IMUtil.setProperties(m_pictureDisplayed.getPicture());
    else
      isFormatSupported = true;

    if (!isFormatSupported)
    {
      new IMMessage(IMMessage.ERROR, "NOT_SUPPORTED");
    }
    else
    {
	  Object[][] data = 
      {
        {"MIME typus",  m_img.getMimeType()},
        {"Magasság", new Integer(m_img.getHeight()).toString()},
        {"Szélesség",  new Integer(m_img.getWidth()).toString()},
        {"Tartalom mérete", new Integer(m_img.getContentLength()).toString()}
      };

      IMAttrTableModel tm = new IMAttrTableModel(data, m_attrColNames);

      IMTable m_jAttrTbl = new IMTable(tm);
      JScrollPane jAttrScrollPane = new JScrollPane(m_jAttrTbl);
      jAttrScrollPane.setPreferredSize(new Dimension(300, 90));

      JPanel m_jMetaPanel = new JPanel();
      m_jMetaPanel.setLayout(new GridLayout(1, 2));
      m_jMetaPanel.setFont(new Font("Default", 0, 11));
      m_jMetaPanel.setBorder(BorderFactory.createTitledBorder(
            null, "Metaadat", TitledBorder.LEADING,
            TitledBorder.TOP, new Font("Default", 0, 12)));

      JPanel   gmPanel = new JPanel();
      gmPanel.setLayout(new BorderLayout());


      String[] gmStrings = { "EXIF", "IPTC-IIM", "XMP"};
      JComboBox gmList = new JComboBox(gmStrings);
      gmList.setFont(new Font("Default", 0, 11));
      gmList.setSelectedIndex(0);

      gmList.setToolTipText(IMMessage.getString("GMCOMBOBOX_DESC"));
      gmList.getAccessibleContext().setAccessibleName(
          IMMessage.getString("GMCOMBOBOX_NAME"));

      gmList.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              showMetadata(e);
            }
          });
      gmList.addKeyListener(new java.awt.event.KeyAdapter()
          {
            public void keyPressed(KeyEvent e)
            {
              showMetadata(e);
            }
          });
      
      gmPanel.add(gmList, BorderLayout.CENTER);
      gmPanel.add(new JLabel("    "), BorderLayout.EAST);


      m_jMetaPanel.add(gmPanel);

      Box m_jAttrPaneBox = Box.createVerticalBox();
      m_jAttrPaneBox.add(jAttrScrollPane);
      m_jAttrPaneBox.add(Box.createVerticalGlue());
      m_jAttrPaneBox.add(m_jMetaPanel);
      m_jAttrPaneBox.setBounds(new Rectangle(m_nButtonX1, m_nButtonY+=(m_nButtonHeight+m_iFieldHeight), m_nSidePanelWidth-m_iFieldHeight*2, m_nPanelHeight));
      sidePanel.add(m_jAttrPaneBox, null);
    }

    return isFormatSupported;
  }
  
  /**
   * Displays a window for showing metadata when
   * a mouse button is clicked, or the
   * ENTER or the SPACE key is pressed 
   * on a combobox metadata type.
   */
  void showMetadata(AWTEvent ae)
  {
    JComboBox cb = (JComboBox)ae.getSource();
    String metaType = (String)cb.getSelectedItem();

    if (ae instanceof KeyEvent 
        && ((KeyEvent)ae).getKeyCode() == KeyEvent.VK_ESCAPE)
    {
      cb.setPopupVisible(false);
      return;
    }

    if (ae instanceof KeyEvent 
        && ((KeyEvent)ae).getKeyCode() == KeyEvent.VK_SHIFT)
    {
      return;
    }

    if ( (ae instanceof ActionEvent &&
         (((ActionEvent)ae).getModifiers() == AWTEvent.MOUSE_EVENT_MASK)) ||
         (ae instanceof KeyEvent && 
          !(((KeyEvent)ae).getKeyCode() == KeyEvent.VK_UP ||
              ((KeyEvent)ae).getKeyCode() == KeyEvent.VK_DOWN))
       )
    {
      cb.setPopupVisible(false);
      IMGetMetadataDialog gmDialog =
        new IMGetMetadataDialog((JDialog)new JDialog(), m_img, metaType);
      gmDialog.setVisible(true);
    }
  }
  
     
   protected JLabel append(JLabel label, String text){
	   label.setText(label.getText()+text);
	   return label;
   }
   
   protected JLabel removeLastTwoChar(JLabel label){
	   label.setText(label.getText().substring(0, label.getText().length()-2));
	   return label;
   }
   

   /**
    * Displays the writing metadata dialog when
    * a mouse button is clicked, or 
    * ENTER or SPACE key is pressed 
    * on a combobox metadata type.
    */
   void writeMetadata(AWTEvent ae)
   {
     JComboBox cb = (JComboBox)ae.getSource();
     String metaType = (String)cb.getSelectedItem();

     if (ae instanceof KeyEvent 
         && ((KeyEvent)ae).getKeyCode() == KeyEvent.VK_ESCAPE)
     {
       cb.setPopupVisible(false);
       return;
     }

     if (ae instanceof KeyEvent 
         && ((KeyEvent)ae).getKeyCode() == KeyEvent.VK_SHIFT)
     {
       return;
     }

     if ( (ae instanceof ActionEvent &&
          (((ActionEvent)ae).getModifiers() == AWTEvent.MOUSE_EVENT_MASK)) ||
          (ae instanceof KeyEvent && 
           !(((KeyEvent)ae).getKeyCode() == KeyEvent.VK_UP ||
               ((KeyEvent)ae).getKeyCode() == KeyEvent.VK_DOWN))
        )
     {
       cb.setPopupVisible(false);
         new IMPutMetadataDialog((JDialog)this, m_img, metaType);
     }
   }


   /**
    * Checks whether the image does exist.
    * @return true if BLOB is empty or not associated with a BFILE;
    *         false otherwise
    */
   static boolean notExist(OrdImage img) throws SQLException, IOException
   {
     if (img == null)
       return true;
     else
     {
       if (img.isLocal() && (img.getDataInByteArray() == null))
         return true;
       else if (!img.isLocal() && (":///".equals(img.getSource())))
         return true;
       else
       {
         if (!img.isLocal())
         {
           BFILE bfile = img.getBFILE();
           if (!bfile.fileExists())
             return true;
           else 
             return false;
         }
         else
           return false;
       }
     }
   }

   /**
    * Retrieves image data in a byte array.
    * @param img the image object
    * @return the byte array contains the image data
    */
   static byte[] getDataInByteArray(OrdImage img) throws SQLException, IOException
   {
     if (notExist(img))
       return null;
     else
     {
       if (!img.isLocal())
       {
         byte[] ctx[] = new byte[1][4000];
         try
         {
           img.importData(ctx);
         }
         catch (SQLException e)
         {
           new IMMessage(IMConstants.ERROR, "MEDIA_SOURCE_ERR", e);
           return null;
         }
       }
       return img.getDataInByteArray();
     }
   }


   public static JLabel showPicture(OrdImage image, Rectangle r, String text){
 	  Icon profilePicture = null;
 		
 		if (image==null){
 			profilePicture = new ImageIcon(IMFrame.class.getResource("icons/no_profile_picture.png"));
 		} else {
 			try {
 				byte[] thumbnail = IMImage.getDataInByteArray(image);
 				profilePicture = new ImageIcon(thumbnail);
 			} catch (SQLException e) {
 				new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
 			} catch (IOException e) {
 				new IMMessage(IMConstants.ERROR, "APP_ERR", e);
 			}
 		}
 		
 		JLabel userPictureLabel = new JLabel(null, profilePicture, JLabel.CENTER);
 		userPictureLabel.setBounds(r);
 		userPictureLabel.setToolTipText(text);
 		
 		return userPictureLabel;
   }

  
}
