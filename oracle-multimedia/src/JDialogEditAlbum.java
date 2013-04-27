import bean.Album;


@SuppressWarnings("serial")
public class JDialogEditAlbum extends JDialogNewAlbum {

	Album m_albumEdited;
	
	public JDialogEditAlbum(IMFrame jFrameOwner, Album a) {
		super(jFrameOwner);
		m_albumEdited = a;
		setFields();
	}
	
	public void setFields(){
		m_jAlbumNameField.setText(m_albumEdited.getName());
		m_jCheckBoxPublic.setSelected(m_albumEdited.isPublic());
	}
	
	
	@Override
	void confirm()
	  {
		  if (
			m_jAlbumNameField.getText().equals("") 
			 ){
			  new IMMessage(IMConstants.ERROR, "EMPTY_FIELD", new Exception());
		  } else {
			  m_albumEdited.setName(m_jAlbumNameField.getText());
			  m_albumEdited.setPublic(m_jCheckBoxPublic.isSelected());
			  
			  IMQuery q = new IMQuery();
			  m_albumEdited = q.editAlbum(m_albumEdited);
			  
			  if (m_albumEdited!=null){
				  new IMMessage(IMConstants.WARNING, "MODIFY_SUCCESS");
				  m_jFrameOwner.refresh();
				  this.setVisible(false);
				  this.dispose();
			  }
	      }
	  }

}
