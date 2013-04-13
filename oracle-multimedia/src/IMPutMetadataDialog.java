/* Copyright (c) 2004, 2008, Oracle. All rights reserved.  */

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;

import java.sql.SQLException;

import oracle.xdb.XMLType;

import oracle.ord.im.OrdImage;

/**
 * The IMPutMetadataDialog shows a dialog to write 
 * metadata into a product photo.
 */
public class IMPutMetadataDialog extends JDialog implements IMConstants
{
  OrdImage          m_img = null;

  JButton m_jButtonWrite = new JButton();
  JButton m_jButtonCancel = new JButton();

  JLabel m_jLabelTitle = new JLabel();
  JTextField m_jTitleField = new JTextField();

  JLabel m_jLabelCreator = new JLabel();
  JTextField m_jCreatorField = new JTextField();

  JLabel m_jLabelDate = new JLabel();
  JTextField m_jDateField = new JTextField();

  JLabel m_jLabelDescription = new JLabel();
  JTextField m_jDescriptionField = new JTextField();

  JLabel m_jLabelCopyright = new JLabel();
  JTextField m_jCopyrightField = new JTextField();

  /**
   * Constructs the put metadata dialog.
   * @param parent       the parent window that created this dialog
   * @param img          the product photo of OrdImage type
   * @param sMetaType    the metadata type to retrieve
   */
  public IMPutMetadataDialog(JDialog parent, OrdImage img, String sMetaType)
  {
    super(parent, "Example of writing " + sMetaType + " Metadata", true);
    this.getAccessibleContext().setAccessibleDescription(
        IMMessage.getString("PMDIAG_DESC"));

    m_img = img;

    setupDialog(sMetaType);
    setupButton();
    setupContentPane();
    addListener();

    setVisible(true);
  }

  /**
   * Writes the XMP metadata into the image
   * using the OrdImage.putMetadata method. 
   */
  void writeMetadata()
  {
    try
    {
      //
      // Lets the StringBuffer hold the XMP packet.
      //
      StringBuffer sb = new StringBuffer(
          "<xmpMetadata xmlns=\"http://xmlns.oracle.com/ord/meta/xmp\" "
          + " xsi:schemaLocation=\"http://xmlns.oracle.com/ord/meta/xmp "
          + " http://xmlns.oracle.com/ord/meta/xmp\" "
          + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" > "
          + " <rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"> "
          + " <rdf:Description about=\"\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\"> "
          );

      String str = null;
      if ( (str=m_jTitleField.getText()) != null)
        sb.append("<dc:title>" + str + "</dc:title>");
      if ( (str=m_jCreatorField.getText()) !=null)
        sb.append("<dc:creator>" + str + "</dc:creator>");
      if ( (str=m_jDateField.getText()) !=null)
        sb.append("<dc:date>" + str + "</dc:date>");
      if ( (str=m_jDescriptionField.getText()) !=null)
        sb.append("<dc:description>" + str + "</dc:description>");
      if ( (str=m_jCopyrightField.getText()) !=null)
        sb.append("<dc:rights>" + str + "</dc:rights>");

      sb.append("</rdf:Description></rdf:RDF></xmpMetadata>");

      XMLType xmp = XMLType.createXML(IMRunnableMain.getDBConnection(), sb.toString(),
                "http://xmlns.oracle.com/ord/meta/xmp", true, true);

      //
      // Makes sure the image data is local.
      //
      if (!m_img.isLocal())
      {
        byte[] ctx[] = new byte[1][4000];
        m_img.importData(ctx);
      }

      //
      // Calls Ordimage.putMetadata.
      //
      m_img.putMetadata(xmp, "XMP", "utf-8");

      this.dispose();
    }
    catch (SQLException e)
    {
      new IMMessage(IMConstants.ERROR, "PM_FAIL", e);
    }
    catch (Exception e)
    {
      new IMMessage(IMConstants.ERROR, "PM_FAIL", e);
    }
  }

  /**
   * Sets up the framework of the dialog. 
   */
  private void setupDialog(String sMetaType) 
  {
    this.setSize(new Dimension(400, 300));
    this.getContentPane().setLayout(new BorderLayout());
    this.setResizable(false);

    IMUIUtil.initJDialogHelper(this);
  }

  /**
   * Draws buttons.
   */
  private void setupButton()
  {
    m_jButtonWrite.setBounds(new Rectangle(70, 225, 100, 35));
    m_jButtonCancel.setBounds(new Rectangle(225, 225, 100, 35));

    m_jButtonWrite.setText(IMMessage.getString("PMDIAG_WRITE_BUTTON"));
    m_jButtonWrite.setMnemonic('W');
    m_jButtonWrite.setToolTipText(IMMessage.getString("PMDIAG_WRITE_BUTTON_DESC"));

    m_jButtonWrite.addKeyListener(new java.awt.event.KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) 
            writeMetadata();
        }
      });
    m_jButtonWrite.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          writeMetadata();
        }
      });

    m_jButtonCancel.setText(IMMessage.getString("PMDIAG_CANCEL_BUTTON"));
    m_jButtonCancel.setMnemonic('N');
    m_jButtonCancel.setToolTipText(
        IMMessage.getString("PMDIAG_CANCEL_BUTTON_DESC"));

    m_jButtonCancel.addKeyListener(new java.awt.event.KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) 
            cancelWrite();
        }
      });
    m_jButtonCancel.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cancelWrite();
        }
      });
  }

  /**
   * Draws the content pane.
   */
  private void setupContentPane() 
  {
    int iHeight = 30;
    int iDeltaY = 10;
    int iLabelStartY = 20;
    int iFieldStartY = iLabelStartY + 5;
    int iLabelStartX = 25;
    int iFieldStartX = 145;

    m_jLabelTitle.setBounds(
        new Rectangle(iLabelStartX, iLabelStartY, 100, iHeight));
    m_jTitleField.setBounds(
        new Rectangle(iFieldStartX, iFieldStartY, 185, iHeight));

    m_jLabelCreator.setBounds(
        new Rectangle(iLabelStartX, iLabelStartY+iHeight+iDeltaY, 100, iHeight));
    m_jCreatorField.setBounds(
        new Rectangle(iFieldStartX, iFieldStartY+iHeight+iDeltaY, 185, iHeight));

    m_jLabelDate.setBounds(
        new Rectangle(iLabelStartX, iLabelStartY+2*(iHeight+iDeltaY), 100, iHeight));
    m_jDateField.setBounds(
        new Rectangle(iFieldStartX, iFieldStartY+2*(iHeight+iDeltaY), 185, iHeight));

    m_jLabelDescription.setBounds(
        new Rectangle(iLabelStartX, iLabelStartY+3*(iHeight+iDeltaY), 100, iHeight));
    m_jDescriptionField.setBounds(
        new Rectangle(iFieldStartX, iFieldStartY+3*(iHeight+iDeltaY), 185, iHeight));

    m_jLabelCopyright.setBounds(
        new Rectangle(iLabelStartX, iLabelStartY+4*(iHeight+iDeltaY), 100, iHeight));
    m_jCopyrightField.setBounds(
        new Rectangle(iFieldStartX, iFieldStartY+4*(iHeight+iDeltaY), 185, iHeight));

    m_jLabelTitle.setText("Title:");
    m_jLabelTitle.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelTitle.setDisplayedMnemonic('T');
    m_jLabelTitle.setLabelFor(m_jTitleField);

    m_jTitleField.setToolTipText("Enter title");

    m_jLabelCreator.setText("Creator:");
    m_jLabelCreator.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelCreator.setDisplayedMnemonic('C');
    m_jLabelCreator.setLabelFor(m_jCreatorField);

    m_jCreatorField.setToolTipText("Enter creator");

    m_jLabelDate.setText("Date:");
    m_jLabelDate.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelDate.setDisplayedMnemonic('D');
    m_jLabelDate.setLabelFor(m_jDateField);
    
    m_jDateField.setToolTipText("Enter date");

    m_jLabelDescription.setText("Description:");
    m_jLabelDescription.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelDescription.setDisplayedMnemonic('S');
    m_jLabelDescription.setLabelFor(m_jDescriptionField);

    m_jDescriptionField.setToolTipText("Enter description");

    m_jLabelCopyright.setText("Copyright:");
    m_jLabelCopyright.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelCopyright.setDisplayedMnemonic('R');
    m_jLabelCopyright.setLabelFor(m_jCopyrightField);

    m_jCopyrightField.setToolTipText("Enter copyright");

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(null);
    contentPanel.add(m_jTitleField, null);
    contentPanel.add(m_jLabelTitle, null);
    contentPanel.add(m_jCreatorField, null);
    contentPanel.add(m_jLabelCreator, null);
    contentPanel.add(m_jDateField, null);
    contentPanel.add(m_jLabelDate, null);
    contentPanel.add(m_jDescriptionField, null);
    contentPanel.add(m_jLabelDescription, null);
    contentPanel.add(m_jCopyrightField, null);
    contentPanel.add(m_jLabelCopyright, null);

    contentPanel.add(m_jButtonCancel, null);
    contentPanel.add(m_jButtonWrite, null);

    this.getContentPane().add(contentPanel, BorderLayout.CENTER);
  }

  /**
   * Cancels write metadata.
   */
  void cancelWrite()
  {
    this.setVisible(false);
    this.dispose();
  }

  /**
   * Adds the WindowEvent listener.
   */
  private void addListener()
  {
    this.addWindowListener(new WindowAdapter()
        {
          boolean gotFocus = false;
          public void windowOpened(WindowEvent e)
          {
            if (!gotFocus)
            {
              m_jTitleField.requestFocus();
              gotFocus =true;
            }
          }
        });
  }
}
