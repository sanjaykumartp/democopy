package com.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.assessment.ExcelWriter;
import com.assessment.FilesSearchController;
import com.assessment.SearchResult;

public class FilesSearch extends JFrame {
	private JLabel directoryLabel;
    private JLabel searchLabel;
    private JTextField directoryField;
    private JTextField searchField;
    private JButton searchButton;
    
    public FilesSearch() {
        // Set up UI components
        directoryLabel = new JLabel("Directory:");
        searchLabel = new JLabel("Search String:");
        directoryField = new JTextField(20);
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        
        // Set up main panel
        JPanel mainPanel = new JPanel(new GridLayout(3, 2));
        mainPanel.add(directoryLabel);
        mainPanel.add(directoryField);
        mainPanel.add(searchLabel);
        mainPanel.add(searchField);
        mainPanel.add(new JLabel()); // empty space
        mainPanel.add(searchButton);
        
        // Set up frame
        setTitle("File Search");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(mainPanel);
        
        // Add action listener to search button
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String directoryPath = directoryField.getText();
                String searchString = searchField.getText();
                try {
                    ExcelWriter excelWriter = new ExcelWriter();
                    FilesSearchController.searchDirectory(new File(directoryPath), searchString, excelWriter);
                    excelWriter.save();
                    JOptionPane.showMessageDialog(FilesSearch.this, "Search complete!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(FilesSearch.this, "Error: " + ex.getMessage());
                }
            }
        });
    }
    
    public static void main(String[] args) {
    	FilesSearch ui = new FilesSearch();
        ui.setVisible(true);
    }
}


//	private JFrame frame;
//	private JPanel panel;
//	private JTextField directoryPathTextField;
//	private JTextField searchStringTextField;
//	private JTextArea resultsTextArea;
//
//	public FilesSearch() {
//		frame = new JFrame("Search Tab");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(600, 400);
//
//		panel = new JPanel();
//		panel.setLayout(null);
//
//		JLabel directoryPathLabel = new JLabel("Directory Path:");
//		directoryPathLabel.setBounds(20, 20, 100, 20);
//		panel.add(directoryPathLabel);
//
//		directoryPathTextField = new JTextField();
//		directoryPathTextField.setBounds(130, 20, 400, 20);
//		panel.add(directoryPathTextField);
//
//		JLabel searchStringLabel = new JLabel("Search String:");
//		searchStringLabel.setBounds(20, 50, 100, 20);
//		panel.add(searchStringLabel);
//
//		searchStringTextField = new JTextField();
//		searchStringTextField.setBounds(130, 50, 400, 20);
//		panel.add(searchStringTextField);
//
//		JButton searchButton = new JButton("Search");
//		searchButton.setBounds(20, 80, 100, 20);
//		panel.add(searchButton);
//
//		resultsTextArea = new JTextArea();
//		JScrollPane scrollPane = new JScrollPane(resultsTextArea);
//		scrollPane.setBounds(20, 110, 1000, 500);
//		panel.add(scrollPane);
//
//		frame.add(panel);
//		frame.setVisible(true);
//
//		searchButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String directoryPath = directoryPathTextField.getText();
//				String searchString = searchStringTextField.getText();
//				String[] searchStrings = { searchString };
//				try {
//					searchDirectory(directoryPath, searchStrings);
//					resultsTextArea.setText("Results saved in Excel file.");
//				} catch (IOException ex) {
//					ex.printStackTrace();
//				}
//			}
//		});
//	}
//
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					new FilesSearch();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	public static void searchDirectory(String directoryPath, String[] searchStrings) throws IOException {
//		if (searchStrings.length == 0) {
//			System.out.println("Please enter at least one search string.");
//			return;
//		}
//
//		File directory = new File(directoryPath);
//		if (!directory.exists() || !directory.isDirectory()) {
//			System.out.println("Invalid directory path: " + directoryPath);
//			return;
//		}
//
//		File[] files = directory.listFiles(new FilenameFilter() {
//			public boolean accept(File directory, String name) {
//				String lowerCaseName = name.toLowerCase();
//				return lowerCaseName.endsWith(".txt") || lowerCaseName.endsWith(".doc") || lowerCaseName.endsWith(".docx") || lowerCaseName.endsWith(".pdf");
//			}
//		});
//
//		if (files.length == 0) {
//			System.out.println("No text, doc, docx or pdf files found in " + directoryPath);
//			return;
//		}
//
//		List<File> matchingFiles = new ArrayList<File>();
//		for (File file : files) {
//	String fileExtension=FilenameUtils.getExtension(file.getName());
//	if(fileExtension.equalsIgnoreCase("pdf")) {
//		
//		
//	}
//	System.out.println(  fileExtension );
//			boolean match = false;
//			for (String searchString : searchStrings) {
//				if (searchFile(file, searchString.trim())) {
//				}
//			}
//			if (match) {
//				matchingFiles.add(file);
//			}
//		}
//
//		if (matchingFiles.isEmpty()) {
//			System.out.println("No files matching the search strings found in " + directoryPath);
//			return;
//		}
//
//		for (String searchString : searchStrings) {
//			File subDir = new File(directoryPath + File.separator + searchString);
//			if (!subDir.exists()) {
//				subDir.mkdirs();
//			}
//			List<String[]> dataRows = new ArrayList<String[]>();
//			for (File file : matchingFiles) {
//				if (searchFile(file, searchString.trim())) {
//					Path sourcePath = Paths.get(file.getAbsolutePath());
//					Path targetPath = Paths.get(subDir.getAbsolutePath() + File.separator + file.getName());
//					Files.copy(sourcePath, targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
//					System.out.println(file.getName() + " copied from the source path " + directoryPath + " to the target path " + subDir);
//					String[] dataRow = extractDataFromResume(file, searchString.trim());
//					if (dataRow != null) {
//						dataRows.add(dataRow);
//					}
//				}
//			}
//			if (!dataRows.isEmpty()) {
//				generateExcelSheet(subDir.getAbsolutePath() + File.separator + searchString + ".xlsx", dataRows);
//			}
//		}
//	}
//	
//	
//	private static boolean searchFile(File file, String searchString) throws IOException {
//	    String fileExtension = getFileExtension(file.getName());
//	    switch (fileExtension) {
//	        case "txt":
//	            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//	                String line;
//	                while ((line = br.readLine()) != null) {
//	                    if (line.toLowerCase().contains(searchString.toLowerCase())) {
//	                        return true;
//	                    }
//	                }
//	            }
//	            break;
//	        case "doc":
//	        case "docx":
//	            try (FileInputStream fis = new FileInputStream(file);
//	                 XWPFDocument document = new XWPFDocument(fis)) {
//	                XWPFWordExtractor extractor = new XWPFWordExtractor(document);
//	                String text = extractor.getText();
//	                if (text.toLowerCase().contains(searchString.toLowerCase())) {
//	                    return true;
//	                }
//	            }
//	            break;
//	        case "pdf":
//	            try (PDDocument document = PDDocument.load(file)) {
//	                PDFTextStripper stripper = new PDFTextStripper();
//	                String text = stripper.getText(document);
//	                if (text.toLowerCase().contains(searchString.toLowerCase())) {
//	                    return true;
//	                }
//	            }
//	            break;
//	        default:
//	            System.out.println("Unsupported file type: " + fileExtension);
//	            break;
//	    }
//	    return false;
//	}
//	
//	
//	private static String[] extractDataFromResume(File file, String searchString) {
//	    String fileExtension = getFileExtension(file.getName());
//	    String[] dataRow = new String[5];
//	    switch (fileExtension) {
//	        case "txt":
//	            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//	                String line;
//	                while ((line = br.readLine()) != null) {
//	                    if (line.toLowerCase().contains(searchString.toLowerCase())) {
//	                        dataRow[0] = extractName(line);
//	                        dataRow[1] = extractEmail(line);
//	                        dataRow[2] = extractPhoneNumber(line);
//	                        dataRow[3] = extractSkills(line);
//	                        dataRow[4] = file.getName();
//	                        return dataRow;
//	                    }
//	                }
//	            } catch (IOException e) {
//	                System.out.println("Error reading file: " + file.getName());
//	            }
//	            break;
//	        case "doc":
//	        case "docx":
//	            try (FileInputStream fis = new FileInputStream(file);
//	                 XWPFDocument document = new XWPFDocument(fis)) {
//	                XWPFWordExtractor extractor = new XWPFWordExtractor(document);
//	                String[] lines = extractor.getText().split("\\r?\\n");
//	                for (String line : lines) {
//	                    if (line.toLowerCase().contains(searchString.toLowerCase())) {
//	                        dataRow[0] = extractName(line);
//	                        dataRow[1] = extractEmail(line);
//	                        dataRow[2] = extractPhoneNumber(line);
//	                        dataRow[3] = extractSkills(line);
//	                        dataRow[4] = file.getName();
//	                        return dataRow;
//	                    }
//	                }
//	            } catch (IOException e) {
//	                System.out.println("Error reading file: " + file.getName());
//	            }
//	            break;
//	        case "pdf":
//	            try (PDDocument document = PDDocument.load(file)) {
//	                PDFTextStripper stripper = new PDFTextStripper();
//	                String[] lines = stripper.getText(document).split("\\r?\\n");
//	                for (String line : lines) {
//	                    if (line.toLowerCase().contains(searchString.toLowerCase())) {
//	                        dataRow[0] = extractName(line);
//	                        dataRow[1] = extractEmail(line);
//	                        dataRow[2] = extractPhoneNumber(line);
//	                        dataRow[3] = extractSkills(line);
//	                        dataRow[4] = file.getName();
//	                        return dataRow;
//	                    }
//	                }
//	            } catch (IOException e) {
//	                System.out.println("Error reading file: " + file.getName());
//	            }
//	            break;
//	        default:
//	            System.out.println("Unsupported file type: " + fileExtension);
//	            break;
//	    }
//	    return null;
//	}
//	
//	private static String extractName(String line) {
//	    String[] words = line.trim().split("\\s+");
//	    String name = "";
//	    for (String word : words) {
//	        if (Character.isUpperCase(word.charAt(0))) {
//	            name += word + " ";
//	        }
//	    }
//	    return name.trim();
//	}
//
//	private static String extractEmail(String line) {
//	    Pattern pattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
//	    Matcher matcher = pattern.matcher(line);
//	    if (matcher.find()) {
//	        return matcher.group();
//	    }
//	    return "";
//	}
//
//	private static String extractPhoneNumber(String line) {
//	    Pattern pattern = Pattern.compile("\\b\\d{10}\\b");
//	    Matcher matcher = pattern.matcher(line);
//	    if (matcher.find()) {
//	        return matcher.group();
//	    }
//	    return "";
//	}
//
//	private static String extractSkills(String line) {
//	    String[] skills = {"Java", "Python", "C++", "HTML", "CSS", "JavaScript"};
//	    String extractedSkills = "";
//	    for (String skill : skills) {
//	        if (line.toLowerCase().contains(skill.toLowerCase())) {
//	            extractedSkills += skill + ", ";
//	        }
//	    }
//	    return extractedSkills.trim().replaceAll(",$", "");
//	}
//
//	private static void generateExcelSheet(String filePath, List<String[]> dataRows) {
//	    try (Workbook workbook = new XSSFWorkbook()) {
//	        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet();
//	        int rowNum = 0;
//	        for (String[] dataRow : dataRows) {
//	            Row row = sheet.createRow(rowNum++);
//	            int cellNum = 0;
//	            for (String data : dataRow) {
//	                org.apache.poi.ss.usermodel.Cell cell = row.createCell(cellNum++);
//	                cell.setCellValue(data);
//	            }
//	        }
//	        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
//	            workbook.write(outputStream);
//	        } catch (IOException e) {
//	            System.out.println("Error writing to file: " + filePath);
//	        }
//	    } catch (IOException e) {
//	        System.out.println("Error creating workbook.");
//	    }
//	}
//	
//	private static String getFileExtension(String fileName) {
//	    int dotIndex = fileName.lastIndexOf(".");
//	    if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
//	        return fileName.substring(dotIndex + 1);
//	    } else {
//	        return "";
//	    }
//	}
//	
//}