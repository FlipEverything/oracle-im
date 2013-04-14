

import javax.swing.tree.DefaultMutableTreeNode;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.ProcessingInstruction;

/**
 * The XMLTreeNode class extends DefaultMutableTreeNode
 * to represent an XML node.
 */
@SuppressWarnings("serial")
class XMLTreeNode extends DefaultMutableTreeNode
{

  private boolean firstTime;
  private int position;

  /**
   * Constructs an XMLTreeNode. 
   * @param node an org.w3c.dom.Node  
   */
  public XMLTreeNode(Node node)
  {
    super(node);
    createTree();
  }

  /**
   * Returns true if this node is an XML leaf.
   */
  public boolean isLeaf()
  {
    Node node = (Node)getUserObject();
    short nodeType = node.getNodeType();

    switch(nodeType)
    {
      case Node.ATTRIBUTE_NODE:
      case Node.TEXT_NODE:
      case Node.CDATA_SECTION_NODE:
      case Node.PROCESSING_INSTRUCTION_NODE:
      case Node.COMMENT_NODE:
        return true;
    }

    NamedNodeMap namednodemap = node.getAttributes();
    if(namednodemap != null && namednodemap.getLength() > 0)
      return false;

    NodeList nodelist = node.getChildNodes();
    if (nodelist != null && nodelist.getLength() > 0)
      return false;

    return true;
  }


  /**
   * Returns the string representation of this node.
   */
  public String toString()
  {
    Node node = (Node)getUserObject();
    switch(node.getNodeType())
    {
      case Node.COMMENT_NODE:
        return "[Comment] --> " + stripLF(((Comment)node).getNodeValue());

      case Node.TEXT_NODE:
        return "\"" + stripLF(((Text)node).getNodeValue()) + "\"";

      case Node.ELEMENT_NODE:
        return ((Element)node).getTagName();

      case Node.DOCUMENT_NODE:
        return "[DOCUMENT]";

      case Node.PROCESSING_INSTRUCTION_NODE:
        return "[ProcessingInstruction] --> " + ((ProcessingInstruction)node).getData();

      case Node.ATTRIBUTE_NODE:
        return "[Attribute] --> " + ((Attr)node).getName()
          +"=\""+((Attr)node).getValue()+"\"";

    }
    return node.getNodeValue();
  }

  private void createTree()
  {
    Node node = (Node)getUserObject();

    NamedNodeMap namednodemap = node.getAttributes();
    if(namednodemap != null)
    {
      int i = namednodemap.getLength();
      for(int j = 0; j < i; j++)
        addXMLNode(namednodemap.item(j));
    }
    
    NodeList nodelist = node.getChildNodes();
    Object obj = null;
    int k = nodelist.getLength();
    if(nodelist != null)
    {
      for(int l = 0; l < k; l++)
      {
        Node node1 = nodelist.item(l);
        switch(node1.getNodeType())
        {
          case Node.COMMENT_NODE:
          case Node.PROCESSING_INSTRUCTION_NODE:
          case Node.ELEMENT_NODE:
          case Node.CDATA_SECTION_NODE:
          case Node.DOCUMENT_NODE:
            addXMLNode(node1);
            break;

          case Node.TEXT_NODE:
            addText(node1);
            break;
        }
      }
    }
  }

  private void addText(Node node)
  {
    if(((Text)node).getData().trim().length() != 0)
      addXMLNode(node);
  }

  private void addXMLNode(Node node)
  {
    insert(new XMLTreeNode(node), position++);
  }

  private String stripLF(String text)
  {
    int maxLength=1000;
    StringBuffer sb=new StringBuffer(text);
    int i;
    int len;
    len=sb.length();
    char [] textChars=new char[len];
    sb.getChars(0, len, textChars, 0);
    for (i=0; i < len; i++)
    {
      if (textChars[i] == '\n')
      {
        sb.setCharAt(i,' ');
      }
    }

    // Truncates long strings.
    if (sb.length()>maxLength) {
      sb.setLength(maxLength);
      sb.append("... (string truncated)");
    }
    return sb.toString();
  }
}
