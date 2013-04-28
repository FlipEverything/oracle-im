

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import bean.User;



@SuppressWarnings("serial")
public class IMTableModelUser extends AbstractTableModel{
	
	private int m_nColumnCount;


	private ArrayList<User> data;
	private IMFrame m_jframeOwner;
	
	public static final String[] columnNames = 	 {
		IMMessage.getString("ID"),
		IMMessage.getString("MAIN_MENU_USER"),
		IMMessage.getString("LOGINDIAG_USERNAME"),
		IMMessage.getString("MAIN_MENU_ALBUMS"),
		IMMessage.getString("MAIN_MENU_PROFILE")
		};
	
	
	public IMTableModelUser(ArrayList<User> data, IMFrame m_jframeOwner) {
		super();
		this.m_nColumnCount = 5;
		this.data = data;
		this.m_jframeOwner = m_jframeOwner;
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
			return "#"+data.get(rowIndex).getUserId();
		} else if (columnIndex==i++){
			return data.get(rowIndex);
		} else if (columnIndex==i++){
			return data.get(rowIndex).getUsername();
		} else if (columnIndex==i++){
			return false;
		} else if (columnIndex==i++){
			return false;
		}
	return null;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
		int i = 0;
		if (columnIndex==i++){
			 //noop
		} else if (columnIndex==i++){
			//noop
		} else if (columnIndex==i++){
			//noop
		} else if (columnIndex==i++){
			m_jframeOwner.showAlbumPanel(data.get(rowIndex));
		} else if (columnIndex==i++){
			m_jframeOwner.showProfilePanel(data.get(rowIndex));
		} else {
			//noop
		}
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		int i = 0;
		if (columnIndex==i++){
			 return super.getColumnClass(columnIndex);
		} else if (columnIndex==i++){
			return super.getColumnClass(columnIndex);
		} else if (columnIndex==i++){
			return super.getColumnClass(columnIndex);
		} else if (columnIndex==i++){
			return Boolean.class;
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
			return false;
		} else if (columnIndex==i++){
			return false;
		} else if (columnIndex==i++){
			return true;
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
