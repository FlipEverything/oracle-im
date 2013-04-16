


import java.awt.Frame;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Toolkit;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Component;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.AWTEvent;

import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JComponent;

import bean.FocusedJTextArea;
import bean.FocusedJTextField;

import java.sql.SQLException;
import java.sql.ResultSet;

import java.io.IOException;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

import oracle.ord.im.OrdMediaUtil;
import oracle.ord.im.OrdImage;
import oracle.ord.im.OrdAudio;
import oracle.ord.im.OrdVideo;
import oracle.ord.im.OrdDoc;

/**
 * The IMProductDialog shows a dialog to display detailed information
 * for a particular product.
 */
public class IMProductDialog extends JDialog implements IMConstants
{
  boolean m_isProdIlluFocused = true;

  private IMResultSetTableModel m_resultSetTableModel = null;

  private int    m_iProdId = -1;
  private int    m_iRowNumber = -1;
  private String m_sProdName = null;
  private String m_sDescription = null;

  JButton m_jButtonApply  = new JButton();
  JButton m_jButtonRevert = new JButton();

  JPanel m_jMediaPanel   = new JPanel();
  JPanel m_jProductPanel = new JPanel();

  OrdImage          m_img = null;
  OrdImage          m_imgThumb = null;
  OrdAudio          m_aud = null;
  OrdVideo          m_vid = null;
  OrdDoc            m_doc = null;

  IMImagePanel m_jImgPanel = null;

  Color m_colorFieldBg = null;

  /**
   * Constructs the product dialog.
   * @param iProdId      the product id
   * @param sProdName    the product name
   * @param sDescription the product description
   * @param tm           the table model to display product_information
   * @param row          the row number in the product_information table
   *                     corresponding to this product
   */
  public IMProductDialog(int iProdId, String sProdName, String sDescription, 
      IMResultSetTableModel tm, int row)
  {
    this(null, "", iProdId, sProdName, sDescription, tm, row);
  }

  /**
   * Constructs the product dialog.
   * @param parent       the parent frame that created this dialog
   * @param title        the title of this dialog
   * @param iProdId      the product id
   * @param sProdName    the product name
   * @param sDescription the product description
   * @param tm           the table model to display product_information
   * @param row          the row number in the product_information table
   *                     corresponding to this product
   */
  public IMProductDialog(Frame parent, String title, int iProdId, 
      String sProdName, String sDescription, IMResultSetTableModel tm, int row)
  {
    super(parent, title, true);
    this.getAccessibleContext().setAccessibleDescription(
        IMMessage.getString("PRODDIAG_DESC"));

    m_iProdId = iProdId;
    m_resultSetTableModel = tm;
    m_iRowNumber = row;
    m_sProdName = sProdName;
    m_sDescription = sDescription;

    setupDialog();
  }

  /**
   * Sets up the frame work of the dialog. Seperates the
   * displays of product illustration, product media and
   * buttons parts.
   */
  private void setupDialog() 
  {
    this.setSize(new Dimension(600, 880));

    this.setTitle("Product Details");

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = this.getSize();

    if (frameSize.height > screenSize.height)
    {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width)
    {
      frameSize.width = screenSize.width;
    }
    this.setLocation((screenSize.width - frameSize.width) / 2, 
        (screenSize.height - frameSize.height) / 2);

    this.addWindowListener(new WindowAdapter()
        {
        public void windowClosing(WindowEvent e)
        {
          m_resultSetTableModel.unsetCheckMedia(m_iRowNumber);
          e.getWindow().setVisible(false);
          e.getWindow().dispose();
        }
        });

    addButtons();

    addProdIllu();

    try
    {
      // Loads and displays this product's media objects.
      loadMedia();
    }
    catch (SQLException e)
    {
      new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
    }
    catch (IOException e)
    {
      new IMMessage(IMConstants.ERROR, "RETRIEVAL_FAILED", e);
    }

    m_jMediaPanel.setPreferredSize(new Dimension(600, 500));

    GridLayout gridLayout = new GridLayout(4, 1);
    m_jMediaPanel.setLayout(gridLayout);

    this.getContentPane().add(m_jMediaPanel, "Center");
  }

  /**
   * Adds the bottom two buttons, for commit or roll back of
   * the changes made to the media objects.
   */
  private void addButtons()
  {
    m_jButtonApply.setText("APPLY");
    m_jButtonApply.setMnemonic('A');
    m_jButtonApply.getAccessibleContext().setAccessibleDescription
      ("Click to commit changes");
    m_jButtonApply.setToolTipText("Commit changes");
    m_jButtonApply.setPreferredSize(new Dimension(90, 40));
    m_jButtonApply.setBounds(new Rectangle(50, 195, 90, 40));
    m_jButtonApply.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          applyChanges(e);
        }
      });
    m_jButtonApply.addKeyListener(new java.awt.event.KeyAdapter()
        {
        public void keyPressed(KeyEvent e)
        {
        applyChanges(e);
        }
        });

    m_jButtonRevert.setText("REVERT");
    m_jButtonRevert.setMnemonic('R');
    m_jButtonRevert.getAccessibleContext().setAccessibleDescription
      ("Click to cancel changes");
    m_jButtonRevert.setToolTipText("Cancel changes");
    m_jButtonRevert.setPreferredSize(new Dimension(90, 40));
    m_jButtonRevert.setBounds(new Rectangle(50, 195, 90, 40));
    m_jButtonRevert.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cancelChanges(e);
        }
      });
    m_jButtonRevert.addKeyListener(new java.awt.event.KeyAdapter()
        {
        public void keyPressed(KeyEvent e)
        {
        cancelChanges(e);
        }
        });

    Box hbox = Box.createHorizontalBox();
    hbox.add(Box.createHorizontalGlue());
    hbox.add(m_jButtonApply);
    hbox.add(Box.createHorizontalGlue());
    hbox.add(m_jButtonRevert);
    hbox.add(Box.createHorizontalGlue());

    this.getContentPane().add(hbox, "South");

  }

  /**
   * Adds the display for product id, name and
   * description.
   */
  private void addProdIllu()
  {
    boolean isFocusNeeded = m_isProdIlluFocused;

    // Lays out the product illustration panel.
    initProdLayout();

    JTextArea prodDesc = null;
    JTextField prodName = null;
    JTextField prodID = null;

    // Sets up the product description text area.
    if (isFocusNeeded)
    {
      prodDesc = new FocusedJTextArea(3,20);
    }
    else
    {
      prodDesc = new JTextArea(3,20);
    }
    prodDesc.setEditable(false);
    prodDesc.setLineWrap(true);
    prodDesc.setWrapStyleWord(true);
    prodDesc.setText(m_sDescription);
    // Sets an accessible name because some screen readers read out punctuation
    // which is annoying.
    prodDesc.getAccessibleContext().setAccessibleName
      ("Product description");
    prodDesc.getAccessibleContext().setAccessibleDescription
      ("This product's description is " + m_sDescription);

    // Sets up the product name text field.
    if (isFocusNeeded)
      prodName = new FocusedJTextField(m_sProdName);
    else
      prodName = new JTextField(m_sProdName);
    prodName.setEditable(false);
    prodName.setBackground(prodDesc.getBackground());
    m_colorFieldBg = prodDesc.getBackground();
    prodName.getAccessibleContext().setAccessibleName("Product name");
    prodName.getAccessibleContext().setAccessibleDescription
      ("This product's name is " + m_sProdName);

    // Sets up the product id textfield.
    if (isFocusNeeded)
      prodID = new FocusedJTextField(new Integer(m_iProdId).toString());
    else
      prodID = new JTextField(new Integer(m_iProdId).toString());
    prodID.setEditable(false);
    prodID.setBackground(prodDesc.getBackground());
    prodID.getAccessibleContext().setAccessibleName("Product ID");
    prodID.getAccessibleContext().setAccessibleDescription
      ("This product's ID is " + m_iProdId);

    // Adds the previous components with the desired labels
    // to the product illustration panel.
    int iNextRow = addProdComponent(m_jProductPanel, "Product Name", prodName, 0);
    iNextRow = addProdComponent(m_jProductPanel, "Product ID", prodID, iNextRow);
    iNextRow = addProdComponent(m_jProductPanel, "Description", 
        new JScrollPane(prodDesc, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
          JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), iNextRow);

    if (isFocusNeeded)
    {
      // This has no effect if m_jProductPanel is not of type
      // FocusedJPanel.
      m_jProductPanel.addFocusListener(new FocusListener()
          {
          public void focusGained(FocusEvent e)
          {
          m_jProductPanel.setBorder(
            UIManager.getBorder("Table.focusCellHighlightBorder")
            );
          }
          public void focusLost(FocusEvent e)
          {
          m_jProductPanel.setBorder(
            BorderFactory.createEmptyBorder()
            );
          }
          });
    }

    this.getContentPane().add(m_jProductPanel, "North");
  }

  /**
   * Loads and displays the media.
   */
  private void loadMedia() throws SQLException, IOException
  {
    String sQuery = 
      "select product_photo, product_thumbnail, product_audio, product_video, " + 
      "product_testimonials from pm.online_media where product_id = ? for update";

    OracleConnection conn = null;
    OracleResultSet rs = null;
    OraclePreparedStatement pstmt = null;
    boolean isInsertNeeded = false;
    byte[] ctx[] = new byte[1][64];

    try
    {
      conn = IMMain.getDBConnection();

      pstmt = (OraclePreparedStatement)conn.prepareStatement(sQuery);
      pstmt.setInt(1, m_iProdId);
      rs = (OracleResultSet)pstmt.executeQuery();
      if (rs.next() == true)
      {
        m_img = (OrdImage)rs.getORAData(1, OrdImage.getORADataFactory());
        m_imgThumb = (OrdImage)rs.getORAData(2, OrdImage.getORADataFactory());
        m_aud = (OrdAudio)rs.getORAData(3, OrdAudio.getORADataFactory());
        m_vid = (OrdVideo)rs.getORAData(4, OrdVideo.getORADataFactory());
        m_doc = (OrdDoc)rs.getORAData(5, OrdDoc.getORADataFactory());
      }

      displayMedia();

      rs.close();
      pstmt.close();
    }
    finally
    {
      IMUtil.cleanup(rs, pstmt);
    }
  }

  /**
   * Displays the product photo, audio, video, and testimonials.
   */
  private void displayMedia() throws SQLException, IOException
  {
    displayImage();
  }

  /**
   * Adds the product photo panel.
   */
  private void displayImage() throws SQLException, IOException
  {
    m_jImgPanel = new IMImagePanel(this, 
        m_img, m_imgThumb, m_iProdId, m_colorFieldBg);
    m_jImgPanel.display();
    m_jImgPanel.getAccessibleContext().setAccessibleName
      ("Product photo panel");
    m_jImgPanel.getAccessibleContext().setAccessibleDescription
      ("Product photo panel with an image icon on the left, " + 
       "image attribute panel in the middle and image control" +
        "panel on the right.");

    m_jMediaPanel.add(m_jImgPanel);

    Component jImgFocus = m_jImgPanel.getFirstFocusComponent();
  }


  /**
   * Calls back when the "APPLY" button is clicked to
   * roll back the changes.
   * @param ae the event passed in
   */
  private void applyChanges(AWTEvent ae)
  {
    try
    {
      if (((ae instanceof KeyEvent) && 
            (((KeyEvent)ae).getKeyCode() == KeyEvent.VK_ENTER)) 
          || (ae instanceof ActionEvent))
      {
        IMMain.commit();

        m_resultSetTableModel.unsetCheckMedia(m_iRowNumber);
        this.setVisible(false);
        this.dispose();
      }
    }
    catch (SQLException e)
    {
      new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
    }
  }

  /**
   * Calls back when the "REVERT" button is clicked to
   * roll back the changes.
   * @param ae the event passed in
   */
  private void cancelChanges(AWTEvent ae)
  {
    try
    {
      if (((ae instanceof KeyEvent) && 
            (((KeyEvent)ae).getKeyCode() == KeyEvent.VK_ENTER)) 
          || (ae instanceof ActionEvent))
      {
        IMMain.rollback();

        m_resultSetTableModel.unsetCheckMedia(m_iRowNumber);
        this.setVisible(false);
        this.dispose();
      }
    }
    catch (SQLException e)
    {
      new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
    }
  }

  /**
   * Lays out the product illustration panel.
   */
  private void initProdLayout()
  {
    m_jProductPanel.setLayout(new GridBagLayout());
    GridBagConstraints constrains = new GridBagConstraints();
    constrains.gridx = 0;
    constrains.gridy = 99;
    constrains.insets = new Insets(10,0,0,0);
    constrains.weighty = 1.0;
    constrains.fill = GridBagConstraints.VERTICAL;
    JLabel verticalFillLabel = new JLabel();
    m_jProductPanel.add(verticalFillLabel, constrains);
  }

  /**
   * Adds the component to a GridBagLayout panel.
   * @param jPanel     the GridBagLayout panel
   * @param sLabelText the label name for the component
   * @param jComponent the component to be added
   * @param row        the row number on the layout for this
   *                   component
   * @return the row number for next component
   */
  private int addProdComponent(JPanel jPanel, String sLabelText, 
      JComponent jComponent, int row)
  {
    int iNextRow = row;

    JLabel jLabel = new JLabel(sLabelText);
    jLabel.setLabelFor(jComponent);

    GridBagConstraints labelConstraints = new GridBagConstraints();
    labelConstraints.gridx = 0;
    labelConstraints.gridy = row;
    labelConstraints.insets = new Insets(10,10,0,0);
    labelConstraints.anchor = GridBagConstraints.NORTHEAST;
    labelConstraints.fill = GridBagConstraints.NONE;

    jPanel.add(jLabel, labelConstraints);

    GridBagConstraints compConstraints = new GridBagConstraints();
    compConstraints.gridx = 1;
    compConstraints.gridy = row;
    compConstraints.insets = new Insets(10,10,0,10);
    compConstraints.weightx = 1.0;
    compConstraints.anchor = GridBagConstraints.WEST;
    compConstraints.fill = GridBagConstraints.HORIZONTAL;

    jPanel.add(jComponent, compConstraints);
    iNextRow++;

    return iNextRow;
  }

}
