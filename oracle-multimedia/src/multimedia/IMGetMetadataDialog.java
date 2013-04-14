package multimedia;
/* Copyright (c) 2004, 2008, Oracle. All rights reserved.  */

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.FocusTraversalPolicy;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Box;

import java.sql.SQLException;

import java.io.IOException;
import java.io.StringReader;

import oracle.xdb.XMLType;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLConstants;
import oracle.xml.parser.v2.XMLParseException;

import org.xml.sax.SAXException;

import bean.XMLTreeView;

import oracle.ord.im.OrdImage;

/**
 * The IMGetMetadataDialog shows a dialog to display detailed information
 * for metadata in a product photo.
 */
@SuppressWarnings("serial")
public class IMGetMetadataDialog extends JDialog implements IMConstants
{
  OrdImage          m_img = null;
  XMLTreeView       m_xmlTreeView = new XMLTreeView();
  JButton           m_jButtonClose = null;

  /**
   * Constructs the get metadata dialog.
   * @param parent       the parent frame that created this dialog
   * @param img          the product photo of OrdImage type
   * @param sMetaType    the metadata type to retrieve
   * @param sDescription the product description
   * @param tm           the table model to display product_information
   * @param row          the row number in the product_information table
   *                     corresponding to this product
   */
  public IMGetMetadataDialog(JDialog parent, OrdImage img, String sMetaType)
  {
    super(parent, sMetaType + " Metadata", true);
    this.getAccessibleContext().setAccessibleDescription(
        IMMessage.getString("GMDIAG_DESC"));

    m_img = img;

    setupDisplay(sMetaType);
    displayMetadata(sMetaType);
    setFocusPolicy();
  }

  /**
   * Retrieves metadata from the image using OrdImage.getMetadata
   * and displays the metadata.
   * @param sMetaType the metadata type  
   */
  private void displayMetadata(String sMetaType) 
  {
    XMLDocument doc = null;
    try
    {
      //
      // Retrieves the metadata into an XMLType array.
      //
      XMLType xmlList[] = m_img.getMetadata(sMetaType);

      if (xmlList.length == 1)
      {
        DOMParser parser = new DOMParser();
        parser.setValidationMode(XMLConstants.NONVALIDATING);
        parser.setPreserveWhitespace(false);
        parser.parse(new StringReader(XMLType.createXML(xmlList[0]).getStringVal()));
        doc = parser.getDocument();
      }
    }
    catch (SQLException e)
    {
      new IMMessage(IMConstants.ERROR, "GM_FAIL", e);
    }
    catch (XMLParseException e)
    {
      new IMMessage(IMConstants.ERROR, "GM_FAIL", e);
    }
    catch (SAXException e)
    {
      new IMMessage(IMConstants.ERROR, "GM_FAIL", e);
    }
    catch (IOException e)
    {
      new IMMessage(IMConstants.ERROR, "GM_FAIL", e);
    }

    if (doc == null)
    {
      JLabel lbl = new JLabel(sMetaType + " metadata does not exist!");
      lbl.setHorizontalAlignment(JLabel.CENTER);
      this.getContentPane().add(lbl, BorderLayout.CENTER);
    }
    else
    {
      m_xmlTreeView.setXMLDocument(doc.getDocumentElement());
      m_xmlTreeView.setFocus();
      this.getContentPane().add(m_xmlTreeView, BorderLayout.CENTER);
    }

  }

  /**
   * Sets up the display of the dialog. 
   */
  private void setupDisplay(String sMetaType) 
  {
    this.setSize(new Dimension(375, 500));
    IMUIUtil.initJDialogHelper(this);

    m_jButtonClose = new JButton("Close");
    m_jButtonClose.setMnemonic('C');
    m_jButtonClose.setToolTipText("Close this window");
    m_jButtonClose.addKeyListener(new java.awt.event.KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) 
            close();
        }
      });
    m_jButtonClose.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          close();
        }
      });

    Box hbox = Box.createHorizontalBox();
    hbox.add(Box.createHorizontalGlue());
    hbox.add(m_jButtonClose);
    hbox.add(Box.createHorizontalGlue());

    this.getContentPane().add(hbox, BorderLayout.SOUTH);
  }

  /** 
   * Closes the dialog window.
   */
  void close()
  {
    this.setVisible(false);
    this.dispose();
  }

  private void setFocusPolicy() 
  {
    FocusTraversalPolicy policy = new FocusTraversalPolicy() 
    {
      public Component getDefaultComponent(Container focusCycleRoot) 
      {
        return m_xmlTreeView.focusComponent();
      }
      public Component getFirstComponent(Container focusCycleRoot) 
      {
        return m_xmlTreeView.focusComponent();
      }
      public Component getLastComponent(Container focusCycleRoot) 
      {
        return m_jButtonClose;
      }
      public Component getComponentAfter(Container focusCycleRoot,
          Component component) 
      {
        if (component == m_xmlTreeView.focusComponent()) 
        {
          return m_jButtonClose;
        }
        if (component == m_jButtonClose)
        {
          return m_xmlTreeView.focusComponent();
        }

        return null;
      }

      public Component getComponentBefore(Container focusCycleRoot,
          Component component) 
      {
        if (component == m_xmlTreeView.focusComponent()) 
        {
          return m_jButtonClose;
        }
        if (component == m_jButtonClose)
        {
          return m_xmlTreeView.focusComponent();
        }

        return null;
      }
    }; 

    this.setFocusTraversalPolicy(policy);
  }
}
