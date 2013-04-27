

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import bean.Category;
import bean.Keyword;



@SuppressWarnings("serial")
public class IMTableModel extends AbstractTableModel{
	
	private int m_nColumnCount;

	private JPanelNewPicture m_jFrameOwner;

	private ArrayList<?> data;
	
	public static final String[] columnNames = 	 {
		IMMessage.getString("LOGINDIAG_DESCRIPTION"),
		IMMessage.getString("CHOSEN")
		};
	
	
	public IMTableModel() {
		super();

		this.m_nColumnCount = 0;
		this.m_jFrameOwner = null;
	}
	
	public IMTableModel(ArrayList<?> data, JPanelNewPicture frameOwner) {
		super();
		this.data = data;
		this.m_nColumnCount = 2;
		this.m_jFrameOwner = frameOwner;
	}

	@Override
	public int getColumnCount() {
		return m_nColumnCount;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		int i = 0;
		if (columnIndex==i++){
			if (data.get(rowIndex) instanceof Keyword)
				return ((Keyword)data.get(rowIndex)).getName();
			else if (data.get(rowIndex) instanceof Category)
				return ((Category)data.get(rowIndex)).getName();
		} else if (columnIndex==i++){
			if (data.get(rowIndex) instanceof Keyword)
				return ((Keyword)data.get(rowIndex)).isSelected();
			else if (data.get(rowIndex) instanceof Category)
				return ((Category)data.get(rowIndex)).isSelected();
		} 
	return null;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
		int i = 0;
		if (columnIndex==i++){
			
		} else if (columnIndex==i++){
			if (data.get(rowIndex) instanceof Keyword){
				 Keyword k = ((Keyword)data.get(rowIndex));
				 k.setSelected(!k.isSelected());
			}
			else if (data.get(rowIndex) instanceof Category)
			{
				 Category c = ((Category)data.get(rowIndex));
				 c.setSelected(!c.isSelected());
			}
		} else {
			
		}
		
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		int i = 0;
		if (columnIndex==i++){
			 return super.getColumnClass(columnIndex);
		} else if (columnIndex==i++){
			return Boolean.class;
		} else {
			 return super.getColumnClass(columnIndex);
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex){
		int i = 0;
		if (columnIndex==i++){
			return false;
		} else if (columnIndex==i++){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String getColumnName(int col) {
        return columnNames[col];
    }
	
}
