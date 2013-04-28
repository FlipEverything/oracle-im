import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import bean.Picture;


@SuppressWarnings("serial")
public class IMTableModelPicture extends AbstractTableModel{
	
	private int m_nColumnCount;


	private ArrayList<Picture> data;
	private IMFrame m_jframeOwner;
	
	public static final String[] columnNames = 	 {
		IMMessage.getString("ID"),
		IMMessage.getString("UPLOADDIAG_NAME"),
		IMMessage.getString("RATING"),
		IMMessage.getString("MAIN_MENU_PROFILE")
		};
	
	
	public IMTableModelPicture(ArrayList<Picture> data, IMFrame m_jframeOwner) {
		super();
		this.m_nColumnCount = 4;
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
			return "#"+data.get(rowIndex).getPictureId();
		} else if (columnIndex==i++){
			return data.get(rowIndex).getPictureName();
		} else if (columnIndex==i++){
			return data.get(rowIndex).getRatingValue();
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
			IMImage d = new IMImage(m_jframeOwner, data.get(rowIndex));
			d.setVisible(true);
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
		} else {
			return false;
		}
	}
	
	@Override
	public String getColumnName(int col) {
        return columnNames[col];
    }

	public ArrayList<Picture> getData() {
		return data;
	}

	public void setData(ArrayList<Picture> data) {
		this.data = data;
	}
	
	
	
}
