


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultRowSorter;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import bean.User;



@SuppressWarnings("serial")
public class JPanelUsers extends JPanel implements IMConstants{
	  
	protected IMFrame m_jFrameOwner = null;
	protected JPanel contentPanel;
	
	protected String m_szTitle;	
	protected int m_nWidth;
	protected int m_nHeight = 450;
	protected int m_nOffset = 30;
	private int m_nFieldHeight = 30;
	

	JTable m_jTableData = new JTable();
	private DefaultRowSorter<TableModel, Integer> m_rowSorterForTableData;
	  
	JTextField m_jFieldSearch = new JTextField(); 
	IMUserTableModel m_model;
	  

	public JPanelUsers(IMFrame frame){
		this.m_jFrameOwner = frame;
		this.m_szTitle = IMMessage.getString("MAIN_MENU_USERS");
		this.m_nWidth = frame.getWidth() - 70;

	}
	
	public void init(){
		try
	    {
	      setupDisplay();
	      setupButton();
	      setupContentPane();
	      add(m_jFieldSearch, BorderLayout.NORTH);
	      add(contentPanel, BorderLayout.CENTER);

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
	  protected void setupButton()
	  {

	  }

	  /**
	   * Draws the content pane.
	   */
	  private void setupContentPane() 
	  {
		  
		  IMQuery q = new IMQuery();
		  ArrayList<User> array = q.selectAllUsers();
		  
		  m_model = new IMUserTableModel(array, m_jFrameOwner); 
		  m_rowSorterForTableData = new TableRowSorter<TableModel>(m_model);
		  
		  m_jTableData = new JTable(m_model);
		  m_jTableData.setRowSorter(m_rowSorterForTableData);
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
		  m_jFieldSearch.setText(IMMessage.getString("MAIN_MENU_SEARCH"));
		  
		  m_jFieldSearch.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				if (m_jFieldSearch.getText().equals("")){
					m_jFieldSearch.setText(IMMessage.getString("MAIN_MENU_SEARCH"));
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				m_jFieldSearch.setText("");
				
			}
		});
		  
		  m_jFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				filter();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				filter();			
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				filter();	
			}
		});
		  
		 

	  }
	  
	  
	  public void filter(){
			String k = m_jFieldSearch.getText();
			if (k.equals(null) || k.equals("") || k.equals(IMMessage.getString("MAIN_MENU_SEARCH"))){
				m_rowSorterForTableData.setRowFilter(null);
			} else {
				List<RowFilter<Object,Object>> rfs = new ArrayList<RowFilter<Object,Object>>(0);
				rfs.add(RowFilter.regexFilter("(?i)"+m_jFieldSearch.getText(), 0));
				rfs.add(RowFilter.regexFilter("(?i)"+m_jFieldSearch.getText(), 1));
			
				RowFilter<TableModel, Object> rf = RowFilter.orFilter(rfs);
				m_rowSorterForTableData.setRowFilter(rf);
			}
			m_jTableData.revalidate();
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
		m_jFrameOwner.setLabel(m_szTitle);
	    this.setSize(new Dimension(m_nWidth, m_nHeight));
	    this.getAccessibleContext().setAccessibleDescription(
	        IMMessage.getString("LOGINDIAG_DESC"));
	    this.setLayout(new BorderLayout());
	  }
	  
	  
	
	
	
}
