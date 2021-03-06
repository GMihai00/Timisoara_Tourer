package com.tourer.gui;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.tourer.App;
import com.tourer.gui.map.Location;
import com.tourer.jdbc.*;

import javafx.application.Platform;

public class SearchField extends JComboBox<String>{
    
    Vector <String> latestSearches;
    static Vector <SearchField> allFields = new Vector <SearchField>();
    public boolean hide_flag = false;
    JTextField search;
    public SearchField(){
        this.setVisible(false);
        this.setEditable(true);
        this.setModel(new DefaultComboBoxModel<String>());
        latestSearches = new Vector<String>();
        this.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        search = (JTextField) super.getEditor().getEditorComponent();
        search.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e){
                    EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            String text = SearchField.this.search.getText();
                            if(text.length() == 0){
                                SearchField.super.hidePopup();
                                SearchField.this.setModel(new DefaultComboBoxModel<String>(SearchField.this.latestSearches), "");
                            }
                            else
                            {
                                if(SearchField.this instanceof UserSearchField){
                                    try {
                                        latestSearches = Connector.getUserList(text);
                                    } catch (SQLException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                                else
                                if(SearchField.this instanceof LocationSearchField){
                                    try {
                                        latestSearches = Connector.getLocationList(text);
                                    } catch (SQLException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                                DefaultComboBoxModel<String> m = SearchField.this.getSuggestedModel(SearchField.this.latestSearches, text);
                                if(m.getSize() == 0 || SearchField.this.hide_flag){
                                    SearchField.super.hidePopup();
                                    SearchField.this.hide_flag = false;
                                }
                                else
                                {
                                    SearchField.this.setModel(m, text);
                                    SearchField.this.showPopup();
                                }
                            }
                            
                        }
                        
                    });
            }
            public void keyPressed(KeyEvent e){
                String text = SearchField.this.search.getText();
                int code = e.getKeyCode();
                if(code == KeyEvent.VK_ENTER){
                    if(!SearchField.this.latestSearches.contains(text)){
                        SearchField.this.latestSearches.addElement(text);
                        Collections.sort(SearchField.this.latestSearches);
                        setModel(SearchField.this.getSuggestedModel(SearchField.this.latestSearches, text), text);
                    }
                    hide_flag = true;
                }
                else
                if(code==KeyEvent.VK_ESCAPE){
                    hide_flag = true;
                }
                else
                if(code==KeyEvent.VK_RIGHT){
                    for(int i = 0; i < SearchField.this.latestSearches.size(); i++){
                        String str = SearchField.this.latestSearches.elementAt(i);
                        if(str.startsWith(text)){
                            SearchField.super.setSelectedIndex(-1);
                            SearchField.this.search.setText(str);
                            return;
                        }
                    }
                }
            }
        });
        this.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    
                    if(SearchField.super.getSelectedIndex() != -1)
                        if(SearchField.this instanceof UserSearchField){
                            //System.out.println("selected");
                            try {
                                UsserButton.userSettingsMenu.showOtherUser((String) SearchField.super.getSelectedItem());
                                
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(UsserButton.userSettingsMenu, "Couldn't load user: " +  SearchField.super.getSelectedItem() , "ERROR", JOptionPane.ERROR_MESSAGE);
                                e.printStackTrace();
                            }
                        }
                        else
                        if(SearchField.this instanceof LocationSearchField){
                            //System.out.print("selected2");
                            String locationName = (String) SearchField.super.getSelectedItem();
                            try {
                                Location searchedLocation = Connector.getFirstLocationByName(locationName);
                                Platform.runLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        Double lat = searchedLocation.getLatitude();
                                        Double lng = searchedLocation.getLongitude();
                                        App.engine.executeScript("setTargetMarker(" + lat + ", " + lng + ");");
                                        App.mainFrame.requestFocus();
                                    }
                                });
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(UsserButton.userSettingsMenu, "Failed to mark location" , "ERROR", JOptionPane.ERROR_MESSAGE);
                                e.printStackTrace();
                            }
                           
                        }
                }
            }
        });
        this.setModel(new DefaultComboBoxModel<String>(latestSearches), "");
    }
    
    /** 
     * @param mdl
     * @param str
     */
    private void setModel(DefaultComboBoxModel<String> mdl, String str){
        SearchField.super.setModel(mdl);
        SearchField.super.setSelectedIndex(-1);
        SearchField.this.search.setText(str);
    }

    
    /** 
     * @param list
     * @param text
     * @return DefaultComboBoxModel<String>
     */
    private  DefaultComboBoxModel<String> getSuggestedModel(Vector <String> list, String text){
        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<String>();
        for(String s : list){
            if(s.toLowerCase().contains(text.toLowerCase()))
                m.addElement(s);
        }
        return m;
    }

    
    /** 
     * @return Dimension
     */
    @Override
    public Dimension getMaximumSize() {
        Dimension dim = super.getMaximumSize();
        dim.height = getPreferredSize().height;
        dim.width = getPreferredSize().width;
        return dim;
    }

    
    /** 
     * @param item
     */
    @Override
    public void addItem(String item){
        latestSearches.add(item);
        this.setModel(new DefaultComboBoxModel<String>(latestSearches), "");
    }
}
