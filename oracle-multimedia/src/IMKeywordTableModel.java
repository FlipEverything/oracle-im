

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import bean.Keyword;



@SuppressWarnings("serial")
public class IMKeywordTableModel extends AbstractTableModel{
	
	private int m_nColumnCount;

	private JPanelNewPicture m_jFrameOwner;

	private ArrayList<Keyword> keywords;
	
	public static final String[] columnNames = 	 {
		IMMessage.getString("LOGINDIAG_DESCRIPTION"),
		IMMessage.getString("LOGINDIAG_HOSTNAME")
		};
	
	
	public IMKeywordTableModel() {
		super();

		this.m_nColumnCount = 0;
		this.m_jFrameOwner = null;
	}
	
	public IMKeywordTableModel(ArrayList<Keyword> keywords, JPanelNewPicture frameOwner) {
		super();
		this.keywords = keywords;
		this.m_nColumnCount = 2;
		this.m_jFrameOwner = frameOwner;
	}

	@Override
	public int getColumnCount() {
		return m_nColumnCount;
	}

	@Override
	public int getRowCount() {
		return keywords.size()+1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		int i = 0;
		if (columnIndex==i++){
			return keywords.get(rowIndex).getName();
		} else if (columnIndex==i++){
			return false;
		} else {
			return null;
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
		int i = 0;
		if (columnIndex==i++){
			
		} else if (columnIndex==i++){
			
		} else {
			
		}
		
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex){
		return true;
	}
	
	@Override
	public String getColumnName(int col) {
        return columnNames[col];
    }
	
}
