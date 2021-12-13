package com.tourer.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class CardDataDialog extends JDialog{
 
    public static String[] cardTypes= new String[]{"Visa", "MasterCard"};
    public static String[] mounth = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    public static String[] year = new String[]{"2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032"};
    public CardDataDialog(Frame frame){
        super(frame, "Add card data", ModalityType.APPLICATION_MODAL);
        this.setVisible(false);
        this.setSize(new Dimension((MainFrame.screenSize.width * 3) / 4 , (MainFrame.screenSize.height * 3) / 4));

        GridPanel contentPane = new GridPanel();
        JLabel label = new JLabel("Credit card");
        label.setFont(new Font(AppSettingsMenu.fontStyle, AppSettingsMenu.fontType, AppSettingsMenu.textSize));
        contentPane.addLeft(label);
        SearchField cardPick = new SearchField();
        cardPick.setVisible(true);
        for(String it : cardTypes){
            cardPick.addItem(it);
        }
        contentPane.addRight(cardPick);
        contentPane.addSpacer(Box.createVerticalStrut(20));
        JLabel label2 = new JLabel("Card number");
        label2.setFont(new Font(AppSettingsMenu.fontStyle, AppSettingsMenu.fontType, AppSettingsMenu.textSize));
        contentPane.addLeft(label2);
        contentPane.addRight(new JTextField());
        contentPane.addSpacer(Box.createVerticalStrut(20));
        JLabel label3 = new JLabel("Expiration date");
        label3.setFont(new Font(AppSettingsMenu.fontStyle, AppSettingsMenu.fontType, AppSettingsMenu.textSize));
        contentPane.addLeft(label3);
        SearchField datePick = new SearchField();
        datePick.setVisible(true);
        for(String it : mounth){
            for(String it2 : year){
                datePick.addItem(it + "/" + it2);
            }
        }
        contentPane.addRight(datePick);
        contentPane.addSpacer(Box.createVerticalStrut(20));
        JLabel label4 = new JLabel("Security Code");
        label4.setFont(new Font(AppSettingsMenu.fontStyle, AppSettingsMenu.fontType, AppSettingsMenu.textSize));
        contentPane.addLeft(label4);
        contentPane.addRight(new JTextField());
        contentPane.addSpacer(Box.createVerticalStrut(20));
        this.setContentPane(contentPane);
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
}