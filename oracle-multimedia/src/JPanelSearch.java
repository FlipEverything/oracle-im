import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTextField;

import bean.Category;
import bean.City;
import bean.Country;
import bean.Keyword;
import bean.Picture;
import bean.Region;
import bean.User;


@SuppressWarnings("serial")
public class JPanelSearch extends JPanelNewPicture {

	protected int m_nResultPanelHeight;
	protected int m_nOffset = 30;
	private int m_nLineHeight = 30;
	private int m_nX;
	private int m_nY;
	private int m_nTableOffset;
	private IMTable m_jTableResult;
	
	private JTextField m_jMinWidthField;
	private JTextField m_jMaxWidthField;
	private JTextField m_jMinHeightField;
	private JTextField m_jMaxHeightField;
	
	public JPanelSearch(IMFrame frame, User user) {
		super(frame, user);
		m_szTitle = IMMessage.getString("MAIN_MENU_SEARCH");
		m_szButtonConfirmTitle = "MAIN_MENU_SEARCH";
	}
	
	
	@Override 
	protected void setupContentPane(){
		super.setupContentPane();
		m_nResultPanelHeight = 400;
		
		this.setPreferredSize(new Dimension((int) this.getPreferredSize().getWidth(), m_nWidth+m_nResultPanelHeight));
		
		ArrayList<Picture> array = new ArrayList<Picture>();
		
		m_nX = 0;
		m_nY = m_nHeight + 15;
		m_nTableOffset = 100;
		  
		m_jTableResult = m_jFrameOwner.createPictureResultTable(array, "SEARCH_RESULTS", m_jFrameOwner.getWidth()-m_nTableOffset, m_nResultPanelHeight, m_nX, m_nY, contentPanel);
		m_nY+=IMQuery.TOP*m_nLineHeight;
		
		m_jLabelImage.setText(IMMessage.getString("WIDTH"));
		m_jLabelAlbum.setText(IMMessage.getString("HEIGHT"));
		
		contentPanel.remove(m_jButtonImage);
		contentPanel.remove(m_jComboBoxAlbum);
		
		m_jMinHeightField = new JTextField();
		m_jMaxHeightField = new JTextField();
		m_jMinWidthField = new JTextField();
		m_jMaxWidthField = new JTextField();
		
		m_jMinHeightField.setText("0");
		m_jMaxHeightField.setText("9999999");
		m_jMinWidthField.setText("0");
		m_jMaxWidthField.setText("9999999");
		
		Rectangle r = m_jButtonImage.getBounds();
		r.width = m_iFieldWidth/2;
		m_jMinWidthField.setBounds(r);
		r.x+=m_iFieldWidth/2;
		m_jMaxWidthField.setBounds(r);
		
		Rectangle r1 = m_jComboBoxAlbum.getBounds();
		r1.width = m_iFieldWidth/2;
		m_jMinHeightField.setBounds(r1);
		r1.x+=m_iFieldWidth/2;
		m_jMaxHeightField.setBounds(r1);
		
		contentPanel.add(m_jMinWidthField, null);
		contentPanel.add(m_jMaxWidthField, null);
		contentPanel.add(m_jMinHeightField, null);
		contentPanel.add(m_jMaxHeightField, null);
		
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
		

						  
		IMQuery q = new IMQuery();
		int countryId = 0;
		int regionId = 0;
		int cityId = 0;
		if (m_jComboCountry.getSelectedIndex()!=0){
			countryId = ((Country)m_jComboCountry.getSelectedItem()).getCountryId();
		}
		if (m_jComboRegion.getSelectedIndex()!=0){
			regionId = ((Region)m_jComboRegion.getSelectedItem()).getRegionId();
		}
		if (m_jComboCity.getSelectedIndex()!=0){
			cityId = ((City)m_jComboCity.getSelectedItem()).getCityId();
		}
		ArrayList<Picture> data = q.search(
				m_jNameField.getText(), 
				Integer.parseInt(m_jMinHeightField.getText()), 
				Integer.parseInt(m_jMaxHeightField.getText()),
				Integer.parseInt(m_jMinWidthField.getText()), 
				Integer.parseInt(m_jMaxWidthField.getText()),
				countryId,
				regionId,
				cityId,
				selectedCategories,
				selectedKeywords
		);
				  
		((IMTableModelPicture)m_jTableResult.getModel()).setData(data);
		new IMMessage(IMMessage.WARNING, "SEARCH_RESULT", data.size()+"", "FOUND");
		m_jFrameOwner.scrollTo(0,600);
		m_jFrameOwner.refresh();

	
	}
	
	@Override
	public void cancel(){
		m_jFrameOwner.showGalleryPanel();
	}

}
