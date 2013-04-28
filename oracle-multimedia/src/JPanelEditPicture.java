import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JLabel;

import bean.Album;
import bean.Category;
import bean.City;
import bean.Keyword;
import bean.Picture;
import bean.User;


@SuppressWarnings("serial")
public class JPanelEditPicture extends JPanelNewPicture {

	Picture m_picture;
	
	public JPanelEditPicture(IMFrame frame, User m_userDisplayed, Picture p) {
		super(frame, m_userDisplayed);
		// TODO Auto-generated constructor stub
		m_szTitle = IMMessage.getString("EDIT_PICTURE");
		m_szButtonConfirmTitle = "EDIT";
		m_picture = p;
	}
	
	@Override 
	protected void setupContentPane(){
		super.setupContentPane();
		
		
		JLabel imageLabel = new JLabel(IMMessage.getString("CANNOT_CHOOSE"));
		imageLabel.setBounds(m_jButtonImage.getBounds());
		
		m_jNameField.setText(m_picture.getPictureName());
		
		
		m_jComboBoxAlbum.removeAllItems();
		
		IMQuery q = new IMQuery();
		ArrayList<Album> array = q.getAlbumsByUser(m_userDisplayed);
		Iterator<Album> italbum = array.iterator();
		while (italbum.hasNext()){
			Album a = italbum.next();
			m_jComboBoxAlbum.addItem(a);
			if (a.getAlbumId()==m_picture.getAlbumId()){
				m_jComboBoxAlbum.setSelectedItem(a);
			}
		}
		
		Iterator<Keyword> itk = m_keywords.iterator();
		while (itk.hasNext()){
			Keyword iteratorKeyword = itk.next();
			Iterator<Keyword> itp = m_picture.getKeywords().iterator();
			iteratorKeyword.setSelected(false);
			while (itp.hasNext()){
				Keyword pictureKeyword = itp.next();
				if (pictureKeyword.getKeywordId()==iteratorKeyword.getKeywordId()){
					iteratorKeyword.setSelected(true);
				}
			}
		}
		
		Iterator<Category> itc = m_categories.iterator();
		while (itc.hasNext()){
			Category iteratorCategory = itc.next();
			Iterator<Category> itpc = m_picture.getCategories().iterator();
			iteratorCategory.setSelected(false);
			while (itpc.hasNext()){
				Category pictureCategory = itpc.next();
				if (pictureCategory.getCategoryId()==iteratorCategory.getCategoryId()){
					iteratorCategory.setSelected(true);
				}
			}
		}
		
		m_jFrameOwner.setPlaceCombos(m_jComboCountry, m_jComboRegion, m_jComboCity, m_picture);
		
		m_jComboCountry.setEnabled(false);
		m_jComboRegion.setEnabled(false);
		m_jComboCity.setEnabled(true);
		
		
		contentPanel.remove(m_jButtonImage);
		contentPanel.add(imageLabel, null);
	}
	
	
	@Override
	public void confirm(){
		ArrayList<Keyword> selectedKeywords = new ArrayList<Keyword>();
		Iterator<Keyword> iteratorK = m_jFrameOwner.getkeywordAll().iterator();
		while (iteratorK.hasNext()){
			Keyword k = iteratorK.next();
			if (k.isSelected()){
				selectedKeywords.add(k);
			}
		}
		
		ArrayList<Category> selectedCategories = new ArrayList<Category>();
		Iterator<Category> iteratorC = m_jFrameOwner.getcategoryAll().iterator();
		while (iteratorC.hasNext()){
			Category c = iteratorC.next();
			if (c.isSelected()){
				selectedCategories.add(c);
			}
		}
	
		if (
				m_jNameField.getText().equals("") ||
				m_jComboCity.getSelectedIndex()==0  ||
				selectedCategories.size()==0 ||
				selectedKeywords.size()==0
				 ){
				  new IMMessage(IMConstants.ERROR, "EMPTY_FIELD");
			  } else {
				  m_picture.setPictureName(m_jNameField.getText());
				  m_picture.setAlbumId(((Album)m_jComboBoxAlbum.getSelectedItem()).getAlbumId());
				  m_picture.setCityId(((City)m_jComboCity.getSelectedItem()).getCityId());
				  
				  IMQuery q = new IMQuery();
				  m_picture = q.updatePicture(m_picture);
				  
				  
				  if ((m_picture!=null) && (m_picture.getPictureId()!=0)){
					  q.removeAllCategoryToPicture(m_picture);
					  q.removeAllKeywordToPicture(m_picture);
					  
					  q.insertCategoryToPicture(m_picture, selectedCategories);
					  q.insertKeywordToPicture(m_picture, selectedKeywords);
					  
					  new IMMessage(IMConstants.WARNING, "MODIFY_SUCCESS");
					  m_jFrameOwner.showAlbumPanel(m_userDisplayed);
				  } else {
					  new IMMessage(IMConstants.ERROR, "UPLOAD_ERROR");
				  }

		      }
	}
	
	@Override
	public void cancel(){
		m_jFrameOwner.showAlbumPanel(m_userDisplayed);
	}

}
