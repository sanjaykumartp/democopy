package com.assessment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class FilesSearchController {
	
	public static void searchDirectory(File directory, String searchString, ExcelWriter excelWriter) throws IOException {
	    if (!directory.isDirectory()) {
	        throw new IllegalArgumentException(directory.getPath() + " is not a directory");
	    }

	    File[] files = directory.listFiles();
	    if (files == null) {
	        return;
	    }

	    for (File file : files) {
	        if (file.isDirectory()) {
	            searchDirectory(file, searchString, excelWriter);
	        } else {
	            String fileExtension = FilenameUtils.getExtension(file.getName());
	            if (fileExtension.equalsIgnoreCase("txt")
	                    || fileExtension.equalsIgnoreCase("doc")
	                    || fileExtension.equalsIgnoreCase("docx")
	                    || fileExtension.equalsIgnoreCase("pdf")) {
	                searchFile(file, searchString, excelWriter);
	            }
	        }
	    }
	}
	
	
	
	private static void searchFile(File file, String searchString, ExcelWriter excelWriter) throws IOException {
	    String fileExtension = FilenameUtils.getExtension(file.getName());
	    switch (fileExtension) {
	    case "txt":
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.toLowerCase().contains(searchString.toLowerCase())) {
                        SearchResult result = new SearchResult(file.getName(), "", "", "");

                        // Extract name
                        String[] words = line.split("\\s+");
                        StringBuilder nameBuilder = new StringBuilder();
                        for (String word : words) {
                            if (Character.isUpperCase(word.charAt(0))) {
                                nameBuilder.append(word).append(" ");
                            }
                        }
                        String name = nameBuilder.toString().trim();
                        result.setName(name);

                        // Extract email
                        Pattern emailPattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
                        Matcher emailMatcher = emailPattern.matcher(line);
                        if (emailMatcher.find()) {
                            String email = emailMatcher.group();
                            result.setEmail(email);
                        }

                        // Extract mobile number
                        Pattern mobilePattern = Pattern.compile("\\d{10}");
                        Matcher mobileMatcher = mobilePattern.matcher(line);
                        if (mobileMatcher.find()) {
                            String mobileNumber = mobileMatcher.group();
                            result.setMobileNumber(mobileNumber);
                        }

                        result.setFileName(file.getName());
                        excelWriter.addResult(result);
                    }
                }
            }
            break;
	    case "doc":
        case "docx":
            try (FileInputStream fis = new FileInputStream(file);
                 XWPFDocument document = new XWPFDocument(fis)) {
                XWPFWordExtractor extractor = new XWPFWordExtractor(document);
                String text = extractor.getText();
                if (text.toLowerCase().contains(searchString.toLowerCase())) {
                    SearchResult result = new SearchResult(file.getName(), "", "", "");
                    result.setFileName(file.getName());
                    extractNameEmailMobile(text, result);
                    excelWriter.addResult(result);
                }
            }
            break;
        case "pdf":
            try (PDDocument document = PDDocument.load(file)) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                if (text.toLowerCase().contains(searchString.toLowerCase())) {
                    SearchResult result = new SearchResult(file.getName(), "", "", "");
                    result.setFileName(file.getName());
                    extractNameEmailMobile(text, result);
                    excelWriter.addResult(result);
                }
            }
            break;
        default:
            System.out.println("Unsupported file type: " + fileExtension);
            break;
    }
	}
	
	private static void extractNameEmailMobile(String text, SearchResult result) {
	    String[] lines = text.split("\\r?\\n");
	    for (String line : lines) {
	        if (line.toLowerCase().contains("name")) {
	            String[] nameParts = line.split(":");
	            if (nameParts.length > 1) {
	                result.setName(nameParts[1].trim());
	            }
	        } else if (line.toLowerCase().contains("email")) {
	            String[] emailParts = line.split(":");
	            if (emailParts.length > 1) {
	                String email = emailParts[1].trim();
	                if (email.contains("@")) {
	                    result.setEmail(email);
	                }
	            }
	        } else if (line.toLowerCase().contains("mobile") || line.toLowerCase().contains("phone")) {
	            String[] phoneParts = line.split(":");
	            if (phoneParts.length > 1) {
	                String phone = phoneParts[1].trim();
	                phone = phone.replaceAll("[^\\d]", "");
	                if (phone.length() == 10) {
	                    result.setMobileNumber(phone);
	                }
	            }
	        }
	    }
	}
	
	
}