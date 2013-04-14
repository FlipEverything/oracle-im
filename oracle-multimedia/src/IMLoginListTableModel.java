

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;



@SuppressWarnings("serial")
public class IMLoginListTableModel extends AbstractTableModel{
	
	private int m_nColumnCount;
	private ArrayList<DatabaseCreditental> m_aCreditentals;
	private IMLoginListDialog m_jFrameOwner;
	
	public static final String[] columnNames = 	 {
		IMMessage.getString("LOGINDIAG_DESCRIPTION"),
		IMMessage.getString("LOGINDIAG_HOSTNAME"),
		IMMessage.getString("LOGINDIAG_PORT"),
		IMMessage.getString("LOGINDIAG_SID"),
		IMMessage.getString("LOGINDIAG_USERNAME"),
		IMMessage.getString("LOGINDIAG_PASSWD")};
	
	
	public IMLoginListTableModel() {
		super();
		this.m_aCreditentals = null;
		this.m_nColumnCount = 0;
		this.m_jFrameOwner = null;
	}
	
	public IMLoginListTableModel(ArrayList<DatabaseCreditental> dbData, IMLoginListDialog frameOwner) {
		super();
		this.m_aCreditentals = dbData;
		this.m_nColumnCount = 5;
		this.m_jFrameOwner = frameOwner;
	}

	@Override
	public int getColumnCount() {
		return m_nColumnCount;
	}

	@Override
	public int getRowCount() {
		return m_aCreditentals.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		int i = 0;
		if (columnIndex==i++){
			return m_aCreditentals.get(rowIndex).getDescription();
		} else if (columnIndex==i++){
			return m_aCreditentals.get(rowIndex).getHostname();
		} else if (columnIndex==i++){
			return m_aCreditentals.get(rowIndex).getPort();
		} else if (columnIndex==i++){
			return m_aCreditentals.get(rowIndex).getSid();
		} else if (columnIndex==i++){
			return m_aCreditentals.get(rowIndex).getUsername();
		} else {
			return null;
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
		int i = 0;
		if (columnIndex==i++){
			m_aCreditentals.get(rowIndex).setDescription(new String(aValue.toString()));
		} else if (columnIndex==i++){
			m_aCreditentals.get(rowIndex).setHostname(new String(aValue.toString()));
		} else if (columnIndex==i++){
			m_aCreditentals.get(rowIndex).setPort(new Integer(Integer.parseInt(aValue.toString())));
		} else if (columnIndex==i++){
			m_aCreditentals.get(rowIndex).setSid(new String(aValue.toString()));
		} else if (columnIndex==i++){
			m_aCreditentals.get(rowIndex).setUsername(new String(aValue.toString()));
		} else {
			
		}
		m_jFrameOwner.update(m_aCreditentals.get(rowIndex));
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
