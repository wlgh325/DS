import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.json.simple.parser.ParseException;

public class GUI extends JFrame{
	private String[] searchContents;
	JsonParser parser;
	
	public GUI() throws ParseException {
		super("Jeju Map route");	//â �̸� ����
		/*
		Container contentPane = this.getContentPane();	//container����
		JPanel pane = new JPanel();	//Panel ����
		JTextField searchtext = new JTextField(10);	//Textfield ����
		JLabel labelPeriod = new JLabel("Input destination: ");
		JButton searchButton = new JButton("Search");	//Buttuon ����
		JComboBox<String> searchInform = new JComboBox<String>();
		
		//enter key�� �˻�
		registerEnterKey(searchButton);
        
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchInform.removeAllItems();
				String str = searchtext.getText();
				searchAPI(str);
				
				for(int i=0; i<searchContents.length; i++)
					searchInform.addItem(searchContents[i]);
			}
		});
		contentPane.add(pane);
		pane.add(labelPeriod);
		pane.add(searchtext);
		pane.add(searchButton);
		pane.add(searchInform);
		
		setBounds(400 , 50 , 600 , 600);	//â ũ�� ����x,y,width,height
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//�ݱ� ��ư ������ ���
		setVisible(true);	//frame's visible property
		*/
	}
	
	
}
