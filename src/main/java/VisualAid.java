import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.*;
public class VisualAid {
	FileInputStream level;
	FileInputStream output;
	FileInputStream correctAns;
	BufferedReader reader = null;
	private int levelCounter = 0;
	private ArrayList<String> labels = new ArrayList<String>();
	private ArrayList<String> answers = new ArrayList<String>();
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JLabel matched = new JLabel("NOT MATCHED");
	JLabel diffchar = new JLabel("Amount of different characters: ");
	JLabel currentLevel = new JLabel("Current Level: " + (levelCounter + 1));
	JLabel lastLevel = new JLabel();
	JLabel answer= new JLabel();
	//Sorry about the ugly try-catches. I had no choice
	
	//This is the constructor that sets everyting up
	 public VisualAid() {
		 gatherLevels();
		 lastLevel.setText("Last Level: " + labels.size());
		 
		 //grabs the text file that is holding the brainfuck code that is current level
		 try {
			level= new FileInputStream("levels\\"+labels.get(levelCounter));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		//grabs whatever file the user is inputting their answer to
		try {
			output = new FileInputStream("output.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//this grabs the answer to the level the user is on. The answer is the output of the compiled code
		try {
			correctAns = new FileInputStream("answers\\"+answers.get(levelCounter));
			reader = new BufferedReader(new InputStreamReader(correctAns));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//This sets the answer JLabel to what the output should be
		try {
			answer.setText("Correct Output: "+reader.readLine());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//This sets the currentLevel JLabel to what the current level is
		 currentLevel = new JLabel("Current Level: "+ (levelCounter+1));
		 
		 //This finds the difference between the initial output file (usually blank)
		 //and the first level
		 int diff = 0;
		 try{
			 diff = difference(level,output);
		 } catch(IOException e){}
		 diffchar = new JLabel("Amount of different character: " + diff);
		 
		 //sets up the JFrame and Panel and adds everything to each other
		 panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		 panel.add(matched);
	     panel.add(diffchar);
	     panel.add(currentLevel);
	     panel.add(answer);
	     panel.add(lastLevel);
	     frame.add(panel);
	     frame.pack();
	     frame.setVisible(true);
	 }
	 //Finds the number of characters that are different between two files
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
		 
		 //if one file is bigger than the other, the difference between the file sizes in bytes 
		 //is going to be the minimum difference of characters
		 counter+=bigger.getChannel().size() - smaller.getChannel().size();
		 return counter;
	 }
	 
	 //iterates through the levels folder and the answers subfolder and adds the names of each txt file
	 //to an arraylist for easy access
	 private void gatherLevels(){
		 File[] files = new File("levels").listFiles();
		 for (File file : files) {
			    if (file.isFile()) {
			        labels.add(file.getName().toString());
			        System.out.println(file.getName().toString());
			    }
			}
		 File[] answer = new File("levels\\answers").listFiles();
		 for (File a : answer) {
			    if (a.isFile()) {
			        answers.add(a.getName().toString());
			        System.out.println(a.getName().toString());
			    }
			}
	 }
	 
	 //updates the visual aid to reflect the current status
	 public void update(){
		 System.out.println("levelCounter: " + levelCounter);
		 System.out.println("label size: "+ labels.size());
		 if(levelCounter<=(labels.size()-1)){
			 int difference=0;
			 try {
				level = new FileInputStream("levels\\"+labels.get(levelCounter));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 
			 try {
				output = new FileInputStream("output.txt");
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
			 //if the difference is zero, the levelCounter gets incremented and each JLabel is updated to
			 //reflect the new level
			 if(difference == 0){
				 matched.setText("Matched");
				 diffchar.setText("Amount of different characters: 0");
					 levelCounter++;
					 currentLevel.setText("Current Level: " + (levelCounter+1));
					 matched.setText("Not Matched");
					 if(levelCounter < labels.size()){
						 try {
							level = new FileInputStream("levels\\"+labels.get(levelCounter));
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							correctAns = new FileInputStream("levels\\answers\\"+answers.get(levelCounter));
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						reader = new BufferedReader(new InputStreamReader(correctAns));
						try {
							answer.setText("Correct Output: "+reader.readLine());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 }
					 else{
						 //One of these JOptionPanes is the correct one
						 //this just informs the user that they completed all the available levels and the game is over
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
