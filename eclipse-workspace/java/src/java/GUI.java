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
		super("Jeju Map route");	//창 이름 설정
		/*
		Container contentPane = this.getContentPane();	//container생성
		JPanel pane = new JPanel();	//Panel 생성
		JTextField searchtext = new JTextField(10);	//Textfield 생성
		JLabel labelPeriod = new JLabel("Input destination: ");
		JButton searchButton = new JButton("Search");	//Buttuon 생성
		JComboBox<String> searchInform = new JComboBox<String>();
		
		//enter key로 검색
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
		
		setBounds(400 , 50 , 600 , 600);	//창 크기 설정x,y,width,height
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//닫기 버튼 눌렀을 경우
		setVisible(true);	//frame's visible property
		*/
	}
	
	
}
