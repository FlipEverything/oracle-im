import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import bean.Album;
import bean.City;
import bean.Country;
import bean.Keyword;
import bean.Picture;
import bean.Region;
import bean.User;


@SuppressWarnings("serial")
public class JPanelNewPicture extends JPanelLogin {
	
	protected JLabel m_jLabelName = new JLabel();
	protected JTextField m_jNameField = new JTextField();
		
	protected JComboBox m_jComboCountry = new JComboBox();
	protected JComboBox m_jComboRegion = new JComboBox();
	protected JComboBox m_jComboCity = new JComboBox();
	
	protected JLabel m_jLabelCountry = new JLabel();
	protected JLabel m_jLabelRegion = new JLabel();
	protected JLabel m_jLabelCity = new JLabel();
	private User m_userDisplayed;
	
	private JLabel m_jLabelAlbum = new JLabel();
	private JComboBox m_jComboBoxAlbum = new JComboBox();
	
	private JLabel m_jLabelImage = new JLabel();
	private JButton m_jButtonImage = new JButton();
	
	private JLabel m_jLabelKeywords = new JLabel();
	private JButton m_jButtonKeywords = new JButton();
	
	private JLabel m_jLabelCategories = new JLabel();
	private JButton m_jButtonCategories = new JButton();
	
	private Picture m_pictureNew = new Picture();
	private IMLoadFile load = null;
	

	
	public JPanelNewPicture(IMFrame frame, User user) {
		super(frame);
		m_szTitle = IMMessage.getString("MAIN_MENU_UPLOAD");
		m_szButtonConfirmTitle = "UPLOADDIAG_SEND";
		m_nHeight = 460;
		m_userDisplayed = user;
	}
	
	
	@Override 
	protected void setupContentPane(){
		
		
		
		m_jLabelName = IMFrame.createJLabel(m_jLabelName, new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth , m_iFieldHeight), 
				"UPLOADDIAG_NAME", 'U', m_jLoginField);
		
		m_jLabelImage = IMFrame.createJLabel(m_jLabelImage, new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth , m_iFieldHeight), 
				"UPLOADDIAG_BROWSE", 'U', m_jButtonImage);
		
		m_jLabelAlbum = IMFrame.createJLabel(m_jLabelAlbum,  new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"NEWALBUMDIAG_NAME", 'O', m_jComboCity);
		
		m_jLabelCountry = IMFrame.createJLabel(m_jLabelCountry,  new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"COUNTRY", 'O', m_jComboCountry);
		
		m_jLabelRegion = IMFrame.createJLabel(m_jLabelRegion,  new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"REGION", 'O', m_jComboRegion);
		
		m_jLabelCity = IMFrame.createJLabel(m_jLabelCity,  new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"CITY", 'O', m_jComboCity);
		
		m_jLabelKeywords = IMFrame.createJLabel(m_jLabelKeywords,  new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"KEYWORDS", 'K', m_jComboCity);
		
		m_jLabelCategories = IMFrame.createJLabel(m_jLabelCategories,  new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"CATEGORIES", 'K', m_jComboCity);
		

		m_jNameField.setBounds(
		        new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth , m_iFieldHeight));
		m_jNameField.setToolTipText(IMMessage.getString("LOGINDIAG_USERNAME_FIELD_DESC"));


		m_jButtonImage = IMFrame.createButton(m_jButtonImage, "UPLOADDIAG_BROWSE", 'T', "upload", "UPLOADDIAG_BROWSE", 
				new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth , m_iFieldHeight), new Callable<Void>() {
					   public Void call() {
					        makeFileChooser();
							return null;
					   }
				});
		

		

		  IMQuery q = new IMQuery();
		  ArrayList<Album> array = q.getAlbumsByUser(m_userDisplayed);
		  m_jComboBoxAlbum = IMFrame.createComboBox(m_jComboBoxAlbum, true, 0, new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth, m_iFieldHeight), 
					array, new Callable<Void>() {
				   public Void call() {
				        
						return null;
				   }
			});
		  
		m_jComboCountry = IMFrame.createComboBox(m_jComboCountry, true, 0, new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth, m_iFieldHeight), 
				m_jFrameOwner.getcountryAll(), new Callable<Void>() {
			   public Void call() {
			        countryListener();
					return null;
			   }
		});
		
		m_jComboRegion = IMFrame.createComboBox(m_jComboRegion, false, 0, new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth, m_iFieldHeight), 
				m_jFrameOwner.getregionAll(), new Callable<Void>() {
			   public Void call() {
			        regionListener();
					return null;
			   }
		});
		
		m_jComboCity = IMFrame.createComboBox(m_jComboCity, false, 0, new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth, m_iFieldHeight), 
				m_jFrameOwner.getcityAll(), new Callable<Void>() {
			   public Void call() {
			        cityListener();
					return null;
			   }
		});
		
		
		m_jButtonKeywords = IMFrame.createButton(m_jButtonKeywords, "UPLOADDIAG_CHOOSE_KEYWORDS", 'T', null, "UPLOADDIAG_BROWSE", 
				new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth , m_iFieldHeight), new Callable<Void>() {
					   public Void call() {
					        showKeywordDialog();
							return null;
					   }
				});
		
		m_jButtonCategories = IMFrame.createButton(m_jButtonCategories, "UPLOADDIAG_CHOOSE_CATEGORIES", 'T', null, "UPLOADDIAG_BROWSE", 
				new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth , m_iFieldHeight), new Callable<Void>() {
					   public Void call() {
					        showCategoryDialog();
							return null;
					   }
				});
		
		contentPanel = new JPanel();
	    contentPanel.setLayout(null);
	    
	    contentPanel.add(m_jNameField, null);
		contentPanel.add(m_jComboCountry, null);
		contentPanel.add(m_jComboRegion, null);
		contentPanel.add(m_jComboCity, null);
		contentPanel.add(m_jComboBoxAlbum, null);
		contentPanel.add(m_jButtonImage, null);
		contentPanel.add(m_jButtonCategories,null);
		contentPanel.add(m_jButtonKeywords,null);
		
	    contentPanel.add(m_jLabelAlbum, null);
	    contentPanel.add(m_jLabelName, null);
	    contentPanel.add(m_jLabelCountry,null);
		contentPanel.add(m_jLabelCity,null);
		contentPanel.add(m_jLabelRegion,null);
		contentPanel.add(m_jLabelImage,null);
		contentPanel.add(m_jLabelCategories,null);
		contentPanel.add(m_jLabelKeywords,null);
		
		contentPanel.add(m_jButtonCancel, null);
		contentPanel.add(m_jButtonLogin, null);

	} 
	
	protected void showCategoryDialog() {
		// TODO Auto-generated method stub
		
	}


	protected void showKeywordDialog() {
		IMQuery q = new IMQuery();
		ArrayList<Keyword> keywords = q.getKeywords();
		JDialogTable dialog = new JDialogTable(m_jFrameOwner, new IMKeywordTableModel(keywords, this));
		
	}


	protected void makeFileChooser() {
		load = new IMLoadFile(m_jFrameOwner, IMMessage.getString("MAIN_MENU_UPLOAD"), m_pictureNew.getPicture(), m_pictureNew.getPictureThumbnail(), m_pictureNew);
		if (load.getReturnVal() == JFileChooser.APPROVE_OPTION){
			m_jButtonImage.setText(IMMessage.getString("UPLOADDIAG_SELECTED"));
		}
	}


	@Override
	protected void setupButton()
	{
	    super.setupButton();
	}
	
	protected void cityListener() {
		if ((m_jComboCity.getSelectedIndex()!=0) && (m_jComboCity.isEnabled()==true)){
			m_jComboRegion.setEnabled(false);
		}
		if ((m_jComboCity.getSelectedIndex()==0) && (m_jComboCity.isEnabled()==true)){
			m_jComboRegion.setEnabled(true);
		}
	}


	protected void regionListener() {
		checkCombo(m_jComboRegion, m_jComboCity, m_jFrameOwner.getcityAll());
		
		if (m_jComboRegion.getSelectedIndex()!=0){
			m_jComboCountry.setEnabled(false);
			m_jComboCity.setEnabled(true);
		} else {
			m_jComboCountry.setEnabled(true);
			m_jComboCity.setEnabled(false);
		}
	}


	protected void countryListener() {
		checkCombo(m_jComboCountry, m_jComboRegion, m_jFrameOwner.getregionAll());
		
		if (m_jComboCountry.getSelectedIndex()!=0){
			m_jComboRegion.setEnabled(true);
		} else {
			m_jComboRegion.setEnabled(false);
		}
	}
	
	private void checkCombo(JComboBox active, JComboBox children, ArrayList<?> all){
		int selected = active.getSelectedIndex();
		
		
		if ((selected!=0) && (active.isEnabled())){
			children.removeAllItems();
			Object o = active.getItemAt(selected);
			
			children.addItem(IMMessage.getString("SELECT"));
				
				if (o instanceof Country ){
					Iterator it = all.iterator();
					while (it.hasNext()){
						Region r = (Region) it.next();
						if (((Country) o).getCountryId()==r.getCountryId()){
							children.addItem(r);
						}	
					}
				} else if (o instanceof Region){
					Iterator it = all.iterator();
					while (it.hasNext()){
						City c = (City) it.next();
						if (((Region) o).getRegionId()==c.getRegionId()){
							children.addItem(c);
						}	
					}
				}	
				
			
			children.validate();
			SwingUtilities.updateComponentTreeUI(this);
		}
	}


	@Override
	public void confirm(){
		if (
				m_jNameField.getText().equals("") ||
				load != null && load.getReturnVal() != JFileChooser.APPROVE_OPTION ||
				m_jComboBoxAlbum.getSelectedIndex() == 0 ||
				m_jComboCity.getSelectedIndex()==0
				 ){
				  new IMMessage(IMConstants.ERROR, "EMPTY_FIELD", new Exception());
			  } else {
				  m_pictureNew.setPictureName(m_jNameField.getText());
				  m_pictureNew.setAlbumId(((Album)m_jComboBoxAlbum.getSelectedItem()).getAlbumId());
				  m_pictureNew.setCityId(((City)m_jComboCity.getSelectedItem()).getCityId());
				  
				  IMQuery q = new IMQuery();
				  m_pictureNew = q.insertPicture(m_pictureNew);
				  
				  
				  if ((m_pictureNew!=null) && (m_pictureNew.getPictureId()!=0)){
					  load.startUpload();
					  m_jFrameOwner.showProfilePanel(m_userDisplayed);
				  } else {
					  new IMMessage(IMConstants.ERROR, "UPLOAD_ERROR", new Exception());
				  }

		      }
	}
	
	@Override
	public void cancel(){
		m_jFrameOwner.showProfilePanel(m_userDisplayed);
	}

}
