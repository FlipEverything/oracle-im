

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import bean.Album;
import bean.Category;
import bean.Keyword;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Displays the login dialog and creates the connection
 * to the database.
 */
@SuppressWarnings("serial")
public class JDialogTable extends JDialog implements IMConstants
{
  
  IMFrame m_jFrameOwner = null;
  JTable m_jTableData = new JTable();
  
  JTextField m_jFieldSearch = new JTextField(); 
  
  private String m_szTitle;
  private int m_nWidth = 400;
  private int m_nHeight = 400;
  private TableModel m_model;
  private JPanel contentPanel;
  private int m_nFieldHeight = 30;
  private Object m_object;

  public JDialogTable(IMFrame jFrameOwner, TableModel model, Object object)
  {
    super(jFrameOwner, true);
    m_jFrameOwner = jFrameOwner;
    m_model = model;
    m_object = object;
    
    if (m_object instanceof Category){
    	m_szTitle = "SELECT_AND_INSERT_CATEGORY";	
    } else if (m_object instanceof Keyword){
    	m_szTitle = "SELECT_AND_INSERT_KEYWORD";
    }
    

    try
    {
      setupDisplay();
      setupButton();
     
      setupContentPane();
      addListener();
      
      add(contentPanel, BorderLayout.CENTER);
      add(m_jFieldSearch, BorderLayout.NORTH);
      
      setVisible(false);
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
	  
	  m_jTableData = new JTable(m_model);
	  //m_jTableData.setFocusable(false);
	  
	  m_jTableData.setRowSelectionAllowed(false);
	  
	  JScrollPane scroll = new JScrollPane(m_jTableData);
	  scroll.setSize(new Dimension(m_nWidth, m_nHeight-m_nFieldHeight));
	  scroll.setPreferredSize(new Dimension(m_nWidth, m_nHeight-m_nFieldHeight));
	  
	  contentPanel = new JPanel();
	  contentPanel.setLayout(null);

	  contentPanel.add(scroll, null);
	  
	  m_jFieldSearch.setSize(new Dimension(m_nWidth, m_nFieldHeight));
	  m_jFieldSearch.setPreferredSize(new Dimension(m_nWidth, m_nFieldHeight));
	  m_jFieldSearch.setText(IMMessage.getString("SEARCH_AND_INSERT"));
	  
	  m_jFieldSearch.addFocusListener(new FocusListener() {
		
		@Override
		public void focusLost(FocusEvent arg0) {
			if (m_jFieldSearch.getText().equals("")){
				m_jFieldSearch.setText(IMMessage.getString("SEARCH_AND_INSERT"));
			}
		}
		
		@Override
		public void focusGained(FocusEvent arg0) {
			m_jFieldSearch.setText(null);
			
		}
	});
	  
	  m_jFieldSearch.addKeyListener(new KeyListener() {
		
		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode()==KeyEvent.VK_ENTER){
				IMQuery q = new IMQuery();
				
				if (m_object instanceof Keyword)
				{
					Keyword k = new Keyword(m_jFieldSearch.getText());
					k = q.insertKeyword(k);
					if ((k!=null) && (k.getKeywordId())!=0){
						new IMMessage(IMMessage.WARNING, "INSERT_SUCCESS");
						m_jFieldSearch.setText(null);
						ArrayList<Keyword> array = m_jFrameOwner.getkeywordAll();
						array.add(k);

					}
				} 
				else if (m_object instanceof Category)
				{
					Category c = new Category(m_jFieldSearch.getText());
					c = q.insertCategory(c);
					if ((c!=null) && (c.getCategoryId()!=0)){
						new IMMessage(IMMessage.WARNING, "INSERT_SUCCESS");
						m_jFieldSearch.setText(null);
						ArrayList<Category> array = m_jFrameOwner.getcategoryAll();
						array.add(c);
					}
				}
					
				refresh();
				
			}
			
		}
	});

  }

  protected void refresh() {
	  m_jTableData.revalidate();
	  SwingUtilities.updateComponentTreeUI(this);
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

    IMUIUtil.initJDialogHelper(this);
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
              
              gotFocus =true;
            }
          }
        });
  }
}
