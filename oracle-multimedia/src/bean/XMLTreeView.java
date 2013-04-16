package bean;



import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import javax.swing.tree.DefaultTreeModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Component;

import org.w3c.dom.Node;

/**
 * The XMLTreeView class shows an XML document as a tree.
 */
@SuppressWarnings("serial")
public class XMLTreeView extends JPanel
{
  private JTree m_jTree = null;
  private JScrollPane m_jScrollPane = null;
  private DefaultTreeModel m_treeModel = null;

  /**
   * Constructs XMLTreeView. 
   */
  public XMLTreeView()
  {
    setLayout(new BorderLayout());
    add("Center", getScrollPane());
  }

  /**
   * Associates the XMLTreeViewer with an XML node.
   *
   * @param xml  The XML node to display.
   */
  public void setXMLDocument(Node xml)
  {
    m_treeModel =  new DefaultTreeModel(new XMLTreeNode(xml));
    getTree().setModel(m_treeModel);
    getTree().updateUI();
  }

  /**
   * Forces the XMLTreeView to update or refresh the user interface.
   */
  public void updateUI()
  {
    super.updateUI();
    getTree().updateUI();
    getScrollPane().updateUI();
  }
  
  /**
   * Returns the XMLTreeView preferred size.
   *
   * @return The <code>Dimension</code> object containing
   * the XMLTreeView prefered size.
   */
  public Dimension getPreferredSize()
  {
    return new Dimension(100, getTree().getRowHeight() * 6);
  }

  private JScrollPane getScrollPane()
  {
    if(m_jScrollPane == null)
      m_jScrollPane = new JScrollPane(getTree());
    return m_jScrollPane;
  }

  private JTree getTree()
  {
    if(m_jTree == null)
    {
      m_jTree = new JTree();
      m_jTree.setRootVisible(true);
      m_jTree.setEditable(false);
      m_jTree.setVisibleRowCount(6);
    }
    return m_jTree;
  }

  public void setFocus()
  {
    m_jTree.setSelectionRow(0);
  }

  public Component focusComponent()
  {
    return m_jTree;
  }
}
