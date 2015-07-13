package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringBufferInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.javatuples.Triplet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Challenge {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		Triplet<String,String[],String> result = getQuestion("ViPR","1","2");
		
		System.out.println(result.getValue0() + " " + result.getValue1() + " " + result.getValue2());
		
		String[] options = result.getValue1();
		for(int i=0;i<options.length;i++){
			System.out.println(options[i]);
		}
		
				//System.out.println(root.getChildNodes().getLength());
	}
	
	
	public static Triplet<String,String[],String> getQuestion(String course, String lesson, String challenge)throws Exception{
		 
		String question = "NA";
		String answer = "NA";
		String[] resultOptions = null;
		
		String xmlFile = readTextFile("C:\\Mumshad Files\\Products\\Play\\Project2\\repository\\questions.xml");
		
		DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document doc = builder.parse(new StringBufferInputStream(xmlFile));
		Element root = doc.getDocumentElement();
		//String ChallengeID = "course_" + course ; // + "_lesson_" + lesson+"_challenge_" + challenge;
		//Element challengeElement = doc.getElementById(ChallengeID);
		
		NodeList courses = doc.getElementsByTagName("course");
		Element courseElement;
		for (int i = 0; i < courses.getLength(); i++) {
			Node courseNode = courses.item(i);
			String courseName = courseNode.getAttributes().getNamedItem("ID").getNodeValue();
			System.out.println("Course = " + courseName);
			if(courseName.equalsIgnoreCase(course)){
				
				if (courseNode.getNodeType() == Node.ELEMENT_NODE) {
					       Element courseelem = (Element) courseNode;
					       NodeList lessons = courseelem.getElementsByTagName("lesson");
					       for (int j = 0; j < lessons.getLength(); j++) {
								Node lessonNode = lessons.item(j);
								String lessonNumber = lessonNode.getAttributes().getNamedItem("ID").getNodeValue();
								System.out.println("Lesson = " + lessonNumber);
								
								if(lessonNumber.equalsIgnoreCase(lesson)){
									if (lessonNode.getNodeType() == Node.ELEMENT_NODE) {
										Element lessonelem = (Element) lessonNode;
										NodeList challenges = lessonelem.getElementsByTagName("challenge");
										for (int k = 0; k < challenges.getLength(); k++) {
											Node challengeNode =challenges.item(k);
											String challengeNumber = challengeNode.getAttributes().getNamedItem("ID").getNodeValue();
											System.out.println("Challenge Number = " + challengeNumber);
											if(challengeNumber.equalsIgnoreCase(challenge)){
												System.out.println("------Identified Challenge---------");
												if (challengeNode.getNodeType() == Node.ELEMENT_NODE) {
													Element challengeelem = (Element) challengeNode;
													
													question=challengeelem.getElementsByTagName("question").item(0).getTextContent();
													
													NodeList answers = challengeelem.getElementsByTagName("answer");
													answer = answers.item(0).getTextContent();
													
													String question_type = challengeelem.getElementsByTagName("question").item(0).getAttributes().getNamedItem("type").getNodeValue();
													if(question_type.equalsIgnoreCase("radio")){
														System.out.println("Question Type is Radio");
														NodeList options = challengeelem.getElementsByTagName("option");	
														resultOptions = new String[options.getLength()];
														for (int l = 0; l < options.getLength(); l++) {
															Node optionElement = options.item(l);
															resultOptions[l] = optionElement.getTextContent();
														}
													}else if(question_type.equalsIgnoreCase("text")){
														System.out.println("Question Type is Text");
														resultOptions = null;
													}
													break;
													
												}
											}
											
										}
									}
									break;
								}
								
					       }
				}	
				
				break;
			}
		}
				
		
		Triplet<String,String[],String> triplet = new Triplet<String,String[],String>(question, resultOptions, answer);
		
		return triplet;
		
	}
	
	public static String readTextFile(String filepath) {
        String contents="";
        String fileName = filepath;
        FileReader file = null;
  
        try {
          file = new FileReader(fileName);
          BufferedReader reader = new BufferedReader(file);
          String line = "";
          while ((line = reader.readLine()) != null) {
            contents += line + "\n";
          }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
          if (file != null) {
            try {
              file.close();
            } catch (IOException e) {
              // Ignore issues during closing 
            }
          }
        }
        return contents;
	} 

}
