import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
public class VisualAid {
	FileInputStream level;
	FileInputStream output;
	private int levelCounter = 0;
	private ArrayList<String> labels = new ArrayList<String>();
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JLabel matched = new JLabel("NOT MATCHED");
	JLabel diffchar = new JLabel("Amount of different characters: ");
	JLabel currentLevel = new JLabel("Current Level: " + (levelCounter + 1));
	JLabel lastLevel = new JLabel();
	//Sorry about the ugly try-catches. I had no choice
	 public VisualAid() {
		 gatherLevels();
		 lastLevel.setText("Last Level: " + labels.size());
		 try {
			level= new FileInputStream("C:\\Users\\bunu\\Desktop\\levels\\"+labels.get(levelCounter));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		try {
			output = new FileInputStream("C:\\Users\\bunu\\Desktop\\output.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 currentLevel = new JLabel("Current Level: "+ (levelCounter+1));
		 int diff = 0;
		 try{
			 diff = difference(level,output);
		 } catch(IOException e){}
		 diffchar = new JLabel("Amount of different character: " + diff);
		 panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		 panel.add(matched);
	     panel.add(diffchar);
	     panel.add(currentLevel);
	     panel.add(lastLevel);
	     frame.add(panel);
	     frame.pack();
	     frame.setVisible(true);
	 }
	 private int difference(FileInputStream file1, FileInputStream file2) throws IOException{
		 FileInputStream smaller = file1;
		 FileInputStream bigger = file2;
		 int counter = 0;
		 if(file1.getChannel().size()>file2.getChannel().size()){
			 smaller = file2;
			 bigger = file1;
		 }
		
		 int c;
		 int d;
		 while ((c = smaller.read()) != -1 && (d=bigger.read()) != -1) {
             if(c != d){
			 	counter++;
             }
         }
		 counter+=bigger.getChannel().size() - smaller.getChannel().size();
		 return counter;
	 }
	 private void gatherLevels(){
		 File[] files = new File("C:\\Users\\bunu\\Desktop\\levels").listFiles();
		 for (File file : files) {
			    if (file.isFile()) {
			        labels.add(file.getName().toString());
			    }
			}
	 }
	 
	 public void update(){
		 System.out.println("levelCounter: " + levelCounter);
		 System.out.println("label size: "+ labels.size());
		 if(levelCounter<=(labels.size()-1)){
			 int difference=0;
			 try {
				level = new FileInputStream("C:\\Users\\bunu\\Desktop\\levels\\"+labels.get(levelCounter));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 
			 try {
				output = new FileInputStream("C:\\Users\\bunu\\Desktop\\output.txt");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				difference = difference(level,output);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			 if(difference == 0){
				 matched.setText("Matched");
				 diffchar.setText("Amount of different characters: 0");
					 levelCounter++;
					 currentLevel.setText("Current Level: " + (levelCounter+1));
					 matched.setText("Not Matched");
					 if(levelCounter < labels.size()){
						 try {
							level = new FileInputStream("C:\\Users\\bunu\\Desktop\\levels\\"+labels.get(levelCounter));
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 }
					 else{
						 System.out.println("else");
						 frame.getContentPane().removeAll();
						 JOptionPane.showMessageDialog(frame, "You have completed all the levels that were provided.");
						 System.exit(0);
					 }
					 try {
						diffchar.setText("Amount of different characters: " + difference(level,output));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 currentLevel.setText("Current Level: "+(levelCounter+1));
				 
			 }
		
			 else{
				 try {
					diffchar.setText("Amount of different characters: "+difference(level,output));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 
		 }
		 else{
			 System.out.println("else");
			 frame.getContentPane().removeAll();
			 JOptionPane.showMessageDialog(frame, "You have completed all the levels that were provided.");
		 }
	 }
}
